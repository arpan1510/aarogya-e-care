package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class webviewforimage extends AppCompatActivity {

    private WebView webvieww;
    private Toolbar toolbar1;
    private ImageView backBtn;
    private String url,finalurl;
    private TextView header;
    private ProgressDialog loadingbar1;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webviewforimage);

        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Loading");
        loadingbar1.setMessage("Please wait");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar1.show();
        toolbar1 = findViewById(R.id.includeToll_webview_image);
        backBtn = findViewById(R.id.backBtn);
        header=findViewById(R.id.simple_toolbar_header);
        header.setText("Aarogya E Care");
        setSupportActionBar(toolbar1);
        url=getIntent().getStringExtra("url");
        webvieww=(WebView)findViewById(R.id.webview);
        webvieww.getSettings().setJavaScriptEnabled(true);
        webvieww.getSettings().setBuiltInZoomControls(true);
        webvieww.setInitialScale(1);
        webvieww.getSettings().setLoadWithOverviewMode(true);
        webvieww.getSettings().setUseWideViewPort(true);
        webvieww.loadUrl(url);
        if(! AppStatus.getInstance(this).isOnline()) {
            finish();
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadwebview();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void loadwebview()
    {
        webvieww.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100)
                {
                    loadingbar1.dismiss();
                }
            }
        });

//        webvieww.setWebViewClient(new WebViewClient(){
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                webvieww.loadUrl(url);
//                loadingbar1.dismiss();
//            }
//        });
    }
}
