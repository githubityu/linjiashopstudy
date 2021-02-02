package com.ityu.linjiaadminapi.controller.system;



import com.ityu.bean.entity.system.Dept;
import com.ityu.bean.vo.node.DeptNode;
import com.ityu.core.BussinessLog;
import com.ityu.service.system.DeptService;
import com.ityu.utils.BeanUtil;
import com.ityu.web.controller.BaseController;

import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;

import com.ityu.service.system.LogObjectHolder;

import com.ityu.bean.vo.front.Rets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * DeptContoller
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@RestController
@RequestMapping("/api/dept")
public class DeptContoller extends BaseController {
    private Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private DeptService deptService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.DEPT})
    public Object list(){
        List<DeptNode> list = deptService.queryAllNode();
        return Rets.success(list);
    }
    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑部门", key = "simplename")
    //@RequiresPermissions(value = {Permission.DEPT_EDIT})
    public Object save(@ModelAttribute @Valid Dept dept){
        if (BeanUtil.isOneEmpty(dept, dept.getSimplename())) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        if(dept.getId()!=null){
            Dept old = deptService.get(dept.getId());
            LogObjectHolder.me().set(old);
            old.setPid(dept.getPid());
            old.setSimplename(dept.getSimplename());
            old.setFullname(dept.getFullname());
            old.setNum(dept.getNum());
            old.setTips(dept.getTips());
            deptService.deptSetPids(old);
            deptService.update(old);
        }else {
            deptService.deptSetPids(dept);
            deptService.insert(dept);
        }
        return Rets.success();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除部门", key = "id")
   // @RequiresPermissions(value = {Permission.DEPT_DEL})
    public Object remove(@RequestParam Long id){
        logger.info("id:{}",id);
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        deptService.deleteDept(id);
        return Rets.success();
    }
}
