package com.example.intern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityFoodBinding;
import com.example.intern.mainapp.MainApp;

public class FoodActivity extends AppCompatActivity {
    ActivityFoodBinding binding;
    private boolean isSearchBarOpen;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.foodImageView1.setOnClickListener(v -> {
            showWaitDialog();
        });
        binding.foodButtonBack.setOnClickListener(v -> onBackPressed());
        binding.foodHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
        binding.foodButtonNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.searchIcon.setOnClickListener(v -> toggleSearchBar(true));
        //Search item when search bar is open
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchWord = v.getText().toString();
            if(!searchWord.isEmpty()){
                Intent foundIntent = AppStaticData.searchSaveMoney(FoodActivity.this, searchWord);
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
            binding.searchIcon.setVisibility(View.GONE);
            binding.imgViewFood.setVisibility(View.GONE);
            binding.txtFood.setVisibility(View.GONE);
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
            binding.searchIcon.setVisibility(View.VISIBLE);
            binding.imgViewFood.setVisibility(View.VISIBLE);
            binding.txtFood.setVisibility(View.VISIBLE);
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
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("These offers are avail by PS+ members @ 499/- Rs only. \n" +
                "Registration is not available during lock down (COVID-19).")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}
