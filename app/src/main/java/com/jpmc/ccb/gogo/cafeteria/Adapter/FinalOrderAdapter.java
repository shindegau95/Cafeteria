package com.jpmc.ccb.gogo.cafeteria.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmc.ccb.gogo.cafeteria.R;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;

import java.util.List;

/**
 * Created by Gaurav Shinde on 08-02-2018.
 */

public class FinalOrderAdapter extends RecyclerView.Adapter<FinalOrderAdapter.FinalOrderHolder > {
    private List<FoodOrder> foodOrderList;
    private Context context;

    public FinalOrderAdapter(List<FoodOrder> foodOrderList, Context context) {
        this.foodOrderList = foodOrderList;
        this.context = context;
    }

    @Override
    public FinalOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_order_list_row, parent, false);
        return new FinalOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(FinalOrderHolder holder, int position) {
        FoodOrder foodOrder = foodOrderList.get(position);
        holder.title.setText(foodOrder.getFoodItem().getFoodItem_name());
        int final_price = foodOrder.getQuantity()* foodOrder.getFoodItem().getPrice();
        holder.price.setText(context.getString(R.string.Rs) + final_price);
        holder.quantity.setText(String.valueOf(foodOrder.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return foodOrderList.size();
    }

    public class FinalOrderHolder extends RecyclerView.ViewHolder {
        public TextView title, price, quantity;

        public FinalOrderHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.order_item_title);
            price = (TextView)itemView.findViewById(R.id.order_item_price);
            quantity = (TextView)itemView.findViewById(R.id.order_item_quantity);
        }
    }
}
