package com.learn.chatai.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Dedicated executor for job-backed async work (PDF extraction, vectorization, chat completion, ...).
 * Kept separate from Spring MVC's request threads so long-running jobs never starve API responsiveness.
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "jobTaskExecutor")
    public Executor jobTaskExecutor(
            @Value("${app.async.job-executor.core-pool-size:2}") int corePoolSize,
            @Value("${app.async.job-executor.max-pool-size:4}") int maxPoolSize,
            @Value("${app.async.job-executor.queue-capacity:50}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("job-exec-");
        executor.initialize();
        return executor;
    }
}
