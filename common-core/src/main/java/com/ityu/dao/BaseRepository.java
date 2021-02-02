package com.ityu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 封装基础的dao接口
 *
 * @author ：enilu
 * @date ：Created in 2019/6/29 12:50
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>
        , PagingAndSortingRepository<T, ID>
        , JpaSpecificationExecutor<T> {
    /**
     * 根据原生sql语句查询数据列表
     * @param sql
     * @return
     */
    List<Map> queryBySql(String sql);

    /**
     * 根据原生sql查询数据，返回指定对象列表
     * @param sql
     * @param klass
     * @return
     */
    List<?> queryBySql(String sql,Class<?> klass);
    List<Map> queryMapBySql(String sql);
    List<?> queryObjBySql(String sql, Class<?> klass);
    /**
     * 根据原生sql查询对象列表
     * @param sql
     * @return
     */
    List<T> query(String sql);

    /**
     * 根据原生sql查询数组对象
     * @param sql
     * @return
     */
    Object getBySql(String sql,Class<?> klass);

    /**
     * 根据原生sql查询对象
     * @param sql
     * @return
     */
    T get(String sql);
    T getOne(ID id);

    /**
     * 执行sql
     * @param sql
     * @return
     */
    int execute(String sql);

    /**
     * 获取数据类型
     * @return
     */
    Class<T> getDataClass();
}
