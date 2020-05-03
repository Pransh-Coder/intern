package com.example.intern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.databinding.ActivityServicesBinding;
import com.example.intern.mainapp.MainApp;

import save_money.SaveMoney;

public class ServicesActivity extends AppCompatActivity {
    ActivityServicesBinding binding;
    private boolean isSearchBarOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.linearLayoutMobileRepair.setOnClickListener(v -> showWaitDialog());
        binding.linearLayoutRoRepair.setOnClickListener(v -> showWaitDialog());
        binding.linearLayoutBikeRepair.setOnClickListener(v -> showMyDialog());
        binding.servicesButtonBack.setOnClickListener(v -> {
            Intent intent = new Intent(ServicesActivity.this, SaveMoney.class);
            startActivity(intent);

        });
        binding.servicesButtonHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);finish();
        });
        binding.servicesButtonNotification.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.servicesButtonSearch.setOnClickListener(v -> toggleSearchBar(true));
        //Search item when search bar is open
        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchWord = v.getText().toString();
            if(!searchWord.isEmpty()){
                Intent foundIntent = AppStaticData.searchSaveMoney(ServicesActivity.this, searchWord);
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
            binding.servicesButtonSearch.setVisibility(View.GONE);
            binding.imgViewServices.setVisibility(View.GONE);
            binding.txtServices.setVisibility(View.GONE);
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
            binding.servicesButtonSearch.setVisibility(View.VISIBLE);
            binding.imgViewServices.setVisibility(View.VISIBLE);
            binding.txtServices.setVisibility(View.VISIBLE);
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
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
    private void showMyDialog(){
        startActivity(new Intent(ServicesActivity.this,two_wheeler_repair_list.class));
    }
}
