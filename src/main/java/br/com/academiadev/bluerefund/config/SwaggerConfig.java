package br.com.academiadev.bluerefund.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apis() {
		return new Docket(DocumentationType.SWAGGER_2).select()//
				.apis(RequestHandlerSelectors.basePackage("br.com.academiadev.bluerefund"))//
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		String titulo = "BlueRefund - Bee Squad";
		String descricao = "Sistema para controle de reembolso.";
		return new ApiInfo(titulo, descricao, "beta 1.0", "termos", null, "reembolsoazul.netlify.com", "reembolsoazul.netlify.com", new ArrayList<>());
	}

}
