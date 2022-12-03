package com.example.mobilesecurity.terms.specific_terms;

import static android.content.Context.BATTERY_SERVICE;

import android.Manifest;
import android.os.BatteryManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.Permissions;

public class BatteryTerm extends TermLogin {
    private int from;
    private int until;
    private final int MIN = 1;
    private final int MAX = 100;

    public BatteryTerm() {
        super();
        permmisionName= Manifest.permission.BATTERY_STATS;
        weight=3;
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        AppCompatActivity appCompatActivity = Permissions.getPermissions().getAppCompatActivity();
        BatteryManager bm = (BatteryManager) appCompatActivity.getSystemService(BATTERY_SERVICE);
        int current = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        if(from<=current && current<= until)
            return new TermStatus(TermStatus.eStatus.OK,"");
        return new TermStatus(TermStatus.eStatus.WRONG,"");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput,boolean getFromListener) {
        if(getFromListener){
            from = listenerInput.getBatteryFrom();
            until = listenerInput.getBatteryUntil();

        }
        if (from > until || from < MIN || until > MAX)
            return new TermStatus(TermStatus.eStatus.WRONG,"battery input need to be between "+MIN+" to "+MAX);
        return new TermStatus(TermStatus.eStatus.OK,"");
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
        Permissions.getPermissions().requestBattery();
    }

    public int getFrom() {
        return from;
    }

    public BatteryTerm setFrom(int from) {
        this.from = from;
        return this;
    }

    public int getUntil() {
        return until;
    }

    public BatteryTerm setUntil(int until) {
        this.until = until;
        return this;
    }

    @Override
    public String toString() {
        return "Battery Term";
    }
}
