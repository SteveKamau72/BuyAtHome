package com.softtech.stevekamau.buyathome.databaseHandlers;

/**
 * Created by Steve Kamau on 02-Jul-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.softtech.stevekamau.buyathome.model.CartModel;

import java.util.ArrayList;
import java.util.HashMap;


public class WishDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WishListDB";
    public static final String TABLE_WISHLIST = "wishlistTable";
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

    private HashMap hp;

    public WishDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WISHLIST_TABLE = "CREATE TABLE " + TABLE_WISHLIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PID + " INTEGER UNIQUE," + COLUMN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " TEXT," + COLUMN_IMAGE_PATH + " TEXT," + COLUMN_QUANTITY + " TEXT,"
                + COLUMN_SUBTOTAL + " TEXT," + COLUMN_DESC + " TEXT" + ")";

        db.execSQL(CREATE_WISHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public void insertIntoWishList(String product_id, String name, String amount, String description,
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

        db.insert(TABLE_WISHLIST, null, contentValues);

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from favourites where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        String countQuery = "SELECT  * FROM " + TABLE_WISHLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


   /* public boolean updateContact(Integer id, String title, Integer amount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("amount", amount);
        contentValues.put("description", description);

        db.update("favourites", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }*/

   /* public void deleteAllWishList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favourites", null, null);
        db.close();
    }*/


    public boolean checkForTables() {
        boolean hasRows = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_WISHLIST, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0)
            hasRows = true;
        db.close();
        return hasRows;
    }

    public ArrayList<CartModel> getAllWishListItems() {
        ArrayList<CartModel> wishlist = new ArrayList<>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from wishlistTable", null);
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
            wishlist.add(cartModel);
            res.moveToNext();
        }
        res.close();
        return wishlist;
    }

    public int getTotalOfAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_WISHLIST, null);
        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i;
    }

    public boolean deleteSingleWishList(int id) {
        String where = COLUMN_PID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WISHLIST, where, null) != 0;
    }
}


