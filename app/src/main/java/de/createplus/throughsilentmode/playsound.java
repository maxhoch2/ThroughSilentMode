package de.createplus.throughsilentmode;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;


public class playsound extends IntentService {
    public final class Constants {
        // Defines a custom Intent action
        public static final String BROADCAST_ACTION =
                "de.createplus.vertretungsplan.throughsilentmode.playsound.BROADCAST";
        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA_STATUS =
                "de.createplus.vertretungsplan.throughsilentmode.playsound.STATUS";
    }


    public playsound() {
        super("playsound");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Sound","Waiting");
        while(Calendar.getInstance().getTimeInMillis() < MainActivity.sync.getTimeInMillis()){}
        Log.e("Sound","Playing");
        playSound(this);

    }


    public void playSound(Context con) {

        Uri path = Uri.parse("android.resource://de.createplus.throughsilentmode/" + R.raw.sound);
        RingtoneManager.setActualDefaultRingtoneUri(
                con, RingtoneManager.TYPE_RINGTONE,
                path);
        Ringtone ringtone = RingtoneManager.getRingtone(con, path);


        ringtone.setStreamType(AudioManager.STREAM_ALARM);

        AudioManager audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if (volume == 0) {
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        if (ringtone != null) {
            ringtone.play();
        }

    }
}
