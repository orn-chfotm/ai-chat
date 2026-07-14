package com.learn.chatai.infra.file;

import com.learn.chatai.domain.policy.port.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

/**
 * Local-disk implementation of {@link FileStoragePort}. Swap this for an S3 adapter later
 * without touching domain code — callers only see the port.
 */
@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    private final Path baseDir;

    public LocalFileStorageAdapter(@Value("${app.file-storage.base-dir}") String baseDir) throws IOException {
        this.baseDir = Path.of(baseDir).toAbsolutePath().normalize();
        Files.createDirectories(this.baseDir);
    }

    @Override
    public StoredFile store(String suggestedFileName, InputStream content) throws IOException {
        String storageKey = UUID.randomUUID() + "-" + sanitize(suggestedFileName);
        Path target = baseDir.resolve(storageKey);

        MessageDigest digest = sha256();
        try (DigestInputStream digestStream = new DigestInputStream(content, digest)) {
            Files.copy(digestStream, target, StandardCopyOption.REPLACE_EXISTING);
        }

        long sizeBytes = Files.size(target);
        String checksum = HexFormat.of().formatHex(digest.digest());
        return new StoredFile(storageKey, sizeBytes, checksum);
    }

    @Override
    public InputStream read(String storageKey) throws IOException {
        return Files.newInputStream(baseDir.resolve(storageKey));
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 알고리즘을 사용할 수 없습니다.", e);
        }
    }

    private static String sanitize(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "upload.pdf";
        }
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
