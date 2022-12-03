package com.example.mobilesecurity.terms;

import android.Manifest;
import android.util.Log;

import com.example.mobilesecurity.terms.specific_terms.BatteryTerm;
import com.example.mobilesecurity.terms.specific_terms.ContactTerm;
import com.example.mobilesecurity.terms.specific_terms.FlashTerm;
import com.example.mobilesecurity.terms.specific_terms.LightTerm;
import com.example.mobilesecurity.terms.specific_terms.LocationTerm;
import com.example.mobilesecurity.terms.specific_terms.PhoneTerm;
import com.example.mobilesecurity.terms.specific_terms.ProximityTerm;
import com.example.mobilesecurity.terms.specific_terms.ShakeTerm;
import com.example.mobilesecurity.terms.specific_terms.UserNamePassTerm;
import com.example.mobilesecurity.tools.Permissions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class TermsLoginManager {
    private ArrayList<TermLogin> terms;
    private TermsListenerInput listenerInput = null;
    private TermStatusListener termStatusListener = null;
    private int termIndex = 0;

    public TermsLoginManager() {
    }

    public TermsLoginManager(ArrayList<TermLogin> terms, TermsListenerInput listenerInput) {
        this.terms = terms;
        this.listenerInput = listenerInput;
    }

    public ArrayList<TermLogin> getTerms() {
        return terms;
    }

    public TermsListenerInput getListenerInput() {
        return listenerInput;
    }

    public TermsLoginManager setTermStatusListener(TermStatusListener termStatusListener) {
        this.termStatusListener = termStatusListener;
        return this;
    }

    public int getTermIndex() {
        return termIndex;
    }

    public void index_plus() {
         termIndex++;
    }

    public TermsLoginManager setTermIndex(int termIndex) {
        this.termIndex = termIndex;
        return this;
    }

    public TermsLoginManager setTerms(ArrayList<TermLogin> terms) {
        this.terms = terms;
        return this;
    }

    public TermsLoginManager addTerm(TermLogin term) {
        this.terms.add(term);
        return this;
    }

    public TermsLoginManager setListenerInput(TermsListenerInput listenerInput) {
        this.listenerInput = listenerInput;
        return this;
    }

    public void verifyPermBefore() {
        if (is_finish()){
            termStatusListener.termStatus(new TermStatus(TermStatus.eStatus.FINISHED,""),null);
            return;
        }
        TermLogin term = terms.get(termIndex);
        termStatusListener.current_term( term,termIndex);
        if(term.isNeedPermission() && !Permissions.getPermissions().verifyPermission(term.permmisionName))
            term.requestPermission();
        else{
            TermStatus status = nextTerm();
            termStatusListener.termStatus(status,term);
        }

    }
    public void getValuesAfterPermissionOk(){
        if(terms.get(termIndex) instanceof LocationTerm && !Permissions.getPermissions().verifyPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            Permissions.getPermissions().requestLocationStep_2();
            return;
        }
        terms.get(termIndex).getValuesAfterPermissionOk();
        verifyPermBefore();
    }

    public TermStatus nextTerm() {
        if (is_finish())
            return new TermStatus(TermStatus.eStatus.FINISHED, "");
        TermStatus status = terms.get(termIndex).isTermOK(listenerInput);
        if (status.term_status == TermStatus.eStatus.OK)
            termIndex++;
        return status;

    }

    public boolean is_finish() {
        return termIndex == terms.size();
    }

    public TermStatus VerifyTermsInputOK() {
        StringBuilder ans = new StringBuilder();
        boolean isOK = true;
        for (TermLogin term : terms) {
            TermStatus res = term.isTermInputOK(listenerInput, true);
            if (res.term_status != TermStatus.eStatus.OK) {
                isOK = false;
                ans.append(res.message + "\n");
            }
        }
        if (!isOK)
            return new TermStatus(TermStatus.eStatus.WRONG, ans.toString());
        return new TermStatus(TermStatus.eStatus.OK, "");
    }

    @Override
    public String toString() {
        return "TermsLoginManager{" +
                "terms=" + terms +
                ", listenerInput=" + listenerInput +
                ", termIndex=" + termIndex +
                '}';
    }

    public HashMap<String, String> getKeysForGson() {
        HashMap<String, String> keysGson = new HashMap<>();
        for (TermLogin term : terms) {
            keysGson.put(term.getClass().getSimpleName(), new Gson().toJson(term));
        }
        return keysGson;
    }

    public void fromJson(HashMap<String, String> input) {
        terms = new ArrayList<>();
        input.forEach((k, v) -> {
            if (k.equals(BatteryTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,BatteryTerm.class));
            } else if (k.equals(ContactTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,ContactTerm.class));
            } else if (k.equals(FlashTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,FlashTerm.class));
            } else if (k.equals(LightTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,LightTerm.class));
            } else if (k.equals(LocationTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,LocationTerm.class));
            } else if (k.equals(PhoneTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,PhoneTerm.class));
            } else if (k.equals(ProximityTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,ProximityTerm.class));
            }else if (k.equals(ShakeTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,ShakeTerm.class));
            }else if (k.equals(UserNamePassTerm.class.getSimpleName())) {
                terms.add(new Gson().fromJson(v,UserNamePassTerm.class));
            }

        });
        terms.sort((termLogin, t1) -> termLogin.weight-t1.weight);
        Log.d("mylog",terms.toString());
        termIndex = 0;
    }

}
