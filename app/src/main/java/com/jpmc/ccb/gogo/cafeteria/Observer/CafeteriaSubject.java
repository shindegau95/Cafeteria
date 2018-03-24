package com.jpmc.ccb.gogo.cafeteria.Observer;

/**
 * Created by Gaurav Shinde on 18-02-2018.
 */

public interface CafeteriaSubject {

    void registerObserver(CafeteriaObserver cafeteriaObserver);
    void removeObserver(CafeteriaObserver cafeteriaObserver);
    void notifyObservers();
}
