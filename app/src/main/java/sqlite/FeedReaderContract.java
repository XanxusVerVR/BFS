package sqlite;

import android.provider.BaseColumns;

public final class FeedReaderContract {//用來定義SQLite的資料庫與資料表長什麼樣子


    private FeedReaderContract() {
    }

    //定義要創造的資料表名稱與欄位
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "tab_order";//資料表名稱，全名為table_order，訂單資料表
        public static final String COLUMN_NAME_RAW_JSON = "raw_json";//要創造的欄位之一，儲存訂單json
        public static final String COLUMN_NAME_STATUS = "status";//要創造的欄位之二，儲存訂單狀態
    }

    //定義創造資料庫與資料表的SQL敘述字串
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_RAW_JSON + " TEXT," +
                    FeedEntry.COLUMN_NAME_STATUS + " TEXT)";
    //_ID直接拿來存訂單物件裡的serialNumber


    //定義刪除資料表的SQL敘述字串
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

}
