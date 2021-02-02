package com.ityu.linjiaadminapi.controller.system;



import com.ityu.cache.DictCache;
import com.ityu.core.BussinessLog;
import com.ityu.bean.entity.system.Dict;

import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.service.system.DictService;
import com.ityu.utils.BeanUtil;
import com.ityu.utils.StringUtil;
import com.ityu.bean.vo.front.Rets;
import com.ityu.warpper.DictWarpper;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DictController
 *
 * @author enilu
 * @version 2018/11/17 0017
 */
@RestController
@RequestMapping("/api/dict")
public class DictController extends BaseController {
    @Autowired
    private DictService dictService;
    @Autowired
    private DictCache dictCache;
    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.DICT})
    public Object list(String name) {

        if(StringUtil.isNotEmpty(name)){
            List<Dict> list = dictService.findByNameLike(name);
            return Rets.success(new DictWarpper(BeanUtil.objectsToMaps(list)).warp());
        }
        List<Dict> list = dictService.findByPid(0L);
        return Rets.success(new DictWarpper(BeanUtil.objectsToMaps(list)).warp());
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "添加字典", key = "dictName")
    //@RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object add(String dictName, String dictValues) {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        dictService.addDict(dictName, dictValues);
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.PUT)
    @BussinessLog(value = "修改字典", key = "dictName")
   // @RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object update(Long id,String dictName, String dictValues) {
        if (BeanUtil.isOneEmpty(dictName, dictValues)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        dictService.editDict(id,dictName, dictValues);
        return Rets.success();
    }


    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除字典", key = "id")
    //@RequiresPermissions(value = {Permission.DICT_EDIT})
    public Object delete(@RequestParam Long id) {
        dictService.delteDict(id);
        return Rets.success();
    }
    @RequestMapping(value = "/getDicts/{dictName}", method = RequestMethod.GET)
    public Object getDicts(@PathVariable("dictName") String dictName) {
        List<Dict> dicts = dictCache.getDictsByPname(dictName);
        return Rets.success(dicts);
    }


}
