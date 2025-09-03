# 🏦 Banking Application (Spring Boot)

A **Spring Boot banking application** for creating and managing bank accounts.  
Supports **account creation, deposits, withdrawals, transfers**, and maintains a **complete transaction history (audit trail)** for every balance change.  

---

## ✨ Features

- ✅ Create and manage bank accounts  
- ✅ Deposit and withdraw funds  
- ✅ Transfer money between accounts (transactional & atomic)  
- ✅ Record all balance changes (`Entry` entity)  
- ✅ Record transfers between accounts (`Transfer` entity)  
- ✅ RESTful APIs for managing accounts and transactions  
- ✅ Exception handling with meaningful error messages  
- ✅ Unit tests for services and controller endpoints  

---

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **Postgres**
- **Maven**
- **Lombok**
- **JUnit 5 & Mockito** for testing
- **MockMvc** for integration testing

## 📌 API Endpoints

### **Accounts**
- `POST /api/accounts` → Create account  
- `GET /api/accounts/{id}` → Get account by ID  
- `PUT /api/accounts/{id}` → Update account  
- `DELETE /api/accounts/{id}` → Delete account  

### **Transactions**
- `POST /api/accounts/{id}/deposit?amount=100` → Deposit money  
- `POST /api/accounts/{id}/withdraw?amount=50` → Withdraw money  
- `POST /api/accounts/transfer?fromAccountId=1&toAccountId=2&amount=200` → Transfer money  
- `GET /api/accounts/{id}/transactions` → Get account transaction history  

---

## ⚠️ Error Handling

The application provides descriptive errors via a **GlobalExceptionHandler**:

- `AccountNotFoundException` → `404 NOT FOUND`  
- `IllegalArgumentException` (invalid amount, insufficient funds, etc.) → `400 BAD REQUEST`  
- `MethodArgumentNotValidException` (invalid request body, validation errors) → `422 UNPROCESSABLE ENTITY`  

---

## 📖 Future Enhancements

- 🔒 Add authentication & authorization (Spring Security, JWT)  
- 🌍 Support multiple currencies with conversion  
- 📊 Add reporting endpoints (daily transactions, account statements)  
- ☁️ Deploy to cloud platforms (AWS / Azure / GCP)  
