package com.example.intern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityScreen11Binding;
import com.example.intern.mainapp.MainApp;

public class Lifestyle extends AppCompatActivity {
    ActivityScreen11Binding binding;
    private boolean isSearchBarOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScreen11Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().hide();
        binding.salonLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.fitnessLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.spaLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.parlorLayout.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.ivBackButton.setOnClickListener(v -> onBackPressed());
        binding.ivHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
        binding.ivNotifButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.ivSearchButton.setOnClickListener(v -> toggleSearchBar(true));
        //Search item when search bar is open
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchWord = v.getText().toString();
            if(!searchWord.isEmpty()){
                Intent foundIntent = AppStaticData.searchSaveMoney(Lifestyle.this, searchWord);
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
            binding.ivSearchButton.setVisibility(View.GONE);
            binding.ivLifestyleIcon.setVisibility(View.GONE);
            binding.tvLifestyle.setVisibility(View.GONE);
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
            binding.ivSearchButton.setVisibility(View.VISIBLE);
            binding.ivLifestyleIcon.setVisibility(View.VISIBLE);
            binding.tvLifestyle.setVisibility(View.VISIBLE);
            binding.searchBar.setVisibility(View.GONE);
            isSearchBarOpen = false;
        }
    }
    
    @Override
    public void onBackPressed() {
        if(isSearchBarOpen)toggleSearchBar(false);
        else super.onBackPressed();
    }
    
    private void showWaitDialog(){
        /*new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();*/

        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("These offers are for PS+ members @499र् only. Registration is not available during lock down (COVID-19).")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I Understand", null).show();
    }
}
