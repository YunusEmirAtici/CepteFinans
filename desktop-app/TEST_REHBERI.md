# FinansCepte Masaustu Uygulamasi - Test Rehberi

Bu rehber, JavaFX masaustu uygulamasini kendi bilgisayarinizda nasil calistirip test edeceginizi adim adim anlatir.

---

## Gereksinimler

| Arac | Minimum Versiyon | Indirme Linki |
|------|-----------------|---------------|
| Java JDK | 17+ | https://adoptium.net/ |
| Maven | 3.9+ | https://maven.apache.org/download.cgi |
| Docker Desktop | 4.x+ | https://www.docker.com/products/docker-desktop/ |
| Git | 2.x+ | https://git-scm.com/downloads |

---

## Adim 1: Projeyi Klonlayin

```bash
git clone <proje-url>
cd CepteFinans-main
```

---

## Adim 2: Backend Servislerini Baslat (Docker)

En kolay yol tum sistemi Docker Compose ile ayaga kaldirmaktir:

```bash
# Tum servisleri baslat (MongoDB + 7 mikroservis + Gateway)
docker compose up --build -d

# Servislerin hazir olmasini bekle (yaklasik 2-3 dakika)
docker compose ps

# Loglari kontrol et
docker compose logs -f api-gateway
```

**Alternatif:** Sadece MongoDB baslatip, servisleri IDE'den calistirabilirsiniz:

```bash
# Sadece veritabani
docker compose up -d mongodb

# Ardindan IntelliJ/Eclipse ile her servisi ayri ayri calistirin:
# 1. service-user (port 8081)
# 2. service-product (port 8082)
# 3. transaction-service (port 8083)
# 4. subscription-service (port 8084)
# 5. budget-service (port 8085)
# 6. notification-service (port 8086)
# 7. report-service (port 8087)
# 8. api-gateway (port 8080)
```

### Saglik Kontrolu

Tum servisler calistiktan sonra asagidaki URL'leri tarayicida acarak kontrol edin:

```
http://localhost:8081/api/users/health    -> User Service
http://localhost:8085/api/budgets/health  -> Budget Service
http://localhost:8080/api/users           -> Gateway uzerinden users
```

**Basarili yanit:**
```json
{"service":"budget-service","status":"ok"}
```

---

## Adim 3: Test Verisi Olusturun

Postman, curl veya tarayici ile asagidaki istekleri gondererek test verisi olusturun:

### 3.1 Kullanici Olustur
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{"username":"testuser","email":"test@test.com"}'
```

### 3.2 Islem (Transaction) Ekle
```bash
# Gelir
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "amount":5000,
    "type":"INCOME",
    "category":"Salary",
    "transactionDate":"2026-05-01",
    "description":"Mayis maasi",
    "recurring":false
  }'

# Gider - Food
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "amount":800,
    "type":"EXPENSE",
    "category":"Food",
    "transactionDate":"2026-05-03",
    "description":"Market alisverisi",
    "recurring":false
  }'

# Gider - Transport
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "amount":300,
    "type":"EXPENSE",
    "category":"Transport",
    "transactionDate":"2026-05-02",
    "description":"Toplu tasima",
    "recurring":false
  }'
```

### 3.3 Butce Olustur
```bash
# Food icin limit 1000 TL, harcanan 800 TL (limit icinde)
curl -X POST http://localhost:8080/api/budgets \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "category":"Food",
    "limitAmount":1000,
    "spentAmount":800,
    "month":5,
    "year":2026
  }'

# Entertainment icin limit 500 TL, harcanan 600 TL (limit asildi!)
curl -X POST http://localhost:8080/api/budgets \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "category":"Entertainment",
    "limitAmount":500,
    "spentAmount":600,
    "month":5,
    "year":2026
  }'
```

### 3.4 Bildirim Olustur
```bash
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-key" \
  -d '{
    "userId":"testuser",
    "type":"ALERT",
    "message":"Butce limiti asildi!"
  }'
```

---

## Adim 4: JavaFX Uygulamasini Calistir

### 4.1 Maven ile Calistir (Tercih Edilen)

```bash
cd desktop-app

# JavaFX plugin ile calistir
mvn clean javafx:run
```

### 4.2 Alternatif: JAR Olustur ve Calistir

```bash
cd desktop-app

# Derle
mvn clean package

# Calistir (JavaFX modulleri ile)
java --module-path %JAVA_FX_PATH% --add-modules javafx.controls,javafx.fxml -jar target/desktop-app-0.0.1-SNAPSHOT.jar
```

**Windows icin JAVA_FX_PATH ornegi:**
```bash
set JAVA_FX_PATH=C:\Program Files\Java\javafx-sdk-21.0.2\lib
java --module-path "%JAVA_FX_PATH%" --add-modules javafx.controls,javafx.fxml -jar target/desktop-app-0.0.1-SNAPSHOT.jar
```

### 4.3 IntelliJ IDEA ile Calistir

1. `desktop-app/pom.xml`'i Maven projesi olarak import edin
2. `DesktopApp.java`'a sag tiklayin → "Run 'DesktopApp.main()'"
3. **VM Options** olarak ekleyin:
   ```
   --module-path "C:\Program Files\Java\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml
   ```

---

## Adim 5: Uygulamayi Test Et

### 5.1 Login Ekrani
1. Uygulama acildiginda **giris ekrani** gelir
2. Email ve sifre alanlari mevcuttur (simdilik backend login endpoint'i basit calistigi icin herhangi bir deger girebilirsiniz)
3. "Giris Yap" butonuna tiklayin

### 5.2 Dashboard Ekrani
Basarili giris sonrasi **Dashboard** acilir:

| Bilesen | Beklenen Gorunum |
|---------|-----------------|
| **Ust Bar** | Hosgeldin mesaji + Yenile/Cikis butonlari |
| **Ozet Kartlar** | Toplam Gelir (yesil), Toplam Gider (kirmizi), Net Bakiye |
| **Pasta Grafigi** | Kategorilere gore harcama dagilimi (Food, Transport, vb.) |
| **Bar Grafigi** | Aylik gelir-gider karsilastirmasi |
| **Butce Uyarlari** | "Entertainment butcesi asildi!" gibi kirmizi uyari mesajlari |

### 5.3 Yenile Butonu
- "Yenile" butonuna tiklayinca API'den en son veriler cekilir
- Grafikler ve kartlar guncellenir

### 5.4 Cikis
- "Cikis Yap" butonu ile login ekranina donulur

---

## Adim 6: Sorun Giderme

### Sorun: `javafx.controls` modulu bulunamadi
**Cozum:** JavaFX SDK'yi indirip `module-path`'i dogru ayarlayin.

### Sorun: `Connection refused` hatasi
**Cozum:** Backend servislerinin calistigindan emin olun:
```bash
docker compose ps
# Tum servislerin STATUS'u Up olmali
```

### Sorun: Dashboard bos geliyor (grafikler yok)
**Cozum:** Test verisi olusturmadiniz. Adim 3'teki curl isteklerini calistirin.

### Sorun: Login basarisiz
**Cozum:** `service-user` servisinin calistigini kontrol edin. Simdilik login endpoint'i basit implementasyondur (gercek sifre kontrolu yok).

---

## Hizli Test Kontrol Listesi

- [ ] Docker Compose tum servisleri baslatti
- [ ] http://localhost:8080/api/users erisilebilir
- [ ] Test verisi olusturuldu (transaction, budget, notification)
- [ ] JavaFX uygulamasi acildi
- [ ] Login ekrani gorundu
- [ ] Dashboard'a gecis yapildi
- [ ] Ozet kartlarda degerler gorunuyor
- [ ] Pasta grafigi kategorileri gosteriyor
- [ ] Bar grafigi aylik karsilastirma yapıyor
- [ ] Butce uyari mesajlari gorunuyor
- [ ] Yenile butonu calisiyor
- [ ] Cikis butonu calisiyor

---

## K6 Performans Testi (Opsiyonel)

k6 yuklu ise performans testini calistirin:

```bash
k6 run --env BASE_URL=http://localhost:8080 k6/gateway-load-test.js
```

Sonuclar `docs/performance-report.md` dosyasina islenebilir.
