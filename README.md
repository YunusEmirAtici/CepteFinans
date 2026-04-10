# FinTrack - Kisisel Finans ve Abonelik Takip Sistemi

Bu repo, Java 17+ tabanli mikroservis mimarisinde gelistirilen FinTrack uygulamasinin adim adim gelistirilen kod tabanidir.

## Moduller

- `backend/common-lib`: Generic yapilar, ortak exception/model siniflari
- `backend/api-gateway`: Spring Cloud Gateway
- `backend/service-user`: Kullanici mikroservisi
- `backend/service-product`: Urun mikroservisi
- `backend/transaction-service`: Gelir/gider islem mikroservisi
- `backend/subscription-service`: Abonelik mikroservisi
- `backend/budget-service`: Butce mikroservisi
- `backend/notification-service`: Bildirim mikroservisi
- `backend/report-service`: Rapor mikroservisi
- `desktop-app`: JavaFX masaustu istemcisi
- `mobile-app`: Android (Java) istemci iskeleti
- `docs`: Mimari ve performans dokumanlari

## Teknolojiler

- Java 17
- Spring Boot 3
- Spring Data MongoDB
- Spring Cloud Gateway
- OpenAPI/Swagger (springdoc)
- JUnit 5 + Mockito
- Docker + Docker Compose
- JavaFX
- Android (Java + Retrofit)

## Hizli Baslangic

1. Docker servilerini kaldir:
   - `docker compose up -d mongodb`
2. Backend servislerini ayri terminallerde calistir:
   - `backend/service-user`
   - `backend/service-product`
   - `backend/api-gateway`
3. Gateway uzerinden endpoint test et:
   - `GET http://localhost:8080/api/users`
4. Swagger arayuzleri:
   - `http://localhost:8081/swagger-ui/index.html`
   - `http://localhost:8082/swagger-ui/index.html`

## TDD Yaklasimi

Her mikroserviste once test yaz, sonra implement et:

1. Red: Basarisiz test
2. Green: Minimum implementasyon
3. Refactor: Kod kalitesi iyilestirmesi

## Gelistirme Durumu

- Tamamlanan: `common-lib`, `service-user` (JWT login basic), `service-product`, `transaction-service`, `subscription-service`, `api-gateway`
- Iskeleti olusturulan: `budget-service`, `notification-service`, `report-service`
- Sonraki adim: budget servisini transaction/subscription seviyesine cikarmak

## Sonraki Adimlar
- Transaction/Subscription/Butce domain modellerinin CRUD implementasyonu
- Notification ve Report servislerinin is kurallarinin tamamlanmasi
- JavaFX ve Android ekranlarinin FinTrack senaryolarina genisletilmesi

## Performans Testi

- `k6/gateway-load-test.js` dosyasi ile gateway yuk testi calistirilabilir.
