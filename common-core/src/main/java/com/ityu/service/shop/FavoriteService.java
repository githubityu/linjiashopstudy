package com.ityu.service.shop;



import com.ityu.bean.entity.shop.Favorite;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.dao.shop.FavoriteRepository;
import com.ityu.service.base.BaseService;
import com.ityu.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService extends BaseService<Favorite,Long, FavoriteRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite get(Long idUser, Long idGoods) {
        return get(Lists.newArrayList(
                SearchFilter.build("idUser",idUser),
                SearchFilter.build("idGoods",idGoods)
        ));

    }
}

