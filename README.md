# Chiikawa Shop

吉伊卡哇主題的前後端分離電商網站專案。

本專案使用 **Spring Boot** 建立 RESTful API、**React + Vite**
建立前端介面、**MySQL** 儲存資料，並使用 **Docker Compose**
整合前端、後端與資料庫環境。

> 本專案為個人學習與作品集用途，非官方吉伊卡哇網站。

## 專案功能

### 使用者功能

* 瀏覽商品不需登入
* 會員註冊與登入
* JWT 身分驗證
* 商品名稱與類別搜尋
* 加入、查看、修改及刪除購物車商品
* 購物車結帳
* 查看歷史訂單

### 管理員功能

* 管理員登入
* 新增商品
* 查詢商品
* 修改商品
* 刪除商品

## 技術架構

### Backend

* Java 17
* Spring Boot 3
* Spring Web
* Spring Data JPA
* MySQL
* JWT Authentication
* Maven

### Frontend

* React
* Vite
* React Router
* Axios
* Nginx

### Database

* MySQL
* SQL 初始化腳本

### DevOps

* Docker
* Docker Compose
* Dockerfile
* `.dockerignore`

## 系統架構

``` text
React + Vite
     │
     │ HTTP / RESTful API
     ▼
Spring Boot
     │
     ├── Controller
     │
     ├── Service
     │
     ├── ServiceImpl
     │
     ├── DAO
     │
     ├── DaoImpl
     │
     └── Repository
             │
             ▼
           MySQL
```

Docker Compose 負責整合：

``` text
Docker Compose
├── Frontend (React + Nginx)
├── Backend (Spring Boot)
└── Database (MySQL)
    └── database/init.sql
```

## 專案結構

``` text
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
│   ├── package-lock.json
│   └── vite.config.js
│
├── .gitignore
├── docker-compose.yml
└── README.md
```

## Docker 啟動

### 1. Clone 專案

``` bash
git clone <your-repository-url>
cd chiikawa-shop
```

### 2. 建置並啟動

在專案根目錄執行：

``` bash
docker compose up --build
```

Docker Compose 會啟動前端、Spring Boot 後端及 MySQL。

MySQL 容器初始化時會使用：

``` text
database/init.sql
```

建立專案所需的資料庫結構與初始化資料。

### 3. 背景執行

``` bash
docker compose up -d --build
```

### 4. 查看容器

``` bash
docker compose ps
```

### 5. 停止服務

``` bash
docker compose down
```

若需要同時移除 Docker Volume：

``` bash
docker compose down -v
```

> `docker compose down -v` 會刪除資料庫 Volume
> 中的資料，使用前請確認是否需要保留資料。

## 本機開發

### Backend

進入後端：

``` bash
cd backend
```

啟動 Spring Boot：

``` bash
mvn spring-boot:run
```

預設後端服務：

``` text
http://localhost:8080
```

### Frontend

進入前端：

``` bash
cd frontend
```

安裝 dependencies：

``` bash
npm install
```

啟動 Vite：

``` bash
npm run dev
```

預設前端服務：

``` text
http://localhost:5173
```

## API 概覽

### 商品 Products

``` http
GET /api/products
GET /api/products/{id}
GET /api/products/search?name=吉伊卡哇&category=PLUSH
POST /api/admin/products
PUT /api/admin/products/{id}
DELETE /api/admin/products/{id}
```

### 會員 / Authentication

``` http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
GET /api/auth/me
```

### 購物車 Cart

``` http
GET /api/cart
POST /api/cart/items
PUT /api/cart/items/{id}
DELETE /api/cart/items/{id}
```

### 訂單 Orders

``` http
POST /api/orders/checkout
GET /api/orders/history
```

> 完整 API 請以目前專案 Controller 實作為準。

## JWT Authentication

登入成功後，後端會回傳 JWT。

前端將 Token 儲存在：

``` javascript
localStorage\['chiikawa\_token']
```

呼叫需要會員身分的 API 時，Axios 會在 Request Header 加入：

``` http
Authorization: Bearer <JWT>
```

## 圖片

商品資料保留 `imageUrl` 欄位。

專案中的靜態圖片可放置於：

``` text
frontend/public/images/
```

若使用後端圖片上傳功能，相關檔案則位於：

``` text
backend/uploads/
```

## Database 初始化

Docker 環境使用：

``` text
database/init.sql
```

進行 MySQL 初始化。

若不使用 Docker，也可以自行建立 MySQL Database，再依本機環境設定 Spring
Boot 的資料庫連線資訊。

## Git Ignore

專案已排除不需要提交至 GitHub 的建置檔與開發環境資料，例如：

``` text
backend/target/
frontend/node_modules/
frontend/dist/
.idea/
.vscode/
.settings/
.classpath
.project
.env
*.log
```

## 專案目的

本專案主要用於練習與整合 Java Web 後端開發技術，包括：

* Spring Boot RESTful API
* 前後端分離架構
* React 與後端 API 串接
* JPA / MySQL 資料存取
* JWT 登入驗證
* 電商購物車與訂單流程
* Docker 容器化
* Docker Compose 多服務整合
* MySQL 自動初始化

## 後續可改善項目

* 增加自動化測試
* 完善錯誤處理與統一 API Response
* 強化輸入資料驗證
* 改善權限管理
* 增加 CI/CD
* 部署至雲端環境

## Disclaimer

本專案僅供程式開發學習與作品展示使用。角色名稱及相關智慧財產權屬於其原權利人，本專案與官方品牌無關。

