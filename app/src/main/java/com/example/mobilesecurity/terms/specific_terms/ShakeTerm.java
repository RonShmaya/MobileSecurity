package com.example.mobilesecurity.terms.specific_terms;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.mobilesecurity.terms.Orderable;
import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermStatusListener;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.Permissions;

public class ShakeTerm extends TermLogin implements Orderable {
    private boolean try_again = true;
    public ShakeTerm() {
        super();
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        SensorManager sensorManager = (SensorManager) Permissions.getPermissions().getAppCompatActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.d("mylog", "in shakekeeeeee");

                shake_event_handler(sensorEvent.values,this,sensorManager);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

        };
        sensorManager.registerListener(sensorEventListener, sensorShake, SensorManager.SENSOR_DELAY_GAME);
        return new TermStatus(TermStatus.eStatus.WRONG,"");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput, boolean getFromListener) {
        return new TermStatus(TermStatus.eStatus.OK, "");
    }

    @Override
    public void getValuesAfterPermissionOk() {

    }

    @Override
    public boolean isNeedPermission() {
        return false;
    }

    @Override
    public void requestPermission() {

    }

    @Override
    public String toString() {
        return "Shake Term";
    }

    private void shake_event_handler(float[] values, SensorEventListener sensorEventListener, SensorManager sensorManager) {
        if(!try_again)
            return;

        float x = values[0];
        float y = values[1];
        float z = values[2];
        float value =(float) Math.sqrt((double) (x * x + y * y + z * z));
        Log.d("mylog", "in sha2222222222222");
        if (value > 10) {
            sensorManager.unregisterListener(sensorEventListener);
            try_again=false;
            Permissions.getPermissions().getTermManage().index_plus();
            ((TermStatusListener)Permissions.getPermissions().getAppCompatActivity()).termStatus(new TermStatus(TermStatus.eStatus.OK,""),this);
        }else{
            ((TermStatusListener)Permissions.getPermissions().getAppCompatActivity()).termStatus(new TermStatus(TermStatus.eStatus.WRONG,""),this);
        }
    }
}


