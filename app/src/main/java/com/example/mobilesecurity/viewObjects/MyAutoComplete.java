package com.example.mobilesecurity.viewObjects;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MyAutoComplete extends View {
    private TextInputLayout my_TIL;
    private AutoCompleteTextView my_ACTV;

    public MyAutoComplete(Context context, TextInputLayout my_TIL, AutoCompleteTextView my_ACTV, ArrayAdapter arrayAdapter) {
        super(context);
        this.my_TIL = my_TIL;
        this.my_ACTV = my_ACTV;
        my_ACTV.setAdapter(arrayAdapter);
    }

    public TextInputLayout getMy_TIL() {
        return my_TIL;
    }

    public MyAutoComplete setMy_TIL(TextInputLayout my_TIL) {
        this.my_TIL = my_TIL;
        return this;
    }

    public AutoCompleteTextView getmy_ACTV() {
        return my_ACTV;
    }

    public MyAutoComplete setmy_ACTV(AutoCompleteTextView my_ACTV) {
        this.my_ACTV = my_ACTV;
        return this;
    }

    @Override
    public String toString() {
        return this.my_ACTV.getText().toString();
    }

    @Override
    public void setVisibility(int visibility) {
        this.my_TIL.setVisibility(visibility);
    }
}
