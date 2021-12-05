package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.operationsmiley.aarogyaecare.module.Paymentmodel;

public class payment extends AppCompatActivity {

    private TextView header;
    Toolbar toolbar;
    LinearLayout nodatavisible;
    private RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog loadingbar1;
    DatabaseReference sharedwithmeref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        header = findViewById(R.id.headertool);
        header.setText("Payment History");
        nodatavisible=findViewById(R.id.nodatalinearlayout1);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Payment History");
        loadingbar1.setMessage("Loading...");
        loadingbar1.setCanceledOnTouchOutside(false);
        toolbar = findViewById(R.id.includeToll_payment);
        sharedwithmeref= FirebaseDatabase.getInstance().getReference().child("Payment").child(FirebaseAuth.getInstance().getUid());
        recycle=(RecyclerView)findViewById(R.id.recyclerpayment);
        recycle.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycle.setLayoutManager(layoutManager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                if(dataSnapshot.exists())
                {
                    recycle.setVisibility(View.VISIBLE);
                    nodatavisible.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<Paymentmodel> options =
                            new FirebaseRecyclerOptions.Builder<Paymentmodel>()
                                    .setQuery(sharedwithmeref, Paymentmodel.class)
                                    .build();

                    FirebaseRecyclerAdapter<Paymentmodel, paymentviewholder> adapter=
                            new FirebaseRecyclerAdapter<Paymentmodel,  paymentviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull  paymentviewholder holder, int position, @NonNull Paymentmodel model) {

                                    holder.orderid.setText(model.getOrder_id());
                                    holder.datep.setText(model.getDate());
                                    holder.payable.setText("₹ "+model.getAmount());
                                    holder.storagep.setText(model.getCategory());

                                    if(model.getStatus()==0)
                                    {
                                        holder.imagepass.setVisibility(View.GONE);
                                        holder.imagefail.setVisibility(View.VISIBLE);
                                        holder.failed.setVisibility(View.VISIBLE);
                                        holder.paid.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        holder.imagepass.setVisibility(View.VISIBLE);
                                        holder.imagefail.setVisibility(View.GONE);
                                        holder.failed.setVisibility(View.GONE);
                                        holder.paid.setVisibility(View.VISIBLE);

                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(holder.upkey.getVisibility()==View.VISIBLE)
                                            {
                                                holder.downkey.setVisibility(View.VISIBLE);
                                                holder.upkey.setVisibility(View.GONE);
                                                holder.relativestatus.setVisibility(View.GONE);
                                                holder.relativestorage.setVisibility(View.GONE);
                                                holder.relativepayable.setVisibility(View.GONE);
                                                holder.relativedate.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                holder.downkey.setVisibility(View.GONE);
                                                holder.upkey.setVisibility(View.VISIBLE);
                                                holder.relativestatus.setVisibility(View.VISIBLE);
                                                holder.relativestorage.setVisibility(View.VISIBLE);
                                                holder.relativepayable.setVisibility(View.VISIBLE);
                                                holder.relativedate.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
//                                    holder.upkey.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            holder.downkey.setVisibility(View.VISIBLE);
//                                            holder.upkey.setVisibility(View.GONE);
//                                            holder.relativestatus.setVisibility(View.GONE);
//                                            holder.relativestorage.setVisibility(View.GONE);
//                                            holder.relativepayable.setVisibility(View.GONE);
//                                            holder.relativedate.setVisibility(View.GONE);
//                                        }
//                                    });
//                                    holder.downkey.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            holder.downkey.setVisibility(View.GONE);
//                                            holder.upkey.setVisibility(View.VISIBLE);
//                                            holder.relativestatus.setVisibility(View.VISIBLE);
//                                            holder.relativestorage.setVisibility(View.VISIBLE);
//                                            holder.relativepayable.setVisibility(View.VISIBLE);
//                                            holder.relativedate.setVisibility(View.VISIBLE);
//                                        }
//                                    });
                                }

                                @NonNull
                                @Override
                                public  paymentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewforpayment,parent,false);
                                    paymentviewholder holder = new  paymentviewholder(view);
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
