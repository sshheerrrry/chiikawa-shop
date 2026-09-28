# Chiikawa Shop

吉伊卡哇主題的前後端分離電商網站專案。

本專案以 **Spring Boot + React + MySQL** 建立完整電商流程，從會員登入、商品瀏覽、收藏、購物車、結帳、訂單、評價到收件地址管理，並提供管理員商品後台。

後端採用分層架構，整合 **Spring Data JPA、MyBatis、JWT RS256、Access / Refresh Token、Transaction、JasperReports**，最後使用 **Docker Compose** 整合 Frontend、Backend 與 MySQL。

> 本專案為個人學習與作品集用途，非官方吉伊卡哇網站。

---

## 專案特色

- React + Spring Boot 前後端分離
- RESTful API
- JWT RS256 身分驗證
- Access Token / Refresh Token
- USER / ADMIN 權限區分
- Spring Data JPA + MyBatis
- MySQL
- Transaction 結帳流程
- 商品上下架 / Active 狀態管理
- 商品圖片上傳
- 收藏、評價、地址管理
- JasperReports 商品 PDF 匯出
- Docker / Docker Compose
- MySQL 初始化腳本

---

## 核心功能

### 使用者功能

- 會員註冊與登入
- JWT 身分驗證
- Access Token / Refresh Token
- 瀏覽商品不需登入
- 商品搜尋與分頁
- 依系列、角色搜尋商品
- 加入 / 移除收藏
- 加入購物車
- 修改購物車數量
- 刪除購物車商品
- 商品下架後禁止修改數量或結帳
- Checkout
- 選擇收件地址
- 選擇付款方式
  - 信用卡
  - 貨到付款
- 查看歷史訂單
- 已購買商品才可評價
- 同一會員對同一商品只能評價一次
- 查看商品平均星等與評價數
- 收件地址 CRUD
- 設定預設地址

### 管理員功能

- 管理員權限驗證
- 查詢商品
- 新增商品
- 修改商品
- 商品圖片上傳
- 商品上下架
- 透過 active 狀態管理商品
- 商品搜尋與分頁
- 系列清單自動取得
- 商品系列與角色重複檢查
- 自動產生 / 更新商品描述
- JasperReports 匯出商品 PDF

---

## 系統架構

```text
React + Vite
     │
     │ HTTP / RESTful API
     ▼
Spring Boot
     │
     ├── Controller
     │
     ├── DTO
     │
     ├── Service
     │
     ├── ServiceImpl
     │
     ├── DAO
     │     ├── JPA DAO
     │     └── MyBatis DAO
     │
     ├── Repository
     │
     └── Mapper
             │
             ▼
           MySQL
```

### Backend 分層

```text
Request
   ↓
Controller
   ↓
Service / ServiceImpl
   ↓
DAO
 ├─ JPA → Repository
 └─ MyBatis → Mapper
   ↓
MySQL
   ↓
DTO / Response
   ↓
Frontend
```

Controller 負責 HTTP Request / Response，Service 負責商業邏輯，DAO / Repository / Mapper 負責資料存取，避免商業邏輯與資料庫操作集中於 Controller。

---

## 技術架構

### Backend

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate
- MyBatis
- Maven
- RESTful API
- JWT
- RS256
- Access Token / Refresh Token
- Transaction
- JasperReports

### Frontend

- React
- Vite
- React Router
- Axios
- HTML / CSS
- JavaScript
- Nginx

### Database

- MySQL
- JPA / Hibernate
- MyBatis
- SQL initialization script

### DevOps / Tools

- Docker
- Docker Compose
- Dockerfile
- `.dockerignore`
- Git / GitHub
- Postman
- Eclipse
- VS Code

---

## JWT Authentication

登入成功後，Backend 會產生：

```text
Access Token
+
Refresh Token
```

需要會員身分的 API：

```http
Authorization: Bearer <Access Token>
```

JWT 使用 **RS256 非對稱簽章**。

```text
Private Key
    ↓
Sign JWT
    ↓
Access Token
    ↓
Public Key
    ↓
Verify JWT
```

目前作品集版本在 Spring Boot 啟動時使用 Java `KeyPairGenerator` 動態建立 2048-bit RSA Key Pair，因此 Private Key 不會直接儲存在 Repository。

Backend 重新啟動後會產生新的 Key Pair，因此舊 Token 會失效；正式環境可改由安全的 Key Management 機制管理固定金鑰。

---

## Checkout 與 Transaction

Checkout 不只是建立一筆 Order，而是一個完整交易流程：

```text
Checkout Request
      ↓
驗證登入會員
      ↓
取得 Cart
      ↓
再次驗證商品 active 狀態
      ↓
建立 Order
      ↓
建立 OrderItem
      ↓
更新商品庫存
      ↓
清空 Cart
      ↓
Commit
```

Checkout 使用 Transaction 管理。

若建立訂單、建立明細、更新庫存或清空購物車其中任何步驟發生 Exception，交易可以 Rollback，避免留下部分成功的資料。

---

## 商品上下架與 Active 狀態

商品不直接從資料庫刪除，而是透過 `active` 狀態控制商品是否上架。

```text
active = 1 → 上架
active = 0 → 下架
```

原因是商品可能已經存在於歷史訂單、評價或其他關聯資料中。

公開商品查詢會排除 `active = 0` 的商品，管理員則可以重新上架商品。

---

## JPA + MyBatis

專案同時實作兩種資料存取方式：

### Spring Data JPA

適合：

- CRUD
- Entity Mapping
- Repository Query
- 分頁
- 一般條件搜尋

### MyBatis

適合：

- 自訂 SQL
- 明確控制查詢條件
- Mapper XML
- 複雜 SQL 練習

Product DAO 曾同時存在 JPA 與 MyBatis implementation，因此 Spring Dependency Injection 發生多 Bean 衝突，透過 `@Primary` / `@Qualifier` 明確指定 implementation。

---

## 主要資料模型

```text
User
 ├── Cart
 │    └── CartItem ─── Product
 │
 ├── CustomerOrder
 │    └── OrderItem ── Product
 │
 ├── Favorite ──────── Product
 │
 ├── Review ────────── Product
 │
 └── UserAddress

Product
 ├── CartItem
 ├── OrderItem
 ├── Favorite
 └── Review
```

主要資料包括：

- User
- Product
- Cart
- CartItem
- CustomerOrder
- OrderItem
- Favorite
- Review
- UserAddress

---

## 商品評價規則

商品評價除了前端 UI 控制之外，Backend 也會進行商業規則驗證。

```text
會員提出 Review Request
        ↓
確認是否購買過商品
        ↓
確認是否已評價
        ↓
建立 Review
```

重要商業規則不只依賴 React UI，而是由 Backend Service 再次驗證。

---

## Docker 架構

```text
Docker Compose
│
├── Frontend
│    └── React + Nginx
│
├── Backend
│    └── Spring Boot
│
└── Database
     └── MySQL
          └── database/init.sql
```

預設 Port：

```text
Frontend : 5173
Backend  : 8080
MySQL    : 3307 → 3306
```

MySQL 使用 Docker Volume 保存資料。

---

## Environment Variables

資料庫 Credential 不直接寫入 `docker-compose.yml`。

Docker Compose 使用：

```yaml
MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
DB_USERNAME: ${DB_USERNAME}
DB_PASSWORD: ${DB_PASSWORD}
```

Clone 專案後，可參考：

```text
.env.example
```

建立自己的：

```text
.env
```

例如：

```env
MYSQL_ROOT_PASSWORD=your_mysql_password
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

`.env` 已加入 `.gitignore`，不會提交至 Repository。

---

## Database 初始化

Docker 環境使用：

```text
database/init.sql
```

第一次建立新的 MySQL Data Directory 時，自動建立專案所需的資料庫結構與初始化資料。

初始化 SQL 與專案一起進行版本管理，而實際 MySQL 運行資料則保存於 Docker Volume。

---

## 專案結構

```text
chiikawa-shop/
│
├── backend/
│   ├── src/
│   ├── uploads/
│   ├── .dockerignore
│   ├── Dockerfile
│   └── pom.xml
│
├── database/
│   └── init.sql
│
├── frontend/
│   ├── public/
│   ├── src/
│   ├── .dockerignore
│   ├── Dockerfile
│   ├── index.html
│   ├── nginx.conf
│   ├── package.json
│   └── vite.config.js
│
├── .env.example
├── .gitignore
├── docker-compose.yml
└── README.md
```

---

## Docker 啟動

### 1. Clone Repository

```bash
git clone <your-repository-url>
cd chiikawa-shop
```

### 2. 建立 `.env`

參考：

```text
.env.example
```

建立：

```text
.env
```

並設定自己的 MySQL Credential。

### 3. 建置並啟動

```bash
docker compose up -d --build
```

### 4. 查看 Container

```bash
docker compose ps
```

### 5. 停止服務

```bash
docker compose stop
```

若要停止並移除 Container / Network：

```bash
docker compose down
```

> 一般只是暫停專案時可使用 `docker compose stop`。

若需要連同 Compose 管理的 Volume 一起移除：

```bash
docker compose down -v
```

> `docker compose down -v` 可能刪除資料庫 Volume 中的資料，執行前請確認是否需要保留資料。

---

## 本機開發

### Backend

```bash
cd backend
mvn spring-boot:run
```

Backend：

```text
http://localhost:8080
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend：

```text
http://localhost:5173
```

---

## API 功能概覽

### Authentication

| Method | Endpoint | 功能 |
|---|---|---|
| POST | `/api/auth/register` | 會員註冊 |
| POST | `/api/auth/login` | 會員登入並取得 Token |
| POST | `/api/auth/refresh` | 使用 Refresh Token 更新 Access Token |
| GET | `/api/auth/me` | 取得目前登入會員資訊 |
| POST | `/api/auth/logout` | 登出 |

### Products

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/products` | 取得商品列表 |
| GET | `/api/products/series` | 取得商品系列 |
| GET | `/api/products/{id}` | 取得單一商品 |
| GET | `/api/products/search` | 搜尋商品 |

### Admin Products

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/admin/products` | 管理員取得商品列表 |
| GET | `/api/admin/products/{id}` | 管理員取得單一商品 |
| POST | `/api/admin/products` | 新增商品 |
| PUT | `/api/admin/products/{id}` | 修改商品 |
| PATCH | `/api/admin/products/{id}/deactivate` | 商品下架 |
| PATCH | `/api/admin/products/{id}/activate` | 商品重新上架 |
| GET | `/api/admin/products/export/pdf` | 匯出商品 PDF |

### Admin Users

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/admin/users` | 管理員取得會員列表 |

### Images

| Method | Endpoint | 功能 |
|---|---|---|
| POST | `/api/admin/images/upload` | 上傳商品圖片 |

### Cart

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/cart` | 取得目前會員購物車 |
| POST | `/api/cart/items` | 加入商品至購物車 |
| PUT | `/api/cart/items/{id}` | 修改購物車商品數量 |
| DELETE | `/api/cart/items/{id}` | 刪除購物車商品 |

### Orders

| Method | Endpoint | 功能 |
|---|---|---|
| POST | `/api/orders/checkout` | 結帳並建立訂單 |
| GET | `/api/orders/history` | 取得目前會員歷史訂單 |

### Favorites

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/favorites` | 取得收藏商品 |
| POST | `/api/favorites/{productId}` | 加入收藏 |
| DELETE | `/api/favorites/{productId}` | 移除收藏 |
| GET | `/api/favorites/{productId}/status` | 查詢商品收藏狀態 |

### Product Reviews

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/products/{productId}/reviews` | 取得商品評價 |
| POST | `/api/products/{productId}/reviews` | 新增商品評價 |
| GET | `/api/products/{productId}/reviews/eligibility` | 查詢目前會員是否可評價 |
| GET | `/api/products/{productId}/reviews/summary` | 取得商品平均星等與評價統計 |

### User Addresses

| Method | Endpoint | 功能 |
|---|---|---|
| GET | `/api/addresses` | 取得目前會員的地址列表 |
| GET | `/api/addresses/{id}` | 取得單一地址 |
| POST | `/api/addresses` | 新增地址 |
| PUT | `/api/addresses/{id}` | 修改地址 |
| DELETE | `/api/addresses/{id}` | 刪除地址 |
| PATCH | `/api/addresses/{id}/default` | 設為預設地址 |

> 以上 Endpoint 依目前 Backend Controller 實作整理。

---

## 開發過程中的問題與改善

### 1. JPA / MyBatis Bean 衝突

**問題**

Product DAO 同時存在 JPA 與 MyBatis implementation，Spring 無法判斷應注入哪一個 Bean。

**改善**

使用 `@Primary` / `@Qualifier` 明確指定 implementation。

### 2. 商品刪除造成資料關聯問題

**問題**

商品可能已存在於歷史訂單，因此不適合直接從資料庫刪除。

**改善**

不直接刪除商品資料，改以 `active` 狀態控制商品上下架，保留歷史訂單與商品之間的資料關聯。

### 3. 下架商品仍存在購物車

**問題**

商品加入購物車後，管理員可能再將商品下架。

**改善**

修改購物車數量及 Checkout 時再次驗證商品狀態。

### 4. JWT Token 到期

**問題**

Access Token 到期後，Frontend UI 與 Backend 驗證狀態可能不同步。

**改善**

加入 Access / Refresh Token 流程，並處理 Authentication 狀態。

### 5. Credential 不應寫入 Repository

**問題**

開發初期資料庫密碼直接設定於 Docker Compose。

**改善**

改為 `.env` Environment Variables，Repository 僅保留 `.env.example`。

---

## 專案學習重點

透過本專案整合：

- Java / Spring Boot Backend
- RESTful API
- Controller / Service / DAO / Repository 分層
- DTO
- Spring Data JPA
- Hibernate
- MyBatis
- MySQL
- JWT RS256
- Access / Refresh Token
- Transaction
- 商品 Active 狀態管理
- React API 串接
- 電商購物車與 Checkout 流程
- Docker
- Docker Compose
- Database Initialization
- Git / GitHub

專案重點不只是完成 CRUD，而是將：

```text
登入
 ↓
商品
 ↓
收藏 / 購物車
 ↓
Checkout
 ↓
訂單
 ↓
評價
```

串成完整的電商資料流程。

---

## 後續可改善項目

- Automated Testing
- 統一 Exception Handling / API Response
- 強化 Input Validation
- 更完整的 Spring Security 權限管理
- RSA Key Management
- CI/CD
- Cloud Deployment

---

## Disclaimer

本專案僅供程式開發學習與作品展示使用。

角色名稱、圖片及相關智慧財產權屬於其原權利人，本專案與官方品牌無關。
