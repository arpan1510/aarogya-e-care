package com.operationsmiley.aarogyaecare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.operationsmiley.aarogyaecare.module.Usage;

public class AppInfoActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView header;
    Dialog mDialog;
    TextView websitelink;
    String userId;
    Usage use1;
    TextView terms,privacy;
    ImageView backBtn;
    GoogleSignInOptions gso;
    private ProgressDialog loadingbar1;
    public FirebaseDatabase mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    public DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        mDialog = new Dialog(this);
        mDatabase = FirebaseDatabase.getInstance();
        backBtn = findViewById(R.id.backBtn);
        header=findViewById(R.id.simple_toolbar_header);
        header.setText("About Us");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(0,0);
            }
        });
        mRef =mDatabase.getReference("FeedSuggestions");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        use1 = new Usage();
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        //Hooks
        toolbar = findViewById(R.id.includeToll);

        websitelink = findViewById(R.id.app_info_web_link);
        terms = findViewById(R.id.app_info_terms_link);
        privacy = findViewById(R.id.app_info_privacy_link);

        userId = FirebaseAuth.getInstance().getUid();
        setSupportActionBar(toolbar);
        websitelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.aarogyaecare.com/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://aarogyaecare.com/user/terms.php";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://aarogyaecare.com/user/privacy.php";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }


    @Override
    public void onBackPressed()
    {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            overridePendingTransition(0,0);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser checkUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkUser == null)
        {
            Intent loginIntent = new Intent(AppInfoActivity.this, Login.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
