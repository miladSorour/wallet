
# 💰 Virtual Wallet

## 📘 Project Description

This is a production-grade **Virtual Wallet** system developed using Java, Spring Boot, JWT authentication, and Oracle DB. It supports core wallet operations like:

- 🔼 Top-Up
- 🔽 Withdraw
- 🔁 Transfer between users
- 📜 Transaction History

It includes:

- ✅ JWT-based Authentication (Spring Security)
- 📦 RESTful API with Swagger documentation
- 🐘 Oracle database integration
- 🐳 Docker support for deployment
- 📈 Built-in monitoring support with Prometheus for metrics and alerting
- 📂 Exception handling using Zalando’s Problem-Spring-Web
- 🧵 Thread-safe, concurrent transfer logic with Optimistic Locking (@Version)

---

## 🔄 Sequence Diagrams

### 1. Top-Up

```plaintext
User ──▶ Auth Service ──▶ JWT
User ──▶ Wallet API ──▶ WalletService ──▶ DB (+record)
```

### 2. Withdraw

```plaintext
User ──▶ Auth Service ──▶ JWT
User ──▶ Wallet API ──▶ WalletService ──▶ DB (balance check & update)
```

### 3. Transfer

```plaintext
User ──▶ Auth Service ──▶ JWT
User ──▶ Wallet API ──▶ WalletService
                  ├─▶ DB (sender balance check)
                  └─▶ DB (receiver balance update)
```

---

## 🧪 Swagger API Docs

Once the app is running, open your browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🐳 Run with Docker

1. **Clone the repository:**
   ```bash
   git clone https://github.com/miladSorour/wallet.git
   cd wallet
   ```

2. **Build the Docker image:**
   ```bash
   mvn compile -DskipTests=true com.google.cloud.tools:jib-maven-plugin:3.2.1:dockerBuild
   ```

3. **Run the Docker container:**
   ```bash
   docker run -p 8080:8080 wallet:latest
   ```

4. **Swagger will be available at:**
   ```http://localhost:8080/swagger-ui/index.html```

---

## Improvement

1. add Flyway db migration
2. add pessimistic lock for strong consistency
3. add GitHub action 
4. add cache layer 
5. add Role base access control (RBAC)
6. add freezing and blocking balance  
7. add hot account
8. horizontal sharding for wallet table
9. add audit to track the tables
