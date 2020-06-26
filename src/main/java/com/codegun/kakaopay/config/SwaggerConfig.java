package com.codegun.kakaopay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.codegun.kakaopay.interfaces.controller"))
        .paths(PathSelectors.any())
        .build()
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET,
            Arrays.asList(new ResponseMessageBuilder()
                    .code(500)
                    .message("Internal Server Error")
                    .responseModel(new ModelRef("string"))
                    .build(),
                new ResponseMessageBuilder()
                    .code(400)
                    .message("Bad Request")
                    .build(),
                new ResponseMessageBuilder()
                    .code(404)
                    .message("Not Found")
                    .build()));
  }


  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("kakao seeding test")
        .description("카카오페이 뿌리기 테스트")
        .build();

  }

}
