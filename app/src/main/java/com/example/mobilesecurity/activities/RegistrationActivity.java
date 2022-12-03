package com.example.mobilesecurity.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mobilesecurity.R;
import com.example.mobilesecurity.terms.Orderable;
import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.terms.TermsLoginManager;
import com.example.mobilesecurity.terms.specific_terms.BatteryTerm;
import com.example.mobilesecurity.terms.specific_terms.ContactTerm;
import com.example.mobilesecurity.terms.specific_terms.FlashTerm;
import com.example.mobilesecurity.terms.specific_terms.LightTerm;
import com.example.mobilesecurity.terms.specific_terms.LocationTerm;
import com.example.mobilesecurity.terms.specific_terms.PhoneTerm;
import com.example.mobilesecurity.terms.specific_terms.ProximityTerm;
import com.example.mobilesecurity.terms.specific_terms.ShakeTerm;
import com.example.mobilesecurity.terms.specific_terms.UserNamePassTerm;
import com.example.mobilesecurity.tools.MySP;
import com.example.mobilesecurity.tools.Permissions;
import com.example.mobilesecurity.viewObjects.MyAutoComplete;
import com.example.mobilesecurity.viewObjects.MyEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

public class RegistrationActivity extends AppCompatActivity implements TermsListenerInput {
    private HashMap<Chip, ArrayList<View>> chips_map;
    private HashMap<Chip, TermLogin> chips_term_map;
    private ArrayList<CompoundButton> checked_chips_lst = new ArrayList<>();
    private MaterialButton profile_BTN_create;
    private ArrayList<String> batteryRange = getBatteryRange();
    private ArrayList<Drawable> orderDrawable;
    private MyEditText userName;
    private MyEditText password;
    private MaterialTextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        MySP.initHelper(this);
        Permissions.initHelper(this);
        findViews();
        setActions();
        setTitle("Create Account");
    }


    private void findViews() {
        userName = new MyEditText(this, findViewById(R.id.profile_TIL_username), findViewById(R.id.profile_TIETL_username));
        password = new MyEditText(this, findViewById(R.id.profile_TIL_password), findViewById(R.id.profile_TIETL_password));

        chips_map = new HashMap<>();
        chips_map.put(findViewById(R.id.chip_contact_name), new ArrayList<>(Arrays.asList(
                new MyEditText(this, findViewById(R.id.profile_TIL_contactname), findViewById(R.id.profile_TIETL_contactname))
        )));
        chips_map.put(findViewById(R.id.chip_call), new ArrayList<>(Arrays.asList(
                new MyEditText(this, findViewById(R.id.profile_TIL_called), findViewById(R.id.profile_TIETL_call))
        )));

        chips_map.put(findViewById(R.id.chip_battery_term), new ArrayList<>(Arrays.asList(
                new MyAutoComplete(this, findViewById(R.id.profile_TIL_batteryFrom), findViewById(R.id.profile_ACTV_presentFrom), new ArrayAdapter(this, R.layout.list_item, batteryRange)),
                new MyAutoComplete(this, findViewById(R.id.profile_TIL_batteryUntil), findViewById(R.id.profile_ACTV_presentUntil), new ArrayAdapter(this, R.layout.list_item, batteryRange))
        )));
        chips_map.put(findViewById(R.id.chip_location_term), new ArrayList<>(Arrays.asList(
                new MyEditText(this, findViewById(R.id.profile_TIL_addressCity), findViewById(R.id.profile_TIETL_addressCity)),
                new MyEditText(this, findViewById(R.id.profile_TIL_addressStreet), findViewById(R.id.profile_TIETL_addressStreet)),
                new MyEditText(this, findViewById(R.id.profile_TIL_addressNum), findViewById(R.id.profile_TIETL_addressNum))
        )));
        chips_map.put(findViewById(R.id.chip_shake), new ArrayList<>());
        chips_map.put(findViewById(R.id.chip_flash), new ArrayList<>());
        chips_map.put(findViewById(R.id.chip_proximity), new ArrayList<>());
        chips_map.put(findViewById(R.id.chip_light), new ArrayList<>());

        profile_BTN_create = findViewById(R.id.profile_BTN_create);

        orderDrawable = new ArrayList<>(Arrays.asList(
                getResources().getDrawable(R.drawable.ic_number_one, this.getTheme()),
                getResources().getDrawable(R.drawable.ic_number_2, this.getTheme()),
                getResources().getDrawable(R.drawable.ic_number_3, this.getTheme()),
                getResources().getDrawable(R.drawable.ic_number_four, this.getTheme())
        ));
        chips_term_map = new HashMap<>();
        chips_term_map.put(findViewById(R.id.chip_contact_name), new ContactTerm());
        chips_term_map.put(findViewById(R.id.chip_call), new PhoneTerm());
        chips_term_map.put(findViewById(R.id.chip_battery_term), new BatteryTerm());
        chips_term_map.put(findViewById(R.id.chip_location_term), new LocationTerm());
        chips_term_map.put(findViewById(R.id.chip_shake), new ShakeTerm());
        chips_term_map.put(findViewById(R.id.chip_flash), new FlashTerm());
        chips_term_map.put(findViewById(R.id.chip_proximity), new ProximityTerm());
        chips_term_map.put(findViewById(R.id.chip_light), new LightTerm());
        errors = findViewById(R.id.errors);
    }


    private void setActions() {
        chips_map.forEach((k, v) -> k.setOnCheckedChangeListener(onCheckedChangeListener));
        profile_BTN_create.setOnClickListener(view -> finish_reg());
    }


    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b == true) {
                chips_map.get(compoundButton).forEach(view -> view.setVisibility(View.VISIBLE));
                checked_chips_lst.add(compoundButton);
                if (chips_map.get(compoundButton).isEmpty())
                    updateOrderChips();

            } else {
                chips_map.get(compoundButton).forEach(view -> view.setVisibility(View.GONE));
                checked_chips_lst.remove(compoundButton);
                if (chips_map.get(compoundButton).isEmpty())
                    updateOrderChips();
            }
        }
    };

    private void updateOrderChips() {
        int drawableCounter = 0;
        for (int i = 0; i < checked_chips_lst.size(); i++)
            if (chips_map.get(checked_chips_lst.get(i)).isEmpty())
                ((Chip) checked_chips_lst.get(i)).setCheckedIcon(orderDrawable.get(drawableCounter++));
    }

    public ArrayList<String> getBatteryRange() {
        ArrayList<String> str_range = new ArrayList<>();
        for (int i = 1; i <= 100; i++)
            str_range.add("" + i);
        return str_range;
    }

    private void finish_reg() {
        Stack<Integer> stack=new Stack<>();
        stack.add(1003);
        stack.add(1002);
        stack.add(1001);
        stack.add(1000);
        errors.setText("");
        ArrayList<TermLogin> checkedTerms = new ArrayList<>();
        checkedTerms.add(new UserNamePassTerm());
        checked_chips_lst.forEach(new Consumer<CompoundButton>() {
            @Override
            public void accept(CompoundButton chip) {
                checkedTerms.add(chips_term_map.get(chip));
                if (chips_term_map.get(chip) instanceof Orderable) {
                    chips_term_map.get(chip).setWeight(stack.pop());
                }
            }
        });

        TermsLoginManager termsLoginManager = new TermsLoginManager(checkedTerms, this);
        TermStatus termsStatus = termsLoginManager.VerifyTermsInputOK();
        if (termsStatus.term_status == TermStatus.eStatus.WRONG) {
            errors.setText(termsStatus.message);
            return;
        }
        termsLoginManager.setListenerInput(null);
        HashMap<String, String> keysForJson = termsLoginManager.getKeysForGson();
        MySP.get_my_SP().putMap(checkedTerms.get(0).toString(), keysForJson);
        String json = new Gson().toJson(keysForJson);
        go_next(LoginActivity.class,json);
    }

    @Override
    public String getUserName() {
        return userName.toString();
    }

    @Override
    public String getPassword() {
        return password.toString();
    }

    @Override
    public String getContact() {
        return chips_map.get(findViewById(R.id.chip_contact_name)).get(0).toString();
    }

    @Override
    public String getPhone() {
        return chips_map.get(findViewById(R.id.chip_call)).get(0).toString();
    }

    @Override
    public String[] getLocation() {
        String[] strLocation = new String[3];
        strLocation[0] = chips_map.get(findViewById(R.id.chip_location_term)).get(0).toString();
        strLocation[1] = chips_map.get(findViewById(R.id.chip_location_term)).get(1).toString();
        strLocation[2] = chips_map.get(findViewById(R.id.chip_location_term)).get(2).toString();
        return strLocation;
    }

    @Override
    public int getBatteryFrom() {
        int from = -1;
        try {
            from = Integer.parseInt(chips_map.get(findViewById(R.id.chip_battery_term)).get(0).toString());
        } catch (Exception exception) {
        }
        return from;
    }

    @Override
    public int getBatteryUntil() {
        int until = -1;
        try {
            until = Integer.parseInt(chips_map.get(findViewById(R.id.chip_battery_term)).get(1).toString());
        } catch (Exception exception) {
        }
        return until;
    }

    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity,String json) {
        Intent intent = new Intent(this, nextActivity);
        Bundle bundle = new Bundle();
        bundle.putString("JSON", json);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}