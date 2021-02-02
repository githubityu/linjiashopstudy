package com.ityu.bean.vo.node;




import com.fasterxml.jackson.annotation.JsonInclude;
import com.ityu.bean.entity.system.Dept;

import java.util.ArrayList;
import java.util.List;

/**
 * DeptNode
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeptNode extends Dept {

    private List<DeptNode> children = null;

    public List<DeptNode> getChildren() {
        return children;
    }

    public void setChildren(List<DeptNode> children) {
        this.children = children;
    }

    public String getLabel() {
        return getSimplename();
    }
}
