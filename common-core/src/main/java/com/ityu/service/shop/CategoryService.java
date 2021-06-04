package com.ityu.service.shop;



import com.ityu.bean.entity.shop.Category;
import com.ityu.bean.vo.node.CategoryNode;
import com.ityu.dao.shop.CategoryRepository;
import com.ityu.service.base.BaseService;
import com.ityu.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService<Category,Long,CategoryRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryNode> getCategories() {
        List<Category> list = queryAll(Sort.by(Sort.Direction.ASC,"sort"));
        List<CategoryNode> nodes = Lists.newArrayList();
        for(Category category:list){
            if(category.getPid()==null) {
                CategoryNode node = new CategoryNode();
                BeanUtils.copyProperties(category, node);
                nodes.add(node);
            }

        }
        for(CategoryNode node:nodes){
            for(Category category:list){
                if(category.getPid()!=null&&category.getPid().intValue() == node.getId().intValue()){
                    CategoryNode child = new CategoryNode();
                    BeanUtils.copyProperties(category,child);
                    if(node.getChildren()==null){
                        node.setChildren(Lists.newArrayList());
                    }
                    node.getChildren().add(child);
                }
            }
        }
        return nodes;
    }
}

