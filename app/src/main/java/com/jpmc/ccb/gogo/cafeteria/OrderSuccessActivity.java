package com.jpmc.ccb.gogo.cafeteria;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jpmc.ccb.gogo.cafeteria.Adapter.ReceiptOrderAdapter;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.model.ReceiptOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderSuccessActivity extends AppCompatActivity {

    private List<ReceiptOrder> receiptOrderList;
    private ReceiptOrderAdapter receiptOrderAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.circle_bg)
    ImageView circle_bg;
    @BindView(R.id.bg)
    ImageView bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Animation checkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.check_animation);
        circle_bg.startAnimation(checkAnimation);

        View bgView = findViewById(R.id.bg);
        reveal(bgView);
        updateUI();
    }

    private void updateUI() {
        receiptOrderList = CafeteriaApplication.getInstance().getReceiptOrderList();

        if(receiptOrderAdapter == null){
            receiptOrderAdapter = new ReceiptOrderAdapter(receiptOrderList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(receiptOrderAdapter);
        }else {
            receiptOrderAdapter.notifyDataSetChanged();
        }
    }

    public void reveal(View myView){
        // previously invisible view

        myView.setVisibility(View.INVISIBLE);

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            // set the view to visible without a circular reveal animation below Lollipop
            myView.setVisibility(View.VISIBLE);
        }
    }

}
