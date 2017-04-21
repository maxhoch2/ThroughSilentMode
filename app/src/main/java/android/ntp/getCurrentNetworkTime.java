package android.ntp;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import de.createplus.throughsilentmode.MainActivity;

public class getCurrentNetworkTime extends AsyncTask<String, Void, Date> {

    private Exception exception;

    @Override
    protected Date doInBackground(String... urls) {
        Date current = null;
        SntpClient client = new SntpClient();
        if (client.requestTime("0.de.pool.ntp.org",2000)) {
            long now = client.getNtpTime() + (SystemClock.elapsedRealtime() -
                    client.getNtpTimeReference())+client.getRoundTripTime();
            current = new Date(now);
            Log.e("NTP tag", current.toString());
            Calendar resultcal = Calendar.getInstance();
            resultcal.setTime(current);

            Calendar currentcal = Calendar.getInstance();
            Log.e("Time","Offset:"+(client.offset -client.getRoundTripTime()) + (SystemClock.elapsedRealtime() -
                    client.getNtpTimeReference()));
            MainActivity.offset = (client.offset -client.getRoundTripTime()) + (SystemClock.elapsedRealtime() -
                    client.getNtpTimeReference());
        }
        return current;
    }
    @Override
    protected void onPostExecute(Date result) {


    }
}