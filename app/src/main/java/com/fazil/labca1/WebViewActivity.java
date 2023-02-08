package com.fazil.labca1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fazil.labca1.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private WebView mywebView;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    TextView textViewWebViewConsole;
    LinearLayout layoutWebViewConsole;

    TextView textViewFileTitle;
    Button buttonShowConsole, buttonHideConsole, buttonPrintWebPage, buttonDesktopView;
    int flagDesktopView = 0;

    String fileTitle;

    // Creating object of WebView
    WebView printWeb;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                setTheme(R.style.LightTheme);


        setContentView(R.layout.activity_web_view);

        // REMOVE DARK MODE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // ACTION BAR
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.gradient));
        getSupportActionBar().setElevation(0);
        // ACTION BAR



        // PRINT WEBPAGE
        buttonPrintWebPage = findViewById(R.id.button_save);
        buttonPrintWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Calling createWebPrintJob()
                        PrintTheWebPage(printWeb);
                    } else {
                        // Showing Toast message to user
                        Toast.makeText(WebViewActivity.this, "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Showing Toast message to user
                    Toast.makeText(WebViewActivity.this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mywebView = findViewById(R.id.webview);
        mywebView.setWebViewClient(new WebViewClient());

        //Add this line for AJAX content
        mywebView.getSettings().setDomStorageEnabled(true);


        // CODE FOR DESKTOP VIEW
        // Setup the User Agent
        // String newDesktopUA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
        // String newMobileUA= "Mozilla/5.0 (Linux; Android 9; Redmi 6 Pro Build/PKQ1.180917.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/89.0.4389.105 Mobile Safari/537.36";
        // mywebView.getSettings().setUserAgentString(newDesktopUA);

        mywebView.getSettings().setSupportZoom(true);
        mywebView.getSettings().setBuiltInZoomControls(true);
        mywebView.getSettings().setDisplayZoomControls(false);
        mywebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mywebView.setScrollbarFadingEnabled(false);
        // DESKTOP VIEW & MOBILE VIEW - CLOSED

        TinyDB tinydb = new TinyDB(WebViewActivity.this);

        String htmlCode = "<img style='width:100%;height:100%;object-fit:contain' src='data:image/png;base64,"
                    + tinydb.getString("mydoc") + "' />";

        mywebView.loadData(htmlCode, "text/html", null);

        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mywebView.setWebViewClient(new WebViewActivity.mywebClient());

        mywebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
            }



        });

    }



    // PRINT WEBVIEW
    // object of print job
    PrintJob printJob;

    // a boolean to check the status of printing
    boolean printBtnPressed = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {

        // set printBtnPressed true
        printBtnPressed = true;

        // Creating  PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(WebViewActivity.PRINT_SERVICE);

        // setting the name of job
        // String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();
        String jobName = "mydoc";

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                // Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {
                // Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            // set printBtnPressed false
            printBtnPressed = false;
        }
    }
    // PRINT WEBVIEW - CLOSED



    public class mywebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);

            // initializing the printWeb Object
            printWeb = mywebView;
        }
    }


}