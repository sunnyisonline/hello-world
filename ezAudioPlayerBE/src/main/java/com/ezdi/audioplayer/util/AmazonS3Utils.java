package com.ezdi.audioplayer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ezdi.s3connection.service.AmazonS3Service;
import com.ezdi.s3connection.service.impl.AmazonS3ServiceImpl;

@Configuration
public class AmazonS3Utils {
	
	@Value("${access.key.id}")
	private String accessKeyId;

	@Value("${secret.access.key}")
	private String secretAccessKey;

	@Value("${region}")
	private String region;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Bean
	public AmazonS3Service amazonS3Service(){
		return AmazonS3ServiceImpl.getInstance(true, accessKeyId,
				secretAccessKey, region);			
	}
		
}