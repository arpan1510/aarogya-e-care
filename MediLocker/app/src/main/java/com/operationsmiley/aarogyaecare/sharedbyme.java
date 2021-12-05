package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.operationsmiley.aarogyaecare.module.Sharing;

public class sharedbyme extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView header,sharedusertext;
    private  ImageView backbtn,info;
    private RecyclerView recycle;
    private LinearLayout nodatashared;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog loadingbar1;
    private EditText sharingemail;
    private TextView sharingbutton;
    private DatabaseReference sharedwithmeref,sharedbymeref,userref,sharingref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedbyme);

        toolbar = findViewById(R.id.includeTollsharedbyme);
        recycle=(RecyclerView)findViewById(R.id.recyclersharedbyme);
        recycle.setHasFixedSize(true);
        nodatashared=findViewById(R.id.nodatasharedbymelinearlayout);
        sharedusertext=findViewById(R.id.sharedusers1111);
        layoutManager=new LinearLayoutManager(this);
        sharingbutton=findViewById(R.id.sharebtn);
        sharingemail=findViewById(R.id.emailshare);
        info=findViewById(R.id.infosharedbyme);
        recycle.setLayoutManager(layoutManager);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Shared with me");
        loadingbar1.setMessage("Loading...");
        loadingbar1.setCanceledOnTouchOutside(false);
        backbtn=findViewById(R.id.backBtn);
        userref=FirebaseDatabase.getInstance().getReference().child("Users");
        sharingref=FirebaseDatabase.getInstance().getReference().child("Sharing");
        sharedwithmeref= FirebaseDatabase.getInstance().getReference().child("Sharing").child("SharebyUid").child(FirebaseAuth.getInstance().getUid());
        sharedbymeref= FirebaseDatabase.getInstance().getReference().child("Sharing").child("SharedwithUid");
        header=findViewById(R.id.simple_toolbar_header);
        header.setText("Shared by me");
        setSupportActionBar(toolbar);

       backbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               finish();
           }
       });

       info.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(sharedbyme.this);
               View mview = getLayoutInflater().inflate(R.layout.dialogimage, null);
               final ImageView dma;
               dma = mview.findViewById(R.id.dialogimage);
               builder.setTitle("Shared by me");
               builder.setCancelable(false);
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       dialog.dismiss();
                   }
               });
               builder.setView(mview);
               final androidx.appcompat.app.AlertDialog dialog = builder.create();
               dialog.setCanceledOnTouchOutside(false);
               dialog.show();
           }
       });

        sharingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharingemail.getText().toString().length()==0)
                {
                    Toast.makeText(sharedbyme.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userref.orderByChild("Self/email").equalTo(sharingemail.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(sharingemail.getText().toString().trim()))
                                {
                                    Toast.makeText(sharedbyme.this, "You have entered your own email id! Please enter another user's correct email id...", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    for(DataSnapshot ds: dataSnapshot.getChildren())
                                    {
                                        sharingref.child("SharebyUid").child(FirebaseAuth.getInstance().getUid()).child(ds.getKey()).child("access").setValue("1");
                                        sharingref.child("SharebyUid").child(FirebaseAuth.getInstance().getUid()).child(ds.getKey()).child("email").setValue(sharingemail.getText().toString().trim());
                                        sharingref.child("SharedwithUid").child(ds.getKey()).child(FirebaseAuth.getInstance().getUid()).child("access").setValue("1");
                                        sharingref.child("SharedwithUid").child(ds.getKey()).child(FirebaseAuth.getInstance().getUid()).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(sharedbyme.this, "No user with this email exists!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        loadingbar1.show();

        sharedwithmeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    info.setVisibility(View.VISIBLE);
                    nodatashared.setVisibility(View.GONE);
                    sharedusertext.setVisibility(View.VISIBLE);
                    recycle.setVisibility(View.VISIBLE);
                    FirebaseRecyclerOptions<Sharing> options =
                            new FirebaseRecyclerOptions.Builder<Sharing>()
                                    .setQuery(sharedwithmeref, Sharing.class)
                                    .build();

                    FirebaseRecyclerAdapter<Sharing, sharedbymeviewholder> adapter=
                            new FirebaseRecyclerAdapter<Sharing, sharedbymeviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull sharedbymeviewholder holder, int position, @NonNull Sharing model) {

                                    holder.email.setText(model.getEmail());
                                    if(model.getAccess().equals("1"))
                                    {
                                        holder.switchaccess.setChecked(true);
                                    }
                                    else
                                    {
                                        holder.switchaccess.setChecked(false);
                                    }
                                    holder.switchaccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if(isChecked)
                                            {

                                                sharedwithmeref.child(getRef(position).getKey()).child("access").setValue("1");
                                                sharedbymeref.child(getRef(position).getKey()).child(FirebaseAuth.getInstance().getUid()).child("access").setValue("1");
//

                                            }
                                            else
                                            {
                                                sharedwithmeref.child(getRef(position).getKey()).child("access").setValue("0");
                                                sharedbymeref.child(getRef(position).getKey()).child(FirebaseAuth.getInstance().getUid()).child("access").setValue("0");
                                            }
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public sharedbymeviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewforuserlistsaccess,parent,false);
                                    sharedbymeviewholder holder = new sharedbymeviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                    loadingbar1.dismiss();
                } else{
                    loadingbar1.dismiss();
                    nodatashared.setVisibility(View.VISIBLE);
                    info.setVisibility(View.GONE);
                    sharedusertext.setVisibility(View.GONE);
                    recycle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
