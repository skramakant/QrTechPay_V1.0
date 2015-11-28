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
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

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
        scanButton = (Button)findViewById(R.id.scan);
        context = this;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //dbHelper = new DbHelper(this);
        //db = dbHelper.getWritableDatabase();
        sharedPref = context.getSharedPreferences("STORE_DATA", Context.MODE_PRIVATE);

        //SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);



        TextView tvStatus=(TextView)findViewById(R.id.textView);
        tvStatus.setText(sharedPref.getString("result", "No Data"));


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                //intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
                startActivityForResult(intent, 0);
            }
        });

        Button showdata = (Button)findViewById(R.id.button);
        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = new DbHelper(getApplicationContext()).getWritableDatabase();
                db.create(null);
                Cursor  cursor = db.rawQuery("select * from CARD_TABLE", null);
                if(cursor != null){
                    if (cursor.moveToFirst()){
                        //place = cursor.getString( cursor.getColumnIndex("PLACE"));
                        String cardName=cursor.getString(cursor.getColumnIndex(C.CARD_NAME));
                        Toast.makeText(MainActivity.this,cardName,Toast.LENGTH_LONG).show();
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
        if(requestCode == 0){
            if (resultCode == RESULT_OK) {
                //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                TextView tvStatus=(TextView)findViewById(R.id.textView);
                tvStatus.setText(data.getStringExtra("SCAN_RESULT"));
                result = data.getStringExtra("SCAN_RESULT");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("result", result);
                editor.commit();

                DbHelper dbHelper = new DbHelper(this);
                SQLiteDatabase db = new DbHelper(this).getWritableDatabase();
                db.create(null);

                ContentValues contentValues = new ContentValues();
                contentValues.put(C.CARD_BANK_NAME,"ICICI");
                contentValues.put(C.CARD_CVV,"786");
                contentValues.put(C.CARD_NAME,"RAMAKANT KUSHWAHA");
                contentValues.put(C.CARD_NUMBER,"622018320000954");
                contentValues.put(C.CARD_TYPE,"CREDIT");
                contentValues.put(C.CARD_VALID_FROM,"06/11");
                contentValues.put(C.CARD_VALID_THRU, "06/20");


                if(db.isOpen()){
                    Toast.makeText(MainActivity.this,"database open successfully",Toast.LENGTH_LONG).show();
                }
               long var = db.insert(C.TABLE_NAME, null, contentValues);
                if(var > 0){
                    Toast.makeText(MainActivity.this,"Values Successfully Inserted in Table",Toast.LENGTH_LONG).show();
                }

                //Cursor cursor = db.query(C.TABLE_NAME,null, null, null, null, null, null);
                //Cursor  cursor = db.rawQuery("select * from CARD_TABLE", null);
                //int a = cursor.getCount();
                //String cardName=cursor.getString(cursor.getColumnIndex(C.CARD_NAME));
                //Toast.makeText(MainActivity.this,cardName,Toast.LENGTH_LONG).show();
                db.close();
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
}
