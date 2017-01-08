package com.softtech.stevekamau.buyathome.app;

/**
 * Created by Steve Kamau on 21-Jun-15.
 */
public class AppConfig {
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String TOPIC_PURCHASE = "purchases";
    public static final String ADD_PURCHASE_URL = "add_purchase.php";
    public static final String URL_ALL_PURCHASES = "all_purchases.php";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    public static final String UPDATE_PURCHASE_STATUS ="update_view_purchase.php" ;
    // Server user login url
    public static String URL_LOGIN = "http://softtech.comuv.com/buyathome/alternative/android_login_api/index.php";
    // Server user register url
    public static String URL_REGISTER = "http://softtech.comuv.com/buyathome/alternative/android_login_api/index.php";
    public static String PURCHASE_COMPLETE = "purchaseComplete";
}