package com.ityu.service.cms;


import com.ityu.bean.entity.cms.Article;

import com.ityu.bean.enumeration.cms.ChannelEnum;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.dao.cms.ArticleRepository;
import com.ityu.service.base.BaseService;
import com.ityu.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService extends BaseService<Article, Long, ArticleRepository> {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * 查询首页最近5条资讯数据
     *
     * @return
     */
    public List<Article> queryIndexNews() {
        Page<Article> page = query(1, 5, ChannelEnum.NEWS.getId());
        return page.getRecords();
    }

    public Page<Article> query(int currentPage, int size, Long idChannel) {
        Page page = new Page(currentPage, size, "id");
        page.addFilter(SearchFilter.build("idChannel", SearchFilter.Operator.EQ, idChannel));
        return queryPage(page);

    }
}
