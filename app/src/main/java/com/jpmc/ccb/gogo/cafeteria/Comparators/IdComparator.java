package com.jpmc.ccb.gogo.cafeteria.Comparators;

import com.jpmc.ccb.gogo.cafeteria.model.Category;

import java.util.Comparator;

/**
 * Created by GauravShinde  on 26-02-2018.
 */

public class IdComparator implements Comparator<Category>{

    @Override
    public int compare(Category category1, Category category2) {
        if (category1.getCategory_id()<category2.getCategory_id()){
            return -1;
        }else if (category1.getCategory_id()>category2.getCategory_id()){
            return 1;
        }else
            return 0;
    }
}
