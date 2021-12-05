package com.operationsmiley.aarogyaecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SlideActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    SlideViewPagerAdapter adapter;
    LinearLayout dotsLayout;
    ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager = findViewById(R.id.viewpager);
        dotsLayout = findViewById(R.id.dots);
        adapter = new SlideViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);



        if(isOpenAlread()){

            Intent intent = new Intent(SlideActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else{
            SharedPreferences.Editor editor = getSharedPreferences("slides", MODE_PRIVATE).edit();
            editor.putBoolean("slide", true);
            editor.commit();
        }
    }

    private  boolean isOpenAlread(){
        SharedPreferences sharedPreferences = getSharedPreferences("slide", MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("slide",false);
        return result;
    }

    public void getStarted(View view){
        Intent intent = new Intent(SlideActivity.this,Login.class);
        startActivity(intent);
    }


    private void addDots(int position){

        dots = new ImageView[3];
        dotsLayout.removeAllViews();

        for (int i=0; i<dots.length; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            dots[i].setLayoutParams(params);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length>0){

            dots[position].setImageResource(R.drawable.selected);
        }

    }

    ViewPager.OnPageChangeListener changeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}


