package com.example.mobilesecurity.terms.specific_terms;

import android.Manifest;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.Permissions;

public class ContactTerm extends TermLogin {
    private String contact;
    public ContactTerm() {
        super();
        permmisionName= Manifest.permission.READ_CONTACTS;
        weight=1;
    }


    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        AppCompatActivity appCompatActivity =Permissions.getPermissions().getAppCompatActivity();
        boolean is_found = false;
        Cursor phones = appCompatActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if(name.equals(contact)) {
                phones.close();
                return new TermStatus(TermStatus.eStatus.OK, "");
            }
        }
        phones.close();
        return new TermStatus(TermStatus.eStatus.WRONG, "");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput,boolean getFromListener) {
        if(getFromListener)
            contact = listenerInput.getContact();
        if(contact == null || contact.isEmpty())
            return new TermStatus(TermStatus.eStatus.WRONG,"Contact name cannot be empty");
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
        Permissions.getPermissions().requestContacts();
    }
    @Override
    public String toString() {
        return "Contact Term";
    }
}
