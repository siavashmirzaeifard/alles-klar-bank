🏦 Bank Microservice Project
This repository contains the backend services for a Bank Application, built with Spring Boot and Kotlin. The architecture is designed for security and robustness, featuring a layered design and advanced concurrency control for financial operations.

⚙️ Key Technologies & Core Components
Component	Technology	Purpose
Framework	Spring Boot / Kotlin	Rapid development and expressive syntax.
Data Access	Spring Data JPA / Hibernate	Database persistence and ORM.
Security	JWT (JWS) + Spring Security	Stateless, token-based authentication and authorization.
Hashing	BCrypt	Secure password encoding (PasswordEncoder).
Concurrency	Pessimistic Locking & Serializable Isolation	Ensuring atomic and consistent financial transactions.
Token Handling	SHA-256	Refresh token hashing/encoding (TokenEncoder).
🔒 Security and Concurrency Features
Your project implements a highly secure and robust back-end, focusing on three critical areas:

1. Robust Transactional Safety (Concurrency Control)

Critical financial transactions (transfer, withdraw, deposit) utilize the following database controls to prevent race conditions, dirty reads, and lost updates:

@Lock(LockModeType.PESSIMISTIC_WRITE): Acquires an exclusive lock on the selected database rows during the transaction.

Isolation.SERIALIZABLE: Guarantees that concurrent transactions execute as if they occurred serially, providing the highest level of data integrity.

2. Stateless JWT Authentication

Access/Refresh Tokens: Uses short-lived Access Tokens for resource access and long-lived, hashed Refresh Tokens for renewal.

Security Configuration: Utilizes JwtAuthFilter and Spring Security to enforce STATELESS sessions and protect all necessary endpoints.

Password Storage: All passwords are securely stored using BCrypt hashing.

3. Comprehensive Global Error Handling

The GlobalExceptionHandler ensures that all API consumers receive clear, standardized JSON error responses based on the failure type:

401 UNAUTHORIZED: For invalid credentials or tokens (InvalidCredentialsException, InvalidTokenException).

403 FORBIDDEN: For failed business logic like insufficient funds (InsufficientMoneyException, InvalidTransactionException).

404 NOT FOUND: For missing resources (UserNotFoundException, AccountNotFoundException).

409 CONFLICT: For conflicting data (UserAlreadyExistsException, SamePasswordException).

🚀 How to Run the Application
(You must configure your database connection details and JWT secret in application.properties or equivalent.)

Prerequisites:

Java Development Kit (JDK) 17+

Maven (or Gradle)

A running Database instance (e.g., PostgreSQL).

Clone the repository:

Bash
git clone https://github.com/YOUR_USERNAME/Bank.git
cd Bank
Build and Run (Assuming Maven):

Bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
💡 API Endpoints & Authorization
Endpoint Pattern	Authorization	Purpose
/api/auth/**	permitAll() (Public)	Registration, Login, Forget Password, Refresh Token operations.
/api/auth/change-password	authenticated() (Requires Access Token)	Change user's password.
All other /api/**	authenticated() (Requires Access Token)	Account Management, Transactions (Transfer, Deposit, Withdraw).
