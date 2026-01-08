# Sprint-1 Backend (Inventory)

Bu proje **Spring Boot 3.4.11** ile oluşturuldu. İçerikler:
- Spring Web, Spring Data JPA, PostgreSQL Driver, Lombok, Actuator
- SpringDoc OpenAPI (Swagger UI) — `/swagger-ui/index.html`
- Örnek `Product` entity + CRUD başlangıcı (list, create)

## 1) Klasör
Bu dosyayı zip'ten çıkardığında kök klasör: `backend`

## 2) Docker ile PostgreSQL (5432)
> 5432 portu doluysa önce boşa çıkarın ya da `ports` değerini değiştirin.

```bash
# 📁 Klasör: backend
docker compose up -d
docker ps
```

## 3) Uygulamayı çalıştırma
```bash
# 📁 Klasör: backend
mvn -q clean package -DskipTests
mvn spring-boot:run
```

## 4) Test
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Health:     http://localhost:8080/actuator/health

## 5) DB Ayarları
`src/main/resources/application.properties` dosyasındaki bilgiler Docker compose ile eşleşiyor:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/inventorydb
spring.datasource.username=inventory
spring.datasource.password=inventory123
```
