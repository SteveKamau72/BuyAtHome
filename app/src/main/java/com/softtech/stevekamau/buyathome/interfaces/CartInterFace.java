package com.softtech.stevekamau.buyathome.interfaces;

/**
 * Created by steve on 8/13/16.
 */
public interface CartInterFace {
    void onQuantityChanged(int position, String quantity, float amount, Integer id);
    void onDeleted(Integer id);
}
