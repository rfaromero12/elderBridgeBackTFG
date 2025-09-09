package es.uco.tfg.elderBridge.infrastructure.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

	@Value("${aws.secret.key}")
	private String secretKey;
	@Value("${aws.access.key}")
	private String accessKey;
	@Value("${aws.region}")
	private String region;
	
	@Bean
		S3Client getS3Client() {
		AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
		return S3Client.builder()
				.region(Region.of(region))
				.endpointOverride(URI.create("https://s3.eu-west-2.amazonaws.com"))
				.credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
				.build();
		
	}
}
