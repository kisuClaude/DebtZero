# 💸 DebtZero

> **Smart personal debt management** — Not just tracking debts, but helping you understand the real risks of borrowing money.

[![CI/CD](https://github.com/kisuClaude/DebtZero/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/kisuClaude/DebtZero/actions/workflows/ci-cd.yml)
![Java](https://img.shields.io/badge/Java-23-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-ready-blue?logo=docker)

---

## 🎯 What is DebtZero?

Most debt management apps just record how much you owe. **DebtZero goes further** — it reveals the true cost of your debt.

**Real problem in Vietnam:** People borrow from informal lenders at "3,000 VND per million per day" and think it's cheap. DebtZero converts this to **108%/year** and shows you how much more you're paying compared to a bank loan.

### Key Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | JWT-based login with refresh token rotation |
| 📊 **Debt Management** | Track all types of debts with real interest rate calculation |
| 💰 **Interest Conversion** | Convert any interest format (daily/monthly/annual) to APR |
| ⚠️ **Risk Analysis** | Classify debt risk: SAFE / CAUTION / HIGH / DANGER |
| 📈 **Payment Priority** | Avalanche & Snowball strategies to suggest payoff order |
| 🔔 **Smart Warnings** | Alert when debt-to-income ratio exceeds 40% |
| 🏦 **Bank Comparison** | Show how much more you pay vs. standard bank rates |
| 📅 **Upcoming Payments** | List debts due in the next 7 days |

---

## 🏗️ Architecture

```
Client (Postman / Mobile App)
        │
        ▼
   REST API (Spring Boot)
        │
   ┌────┴────┐
   │         │
Security   Business Logic
(JWT)      (Debt / Payment / Analysis)
        │
        ▼
  PostgreSQL Database
```

### Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 23 |
| Framework | Spring Boot 3.x |
| Security | Spring Security + JWT (JJWT) |
| Database | PostgreSQL 17 |
| ORM | Spring Data JPA / Hibernate |
| Mapper | MapStruct |
| Validation | Bean Validation (Jakarta) |
| Testing | JUnit 5 + Mockito |
| Containerization | Docker + Docker Compose |
| CI/CD | GitHub Actions |

---

## 🔐 Security & Authentication

```
POST /api/auth/login
  → Returns: accessToken (1 day) + refreshToken (7 days)

POST /api/auth/refresh
  → Exchange refreshToken for new accessToken

POST /api/auth/logout
  → Invalidate refreshToken in database
```

- Access tokens are **stateless** (JWT) — verified by signature
- Refresh tokens are **stateful** — stored in DB, can be revoked instantly
- Passwords hashed with **BCrypt (strength 10)**
- Roles: `ROLE_USER` and `ROLE_ADMIN` (Many-to-Many)

---

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Login | No |
| POST | `/api/auth/refresh` | Refresh access token | No |
| POST | `/api/auth/logout` | Logout | No |

### Users
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/user/create-user` | Register new user | No |
| GET | `/api/user/all` | Get all users | ADMIN |

### Debts
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/debt/create` | Create new debt | USER |
| GET | `/api/debt/all` | Get all user's debts | USER |
| GET | `/api/debt/{id}` | Get debt by ID | USER |
| PATCH | `/api/debt/{id}/balance?amount=X` | Update remaining balance | USER |
| DELETE | `/api/debt/{id}` | Delete debt | USER |
| GET | `/api/debt/upcoming` | Debts due in 7 days | USER |

### Payments
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/api/payment/{debtId}/record` | Record a payment | USER |
| GET | `/api/payment/{debtId}/history` | Get payment history | USER |

### Analysis
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/api/analysis/overview` | Monthly debt overview | USER |
| GET | `/api/analysis/priority` | Debt payoff priority | USER |
| GET | `/api/analysis/warnings` | Smart warnings | USER |
| GET | `/api/analysis/bank-compare` | Compare vs bank rates | USER |

---

## 💡 Interest Rate Conversion

DebtZero converts any interest format to **Annual Percentage Rate (APR)**:

| Input Format | Example | APR |
|---|---|---|
| Daily per million VND | 3,000 VND/million/day | **108%/year** |
| Monthly percentage | 3%/month | **36%/year** |
| Annual percentage | 12%/year | 12%/year |
| Fixed monthly payment | 500,000 VND/month | N/A |

### Risk Classification

| APR | Risk Level | Label |
|---|---|---|
| < 15% | 🟢 | SAFE |
| 15% - 30% | 🟡 | CAUTION |
| 30% - 60% | 🟠 | HIGH |
| > 60% | 🔴 | DANGER |

---

## 🚀 Getting Started

### Prerequisites
- Java 23+
- Docker & Docker Compose
- PostgreSQL 17 (if running locally)

### Run with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/kisuClaude/DebtZero.git
cd DebtZero

# 2. Create environment file
cp .env.example .env
# Edit .env with your values

# 3. Start all services
docker compose up --build
```

App will be available at `http://localhost:8080`

### Run Locally

```bash
# 1. Clone and configure
git clone https://github.com/kisuClaude/DebtZero.git
cd DebtZero

# 2. Set environment variables
export JWT_SECRET=your_jwt_secret
export ADMIN_PASSWORD=your_admin_password
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password

# 3. Run
./mvnw spring-boot:run
```

### Environment Variables

| Variable | Description | Required |
|---|---|---|
| `JWT_SECRET` | Base64 encoded secret (min 256-bit) | ✅ |
| `ADMIN_PASSWORD` | Default admin password | ✅ |
| `DB_USER` | PostgreSQL username | ✅ |
| `DB_PASSWORD` | PostgreSQL password | ✅ |
| `JWT_EXPIRATION` | Access token TTL (ms) | Default: 86400000 |
| `JWT_REFRESH_EXPIRATION` | Refresh token TTL (ms) | Default: 604800000 |

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Test coverage includes:
# - AuthenticationService (login, invalid credentials)
# - DebtService (create, update balance, auto-mark as PAID)
# - PaymentService (record payment, late detection, error cases)
```

---

## 🗄️ Database Schema

```
users ──────────────────┐
  id (UUID)             │
  username              │
  email                 │
  password (BCrypt)     │
  monthly_income        │
  roles (Many-to-Many)  │
                        │
debts ──────────────────┘
  id (UUID)              user_id (FK)
  category (enum)
  name, platform
  principal_amount
  remaining_balance
  interest_input_type (enum)
  interest_input_value
  penalty_rate
  due_day, start_date, end_date
  status (ACTIVE/OVERDUE/PAID)

payment_history
  id (UUID)              debt_id (FK)
  amount_paid
  principal_paid, interest_paid
  payment_date
  is_late
```

---

## 🔄 CI/CD Pipeline

Every push to `main` branch triggers:

```
Push to main
    │
    ▼
┌─────────────┐     ┌──────────────────────┐
│  Run Tests  │────▶│ Build & Push Docker  │
│  (JUnit 5)  │     │ Image to Docker Hub  │
└─────────────┘     └──────────────────────┘
```

---

## 📁 Project Structure

```
src/
├── config/          # Security, JWT, DataInitializer
├── controller/      # REST endpoints
├── service/         # Business logic
├── entity/          # JPA entities
├── repository/      # Data access layer
├── dto/             # Request/Response DTOs
│   ├── request/
│   └── response/
├── enums/           # DebtCategory, DebtStatus, RiskLevel...
├── mapper/          # MapStruct mappers
├── exception/       # Global exception handling
└── utils/           # SecurityUtils
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is for educational and portfolio purposes.

---

<div align="center">
  <p>Built with ❤️ by <a href="https://github.com/kisuClaude">kisuClaude</a></p>
</div>
