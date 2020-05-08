package save_money;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.AppStaticData;
import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivityHealthBinding;
import com.example.intern.mainapp.MainApp;

public class Health extends AppCompatActivity {
    ActivityHealthBinding binding;
    boolean isSearchBarOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ViewPager

        //viewPager = findViewById(R.id.viewpager);
/*
        //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Autoslide(), 1000, 3000);*/

        binding.dental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, DentalCategoryOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        binding.eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, EyeClinicOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        binding.homeopathy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, HomeopathicOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        binding.dietician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, DieticianOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        binding.pathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, Patholgy_LaborartoryOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        binding.physiotherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, PhysiotherapyOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });

        binding.home.setOnClickListener(view -> {
            Intent intent = new Intent(Health.this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.back.setOnClickListener(v -> onBackPressed());
        findViewById(R.id.notifi).setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
        binding.notifi.setOnClickListener(v -> {
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
            if (!searchWord.isEmpty()) {
                Intent foundIntent = AppStaticData.searchSaveMoney(Health.this, searchWord);
                if (foundIntent != null) {
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
        if (b) {
            binding.searchIcon.setVisibility(View.GONE);
            binding.healthIconTop.setVisibility(View.GONE);
            binding.tvHealth.setVisibility(View.GONE);
            binding.searchBar.setVisibility(View.VISIBLE);
            binding.searchBar.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View focus = getCurrentFocus();
                if (focus != null)
                    inputMethodManager.toggleSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken()
                            , InputMethodManager.SHOW_FORCED, 0);
            }
            isSearchBarOpen = true;
        } else {
            binding.searchBar.setText("");
            binding.searchBar.clearFocus();
            binding.searchIcon.setVisibility(View.VISIBLE);
            binding.healthIconTop.setVisibility(View.VISIBLE);
            binding.tvHealth.setVisibility(View.VISIBLE);
            binding.searchBar.setVisibility(View.GONE);
            isSearchBarOpen = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchBarOpen) toggleSearchBar(false);
        else super.onBackPressed();
    }

    private void showWaitDialog() {
        /*new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();*/

        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage(Html.fromHtml("Offer is for <b>PS+</b> members only. Buy membership @<b>499</b>/- Rs. \n" +
                "But services will be available after<b>LockDown</b>."))
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I Understand", null).show();
    }
}
