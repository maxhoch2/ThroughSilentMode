package de.createplus.throughsilentmode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;
import android.ntp.getCurrentNetworkTime;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static Calendar sync;
    public static long offset = -1;
    private final int REQUEST_PERMISSION_WRITE_SETTINGS=777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loaded = false;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IntentFilter statusIntentFilterPlanData = new IntentFilter(
                playsound.Constants.BROADCAST_ACTION);


        new getCurrentNetworkTime().execute();
        while (offset == -1){

        }

        int h = 22;
        int m = 40;



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.MONTH,3);//because is started from 0
        calendar.set(Calendar.YEAR,2017);
        calendar.set(Calendar.DAY_OF_MONTH,18);
        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        sync = Calendar.getInstance();
        sync.setTimeInMillis(0);
        sync.set(Calendar.MONTH,3);//because is started from 0
        sync.set(Calendar.YEAR,2017);
        sync.set(Calendar.DAY_OF_MONTH,18);
        sync.set(Calendar.HOUR_OF_DAY,h);
        sync.set(Calendar.MINUTE,m+1);
        sync.set(Calendar.SECOND,0);
        sync.set(Calendar.MILLISECOND,0);

        sync.setTimeInMillis((sync.getTimeInMillis()-offset));


        Log.e("Time",calendar.getTime().toString());
        Intent intent = new Intent(this, playsound.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - offset, pendingIntent);






        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showExplanation("","");
                //playSound(getApplicationContext());
                //Intent mServiceIntent = new Intent(MainActivity.this, playsound.class);
                //MainActivity.this.startService(mServiceIntent);
            }
        });
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



    private void showExplanation(String title,
                                 String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openAndroidPermissionsMenu();
                    }
                });
        builder.create().show();
    }


    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d("HI", "Can Write Settings: " + retVal);
            if(retVal){
                Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Write not allowed :-(", Toast.LENGTH_LONG).show();
                showExplanation("TEXT","MEHE");

            }
        }
        return retVal;
    }
    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.getPackageName()));
        startActivity(intent);
    }

}
