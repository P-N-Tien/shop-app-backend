# Overview – Shop Application

Author: Pham Ngoc Tien

Version: 1.0.0

Year: 2025

### 1. Core Features:

    - Product Discovery: Efficient search and filtering functionality.
    - Order Management: Streamlined "Place order" and "Cancel order" workflows.
    - VNPay Integration: Handles secure redirect, payment verification (IPN)
      and transaction status updates.

### 2. Functional Requirements:

    - Authentication: User registration and Login.
    - Product Management: CRUD operations for categories and products.
    - Order Processing: Seamlessly place and cancel orders.
    - Inventory Management: Automatically adjusts stock levels upon order placement or cancellation.
    - Online Payment: Supports secure transactions via VNPay.

### 3. Non-functional Requirements:

    - Security: Implementations using JWT with Secure/HttpOnly Cookies.
    - Token Management: Revokes expired or compromised tokens.
    - Concurrency Control: Prevents race condition using Optimistic/Pessimistic Locking.
    - Design patterns: Implements Strategy, Factory and Singleton patterns.
    - Code Quality: Adheres to Clean Code principles and SOLID.



