package com.ezdi.audioplayer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.ezdi.cassandra.impl.CassandraService;

@Configuration
@EnableCassandraRepositories(basePackages = { "com.ezdi.audioplayer.daoimpl" })
public class CassandraUtil {

	@Value("${cassandra.document.keyspace}")
	private String documentKeyspace;
	
	@Value("${cassandra.contactpoints}")
	private String contactPoints;

	@Value("${cassandra.port}")
	private int port;

	@Value("${cassandra.username}")
	private String username;

	@Value("${cassandra.password}")
	private String password;

	public String getContactPoints() {
		return contactPoints;
	}

	public void setContactPoints(String contactPoints) {
		this.contactPoints = contactPoints;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Bean(name = "cassandraService")
	public CassandraOperations cassandraService() throws Exception {
		CassandraOperations cassandraOperations = CassandraService
				.cassandraTemplate(documentKeyspace, getContactPoints(),
						getPort(), getUsername(), getPassword());
		return cassandraOperations;
	}
}