package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.operationsmiley.aarogyaecare.module.Usage;
import com.operationsmiley.aarogyaecare.module.Users;
import com.operationsmiley.aarogyaecare.prevalent.prevalent;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View headerView;
    private LinearLayout storagedetails;
    float pastused1,totalavai;
    ChipNavigationBar bottomnavigation;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    FirebaseUser checkUser;
    Toolbar toolbar;
    String usertype, totalstorage="0",storageused="0",nooffiles="0",noofimages="0",noofpdf="0",noofreports="0",noofprescriptions="0";
    TextView username,phonenumber,mlid,storagepercent,storageoutof,progressstorage,usertypeshow;
    ProgressBar prog;
    int adTotalCount;
    String userId;
    String pastused;
    Usage use,use1;
    int getrandom;
    List<String> reply;
    LinearLayout upgradenow;
    TextView viewallblogs,buystorage;
    ImageView settings;
    private ProgressDialog loadingbar1;
    Dialog mDialog;
    ImageView threedots;
    private RecyclerView recycler11;
    RecyclerView.LayoutManager layoutManager1;
    DatabaseReference usageref;
    SliderView sliderView;
    List<AdImageSliderModel> imageSliderModelList;

    public FirebaseDatabase mDatabase;
    public DatabaseReference mRef,myref,adRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);

        //pop message
        checkUser= FirebaseAuth.getInstance().getCurrentUser();
        userId = checkUser.getUid();
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Save your Records!");
        loadingbar1.setMessage("Please wait...");
        loadingbar1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loadingbar1.setCanceledOnTouchOutside(false);
        mDialog = new Dialog(this);
        threedots=findViewById(R.id.menudots);
        adRef = FirebaseDatabase.getInstance().getReference().child("AdBanner");
        use1=new Usage();
        mAuth=FirebaseAuth.getInstance();
        use=new Usage();
        reply = new ArrayList<>();
        reply.add("Wonderful!");
        reply.add("Sounds Great!");
        reply.add("Awesome!");
        reply.add("Exciting!");
        Paper.init(this);
        mDatabase = FirebaseDatabase.getInstance();
        adRef = FirebaseDatabase.getInstance().getReference().child("AdBanner");
        mRef =mDatabase.getReference("FeedSuggestions");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);

        UpdateToken();

//      Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        viewallblogs = findViewById(R.id.viewall_btn);
        viewallblogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte02 = new Intent(HomeActivity.this, BlogsActivity.class);
                startActivity(inte02);
                overridePendingTransition(0,0);
            }
        });
        toolbar = findViewById(R.id.includeToll);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        bottomnavigation = findViewById(R.id.bottom_nav);
//      bottomnavigation.getOrCreateBadge(R.id.nav_notifications).setNumber(5);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawerOpen, R.string.navigation_drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        bottomnavigation.setOnItemSelectedListener(navLister);
        bottomnavigation.setItemSelected(R.id.nav_home,true);
//        bottomnavigation.setOnNavigationItemSelectedListener(navListner);

        sliderView = findViewById(R.id.ad_imageSlider);
        navigationView.getMenu().getItem(0).setChecked(true);
        headerView = navigationView.inflateHeaderView(R.layout.header);
        username = headerView.findViewById(R.id.user_name);
        phonenumber = headerView.findViewById(R.id.phone_number);
        usertypeshow = headerView.findViewById(R.id.type);
//        settings = headerView.findViewById(R.id.setting);
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0,0);
//            }
//        });
//        usertypeshow.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v)
//            {
//                drawerLayout.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(HomeActivity.this,UserTypes.class);
//                startActivity(intent);
//            }
//        });
        storagedetails=headerView.findViewById(R.id.storagedetailslinearlayout);
        storagepercent=headerView.findViewById(R.id.progresspercent);
        storageoutof=headerView.findViewById(R.id.progressvalue);
        upgradenow = headerView.findViewById(R.id.upgrade_banner);
        prog=headerView.findViewById(R.id.progressbarstorage);
        buystorage=headerView.findViewById(R.id.buystorage);

        buystorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aarogyaecare.com/user/payment.php"));
                startActivity(browserIntent);

            }
        });

        threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this,threedots);
                popupMenu.getMenuInflater().inflate(R.menu.popup2,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){

                            case R.id.invite : {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                String shareBody = "Hey There! I am using  Aarogya E Care App.\n\nYou can store your all previous Medical Records at your provided digital medical locker.\n\nDownload the App Now!:\nplay.google.com/store/apps/details?id="+getPackageName();
                                String shareSub = "Aarogya E Care App";

                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);

                                startActivity(Intent.createChooser(shareIntent,"Share Using"));
                            }
                            return true;
                            case R.id.suggestion:{
                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(HomeActivity.this);
                                flatDialog.setTitle("Feedback / Suggestion")
                                        .setTitleColor(getResources().getColor(android.R.color.black))
                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                        .setFirstTextFieldHint("Subject")
                                        .setFirstTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
                                        .setFirstTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
                                        .setFirstTextFieldTextColor(getResources().getColor(android.R.color.black))
                                        .setLargeTextFieldHint("Write your Message here")
                                        .setLargeTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
                                        .setLargeTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
                                        .setLargeTextFieldTextColor(getResources().getColor(android.R.color.black))
                                        .setFirstButtonText("Send")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                DatabaseReference myref;
                                                String subject = flatDialog.getFirstTextField().trim();
                                                String feedback = flatDialog.getLargeTextField().trim();
                                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                Date currentTime = Calendar.getInstance().getTime();
                                                String timestamp = currentTime.toString();
                                                String timecheck = timestamp.substring(0,10);
                                                myref = FirebaseDatabase.getInstance().getReference().child("Feedback");
                                                HashMap<String, Object> dataMap = new HashMap<>();
                                                dataMap.put("subject",subject);
                                                dataMap.put("feedback",feedback);
                                                dataMap.put("timestamp",timestamp);
                                                dataMap.put("uid",userid);
                                                if(subject.isEmpty())
                                                {
                                                    Toast.makeText(HomeActivity.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
                                                }
                                                else if (feedback.isEmpty())
                                                {
                                                    Toast.makeText(HomeActivity.this, "Feedback can't be Empty!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    final int[] aman = {1};
                                                    Query query = myref.child(userid).orderByChild("uid");
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot delSnapshot: dataSnapshot.getChildren()) {
                                                                AdImageSliderModel model = delSnapshot.getValue(AdImageSliderModel.class);
                                                                if (model.getTimestamp().contains(timecheck)) {
                                                                    aman[0] = 0;
                                                                    Toast.makeText(HomeActivity.this, "You can send Feedback once a Day!", Toast.LENGTH_LONG).show();
                                                                    flatDialog.dismiss();
                                                                }
                                                            }
                                                            if(aman[0] == 1){
                                                                myref.child(userid).child(timestamp).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        flatDialog.dismiss();
                                                                        Toast.makeText(HomeActivity.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .show();
                                flatDialog.setCanceledOnTouchOutside(true);
                            }
                            return true;
                            case R.id.aboutus:{
                                startActivity(new Intent(getApplicationContext(),AppInfoActivity.class));
                            }
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

//        storagedetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                String totalstorage,storageused,nooffiles,noofimages,noofpdf,noofreports,noofprescriptions;
//                DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                DatabaseReference usageref= FirebaseDatabase.getInstance().getReference().child("Usage").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                usageref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            totalstorage=dataSnapshot.child("total").getValue(String.class);
//                            storageused=dataSnapshot.child("used").getValue(String.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.orderByChild("record_type").equalTo("Prescription").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        noofprescriptions= String.valueOf(dataSnapshot.getChildrenCount());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.orderByChild("record_type").equalTo("Report").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        noofreports= String.valueOf(dataSnapshot.getChildrenCount());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists())
//                        {
//                            nooffiles = "No of files : ".concat(String.valueOf(dataSnapshot.getChildrenCount()));
//                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
////                                if(ds.child("record_type").getValue(String.class).equals("Prescription"))
////                                {
////                                    noofprescriptions=String.valueOf(Long.valueOf(noofprescriptions)+1);
////                                }
////                                else
////                                {
////                                    noofreports=String.valueOf(Long.valueOf(noofreports)+1);
////                                }
//                                if (ds.child("filetype").getValue(String.class).equals("PDF"))
//                                {
//                                    noofpdf=String.valueOf(Long.valueOf(noofpdf)+1);
//                                }
//                                else
//                                {
//                                    noofimages=String.valueOf(Long.valueOf(noofimages)+1);
//                                }
//
//                            }
//                        }
//                        else
//                        {
////                            noofprescriptions="0";
////                            noofreports="0";
//                            noofpdf="0";
//                            noofimages="0";
//                        }
//
//                        noofimages = "No. of Images : ".concat(noofimages);
//                        noofpdf  = "No. of PDFs : ".concat(noofpdf);
//                        noofreports = "No. of Reports: ".concat(noofreports);
//                        noofprescriptions = "No. of Prescriptions : ".concat(noofprescriptions);
//                        totalstorage = "Total Storage : ".concat(totalstorage);
//                        storageused="Storage used : ".concat(storageused);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                String Message = totalstorage.concat("\n").concat(storageused).concat("\n").concat(nooffiles).concat("\n").concat(noofimages).concat("\n").concat(noofpdf).concat("\n").concat(noofreports).concat("\n").concat(noofprescriptions).concat("\n");
//
//                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(HomeActivity.this)
//                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                        .setTitle("Storage Details")
//                        .setMessage(Message)
//                        .addButton("Buy Storage", Color.parseColor("#000000"), Color.parseColor("#ffffff"), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
//                            dialog.dismiss();
//                        });
//                // Show the alert
//                builder5.show();
//            }
//        });
        upgradenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "https://aarogyaecare.com/user/payment1.php?id=2&&email=";
//                String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                loadingbar1.show();
//                Intent intent = new Intent(HomeActivity.this,WebViewBlogs.class);
//                intent.putExtra("url",url.concat(Email));
//                intent.putExtra("name","Payment Link for Aarogya E Care");
//                loadingbar1.dismiss();
//                startActivity(intent);
                onBackPressed();
                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(HomeActivity.this);
                Random random = new Random();
                getrandom = random.nextInt(4);
                flatDialog.setTitle("Coming Soon!")
                        .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                        .setIcon(R.drawable.crown2)
                        .setSubtitle("Premium Users will get special access to the Premium Features and Services on Aarogya E Care App.")
                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                        .setFirstButtonText(reply.get(getrandom))
                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flatDialog.dismiss();
                            }
                        })
                        .show();
                flatDialog.setCanceledOnTouchOutside(true);
            }
        });


        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
        usageref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                use1=dataSnapshot.getValue(Usage.class);
                assert use1 != null;
                prog.setProgress((int)((100*Float.parseFloat(use1.getUsed()))/Float.parseFloat(use1.getTotal())));
                String numberAsString = String.format ("%.2f", (100*Float.valueOf(use1.getUsed()))/Float.valueOf(use1.getTotal()));
                storagepercent.setText("Storage ("+numberAsString+"% used)");
                String numberAsString1 = String.format ("%.2f", Float.valueOf(use1.getUsed()));
                storageoutof.setText(numberAsString1+"MB used out of "+use1.getTotal()+"MB");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(checkUser == null){
            Intent intent = new Intent(HomeActivity.this,Login.class);
            startActivity(intent);
        }
        else {

            myref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Self");
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users userDetails = dataSnapshot.getValue(Users.class);
                    String fname = userDetails.getFname().substring(0,1).toUpperCase()+userDetails.getFname().substring(1).toLowerCase();
                    username.setText(fname);
                    phonenumber.setText(userDetails.getPhone());
                    usertypeshow.setText("AEC_ID : "+userDetails.getMLID());
//                    if(userDetails.getUsertype().equals("PREMIUM")){
//                        MenuItem item = navigationView.findViewById(R.id.nav_add_family);
//                        item.setActionView(R.layout.menu_image);
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        FirebaseDatabase.getInstance().getReference("AdBannerImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long counts = dataSnapshot.getChildrenCount();
                adTotalCount = counts.intValue();
                sliderView.setSliderAdapter(new AdSliderAdapter(HomeActivity.this,adTotalCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        //Advertisement Recycler View
        FirebaseRecyclerOptions<Advertisement> options =
                new FirebaseRecyclerOptions.Builder<Advertisement>()
                        .setQuery(adRef.limitToFirst(2), Advertisement.class)
                        .build();
        recycler11 = findViewById(R.id.recycler_view_advertisement);
        layoutManager1 = new LinearLayoutManager(this);
        recycler11.setLayoutManager(layoutManager1);
        recycler11.setHasFixedSize(false);

        FirebaseRecyclerAdapter<Advertisement, AdViewHolder> adapter =
                new FirebaseRecyclerAdapter<Advertisement, AdViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull AdViewHolder holder, int position, @NonNull final Advertisement model) {
                        holder.ad_header.setText(model.getAd_header());

                        Picasso.get().load(model.getAd_image()).into(holder.ad_image);
                        String url = model.getAd_url();
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!url.isEmpty())
                                {
                                    loadingbar1.show();
                                    Intent intent = new Intent(HomeActivity.this,WebViewBlogs.class);
                                    intent.putExtra("url",url);
                                    intent.putExtra("name",model.getAd_header());
                                    loadingbar1.dismiss();
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item_layout, parent, false);
                        AdViewHolder holder = new AdViewHolder(view);
                        return holder;
                    }
                };
        adapter.startListening();
        recycler11.setAdapter(adapter);




    }

    //             CardViews Functions
    public void saveRecords_cardView(View view)
    {
//        fetchdata();
        Intent intent = new Intent(HomeActivity.this, SaveRecords.class);
//        intent.putExtra("useddata",pastused);
        startActivity(intent);
    }

//    private void fetchdata() {
//        loadingbar1.show();
//
//        usageref.child(checkUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                use=dataSnapshot.getValue(Usage.class);
//                pastused=use.getUsed();
//                pastused1=Float.valueOf(pastused)+5;
//                totalavai=Float.valueOf(use.getTotal());
////                Paper.book().write(prevalent.pastusedpre,pastused);
//                loadingbar1.dismiss();
//                if(pastused1>=totalavai)
//                {
//                    Toast.makeText(HomeActivity.this, "Memory full", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Intent intent = new Intent(HomeActivity.this, SaveRecords.class);
//                    intent.putExtra("useddata",pastused);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void consultDoctors_cardView(View view) {
        Random random = new Random();
        getrandom = random.nextInt(4);
        final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(this);
        flatDialog.setTitle("Coming Soon!")
                .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                .setBackgroundColor(Color.parseColor("#ffffff"))
                .setIcon(R.drawable.consult_doctor_pop)
                .setSubtitle("Now you will be able to Consult a Doctor using Aarogya E Care App.")
                .setSubtitleColor(getResources().getColor(android.R.color.black))
                .setFirstButtonText(reply.get(getrandom))
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flatDialog.dismiss();
                    }
                })
                .show();
        flatDialog.setCanceledOnTouchOutside(true);

    }

    public void orderMedicine_cardView(View view) {
        Random random = new Random();
        getrandom = random.nextInt(4);
        final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(this);
        flatDialog.setTitle("Coming Soon!")
                .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                .setBackgroundColor(Color.parseColor("#ffffff"))
                .setIcon(R.drawable.order_medicines)
                .setSubtitle("Now you will be able to Order Medicines from your nearest Medical Stores using Aarogya E Care App.")
                .setSubtitleColor(getResources().getColor(android.R.color.black))
                .setFirstButtonText(reply.get(getrandom))
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flatDialog.dismiss();
                    }
                })
                .show();
        flatDialog.setCanceledOnTouchOutside(true);

    }


    // advertisment cardviews

//    public void health_tip_videos(View view) {
//        Intent healthTipIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=DQniWOTizpA"));
//        startActivity(healthTipIntent);
//    }
//
//    public void corona_guidance_website(View view) {
//        Intent healthTipIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mohfw.gov.in/"));
//        startActivity(healthTipIntent);
//    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Exit")
                    .setMessage("Exit from Aarogya E care?")
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_add_family:
                DatabaseReference usertyperef;

                usertyperef=FirebaseDatabase.getInstance().getReference().child("Users");
                usertyperef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usertype=dataSnapshot.child("Self").child("topup").getValue(String.class);

                        if(usertype.equals("0"))
                        {
//                            Toast.makeText(HomeActivity.this, "Please upgrade your account to access this feature!", Toast.LENGTH_SHORT).show();
                            final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(HomeActivity.this);
                            flatDialog.setTitle("Add Family Member!")
                                    .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                                    .setBackgroundColor(Color.parseColor("#ffffff"))
                                    .setIcon(R.drawable.crown2)
                                    .setSubtitle("Through this feature user can add three family members to store their data, Buy Storage once to get access to this amazing feature.")
                                    .setSubtitleColor(getResources().getColor(android.R.color.black))
                                    .setFirstButtonText("Buy Storage")
                                    .withFirstButtonListner(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            flatDialog.dismiss();
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aarogyaecare.com/user/payment.php"));
                                            startActivity(browserIntent);
                                        }
                                    })
                                    .show();
                            flatDialog.setCanceledOnTouchOutside(true);
                        }
                        else
                        {
                            if(dataSnapshot.getChildrenCount()>=4)
                            {
//                                Toast.makeText(HomeActivity.this, "You have already added 3 members, to add unlimited members please upgrade to VIP account", Toast.LENGTH_SHORT).show();
                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(HomeActivity.this);
                                flatDialog.setTitle("Add Family Member!")
                                        .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                        .setIcon(R.drawable.crown2)
                                        .setSubtitle("You have reached to maximum limit of three family members, Aarogya E Care allows maximum three family members to store their data.")
                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                                        .setFirstButtonText("Alright")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                flatDialog.dismiss();
                                            }
                                        })
                                        .show();
                                flatDialog.setCanceledOnTouchOutside(true);
                            }
                            else
                            {
                                Intent intent = new Intent(HomeActivity.this,addfamilymember.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                overridePendingTransition(0,0);
                break;

//            case R.id.nav_app_info:
//                startActivity(new Intent(getApplicationContext(),AppInfoActivity.class));
//                overridePendingTransition(0,0);
//                break;
//
//            case R.id.nav_feed_suggest:
//                final com.operationsmiley.medilocker.FlatDialog flatDialog = new com.operationsmiley.medilocker.FlatDialog(this);
//                flatDialog.setTitle("Feedback / Suggestion")
//                        .setTitleColor(getResources().getColor(android.R.color.black))
//                        .setBackgroundColor(Color.parseColor("#ffffff"))
//                        .setFirstTextFieldHint("Subject")
//                        .setFirstTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
//                        .setFirstTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
//                        .setFirstTextFieldTextColor(getResources().getColor(android.R.color.black))
//                        .setLargeTextFieldHint("Write your Message here")
//                        .setLargeTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
//                        .setLargeTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
//                        .setLargeTextFieldTextColor(getResources().getColor(android.R.color.black))
//                        .setFirstButtonText("Send")
//                        .withFirstButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                DatabaseReference myref;
//                                String subject = flatDialog.getFirstTextField().trim();
//                                String feedback = flatDialog.getLargeTextField().trim();
//                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                Date currentTime = Calendar.getInstance().getTime();
//                                String timestamp = currentTime.toString();
//                                String timecheck = timestamp.substring(0,10);
//                                myref = FirebaseDatabase.getInstance().getReference().child("Feedback");
//                                HashMap<String, Object> dataMap = new HashMap<>();
//                                dataMap.put("subject",subject);
//                                dataMap.put("feedback",feedback);
//                                dataMap.put("timestamp",timestamp);
//                                dataMap.put("uid",userid);
//                                if(subject.isEmpty())
//                                {
//                                    Toast.makeText(HomeActivity.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (feedback.isEmpty())
//                                {
//                                    Toast.makeText(HomeActivity.this, "Feedback can't be Empty!", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    final int[] aman = {1};
//                                    Query query = myref.child(userid).orderByChild("uid");
//                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot delSnapshot: dataSnapshot.getChildren()) {
//                                                AdImageSliderModel model = delSnapshot.getValue(AdImageSliderModel.class);
//                                                if (model.getTimestamp().contains(timecheck)) {
//                                                    aman[0] = 0;
//                                                    Toast.makeText(HomeActivity.this, "You can send Feedback once a Day!", Toast.LENGTH_LONG).show();
//                                                    flatDialog.dismiss();
//                                                }
//                                            }
//                                            if(aman[0] == 1){
//                                                myref.child(userid).child(timestamp).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        flatDialog.dismiss();
//                                                        Toast.makeText(HomeActivity.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//                        })
//                        .show();
//                flatDialog.setCanceledOnTouchOutside(true);
//                break;
//
//
//            case R.id.nav_invite_friend:
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                String shareBody = "Hey There! I am using  MediLocker App.\n\nYou can store your all previous Medical Records at your provided MediLocker.\n\nDownload the App Now!:\nplay.google.com/store/apps/details?id=com.whatsapp&hl=en";
//                String shareSub = "MediLocker App";
//
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
//
//                startActivity(Intent.createChooser(shareIntent,"Share Using"));
//                break;

            case R.id.nav_sharedbyme:
                Intent intent04 = new Intent(this,sharedbyme.class);
                startActivity(intent04);
                break;

            case R.id.nav_sharedwithme:
                Intent intent05 = new Intent(this,sharedwithme.class);
                startActivity(intent05);
                break;

            case R.id.nav_setting:
                Intent intent03 = new Intent(this,SettingActivity.class);
                startActivity(intent03);
                break;

            case R.id.nav_payment:
                Intent intent10 = new Intent(this,payment.class);
                startActivity(intent10);
                break;


            case R.id.nav_logout:
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Signout")
                        .setMessage("Logout from Aarogya E care?")
                        .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            mAuth.signOut();

                            // Google sign out
                            mGoogleSignInClient.signOut();
                            Intent intent06 = new Intent(HomeActivity.this,Login.class);
                            startActivity(intent06);
                            dialog.dismiss();
                        }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                            dialog.dismiss();
                        });
                // Show the alert
                builder.show();
                break;
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private ChipNavigationBar.OnItemSelectedListener navLister = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            switch (i){
                case R.id.nav_home :
                    break;

                case R.id.nav_records :
                    startActivity(new Intent(getApplicationContext(),ViewAllFiles.class));
//                    Intent intent05 = new Intent(HomeActivity.this, ViewAllFiles.class);
//                    startActivity(intent05);
                    overridePendingTransition(0,0);
                    break;

                case R.id.nav_blogs :
                            Intent inte02 = new Intent(HomeActivity.this, BlogsActivity.class);
                            startActivity(inte02);
                            overridePendingTransition(0,0);
                            break;
//                            return true;

                case R.id.nav_notifications :
                    startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
//                            var.setCount(0);
                    overridePendingTransition(0,0);
                    break;

            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(refreshToken);
    }
}
