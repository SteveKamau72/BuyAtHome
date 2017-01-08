package com.softtech.stevekamau.buyathome.databaseHandlers;

/**
 * Created by Steve Kamau on 21-Jun-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.softtech.stevekamau.buyathome.model.CartModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class CartDB extends SQLiteOpenHelper {
    // All Static variables
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PID = "p_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String TAG = CartDB.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "BuyAtHome.db";
    // table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_NOTIFS = "notifications";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_SUBTOTAL = "subtotal";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_LANDMARK = "land_mark";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_PAYMENT_MODE = "payment_mode";
    private static final String KEY_SHIPPING = "shipping_mode";
    private static final String KEY_ORDER_AT = "order_at";
    private static final String ALLOWED_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm";
    HashMap<String, String> items_sold;
    ;
    Context context;
    private HashMap hp;

    public CartDB(Context c_context) {
        super(c_context, DATABASE_NAME, null, DATABASE_VERSION);
        context = c_context;

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PID + " INTEGER UNIQUE," + COLUMN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " TEXT," + COLUMN_IMAGE_PATH + " TEXT," + COLUMN_QUANTITY + " TEXT,"
                + COLUMN_SUBTOTAL + " TEXT," + COLUMN_DESC + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_CART_TABLE);

        Log.d(TAG, "Database tables created");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);
    }


    public void insertIntoCart(String product_id, String name, String amount, String description,
                               String image_path, String quantity, String subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("p_id", product_id);
        contentValues.put("name", name);
        contentValues.put("amount", amount);
        contentValues.put("description", description);
        contentValues.put("image_path", image_path);
        contentValues.put("quantity", quantity);
        contentValues.put("subtotal", subtotal);

        db.insert(TABLE_CART, null, contentValues);

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from cart where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        String countQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public boolean updateCart(Integer id, String name, int amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("amount", amount);
        contentValues.put("description", description);

        db.update("cart", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public void deleteCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }

    public boolean deleteSingleCartItem(int id) {
        String where = COLUMN_PID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CART, where, null) != 0;
    }

    public boolean checkForTables() {
        boolean hasRows = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CART, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0)
            hasRows = true;
        db.close();
        return hasRows;
    }

    public ArrayList<CartModel> getAllCartItems() {
        ArrayList<CartModel> cartList = new ArrayList<>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from cart", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            CartModel cartModel = new CartModel();
            cartModel.setId(res.getInt(res
                    .getColumnIndex("p_id")));
            cartModel.setTitle(res.getString(res
                    .getColumnIndex("name")));
            cartModel.setAmount((int) res.getFloat(res
                    .getColumnIndex("amount")));
            cartModel.setDescription(res.getString(res
                    .getColumnIndex("description")));
            cartModel.setImageFromPath(res.getString(res
                    .getColumnIndex("image_path")));
            cartModel.setQuantity(res.getString(res
                    .getColumnIndex("quantity")));
            cartList.add(cartModel);
            res.moveToNext();
        }
        res.close();
        return cartList;
    }

    public int getTotalOfAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(subtotal) FROM " + TABLE_CART, null);
        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i;
    }

    public ArrayList<HashMap> getSalesSold() {
        ArrayList<CartModel> cartList = new ArrayList<>();
        ArrayList<HashMap> purchaseHashMap = new ArrayList<>();
        HashMap<String, ArrayList<CartModel>> productsHash = new HashMap<String, ArrayList<CartModel>>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from cart", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            CartModel cartModel = new CartModel();
            cartModel.setId(res.getInt(res
                    .getColumnIndex("p_id")));
            cartModel.setTitle(res.getString(res
                    .getColumnIndex("name")));
            cartModel.setAmount((int) res.getFloat(res
                    .getColumnIndex("amount")));
            cartModel.setDescription(res.getString(res
                    .getColumnIndex("description")));
            cartModel.setImageFromPath(res.getString(res
                    .getColumnIndex("image_path")));
            cartModel.setQuantity(res.getString(res
                    .getColumnIndex("quantity")));
            cartList.add(cartModel);
            res.moveToNext();
        }
        res.close();
        HashMap<String, String> user = new HashMap<String, String>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("ACCOUNT", context.MODE_PRIVATE);
        user.put("phone", sharedPreferences.getString("phone", ""));
        user.put("city", sharedPreferences.getString("city", ""));
        user.put("land_mark", sharedPreferences.getString("landmark", ""));
        user.put("user_name", sharedPreferences.getString("name", ""));
        user.put("email", sharedPreferences.getString("email", ""));
        user.put("payment_mode", sharedPreferences.getString("payment_mode", ""));
        user.put("shipping_mode", sharedPreferences.getString("shipping_mode", ""));
        user.put("order_at", getDateTime());
        user.put("receipt_code", getTimeCode());

        productsHash.put("products", cartList);

        purchaseHashMap.add(user);
        purchaseHashMap.add(productsHash);
        return purchaseHashMap;
    }

    /* public HashMap<String, HashMap<String, String>> getSalesSold() {
         HashMap<String, String> user = new HashMap<String, String>();
         HashMap<String, HashMap<String, String>> sales = new HashMap<String, HashMap<String, String>>();
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor res = db.rawQuery("select * from cart", null);
         while (res.moveToNext()) {
             items_sold = new HashMap<String, String>();
             items_sold.put("p_id", res.getString(1));
             items_sold.put("name", res.getString(2));
             items_sold.put("amount", res.getString(3));
             items_sold.put("quantity", res.getString(5));
             items_sold.put("subtotal", res.getString(6));
             sales.put(String.valueOf(res.getInt(0)), items_sold);
             Log.d("cart_sales_items", sales.toString());
         }
         SharedPreferences sharedPreferences = context.getSharedPreferences("ACCOUNT", context.MODE_PRIVATE);
         user.put("phone", sharedPreferences.getString("phone", ""));
         user.put("city", sharedPreferences.getString("city", ""));
         user.put("land_mark", sharedPreferences.getString("landmark", ""));
         user.put("user_name", sharedPreferences.getString("name", ""));
         user.put("email", sharedPreferences.getString("email", ""));
         user.put("payment_mode", sharedPreferences.getString("payment_mode", ""));
         user.put("shipping_mode", sharedPreferences.getString("shipping_mode", ""));
         user.put("order_at", getDateTime());
         user.put("receipt_code", getTimeCode());
         sales.put("user", user);

         res.close();
         db.close();
         // return user
         Log.d("cart_sales", "Fetching user from Sqlite: " + sales.toString());

         return sales;
     }
 */
    String getDateTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    String getTimeCode() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss_");
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sdf.format(now) + sb.toString();

    }

    public void updateQuantity(int p_id, String quantity, String subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        contentValues.put("subtotal", subtotal);

        db.update("cart", contentValues, "p_id = ? ", new String[]{Integer.toString(p_id)});
    }

    /**
     * Storing user details in database
     */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user login status return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /*public boolean checkForTables() {
        boolean hasRows = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LOGIN, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0)
            hasRows = true;
        db.close();
        return hasRows;
    }*/
}
