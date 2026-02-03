# Technology Solution

### 1. Requirement Analysis

    - Platform: Web only.
    - Throughput: 10,000 requests per second (RPS).
    - Security: JWT-based Authentication.
    - Team size: 1 developer.
    - Timeline: 3 months.

### 2. System Architecture

**Monolithic Architecture:**

    Simplifies development, testing and deployment.

**Modular Design:**

    - Independent modules: Enhances testability, readability, maintainability and findability.
    - Programming to an interface: Ensures loose coupling and enables Dependency Injection.

**Layered Structure:**

    - Controller: Handles requests and responses (REST API).
    - Service: Encapsulates business logic.
    - Repository: Manages database queries (PostgreSQL).
    - Entity: Maps application objects to database tables.
    - DTO (Data Transfer Object): Transfers data across service layers.
    - Exception Handling: Captures exceptions and notifies users.
    - Logging: Audits system history and events.

### 3. Model

    - Role: Defines system roles (e.g., USER, ADMIN).
    - User: Stores user information such as username, password, name, and address.
    - Refresh Token: Manages token data for renewing and revoking access and refresh token.
    - Category: Manages product categories.
    - Product: Handles CRUD operations and product searching.
    - Product-Images: Stores associated product images.
    - Order: Processes orders, validates inventory and integrates online payments.
    - Order Detail: Stores product names and prices at the time of purchase for order historical accurary.
    - Inventory: Stores product stock quantities.
    - Idempotency: Guarantees that duplicate requests do not result in multiple orders.

### 4. Database schema

    - Role(id, name).
    - User(id, name, email, address, username, password, role_id).
    - RefreshToken(jti, expires_at, revoke, user_id).
    - Category(id, name).
    - Product(id, name, price, description, inventory_id, category_id).
    - ProductImage(id, url, product_id).
    - Order(id, total_money, payment_status, status, created_at, user_id, order_detail_id).
    - OrderDetail(id, quantity, price_at_purchase, order_id, product_id).
    - Inventory(id, quantity, reserved_quantity, sale_quantity, product_id).
    - Payment(id, amount, transaction_id, status, order_id).
    - Idempotency (id, idempotency_key, created_at).

### 5. Technologies

    - Backend: Spring Boot.
    - Database: PostgresSQL.
    - Cache: Redis.
    - ORM: Hibernate / Spring Data JPA.
    - Authentication: Stateless JWT.
    - Deployment: Docker & Docker compose.
    - Testing tools: K6(Load testing), Postman (API testing).
