# Flash Sale Inventory Engine

## Problem Statement

Build a high-concurrency inventory management system that simulates a "Black Friday" flash sale. The project aims to demonstrate the transition from basic JDBC to robust connection pooling (HikariCP), ORM-based persistence (DataNucleus), and advanced Java 21 concurrency patterns. The primary challenge is ensuring that 5,000+ simultaneous users cannot "oversell" a limited stock of 500 items while maintaining system stability and performance.

## Project Phases

### Phase 1: The Foundation (JDBC + Initial Schema)

* **Goal:** Establish basic database connectivity and schema.
* **Details:** Create `Product` and `Order` entities. Use plain JDBC (`DriverManager`) to initialize the database and perform a single-threaded purchase.
* **Objective:** Understand the manual overhead of connection management.

### Phase 2: The Thundering Herd (JDK 21 Virtual Threads)

* **Goal:** Simulate massive parallel traffic.
* **Details:** Use JDK 21 Virtual Threads to launch 5,000 concurrent "buy" requests against the JDBC-backed database.
* **Objective:** Observe the system break under load (connection timeouts and "dirty write" race conditions).

### Phase 3: The Race Condition (Concurrency Control)

* **Goal:** Prevent overselling.
* **Details:** Implement locking mechanisms (Optimistic or Pessimistic) via DataNucleus to ensure exactly 500 items are sold.
* **Objective:** Master transactional integrity and data consistency in a multi-threaded environment.

### Phase 4: Connection Pool Starvation (HikariCP Tuning)

* **Goal:** Optimize connection lifecycle.
* **Details:** Transition to HikariCP. Introduce artificial network latency (simulated payment gateway) inside the transaction to cause pool exhaustion. Refactor the code to minimize "connection lease time."
* **Objective:** Learn how to tune HikariCP and scope transactions for maximum throughput.

### Phase 5: Structured Concurrency (JDK 21 Polish)

* **Goal:** Modernize asynchronous logic.
* **Details:** Use `StructuredTaskScope` to handle complex checkout flows (e.g., fetching user profile and shipping rates concurrently with inventory checks).
* **Objective:** Implement fail-fast logic to save database resources when sub-tasks fail.

