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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "Inventory", detachable = "true")
public class Inventory {
  @PrimaryKey
  @Persistent(column = "id")
  private int productId;

  @Persistent(column = "name")
  private String name;

  @Persistent(column = "quantity")
  private int stockCount;

  // JDO requires a no-args constructor
  public Inventory() {}

  public Inventory(int productId, String name, int stockCount) {
    this.productId = productId;
    this.name = name;
    this.stockCount = stockCount;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStockCount() {
    return stockCount;
  }

  public void setStockCount(int stockCount) {
    this.stockCount = stockCount;
  }

  @Override
  public String toString() {
    return String.format("ID: %d | Name: %s | Quantity: %d", productId, name, stockCount);
  }
}
