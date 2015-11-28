package qrtech.qrtechpay;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
    //boolean isZxingInstalled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //textView = (TextView)findViewById(R.id.textView);
        scanButton = (Button)findViewById(R.id.scan);

        context = this;
        sharedPref = context.getSharedPreferences("STORE_DATA", Context.MODE_PRIVATE);

        //SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);



        TextView tvStatus=(TextView)findViewById(R.id.textView);
        tvStatus.setText(sharedPref.getString("result","No Data"));


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                //intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
                startActivityForResult(intent, 0);
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

/*    @Override
    protected void onResume() {
        TextView tvStatus=(TextView)findViewById(R.id.textView);
        tvStatus.setText(result);
        super.onResume();
    }*/

/*    @Override
    protected void onRestart() {
        TextView tvStatus=(TextView)findViewById(R.id.textView);
        tvStatus.setText(result);
        super.onRestart();
    }*/

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
