package com.ityu.service.base;


import com.google.common.collect.Lists;

import com.ityu.bean.constant.cache.Cache;
import com.ityu.bean.vo.query.DynamicSpecifications;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.dao.BaseRepository;

import com.ityu.utils.factory.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基础服务类<br>
 * 本服务类使用了@Cache相关注解做缓存管理，如果子类要实现缓存管理的方法，务必保证参数名和父类一致，否则缓存管理将失败；如果必须更改参数名，请在子类中覆写缓存配置
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 22:32
 */
public abstract class BaseService<T, ID extends Serializable, R extends BaseRepository<T, ID>>
        implements CrudService<T, ID> {
    @Autowired
    private R dao;
    @Override
    public void delete(T record){
        dao.delete(record);
    }
    @Override
    @CacheEvict(value = Cache.APPLICATION, key = "#root.targetClass.simpleName+':'+#id")
    public void deleteById(ID id) {
        dao.deleteById(id);
    }

    @Override
    public void delete(Iterable<ID> ids) {
        Iterator<ID> iterator = ids.iterator();
        while (iterator.hasNext()) {
            ID id = iterator.next();
            dao.deleteById(id);
        }
    }
    @Override
    public void deleteAll(Iterable<T> list){
        dao.deleteInBatch(list);
    }

    @Override
    public T insert(T record) {
        return dao.save(record);
    }

    @Override
//    todo 暂时不使用缓存，因为前后端分离，使用本地缓存无法做到数据同步更新，后面可以考虑使用redis缓存来替代ehcache
//    todo 如果针对get方法使用启用缓存功能，建议在更新数据的时候统一调用update方法，以避免使用其他方法而忘记使用CacheEvict没有更新缓存数据导致数据错乱
//    @Cacheable(value = Cache.APPLICATION, key = "#root.targetClass.simpleName+':'+#id")
    public T get(ID id) {
        Optional<T> o = dao.findById(id);
        return o.isPresent()?o.get():null;
    }

    @Override
    public T get(SearchFilter filter) {
        List<T> list = queryAll(filter);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public T get(List<SearchFilter> filters) {
        List<T> list = queryAll(filters);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Object get(String sql,Class<?> klass) {
        return dao.getBySql(sql,klass);
    }

    @Override
    public List<T> query(Iterable<ID> ids) {
        return dao.findAllById(ids);
    }

    @Override
    public List<T> queryAll() {
        return dao.findAll();
    }

    @Override
    public List<T> queryAll(Sort sort) {
        return dao.findAll(sort);
    }
    @Override
    public List<Map> queryBySql(String sql) {
        return dao.queryBySql(sql);
    }

    @Override
    public Map getMapBySql(String sql) {
        List<Map> list = queryBySql(sql);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<T> queryPage(Page<T> page) {
        Pageable pageable = null;
        if (page.getSort() != null) {
            pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), page.getSort());
        } else {
            pageable = PageRequest.of(page.getCurrent() - 1, page.getSize(), Sort.Direction.DESC, "id");
        }
        Specification<T> specification = DynamicSpecifications.bySearchFilter(page.getFilters(), dao.getDataClass());
        org.springframework.data.domain.Page<T> pageResult = dao.findAll(specification, pageable);
        page.setTotal(Integer.valueOf(pageResult.getTotalElements() + ""));
        page.setRecords(pageResult.getContent());
        return page;
    }

    @Override
    public List<T> queryAll(List<SearchFilter> filters) {
        return queryAll(filters, null);
    }

    @Override
    public List<T> queryAll(SearchFilter filter) {
        return queryAll(filter, null);
    }

    @Override
    public List<T> queryAll(List<SearchFilter> filters, Sort sort) {
        Specification<T> specification = DynamicSpecifications.bySearchFilter(filters, dao.getDataClass());
        if (sort == null) {
            return dao.findAll(specification);
        }
        return dao.findAll(specification, sort);
    }

    @Override
    public List<T> queryAll(SearchFilter filter, Sort sort) {
        if (filter != null) {
            return queryAll(Lists.newArrayList(filter), sort);
        } else {
            return queryAll(Lists.newArrayList(), sort);
        }

    }

    @Override
    public long count(SearchFilter filter) {
        return count(Lists.newArrayList(filter));
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public long count(List<SearchFilter> filters) {
        Specification<T> specification = DynamicSpecifications.bySearchFilter(filters, dao.getDataClass());
        return dao.count(specification);
    }

    @Override
    @CacheEvict(value = Cache.APPLICATION, key = "#root.targetClass.simpleName+':'+#record.id")
    public T update(T record) {
        return dao.save(record);
    }

    @Override
    public void update(Iterable<T> list) {
        dao.saveAll(list);
    }

    @Override
    public void clear() {
        dao.deleteAllInBatch();
    }


}
