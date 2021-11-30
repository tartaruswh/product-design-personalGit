package com.bewg.pd.workbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * <p>
 * swagger-ui配置类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Spring Boot 使用 Swagger2 构建RESTFUL API").contact(new Contact("田志涛", "https://swagger.io/tools/swagger-ui/", "tianzhitao@bewg.net.cn")).version("1.0").description("北京北控悦慧环境科技有限公司-计算书服务API").build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.bewg.pd.workbook")).paths(PathSelectors.any()).build();
    }

    /**
     * 接收跨域请求方法类型
     */
    private static final String[] ORIGINS = new String[] {"GET", "POST", "PUT", "DELETE"};

    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
            // .allowCredentials(true)
            .allowedHeaders("*").allowedMethods(ORIGINS).maxAge(3600);
    }
}