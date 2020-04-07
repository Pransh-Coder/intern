package com.example.intern.payment;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class PaymentViewModel extends ViewModel {
    //Needed to create customer in RazorPay, initialize from the
    private String userName;
    private String userPhone;
    private String userEmail;
    private JSONObject basePayload = new JSONObject();

    void makeBasePayload(String amount) {
        //TODO: load payload data from userdataviewmodel
        try {
            basePayload.put("amount", amount);
            basePayload.put("currency", "INR");
            basePayload.put("contact", "9958223456");
            basePayload.put("email", "debug@gmail.com");
        } catch (Exception ignored) {
        }
    }

    JSONObject getPayload() {
        return this.basePayload;
    }

    void makeCardPaymentPayload(String name, String cardNumber, String expiryMonth, String expiryYear, String cvv) {
        try {
            basePayload.put("method", "card");
            basePayload.put("card[name]", name);
            basePayload.put("card[number]", cardNumber);
            basePayload.put("card[expiry_month]", expiryMonth);
            basePayload.put("card[expiry_year]", expiryYear);
            basePayload.put("card[cvv]", cvv);
        } catch (Exception ignored) {
        }
    }

    JSONObject getNetBankingPayload(String bankId) {
        try {
            basePayload.put("method", "netbanking");
            basePayload.put("bank", bankId);
        } catch (Exception ignored) {
        } finally {
            return basePayload;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
