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

import com.github.raghav.Timer.DBTimer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
  private static final String URL = "jdbc:mysql://localhost:3307/flashSale";
  private static final String USER = "root";
  private static final String PASSWORD = "qwerty@123";

  public static Connection getConnection() {
    return DBTimer.record(
        "MySql Connection Creation",
        () -> {
          try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
          } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
          }
        });
  }
}
