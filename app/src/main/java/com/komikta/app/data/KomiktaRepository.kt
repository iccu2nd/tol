package com.komikta.app.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.ConcurrentHashMap

/** Repository with short-lived in-memory caching to avoid re-scraping older chapters. */
class KomiktaRepository(private val api: KomiktaApi = ApiClient.service) {
    private data class Entry<T>(val value: T, val savedAt: Long)
    private val detailCache = ConcurrentHashMap<String, Entry<DetailData>>()
    private val chapterCache = ConcurrentHashMap<String, Entry<ReaderData>>()
    private val ttlMs = 10 * 60 * 1000L

    suspend fun home(): Result<ComicListData> = safe {
        val data = api.home().data ?: throw IOException("Respons beranda kosong")
        ComicListData(popular = data.popular, latest = data.latest, new = data.new, items = data.items)
    }
    suspend fun latest(page: Int = 1): Result<ComicListData> = safe {
        api.latest(page = page).data ?: throw IOException("Data update tidak tersedia")
    }
    suspend fun search(q: String, page: Int = 1): Result<ComicListData> = safe {
        api.search(q, page = page).data ?: throw IOException("Hasil pencarian tidak tersedia")
    }
    suspend fun byType(type: String, page: Int = 1): Result<ComicListData> = safe {
        api.byType(type, page = page).data ?: throw IOException("Katalog tidak tersedia")
    }
    suspend fun detail(comic: Comic): Result<DetailData> = safe {
        val id = comic.sourceId.ifBlank { comic.id.substringAfter(":") }
        val key = "${comic.source}/$id"
        detailCache[key]?.takeIf { System.currentTimeMillis() - it.savedAt < ttlMs }?.value ?: retry {
            val response = api.detail(comic.source, id)
            var data = response.data ?: throw IOException(response.error?.message ?: "Detail komik tidak tersedia")
            if (data.chapters.isEmpty()) {
                runCatching { api.chapters(comic.source, id).data?.chapters }.getOrNull()?.takeIf { it.isNotEmpty() }?.let { list ->
                    data = data.copy(chapters = list)
                }
            }
            detailCache[key] = Entry(data, System.currentTimeMillis())
            data
        }
    }
    suspend fun chapter(comic: Comic, chapter: String): Result<ReaderData> = safe {
        val id = comic.sourceId.ifBlank { comic.id.substringAfter(":") }
        val key = "${comic.source}/$id/$chapter"
        chapterCache[key]?.takeIf { System.currentTimeMillis() - it.savedAt < ttlMs }?.value ?: retry {
            val response = api.chapter(comic.source, id, chapter)
            val data = response.data ?: throw IOException(response.error?.message ?: "Isi chapter tidak tersedia")
            if (data.chapter.images.isEmpty() && data.chapter.novelContent?.paragraphs.isNullOrEmpty()) {
                throw IOException("Server tidak mengirim halaman chapter. Coba lagi beberapa saat.")
            }
            chapterCache[key] = Entry(data, System.currentTimeMillis())
            data
        }
    }

    private suspend fun <T> retry(block: suspend () -> T): T {
        var last: Throwable? = null
        repeat(2) { attempt ->
            try { return block() } catch (t: Throwable) {
                if (t is CancellationException) throw t
                last = t
                val transient = t is SocketTimeoutException || t is UnknownHostException || t is IOException || (t is HttpException && t.code() >= 500)
                if (!transient || attempt == 1) throw friendlyError(t)
                delay(350L)
            }
        }
        throw friendlyError(last ?: IOException("Permintaan gagal"))
    }

    private fun friendlyError(t: Throwable): IOException {
        if (t is HttpException) {
            val msg = when (t.code()) {
                400, 404 -> "Komik atau chapter tidak ditemukan (HTTP ${t.code()})."
                401, 403 -> "Akses API ditolak (HTTP ${t.code()})."
                429 -> "Server sedang sibuk. Tunggu sebentar lalu coba lagi."
                in 500..599 -> "Server sumber komik sedang bermasalah (HTTP ${t.code()}). Coba lagi."
                else -> "Permintaan gagal (HTTP ${t.code()})."
            }
            return IOException(msg, t)
        }
        if (t is SocketTimeoutException) return IOException("Server terlalu lama merespons. Coba lagi.", t)
        if (t is UnknownHostException) return IOException("Tidak dapat terhubung ke server. Periksa koneksi internet.", t)
        return if (t is IOException) t else IOException(t.message ?: "Terjadi kesalahan. Coba lagi.", t)
    }

    private suspend fun <T> safe(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        if (t is CancellationException) throw t
        Result.failure(friendlyError(t))
    }
}

object KomiktaRepositories { val shared: KomiktaRepository by lazy { KomiktaRepository() } }
