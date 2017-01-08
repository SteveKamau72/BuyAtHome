package com.softtech.stevekamau.buyathome.databaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.softtech.stevekamau.buyathome.model.PurchasesModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steve on 12/6/16.
 */

public class PurchasesDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotificationsDB";
    public static final String TABLE_NOTIF = "notificationsTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PID = "p_id";
    public static final String COLUMN_PURCHASE_ID = "purchase_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_SUBTOTAL = "subtotal";
    public static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_CITY = "city";
    private static final String KEY_LANDMARK = "land_mark";
    private static final String KEY_PAYMENT = "payment";
    private static final String KEY_PAYMENT_MODE = "payment_mode";
    private static final String KEY_SHIPPING = "shipping_mode";
    private static final String KEY_ORDER_AT = "order_at";
    private static final String KEY_RECEIPT_CODE = "receipt_code";
    private static final String KEY_VIEWED_AT = "viewed_at";
    private static final String KEY_VIEWED_BY = "viewed_by";
    private static final String KEY_COMPELETION_STATUS = "completion_status";
    // Database Version
    private static final int DATABASE_VERSION = 5;

    private HashMap hp;

    public PurchasesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PURCHASES_TABLE = "CREATE TABLE " + TABLE_NOTIF + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PID + " TEXT,"
                + COLUMN_PURCHASE_ID + " INTEGER UNIQUE," + COLUMN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " TEXT," + COLUMN_IMAGE_PATH + " TEXT," + COLUMN_QUANTITY + " TEXT,"
                + COLUMN_SUBTOTAL + " TEXT," + KEY_PHONE + " TEXT," + KEY_USERNAME + " TEXT,"
                + KEY_CITY + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_LANDMARK + " TEXT," + KEY_PAYMENT + " TEXT," + KEY_PAYMENT_MODE + " TEXT,"
                + KEY_SHIPPING + " TEXT," + KEY_ORDER_AT + " TEXT," + KEY_RECEIPT_CODE + " TEXT,"
                + KEY_VIEWED_AT + " TEXT," + KEY_VIEWED_BY + " TEXT," + KEY_COMPELETION_STATUS + " TEXT,"
                + COLUMN_DESC + " TEXT" + ")";

        db.execSQL(CREATE_PURCHASES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notificationsTable");
        onCreate(db);
    }

    public void insertIntoPurchasesList(String purchase_id, String product_id, String name,
                                        String amount, String description, String email,
                                        String quantity, String phone, String subtotal, String user_name, String city,
                                        String land_mark, String payment, String payment_mode, String shipping_mode,
                                        String order_at, String receipt_code, String viewed_at,
                                        String viewed_by, String completion_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("p_id", product_id);
        contentValues.put("purchase_id", purchase_id);
        contentValues.put("name", name);
        contentValues.put("amount", amount);
        contentValues.put("description", description);
        contentValues.put("quantity", quantity);
        contentValues.put("subtotal", subtotal);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("user_name", user_name);
        contentValues.put("city", city);
        contentValues.put("land_mark", land_mark);
        contentValues.put("payment", payment);
        contentValues.put("payment_mode", payment_mode);
        contentValues.put("shipping_mode", shipping_mode);
        contentValues.put("order_at", order_at);
        contentValues.put("receipt_code", receipt_code);
        contentValues.put("viewed_at", viewed_at);
        contentValues.put("viewed_by", viewed_by);
        contentValues.put("completion_status", completion_status);
        Log.d("notif_inserted", completion_status);
        db.insertWithOnConflict(TABLE_NOTIF, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

    }


    public int numberOfRows() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTIF;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int numberOfUnViewdNotifs() {
        //String countQuery = "SELECT  * FROM " + TABLE_NOTIF;
        String countQuery = "SELECT  * FROM " + TABLE_NOTIF + " WHERE " + KEY_VIEWED_AT + "!= '0'";
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
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NOTIF, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0)
            hasRows = true;
        db.close();
        return hasRows;
    }

    public ArrayList<PurchasesModel> getAllPurchasedItems() {
        ArrayList<PurchasesModel> purchasesModelsArrayList = new ArrayList<>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from notificationsTable order by purchase_id DESC", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            PurchasesModel purchasesModel = new PurchasesModel();
            purchasesModel.setPurchaseId(res.getString(res
                    .getColumnIndex("purchase_id")));
            purchasesModel.setUser_name(res.getString(res
                    .getColumnIndex("user_name")));
            purchasesModel.setP_name(res.getString(res
                    .getColumnIndex("name")));
            purchasesModel.setQuantity(res.getString(res
                    .getColumnIndex("quantity")));
            purchasesModel.setOrder_at(res.getString(res
                    .getColumnIndex("order_at")));
            purchasesModel.setSub_total(res.getString(res
                    .getColumnIndex("subtotal")));
            purchasesModel.setPhone(res.getString(res
                    .getColumnIndex("phone")));
            purchasesModel.setCity(res.getString(res
                    .getColumnIndex("city")));
            purchasesModel.setEmail(res.getString(res
                    .getColumnIndex("email")));
            purchasesModel.setLandmark(res.getString(res
                    .getColumnIndex("land_mark")));
            purchasesModel.setPayment_mode(res.getString(res
                    .getColumnIndex("payment_mode")));
            purchasesModel.setShipping_mode(res.getString(res
                    .getColumnIndex("shipping_mode")));
            purchasesModel.setReceipt_code(res.getString(res
                    .getColumnIndex("receipt_code")));
            purchasesModel.setViewedAt(res.getString(res
                    .getColumnIndex("viewed_at")));
            purchasesModel.setViewedBy(res.getString(res
                    .getColumnIndex("viewed_by")));
            purchasesModel.setCompletionStatus(res.getString(res
                    .getColumnIndex("completion_status")));
            purchasesModelsArrayList.add(purchasesModel);
            res.moveToNext();
        }
        res.close();
        return purchasesModelsArrayList;
    }

    public void updateViewStatus(String purchase_id, String viewed_at, String viewed_by) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("UPDATE " + TABLE_NOTIF + " SET " + KEY_VIEWED_AT + " = '"
                + viewed_at + "'" + " WHERE " + COLUMN_PURCHASE_ID + "= '" + purchase_id + "'");

    }

    public int getTotalOfAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(subtotal) FROM " + TABLE_NOTIF, null);
        c.moveToFirst();
        int i = c.getInt(0);
        c.close();
        return i;
    }

    public ArrayList<PurchasesModel> getRelatedPurchasedItems(String receipt_code) {
        ArrayList<PurchasesModel> purchasesModelsArrayList = new ArrayList<>();
        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NOTIF + " WHERE "
                + KEY_RECEIPT_CODE + " = '" + receipt_code + "'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            PurchasesModel purchasesModel = new PurchasesModel();
            purchasesModel.setPurchaseId(res.getString(res
                    .getColumnIndex("purchase_id")));
            purchasesModel.setUser_name(res.getString(res
                    .getColumnIndex("user_name")));
            purchasesModel.setP_name(res.getString(res
                    .getColumnIndex("name")));
            purchasesModel.setQuantity(res.getString(res
                    .getColumnIndex("quantity")));
            purchasesModel.setOrder_at(res.getString(res
                    .getColumnIndex("order_at")));
            purchasesModel.setSub_total(res.getString(res
                    .getColumnIndex("subtotal")));
            purchasesModel.setPhone(res.getString(res
                    .getColumnIndex("phone")));
            purchasesModel.setCity(res.getString(res
                    .getColumnIndex("city")));
            purchasesModel.setEmail(res.getString(res
                    .getColumnIndex("email")));
            purchasesModel.setLandmark(res.getString(res
                    .getColumnIndex("land_mark")));
            purchasesModel.setPayment_mode(res.getString(res
                    .getColumnIndex("payment_mode")));
            purchasesModel.setShipping_mode(res.getString(res
                    .getColumnIndex("shipping_mode")));
            purchasesModel.setReceipt_code(res.getString(res
                    .getColumnIndex("receipt_code")));
            purchasesModel.setViewedAt(res.getString(res
                    .getColumnIndex("viewed_at")));
            purchasesModel.setViewedBy(res.getString(res
                    .getColumnIndex("viewed_by")));
            purchasesModelsArrayList.add(purchasesModel);
            res.moveToNext();
        }
        res.close();
        return purchasesModelsArrayList;
    }

    public boolean deleteSingleNotif(int id) {
        String where = COLUMN_PID + "=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTIF, where, null) != 0;
    }

    public void updateCompletionStatus(String purchaseId, String value) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("UPDATE " + TABLE_NOTIF + " SET " + KEY_COMPELETION_STATUS + " = '"
                + value + "'" + " WHERE " + COLUMN_PURCHASE_ID + "= '" + purchaseId + "'");
    }
}


