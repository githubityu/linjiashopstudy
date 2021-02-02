package com.ityu.linjiaadminapi.controller.front.officialsite;

import com.ityu.bean.entity.cms.Article;
import com.ityu.bean.enumeration.cms.BannerTypeEnum;
import com.ityu.bean.enumeration.cms.ChannelEnum;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.offcialsite.BannerVo;
import com.ityu.bean.vo.offcialsite.News;
import com.ityu.service.cms.ArticleService;
import com.ityu.service.cms.BannerService;
import com.ityu.utils.factory.Page;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/news")
public class NewsController extends BaseController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public Object list() {
        Map<String, Object> dataMap = new HashMap<>(10);
        BannerVo banner = bannerService.queryBanner(BannerTypeEnum.NEWS.getValue());
        dataMap.put("banner", banner);

        List<News> newsList = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.NEWS.getId());

        for (com.ityu.bean.entity.cms.Article article : articlePage.getRecords()) {
            News news = new News();
            news.setDesc(article.getTitle());
            news.setUrl("/article?id=" + article.getId());
            news.setSrc("static/images/icon/user.png");
            newsList.add(news);
        }

        dataMap.put("list", newsList);

        Map map = new HashMap(2);

        map.put("data", dataMap);
        return Rets.success(map);

    }
}
