package com.learn.chatai.domain.policy.port;

import java.io.IOException;
import java.io.InputStream;

/**
 * Port implemented by {@code infra.file}. Kept independent of any web/servlet type
 * (no MultipartFile here) so the storage backend can be swapped (local disk now, S3 later)
 * without touching domain code.
 */
public interface FileStoragePort {

    StoredFile store(String suggestedFileName, InputStream content) throws IOException;

    InputStream read(String storageKey) throws IOException;

    record StoredFile(String storageKey, long sizeBytes, String checksum) {
    }
}
