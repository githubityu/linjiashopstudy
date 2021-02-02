package com.ityu.linjiaadminapi.controller.front.officialsite;

import com.ityu.bean.entity.cms.Article;
import com.ityu.bean.enumeration.cms.BannerTypeEnum;
import com.ityu.bean.enumeration.cms.ChannelEnum;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.offcialsite.BannerVo;
import com.ityu.bean.vo.offcialsite.Product;
import com.ityu.service.cms.ArticleService;
import com.ityu.service.cms.BannerService;
import com.ityu.utils.Maps;
import com.ityu.utils.factory.Page;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offcialsite/product")
public class OffcialSiteProductController extends BaseController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public Object index() {
        Map<String, Object> dataMap = Maps.newHashMap();

                BannerVo banner = bannerService.queryBanner(BannerTypeEnum.SOLUTION.getValue());
        dataMap.put("banner", banner);

        List<Product> products = new ArrayList<>();
        Page<Article> articlePage = articleService.query(1, 10, ChannelEnum.PRODUCT.getId());
        for (Article article : articlePage.getRecords()) {
            products.add(new Product(article.getId(), article.getTitle(), article.getImg()));
        }
        dataMap.put("productList", products);

        Map map =  Maps.newHashMap("data",dataMap);
        return Rets.success(map);

    }
}
