package com.softtech.stevekamau.buyathome.databaseHandlers;

/**
 * Created by Steve Kamau on 28-Mar-15.
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


public class CartDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Cart.db";
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PID = "p_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    // Database Version
    private static final int DATABASE_VERSION = 2;
    Context context;
    HashMap<String, String> items_sold;
    private HashMap hp;

    public CartDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PID + " INTEGER UNIQUE," + COLUMN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " TEXT," + COLUMN_IMAGE_PATH + " TEXT," + COLUMN_QUANTITY + " TEXT,"
                + COLUMN_SUBTOTAL + " TEXT," + COLUMN_DESC + " TEXT" + ")";

        db.execSQL(CREATE_CART_TABLE);
               /* "create table cart " +
                        "(id integer primary key, name text,amount float,description text)"
        );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS cart");
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

    /* public void removeSingleContact(String name) {
         //Open the database
         SQLiteDatabase database = this.getWritableDatabase();

         //Execute sql query to remove from database
         //NOTE: When removing by String in SQL, value must be enclosed with ''
         database.execSQL("DELETE FROM " + TABLE_CART + " WHERE " + COLUMN_NAME + "= '" + name + "'");

         //Close the database
         database.close();
     }*/
    public HashMap<String, HashMap<String, String>> getSalesSold() {
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
        sales.put("user", user);
        res.close();
        db.close();
        // return user
        Log.d("cart_sales", "Fetching user from Sqlite: " + sales.toString());

        return sales;
    }

    String getDateTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    public void updateQuantity(int p_id, String quantity, String subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        contentValues.put("subtotal", subtotal);

        db.update("cart", contentValues, "p_id = ? ", new String[]{Integer.toString(p_id)});
    }
}

