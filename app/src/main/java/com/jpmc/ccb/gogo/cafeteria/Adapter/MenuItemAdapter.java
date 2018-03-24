package com.jpmc.ccb.gogo.cafeteria.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.MenuMainActivity;
import com.jpmc.ccb.gogo.cafeteria.R;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;
import com.jpmc.ccb.gogo.cafeteria.model.FoodItem;

import java.util.List;

/**
 * Created by Gaurav Shinde on 07-02-2018.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuitemViewHolder>{

    private static Context context;
    private List<FoodOrder> orderlist;
    private CafeteriaApplication ca;
    private MenuMainActivity parentActivity;

    public MenuItemAdapter(Context context, List<FoodOrder> mOrderlist, CafeteriaApplication ca, MenuMainActivity menuMainActivity) {
        this.context = context;
        this.ca = ca;
        this.orderlist = mOrderlist;
        this.parentActivity = menuMainActivity;
    }

    @Override
    public MenuitemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuitem_card, parent, false);

        return new MenuitemViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MenuitemViewHolder holder, final int position) {

        final FoodOrder foodOrderItem = orderlist.get(position);
        holder.menuItem_name.setText(((FoodItem) foodOrderItem.getFoodItem()).getFoodItem_name());
        holder.price.setText(context.getString(R.string.Rs)+ foodOrderItem.getFoodItem().getPrice());

        holder.menuItemquantity.setText(String.valueOf(foodOrderItem.getQuantity()));


        holder.menuItemplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.menuItemquantity.setText(String.valueOf(foodOrderItem.incrementqty()));
                if(foodOrderItem.getQuantity()>0){
                    ca.addToFinalOrder(foodOrderItem);
                }

                notifyDataSetChanged();

            }
        });

        holder.menuItemminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foodOrderItem.getQuantity()>0) {
                    holder.menuItemquantity.setText(String.valueOf(foodOrderItem.decrementqty()));
                        ca.removeFromFinalOrder(foodOrderItem);
                }


                notifyDataSetChanged();
            }
        });
        if(foodOrderItem.getFoodItem().isVeg())
            holder.mIsVeg.setImageDrawable(context.getDrawable(R.drawable.veg));
        else
            holder.mIsVeg.setImageDrawable(context.getDrawable(R.drawable.non_veg));

    }


    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class MenuitemViewHolder extends RecyclerView.ViewHolder {
        public TextView menuItem_name, price;
        //public ImageView thumbnail;
        public CardView mCardView;
        public ImageView mIsVeg;
        public TextView menuItemplus;
        public TextView menuItemminus;
        public TextView menuItemquantity;

        public MenuitemViewHolder(View view) {
            super(view);

            menuItem_name = (TextView) view.findViewById(R.id.foodItem_name);
            price = (TextView)view.findViewById(R.id.price);
            //thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mIsVeg = (ImageView) view.findViewById(R.id.veg_icon);
            mCardView = (CardView)view.findViewById(R.id.card_view);

            menuItemplus = (TextView)view.findViewById(R.id.menuItem_plus);
            menuItemminus = (TextView)view.findViewById(R.id.menuItem_minus);
            menuItemquantity = (TextView)view.findViewById(R.id.menuItem_quantity);
        }
    }
}
