package com.carhub.api.auth.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.api.version}")
    private String apiVersion;
    final static String AUTHORIZATION_HEADER = "Authorization";
    final static String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.carhub.api.auth.controllers"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .apiInfo(getInfos());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo getInfos() {
        return new ApiInfoBuilder().
                title("Mima API").
                description(swaggerAPIDescription()).
                version(apiVersion).build();
    }

    private String swaggerAPIDescription() {
        return "Description";
    }


    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
    public ApiListingBuilderPlugin getApiPathEnrichPlugin() {
        return new ApiListingBuilderPlugin() {
            @Override
            public boolean supports(DocumentationType delimiter) {
                return true;
            }

            @Override
            public void apply(ApiListingContext apiListingContext) {
                List<ApiDescription> apis = apiListingContext.apiListingBuilder().build().getApis();
                ImmutableList.Builder<ApiDescription> builder = ImmutableList.builder();
                if (apis != null) {
                    apis.forEach(api -> builder.add(new ApiDescription(api.getGroupName().get(),
                            api.getPath() + "?apiDescription=" + api.getDescription(),
                            api.getDescription(), api.getOperations(),
                            api.isHidden())));
                    apis = builder.build();
                    apiListingContext.apiListingBuilder().apis(apis);
                }
            }
        };
    }
}
