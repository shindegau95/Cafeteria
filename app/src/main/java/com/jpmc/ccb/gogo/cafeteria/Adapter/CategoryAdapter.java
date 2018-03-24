package com.jpmc.ccb.gogo.cafeteria.Adapter;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.images.ImageManager;
import com.jpmc.ccb.gogo.cafeteria.MenuMainActivity;
import com.jpmc.ccb.gogo.cafeteria.R;
import com.jpmc.ccb.gogo.cafeteria.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gaurav Shinde on 07-02-2018.
 */


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{


    private List<Category> mDataset;
    private Context context;

    public CategoryAdapter(Context context, List<Category> mDataset ) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        CategoryViewHolder vh = new CategoryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder holder, final int position) {
        holder.category_textView.setText(mDataset.get(position).getCategory_name());

        int drawableId = context.getResources().getIdentifier("cover_"+mDataset.get(position).getCategory_name().toLowerCase().replace(" ",""), "drawable", context.getPackageName());
        if(drawableId == 0){
            drawableId = context.getResources().getIdentifier("cover_default", "drawable", context.getPackageName());
        }
        Glide.with(context).load(drawableId).into(holder.category_imageView);


        holder.category_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = MenuMainActivity.newIntent(context, mDataset.get(position).getCategory_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{


        TextView category_textView;
        ImageView category_imageView;
        CardView mCardView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            category_textView = (TextView)itemView.findViewById(R.id.category_name);
            category_imageView = (ImageView) itemView.findViewById(R.id.category_thumbnail);
            mCardView = (CardView)itemView.findViewById(R.id.card_view);
        }
    }

}
