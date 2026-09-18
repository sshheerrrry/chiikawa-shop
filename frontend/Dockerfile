# 第一階段：使用 Node.js 建置 React
FROM node:22-alpine AS build

WORKDIR /app

# 先複製套件設定
COPY package*.json ./

# 安裝套件
RUN npm ci

# 複製 React 專案
COPY . .

# 建置正式版
RUN npm run build


# 第二階段：使用 Nginx 提供 React 網頁
FROM nginx:alpine

# 移除 Nginx 預設設定
RUN rm /etc/nginx/conf.d/default.conf

# 使用自己的 nginx.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 複製 Vite build 產生的 dist
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx Container 使用 80 port
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]