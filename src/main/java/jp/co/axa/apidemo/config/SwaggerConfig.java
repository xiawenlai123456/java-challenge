package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
/**
 * customize the SwaggerApi
 * 
 * @author natsubunrai
 */
@Configuration
public class SwaggerConfig {
    @Bean
    Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select() //to include the interface into the document Api 
                .apis(RequestHandlerSelectors.basePackage("jp.co.axa.apidemo.controllers"))
                .paths(PathSelectors.any()) 
                .build();
    }

    @SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Employee Information management")
                .description("Information management</br></br><a href=\"/api/v1/logout\">LOGOUT</a>")
                .contact("XIA WENLAI")
                .version("1.0")
                .build();
    }
}
