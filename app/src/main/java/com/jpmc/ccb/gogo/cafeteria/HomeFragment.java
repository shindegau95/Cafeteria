package com.jpmc.ccb.gogo.cafeteria;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jpmc.ccb.gogo.cafeteria.Adapter.CategoryAdapter;
import com.jpmc.ccb.gogo.cafeteria.Factory.CategoryFactory;
import com.jpmc.ccb.gogo.cafeteria.Observer.CategoryObserver;
import com.jpmc.ccb.gogo.cafeteria.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gaurav Shinde on 06-02-2018.
 */

public class HomeFragment extends Fragment implements CategoryObserver{

    private RecyclerView mRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private static String TAG = "yourOrders";

    private List<Category> categories;
    private CategoryAdapter categoryAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_your_orders, container, false);

        Log.d(TAG, "kuch toh hua");
        firebaseFirestore = FirebaseFirestore.getInstance();

        CategoryFactory categoryFactory = CategoryFactory.getInstance();
        categories = categoryFactory.getCategoryList();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(categoryAdapter);


        categoryFactory.getCat(this);

        //fs.listenToCategoryChanges();
        return rootView;
    }


    @Override
    public void dataChanged(ArrayList<Category> categories) {
        categoryAdapter.notifyDataSetChanged();
    }


}
