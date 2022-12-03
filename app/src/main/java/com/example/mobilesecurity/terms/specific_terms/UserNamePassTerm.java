package com.example.mobilesecurity.terms.specific_terms;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;

public class UserNamePassTerm extends TermLogin {
    private String username;
    private String password;
    public UserNamePassTerm() {
        super();
        weight=0;
    }

    public String getUsername() {
        return username;
    }

    public UserNamePassTerm setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserNamePassTerm setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "UserNamePassTerm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        return new TermStatus(TermStatus.eStatus.OK,"");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput,boolean getFromListener) {
        if(getFromListener){
            username=listenerInput.getUserName();
            password=listenerInput.getPassword();
        }
        if(username == null || username.isEmpty() || password == null || password.isEmpty())
            return new TermStatus(TermStatus.eStatus.WRONG,"Username & Password cannot be empty");
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

    }
}
