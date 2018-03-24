package com.jpmc.ccb.gogo.cafeteria;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jpmc.ccb.gogo.cafeteria.Applications.CafeteriaApplication;
import com.jpmc.ccb.gogo.cafeteria.Comparators.IdComparator;
import com.jpmc.ccb.gogo.cafeteria.Observer.CategoryObserver;
import com.jpmc.ccb.gogo.cafeteria.Observer.CategorySubject;
import com.jpmc.ccb.gogo.cafeteria.model.Category;
import com.jpmc.ccb.gogo.cafeteria.model.FoodItem;
import com.jpmc.ccb.gogo.cafeteria.model.FoodOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gaurav Shinde on 26-02-2018.
 */

public class FirestoreService implements CategorySubject {

    private static final String TAG = "FIRESTORESERVICE" ;
    private static FirebaseFirestore db;
    private List<Category> categories;
    private static List<CategoryObserver> observers;

    private static FirestoreService firestoreService;

    public FirestoreService() {
        db = FirebaseFirestore.getInstance();
        observers = new ArrayList<CategoryObserver>();
    }

/*
    public static FirestoreService getInstance(){
        if(firestoreService==null) {
            firestoreService = new FirestoreService();
            db = FirebaseFirestore.getInstance();
            categories = new ArrayList<Category>();
            observers = new ArrayList<CategoryObserver>();
        }
        return firestoreService;
    }*/



    public void refreshCategories(List<Category> categoryList){

        this.categories = categoryList;
        db.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                //first clear the list
                categories.clear();
                CafeteriaApplication.getInstance().removeAll();
                if(e != null){
                    Log.d(TAG, "Error : " + e.getMessage());
                }

                for(final DocumentSnapshot documentSnapshot:  documentSnapshots){
                    //add them to categories
                    final Category category = documentSnapshot.toObject(Category.class);
                    categories.add(category);


                    documentSnapshot.getReference().collection("foodOrderList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnaps, FirebaseFirestoreException e) {
                            for(final DocumentSnapshot documentSnap:  documentSnaps){
                                Log.d(TAG, documentSnap.getData().values().toString());

                                int i = Integer.parseInt(documentSnap.get("foodItem_id").toString());
                                db.collection("FoodItems").whereEqualTo("foodItem_id", i)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                                Log.d(TAG, documentSnapshots.toString());
                                                for (DocumentSnapshot d : documentSnapshots){
                                                    FoodItem foodItem = d.toObject(FoodItem.class);
                                                    Log.d(TAG, foodItem.toString());
                                                    category.addToCategory(foodItem);
                                                }
                                            }
                                        });
                            }
                        }
                    });

                    notifyObservers(categories);
                }

                Log.d(TAG, categories.toString());
            }
        });

    }



    @Override
    public void registerObserver(CategoryObserver categoryObserver) {
        if(!observers.contains(categoryObserver)){
            observers.add(categoryObserver);
        }
    }

    @Override
    public void removeObserver(CategoryObserver categoryObserver) {
        if(observers.contains(categoryObserver)){
            observers.remove(categoryObserver);
        }
    }

    @Override
    public void notifyObservers(List<Category> categories) {
        Collections.sort(categories,new IdComparator());
        for (CategoryObserver categoryObserver : observers){
            categoryObserver.dataChanged((ArrayList<Category>) categories);
        }
    }

}


