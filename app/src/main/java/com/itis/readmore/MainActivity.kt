package com.itis.readmore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.BitmapFactory
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.IOException
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.folioreader.FolioReader
import com.folioreader.model.locators.ReadLocator
import com.folioreader.util.ReadLocatorListener
import org.readium.r2.shared.Locations
import com.folioreader.model.locators.ReadLocator.Companion.LOG_TAG




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val folioReader = FolioReader.get()

        folioReader.openBook("file:///TheSilverChair.epub")
    }

}


