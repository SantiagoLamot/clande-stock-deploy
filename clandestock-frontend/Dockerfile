# Build de React/Vite
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# Nginx de producción
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Si usas CRA, cámbialo a: /app/build
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
