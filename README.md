# KassaRasa POS

Aplikasi Android **KassaRasa** (*Sistem Kasir Digital untuk UMKM Kuliner*) yang dibangun menggunakan **Kotlin** dan **Jetpack Compose (Material 3)**.

## Struktur Proyek

```text
Kassa_rasa/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/kassarasa/pos/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── model/PosModels.kt
│   │   │   │   ├── state/KassaRasaViewModel.kt
│   │   │   │   ├── navigation/AppNavGraph.kt
│   │   │   │   ├── screens/
│   │   │   │   │   ├── BerandaScreen.kt
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── RegisterScreen.kt
│   │   │   │   │   ├── DashboardScreen.kt
│   │   │   │   │   ├── KelolaMenuScreen.kt
│   │   │   │   │   ├── TransaksiScreen.kt
│   │   │   │   │   ├── PembayaranScreen.kt
│   │   │   │   │   ├── RiwayatTransaksiScreen.kt
│   │   │   │   │   └── LaporanScreen.kt
│   │   │   │   ├── components/
│   │   │   │   │   ├── CommonComponents.kt
│   │   │   │   │   ├── BottomNavBars.kt
│   │   │   │   │   ├── MenuDialogs.kt
│   │   │   │   │   └── InfoDialogs.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Type.kt
│   │   │   │       └── Theme.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── mipmap-anydpi-v26/
│   │   │   │   └── values/
│   │   │   └── AndroidManifest.xml
│   │   └── test/java/com/kassarasa/pos/KassaRasaViewModelTest.kt
│   └── build.gradle.kts
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Cara Membuka dan Menjalankan di Android Studio

1. Buka **Android Studio**.
2. Pilih **File > Open** (atau **Open** dari layar awal).
3. Arahkan ke folder `d:\kasa_rasa\Kassa_rasa` lalu klik **OK**.
4. Tunggu proses **Gradle Sync** selesai.
5. Buat atau pilih **Android Virtual Device (AVD)** melalui **Device Manager** (atau hubungkan perangkat Android dengan USB Debugging aktif).
6. Klik tombol **Run** (`Shift + F10`) untuk menginstal dan menjalankan aplikasi.

## Build via Command Line (PowerShell)

```powershell
cd d:\kasa_rasa\Kassa_rasa
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
```

## Akun Demo (Lokal)

- **Email / Username**: `admin@kassarasa.id`
- **Password**: `password123`
- Anda juga dapat mendaftarkan akun baru secara lokal melalui halaman **Daftar**.
