package com.ityu.linjiaadminapi.controller.shop;

import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.core.BussinessLog;

import com.ityu.bean.entity.cms.Banner;
import com.ityu.bean.entity.shop.AttrKey;
import com.ityu.bean.entity.shop.Category;
import com.ityu.bean.entity.shop.CategoryBannerRel;
import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.bean.enumeration.Permission;
import com.ityu.bean.exception.ApplicationException;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.service.shop.AttrKeyService;
import com.ityu.service.shop.CategoryBannerRelService;
import com.ityu.service.shop.CategoryService;
import com.ityu.service.shop.GoodsService;
import com.ityu.utils.Lists;
import com.ityu.utils.factory.Page;
import com.ityu.web.controller.BaseController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/category")
public class CategoryController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryBannerRelService categoryBannerRelService;
	@Autowired
	private AttrKeyService attrKeyService;
	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public Object list() {
		Page<Category> page = new PageFactory<Category>().defaultPage();
		page = categoryService.queryPage(page);
		return Rets.success(page);
	}
	@RequestMapping(value = "/getAll",method = RequestMethod.GET)
	public Object getAll() {

		List<Category> categories = categoryService.queryAll();
		return Rets.success(categories);
	}
	@RequestMapping(method = RequestMethod.POST)
	@BussinessLog(value = "编辑商品类别", key = "name")
	//@RequiresPermissions(value = {Permission.CATEGORY_EDIT})
	public Object save(@ModelAttribute Category category){
		if(category.getId()==null){
			categoryService.insert(category);
		}else {
			categoryService.update(category);
		}
		return Rets.success();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	@BussinessLog(value = "删除商品类别", key = "id")
	//@RequiresPermissions(value = {Permission.CATEGORY_EDIT})
	public Object remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		long goodsCount = goodsService.count(SearchFilter.build("idCategory",id));
		if(goodsCount>0){
			throw new ApplicationException(BizExceptionEnum.DATA_CANNOT_REMOVE);
		}
		categoryService.deleteById(id);
		return Rets.success();
	}
	@RequestMapping(value="/getBanners/{idCategory}",method = RequestMethod.GET)
	public Object getBanners(@PathVariable("idCategory") Long idCategory){
		if (idCategory == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		List<CategoryBannerRel> relList = categoryBannerRelService.queryAll(SearchFilter.build("idCategory", SearchFilter.Operator.EQ,idCategory));
		List<Banner> bannerList = Lists.newArrayList();
		relList.forEach( item->{
			bannerList.add(item.getBanner());
		});

		return Rets.success(bannerList);
	}
	@RequestMapping(value ="getAttrKeys/{idCategory}",method = RequestMethod.GET)
	public Object getAttrKeys(@PathVariable("idCategory") Long idCategory){
		if (idCategory == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		List<AttrKey> list = attrKeyService.queryBy(idCategory);
		return Rets.success(list);

	}
	@RequestMapping(value="/removeBanner/{idCategory}/{idBanner}",method = RequestMethod.DELETE)
	//@RequiresPermissions(value = {Permission.CATEGORY_EDIT})
	public Object removeBanner(@PathVariable("idCategory") Long idCategory,
							@PathVariable("idBanner") Long idBanner){
		if (idCategory == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		CategoryBannerRel rel = categoryBannerRelService.get(Lists.newArrayList(
				SearchFilter.build("idCategory",idCategory),
				SearchFilter.build("idBanner",idBanner)
		));
		if(rel!=null){
			categoryBannerRelService.delete(rel);
		}
		return Rets.success();
	}
	@RequestMapping(value="/setBanner/{idCategory}/{idBanner}",method = RequestMethod.POST)
	//@RequiresPermissions(value = {Permission.CATEGORY_EDIT})
	public Object setBanner(@PathVariable("idCategory") Long idCategory,
							@PathVariable("idBanner") Long idBanner){
		if (idCategory == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		CategoryBannerRel rel = categoryBannerRelService.get(Lists.newArrayList(
				SearchFilter.build("idCategory",idCategory),
				SearchFilter.build("idBanner",idBanner)
		));
		if(rel!=null){
			return Rets.success();
		}
		rel = new CategoryBannerRel();
		rel.setIdCategory(idCategory);
		rel.setIdBanner(idBanner);
		categoryBannerRelService.insert(rel);
		return Rets.success();
	}

}
