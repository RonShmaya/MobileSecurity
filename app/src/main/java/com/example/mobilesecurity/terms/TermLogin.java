package com.example.mobilesecurity.terms;

public abstract class TermLogin {
    public String permmisionName = "";
    public int weight;

    public TermLogin() {
    }

    protected abstract TermStatus isTermOK(TermsListenerInput listenerInput);// TODO: 19/11/2022 change interface
    protected abstract TermStatus isTermInputOK(TermsListenerInput listenerInput,boolean getFromListener);
    public abstract void getValuesAfterPermissionOk();
    public abstract boolean isNeedPermission();
    public abstract void requestPermission();

    public String getPermmisionName() {
        return permmisionName;
    }

    public TermLogin setPermmisionName(String permmisionName) {
        this.permmisionName = permmisionName;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public TermLogin setWeight(int weight) {
        this.weight = weight;
        return this;
    }
}
