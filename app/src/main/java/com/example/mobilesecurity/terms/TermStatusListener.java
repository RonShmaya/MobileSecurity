package com.example.mobilesecurity.terms;

public interface TermStatusListener {
    void termStatus(TermStatus status,TermLogin termLogin);
    void current_term(TermLogin termLogin,int index);
}
