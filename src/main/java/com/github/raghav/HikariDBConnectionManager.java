/*
 * Copyright 2026 Raghav Aggarwal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.raghav;

import com.github.raghav.util.ConfigLoader;
import com.github.raghav.util.DBTimer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariDBConnectionManager {
  // HikariDataSource is thread-safe and TestPoolInventory should be a singleton
  private static final HikariDataSource dataSource;

  // Static block initializes the pool once when the class is first loaded
  static {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(ConfigLoader.get("db.url"));
    config.setUsername(ConfigLoader.get("db.user"));
    config.setPassword(ConfigLoader.get("db.password"));

    // Recommended HikariCP settings for MySQL
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("useServerPrepStmts", "true");

    // Pool sizing
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);

    // Initialize the pool
    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() {
    return DBTimer.record(
        "HikariCP Connection Request",
        () -> {
          try {
            return dataSource.getConnection();
          } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection from pool", e);
          }
        });
  }
}
