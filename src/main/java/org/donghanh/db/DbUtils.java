package org.donghanh.db;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DbUtils {
  public static PoolingDataSource setupDataSource(String url, String user, String pass) {
    GenericObjectPool connectionPool = new GenericObjectPool(null);
    ConnectionFactory connectionFactory =
        new DriverManagerConnectionFactory(url, user, pass);
    PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,
        connectionPool, null, null, false, true);
    return new PoolingDataSource(connectionPool);
  }
}
