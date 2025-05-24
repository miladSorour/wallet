
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
## init database
1. **Init database**
``` sql
create table transaction
(
    id                 number(19, 0)      not null,
    created_by         number(19, 0)      not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    amount             float(53)          not null,
    detail             varchar2(255 char),
    timestamp          timestamp(6)       not null,
    type               varchar2(255 char) not null check (type in ('TOPUP', 'WITHDRAW', 'TRANSFER')),
    wallet_id          number(19, 0)      not null,
    primary key (id)
);
create table wallet
(
    id                 number(19, 0) not null,
    created_by         number(19, 0) not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    balance            float(53)     not null,
    version            number(19, 0),
    user_id            number(19, 0) not null,
    primary key (id)
);
create table wallet_user
(
    id                 number(19, 0)      not null,
    created_by         number(19, 0)      not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    enabled            number(1, 0)       not null check (enabled in (0, 1)),
    full_name          varchar2(255 char) not null,
    password           varchar2(255 char) not null,
    username           varchar2(255 char) not null,
    version            number(19, 0),
    primary key (id)
);
alter table wallet
    add constraint UKhgee4p1hiwadqinr0avxlq4eb unique (user_id);
alter table wallet_user
    add constraint UKegqusy937sbn54ed75mhknm71 unique (username);
alter table transaction
    add constraint FKtfwlfspv2h4wcgc9rjd1658a6 foreign key (wallet_id) references wallet;
alter table wallet
    add constraint FKmkf27omhr0j9t77rpxemavecp foreign key (user_id) references wallet_user;

create sequence tx_sequence_generator start with 1 increment by 1;
create sequence user_sequence_generator start with 1 increment by 1;
create sequence wallet_sequence_generator start with 1 increment by 1;

alter table wallet_user add account_non_locked number(1,0) default 1 not null check (account_non_locked in (0,1));
alter table wallet_user add failed_attempts number(10,0) default 0 not null;
alter table wallet_user add lock_time timestamp(6) with time zone;


create table authority (id number(19,0) not null, name varchar2(255 char) not null, primary key (id));
alter table authority add constraint UKjdeu5vgpb8k5ptsqhrvamuad2 unique (name);
create sequence authority_sequence_generator start with 1 increment by 1;

create table user_authority (user_id number(19,0) not null, authority_id number(19,0) not null, primary key (user_id, authority_id));
alter table user_authority add constraint FKgvxjs381k6f48d5d2yi11uh89 foreign key (authority_id) references authority;
alter table user_authority add constraint FKc7687bpykk86a2lgh28qonnk9 foreign key (user_id) references wallet_user;


insert into AUTHORITY (ID, NAME) values (1, 'deposit');
insert into AUTHORITY (ID, NAME) values (1, 'create:user');

``` 
2. **Generate default admin**

username : admin

password: A1a
``` sql
DECLARE
v_user_id long;
BEGIN
-- 1. Insert new user


    insert into WALLET_USER (id, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE, ENABLED, FULL_NAME,
                             PASSWORD, USERNAME, VERSION, ACCOUNT_NON_LOCKED, FAILED_ATTEMPTS, LOCK_TIME)
    values (24, 0, null, 0,
            null, 1, 'milad sorour',
            '$2a$10$DFkvYZ9DF7Okl/qm8VZl4ObXOGR/QjIQu/7JqfwMA1TX21rRwCdcu', 'admin',
            4, 1, 0, null)

    RETURNING id INTO v_user_id;

    -- 2. Assign all authorities to this user
    INSERT INTO user_authority (user_id, authority_id)
    SELECT v_user_id, ID
    FROM authority;
END;
/
```

---
## Improvement

1. add Flyway db migration
2. add pessimistic lock
3. add GitHub action 
4. add cache layer 
5. add Role base access control (RBAC)
6. add freezing and blocking balance  
7. add hot account
8. horizontal sharding for wallet table
9. add audit to track the tables
10. add mapstruct
11. add reverse api for the user wallet
