package com.ityu.linjiamobileapi.contrlloer;

import com.ityu.bean.entity.shop.Favorite;
import com.ityu.bean.vo.front.Rets;
import com.ityu.service.shop.FavoriteService;
import com.ityu.web.controller.BaseController;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 1/25/2020 9:28 PM
 */
@RestController
@RequestMapping("/user/favorite")
public class FavoriteController extends BaseController {
    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping(value = "/add/{idGoods}", method = RequestMethod.POST)
    public Object add(@PathVariable("idGoods") Long idGoods) {
        Long idUser = getAdminUser().getId();
        Favorite old = favoriteService.get(idUser, idGoods);
        if (old != null) {
            return Rets.success();
        }
        Favorite favorite = new Favorite();
        favorite.setIdUser(idUser);
        favorite.setIdGoods(idGoods);
        favoriteService.insert(favorite);
        return Rets.success();
    }

    @RequestMapping(value = "/ifLike/{idGoods}", method = RequestMethod.GET)
    public Object ifLike(@PathVariable("idGoods") Long idGoods) {
        Long idUser = getAdminUser().getId();
        Favorite favorite = favoriteService.get(idUser, idGoods);
        return Rets.success(favorite != null);
    }

    @RequestMapping(value = "/dislike/{idGoods}", method = RequestMethod.POST)
    public Object disLike(@PathVariable("idGoods") long idGoods) {
        Long idUser = getAdminUser().getId();
        Favorite old = favoriteService.get(idUser, idGoods);
        if (old == null) {
            return Rets.failure("未收藏改商品");
        }
        favoriteService.delete(old);
        return Rets.success();
    }

    @RequestMapping(value = "/dislikeBatch", method = RequestMethod.POST)
    public Object disLike(@RequestBody List<Long> ids) {
        logger.info("ids:{}", Json.toJson(ids));
        favoriteService.delete(ids);
        return Rets.success();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object list() {
        List<Favorite> list = favoriteService.queryAll();
        return Rets.success(list);
    }

}
