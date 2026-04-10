# Mimari Dokumani

## Genel Bakis

FinansCepte, mikroservis mimarisi ile gelistirilmistir. Her servis kendi sorumluluguna ve MongoDB verisine sahiptir.

## Bilesenler

- API Gateway (`api-gateway`)
- User Service (`service-user`)
- Product Service (`service-product`)
- Transaction Service (`transaction-service`)
- Subscription Service (`subscription-service`)
- Budget Service (`budget-service`)
- Notification Service (`notification-service`)
- Report Service (`report-service`)
- Ortak kutuphane (`common-lib`)
- JavaFX masaustu istemcisi
- Android mobil istemci

## Mermaid - Sistem Mimarisi

```mermaid
flowchart LR
    D[Desktop App - JavaFX] --> G[API Gateway]
    M[Mobile App - Android] --> G
    G --> U[User Service]
    G --> P[Product Service]
    G --> T[Transaction Service]
    G --> S[Subscription Service]
    G --> B[Budget Service]
    G --> N[Notification Service]
    G --> R[Report Service]
    U --> MU[(MongoDB - user db)]
    P --> MP[(MongoDB - product db)]
```

## Katmanli Yapi (Ornek: User Service)

- controller: REST endpointler
- service: is kurallari
- repository: data erisim katmani
- model: entity/DTO
- config: teknik konfig
- exception: hata yonetimi
- util: yardimci siniflar
