package com.example.skinwise.view

import android.content.Intent
import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureActivity
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.skinwise.R

class ScanBarcodeActivity : CaptureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
    }

    // Metoda care va fi apelată când scanarea este completă
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificăm dacă rezultatul scanării a fost OK
        if (resultCode == RESULT_OK) {
            // Obținem codul scanat
            val barcode = data?.getStringExtra("SCAN_RESULT")

            // Afișăm un Toast cu codul de bare scanat
            Toast.makeText(this, "Cod de bare scanat: $barcode", Toast.LENGTH_SHORT).show()

            // Poți adăuga aici logica de a prelucra codul de bare (căutarea produsului etc.)
        } else {
            Toast.makeText(this, "Scanare eșuată", Toast.LENGTH_SHORT).show()
        }
    }
}
