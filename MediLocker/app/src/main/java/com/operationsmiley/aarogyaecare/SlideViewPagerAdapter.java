package com.operationsmiley.aarogyaecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context ctx;

    public SlideViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override

    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container,false);
        ImageView logo = view.findViewById(R.id.logo);
        TextView tittle = view.findViewById(R.id.tittle);
        TextView desc = view.findViewById(R.id.desc);

        ImageView next = view.findViewById(R.id.btnNext);
        ImageView back = view.findViewById(R.id.btnBack);
       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SlideActivity.viewPager.setCurrentItem(position + 1);
           }
       });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SlideActivity.viewPager.setCurrentItem(position - 1);

            }
        });

        switch (position)
        {
            case 0:
                logo.setImageResource(R.drawable.access_documents);
                tittle.setText("Bringing Best Personal Healthcare for you");
                desc.setText("Consult Doctors, Order Medicines, Find Best Healthcare Providers");
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            break;

            case 1:
                logo.setImageResource(R.drawable.cloud_storage);
                tittle.setText("Your Personal Storage at Cloud");
                desc.setText("Store Unlimited Documents in Your provided Storage");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;

            case 2:
                logo.setImageResource(R.drawable.medical_record);
                tittle.setText("Access Documents Anytime, Anywhere");
                desc.setText("Access the Documents from Any place at Anytime");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
