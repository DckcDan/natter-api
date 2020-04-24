package com.example.natterapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Enables Swagger.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
	
	private final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
	private static final String API_VERSION = "1.0";
	private static final String API_LICENSE = "License";
	private static final String API_TITLE = "The Natter API";
	private static final String API_DESCRIPTION = "The REST secured Natter API";
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(API_TITLE)
			.description(API_DESCRIPTION)
			.license(API_LICENSE)
			.version(API_VERSION)
			.build();
	}
	
	/**
	 * Create configuration for swagger.
	 * @return configured {@link Docket} object
	 */
	@Bean
	public Docket productApi() {
		logger.warn("Swagger UI enabled under /swagger-ui.html. Please don't do this in higher environments.");
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.example.natterapi"))
			.paths(PathSelectors.regex("/.*"))
			.build()
			.apiInfo(apiInfo())
			.pathMapping("/");
	}
}
