package com.mtc.payment

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.mtc.R

class WebViewAct : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)
        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //Toast.makeText(view!!.context, "Finished", Toast.LENGTH_SHORT).show()
            }


        }
        webView.loadUrl(intent.extras?.getString("url").toString())
    }



    override fun onDestroy() {
        super.onDestroy()
//        try {
//            DialogChatUser.senderId = ""
//            OrderListViewModel.orderListSelected.clear()
//            OrderListViewModel.instractions = ""
//            FragmentPayment.costToAdd = ""
//            FragmentPayment.calculatedValue = 0.0
//            FragmentPayment.placeOrder.paymentType = ""
//            FragmentPayment.placeOrder.grandTotal = "0"
//            FragmentPayment.placeOrder.promoCode = ""
//            FragmentPayment.order_id = ""
//            try {
//                BaseViewModel.reportHomeButton()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
    }

}