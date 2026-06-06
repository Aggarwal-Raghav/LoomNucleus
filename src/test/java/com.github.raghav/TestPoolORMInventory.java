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

import static com.github.raghav.Timer.DBTimer.record;

import java.util.List;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestPoolORMInventory {
  private static PersistenceManagerFactory pmf;

  @BeforeAll
  public static void setup() {
    // 1. Initialize the PersistenceManagerFactory using the unit defined in persistence.xml
    pmf = JDOHelper.getPersistenceManagerFactory("FlashSaleUnit");
  }

  @AfterAll
  public static void tearDown() {
    if (pmf != null && !pmf.isClosed()) {
      pmf.close();
    }
  }

  @Test
  public void populateAndReadInventory() {
    // 2. Obtain a PersistenceManager
    PersistenceManager pm = pmf.getPersistenceManager();

    try (pm) {
      Transaction tx = pm.currentTransaction();
      // --- CLEAN STEP ---
      tx.begin();
      Query<Inventory> qry = pm.newQuery(Inventory.class);
      qry.deletePersistentAll();
      tx.commit();

      // --- INSERT OPERATION ---
      record(
          "ORM Insert Inventory",
          () -> {
            try {
              tx.begin();
              Inventory item = new Inventory(101, "Pixel 10 Pro (ORM)", 500);
              pm.makePersistent(item);
              tx.commit();
            } finally {
              if (tx.isActive()) {
                tx.rollback();
              }
            }
          });

      // --- SELECT OPERATION ---
      record(
          "ORM Select From Inventory",
          () -> {
            Query<Inventory> q = pm.newQuery(Inventory.class);
            List<Inventory> results = q.executeList();

            System.out.println("\n--- Inventory List (DataNucleus ORM) ---");
            for (Inventory item : results) {
              System.out.println(item);
            }

            System.out.println("----------------------------------------\n");
          });
    }
  }
}
