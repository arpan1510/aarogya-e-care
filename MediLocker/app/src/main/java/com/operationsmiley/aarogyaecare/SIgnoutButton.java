package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.operationsmiley.aarogyaecare.prevalent.prevalent;

import io.paperdb.Paper;

public class SIgnoutButton extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser currentUser;

    @Override
    public void onBackPressed() {


        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                .setTitle("Exit")
                .setMessage("Are you sure that you want to Exit?")
                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
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
        setContentView(R.layout.activity_signoutbutton);

        Paper.init(this);
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        logout=findViewById(R.id.logout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut();
        Paper.book().write(prevalent.UserEmailKey,"");
        Paper.book().write(prevalent.UserPasswordKey,"");
        Intent intent=new Intent(SIgnoutButton.this,Login.class);
        startActivity(intent);

    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser=mAuth.getCurrentUser();
        if(currentUser == null)
        {
            Intent loginIntent = new Intent(SIgnoutButton.this, Login.class);
            startActivity(loginIntent);
        }
    }
}
