package com.ityu.bean.vo.node;

import com.ityu.bean.entity.shop.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryNode extends Category {
    private List<CategoryNode> children= null;
    public String getLabel(){
        return getName();
    }

}
