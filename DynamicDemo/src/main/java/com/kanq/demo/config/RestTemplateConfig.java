package com.kanq.demo.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author yyc
 * @date 2020/7/17 18:05
 */
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    HttpClient httpClient =
        HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    factory.setHttpClient(httpClient);
    restTemplate.setRequestFactory(factory);
    return restTemplate;
  }
}
