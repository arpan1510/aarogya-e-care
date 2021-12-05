package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewBlogs extends AppCompatActivity {

    private WebView webvieww;
    private String url,subUrl,name;
    private TextView urlHead;
    private Toolbar mtoolbar;
    private ProgressDialog loadingbar1;
    boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_blogs);

        loadingbar1= new ProgressDialog(this);
        loadingbar1.setMessage("Please wait");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar1.show();
        mtoolbar = findViewById(R.id.blog_page_toolbar);
        setSupportActionBar(mtoolbar);
        url=getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        urlHead = findViewById(R.id.toolbar_header_url);
        subUrl = (String) url.subSequence(0,24);
        subUrl = subUrl.concat("...");
        urlHead.setText(url);
        webvieww=(WebView)findViewById(R.id.webview);
        webvieww.getSettings().setJavaScriptEnabled(true);
        webvieww.loadUrl(url);
        loadwebview();
    }
    public void loadwebview()
    {


        webvieww.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100)
                {
                    loadingbar1.dismiss();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.webview_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.webview_share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = name;
            String shareSub = "Aarogya E Care App";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody.concat("\n").concat(url));
            startActivity(Intent.createChooser(shareIntent,"Share Using"));
        }
        else if(item.getItemId() == R.id.webview_copy)
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String label = "Shared from Aarogya E Care";
            String string = url;
            ClipData clip = ClipData.newPlainText(label, string);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Link Copied!", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.webview_refresh)
        {
            finish();
            Intent intent = new Intent(WebViewBlogs.this,WebViewBlogs.class);
            intent.putExtra("url",url);
            intent.putExtra("name",name);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
        else if(item.getItemId() == R.id.webview_browser)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
        else{
            return true;
        }
        return true;
    }

}