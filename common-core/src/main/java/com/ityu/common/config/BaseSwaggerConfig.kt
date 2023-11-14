//package com.ityu.common.config
//
//import com.ityu.common.domain.SwaggerProperties
//import io.swagger.annotations.Api
//import io.swagger.annotations.ApiOperation
//import org.springframework.beans.BeansException
//import org.springframework.beans.factory.config.BeanPostProcessor
//import org.springframework.context.annotation.Bean
//import org.springframework.util.ReflectionUtils
//import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
//
//import java.util.stream.Collectors
//
///**
// * 文档配置
// *
// * @author lihe
// */
//
//abstract class BaseSwaggerConfig {
//    @Bean
//    open fun createRestApi(): Docket {
//        val swaggerProperties: SwaggerProperties = swaggerProperties()
//        val docket = Docket(DocumentationType.SWAGGER_2)
//            .apiInfo(apiInfo(swaggerProperties))
//            .select()
//            .apis(RequestHandlerSelectors.withClassAnnotation(Api::class.java))
//            .paths(PathSelectors.any())
//            .build()
//        if (swaggerProperties.enableSecurity) {
//            docket.securitySchemes(securitySchemes()).securityContexts(securityContexts())
//        }
//        return docket
//    }
//
//    private fun apiInfo(swaggerProperties: SwaggerProperties): ApiInfo {
//        return ApiInfoBuilder()
//            .title(swaggerProperties.title)
//            .description(swaggerProperties.description)
//            .contact(
//                Contact(
//                    swaggerProperties.contactName,
//                    swaggerProperties.contactUrl,
//                    swaggerProperties.contactEmail
//                )
//            )
//            .version(swaggerProperties.version)
//            .build()
//    }
//
//    private fun securitySchemes(): List<SecurityScheme> {
//        //设置请求头信息
//        val result: MutableList<SecurityScheme> = ArrayList()
//        val apiKey = ApiKey("Authorization", "Authorization", "header")
//        result.add(apiKey)
//        return result
//    }
//
//    private fun securityContexts(): List<SecurityContext> {
//        //设置需要登录认证的路径
//        val result: MutableList<SecurityContext> = ArrayList()
//        result.add(getContextByPath("/*/.*"))
//        return result
//    }
//
//    private fun getContextByPath(pathRegex: String): SecurityContext {
//        return SecurityContext.builder()
//            .securityReferences(defaultAuth())
//            .forPaths(PathSelectors.regex(pathRegex))
//            .build()
//    }
//
//    private fun defaultAuth(): List<SecurityReference> {
//        val result: MutableList<SecurityReference> = ArrayList()
//        val authorizationScope = AuthorizationScope("global", "accessEverything")
//        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
//        authorizationScopes[0] = authorizationScope
//        result.add(SecurityReference("Authorization", authorizationScopes))
//        return result
//    }
//
//    fun generateBeanPostProcessor(): BeanPostProcessor {
//        return object : BeanPostProcessor {
//            @Throws(BeansException::class)
//            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
//                if (bean is WebMvcRequestHandlerProvider || bean is WebFluxRequestHandlerProvider) {
//                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean))
//                }
//                return bean
//            }
//
//            private fun <T : RequestMappingInfoHandlerMapping?> customizeSpringfoxHandlerMappings(mappings: MutableList<T>) {
//                val copy = mappings.stream()
//                    .filter { mapping: T -> mapping!!.patternParser == null }
//                    .collect(Collectors.toList())
//                mappings.clear()
//                mappings.addAll(copy)
//            }
//
//            private fun getHandlerMappings(bean: Any): MutableList<RequestMappingInfoHandlerMapping> {
//                return try {
//                    val field = ReflectionUtils.findField(bean.javaClass, "handlerMappings")
//                    field!!.setAccessible(true)
//                    field[bean] as MutableList<RequestMappingInfoHandlerMapping>
//                } catch (e: IllegalArgumentException) {
//                    throw IllegalStateException(e)
//                } catch (e: IllegalAccessException) {
//                    throw IllegalStateException(e)
//                }
//            }
//        }
//    }
//
//    /**
//     * 自定义Swagger配置
//     */
//    abstract fun swaggerProperties(): SwaggerProperties
//}
//
