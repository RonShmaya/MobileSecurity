package com.example.mobilesecurity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mobilesecurity.R;
import com.example.mobilesecurity.terms.Orderable;
import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermStatusListener;
import com.example.mobilesecurity.terms.TermsLoginManager;
import com.example.mobilesecurity.terms.specific_terms.UserNamePassTerm;
import com.example.mobilesecurity.tools.MySP;
import com.example.mobilesecurity.tools.Permissions;
import com.example.mobilesecurity.viewObjects.MyEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements TermStatusListener {
    private MaterialButton login_BTN_create;
    private MyEditText userName;
    private MyEditText password;
    private ArrayList<MaterialButton> term_view;
    private ArrayList<ExtendedFloatingActionButton> yes_lst;
    private ArrayList<ExtendedFloatingActionButton> no_lst;
    private TermsLoginManager termsLoginManager = new TermsLoginManager().setTermStatusListener(this);
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MySP.initHelper(this);
        Permissions.initHelper(this);
        Permissions.getPermissions().setTermManager(termsLoginManager);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            json = "";
        else
            json = bundle.getString("JSON", "");
        findViews();
        init_actions();
        setTitle("Login");
    }

    private void init_actions() {

        login_BTN_create.setOnClickListener(view -> {
            hide_chips();
            HashMap<String, String> input;
            if (!json.isEmpty())
                input = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
                }.getType());
            else
                input = MySP.get_my_SP().getMap(
                        new UserNamePassTerm().setUsername(userName.toString()).setPassword(password.toString()).toString(),
                        new TypeToken<HashMap<String, String>>() {
                        });

            termsLoginManager.fromJson(input);
            if (termsLoginManager.is_finish()) {
                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                return;
            }
            termsLoginManager.nextTerm();
            VerifyTerms();
        });


    }

    private void hide_chips() {
        for (int i = 0; i < termsLoginManager.getTermIndex(); i++) {
            term_view.get(i).setVisibility(View.GONE);
            term_view.get(i).setChecked(false);
        }
        yes_lst.forEach(v -> v.setVisibility(View.GONE));
        no_lst.forEach(v -> v.setVisibility(View.GONE));

    }

    private void VerifyTerms() {
        termsLoginManager.verifyPermBefore();
    }

    private void findViews() {
        login_BTN_create = findViewById(R.id.login_BTN_create);
        userName = new MyEditText(this, findViewById(R.id.login_TIL_username), findViewById(R.id.login_TIETL_username));
        password = new MyEditText(this, findViewById(R.id.login_TIL_password), findViewById(R.id.login_TIETL_password));

        term_view = new ArrayList<>(Arrays.asList(
                findViewById(R.id.chip_term_1),
                findViewById(R.id.chip_term_2),
                findViewById(R.id.chip_term_3),
                findViewById(R.id.chip_term_4),
                findViewById(R.id.chip_term_5),
                findViewById(R.id.chip_term_6),
                findViewById(R.id.chip_term_7),
                findViewById(R.id.chip_term_8),
                findViewById(R.id.chip_term_9)
        ));

        yes_lst = new ArrayList<>(Arrays.asList(
                findViewById(R.id.chip_term_1_yes),
                findViewById(R.id.chip_term_2_yes),
                findViewById(R.id.chip_term_3_yes),
                findViewById(R.id.chip_term_4_yes),
                findViewById(R.id.chip_term_5_yes),
                findViewById(R.id.chip_term_6_yes),
                findViewById(R.id.chip_term_7_yes),
                findViewById(R.id.chip_term_8_yes),
                findViewById(R.id.chip_term_9_yes)
        ));
        no_lst = new ArrayList<>(Arrays.asList(
                findViewById(R.id.chip_term_1_no),
                findViewById(R.id.chip_term_2_no),
                findViewById(R.id.chip_term_3_no),
                findViewById(R.id.chip_term_4_no),
                findViewById(R.id.chip_term_5_no),
                findViewById(R.id.chip_term_6_no),
                findViewById(R.id.chip_term_7_no),
                findViewById(R.id.chip_term_8_no),
                findViewById(R.id.chip_term_9_no)
        ));

    }

    @Override
    public void termStatus(TermStatus status, TermLogin termLogin) {
        if (status.term_status == TermStatus.eStatus.FINISHED) {
            // TODO: 21/11/2022 finish
            //Toast.makeText(LoginActivity.this, "FINISH", Toast.LENGTH_LONG).show();
            go_next(FinishActivity.class);
        } else if (status.term_status == TermStatus.eStatus.OK) {
            Log.d("mylog", " index " + (termsLoginManager.getTermIndex() - 2) + " " + termLogin.toString());
            term_view.get(termsLoginManager.getTermIndex() - 2).setText(termLogin.toString());
            yes_lst.get(termsLoginManager.getTermIndex() - 2).setVisibility(View.VISIBLE);
            no_lst.get(termsLoginManager.getTermIndex() - 2).setVisibility(View.GONE);
            termsLoginManager.verifyPermBefore();

        } else if (status.term_status == TermStatus.eStatus.WRONG) {
            no_lst.get(termsLoginManager.getTermIndex() - 1).setVisibility(View.VISIBLE);
            yes_lst.get(termsLoginManager.getTermIndex() - 1).setVisibility(View.GONE);
        }
    }

    @Override
    public void current_term(TermLogin termLogin, int index) {
        term_view.get(index - 1).setVisibility(View.VISIBLE);
        if (termLogin instanceof Orderable)
            term_view.get(index - 1).setText("Sensor Order Term");
        else
            term_view.get(index - 1).setText(termLogin.toString());
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity ) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}