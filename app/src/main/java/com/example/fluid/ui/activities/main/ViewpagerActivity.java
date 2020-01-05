package com.example.fluid.ui.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fluid.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ViewPager2 viewPager = findViewById(R.id.viewPager2);
        TabLayout tabLayout = findViewById(R.id.tabs);

        List<String> list = new ArrayList<>();
        list.add("First Screen");
        list.add("Second Screen");
        list.add("Third Screen");
        list.add("Fourth Screen");

        viewPager.setAdapter(new ViewPagerAdapter(this, list, viewPager));
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("Tab " + (position + 1));
                    }
                }).attach();

    }

    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;
        private ViewPager2 viewPager2;
        private int[] colorArray = new int[]{android.R.color.black, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_dark};

        ViewPagerAdapter(Context context, List<String> data, ViewPager2 viewPager2) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            this.viewPager2 = viewPager2;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_viewpager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String animal = mData.get(position);
            holder.myTextView.setText(animal);
            holder.relativeLayout.setBackgroundResource(colorArray[position]);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView myTextView;
            RelativeLayout relativeLayout;
            Button button;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.tvTitle);
                relativeLayout = itemView.findViewById(R.id.container);
                button = itemView.findViewById(R.id.btnToggle);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL)
                            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        else {
                            viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                        }
                    }
                });
            }
        }

    }
}