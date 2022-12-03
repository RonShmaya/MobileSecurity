package com.example.mobilesecurity.viewObjects;

import android.content.Context;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyEditText extends View {
    private TextInputLayout my_TIL;
    private TextInputEditText my_TIETL;

    public MyEditText(Context context,TextInputLayout my_TIL,TextInputEditText my_TIETL) {
        super(context);
        this.my_TIL = my_TIL;
        this.my_TIETL = my_TIETL;
    }

    public TextInputLayout getMy_TIL() {
        return my_TIL;
    }

    public MyEditText setMy_TIL(TextInputLayout my_TIL) {
        this.my_TIL = my_TIL;
        return this;
    }

    public TextInputEditText getMy_TIETL() {
        return my_TIETL;
    }

    public MyEditText setMy_TIETL(TextInputEditText my_TIETL) {
        this.my_TIETL = my_TIETL;
        return this;
    }

    @Override
    public String toString() {
        return this.my_TIETL.getText().toString();
    }

    @Override
    public void setVisibility(int visibility) {
        this.my_TIL.setVisibility(visibility);
    }
}
