package com.learn.chatai.infra.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    /**
     * Owns the actual HTTP connection pool, so it (not {@link ElasticsearchClient}, which has
     * no close method of its own) is what needs an explicit destroy method on shutdown.
     */
    @Bean(destroyMethod = "close")
    public RestClient elasticsearchRestClient(@Value("${app.vectordb.elasticsearch.url}") String url) {
        return RestClient.builder(HttpHost.create(url)).build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient elasticsearchRestClient) {
        ElasticsearchTransport transport = new RestClientTransport(elasticsearchRestClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
