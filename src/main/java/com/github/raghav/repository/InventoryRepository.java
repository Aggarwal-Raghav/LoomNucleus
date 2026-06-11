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
package com.github.raghav.repository;

import static com.github.raghav.util.DBTimer.record;

import com.github.raghav.Inventory;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class InventoryRepository {
  private final PersistenceManagerFactory pmf;

  public InventoryRepository(PersistenceManagerFactory pmf) {
    this.pmf = pmf;
  }

  public void save(Inventory item) {
    PersistenceManager pm = pmf.getPersistenceManager();
    try (pm) {
      Transaction tx = pm.currentTransaction();
      record(
          "ORM Insert Inventory",
          () -> {
            try {
              tx.begin();
              pm.makePersistent(item);
              tx.commit();
            } finally {
              if (tx.isActive()) {
                tx.rollback();
              }
            }
          });
    }
  }

  public List<Inventory> findAll() {
    PersistenceManager pm = pmf.getPersistenceManager();
    try (pm) {
      return record(
          "ORM Select From Inventory",
          () -> {
            Query<Inventory> q = pm.newQuery(Inventory.class);
            return (List<Inventory>) pm.detachCopyAll(q.executeList());
          });
    }
  }

  public Inventory findById(int id) {
    PersistenceManager pm = pmf.getPersistenceManager();
    try (pm) {
      return record(
          "ORM Find Inventory By ID",
          () -> {
            // Retrieve object by Primary Key
            Inventory item = pm.getObjectById(Inventory.class, id);
            // Detach so we can use it outside this method
            return pm.detachCopy(item);
          });
    }
  }

  public void updateStock(int id) {
    PersistenceManager pm = pmf.getPersistenceManager();
    try (pm) {
      Transaction tx = pm.currentTransaction();
      record(
          "ORM Update Stock",
          () -> {
            try {
              tx.begin();

              // 1. Retrieve the ATTACHED object inside the transaction
              Inventory item = pm.getObjectById(Inventory.class, id);

              // 2. Simply use the setter. DataNucleus "marks" the object as dirty
              item.setStockCount(item.getStockCount() - 1);

              // 3. Commit. DataNucleus automatically generates the SQL UPDATE statement
              tx.commit();
            } finally {
              if (tx.isActive()) {
                tx.rollback();
              }
            }
          });
    }
  }

  public void deleteAll() {
    PersistenceManager pm = pmf.getPersistenceManager();
    try (pm) {
      Transaction tx = pm.currentTransaction();
      tx.begin();
      Query<Inventory> qry = pm.newQuery(Inventory.class);
      qry.deletePersistentAll();
      tx.commit();
    }
  }
}
