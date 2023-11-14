package com.ityu.sdf.api.controller.test


import com.ityu.common.bean.constant.cache.Cache
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.dto.TestValid
import com.ityu.common.service.RedisService
import com.ityu.common.utils.JwtTokenUtil
import com.ityu.security.config.IgnoreUrlsConfig
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid


@Api(tags = ["test"])
@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    var config: IgnoreUrlsConfig? = null

    @Autowired
    var jwtUtil: JwtTokenUtil? = null

    @Autowired
    var redisService: RedisService? = null

    @ApiOperation("test")
    @GetMapping(value = ["/test"])
    fun testA(@RequestParam("key") key: String?): Ret<*> {
        var d = redisService?.hGet(Cache.APPLICATION, "UserService:1")
        return Rets.success(d)
    }

    @ApiOperation("test2")
    @GetMapping(value = ["/test2"])
    fun testB(@RequestParam("key") key: String?): Ret<*> {
        val d = jwtUtil?.generateToken("11111", 1)
        val d2 = jwtUtil?.getUserNameFromToken(d!!)
        redisService?.set("aaa", 5)
        return Rets.success("===key=$key d2=$d2")
    }

    @ApiOperation("clearCache")
    @GetMapping(value = ["/clearCache"])
    fun clearCache(): Ret<*> {
        val state = redisService?.clearAll() ?: false
        if (state) {
            return Rets.success("清除缓存成功")
        } else {
            return Rets.failure("无缓存数据可清除")

        }
    }


    @ApiOperation("testValid")
    @PostMapping(value = ["/testValid"])
    fun testValid(@Valid  @RequestBody user:  TestValid): Ret<*> {
        return Rets.success(user?.account)
    }

}
