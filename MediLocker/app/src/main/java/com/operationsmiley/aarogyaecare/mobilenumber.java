package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class mobilenumber extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView getotp,header;
    private ImageView backbtn;
    private CountryCodePicker ccp;
    GoogleSignInClient mGoogleSignInClient;
    private EditText mobilenumber;

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilenumber);

        header = findViewById(R.id.simple_toolbar_header);
        header.setText("Add Mobile Number");
        ccp=(CountryCodePicker)findViewById(R.id.ccp_mobilenumber);
        backbtn=findViewById(R.id.backBtn);
        mobilenumber=findViewById(R.id.mobilenumber);
        toolbar = findViewById(R.id.includeToll_mobilenumber);
        setSupportActionBar(toolbar);
        ccp.registerCarrierNumberEditText(mobilenumber);
        getotp=findViewById(R.id.getotpbtn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mobilenumber.getText().toString().trim().length()==0)
                {
                    mobilenumber.setError("Enter mobile number!");
                }
                else if(!ccp.isValidFullNumber())
                {
                    mobilenumber.setError("Please Enter Valid Number");
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Self/phone").equalTo(ccp.getFullNumberWithPlus()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {

                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(mobilenumber.this);
                                flatDialog.setTitle("Mobile Number already registered!")
                                        .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                        .setIcon(R.drawable.numberexists)
                                        .setSubtitle("User with this mobile number already exists")
                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                                        .setFirstButtonText("OK")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                flatDialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                                flatDialog.setCanceledOnTouchOutside(true);
                            }
                            else {

                                Intent intent=new Intent(mobilenumber.this,verificationactivity.class);
                                intent.putExtra("number",ccp.getFullNumberWithPlus());
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
