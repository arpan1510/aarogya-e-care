package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.operationsmiley.aarogyaecare.prevalent.prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class Login extends AppCompatActivity{

    private static final int RC_SIGN_IN = 101 ;
    TextView header,forgot;
    private EditText e,p;
    TextView createAccount;
    private ImageView pv,pvo;
    private FirebaseAuth mAuth;
    private TextView login;
    private int status;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String year;
    private LinearLayout gs;
    private DatabaseReference ref;
    private String finalmobileglogin;
    private DatabaseReference rrr,usageref;
    GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog loadingbar,loadingbar2,loadingbar1;

    @Override
    public void onBackPressed() {


        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Exit")
                .setMessage("Exit from Aarogya E Care?")
                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                    dialog.dismiss();
                });

// Show the alert
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //        Initialization
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(calendar.getTime());
        Paper.init(this);
        forgot=findViewById(R.id.forgot_login_password);
        gs= findViewById(R.id.googlesigninlinear);
        rrr=FirebaseDatabase.getInstance().getReference().child("Users");
        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Forgot Password");
        loadingbar1.setMessage("Please wait");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar2= new ProgressDialog(this);
        loadingbar2.setTitle(" Aarogya E Care");
        loadingbar2.setMessage("You are already logged in!");
        loadingbar2.setCanceledOnTouchOutside(false);
        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Login");
        loadingbar.setMessage("Welcome to Aarogya E Care!");
        loadingbar.setCanceledOnTouchOutside(false);
        ref= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();
        pv=findViewById(R.id.pvisibilitysignin);
        pvo=findViewById(R.id.pvisibilityoffsignin);
        createAccount = findViewById(R.id.create_account_btn);
        e=findViewById(R.id.login_email_input);
        p=findViewById(R.id.login_password_input);
        login=findViewById(R.id.login_btn);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);

//      Click Listeners
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View mview= getLayoutInflater().inflate(R.layout.forgotpassdialog,null);
                final EditText femail;

                femail=mview.findViewById(R.id.addemail);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setView(mview);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(femail.getText().toString().trim().length()!=0){
                            dialog.dismiss();
                            loadingbar1.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(femail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingbar1.dismiss();
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                                                View mview= getLayoutInflater().inflate(R.layout.dialogdetails,null);
                                                final TextView dma;
                                                dma=mview.findViewById(R.id.dialogtext);

                                                builder1.setTitle("Forgot Password");
                                                dma.setText("Link to change password is sent to your email Please Check it out");

                                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder1.setView(mview);
                                                final AlertDialog dialog1 = builder1.create();
                                                dialog1.setCanceledOnTouchOutside(false);
                                                dialog1.show();
                                            }
                                            else
                                            {
                                                loadingbar1.dismiss();
                                                Toast.makeText(Login.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            femail.setError("Please enter your email!");
                        }
                    }
                });
            }
        });
        gs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingbar.show();
                signIn();
            }
        });
        String UserEmailKeyy = Paper.book().read(prevalent.UserEmailKey);
        String UserPasswordKeyy = Paper.book().read(prevalent.UserPasswordKey);
//        if(UserEmailKeyy != "" && UserPasswordKeyy != ""){
//            if(!TextUtils.isEmpty(UserEmailKeyy) && !TextUtils.isEmpty(UserPasswordKeyy)) {
//
//                Allowaccess(UserEmailKeyy, UserPasswordKeyy);
//            }
//        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(e.getText().toString().trim().length()==0)
                {
                    e.setError("Email is required");
                }
                else if(p.getText().toString().trim().length()==0)
                {
                    Toast.makeText(Login.this, "Password is required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.show();

                    mAuth.signInWithEmailAndPassword(e.getText().toString(),p.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {


                                if( mAuth.getCurrentUser().isEmailVerified())
                                {


                                    ref.child(mAuth.getCurrentUser().getUid()).child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child("phone").exists())
                                            {
                                                if(dataSnapshot.child("fname").exists())
                                                {
                                                    status=1;
                                                    Intent intent=new Intent(Login.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                                                    Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                                                    Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    status=0;
                                                    loadingbar.dismiss();
                                                    Intent intent=new Intent(Login.this,Details.class);
                                                    startActivity(intent);
                                                    Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                                                    Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                                                    Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            else
                                            {
                                                status=0;
                                                loadingbar.dismiss();
                                                Intent intent=new Intent(Login.this,mobilenumber.class);
                                                startActivity(intent);
                                                Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                                                Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                                                Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//                                    if(status==1)
//                                    {
//                                        loadingbar.dismiss();
//                                        Intent intent=new Intent(Login.this,home.class);
//                                        startActivity(intent);
//                                        Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
//                                        Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
//                                        Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
//                                    }
//                                    else
//                                    {
//                                        loadingbar.dismiss();
//                                        Intent intent=new Intent(Login.this,Details.class);
//                                        startActivity(intent);
//                                        Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
//                                        Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
//                                        Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
//                                    }

                                }
                                else
                                {
                                    loadingbar.dismiss();
                                    mAuth.signOut();
                                    Toast.makeText(Login.this, "Please verify your email from the link sent to your email address! ", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                loadingbar.dismiss();
                                Toast.makeText(Login.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        loadingbar.dismiss();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        loadingbar.show();
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    loadingbar.dismiss();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    // ...
                }
            }
            else {
                loadingbar.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);



        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUI(user);
//                            if(newuser)
//                            {
//                                final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
//                                builder.setCancelable(false);
//                                View mview= getLayoutInflater().inflate(R.layout.dialogdetailsmobile,null);
//                                final EditText fmobile;
//                                final CountryCodePicker ccp_dialog;
//
//                                fmobile=mview.findViewById(R.id.dialogccp_contact_details);
//                                ccp_dialog=mview.findViewById(R.id.ccpdialog);
//                                ccp_dialog.registerCarrierNumberEditText(fmobile);
//
//
//                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                });
//                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        loadingbar.dismiss();
//                                        mGoogleSignInClient.signOut();
//                                        mAuth.signOut();
//                                        user.delete();
//                                        dialog.dismiss();
//                                    }
//                                });
//                                builder.setView(mview);
//                                final AlertDialog dialog = builder.create();
//                                dialog.setCanceledOnTouchOutside(false);
//                                dialog.show();
//                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if(fmobile.getText().toString().trim().length()!=0&&ccp_dialog.isValidFullNumber()){
//                                            finalmobileglogin = ccp_dialog.getFullNumberWithPlus();
//                                            dialog.dismiss();
//                                            loadingbar.show();
//                                            ref.orderByChild("Self/phone").equalTo(finalmobileglogin).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    if(dataSnapshot.exists())
//                                                    {
//                                                        mGoogleSignInClient.signOut();
//                                                       mAuth.signOut();
//                                                       user.delete();
//                                                       loadingbar.dismiss();
//                                                       Toast.makeText(Login.this, "User with this mobile already exists!", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                    else
//                                                    {
//                                                        updateUI(user);
//
//                                                        usageref.child(user.getUid()).child("total").setValue("250");
//                                                        usageref.child(user.getUid()).child("used").setValue("0");
//                                                        rrr.child(user.getUid()).child("Self").child("phone").setValue(finalmobileglogin);
//                                                        rrr.child(user.getUid()).child("Self").child("MLID").setValue("ML"+year+ccp_dialog.getFullNumber());
//                                                        Toast.makeText(Login.this, user.getEmail(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//                                        else
//                                        {
//                                            fmobile.setError("Please enter a valid Mobile Number!");
//                                        }
//                                    }
//                                });
//                            }
//                            else
//                            {
//                                updateUI(user);
//                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            loadingbar.dismiss();
                            Toast.makeText(Login.this,task.getException().toString(), Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        ref.child(mAuth.getCurrentUser().getUid()).child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("phone").exists())
                {
                    if(dataSnapshot.child("fname").exists())
                    {
                        status=1;
                        loadingbar.dismiss();
                        Intent intent=new Intent(Login.this, HomeActivity.class);
                        startActivity(intent);
                        Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                        Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                        Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        status=1;
                        loadingbar.dismiss();
                        Intent intent=new Intent(Login.this, Details.class);
                        startActivity(intent);
                        Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                        Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                        Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    status=0;
                    loadingbar.dismiss();
                    Intent intent=new Intent(Login.this,mobilenumber.class);
                    startActivity(intent);
                    Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
                    Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
                    Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        if(status==1)
//        {
//            loadingbar.dismiss();
//            Intent intent=new Intent(Login.this,home.class);
//            startActivity(intent);
//            Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
//            Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
//            Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            loadingbar.dismiss();
//            Intent intent=new Intent(Login.this,Details.class);
//            startActivity(intent);
//            Paper.book().write(prevalent.UserEmailKey,e.getText().toString());
//            Paper.book().write(prevalent.UserPasswordKey,p.getText().toString());
//            Toast.makeText(Login.this, "You are successfully logged in!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            loadingbar2.show();
            ref.child(mAuth.getCurrentUser().getUid()).child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("phone").exists())
                    {
                        if(dataSnapshot.child("fname").exists())
                        {
                            status=1;
                            loadingbar2.dismiss();
                            Intent intent=new Intent(Login.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            status=1;
                            loadingbar2.dismiss();
                            Intent intent=new Intent(Login.this, Details.class);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        status=0;
                        loadingbar2.dismiss();
                        Intent intent=new Intent(Login.this,mobilenumber.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        final IntentFilter intentfilter=new IntentFilter();
//        intentfilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        connectivityreceiver connectivityreceiver=new connectivityreceiver();
//        registerReceiver(connectivityreceiver,intentfilter);
//        myapp.getInstance().setConnectivityListener(this);
//    }

//    private void showsnackbar(boolean isConnected) {
//
//        String message;
//        int color;
//
//        if(isConnected)
//        {
//            message="You Are Online!";
//            color= Color.WHITE;
//        }
//        else
//        {
//            message="You Are Offline!";
//            color= Color.RED;
//        }
//
//    }



//    private void Allowaccess(String userEmailKeyy, String userPasswordKeyy) {
//        loadingbar2.show();
//        mAuth.signInWithEmailAndPassword(userEmailKeyy,userPasswordKeyy).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//                    loadingbar2.dismiss();
//                    Toast.makeText(Login.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(Login.this,home.class);
//                    startActivity(intent);
//                }
//                else
//                {
//                    loadingbar2.dismiss();
//                    Toast.makeText(Login.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
