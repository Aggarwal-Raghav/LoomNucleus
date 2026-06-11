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

import java.sql.Connection;
import java.sql.SQLException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDatabaseSetup {

  // Instance variable for isolated raw JDBC tests
  protected Connection conn;

  // Shared factory for ORM tests
  protected static PersistenceManagerFactory pmf;

  @BeforeAll
  public static void globalSetup() {
    // 1. We no longer eagerly initialize the PersistenceManagerFactory here.
    // Doing so forces DataNucleus and HikariCP to start even for tests that only want raw JDBC.
  }

  // A helper method for ORM tests to call manually in their @BeforeAll
  protected static void initOrmFactory() {
    if (pmf == null || pmf.isClosed()) {
      pmf = JDOHelper.getPersistenceManagerFactory("FlashSaleUnit");
    }
  }

  @BeforeEach
  public void setUpConnection() {
    // 2. Setup raw JDBC connection (Isolated per test)
    conn = DBConnectionManager.getConnection();
  }

  @AfterEach
  public void tearDownConnection() throws SQLException {
    if (conn != null && !conn.isClosed()) {
      conn.close();
    }
  }

  @AfterAll
  public static void globalTearDown() {
    if (pmf != null && !pmf.isClosed()) {
      pmf.close();
    }
  }
}
