package qrtech.qrtechpay;

import android.os.Environment;

import java.io.File;

/**
 * Created by Ramakant on 11/28/2015.
 */
public class C {

    public static final File DATABASE_FILE_PATH      = Environment.getExternalStorageDirectory();
    public static final String TABLE_NAME = "CARD_TABLE";
    public static final String _ID = "_ID";
    public static final String CARD_NAME = "CARD_NAME";
    public static final String CARD_NUMBER = "CARD_NUMBER";
    public static final String CARD_TYPE = "CARD_TYPE";
    public static final String CARD_CVV = "CARD_CVV";
    public static final String CARD_VALID_THRU = "CARD_VALID_THRU";
    public static final String CARD_VALID_FROM = "CARD_VALID_FROM";
    public static final String CARD_BANK_NAME = "CARD_VALID_FROM";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY autoincrement," +
                    CARD_BANK_NAME + TEXT_TYPE + COMMA_SEP +
                    CARD_NAME + TEXT_TYPE + COMMA_SEP +
                    CARD_TYPE + TEXT_TYPE + COMMA_SEP +
                    CARD_NUMBER + TEXT_TYPE + COMMA_SEP +
                    CARD_CVV + TEXT_TYPE + COMMA_SEP +
                    CARD_VALID_THRU + TEXT_TYPE + COMMA_SEP +
                    CARD_VALID_FROM + TEXT_TYPE + COMMA_SEP +

            " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


}
