package com.jpmc.ccb.gogo.cafeteria.Applications;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jpmc.ccb.gogo.cafeteria.ConnectionPackage.ConnectivityReceiver;
import com.jpmc.ccb.gogo.cafeteria.Observer.CafeteriaObserver;
import com.jpmc.ccb.gogo.cafeteria.Observer.CafeteriaSubject;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;
import com.jpmc.ccb.gogo.cafeteria.model.ReceiptOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Shinde on 05-02-2018.
 */

public class CafeteriaApplication extends Application implements CafeteriaSubject {

    private static CafeteriaApplication mInstance;
    private List<FoodOrder> foodOrderList;
    private List<ReceiptOrder> receiptOrderList;
    private double walletAmount;
    private double entitlement;
    private static List<CafeteriaObserver> mObservers;
    private FirebaseFirestore db;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.setEntitlement(60);
        //creates order list by default
        getFoodOrderList();
        getReceiptOrderList();
    }

    public static synchronized CafeteriaApplication getInstance(){
        if(mObservers == null){
            mObservers = new ArrayList<>();
        }
        return mInstance;

    }

    public  void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public List<FoodOrder> getFoodOrderList() {
        if(foodOrderList == null){
            foodOrderList = new ArrayList<FoodOrder>();
        }
        return foodOrderList;
    }

    public void setFoodOrderList(List<FoodOrder> foodOrderList)
    {
        this.foodOrderList = foodOrderList;
        notifyObservers();
    }

    public List<ReceiptOrder> getReceiptOrderList() {
        if(receiptOrderList == null){
            receiptOrderList = new ArrayList<ReceiptOrder>();
        }
        return receiptOrderList;
    }

    public void setReceiptOrderList(List<ReceiptOrder> receiptOrderList) {
        this.receiptOrderList = receiptOrderList;
    }

    public void addToFinalOrder(FoodOrder foodOrder){
        foodOrderList = getFoodOrderList();
        if(!foodOrderList.contains(foodOrder)){
            foodOrderList.add(foodOrder);
        }
        notifyObservers();
    }

    public void removeFromFinalOrder(FoodOrder foodOrder){
        foodOrderList = getFoodOrderList();
        if(foodOrderList.contains(foodOrder) && foodOrder.getQuantity()==0) {
            foodOrderList.remove(foodOrder);
        }
        notifyObservers();
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        if(foodOrderList != null && !foodOrderList.isEmpty())
        {
            for(FoodOrder foodOrder : foodOrderList){
                totalPrice += (foodOrder.getFoodItem().getPrice() * foodOrder.getQuantity());
            }
        }
        return totalPrice;
    }

    public void removeAll() {
        foodOrderList.removeAll(foodOrderList);
        notifyObservers();
    }

    public double getWalletAmount(){
        double balance = (getEntitlement() - getTotalPrice())>0?(getEntitlement() - getTotalPrice()):0;
        return balance;
    }

    public double getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(double entitlement) {
        this.entitlement = entitlement;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Window window, int color){

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(getInstance(), color));
    }

    @Override
    public void registerObserver(CafeteriaObserver cafeteriaObserver) {
        if(!mObservers.contains(cafeteriaObserver)){
            mObservers.add(cafeteriaObserver);
        }
    }

    @Override
    public void removeObserver(CafeteriaObserver cafeteriaObserver) {
        if(mObservers.contains(cafeteriaObserver)){
            mObservers.remove(cafeteriaObserver);
        }
    }

    @Override
    public void notifyObservers() {

        for(CafeteriaObserver cafeteriaObserver: mObservers){
            cafeteriaObserver.dataChanged(getTotalPrice());
        }
    }

    public FirebaseFirestore getDb() {
        return db;
    }


    public void setRecieptOrderListUsingFoodOrder(List<FoodOrder> foodOrderList) {
        for (FoodOrder foodOrder : foodOrderList){
            ReceiptOrder receiptOrder = new ReceiptOrder(foodOrder.getFoodItem(), foodOrder.getQuantity());
            this.receiptOrderList.add(receiptOrder);
        }
    }
}
