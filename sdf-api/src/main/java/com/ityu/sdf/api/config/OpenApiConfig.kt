package com.ityu.sdf.api.config


import com.ityu.sdf.api.utils.ApiConstants
import io.swagger.v3.oas.annotations.ExternalDocumentation
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration


/**
 * http://localhost:8087/swagger-ui/index.html#/
 * 如果想过滤那个api就用  @SecurityRequirements
 */
@Configuration
//定义
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = ApiConstants.Authorization,
    scheme = "bearer",
    `in` = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition(
    info = Info(
        title = "文档标题",
        version = "1.0",
        description = "文档描述",
        contact = Contact(name = "itYu", email = "123@164.com", url = "www.baidu.com")
    ),
    //使用
    security = [SecurityRequirement(name = ApiConstants.Authorization)],
    externalDocs = ExternalDocumentation(
        description = "参考文档",
        url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"
    )
)
open class OpenApiConfig

