package qrtech.qrtechpay;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button scanButton;
    TextView textView;
    String result;
    SharedPreferences sharedPref;
    Context context;

    //DbHelper dbHelper;
    //SQLiteDatabase db;
    //boolean isZxingInstalled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView)findViewById(R.id.textView);
        scanButton = (Button) findViewById(R.id.scan);
        context = this;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //dbHelper = new DbHelper(this);
        //db = dbHelper.getWritableDatabase();
        sharedPref = context.getSharedPreferences("STORE_DATA", Context.MODE_PRIVATE);

        //SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);


/*
        TextView tvStatus = (TextView) findViewById(R.id.textView);
        tvStatus.setText(sharedPref.getString("result", "No Data"));
*/


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                //intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
                startActivityForResult(intent, 0);
            }
        });

        Button showdata = (Button) findViewById(R.id.Login);
        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = new DbHelper(getApplicationContext()).getWritableDatabase();
                db.create(null);
                //Cursor cursor = db.rawQuery("select * from CARD_TABLE", null);
                Cursor cursor = db.query(C.TABLE_NAME,null,null,null,null,null,null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        //place = cursor.getString( cursor.getColumnIndex("PLACE"));
                        String cardName = cursor.getString(cursor.getColumnIndex(C.CARD_NAME));
                        Toast.makeText(MainActivity.this, cardName, Toast.LENGTH_LONG).show();
                    }
                    int a = cursor.getCount();
                }
                //String cardName=cursor.getString(cursor.getColumnIndex(C.CARD_NAME));
                //Toast.makeText(MainActivity.this,cardName,Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                TextView tvStatus = (TextView) findViewById(R.id.abc);
                tvStatus.setText(data.getStringExtra("SCAN_RESULT"));
                result = data.getStringExtra("SCAN_RESULT");
                try {
                    addCardDetailsToDb(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("result", result);
                editor.commit();

                //DbHelper dbHelper = new DbHelper(this);
                //SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
                //db.create(null);

/*                ContentValues contentValues = new ContentValues();
                contentValues.put(C.CARD_BANK_NAME, "ICICI");
                contentValues.put(C.CARD_CVV, "786");
                contentValues.put(C.CARD_NAME, "RAMAKANT KUSHWAHA");
                contentValues.put(C.CARD_NUMBER, "622018320000954");
                contentValues.put(C.CARD_TYPE, "CREDIT");
                contentValues.put(C.CARD_VALID_FROM, "06/11");
                contentValues.put(C.CARD_VALID_THRU, "06/20");*/


/*
                if (db.isOpen()) {
                    Toast.makeText(MainActivity.this, "database open successfully", Toast.LENGTH_LONG).show();
                }
                long var = db.insert(C.TABLE_NAME, null, contentValues);
                if (var > 0) {
                    Toast.makeText(MainActivity.this, "Values Successfully Inserted in Table", Toast.LENGTH_LONG).show();
                }
*/

                //Cursor cursor = db.query(C.TABLE_NAME,null, null, null, null, null, null);
                //Cursor  cursor = db.rawQuery("select * from CARD_TABLE", null);
                //int a = cursor.getCount();
                //String cardName=cursor.getString(cursor.getColumnIndex(C.CARD_NAME));
                //Toast.makeText(MainActivity.this,cardName,Toast.LENGTH_LONG).show();
                //db.close();
            } else if (resultCode == RESULT_CANCELED) {
                //tvStatus.setText("Press a button to start a scan.");
                textView.setText("Scan cancelled.");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);
    }

    /*    @Override
        public Object onRetainNonConfigurationInstance() {
            HashMap<String, Object> savedValues = new HashMap<String, Object>();
            savedValues.put("someKey", someData);
            return savedValues;
        }*/
    public void addCardDetailsToDb(String response) throws JSONException {
        String newResponse = response;
        newResponse = newResponse.replace("\n", "").replace("\r", "");
        String myStrings[];
        myStrings = new String[] { C.CARD_NAME, C.CARD_NUMBER, C.CARD_BANK_NAME ,C.CARD_TYPE,C.CARD_VALID_THRU,C.CARD_VALID_FROM,C.CARD_CVV};
        JSONArray jsonArray = new JSONArray(newResponse);
        //for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonData = jsonArray.getJSONObject(0);
            String cardName = jsonData.getString(C.CARD_NAME);
            String cardNumber = jsonData.getString(C.CARD_NUMBER);
            String cardBankName = jsonData.getString(C.CARD_BANK_NAME);
            String cardType = jsonData.getString(C.CARD_TYPE);
            String cardValidThro = jsonData.getString(C.CARD_VALID_THRU);
            String cardValidFrom = jsonData.getString(C.CARD_VALID_FROM);
            String cardCvv = jsonData.getString(C.CARD_CVV);
            ContentValues cardValues = new ContentValues();
            cardValues.put(C.CARD_NAME, cardName);
            cardValues.put(C.CARD_BANK_NAME, cardBankName);
            cardValues.put(C.CARD_CVV, cardCvv);
            cardValues.put(C.CARD_NUMBER, cardNumber);
            cardValues.put(C.CARD_TYPE, cardType);
            cardValues.put(C.CARD_VALID_FROM, cardValidFrom);
            cardValues.put(C.CARD_VALID_THRU, cardValidThro);

            if(isDataPresentInDb(cardValues)){
                CustomDialogClass cdd = new CustomDialogClass(MainActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
            insertRowToDB(cardValues);

        //}

    }
    public void insertRowToDB(ContentValues values){
/*        SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
        db.create(null);
        if (db.isOpen()) {
            Toast.makeText(MainActivity.this, "database open successfully", Toast.LENGTH_LONG).show();
        }*/
        SQLiteDatabase db = openDB(this);
        long var = db.insert(C.TABLE_NAME, null, values);
        if (var > 0) {
            Toast.makeText(MainActivity.this, "Values Successfully Inserted in Table", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public boolean isDataPresentInDb(ContentValues values){
        SQLiteDatabase db = openDB(this);
        String[] columns = { C.CARD_NUMBER };
        String selection = C.CARD_NUMBER + " =?";
        String[] selectionArgs = { values.getAsString(C.CARD_NUMBER) };
        String limit = "1";
        Cursor cursor = db.query(C.TABLE_NAME,columns,selection,selectionArgs,null,null,null,limit);
        if(cursor.moveToFirst()){
            Toast.makeText(MainActivity.this,"Record Exist In Database",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public SQLiteDatabase openDB(Context context){
        SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
        db.create(null);
        if (db.isOpen()) {
            Toast.makeText(MainActivity.this, "database open successfully", Toast.LENGTH_LONG).show();
        }
        return db;
    }
}
