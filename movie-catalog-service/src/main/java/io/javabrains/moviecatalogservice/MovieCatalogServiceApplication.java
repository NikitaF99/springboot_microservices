package io.javabrains.moviecatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
public class MovieCatalogServiceApplication {


	@Bean //map to this 1 instance of restTemplte. This gets executed only once
	@LoadBalanced //does service discovery. That is it tells that the URL that is being given is a hint for the service to be discovered //so restTemplates looks for those hints when we give the URL that is we simply say go check with eureka
	public RestTemplate getRestTemplate() {
		//Adding timeout to solve slow microservice issue
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(3000);
		return new RestTemplate(clientHttpRequestFactory);
	}

	/**
	 * WEB CLIENT : A NEW WAY OF MAKING API CALLS
	 */
	//reactive springboot
	//gives more asynchronous mechanism
	//W e create abuilder, and from that builder every time we make a call we create a new client, put the parameters needed
	//and GO

//	@Bean
//	public WebClient.Builder getWebClientBuilder(){
//		return WebClient.builder();
//	}

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogServiceApplication.class, args);
	}

}
