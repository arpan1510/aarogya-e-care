package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.operationsmiley.aarogyaecare.module.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateAccount extends AppCompatActivity {

    TextView header,terms;
    private int check1111;
    com.suke.widget.SwitchButton switchButton;
    TextView createAccountBtn;
    private Toolbar toolbar;
    Typeface tfavv;
    private Users user;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String year;
    private DatabaseReference userref,usageref;
    private EditText e,p,cp,contactno;
    private CountryCodePicker ccp;
    private ImageView pv,pvo,cpv,cpvo,back;
    private FirebaseAuth signupauth;
    private ProgressDialog loadingbar,loadingbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Initialization
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(calendar.getTime());
        loadingbar= new ProgressDialog(this);
        loadingbar1 = new ProgressDialog(this);
        user=new Users();
        header=findViewById(R.id.simple_toolbar_header);
        header.setText("Create Account");
        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
//        ccp=(CountryCodePicker) findViewById(R.id.ccp);
//        tfavv = ResourcesCompat.getFont(CreateAccount.this,R.font.roboto_black);
//        contactno=findViewById(R.id.contact_details);
        loadingbar.setTitle("Creating Account");
        loadingbar.setMessage("Please wait while we set up your account");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar1.setMessage("Please wait..");
        loadingbar1.setCanceledOnTouchOutside(false);
        pv=findViewById(R.id.pvisibility);
        cpv=findViewById(R.id.cpvisibility);
        pvo=findViewById(R.id.pvisibilityoff);
        cpvo=findViewById(R.id.cpvisibilityoff);
        terms = findViewById(R.id.terms_services);
        toolbar = findViewById(R.id.first_page_toolbar);
        back = findViewById(R.id.backBtn);
        e=findViewById(R.id.create_account_email_input);
        p=findViewById(R.id.create_account_password_input);
        cp=findViewById(R.id.create_account_confirm_password_input);
        switchButton = findViewById(R.id.switch_button);
        createAccountBtn = findViewById(R.id.create_account_btn);
        signupauth=FirebaseAuth.getInstance();
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchButton.isChecked())
                {
                    check1111=1;
                }
                else
                {
                    check1111=0;
                }

            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar1.show();
                String url = "https://aarogyaecare.com/user/terms.php";
                Intent intent = new Intent(CreateAccount.this,WebViewBlogs.class);
                intent.putExtra("url",url);
                intent.putExtra("name","Terms & Conditions of Aarogya E Care");
                loadingbar1.dismiss();
                startActivity(intent);
            }
        });
        pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pv.setVisibility(View.GONE);
                pvo.setVisibility(View.VISIBLE);
                p.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        pvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvo.setVisibility(View.GONE);
                pv.setVisibility(View.VISIBLE);
                p.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        cpv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpv.setVisibility(View.GONE);
                cpvo.setVisibility(View.VISIBLE);
                cp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        cpvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpvo.setVisibility(View.GONE);
                cpv.setVisibility(View.VISIBLE);
                cp.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchButton.setChecked(false);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchButton.toggle();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getText().toString().trim().length()==0)
                {
                    e.setError("Email is required");
                }
//                else if (contactno.getText().toString().trim().length()==0)
//                {
//                    contactno.setError("Please Enter Your Mobile Number");
//                }
                else if(p.getText().toString().trim().length()==0)
                {
                    Toast.makeText(CreateAccount.this, "Password is required", Toast.LENGTH_SHORT).show();
                }
                else if(cp.getText().toString().trim().length()==0)
                {
                    Toast.makeText(CreateAccount.this, "Please Confirm your password", Toast.LENGTH_SHORT).show();
                }
                else if(!(p.getText().toString()).equals(cp.getText().toString()))
                {
                    Toast.makeText(CreateAccount.this, "Confirm Password isn't matching", Toast.LENGTH_SHORT).show();
                }
                else if(!(switchButton.isChecked()))
                {
                    Toast.makeText(CreateAccount.this, "Please Accept the Terms of Service", Toast.LENGTH_SHORT).show();
                }
//                else if(!ccp.isValidFullNumber())
//                {
//                    contactno.setError("Please Enter Valid Number");
//                }
                else
                {
//                    String phone = ccp.getFullNumberWithPlus();
//                    String phonewithoutplus=ccp.getFullNumber();
                    loadingbar.show();
                    signupauth.createUserWithEmailAndPassword(e.getText().toString(),p.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signupauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String id1 = signupauth.getCurrentUser().getUid();
//                                                        user.setPhone(phone);
//                                                        user.setMLID("ML"+year+phonewithoutplus);
//                                                        userref.child(id1).child("Self").setValue(user);
//                                                        userref.child(id1).child("Self").child("MLID").setValue("ML"+year+phonewithoutplus);
                                            loadingbar.dismiss();
                                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateAccount.this);
                                            View mview = getLayoutInflater().inflate(R.layout.dialogdetails, null);
                                            final TextView dma;
                                            dma = mview.findViewById(R.id.dialogtext);
                                            builder.setTitle("Registered Successfully!");
                                            builder.setCancelable(false);
                                            dma.setText("Verification Link is sent to your email id Please verify to login");

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseUser user = signupauth.getCurrentUser();

//                                                    usageref.child(user.getUid()).child("total").setValue("250");
//                                                    usageref.child(user.getUid()).child("used").setValue("0");
                                                    signupauth.signOut();

                                                    Intent intent=new Intent(CreateAccount.this,Login.class);
                                                    startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.setView(mview);
                                            final androidx.appcompat.app.AlertDialog dialog = builder.create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();

                                        } else {
                                            loadingbar.dismiss();
                                            Toast.makeText(CreateAccount.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                loadingbar.dismiss();
                                Toast.makeText(CreateAccount.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
