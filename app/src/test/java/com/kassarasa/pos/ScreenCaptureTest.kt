package com.kassarasa.pos

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.inspector.WindowInspector
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowLooper
import java.io.File
import java.io.FileOutputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w412dp-h892dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ScreenCaptureTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val outputDir: File by lazy {
        val custom = System.getProperty("kassarasa.screenshot.dir")
        val dir = if (!custom.isNullOrBlank()) File(custom) else File("build/kassarasa_screenshots")
        dir.mkdirs()
        dir
    }

    private fun settleAndDraw(fileName: String) {
        composeRule.mainClock.autoAdvance = false
        repeat(10) {
            composeRule.mainClock.advanceTimeBy(80L)
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }

        composeRule.runOnUiThread {
            val views = WindowInspector.getGlobalWindowViews()
            require(views.isNotEmpty()) { "No window views available to capture $fileName" }
            val baseView = views.first()
            val width = baseView.width.coerceAtLeast(1)
            val height = baseView.height.coerceAtLeast(1)

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            baseView.draw(canvas)

            if (views.size > 1) {
                val scrimPaint = Paint().apply {
                    color = Color.argb(128, 0, 0, 0)
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scrimPaint)
                for (i in 1 until views.size) {
                    val dialogView = views[i]
                    val loc = IntArray(2)
                    dialogView.getLocationOnScreen(loc)
                    val dx = if (loc[0] == 0 && dialogView.width < width) {
                        (width - dialogView.width) / 2f
                    } else {
                        loc[0].toFloat()
                    }
                    val dy = if (loc[1] == 0 && dialogView.height < height) {
                        (height - dialogView.height) / 2f
                    } else {
                        loc[1].toFloat()
                    }
                    canvas.save()
                    canvas.translate(dx, dy)
                    dialogView.draw(canvas)
                    canvas.restore()
                }
            }

            val outFile = File(outputDir, fileName)
            FileOutputStream(outFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
        }
    }

    private fun loginToDashboard() {
        composeRule.onNodeWithText("Masuk").performClick()
        composeRule.onAllNodes(hasText("Email atau Username") and hasSetTextAction())
            .onLast()
            .performTextInput("admin@kassarasa.id")
        composeRule.onAllNodes(hasText("Password") and hasSetTextAction())
            .onLast()
            .performTextInput("password123")
        composeRule.onAllNodesWithText("Masuk").onLast().performClick()
    }

    @Test
    fun captureMainFlowScreens() {
        // E.1 Beranda
        settleAndDraw("01_beranda.png")

        // E.3 Register
        composeRule.onNodeWithText("Daftar").performClick()
        settleAndDraw("03_register.png")

        // E.2 Login
        composeRule.onNodeWithText("Masuk di sini").performClick()
        settleAndDraw("02_login.png")

        // Login -> E.4 Dashboard
        composeRule.onAllNodes(hasText("Email atau Username") and hasSetTextAction())
            .onLast()
            .performTextInput("admin@kassarasa.id")
        composeRule.onAllNodes(hasText("Password") and hasSetTextAction())
            .onLast()
            .performTextInput("password123")
        composeRule.onAllNodesWithText("Masuk").onLast().performClick()
        settleAndDraw("04_dashboard.png")

        // E.5 Kelola Menu
        composeRule.onNodeWithText("Menu").performClick()
        settleAndDraw("05_kelola_menu.png")

        // E.9.3 Riwayat Transaksi
        composeRule.onNodeWithText("Riwayat").performClick()
        settleAndDraw("11_riwayat.png")

        // E.9.4 Laporan Penjualan
        composeRule.onNodeWithText("Laporan").performClick()
        settleAndDraw("12_laporan.png")

        // E.9.1 Transaksi Penjualan (Kasir POS)
        composeRule.onNodeWithText("Transaksi").performClick()
        settleAndDraw("09_transaksi.png")

        // E.9.2 Pembayaran & Struk Digital
        composeRule.onNodeWithText("Proses Bayar").performClick()
        settleAndDraw("10_pembayaran.png")
    }

    @Test
    fun captureDialogTambahMenu() {
        loginToDashboard()
        composeRule.onNodeWithText("Menu").performClick()
        composeRule.onNodeWithText("Tambah Menu").performClick()
        settleAndDraw("06_tambah_menu.png")
    }

    @Test
    fun captureDialogEditMenu() {
        loginToDashboard()
        composeRule.onNodeWithText("Menu").performClick()
        composeRule.onNodeWithContentDescription("Edit Nasi Goreng Spesial").performClick()
        settleAndDraw("07_edit_menu.png")
    }

    @Test
    fun captureDialogHapusMenu() {
        loginToDashboard()
        composeRule.onNodeWithText("Menu").performClick()
        composeRule.onNodeWithContentDescription("Hapus Nasi Goreng Spesial").performClick()
        settleAndDraw("08a_hapus_menu.png")
    }

    @Test
    fun captureDialogDetailStruk() {
        loginToDashboard()
        composeRule.onNodeWithText("Riwayat").performClick()
        composeRule.onNodeWithContentDescription("Detail #TRX-2023101").performClick()
        settleAndDraw("08b_detail_struk.png")
    }
}
