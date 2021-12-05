package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class webview extends AppCompatActivity {

    private WebView webvieww;
    private TextView header;
    boolean check=false;
    private ImageView backBtn;
    private Toolbar toolbar1;
    private String url,finalurl;
    private ProgressDialog loadingbar1;
    String encodedurl;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Loading");
        toolbar1 = findViewById(R.id.includeToll_webview);
        setSupportActionBar(toolbar1);
        loadingbar1.setMessage("Please wait");
        loadingbar1.setCanceledOnTouchOutside(false);
        webvieww=(WebView)findViewById(R.id.webview);
        loadingbar1.show();
        backBtn = findViewById(R.id.backBtn);
        header=findViewById(R.id.simple_toolbar_header);
        header.setText("Aarogya E Care");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        url=getIntent().getStringExtra("url");
        if(! AppStatus.getInstance(this).isOnline()) {
            finish();
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadwebview();
        }


    }




    public void loadwebview()
    {


        webvieww.getSettings().setJavaScriptEnabled(true);
        webvieww.getSettings().setBuiltInZoomControls(true);
        webvieww.getSettings().setDomStorageEnabled(true);
        try{
            encodedurl = URLEncoder.encode(url, "UTF-8");
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        finalurl="https://drive.google.com/viewerng/viewer?embedded=true&url=" +encodedurl;
        webvieww.loadUrl(finalurl);

//        webvieww.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if(newProgress==100)
//                {
//                    loadingbar1.dismiss();
//                }
//                boolean check=false;
//
//            }
//        });
        webvieww.setWebViewClient(new WebViewClient(){
            boolean checkOnPageStartedCalled = false;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    loadingbar1.dismiss();
                } else {
                   loadwebview();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
       Intent intent=new Intent(webview.this,ViewAllFiles.class);
       startActivity(intent);

    }
}
