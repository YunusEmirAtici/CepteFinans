# CepteFinans - Proje Geli┼ştirme Yol Haritas─▒

**Hedef:** TBL324 ─░leri Java Uygulamalar─▒ - 100 Puan
**Mevcut Tahmini Puan:** ~67/100
**Hedeflenen Puan:** 85+

---

## 1. Genel Durum Analizi

### Tamamlanan Bile┼şenler (G├╝├ğl├╝ Temel)
- `common-lib`: Generic repository, ortak exception modelleri
- `api-gateway`: Spring Cloud Gateway y├Ânlendirmeleri
- `service-user`: JWT login/register, CRUD, testler, testcontainers
- `service-product`: Full CRUD, mapper, OpenAPI, exception handler, testler
- `transaction-service`: Full CRUD, testler
- `subscription-service`: Full CRUD, testler
- `mobile-app`: Android (Java) iskeleti - User/Product listesi
- Docker Compose ve Dockerfile'lar mevcut

### Kritik Eksiklikler
1. **JDBC Katman─▒ Yok** - Sadece MongoDB (NoSQL) kullan─▒l─▒yor. Puanlama hem JDBC hem NoSQL istiyor.
2. **Hata Y├Ânetimi Eksik** - Sadece `service-product`'ta GlobalExceptionHandler var.
3. **Custom GUI Yetersiz** - JavaFX sadece basit Canvas ├ğizimi ve health check i├ğeriyor.
4. **3 Servis ─░skelet** - Budget, Notification, Report sadece health endpoint'i bar─▒nd─▒r─▒yor.
5. **TDD Tarih Damgas─▒** - Testler yaz─▒l─▒m kodundan ├Ânce yaz─▒lmal─▒ ve versiyon kontrol├╝nde g├Âr├╝nmeli.

---

## 2. Fazlar ve G├Ârevler

### FAZ 1: JDBC Entegrasyonu + Hata Y├Ânetimi (T├╝m Servisler)
**Hedef Puan Art─▒┼ş─▒:** ~+8 puan
**├ûncelik:** Kritik

#### 1.1 JDBC + NoSQL Dual Repository (Puanlama: JDBC & NoSQL - 10 puan)
- **Se├ğilen Servis:** `service-user`
- **Ama├ğ:** Hem PostgreSQL/H2 (JDBC) hem MongoDB (NoSQL) deste─şi sa─şlamak
- **Yap─▒lacaklar:**
  - `service-user/pom.xml`'e `spring-boot-starter-data-jpa` ve `h2` (veya PostgreSQL driver) ba─ş─▒ml─▒l─▒─ş─▒ ekle
  - `com.finanscepte.user.repository.jdbc` paketi olu┼ştur
  - `JdbcUserRepository` interface'i olu┼ştur (`JpaRepository<UserEntityJDBC, Long>`)
  - `UserEntityJDBC` entity s─▒n─▒f─▒ olu┼ştur (`@Entity`, `@Table`)
  - `DualUserService` veya profile-based (`@Profile`) yap─▒land─▒rma ile repository se├ğimi
  - `application.yml`'de `spring.profiles.active` ile jdbc/mongo ge├ği┼şi
  - Unit test: `JdbcUserRepositoryTest` (H2 in-memory)
  - Entegrasyon testi: Her iki repository i├ğin CRUD testleri

#### 1.2 GlobalExceptionHandler (T├╝m Servislere) (Puanlama: Hata Y├Ânetimi - 5 puan)
- **Kopyalanacak Kaynak:** `service-product`'taki `GlobalExceptionHandler` ve `ResourceNotFoundException`
- **Hedef Servisler:**
  - `service-user`
  - `transaction-service`
  - `subscription-service`
  - `budget-service`
  - `notification-service`
  - `report-service`
- **Standart Error Response:**
  ```json
  {
    "timestamp": "2026-05-03T10:15:30",
    "status": 404,
    "error": "Not Found",
    "message": "User not found with id: 123",
    "path": "/api/users/123"
  }
  ```
- **Exception Tipleri:**
  - `ResourceNotFoundException` -> 404
  - `IllegalArgumentException` -> 400
  - `MethodArgumentNotValidException` -> 400
  - `Exception` (genel) -> 500

---

### FAZ 2: Budget, Notification, Report Servislerini Tamamla
**Hedef Puan Art─▒┼ş─▒:** ~+3 puan (API & Back-end tamamlanmas─▒)
**├ûncelik:** Y├╝ksek

#### 2.1 Budget Service
- **Entity:** `Budget` (id, userId, category, limitAmount, spentAmount, month, year, createdAt)
- **Repository:** `BudgetRepository extends MongoRepository<Budget, String>`
- **Service:** `BudgetService` + `BudgetServiceImpl`
  - CRUD operasyonlar─▒
  - `findByUserIdAndMonthAndYear()`
  - `checkBudgetLimit()` -> limit a┼ş─▒m─▒ kontrol├╝
- **Controller:** `BudgetController`
  - `GET /api/budgets`
  - `GET /api/budgets/{id}`
  - `POST /api/budgets`
  - `PUT /api/budgets/{id}`
  - `DELETE /api/budgets/{id}`
  - `GET /api/budgets/user/{userId}/status` -> b├╝t├ğe durumu ├Âzeti
- **DTO:** `BudgetRequest`, `BudgetResponse`
- **Test:** `BudgetServiceTest`, `BudgetControllerTest`

#### 2.2 Notification Service
- **Entity:** `Notification` (id, userId, type, message, read, createdAt)
- **Repository:** `NotificationRepository`
  - `findByUserIdAndReadFalse()`
- **Service:** `NotificationService`
  - CRUD
  - `markAsRead()`
  - `getUnreadNotifications()`
- **Controller:** `NotificationController`
  - Standart CRUD
  - `GET /api/notifications/user/{userId}/unread`
  - `PATCH /api/notifications/{id}/read`
- **Test:** `NotificationServiceTest`, `NotificationControllerTest`

#### 2.3 Report Service
- **Entity:** `Report` (id, userId, type, startDate, endDate, data, createdAt)
- **Tip:** Enum `ReportType` -> MONTHLY_SUMMARY, CATEGORY_BREAKDOWN
- **Service:** `ReportService`
  - `generateMonthlyReport(userId, month, year)`
  - `generateCategoryReport(userId, month, year)`
  - (Opsiyonel) PDF export i├ğin iText veya Apache PDFBox
- **Controller:** `ReportController`
  - `POST /api/reports/generate`
  - `GET /api/reports/user/{userId}`
  - `GET /api/reports/{id}`
- **Test:** `ReportServiceTest`

---

### FAZ 3: JavaFX Custom GUI Geli┼ştirme
**Hedef Puan Art─▒┼ş─▒:** ~+7 puan (Custom GUI - 10 puan)
**├ûncelik:** Y├╝ksek

#### 3.1 Mevcut Durum
- Sadece `main-view.fxml` var.
- `MainController` sadece API health check ve basit Canvas ├ğizimi yap─▒yor.

#### 3.2 Yap─▒lacak Ekranlar ve ├ûzellikler

**A. Login Ekran─▒ (`login-view.fxml`)**
- Kullan─▒c─▒ ad─▒ / ┼şifre giri┼şi
- API'ye login iste─şi, JWT token alma
- Token'─▒ local store etme

**B. Dashboard Ekran─▒ (`dashboard-view.fxml`)**
- **Custom Pie Chart (Canvas):** Kategori bazl─▒ harcamalar (gelir/gider)
  - Canvas ├╝zerinde elle ├ğizim (arc, renkler, legend)
  - Hover ile detay g├Âsterme (tooltip benzeri)
- **Custom Bar Chart (Canvas):** Ayl─▒k gelir-gider kar┼ş─▒la┼şt─▒rmas─▒
- **├ûzet Kartlar:** Toplam gelir, toplam gider, net bakiye

**C. Transaction Y├Ânetim Ekran─▒ (`transactions-view.fxml`)**
- Tablo (TableView) ile transaction listesi
- Yeni transaction ekleme formu
- D├╝zenleme / Silme butonlar─▒
- CRUD operasyonlar─▒ API ├╝zerinden

**D. Budget Y├Ânetim Ekran─▒ (`budgets-view.fxml`)**
- B├╝t├ğe limitleri listesi
- Progress bar (JavaFX ProgressBar veya Canvas custom)
- B├╝t├ğe a┼ş─▒m─▒ uyar─▒lar─▒ (k─▒rm─▒z─▒ renk, ikon)

**E. Navigation / Men├╝**
- Sol tarafta Sidebar (VBox + custom styling)
- Scene ge├ği┼şleri

#### 3.3 Teknik Detaylar
- `pom.xml`'e Retrofit veya HttpClient ba─ş─▒ml─▒l─▒─ş─▒
- `AuthManager` singleton - JWT token y├Ânetimi
- `ApiService` - Backend gateway ├ğa─şr─▒lar─▒
- CSS dosyas─▒ (`style.css`) - Dark theme, modern UI

---

### FAZ 4: TDD ve Test Coverage Art─▒r─▒m─▒
**Hedef Puan Art─▒┼ş─▒:** ~+5 puan (TDD - 10 puan)
**├ûncelik:** Orta-Y├╝ksek

#### 4.1 TDD S├╝reci (Red-Green-Refactor)
- **Kural:** Yeni feature'lar i├ğin ├ûNCE test yaz, SONRA implemente et
- Git commit tarih damgalar─▒ test dosyalar─▒n─▒n production kodundan ├ûNCE oldu─şunu g├Âstermeli

#### 4.2 Eksik Testler
- `service-user`: `UserControllerTest` eksik, sadece repository ve service testi var
- `transaction-service`: Controller testleri eksik
- `subscription-service`: Controller testleri eksik
- `budget-service`: T├╝m testler eksik
- `notification-service`: T├╝m testler eksik
- `report-service`: T├╝m testler eksik

#### 4.3 Test Yap─▒s─▒ (Her servis i├ğin)
```
src/test/java/com/finanscepte/xxx/
Ôö£ÔöÇÔöÇ controller/
Ôöé   ÔööÔöÇÔöÇ XxxControllerTest.java       (WebTestClient/MockMvc)
Ôö£ÔöÇÔöÇ service/
Ôöé   ÔööÔöÇÔöÇ XxxServiceTest.java          (JUnit + Mockito)
Ôö£ÔöÇÔöÇ repository/
Ôöé   ÔööÔöÇÔöÇ XxxRepositoryTest.java       (Testcontainers MongoDB)
ÔööÔöÇÔöÇ integration/
    ÔööÔöÇÔöÇ XxxIntegrationTest.java      (SpringBootTest)
```

#### 4.4 K6 Performans Testi Geni┼şletme
- `k6/gateway-load-test.js`:
  - User endpoint'leri
  - Product endpoint'leri
  - Transaction CRUD
  - Concurrent kullan─▒c─▒ sim├╝lasyonu
- Sonu├ğlar─▒n `docs/performance-report.md`'ye eklenmesi

---

### FAZ 5: Docker Compose Entegrasyon ve Dok├╝mantasyon
**Hedef Puan Art─▒┼ş─▒:** ~+4 puan (Docker + Analiz)
**├ûncelik:** Orta

#### 5.1 Docker Compose Kontrol├╝
- `docker-compose.yml` kontrol├╝:
  - MongoDB servisi
  - T├╝m backend servislerinin Dockerfile'lar─▒
  - Network yap─▒land─▒rmas─▒
  - Ortam de─şi┼şkenleri (MongoDB URI vs.)
- `docker compose up --build` tek komutla ├ğal─▒┼şmal─▒
- Health check'ler ekle

#### 5.2 Dok├╝mantasyon G├╝ncelleme
- `README.md`:
  - Ba┼şlang─▒├ğ rehberini g├╝ncelle (t├╝m servisler)
  - Ekran g├Âr├╝nt├╝leri (JavaFX, Android)
  - API endpoint listesi
- `docs/architecture.md`:
  - G├╝ncellenmi┼ş Mermaid diagram (t├╝m servisler)
  - Teknoloji stack tablosu
- `docs/performance-report.md`:
  - k6 test sonu├ğlar─▒
  - Response time, throughput, hata oranlar─▒

---

### FAZ 6: SOLID ve Design Patterns ─░yile┼ştirme
**Hedef Puan Art─▒┼ş─▒:** ~+2 puan (SOLID & OOP)
**├ûncelik:** Orta

#### 6.1 Mevcut Durum
- **S**RP: Servisler katmanl─▒, iyi durumda
- **O**CP: Interface'ler var ama daha fazla polymorphism kullan─▒labilir
- **L**SP: Hen├╝z inheritance hierarchy yok
- **I**SP: Interface'ler uygun b├╝y├╝kl├╝kte
- **D**IP: DI kullan─▒l─▒yor, iyi

#### 6.2 ─░yile┼ştirmeler
- **Strategy Pattern:** Report generation (PDF, JSON, CSV formatlar─▒)
- **Observer Pattern:** Notification system (Event-driven)
- **Factory Pattern:** Report type creation
- **Template Method:** Generic CRUD service template (common-lib'e ta┼ş─▒)

---

## 3. Puanlama Tahmini (Plan Sonras─▒)

| Kriter | Mevcut | Plan Sonras─▒ | Fark |
|--------|--------|-------------|------|
| API & Back-end | ~7 | 10 | +3 |
| Generic Yap─▒lar | ~7 | 9 | +2 |
| Custom GUI | ~3 | 9 | +6 |
| JDBC & NoSQL | ~5 | 9 | +4 |
| SOLID & OOP | ~8 | 10 | +2 |
| Hata Y├Ânetimi | ~2 | 5 | +3 |
| Performans Testleri | ~3 | 5 | +2 |
| Analiz & Dok├╝man | ~4 | 5 | +1 |
| Mikroservis Mimarisi | +10 | +10 | 0 |
| Gateway | +5 | +5 | 0 |
| Mobil GUI | +5 | +5 | 0 |
| Test-Driven Geli┼ştirme | ~5 | +10 | +5 |
| Dockerize Sistem | ~3 | +5 | +2 |
| **Toplam** | **~67** | **~92** | **+25** |

---

## 4. ├çal─▒┼şma Takvimi (├ûneri)

| Hafta | Faz | G├Ârevler | Hedef Puan |
|-------|-----|----------|-----------|
| 1 | Faz 1 | JDBC entegrasyonu (service-user) + GlobalExceptionHandler (t├╝m servisler) | +8 |
| 2 | Faz 2 | Budget/Notification/Report CRUD + Testleri | +3 |
| 3 | Faz 3 | JavaFX Login + Dashboard + Custom Charts | +7 |
| 4 | Faz 3 (devam) | JavaFX Transaction/Budget ekranlar─▒ | - |
| 5 | Faz 4 | Eksik testler + TDD yeni feature'lar | +5 |
| 6 | Faz 5 | Docker Compose + Dok├╝mantasyon | +4 |
| 7 | Faz 6 | SOLID/Design Patterns + Final kontrol | +2 |

---

## 5. Acil Ba┼şlang─▒├ğ G├Ârevleri (Bug├╝n)

1. [ ] `PLAN.md` dosyas─▒n─▒ oku ve onayla
2. [ ] Faz 1 - Ad─▒m 1.1: `service-user/pom.xml`'e JPA + H2 ekle
3. [ ] Faz 1 - Ad─▒m 1.2: `service-user`'da JDBC entity + repository olu┼ştur
4. [ ] Faz 1 - Ad─▒m 1.3: `service-product`'taki exception handler'─▒ `common-lib`'e ta┼ş─▒ ve t├╝m servislere uygula

---

## 6. Notlar

- **Git commit stratejisi:** Her feature i├ğin ayr─▒ commit. Test dosyalar─▒ ilk commit'te olmal─▒.
- **Branch stratejisi:** `main`'e do─şrudan push yerine `feature/faz-1-jdbc`, `feature/faz-2-budget` gibi branch'ler.
- **Kod kalitesi:** Her servis `service-product` kalitesinde olmal─▒ (mapper, DTO, exception handler, OpenAPI).
- **API Gateway:** Yeni servisler eklendik├ğe `application.yml`'de route tan─▒mlar─▒n─▒ g├╝ncelle.

**Planlama Tarihi:** 03.05.2026
**Hedef Tamamlanma:** 24.05.2026 (3 hafta)
