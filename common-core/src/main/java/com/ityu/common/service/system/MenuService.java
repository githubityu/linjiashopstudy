package com.ityu.common.service.system;


import com.ityu.common.bean.entity.system.Menu;
import com.ityu.common.bean.vo.node.*;
import com.ityu.common.dao.system.MenuRepository;
import com.ityu.common.exception.ApiException;
import com.ityu.common.exception.ResultCode;
import com.ityu.common.service.BaseService;
import com.ityu.common.utils.Lists;
import com.ityu.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created  on 2018/3/23 0023.
 *
 * @author enilu
 */
@Service
public class MenuService extends BaseService<Menu, Long, MenuRepository> {

    private Logger logger = LoggerFactory.getLogger(MenuService.class);
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void delete(Long menuId) {
        //删除菜单
        menuRepository.deleteById(menuId);
        //删除关联的relation
        menuRepository.deleteRelationByMenu(menuId);

    }

    public void delMenuContainSubMenus(Long menuId) {
        Menu menu = get(menuId);
        //删除所有子菜单
        List<Menu> menus = menuRepository.findByPcodesLike("%[" + menu.getCode() + "]%");
        menuRepository.deleteAll(menus);
        //删除当前菜单
        delete(menuId);

    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    public List<MenuNode> getMenus() {
        List<MenuNode> list = transferMenuNode(menuRepository.getMenus());
        List<MenuNode> result = generateTree(list);
        for (MenuNode menuNode : result) {
            if (!menuNode.getChildren().isEmpty()) {
                sortTree(menuNode.getChildren());
                for (MenuNode menuNode1 : menuNode.getChildren()) {
                    if (!menuNode1.getChildren().isEmpty()) {
                        sortTree(menuNode1.getChildren());
                    }
                }
            }
        }
        sortTree(result);
        return result;
    }

    /**
     * 获取左侧菜单树
     *
     * @return
     */
    public List<RouterMenu> getSideBarMenus(List roleIds) {
        List<RouterMenu> list = transferRouteMenu(menuRepository.getMenusByRoleids(roleIds), false);
        List<RouterMenu> result = generateRouterTree(list);
        for (RouterMenu menuNode : result) {
            if (!menuNode.getChildren().isEmpty()) {
                sortRouterTree(menuNode.getChildren());
                for (RouterMenu menuNode1 : menuNode.getChildren()) {
                    if (!menuNode1.getChildren().isEmpty()) {
                        sortRouterTree(menuNode1.getChildren());
                    }
                }
            }
        }
        sortRouterTree(result);
        return result;
    }


    private void sortTree(List<MenuNode> list) {
        Collections.sort(list, new Comparator<MenuNode>() {
            @Override
            public int compare(MenuNode o1, MenuNode o2) {
                return o1.getNum() - o2.getNum();
            }
        });
    }

    private void sortRouterTree(List<RouterMenu> list) {
        Collections.sort(list, new Comparator<RouterMenu>() {
            @Override
            public int compare(RouterMenu o1, RouterMenu o2) {
                return o1.getNum() - o2.getNum();
            }
        });
    }

    private List<MenuNode> generateTree(List<MenuNode> list) {
        List<MenuNode> result = new ArrayList<>(20);
        Map<Long, MenuNode> map = Lists.toMap(list, "id");
        for (Map.Entry<Long, MenuNode> entry : map.entrySet()) {
            MenuNode menuNode = entry.getValue();

            if (menuNode.getParentId().intValue() != 0) {
                MenuNode parentNode = map.get(menuNode.getParentId());
                if (parentNode != null) {
                    parentNode.getChildren().add(menuNode);
                }

            } else {
                result.add(menuNode);
            }
        }
        return result;

    }

    private List<RouterMenu> generateRouterTree(List<RouterMenu> list) {
        List<RouterMenu> result = new ArrayList<>(20);
        Map<Long, RouterMenu> map = Lists.toMap(list, "id");
        for (Map.Entry<Long, RouterMenu> entry : map.entrySet()) {
            RouterMenu menuNode = entry.getValue();

            if (menuNode.getParentId() != 0) {
                Long pId = menuNode.getParentId();
                RouterMenu parentNode = map.get(pId);
                if (parentNode != null) {
                    parentNode.getChildren().add(menuNode);
                }
            } else {
                result.add(menuNode);
            }
        }
        return result;

    }

    private List<MenuNode> transferMenuNode(List menus) {
        List<MenuNode> menuNodes = new ArrayList<>();
        try {
            for (int i = 0; i < menus.size(); i++) {
                Object[] source = (Object[]) menus.get(i);
                MenuNode menuNode = new MenuNode();
                menuNode.setId(Long.valueOf(source[0].toString()));
                menuNode.setIcon(StringUtil.sNull(source[1]));
                menuNode.setParentId(Long.valueOf(source[2].toString()));
                menuNode.setName(String.valueOf(source[3]));
                menuNode.setUrl(String.valueOf(source[4]));
                menuNode.setLevels(Integer.valueOf(source[5].toString()));
                menuNode.setIsmenu(Integer.valueOf(source[6].toString()));
                menuNode.setNum(Integer.valueOf(source[7].toString()));
                menuNode.setCode(String.valueOf(source[8]));

                if (source[9] != null) {
                    menuNode.setComponent(source[9].toString());
                }
                if ("1".equals(source[10].toString())) {
                    menuNode.setHidden(true);
                } else {
                    menuNode.setHidden(false);
                }
                menuNode.setPcode(StringUtil.sNull(source[11]));
                menuNodes.add(menuNode);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return menuNodes;
    }

    public List<RouterMenu> transferRouteMenu(List menus, boolean isAdd) {
        List<RouterMenu> routerMenus = new ArrayList<>();
        try {
            for (int i = 0; i < menus.size(); i++) {
                Object[] source = (Object[]) menus.get(i);
                if (!isAdd) {
                    if (source[9] == null) {
                        continue;
                    }
                }
                RouterMenu menu = new RouterMenu();
                menu.setPath(String.valueOf(source[4]));
                menu.setName(String.valueOf(source[8]));
                MenuMeta meta = new MenuMeta();
                meta.setIcon(String.valueOf(source[1]));
                meta.setTitle(String.valueOf(source[3]));
                menu.setNum(Integer.valueOf(source[7].toString()));
                menu.setParentId(Long.valueOf(source[2].toString()));
                if (source[9] != null) {
                    menu.setComponent(source[9].toString());
                }
                menu.setId(Long.valueOf(source[0].toString()));
                menu.setMeta(meta);
                if ("1".equals(source[10].toString())) {
                    menu.setHidden(true);
                }
                routerMenus.add(menu);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return routerMenus;
    }

    public List<ZTreeNode> menuTreeList(List<Long> menuIds) {
        List<ZTreeNode> nodes = new ArrayList<>();
        if (menuIds == null) {
            List list = menuRepository.menuTreeList();

            for (int i = 0; i < list.size(); i++) {
                Object[] source = (Object[]) list.get(i);
                ZTreeNode node = new ZTreeNode();
                node.setId(Long.valueOf(source[0].toString()));
                node.setPId(Long.valueOf(source[1].toString()));
                node.setName(source[2].toString());
                node.setOpen(Boolean.valueOf(source[3].toString()));
                nodes.add(node);
            }
        } else {
            nodes = menuTreeListByMenuIds(menuIds);
        }
        return nodes;
    }

    private List<ZTreeNode> menuTreeListByMenuIds(List<Long> menuIds) {
        List list = menuRepository.menuTreeListByMenuIds(menuIds);
        List<ZTreeNode> nodes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] source = (Object[]) list.get(i);
            ZTreeNode node = new ZTreeNode();
            node.setId(Long.valueOf(source[0].toString()));
            node.setPId(Long.valueOf(source[1].toString()));
            node.setName(source[2].toString());
            node.setOpen(Boolean.valueOf(source[3].toString()));
            node.setChecked(Boolean.valueOf(source[4].toString()));
            nodes.add(node);
        }
        return nodes;
    }

    public void menuSetPcode(Menu menu) {
        if (StringUtil.isEmpty(menu.getPcode()) || "0".equals(menu.getPcode())) {
            menu.setPcode("0");
            menu.setPcodes("[0],");
            menu.setLevels(1);
        } else {

            Menu pMenu = menuRepository.findByCode(menu.getPcode());
            Integer pLevels = pMenu.getLevels();
            menu.setPcode(pMenu.getCode());

            //如果编号和父编号一致会导致无限递归
            if (menu.getCode().equals(menu.getPcode())) {
                throw new ApiException(ResultCode.MENU_PCODE_COINCIDENCE);
            }

            menu.setLevels(pLevels + 1);
            menu.setPcodes(pMenu.getPcodes() + "[" + pMenu.getCode() + "],");
        }
    }

    public List<Node> generateMenuTreeForRole(List<ZTreeNode> list) {

        List<Node> nodes = new ArrayList<>(20);
        for (ZTreeNode menu : list) {
            Node permissionNode = new Node();
            permissionNode.setId(menu.getId());
            permissionNode.setName(menu.getName());
            permissionNode.setPid(menu.getPId());
            permissionNode.setChecked(menu.getChecked());
            nodes.add(permissionNode);
        }
        for (Node permissionNode : nodes) {
            for (Node child : nodes) {
                if (child.getPid().intValue() == permissionNode.getId().intValue()) {
                    permissionNode.getChildren().add(child);
                }
            }
        }
        List<Node> result = new ArrayList<>(20);
        for (Node node : nodes) {
            if (node.getPid().intValue() == 0) {
                result.add(node);
            }
        }
        return result;


    }


    public List<RouterMenu> getAllMenus(List roleIds) {
        return transferRouteMenu(menuRepository.getMenusByRoleids(roleIds), true);
    }

    public List<Long> getMenuIdsByRoleId(Integer roleId) {
        return menuRepository.getMenuIdsByRoleId(roleId);
    }

    public List getMenusByRoleids(List roleIds) {
        return menuRepository.getMenusByRoleids(roleIds);
    }

    public List<String> getResCodesByRoleId(Long roleId) {
        return menuRepository.getResCodesByRoleId(roleId);
    }

    public List<String> getResUrlsByRoleId(Long roleId) {
        return menuRepository.getResUrlsByRoleId(roleId);
    }
}
