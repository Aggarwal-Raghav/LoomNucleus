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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestThunderingHerd extends AbstractDatabaseSetup {

  static InventoryRepository repository;
  private static final ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();

  @BeforeAll
  public static void setupOrm() {
    initOrmFactory();
    populateAndReadInventory();
  }

  public static void populateAndReadInventory() {
    repository = new InventoryRepository(pmf);
    repository.deleteAll();
    Inventory item = new Inventory(101, "Pixel 10 Pro (ORM)", 500);
    repository.save(item);
  }

  @Test
  public void testThunderingHerd() throws InterruptedException {
    IntStream.range(0, 5000)
        .forEach(
            i ->
                pool.submit(
                    () -> {
                      try {
                        repository.updateStock(101);
                        System.out.println(
                            "STOCK COUNT For thread-" + i + "): " + repository.findById(101));
                      } catch (Exception e) {
                        // Ignore expected concurrency exceptions for now
                      }
                    }));

    // Wait for all virtual threads to finish
    pool.shutdown();
    pool.awaitTermination(2, java.util.concurrent.TimeUnit.MINUTES);

    Inventory finalItem = repository.findById(101);
    System.out.println("FINAL STOCK COUNT (Phase 2): " + finalItem.getStockCount());
  }
}
