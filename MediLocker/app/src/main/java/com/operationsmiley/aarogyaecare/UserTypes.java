package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.operationsmiley.aarogyaecare.module.Users;

public class UserTypes extends AppCompatActivity {
    Toolbar toolbar;
    ImageView backBtn;
    TextView header,usertype,validity;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_types);
        toolbar = findViewById(R.id.usertype_toolbar);
        usertype = findViewById(R.id.usertype);
        validity = findViewById(R.id.validity);
        setSupportActionBar(toolbar);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Self");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userDetails = dataSnapshot.getValue(Users.class);
                String user_type = userDetails.getUsertype();
                usertype.setText(user_type.concat(" ").concat("User"));
                // Validity to be set
                if (user_type.equals("FREE")){
                    validity.setText("Valid up to: 2030");
                }
                else {
                    validity.setText("Valid up to: XX Days");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
