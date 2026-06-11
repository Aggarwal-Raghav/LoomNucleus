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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

  public static Connection getConnection() {
    return DBTimer.record(
        "Raw JDBC Connection Creation",
        () -> {
          try {
            return DriverManager.getConnection(
                ConfigLoader.get("db.url"),
                ConfigLoader.get("db.user"),
                ConfigLoader.get("db.password"));
          } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
          }
        });
  }
}
