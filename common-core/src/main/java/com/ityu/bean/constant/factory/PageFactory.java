package com.ityu.bean.constant.factory;



import com.ityu.bean.constant.state.Order;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpUtil.getRequest();
        //每页多少条数据
        int limit = 10;
        if(request.getParameter("limit")!=null) {
            limit = Integer.valueOf(request.getParameter("limit"));
        }
        String pageNum = "1";

        if(request.getParameter("page")!=null){
            pageNum = request.getParameter("page");
        }
        //每页的偏移量(本页当前有多少条)
        int offset = 0;
        if (StringUtil.isNotEmpty(pageNum)) {
            offset = (Integer.valueOf(pageNum) - 1) * limit;
        } else {

            offset = Integer.valueOf(request.getParameter("offset"));
        }
        //排序字段名称
        String sortName = request.getParameter("sort");
        //asc或desc(升序或降序)
        String order = request.getParameter("order");
        if (StringUtil.isEmpty(sortName)) {
            Page<T> page = new Page<>((offset / limit + 1), limit);
            return page;
        } else {
            Page<T> page = new Page<>((offset / limit + 1), limit, sortName);
            if (Order.ASC.getDes().equals(order)) {
                Sort sort = Sort.by(Sort.Direction.ASC, order);
                page.setSort(sort);
            } else {
                Sort sort = Sort.by(Sort.Direction.DESC, order);
                page.setSort(sort);

            }
            return page;
        }
    }
}
