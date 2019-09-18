package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static sqlite.FeedReaderContract.SQL_CREATE_ENTRIES;
import static sqlite.FeedReaderContract.SQL_DELETE_ENTRIES;


public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;//如果要改變資料庫的建置，要更改此版本數字
    public static final String DATABASE_NAME = "bfs_lite.db";//定義資料庫名稱

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {//執行創造資料庫及資料表及欄位
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//使用建構子時如果版本增加,便會呼叫onUpgrade()刪除舊的資料表與其內容,再重新呼叫onCreate()建立新的資料表
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {//先不用理這個
        onUpgrade(db, oldVersion, newVersion);
    }
}
