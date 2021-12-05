package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class verificationactivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText otptext;
    private TextView resend,header;
    private TextView verifybtn;
    private String number,id;
    private FirebaseAuth mAuth;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String year;
    private ImageView backbtn;
    private DatabaseReference uref,usageref;
    private ProgressDialog loadingbar;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationactivity);

        header = findViewById(R.id.simple_toolbar_header);
        header.setText("Verify Mobile Number");
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy");
        backbtn=findViewById(R.id.backBtn);
        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Loading");
        loadingbar.setMessage("Please wait...");
        loadingbar.setCanceledOnTouchOutside(false);
        mAuth=FirebaseAuth.getInstance();
        usageref= FirebaseDatabase.getInstance().getReference().child("Usage");
        uref= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).child("Self");
        number= getIntent().getStringExtra("number");
        toolbar = findViewById(R.id.includeToll_verificationactivity);
        setSupportActionBar(toolbar);
        otptext=findViewById(R.id.otp);
        resend=findViewById(R.id.resend);
        verifybtn=findViewById(R.id.verifybutton);

        sendverificationcode();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingbar.show();

                year = dateFormat.format(calendar.getTime());
                if(otptext.getText().toString().trim().length()==0)
                {
                    loadingbar.dismiss();
                    otptext.setError("Please enter OTP");
                }
                else
                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otptext.getText().toString());

                    mAuth.getCurrentUser().linkWithCredential(credential)
                            .addOnCompleteListener(verificationactivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = task.getResult().getUser();
                                        usageref.child(mAuth.getUid()).child("total").setValue("250");
                                        usageref.child(mAuth.getUid()).child("used").setValue("0");
                                        uref.child("phone").setValue(number);
                                        uref.child("MLID").setValue("AEC"+year+number.replace("+",""));
                                        uref.child("MLID2").setValue("AEC"+year+number.replace("+",""));
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(verificationactivity.this,Details.class);
                                        startActivity(intent);
                                    } else {
                                        loadingbar.dismiss();
                                        Toast.makeText(verificationactivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }


            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationcode();
            }
        });

    }
    private void sendverificationcode() {

        new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                resend.setText(""+millisUntilFinished/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("RESEND");
                resend.setEnabled(true);
            }
        }.start();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,            // Phone number to verify
                60,             // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,      // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        verificationactivity.this.id=id;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(verificationactivity.this, "Failed!"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });        // OnVerificationStateChangedCallbacks
    }
}
