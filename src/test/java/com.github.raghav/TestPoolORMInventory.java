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

import com.github.raghav.repository.InventoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestPoolORMInventory extends AbstractDatabaseSetup {

  @BeforeAll
  public static void setupOrm() {
    initOrmFactory();
  }

  @Test
  public void populateAndReadInventory() {
    InventoryRepository repository = new InventoryRepository(pmf);

    // --- CLEAN STEP ---
    repository.deleteAll();

    // --- INSERT OPERATION ---
    Inventory item = new Inventory(101, "Pixel 10 Pro (ORM)", 500);
    repository.save(item);

    // --- SELECT OPERATION ---
    List<Inventory> results = repository.findAll();

    System.out.println("\n--- Inventory List (DataNucleus ORM) ---");
    for (Inventory result : results) {
      System.out.println(result);
    }
    System.out.println("----------------------------------------\n");
  }
}
