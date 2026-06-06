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
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(table = "Order", detachable = "true")
public class Order {
  @PrimaryKey public int orderId;
  public int productId;
  public int UserId;
  public Status status;

  public enum Status {
    CONFIRMED,
    OUT_OF_STOCK,
    FAILED,
  }

  public Order() {}

  public Order(int orderId, int productId, int userId, Status status) {
    this.orderId = orderId;
    this.productId = productId;
    UserId = userId;
    this.status = status;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getUserId() {
    return UserId;
  }

  public void setUserId(int userId) {
    UserId = userId;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return String.format(
        "OrderId: %d | ProductId: %d | UserId: %d | Status: %s",
        orderId, productId, UserId, status.name());
  }
}
