package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.Dict
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.cache.DictCache
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.DictService
import com.ityu.common.utils.BeanUtil
import com.ityu.common.utils.StringUtil
import com.ityu.common.warpper.DictWrapper
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * DictController
 *
 * @author enilu
 * @version 2018/11/17 0017
 */
@Api(tags = ["dict"])
@Tag(name = "dict", description = "字典模块")
@RestController
@RequestMapping("/dict")
open class DictController(private var dictService: DictService, private val dictCache: DictCache) {


    /**
     * 获取所有字典列表
     */
    @GetMapping(value = ["/list"])
    open fun list(name: String?): Any {
        if (StringUtil.isNotEmpty(name)) {
            val list = dictService!!.findByNameLike(name)
            return success(DictWrapper(BeanUtil.objectsToMaps(list)).warp())
        }
        val list = dictService!!.findByPid(0L)
        return success(DictWrapper(BeanUtil.objectsToMaps(list)).warp())
    }

    @PostMapping
    @BussinessLog(value = "添加字典", key = "dictName")
    open fun add(dictName: String?, dictValues: String?): Any {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        dictService.addDict(dictName, dictValues)
        return success()
    }

    @PutMapping
    @BussinessLog(value = "修改字典", key = "dictName")
    open fun update(id: Long?, dictName: String?, dictValues: String?): Any {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        dictService.editDict(id, dictName, dictValues)
        return success()
    }


    @DeleteMapping
    @BussinessLog(value = "删除字典", key = "id")
    open fun delete(@RequestParam id: Long?): Any {
        dictService.delteDict(id)
        return success()
    }

    @GetMapping(value = ["/getDicts"])
    open fun getDicts(@RequestParam dictName: String?): Any {
        val dicts: List<Dict> = dictCache.getDictsByPname(dictName)
        return success(dicts)
    }

}
