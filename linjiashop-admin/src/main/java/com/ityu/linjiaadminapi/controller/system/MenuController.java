package com.ityu.linjiaadminapi.controller.system;



import com.ityu.bean.entity.system.Menu;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.node.*;
import com.ityu.cache.TokenCache;
import com.ityu.core.BussinessLog;
import com.ityu.security.AdminUser;
import com.ityu.service.base.MenuService;
import com.ityu.service.system.LogObjectHolder;
import com.ityu.service.system.impl.ConstantFactory;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.Lists;
import com.ityu.utils.Maps;
import com.ityu.utils.StringUtil;
import com.ityu.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * MenuController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@Slf4j
@RequestMapping("/api/menu")
public class MenuController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;
    @Autowired
    private TokenCache tokenCache;

    @RequestMapping(value = "/listForRouter", method = RequestMethod.GET)
    public Object listForRouter() {
        AdminUser shiroUser = tokenCache.getUser(HttpUtil.getToken());

        List<RouterMenu> list = menuService.getSideBarMenus(shiroUser.getRoleList());
        return Rets.success(list);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        List<MenuNode> list = menuService.getMenus();
        return Rets.success(list);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public Object tree() {
        List<MenuNode> list = menuService.getMenus();
        List<TreeSelectNode> treeSelectNodes = com.google.common.collect.Lists.newArrayList();
        for (MenuNode menuNode : list) {
            TreeSelectNode tsn = transfer(menuNode);
            treeSelectNodes.add(tsn);
        }
        return Rets.success(treeSelectNodes);
    }

    public TreeSelectNode transfer(MenuNode node) {
        TreeSelectNode tsn = new TreeSelectNode();
        tsn.setId(node.getCode());
        tsn.setLabel(node.getName());
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            List<TreeSelectNode> children = com.google.common.collect.Lists.newArrayList();
            for (MenuNode child : node.getChildren()) {
                children.add(transfer(child));
            }
            tsn.setChildren(children);
        }
        return tsn;
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑菜单", key = "name")
//    @PreAuthorize("hasAuthority('administrator') or hasAuthority('admin')")
//    @PreAuthorize("hasAuthority('administrator') and hasAuthority('admin')")
    public Object save(@ModelAttribute @Valid Menu menu) {
        //判断是否存在该编号
        if (menu.getId() == null) {
            String existedMenuName = ConstantFactory.me().getMenuNameByCode(menu.getCode());
            if (StringUtil.isNotEmpty(existedMenuName)) {
                throw new ApplicationException(BizExceptionEnum.EXISTED_THE_MENU);
            }
        }

        //设置父级菜单编号
        menuService.menuSetPcode(menu);
        if (menu.getId() == null) {
            menuService.insert(menu);
        } else {
            menuService.update(menu);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除菜单", key = "id")
    // @RequiresPermissions(value = {Permission.MENU_DEL})
    @PreAuthorize("hasRole('administrator')")
    public Object remove(@RequestParam Long id) {
        logger.info("id:{}", id);
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL.REQUEST_NULL);
        }
        //演示环境不允许删除初始化的菜单
        if (id.intValue() < 70) {
            return Rets.failure("演示环境不允许删除初始菜单");
        }
        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(id));
        menuService.delMenuContainSubMenus(id);
        return Rets.success();
    }

    /**
     * 获取菜单树
     */
    @RequestMapping(value = "/menuTreeListByRoleId", method = RequestMethod.GET)
    //@RequiresPermissions(value = {Permission.MENU})
    //@PreAuthorize("hasAnyRole('administrator','avcd')")
    //@PreAuthorize("hasAnyAuthority('administrator','avcd')")
    //@PreAuthorize("hasAuthority('administrator') and hasAuthority('admin')")
    @PreAuthorize("hasPermission('read','[1,2]')")
    public Object menuTreeListByRoleId(Integer roleId) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
        List<ZTreeNode> roleTreeList = null;
        if (menuIds == null || menuIds.isEmpty()) {
            roleTreeList = menuService.menuTreeList(null);
        } else {
            roleTreeList = menuService.menuTreeList(menuIds);

        }
        List<Node> list = menuService.generateMenuTreeForRole(roleTreeList);

        //element-ui中tree控件中如果选中父节点会默认选中所有子节点，所以这里将所有非叶子节点去掉
        Map<Long, ZTreeNode> map = Lists.toMap(roleTreeList, "id");
        Map<Long, List<ZTreeNode>> group = Lists.group(roleTreeList, "pId");
        for (Map.Entry<Long, List<ZTreeNode>> entry : group.entrySet()) {
            if (entry.getValue().size() > 1) {
                roleTreeList.remove(map.get(entry.getKey()));
            }
        }

        List<Long> checkedIds = Lists.newArrayList();
        for (ZTreeNode zTreeNode : roleTreeList) {
            if (zTreeNode.getChecked() != null && zTreeNode.getChecked()
                    && zTreeNode.getpId().intValue() != 0) {
                checkedIds.add(zTreeNode.getId());
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds));
    }
}
