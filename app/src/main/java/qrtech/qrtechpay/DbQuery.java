package qrtech.qrtechpay;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ramakant on 11/28/2015.
 */
public class DbQuery {
    Context mContext;

    public DbQuery(Context context){
        mContext = context;
    }

    DbHelper dbHelper = new DbHelper(mContext);
    SQLiteDatabase db = dbHelper.getWritableDatabase();

}
