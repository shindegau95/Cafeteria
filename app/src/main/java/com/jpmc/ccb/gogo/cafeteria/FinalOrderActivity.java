package com.jpmc.ccb.gogo.cafeteria;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.jpmc.ccb.gogo.cafeteria.Adapter.FinalOrderAdapter;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.Factory.CategoryFactory;
import com.jpmc.ccb.gogo.cafeteria.Observer.CafeteriaObserver;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinalOrderActivity extends AppCompatActivity {

    private static List<FoodOrder> foodOrderList;
    private static int totalPrice;
    private FinalOrderAdapter finalOrderAdapter;
    private Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.total_price)
    TextView totalPriceText;
    @BindView(R.id.checkout)
    AppCompatButton checkOutButton;
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.remove_orders)
    Button btnRemoveOrders;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order);
        ButterKnife.bind(this);

        foodOrderList = new ArrayList<>();
        updateUI();

        walletBalance.setText(getString(R.string.Rs) + String.valueOf(CafeteriaApplication.getInstance().getWalletAmount()));


        if(totalPrice>0) {
            totalPriceText.setText("Total Price is " + getString(R.string.Rs) + String.valueOf(totalPrice));
        }
        /*else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.activity_no_orders);
        }*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void updateUI(){
        foodOrderList = CafeteriaApplication.getInstance().getFoodOrderList();
        totalPrice = CafeteriaApplication.getInstance().getTotalPrice();
        totalPriceText.setText(String.valueOf(totalPrice));

        if(finalOrderAdapter == null){
            finalOrderAdapter = new FinalOrderAdapter(foodOrderList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(finalOrderAdapter);
        }else {
            finalOrderAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.checkout)
    public void onCheckOutButtonClick(){
        CafeteriaApplication.getInstance().setRecieptOrderListUsingFoodOrder(foodOrderList);
        CafeteriaApplication.getInstance().removeAll();
        CategoryFactory.resetInstance();
        Intent intent = new Intent(FinalOrderActivity.this, OrderSuccessActivity.class);
        startActivity(intent);
        //restartApplication();
    }

    @OnClick(R.id.remove_orders)
    public void onRemoveOrdersClick(){
        CafeteriaApplication.getInstance().removeAll();
        CategoryFactory.resetInstance();
        Intent intent = new Intent(FinalOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void restartApplication(){
        Intent i = FinalOrderActivity.this.getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( FinalOrderActivity.this.getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : onBackPressed();
                                return true;
        }
        return false;
    }
}
