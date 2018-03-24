package com.jpmc.ccb.gogo.cafeteria.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpmc.ccb.gogo.cafeteria.R;
import com.jpmc.ccb.gogo.cafeteria.model.ReceiptOrder;

import java.util.List;

/**
 * Created by GauravShinde on 01-03-2018.
 */

public class ReceiptOrderAdapter extends RecyclerView.Adapter<ReceiptOrderAdapter.ReceiptOrderViewHolder>{
    private List<ReceiptOrder> receiptOrderList;
    private Context context;

    public ReceiptOrderAdapter(List<ReceiptOrder> receiptOrderList, Context context) {
        this.receiptOrderList = receiptOrderList;
        this.context = context;
    }

    @Override
    public ReceiptOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.final_order_list_row, parent, false);
        return new ReceiptOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptOrderViewHolder holder, int position) {
        ReceiptOrder receiptorder = receiptOrderList.get(position);
        holder.title.setText(receiptorder.getFoodItem().getFoodItem_name());
        int final_price = receiptorder.getQuantity()* receiptorder.getFoodItem().getPrice();
        holder.price.setText(context.getString(R.string.Rs) + final_price);
        holder.quantity.setText(String.valueOf(receiptorder.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return receiptOrderList.size();
    }

    public class ReceiptOrderViewHolder extends RecyclerView.ViewHolder{

        public TextView title, price, quantity;

        public ReceiptOrderViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.order_item_title);
            price = (TextView)itemView.findViewById(R.id.order_item_price);
            quantity = (TextView)itemView.findViewById(R.id.order_item_quantity);
        }
    }
}
