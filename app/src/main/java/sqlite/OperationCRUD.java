package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javabean.order.OrderBean;

import static java.lang.Integer.parseInt;

//此類別為定義SQLite新增、查詢、刪除、修改的操作方法
public class OperationCRUD {
    private static final String ERROR = "新增失敗";
    private FeedReaderDbHelper mDbHelper;
    private SQLiteDatabase db;
    private int serialNumber;
    private String status;
    private String rawOrderJson;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

    public OperationCRUD(Context context) { //定義建構子
        mDbHelper = new FeedReaderDbHelper(context);
    }

    public long insertOneOrder(OrderBean ob) {//新增一筆訂單
        deSerializationOrderBean(ob);

        db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry._ID, serialNumber);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RAW_JSON, rawOrderJson);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_STATUS, status);

        long newRowId = 0;//將新增的這筆訂單的serialNumber存放至這個變數
        // Insert the new row, returning the primary key value of the new row
        try {
            newRowId = db.insertOrThrow(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);//insertOrThrow可拋出SQLException例外，以方便捕捉加入重複serialNumber的錯誤例外
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(ERROR, "不能新增serialNumber一樣的訂單");
        }
        return newRowId;
    }

    public OrderBean getOneOrder(int serialNumber) throws Exception {//查詢單筆訂單，以serialNumber來查
        db = mDbHelper.getReadableDatabase();

        //定義要取出的欄位
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_RAW_JSON
        };

        //定義where敘述，找出_ID欄位等於serialNumber的訂單
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {
                String.valueOf(serialNumber)
        };
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        OrderBean oneBeanOrder = null;
        while (cursor.moveToNext()) {
            oneBeanOrder = getRecord(cursor);
        }
        cursor.close();
        if(Objects.equals(oneBeanOrder, null)){
            throw new Exception("回傳結果是空值，請再確認是否有這筆訂單");
        }
        else{
            return oneBeanOrder;
        }

    }

    public List<OrderBean> getAllOrder() {//查詢or取得所有訂單
        db = mDbHelper.getReadableDatabase();
        String[] projection = { //要查詢的欄位
//                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RAW_JSON
//                FeedReaderContract.FeedEntry.COLUMN_NAME_STATUS
        };
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                               // The sort order
        );

        List<OrderBean> result = new ArrayList<OrderBean>();
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    public OrderBean getRecord(Cursor cursor) {//將從SQLite取出的訂單的json，封裝成OrderBean物件
        // 準備回傳結果用的物件
        OrderBean enums = gson.fromJson(cursor.getString(0), OrderBean.class);
        return enums;
    }

    public Boolean deleteOrder(int serialNumber) {
        db = mDbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {
                String.valueOf(serialNumber)
        };
        return db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs) > 0;
    }
//    public void deleteTable() {//刪除資料表...未完成...不要用
//        db = mDbHelper.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME);
//    }

    public Boolean updateOrder(OrderBean ob) {//更新單筆訂單
        deSerializationOrderBean(ob);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RAW_JSON, rawOrderJson);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_STATUS, status);

        // Which row to update, based on the title
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = {
                String.valueOf(serialNumber)
        };

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        return count > 0;
    }

    public long getMostNewSerialNumber() {//取得最新一筆訂單的序號
        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT max("+FeedReaderContract.FeedEntry._ID+") FROM tab_order",null);
        int result = 0;
        while (cursor.moveToNext()) {
            try {
                result = parseInt(cursor.getString(0));
            }
            catch (RuntimeException e){//當沒有訂單的時候就回傳-1
                result = -1;
//                Log.d("沒有訂單會印出什麼", String.valueOf(result));
            }
        }
        cursor.close();
        return result;
    }
    public void deSerializationOrderBean(OrderBean ob) {//拆解Json物件，取出serialNumber、status、和訂單Json字串
        String orderJson = gson.toJson(ob);
        this.serialNumber = ob.getSerialNumber();
        this.status = ob.getStatus();
        this.rawOrderJson = orderJson;
    }
}
