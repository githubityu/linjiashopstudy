package com.ityu.config;


import com.ityu.utils.IdWorker;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class SnowflakeId implements IdentifierGenerator {

    public SnowflakeId() {

    }
    @Autowired
    IdWorker idWorker;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return idWorker.nextId() + "";
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return false;
    }
}
