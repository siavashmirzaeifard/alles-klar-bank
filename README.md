# 🏦 Bank Microservice Project

This repository contains the backend services for a **Bank Application**, built with **Spring Boot** and **Kotlin**. The architecture is designed for security and robustness, featuring a layered design and advanced concurrency control for financial operations.

---

## ⚙️ Key Technologies & Core Components

| Component | Technology | Purpose |
| :--- | :--- | :--- |
| **Framework** | **Spring Boot / Kotlin** | Rapid development and expressive syntax. |
| **Build Tool** | **Gradle** | Dependency management and building. |
| **Data Access** | **Spring Data JPA / Hibernate** | Database persistence and ORM. |
| **Security** | **JWT (JWS) + Spring Security** | Stateless, token-based authentication and authorization. |
| **Concurrency** | **Pessimistic Locking** & **Serializable Isolation** | Ensuring atomic and consistent financial transactions. |

---

## 🔒 Security and Concurrency Features

Your project implements a highly secure and robust back-end, focusing on three critical areas:

### 1. Robust Transactional Safety (Concurrency Control)
Critical financial transactions (`transfer`, `withdraw`, `deposit`) utilize the following database controls to prevent race conditions, dirty reads, and lost updates:
* **`@Lock(LockModeType.PESSIMISTIC_WRITE)`**: Acquires an exclusive lock on the selected database rows during the transaction.
* **`Isolation.SERIALIZABLE`**: Guarantees that concurrent transactions execute as if they occurred serially, providing the highest level of data integrity.

### 2. Stateless JWT Authentication
* **Access/Refresh Tokens:** Uses short-lived Access Tokens for resource access and long-lived, hashed Refresh Tokens for renewal.
* **Password Storage:** All passwords are securely stored using **BCrypt** hashing.

### 3. Comprehensive Global Error Handling
The **`GlobalExceptionHandler`** ensures that all API consumers receive clear, standardized JSON error responses based on the failure type:
* **`401 UNAUTHORIZED`**: Invalid credentials or tokens.
* **`403 FORBIDDEN`**: Failed business logic (e.g., insufficient funds).
* **`404 NOT FOUND`**: Missing resources.

---

## 🚀 How to Run the Application (Kotlin & Gradle)

*(You must configure your database connection details and JWT secret in `application.properties` or equivalent.)*

1.  **Prerequisites:**
    * Java Development Kit (JDK) 17+
    * Gradle
    * A running **Database** instance (e.g., PostgreSQL).

2.  **Clone the repository:**
    ```bash
    git clone [https://github.com/siavashmirzaeifard/Bank.git](https://github.com/siavashmirzaeifard/alles-klar-bank.git)
    cd Bank
    ```

3.  **Build and Run with Gradle:**
    ```bash
    # Build the project
    ./gradlew clean build
    
    # Run the application
    ./gradlew bootRun
    ```

---

## 💡 API Endpoints & Authorization

| Endpoint Pattern | Authorization | Purpose |
| :--- | :--- | :--- |
| `/api/auth/**` | **Public** | Registration, Login, Forget Password, Refresh Token operations. |
| `/api/auth/change-password` | **Authenticated** | Change user's password. |
| All other `/api/**` | **Authenticated** | Account Management, Transactions (Transfer, Deposit, Withdraw). |
