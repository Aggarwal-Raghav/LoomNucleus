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
package com.github.raghav.util;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBTimer {
  private static final Logger LOG = LoggerFactory.getLogger(DBTimer.class);

  public static <T> T record(String operationName, Supplier<T> dbOperation) {
    long startTime = System.nanoTime();
    try {
      return dbOperation.get();
    } finally {
      long endTime = System.nanoTime();
      logExecution(operationName, startTime, endTime);
    }
  }

  public static void record(String operationName, Runnable dbOperation) {
    long startTime = System.nanoTime();
    try {
      dbOperation.run();
    } finally {
      long endTime = System.nanoTime();
      logExecution(operationName, startTime, endTime);
    }
  }

  private static void logExecution(String operationName, long startTime, long endTime) {
    double durationMs = (endTime - startTime) / 1_000_000.0;
    LOG.info("[DB Metrics] Operation: '{}' | Duration: {} ms", operationName, durationMs);
  }
}
