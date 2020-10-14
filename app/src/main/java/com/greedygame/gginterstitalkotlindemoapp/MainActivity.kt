package com.greedygame.gginterstitalkotlindemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.greedygame.core.AppConfig
import com.greedygame.core.GreedyGameAds
import com.greedygame.core.adview.modals.AdRequestErrors
import com.greedygame.core.interstitial.general.GGInterstitialAd
import com.greedygame.core.interstitial.general.GGInterstitialEventsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var ggInterstitialAd: GGInterstitialAd

    /*
   Control the display of ads using shouldShowAd flag
    */
    private var shouldShowAd = true
    private val eventListener: InterstitialEventListener = InterstitialEventListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appConfig = AppConfig.Builder(this).withAppId("89221032")
            .build()
        GreedyGameAds.initWith(appConfig)
        ggInterstitialAd = GGInterstitialAd(this, "float-4839")
        loadAdAgain.setOnClickListener {
            showAdAgain()
        }

        loadIntersitialAd()
    }

    private fun loadIntersitialAd() {
        progressBar.visibility = View.VISIBLE
        ggInterstitialAd.loadAd(eventListener)

    }

    private fun showAdAgain() {
        if (ggInterstitialAd.isAdLoaded) {
            /*
            SDKX automatically refresh the ad automatically when ad is closed. If its already
            loaded, we show it here.
            */
            ggInterstitialAd.show()
        } else {
            /*
           Incase the ad is not loaded by the time this function is called again,
           we  make the flag true and load the ad again, the ad will be shown automatically
           once it's loaded
            */
            shouldShowAd = true
            loadIntersitialAd()
        }
    }

    inner class InterstitialEventListener : GGInterstitialEventsListener {
        override fun onAdLoaded() {
            Log.d("GGADS", "Ad Loaded")
            progressBar.visibility = View.GONE
            if (shouldShowAd) {
                // Setting flag to false to not show the ad again.
                shouldShowAd = false
                ggInterstitialAd.show()
            }
        }

        override fun onAdLoadFailed(cause: AdRequestErrors) {
            Log.d("GGADS", "Ad Load Failed $cause")
            progressBar.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Ad Load failed $cause", Toast.LENGTH_SHORT).show()
            //Called when the ad load failed. The reason is available in cause variable

        }

        override fun onAdOpened() {
            Log.d("GGADS", "Ad Opened")
        }

        override fun onAdClosed() {
            // Setting flag to false to not show the ad again. This covers the case of opening
            // and ad that is already loaded
            Log.d("GGADS", "Ad Closed")
            shouldShowAd = false
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            finish()
        }

        override fun onAdLeftApplication() {
            Log.d("GGADS", "Ad Left Application")
        }
    }
}