package com.ityu.linjiaadminapi.controller.system;


import com.ityu.bean.constant.factory.PageFactory;

import com.ityu.bean.entity.system.Express;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.core.BussinessLog;
import com.ityu.service.system.ExpressService;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys/express")
public class ExpressController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ExpressService expressService;

    /**
     * 分页查询物流公司
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.EXPRESS})
    public Object list() {
        Page<Express> page = new PageFactory<Express>().defaultPage();
        page.setSort(Sort.by(Sort.Direction.ASC, "sort"));
        page = expressService.queryPage(page);
        return Rets.success(page);
    }

    /**
     * 获取全部非禁用的物流公司列表
     * @return
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.EXPRESS})
    public Object queryAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        SearchFilter searchFilter = SearchFilter.build("disabled", false);
        List<Express> list = expressService.queryAll(searchFilter, sort);
        return Rets.success(list);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑物流公司", key = "name")
    //@RequiresPermissions(value = {Permission.EXPRESS_EDIT})
    public Object save(@ModelAttribute Express express) {
        if (express.getId() == null) {
            expressService.insert(express);
        } else {
            expressService.update(express);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除物流公司", key = "id")
    //@RequiresPermissions(value = {Permission.EXPRESS_EDIT})
    public Object remove(Long id) {
        if (StringUtil.isEmpty(id)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        expressService.deleteById(id);
        return Rets.success();
    }

    @RequestMapping(value="/changeDisabled",method = RequestMethod.POST)
    //@RequiresPermissions(value = {Permission.EXPRESS_EDIT})
    @BussinessLog(value = "启用禁用物流公司", key = "id")
    public Object changeIsOnSale(@RequestParam("id")  Long id, @RequestParam("disabled") Boolean disabled){
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        expressService.changeDisabled(id,disabled);
        return Rets.success();
    }
}
