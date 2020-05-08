package com.example.intern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityScreen12Binding;
import com.example.intern.mainapp.MainApp;

public class Gifts extends AppCompatActivity {

    ActivityScreen12Binding binding;
    private boolean isSearchBarOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreen12Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent services = new Intent(getApplicationContext(), GiftDetailsActivity.class);
                startActivity(services);*/
                showWaitDialog();
            }
        });
        binding.imageView8.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.imageView7.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.imageView9.setOnClickListener(v -> {
            showWaitDialog();

        });
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.notifi.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.search.setOnClickListener(v -> toggleSearchBar(true));
        //Search item when search bar is open
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchWord = v.getText().toString();
            if(!searchWord.isEmpty()){
                Intent foundIntent = AppStaticData.searchSaveMoney(this, searchWord);
                if(foundIntent!=null){
                    startActivity(foundIntent);
                    finish();
                }
            }
            binding.searchBar.setText("");
            binding.searchBar.clearFocus();
            return false;
        });
    }
    
    private void toggleSearchBar(boolean b) {
        if(b){
            binding.search.setVisibility(View.GONE);
            binding.giftIconTop.setVisibility(View.GONE);
            binding.tvGift.setVisibility(View.GONE);
            binding.searchBar.setVisibility(View.VISIBLE);
            binding.searchBar.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null){
                View focus = getCurrentFocus();
                if(focus != null) inputMethodManager.toggleSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken()
                        ,InputMethodManager.SHOW_FORCED, 0);
            }
            isSearchBarOpen = true;
        }else{
            binding.searchBar.setText("");
            binding.searchBar.clearFocus();
            binding.search.setVisibility(View.VISIBLE);
            binding.giftIconTop.setVisibility(View.VISIBLE);
            binding.tvGift.setVisibility(View.VISIBLE);
            binding.searchBar.setVisibility(View.GONE);
            isSearchBarOpen = false;
        }
    }
    
    private void showWaitDialog() {
        /*new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();*/

        new AlertDialog.Builder(this).setTitle(Html.fromHtml("Offer is for <b>PS+</b> members only. Buy membership @<b>499</b>/- Rs. \n" +
                "But services will be available after<b>LockDown</b>."))
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I Understand", null).show();
    }
    
    @Override
    public void onBackPressed() {
        if(isSearchBarOpen)toggleSearchBar(false);
        else super.onBackPressed();
    }
}
