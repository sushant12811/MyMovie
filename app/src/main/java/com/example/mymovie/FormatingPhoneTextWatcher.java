package com.example.mymovie;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FormatingPhoneTextWatcher implements TextWatcher {

    private boolean isFormatting;
    boolean deletingHyphen;
    int hyphenStart;
    EditText editText;

    public FormatingPhoneTextWatcher(EditText editText) {
        this.editText = editText;
        this.isFormatting = false;
        this.deletingHyphen = false;
        this.hyphenStart = 0;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (isFormatting) return;

        // Check if deleting hyphen
        if (count > 0 && after == 0) {
            deletingHyphen = s.charAt(start) == '-';
            hyphenStart = start;
        } else {
            deletingHyphen = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFormatting) return;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isFormatting) return;

        isFormatting = true;

        String phoneNumber = s.toString().replaceAll("\\D", "");
        String formattedNumber = formatPhoneNumber(phoneNumber);

        editText.setText(formattedNumber);
        editText.setSelection(formattedNumber.length());

        isFormatting = false;
    }

    private String formatPhoneNumber(String phoneNumber) {
        int length = phoneNumber.length();

        if (length <= 3) {
            return phoneNumber;
        } else if (length <= 6) {
            return String.format("(%s) %s", phoneNumber.substring(0, 3), phoneNumber.substring(3));
        } else {
            return String.format("(%s) %s-%s", phoneNumber.substring(0, 3), phoneNumber.substring(3, 6), phoneNumber.substring(6));
        }
    }
}
