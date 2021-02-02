package com.ityu.linjiaadminapi.controller.system;


import com.ityu.bean.constant.Const;
import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.bean.constant.state.ManagerStatus;

import com.ityu.core.BussinessLog;

import com.ityu.bean.dto.UserDto;
import com.ityu.bean.entity.system.User;

import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.core.factory.UserFactory;
import com.ityu.service.system.ManagerService;
import com.ityu.utils.BeanUtil;
import com.ityu.utils.RandomUtil;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.warpper.UserWarpper;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * UserController
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.USER})
    public Object list(@RequestParam(required = false) String account,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer sex) {
        Page page = new PageFactory().defaultPage();
        if(StringUtil.isNotEmpty(name)){
            page.addFilter(SearchFilter.build("name", SearchFilter.Operator.LIKE, name));
        }
        if(StringUtil.isNotEmpty(account)){
            page.addFilter(SearchFilter.build("account", SearchFilter.Operator.LIKE, account));
        }
        page.addFilter( "status",SearchFilter.Operator.GT,0);
        page.addFilter("sex", sex);
        page = managerService.queryPage(page);
        List list = (List) new UserWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp();
        page.setRecords(list);
        return Rets.success(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑管理员", key = "name")
    // @RequiresPermissions(value = {Permission.USER_EDIT})
    public Object save(@Valid UserDto user) {
        if (user.getId() == null) {
            // 判断账号是否重复
            User theUser = managerService.findByAccount(user.getAccount());
            if (theUser != null) {
                throw new ApplicationException(BizExceptionEnum.USER_ALREADY_REG);
            }
            if(user.getPassword()==null){
                return Rets.failure("密码不能为空");
            }
            // 完善账号信息
            user.setSalt(RandomUtil.getRandomString(5));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setStatus(ManagerStatus.OK.getCode());
            managerService.insert(UserFactory.createUser(user, new User()));
        } else {
            User oldUser = managerService.get(user.getId());
            managerService.update(UserFactory.updateUser(user, oldUser));
        }
        return Rets.success();
    }

    @BussinessLog(value = "删除账号", key = "userId")
    @RequestMapping(method = RequestMethod.DELETE)
    //@RequiresPermissions(value = {Permission.USER_DEL})
    public Object remove(@RequestParam Long userId) {
        if (userId == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        if (userId.intValue() <= 2) {
            return Rets.failure("不能删除初始用户");
        }
        User user = managerService.get(userId);
        user.setStatus(ManagerStatus.DELETED.getCode());
        managerService.update(user);
        return Rets.success();
    }

    @BussinessLog(value = "设置账号角色", key = "userId")
    @RequestMapping(value = "/setRole", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.USER_EDIT})
    public Object setRole(@RequestParam("userId") Long userId, @RequestParam("roleIds") String roleIds) {
        if (BeanUtil.isOneEmpty(userId, roleIds)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能修改超级管理员
        if (userId.intValue() == Const.ADMIN_ID.intValue()) {
            throw new ApplicationException(BizExceptionEnum.CANT_CHANGE_ADMIN);
        }
        User user = managerService.get(userId);
        user.setRoleid(roleIds);
        managerService.update(user);
        return Rets.success();
    }

    @BussinessLog(value = "冻结/解冻账号", key = "userId")
    @RequestMapping(value = "changeStatus", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.USER_EDIT})
    public Object changeStatus(@RequestParam Long userId) {
        if (userId == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        User user = managerService.get(userId);
        user.setStatus(user.getStatus().intValue() == ManagerStatus.OK.getCode() ? ManagerStatus.FREEZED.getCode() : ManagerStatus.OK.getCode());
        managerService.update(user);
        return Rets.success();
    }

}
