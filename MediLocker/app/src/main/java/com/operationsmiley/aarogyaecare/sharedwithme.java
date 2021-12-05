package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.operationsmiley.aarogyaecare.module.Sharing;

public class sharedwithme extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycle;
    ImageView backbtn;
    TextView header,textt;
    LinearLayout nodatavisible;
    DatabaseReference sharedwithmeref;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog loadingbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedwithme);

        recycle=(RecyclerView)findViewById(R.id.recyclersharedwithme);
        recycle.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycle.setLayoutManager(layoutManager);
        backbtn=findViewById(R.id.backBtn);
        toolbar = findViewById(R.id.includeTollsharedwithme);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Shared with me");
        loadingbar1.setMessage("Loading...");
        loadingbar1.setCanceledOnTouchOutside(false);
        nodatavisible=findViewById(R.id.nodatalinearlayout);
        textt=findViewById(R.id.texttt);
        sharedwithmeref=FirebaseDatabase.getInstance().getReference().child("Sharing").child("SharedwithUid").child(FirebaseAuth.getInstance().getUid());
//        header=findViewById(R.id.simple_toolbar_header);
//        header.setText("Shared with me");
        setSupportActionBar(toolbar);

        header=findViewById(R.id.simple_toolbar_header);
        header.setText("Records shared with me");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingbar1.show();

        sharedwithmeref.orderByChild("access").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    textt.setVisibility(View.VISIBLE);
                    recycle.setVisibility(View.VISIBLE);
                    nodatavisible.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<Sharing> options =
                            new FirebaseRecyclerOptions.Builder<Sharing>()
                                    .setQuery(sharedwithmeref.orderByChild("access").equalTo("1"), Sharing.class)
                                    .build();

                    FirebaseRecyclerAdapter<Sharing, sharedwithmeviewholder> adapter=
                            new FirebaseRecyclerAdapter<Sharing, sharedwithmeviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull sharedwithmeviewholder holder, int position, @NonNull Sharing model) {

                                    holder.email.setText(model.getEmail());
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent intent=new Intent(sharedwithme.this,sharedwithmeviewfiles.class);
                                            intent.putExtra("uid",getRef(position).getKey());
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public sharedwithmeviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewforuserlist,parent,false);
                                    sharedwithmeviewholder holder = new sharedwithmeviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                    loadingbar1.dismiss();
                }
                else
                {
                    loadingbar1.dismiss();
                    textt.setVisibility(View.GONE);
                    recycle.setVisibility(View.GONE);
                    nodatavisible.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
