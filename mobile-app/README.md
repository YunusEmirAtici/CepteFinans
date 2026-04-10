# Mobile App (Android - Java)

Bu klasor Android Studio projesi icin temel iskelet dosyalarini icerir.

## Uygulanan Yapilar

- MVVM (Activity + Fragment + ViewModel)
- Retrofit ile API baglantisi
- RecyclerView + Material Design
- Repository katmani
- TabLayout + ViewPager2 ile sekmeli ekran
- Loading ve error state yonetimi
- API key header interceptor

## Akis

- `MainActivity` icinde `Users` ve `Products` sekmeleri vardir.
- `UserListViewModel` -> `UserRepository` -> `Retrofit(UserApi)` zinciri ile veri alinir.
- Sonuc `RecyclerView` uzerinde `UserAdapter` ile gosterilir.
- Benzer akis `ProductListViewModel` ve `ProductAdapter` icin de gecerlidir.

## Notlar

- Android emulator icin backend adresi `10.0.2.2:8080` olarak ayarlidir.
- Gerekirse `ApiClient` icindeki `BASE_URL` degistirilebilir.
- Varsayilan API key `dev-key` olarak gonderilir.
