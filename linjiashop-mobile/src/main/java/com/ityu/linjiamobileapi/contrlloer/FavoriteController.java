package com.ityu.linjiamobileapi.contrlloer;

import com.ityu.bean.entity.shop.Favorite;
import com.ityu.bean.vo.front.Rets;
import com.ityu.service.shop.FavoriteService;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：enilu
 * @date ：Created in 1/25/2020 9:28 PM
 */
@RestController
@RequestMapping("/user/favorite")
public class FavoriteController extends BaseController {
    @Autowired
    private FavoriteService favoriteService;
    @RequestMapping(value = "/add/{idGoods}",method = RequestMethod.POST)
    public Object add(@PathVariable("idGoods") Long idGoods){
        Long idUser = getAdminUser().getId();
        Favorite old = favoriteService.get(idUser,idGoods);
        if(old!=null){
            return Rets.success();
        }
        Favorite favorite = new Favorite();
        favorite.setIdUser(idUser);
        favorite.setIdGoods(idGoods);
        favoriteService.insert(favorite);
        return Rets.success();
    }
    @RequestMapping(value = "/ifLike/{idGoods}",method = RequestMethod.GET)
    public Object ifLike(@PathVariable("idGoods") Long idGoods){
        Long idUser = getAdminUser().getId();
        Favorite favorite = favoriteService.get(idUser,idGoods);
        return Rets.success(favorite!=null);
    }

}
