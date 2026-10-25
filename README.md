# Komikta — Android Native

Aplikasi pembaca manhwa/manhua/manga berbasis Kotlin + Jetpack Compose.

## Backend
Base URL: `https://tets-server-production.up.railway.app/`

Endpoint yang digunakan mengikuti README dan route dalam `manhwa-api.zip`:
- `GET /api/home?source=all`
- `GET /api/list?source=all&page=1`
- `GET /api/search?q=...&source=all&page=1`
- `GET /api/type/{type}?source=all&page=1`
- `GET /api/comic/{source}/{id}`
- `GET /api/comic/{source}/{id}/chapter/{chapter}`

Respons API dibungkus `{ "success": true, "data": ... }`. Model Kotlin mengikuti struktur yang didokumentasikan di API ZIP. Server live tidak dapat dijangkau dari lingkungan pembuat ini, jadi endpoint belum bisa diverifikasi secara live.

## Build
1. Buka folder `Komikta` di Android Studio (versi yang mendukung AGP 8.7.3).
2. Pastikan JDK 17 dan Android SDK platform 35 terpasang.
3. Tunggu Gradle sync selesai.
4. Jalankan di emulator/perangkat, atau pilih **Build > Build APK(s)**.

Alternatif terminal:
```bash
gradle assembleDebug
```
Jika Gradle wrapper belum tersedia, gunakan Android Studio untuk membuat wrapper atau jalankan Gradle lokal yang kompatibel.

APK output biasanya berada di `app/build/outputs/apk/debug/app-debug.apk`.

## API key
API key opsional. Jika server kamu mengaktifkannya, ubah `API_KEY` di `app/build.gradle.kts` atau pindahkan ke konfigurasi lokal yang tidak di-commit. Jangan simpan secret produksi dalam repository publik.

## Status implementasi
- Home: hero banner dan section Update/Populer/Baru dari API.
- Explore: pencarian dan filter manhwa/manga/manhua.
- Detail: metadata dan daftar chapter.
- Reader: vertical strip images dan dukungan konten teks novel.
- Library: bookmark tersimpan secara lokal di SharedPreferences dan bisa dihapus dari koleksi. Profile masih berupa layar informasi dasar.
- Notifikasi: ikon UI saat ini belum terhubung ke backend.
- Prev/next chapter: menggunakan nomor chapter yang dikembalikan API untuk berpindah chapter.

## Catatan
Sumber konten berasal dari situs pihak ketiga. Pastikan penggunaan sesuai ketentuan situs sumber dan hukum hak cipta yang berlaku.
