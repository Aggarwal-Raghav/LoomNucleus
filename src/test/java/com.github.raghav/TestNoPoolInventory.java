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

import static com.github.raghav.util.DBTimer.record;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class TestNoPoolInventory extends AbstractDatabaseSetup {

  @Test
  public void populateAndReadInventory() throws SQLException {
    String createTbl =
        "CREATE TABLE IF NOT EXISTS inventory (id INT PRIMARY KEY, name VARCHAR(256), quantity INT)";
    try (Statement stmt = conn.createStatement()) {
      record(
          "Create & Clean Inventory Table",
          () -> {
            try {
              stmt.execute(createTbl);
              // Clean the table to prevent duplicate primary key errors on repeated runs
              return stmt.execute("TRUNCATE TABLE inventory");
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });
    }

    String insertQuery = "INSERT INTO inventory (id, name, quantity) values(?,?,?)";
    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
      pstmt.setInt(1, 1);
      pstmt.setString(2, "Pixel 10 Pro");
      pstmt.setInt(3, 500);

      record(
          "Insert Inventory",
          () -> {
            try {
              return pstmt.executeUpdate();
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });
    }

    String selectQuery = "SELECT id, name, quantity FROM inventory";

    try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
      record(
          "Select From Inventory",
          () -> {
            try (ResultSet rs = pstmt.executeQuery()) {
              System.out.println("\n--- Inventory List (No Pool) ---");
              while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                System.out.printf("ID: %d | Name: %s | Quantity: %d%n", id, name, quantity);
              }
              System.out.println("----------------------\n");
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }
          });
    }
  }
}
