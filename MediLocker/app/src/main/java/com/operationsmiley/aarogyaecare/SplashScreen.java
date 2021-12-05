package com.operationsmiley.aarogyaecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    PackageInfo pinfo;
    int counts;
    String versionname;
    List<String> quotes;
    DatabaseReference versionref;
    String fdbaseversion;
    TextView quote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        quotes = new ArrayList<>();
        quotes.add("A man too busy to take care of his health is like a mechanic too busy to take care of his tools.");
        quotes.add("Take care of your health, that it may serve you to serve God.");
        quotes.add("Time and health are two precious assets that we don’t recognize and appreciate until they have been depleted.");
        quotes.add("Remember to take care of yourself, you can’t pour from an empty cup");
        quotes.add("Treat your body like it belongs to someone you love.");
        quotes.add("Taking care of your mental and physical health is just as important as any career move or responsibility.");
        Random random = new Random();
        quote = findViewById(R.id.quotes);
        quote.setText(quotes.get(random.nextInt(6)));
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Paper.init(this);
        versionname=pinfo.versionName;
        versionref=FirebaseDatabase.getInstance().getReference().child("version").child("value");
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firststart=prefs.getBoolean("firststart",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                versionref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fdbaseversion=dataSnapshot.getValue(String.class);

                        if(fdbaseversion.equals(versionname))
                        {
                            if(firststart)
                            {
                                SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor=prefs.edit();
                                editor.putBoolean("firststart",false);
                                editor.apply();
                                Intent homeIntent = new Intent(SplashScreen.this, SlideActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }
                            else
                            {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(currentUser == null)
                                {
                                    Intent loginIntent = new Intent(SplashScreen.this, Login.class);
                                    startActivity(loginIntent);
                                    finish();
                                }
                                else
                                {
                                    DatabaseReference useref;
                                    useref= FirebaseDatabase.getInstance().getReference().child("Users");
                                    useref.child(currentUser.getUid()).child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {
                                                if(dataSnapshot.child("phone").exists())
                                                {
                                                    if(dataSnapshot.child("fname").exists())
                                                    {
                                                        Intent homeIntent = new Intent(SplashScreen.this, HomeActivity.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Intent homeIntent = new Intent(SplashScreen.this, Details.class);
                                                        startActivity(homeIntent);
                                                        finish();
                                                    }

                                                }
                                                else
                                                {
                                                    Intent loginIntent = new Intent(SplashScreen.this,mobilenumber.class);
                                                    startActivity(loginIntent);
                                                    finish();
                                                }
                                            }
                                            else
                                            {
                                                FirebaseAuth.getInstance().signOut();
                                                currentUser.delete();
                                                Intent loginIntent = new Intent(SplashScreen.this, Login.class);
                                                startActivity(loginIntent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }
                        }
                        else
                        {
                            Intent intent=new Intent(SplashScreen.this,pleaseupdate.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },SPLASH_TIME_OUT);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(! AppStatus.getInstance(this).isOnline()) {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("No Internet")
                    .setMessage("Exit from Aarogya E Care?")
                    .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }).addButton("Retry",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                        super.onStart();
                        if(AppStatus.getInstance(this).isOnline()){
                            dialog.dismiss();
                        }
                    });
            // Show the alert
            builder.show();
            builder.setCancelable(false);
        }
    }
}
