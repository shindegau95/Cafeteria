package com.jpmc.ccb.gogo.cafeteria.Observer;

import com.jpmc.ccb.gogo.cafeteria.model.Category;

import java.util.List;

/**
 * Created by DELL on 26-02-2018.
 */

public interface CategorySubject {
    void registerObserver(CategoryObserver categoryObserver);
    void removeObserver(CategoryObserver categoryObserver);
    void notifyObservers(List<Category> categories);
}
