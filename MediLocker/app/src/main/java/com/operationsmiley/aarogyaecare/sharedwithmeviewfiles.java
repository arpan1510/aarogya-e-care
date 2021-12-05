package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.operationsmiley.aarogyaecare.R.drawable.ic_image_black_24dp;
import static com.operationsmiley.aarogyaecare.R.drawable.ic_picture_as_pdf_black_24dp;

public class sharedwithmeviewfiles extends AppCompatActivity {

    Toolbar toolbar,toolbar2;
    ImageView searchbtn,cancel;
    Spinner userwho;
    String uid;
    androidx.appcompat.widget.SearchView search;
    DatabaseReference userwhoref;
    Typeface tfavv;
    RecyclerView recycle;
    ProgressDialog loadingbar1;
    LinearLayout li;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    String ofilename,extension,downurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedwithmeviewfiles);

        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Records");
        loadingbar1.setMessage("Loading...");
        loadingbar1.setCanceledOnTouchOutside(false);
        li=findViewById(R.id.linearlayoutsharedwithme);
        searchbtn=findViewById(R.id.searchbtnbb);
        toolbar = findViewById(R.id.includeToll_sharedwithmefiles);
        toolbar2 = findViewById(R.id.toolbar_sharedwithmefiles);
        cancel=findViewById(R.id.cancelbtn2);
        uid =getIntent().getStringExtra("uid");
        search=findViewById(R.id.searchedittext2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(uid);
        userwhoref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userwho=(Spinner)findViewById(R.id.spinner_selectusertype_sharedwithmefiles);
        recycle=(RecyclerView)findViewById(R.id.recyclersharedwithmefiles);
        recycle.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        recycle.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);

        List<String> usernamelist = new ArrayList<>();
        userwhoref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usernamelist.add("All Members");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    usernamelist.add(key);

                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(sharedwithmeviewfiles.this, android.R.layout.simple_spinner_dropdown_item, usernamelist){
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        tfavv = ResourcesCompat.getFont(sharedwithmeviewfiles.this,R.font.roboto_black);
                        TextView v = (TextView) super.getView(position, convertView, parent);
                        v.setTypeface(tfavv);
                        if(position==0)
                        {
                            v.setTextColor(Color.parseColor("#888888"));
                        }
                        else
                        {
                            v.setTextColor(Color.BLACK);
                        }
                        v.setTextSize(16);
                        return v;
                    }

                    public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                        TextView v = (TextView) super.getView(position, convertView, parent);
                        v.setTypeface(tfavv);
                        if(position==0)
                        {
                            v.setTextColor(Color.parseColor("#888888"));
                        }
                        else
                        {
                            v.setTextColor(Color.BLACK);
                        }
                        v.setTextSize(16);
                        return v;
                    }
                };
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userwho.setAdapter(adapter1);
//                userwho.setSelection(adapter1.getPosition(usernamelist.get(0)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.INVISIBLE);
                toolbar2.setVisibility(View.VISIBLE);
                li.setVisibility(View.GONE);
                setSupportActionBar(toolbar2);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                li.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                toolbar2.setVisibility(View.GONE);

                if(userwho.getSelectedItem().toString().equals("All Members"))
                {
                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){

                                                        case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                            // Create Alert using Builder
                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                            recordref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                    String filename = "File Name: ".concat(show.getFilename());
                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                    String clinic = "Clinic: ".concat(show.getHospital());
                                                                    String Notes = "Notes: ".concat(show.getNotes());
                                                                    String Date = "Created On: ".concat(show.getDate_o());
                                                                    String ext = "Extension: ".concat(show.getFiletype());
                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                    String subtype="Category: ".concat(show.getRecord_type1());
                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                            concat(ext).concat("\n").concat(size).concat("\n");
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                            .setTitle("Record Details")
                                                                            .setMessage(Message)
                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                dialog.dismiss();
                                                                            });


                                                                    // Show the alert
                                                                    builder5.show();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                            popupMenu.show();
                                        }
                                    });
                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                    holder.vhfilesize.setText(numberAsString+"MB");
                                    if(model.getFiletype().equals("pdf"))
                                    {
                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                    }
                                    else
                                    {
                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(model.getFiletype().equals("pdf"))
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }

                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                    recordviewholder holder = new recordviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                }
                else
                {
                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()),uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){

                                                        case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                            // Create Alert using Builder
                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                            recordref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                    String filename = "File Name: ".concat(show.getFilename());
                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                    String clinic = "Clinic: ".concat(show.getHospital());
                                                                    String Notes = "Notes: ".concat(show.getNotes());
                                                                    String Date = "Created On: ".concat(show.getDate_o());
                                                                    String ext = "Extension: ".concat(show.getFiletype());
                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                    String subtype="Category: ".concat(show.getRecord_type1());
                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                            concat(ext).concat("\n").concat(size).concat("\n");
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                            .setTitle("Record Details")
                                                                            .setMessage(Message)
                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                dialog.dismiss();
                                                                            });


                                                                    // Show the alert
                                                                    builder5.show();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                            popupMenu.show();
                                        }
                                    });
                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                    holder.vhfilesize.setText(numberAsString+"MB");
                                    if(model.getFiletype().equals("pdf"))
                                    {
                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                    }
                                    else
                                    {
                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(model.getFiletype().equals("pdf"))
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                    recordviewholder holder = new recordviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                }


            }
        });

        userwho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(userwho.getSelectedItem().toString().equals("All Members"))
                {
                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){

                                                        case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                            // Create Alert using Builder
                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                            recordref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                    String filename = "File Name: ".concat(show.getFilename());
                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                    String clinic = "Clinic: ".concat(show.getHospital());
                                                                    String Notes = "Notes: ".concat(show.getNotes());
                                                                    String Date = "Created On: ".concat(show.getDate_o());
                                                                    String ext = "Extension: ".concat(show.getFiletype());
                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                    String subtype="Category: ".concat(show.getRecord_type1());
                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                            concat(ext).concat("\n").concat(size).concat("\n");
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                            .setTitle("Record Details")
                                                                            .setMessage(Message)
                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                dialog.dismiss();
                                                                            });


                                                                    // Show the alert
                                                                    builder5.show();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                            popupMenu.show();
                                        }
                                    });
                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                    holder.vhfilesize.setText(numberAsString+"MB");
                                    if(model.getFiletype().equals("pdf"))
                                    {
                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                    }
                                    else
                                    {
                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(model.getFiletype().equals("pdf"))
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }

                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                    recordviewholder holder = new recordviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                }
                else
                {
                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()),uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){

                                                        case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                            // Create Alert using Builder
                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                            recordref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                    String filename = "File Name: ".concat(show.getFilename());
                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                    String clinic = "Clinic: ".concat(show.getHospital());
                                                                    String Notes = "Notes: ".concat(show.getNotes());
                                                                    String Date = "Created On: ".concat(show.getDate_o());
                                                                    String ext = "Extension: ".concat(show.getFiletype());
                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                    String subtype="Category: ".concat(show.getRecord_type1());
                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                            concat(ext).concat("\n").concat(size).concat("\n");
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                            .setTitle("Record Details")
                                                                            .setMessage(Message)
                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                dialog.dismiss();
                                                                            });


                                                                    // Show the alert
                                                                    builder5.show();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                            popupMenu.show();
                                        }
                                    });
                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                    holder.vhfilesize.setText(numberAsString+"MB");
                                    if(model.getFiletype().equals("pdf"))
                                    {
                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                    }
                                    else
                                    {
                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(model.getFiletype().equals("pdf"))
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                    recordviewholder holder = new recordviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((EditText)  search.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        ((EditText)  search.findViewById(R.id.search_src_text))
                .setTextColor(getResources().getColor(android.R.color.black));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                firebasesearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebasesearch(newText);
                return false;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadingbar1.show();
        FirebaseRecyclerOptions<uploadFile> options =
                new FirebaseRecyclerOptions.Builder<uploadFile>()
                        .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
                        .build();

        FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                        String key=getRef(position).getKey();
                        String furl=model.getUrl();
                        holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                        holder.vhdate.setText(model.getDate());
                        holder.vhrecordtype.setText(model.getRecord_type());

                        holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ofilename=model.getOriginal_filename();
                                extension="."+model.getFiletype();
                                PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch(item.getItemId()){

                                            case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                // Create Alert using Builder
                                                DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                recordref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                        String filename = "File Name: ".concat(show.getFilename());
                                                        String filetype = "File Type: ".concat(show.getRecord_type());
                                                        String doctor = "Doctor: ".concat(show.getDoctorname());
                                                        String clinic = "Clinic: ".concat(show.getHospital());
                                                        String Notes = "Notes: ".concat(show.getNotes());
                                                        String Date = "Created On: ".concat(show.getDate_o());
                                                        String ext = "Extension: ".concat(show.getFiletype());
                                                        String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                        String subtype="Category: ".concat(show.getRecord_type1());
                                                        String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                        String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                concat(ext).concat("\n").concat(size).concat("\n");
                                                        CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                .setTitle("Record Details")
                                                                .setMessage(Message)
                                                                .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                    dialog.dismiss();
                                                                });


                                                        // Show the alert
                                                        builder5.show();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                popupMenu.show();
                            }
                        });
                        String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                        holder.vhfilesize.setText(numberAsString+"MB");
                        if(model.getFiletype().equals("pdf"))
                        {
                            holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                        }
                        else
                        {
                            holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(model.getFiletype().equals("pdf"))
                                {
                                    loadingbar1.show();
                                    Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                    intent.putExtra("url",furl);
                                    loadingbar1.dismiss();
                                    startActivity(intent);
                                }
                                else
                                {
                                    loadingbar1.show();
                                    Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                    intent.putExtra("url",furl);
                                    loadingbar1.dismiss();
                                    startActivity(intent);
                                }

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                        recordviewholder holder = new recordviewholder(view);
                        return holder;
                    }
                };
        recycle.setAdapter(adapter);
        adapter.startListening();
        loadingbar1.dismiss();
    }
    private void firebasesearch(String searchtext) {
        String query = searchtext.toUpperCase();

        Query firebasesearchquery = databaseReference.orderByChild("filename").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<uploadFile> options =
                new FirebaseRecyclerOptions.Builder<uploadFile>()
                        .setQuery(firebasesearchquery,uploadFile.class)
                        .build();

        FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                        String key=getRef(position).getKey();
                        String furl=model.getUrl();
                        holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                        holder.vhdate.setText(model.getDate());
                        holder.vhrecordtype.setText(model.getRecord_type());
                        loadingbar1.dismiss();
                        holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ofilename=model.getOriginal_filename();
                                extension="."+model.getFiletype();
                                PopupMenu popupMenu = new PopupMenu(sharedwithmeviewfiles.this,holder.vhdropdown);
                                popupMenu.getMenuInflater().inflate(R.menu.popup1,popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch(item.getItemId()){

                                            case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                // Create Alert using Builder
                                                DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                recordref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                        String filename = "File Name: ".concat(show.getFilename());
                                                        String filetype = "File Type: ".concat(show.getRecord_type());
                                                        String doctor = "Doctor: ".concat(show.getDoctorname());
                                                        String clinic = "Clinic: ".concat(show.getHospital());
                                                        String Notes = "Notes: ".concat(show.getNotes());
                                                        String Date = "Created On: ".concat(show.getDate_o());
                                                        String ext = "Extension: ".concat(show.getFiletype());
                                                        String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                        String subtype="Category: ".concat(show.getRecord_type1());
                                                        String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                        String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                concat(ext).concat("\n").concat(size).concat("\n");
                                                        CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(sharedwithmeviewfiles.this)
                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                .setTitle("Record Details")
                                                                .setMessage(Message)
                                                                .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                    dialog.dismiss();
                                                                });


                                                        // Show the alert
                                                        builder5.show();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                popupMenu.show();
                            }
                        });
                        String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                        holder.vhfilesize.setText(numberAsString+"MB");
                        if(model.getFiletype().equals("pdf"))
                        {
                            holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                        }
                        else
                        {
                            holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(model.getFiletype().equals("pdf"))
                                {
                                    loadingbar1.show();
                                    Intent intent = new Intent(sharedwithmeviewfiles.this,webview.class);
                                    intent.putExtra("url",furl);
                                    loadingbar1.dismiss();
                                    startActivity(intent);
                                }
                                else
                                {
                                    loadingbar1.show();
                                    Intent intent = new Intent(sharedwithmeviewfiles.this,webviewforimage.class);
                                    intent.putExtra("url",furl);
                                    loadingbar1.dismiss();
                                    startActivity(intent);
                                }

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                        recordviewholder holder = new recordviewholder(view);
                        return holder;
                    }
                };
        recycle.setAdapter(adapter);
        adapter.startListening();

    }
}
