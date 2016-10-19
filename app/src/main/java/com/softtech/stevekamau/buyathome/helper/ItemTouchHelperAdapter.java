package com.softtech.stevekamau.buyathome.helper;

/**
 * Created by steve on 7/18/16.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}