@echo off
chcp 65001 >nul
title FinansCepte - Masaüstü Uygulaması Başlatıcı

echo ╔══════════════════════════════════════════════╗
echo ║   FinansCepte - Masaüstü Uygulaması         ║
echo ╚══════════════════════════════════════════════╝
echo.

REM Java kontrolü
java -version >nul 2>&1
if errorlevel 1 (
    echo [HATA] Java bulunamadı!
    echo Lütfen Java 17 veya üzeri yükleyin: https://adoptium.net/
    pause
    exit /b 1
)

echo [✓] Java kontrolü başarılı
echo.

REM Maven kontrolü
call mvn -version >nul 2>&1
if errorlevel 1 (
    echo [HATA] Maven bulunamadı!
    echo Lütfen Maven yükleyin: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo [✓] Maven kontrolü başarılı
echo.

REM Backend servislerinin çalışıp çalışmadığını kontrol et
echo [i] Backend servisleri kontrol ediliyor...
curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/users > %TEMP%\gateway_check.txt
set /p GATEWAY_STATUS=<%TEMP%\gateway_check.txt

if "%GATEWAY_STATUS%"=="200" (
    echo [✓] API Gateway erişilebilir (Port 8080)
) else (
    echo [!] UYARI: API Gateway erişilemiyor (Port 8080)
    echo [!] Backend servislerinin çalıştığından emin olun.
    echo [!] Docker ile başlatmak için: docker compose up -d
    echo.
    choice /C HY /N /M "Yine de devam etmek istiyor musunuz? (H=Hayır, Y=Evet)"
    if errorlevel 2 goto :CONTINUE
    if errorlevel 1 exit /b
)

:CONTINUE
echo.
echo [i] Masaüstü uygulaması başlatılıyor...
echo.

cd /d "%~dp0\desktop-app"

REM JavaFX modül yolunu belirle
set "JAVAFX_MODULES=javafx.controls,javafx.fxml"

REM Maven ile çalıştır
call mvn clean javafx:run

if errorlevel 1 (
    echo.
    echo [HATA] Uygulama başlatılamadı!
    echo.
    echo Olası çözümler:
    echo 1. JAVA_HOME ortam değişkeninin doğru ayarlandığından emin olun
    echo 2. JavaFX SDK yüklü değilse, pom.xml'de javafx versiyonunu kontrol edin
    echo 3. Maven bağımlılıkları eksikse: mvn clean install
    echo.
    pause
    exit /b 1
)

echo.
echo [✓] Uygulama kapatıldı.
pause
