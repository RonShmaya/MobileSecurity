package com.example.mobilesecurity.terms.specific_terms;

import android.Manifest;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobilesecurity.terms.Orderable;
import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermStatusListener;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.Permissions;

public class FlashTerm extends TermLogin implements Orderable {
    private boolean try_again = true;
    public FlashTerm() {
        super();
        permmisionName= Manifest.permission.CAMERA;
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        CameraManager cm = Permissions.getPermissions().getAppCompatActivity().getSystemService(CameraManager.class);
        CameraManager.TorchCallback torchCallback=new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeUnavailable(@NonNull String cameraId) {
                super.onTorchModeUnavailable(cameraId);
            }

            @Override
            public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                super.onTorchModeChanged(cameraId, enabled);
                if(Permissions.getPermissions().getAppCompatActivity() instanceof TermStatusListener && try_again){
                    Log.d("mylog", "in flash - "+enabled);
                    if(enabled){
                        try_again=false;
                        Permissions.getPermissions().getTermManage().index_plus();
                        ((TermStatusListener)Permissions.getPermissions().getAppCompatActivity()).termStatus(new TermStatus(TermStatus.eStatus.OK,""),FlashTerm.this);
                    }else{
                        ((TermStatusListener)Permissions.getPermissions().getAppCompatActivity()).termStatus(new TermStatus(TermStatus.eStatus.WRONG,""),FlashTerm.this);
                    }

                }

            }
        };
        cm.registerTorchCallback(torchCallback, null);
        return new TermStatus(TermStatus.eStatus.WRONG,"");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput, boolean getFromListener) {
        return new TermStatus(TermStatus.eStatus.OK,"");
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
        Permissions.getPermissions().requestCAMERA();
    }
    @Override
    public String toString() {
        return "Flash Term";
    }
}
