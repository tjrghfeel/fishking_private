package com.tobe.fishking.v2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;

import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;





@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String BASE_PACKAGE = "com.tobe.fishking.v2";
    private static final String BASE_VER = "2.0";

    @Bean
    public Docket swaggerApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder.name("Authorization") //헤더 이름
                .description("Access Tocken") //설명
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(aParameters)
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                //.paths(PathSelectors.any())
                .paths(PathSelectors.ant("/v2/api/**"))
                .build()
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false); // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("어복황제 v2 API")
                .description("웹 개발시 사용되는 서버 API에 대한 연동 문서입니다")
                .license("kaimobile")
                .licenseUrl("http://kaimobile.com")
                .version(BASE_VER)
                .build() ;




    }



    private Set<String> getContentType() {
        final HashSet<String> mediaType = new HashSet<>();
        mediaType.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return mediaType;
    }


    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("어복황제 v2 API 만들기")
                .description("API Docs")
                .version("1.0")
                .build();
    }

}

/*@EnableSwagger2
@Configuration
public class SwaggerConfig {
    private static final String BASE_PACKAGE = "com.tobe.fishking.v2";
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                //.paths(any())
                .paths(PathSelectors.ant("/v2/api/**"))
                .build()
    *//*            .consumes(getContentType())
                .produces(getContentType())*//*
                .apiInfo(getApiInfo());
    }



    }
    */


/*
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;


    @Bean
    public Docket apiV2() {
        version = "V2";
        title = "fishking V2 API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tobe.fishking.v2"))
                .paths(PathSelectors.ant("/v2/api/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }


    @Bean
    public Docket apiV1() {
        version = "V1";
        title = "fishking API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tobe.fishking.v1"))
                .paths(PathSelectors.ant("/v1/api/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }



    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Swagger로 생성한 API Docs",
                version,
                "www.example.com",
                new Contact("Contact Me", "www.example.com", "foo@example.com"),
                "Licenses",

                "www.example.com",

                new ArrayList<>());
    }
}
*/