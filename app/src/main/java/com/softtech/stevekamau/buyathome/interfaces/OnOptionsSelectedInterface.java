package com.softtech.stevekamau.buyathome.interfaces;

/**
 * Created by steve on 10/20/16.
 */
public interface OnOptionsSelectedInterface {
    void onShareButtonclicked(String name, String amount);
    void onAddToCartButtonClicked(int id, String name, String amount, String details, String encodedImageData, String s, String mAmount);
    void onAddToWishList(int id, String name, String amount, String details, String encodedImageData, String s, String mAmount);
    void onDeleteItem();
}
