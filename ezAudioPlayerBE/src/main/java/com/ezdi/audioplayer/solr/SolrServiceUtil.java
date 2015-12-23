package com.ezdi.audioplayer.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan
@EnableSolrRepositories("com.mscharhag.solr.repository")
// @PropertySource(value ={"classpath:solr.properties"} )
@Component
public class SolrServiceUtil {

	@Value("${solr.server.url}")
	private String solrURL;

	@Value("${solr.service}")
	private String solrService;

	@Value("${solr.handler}")
	private String solrHandler;

	@Bean
	public SolrServer solrServer() {
		return new HttpSolrServer(solrURL + "/" + solrService);
	}

	@Bean(name = "SolrTemplate")
	public SolrTemplate solrTemplate(SolrServer server) throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrServer());
		System.out.println("solr server created :- " + solrTemplate);
		return solrTemplate;

	}
}
