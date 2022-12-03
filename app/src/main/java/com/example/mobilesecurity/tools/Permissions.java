package com.example.mobilesecurity.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermsLoginManager;

public class Permissions {
    private static Permissions _instance = null;
    private AppCompatActivity appCompatActivity;
    private final String CONTACT_PERMISSION = Manifest.permission.READ_CONTACTS;
    private final String BATTERY_PERMISSION = Manifest.permission.BATTERY_STATS;
    private final String LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String LOCATION_PERMISSION_STEP_2 = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String CALL_PERMISSION = Manifest.permission.READ_CALL_LOG;
    private final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private String current_ask = "";
    private TermsLoginManager termsLoginManager;

    public TermsLoginManager getTermManage() {
        return termsLoginManager;
    }

    public Permissions setTermManager(TermsLoginManager termsLoginManager) {
        this.termsLoginManager = termsLoginManager;
        return this;
    }

    private Permissions(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        requestPermissionLauncher = this.appCompatActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), permissionCallBack);
        manuallyPermissionResultLauncher = this.appCompatActivity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        termsLoginManager.getValuesAfterPermissionOk();
                    }
                });
    }
    public static Permissions getPermissions() {
        return _instance;
    }
    public static void initHelper(AppCompatActivity appCompatActivity) {
        if (_instance == null) {
            _instance = new Permissions(appCompatActivity);
        }
        else{
            _instance.appCompatActivity=appCompatActivity;
        }
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    ActivityResultCallback<Boolean> permissionCallBack = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if (!isGranted) {
                requestPermissionWithRationaleCheck();
            }else{
                termsLoginManager.getValuesAfterPermissionOk();
            }
        }
    };
    ActivityResultLauncher<String> requestPermissionLauncher;

    public void requestLocation() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        current_ask = LOCATION_PERMISSION;
    }
    public void requestLocationStep_2() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        current_ask = LOCATION_PERMISSION;
    }
    public void requestBattery() {
        requestPermissionLauncher.launch(Manifest.permission.BATTERY_STATS);
        current_ask = LOCATION_PERMISSION_STEP_2;

    }
    public void requestContacts() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
         current_ask = CONTACT_PERMISSION;
    }
    public void requestCall() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
         current_ask = CALL_PERMISSION;

    }
    public void requestCAMERA() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
         current_ask = CAMERA_PERMISSION;

    }
    public boolean verifyPermission(String permission){

        return permission.isEmpty() ? true : ContextCompat.checkSelfPermission(appCompatActivity, permission) == PackageManager.PERMISSION_GRANTED ;
        //Manifest.permission.READ_CONTACTS
    }
    private void requestPermissionWithRationaleCheck() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity, current_ask)) {

            String message = "it's necessary to give the permissions";
            AlertDialog alertDialog =
                    new AlertDialog.Builder(appCompatActivity)
                            .setMessage(message)
                            .setPositiveButton(appCompatActivity.getString(android.R.string.ok),
                                    (dialog, which) -> {
                                        if(current_ask.equals(BATTERY_PERMISSION))
                                            requestBattery();
                                        else if(current_ask.equals(LOCATION_PERMISSION))
                                            requestLocation();
                                        else if(current_ask.equals(CALL_PERMISSION))
                                            requestCall();
                                        else if(current_ask.equals(CONTACT_PERMISSION))
                                            requestContacts();
                                        dialog.cancel();
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // disabled functions due to denied permissions
                                }
                            })
                            .show();
            alertDialog.setCanceledOnTouchOutside(true);
        } else {
            openPermissionSettingDialog();
        }
    }

    private void openPermissionSettingDialog() {
        String message = "Setting screen if user have permanently disable the permission by clicking Don't ask again checkbox.";
        AlertDialog alertDialog =
                new AlertDialog.Builder(appCompatActivity)
                        .setMessage(message)
                        .setPositiveButton(appCompatActivity.getString(android.R.string.ok),
                                (dialog, which) -> {
                                    openSettingsManually ();
                                    dialog.cancel();
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void openSettingsManually() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", appCompatActivity.getPackageName(), null);
        intent.setData(uri);
        manuallyPermissionResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> manuallyPermissionResultLauncher;
}
