package com.example.mobilesecurity.terms;

public class TermStatus {
    public static enum eStatus {
      OK, WRONG, FINISHED
    }
    public eStatus term_status;
    public String message;

    public TermStatus(eStatus term_ok, String message) {
        this.term_status = term_ok;
        this.message = message;
    }

}
