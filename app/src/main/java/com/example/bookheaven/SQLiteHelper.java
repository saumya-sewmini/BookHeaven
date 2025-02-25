package com.example.bookheaven;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "orderHistory.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "id";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_ORDER_STATUS = "order_status";

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the orders table
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TOTAL_PRICE + " DOUBLE, "
                + COLUMN_ORDER_DATE + " TEXT, "
                + COLUMN_ORDER_STATUS + " TEXT)";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    // Add a new order to the database
    public void insertOrder(OrderModel order) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_ORDERS + "("
                + COLUMN_TOTAL_PRICE + ", "
                + COLUMN_ORDER_DATE+ ", "
                + COLUMN_ORDER_STATUS + ") VALUES ('"
                + order.getTotal_Price() + "', '"
                + order.getDate_time() + "', '"
                + order.getOrder_Status() + "')";
        db.execSQL(insertQuery);
        db.close();
    }


    // Get all orders for a specific user
    public List<OrderModel> getOrders(int userId) {
        List<OrderModel> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") OrderModel orderModel = new OrderModel(

                        cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_STATUS))

                );


                orders.add(orderModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;

    }

    public void deleteOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ORDERS);
        db.close();
    }
}
