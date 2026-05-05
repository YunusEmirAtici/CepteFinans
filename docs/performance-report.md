# Performans Test Raporu

**Tarih:** 03.05.2026
**Test Araci:** k6
**Hedef Sistem:** FinansCepte Mikroservis Mimarisi (Gateway uzerinden)

---

## 1. Test Ortami

| Bilesen | Versiyon / Konfigurasyon |
|---------|------------------------|
| API Gateway | Spring Cloud Gateway (port 8080) |
| User Service | Port 8081 |
| Product Service | Port 8082 |
| Transaction Service | Port 8083 |
| Subscription Service | Port 8084 |
| Budget Service | Port 8085 |
| Notification Service | Port 8086 |
| Report Service | Port 8087 |
| Veritabani | MongoDB 7.0 (Docker) |
| k6 | v0.50+ |

---

## 2. k6 Komutu

```bash
k6 run --env BASE_URL=http://localhost:8080 --env API_KEY=dev-key k6/gateway-load-test.js
```

---

## 3. Test Senaryolari

### 3.1 Yuk Profili (Load Stages)
- **Ramp-up:** 10 saniyede 10 VU -> 50 VU
- **Sabit yuk:** 50 VU ile 30 saniye
- **Ramp-down:** 10 saniyede 50 VU -> 0 VU

### 3.2 Test Edilen Endpoint'ler

| Servis | Metod | Endpoint | Aciklama |
|--------|-------|----------|----------|
| User Service | GET | /api/users | Tum kullanicilar |
| Budget Service | POST | /api/budgets | Butce olusturma |
| Notification Service | POST | /api/notifications | Bildirim olusturma |
| Report Service | POST | /api/reports | Rapor olusturma |
| Transaction Service | POST | /api/transactions | Islem olusturma |
| Subscription Service | POST | /api/subscriptions | Abonelik olusturma |
| All Services | GET | /api/{service}/health | Saglik kontrolu |

---

## 4. Beklenen Sonuclar (Metrik Sablonu)

Testi calistirdiktan sonra asagidaki tabloyu gercek degerlerle doldurun:

| Metrik | Hedef | Gerceklesen | Durum |
|--------|-------|-------------|-------|
| Toplam istek sayisi | - | ___________ | - |
| Basarili istek orani | > %95 | ___________ | - |
| p(95) response suresi | < 1000ms | ___________ | - |
| p(99) response suresi | < 2000ms | ___________ | - |
| Hata orani | < %5 | ___________ | - |
| Saniye basina istek (RPS) | - | ___________ | - |

---

## 5. Kirilma Noktasi Analizi

| Parametre | Deger |
|-----------|-------|
| Es zamanli kullanici sayisi (VU) | 50 |
| Tikanma noktasi | Belirlenmedi |
| Veritabani CPU kullanimi | ___________ |
| Gateway CPU kullanimi | ___________ |

---

## 6. Onerilen Iyilestirmeler

1. **Caching:** Sik okunan endpoint'ler (GET /api/users, GET /api/products) icin Redis onbellegi
2. **Rate Limiting:** Gateway uzerinde asiri istek korumasi
3. **Veritabani Index:** `userId` alani uzerinde index olusturulmasi
4. **Asenkron Islemler:** Bildirim ve rapor olusturma islemleri RabbitMQ/Kafka ile asenkron hale getirilebilir

---

## 7. Karsilastirmali Analiz

| Asama | p(95) Response | Hata Orani | Not |
|-------|----------------|------------|-----|
| Baslangic (10 VU) | ___________ | ___________ | - |
| Orta Yuk (30 VU) | ___________ | ___________ | - |
| Pik Yuk (50 VU) | ___________ | ___________ | - |

---

## 8. Sonuc

Test sonuclarina gore sistem ___________ VU'a kadar saglikli calismaktadir.

**Raporu Hazirlayan:** ___________
**Onaylayan:** ___________
