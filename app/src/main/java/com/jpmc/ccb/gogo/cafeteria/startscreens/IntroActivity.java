package com.jpmc.ccb.gogo.cafeteria.startscreens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jpmc.ccb.gogo.cafeteria.MainActivity;
import com.jpmc.ccb.gogo.cafeteria.Others.PrefManager;
import com.jpmc.ccb.gogo.cafeteria.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gaurav Shinde on 03-02-2018.
 */

public class IntroActivity extends AppCompatActivity {

    private CustomPagerAdapter customPagerAdapter;
    private TextView[] dots;
    private int[] layouts;


    private ViewPager viewPager;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_intro);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ButterKnife.bind(this);
        //checking First Time Launch
        if(!prefManager.isFirstTimeLaunch()){
            launchHomeScreen();
            finish();
        }




        //layout array
        layouts = new int[]{
                R.layout.intro_slide_1,
                R.layout.intro_slide_2,
                R.layout.intro_slide_3,
                R.layout.intro_slide_4,
                R.layout.intro_slide_5,
                R.layout.intro_slide_6
        };

        addBottomDotsWithInitialPosition(0);


        customPagerAdapter = new CustomPagerAdapter();
        viewPager.setAdapter(customPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void addBottomDotsWithInitialPosition(int currentPage) {
        dots = new TextView[layouts.length];

        //initialize active and inactive color dots
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInActive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInActive[currentPage]);
        }

        if(dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @OnClick(R.id.btn_skip)
    public void OnBtnSkipClick(){
        launchHomeScreen();
    }

    @OnClick(R.id.btn_next)
    public void OnBtnNextClick(){
        int current = getItem(+1);
        if(current < layouts.length){
            //move to next screen
            viewPager.setCurrentItem(current);
        }
        else{
            launchHomeScreen();
        }
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }



    //View Page Adapter

    public class CustomPagerAdapter extends PagerAdapter{
        private LayoutInflater layoutInflater;

        public CustomPagerAdapter(){

        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDotsWithInitialPosition(position);

            //changing the next button text to 'GOT IT'
            if(position == layouts.length - 1){
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            }else{
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
