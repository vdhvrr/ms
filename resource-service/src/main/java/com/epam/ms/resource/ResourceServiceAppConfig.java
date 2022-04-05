package com.epam.ms.resource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceServiceAppConfig {

  @Value("${s3.access-key-id}")
  private String accessKeyId;

  @Value("${s3.key-secret}")
  private String accessKeySecret;

  @Value("${s3.service-endpoint}")
  private String serviceEndpoint;

  @Bean
  public AmazonS3 s3Client() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, accessKeySecret)))
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, ""))
        .enablePathStyleAccess()
        .disableChunkedEncoding()
        .build();
  }
}
