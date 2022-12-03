package com.example.mobilesecurity.terms.specific_terms;

import android.Manifest;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.Permissions;

import java.util.Date;

public class PhoneTerm extends TermLogin {
    private String phone;

    public PhoneTerm() {
        super();
        permmisionName = Manifest.permission.READ_CALL_LOG;
        weight = 2;
    }

    public String getPhone() {
        return phone;
    }

    public PhoneTerm setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        AppCompatActivity appCompatActivity = Permissions.getPermissions().getAppCompatActivity();
        boolean is_found = false;
        Date currentTime = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Log.d("mylog", "phoneeeeee: " + phone + "-" + year + "-" + month + "-" + day);
        Cursor phones = appCompatActivity.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = phones.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
        int date = phones.getColumnIndexOrThrow(CallLog.Calls.DATE);

        while (phones.moveToNext()) {

            String phNumber = phones.getString(number);
            phNumber.replace("-", "");
            String callDate = phones.getString(date);
            Log.d("mylog", "phon00000000000000000: " + callDate + "-" + callDate);

            if (phNumber.equals(phone)) {
                Date callDayTime = new Date(Long.valueOf(callDate));

                Calendar cal_2 = Calendar.getInstance();
                cal_2.setTime(callDayTime);
                int year_2 = cal_2.get(Calendar.YEAR);
                int month_2 = cal_2.get(Calendar.MONTH);
                int day_2 = cal_2.get(Calendar.DAY_OF_MONTH);
                Log.d("mylog", phNumber + "-" + year_2 + "-" + month_2 + "-" + day_2);

                if (year_2 == year && month_2 == month && day_2 == day) {
                    phones.close();
                    return new TermStatus(TermStatus.eStatus.OK, "");
                }
            }
            //phones.moveToPrevious();


        }
        phones.close();

        return new TermStatus(TermStatus.eStatus.WRONG, "");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput, boolean getFromListener) {
        if (getFromListener)
            phone = listenerInput.getPhone();
        if (phone == null || phone.isEmpty())
            return new TermStatus(TermStatus.eStatus.WRONG, "Phone number cannot be empty");
        return new TermStatus(TermStatus.eStatus.OK, "");
    }

    @Override
    public void getValuesAfterPermissionOk() {

    }

    @Override
    public boolean isNeedPermission() {
        return true;
    }

    @Override
    public void requestPermission() {
        Permissions.getPermissions().requestCall();
    }

    @Override
    public String toString() {
        return "Phone Call Term";
    }

}
