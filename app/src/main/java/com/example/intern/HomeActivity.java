package com.example.intern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import save_money.SaveMoney;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout save_money;
    //Navigation Drawer
     DrawerLayout drawerLayout;
     Toolbar toolbar;
     ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView=findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener(this);

        save_money = findViewById(R.id.SaveMoneyLinear);

        //Navigation Drawer
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);                                                           // we have to add icon to drawable
        actionBarDrawerToggle.syncState();
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(6).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(8).setActionView(R.layout.menu_image);

        save_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saveMoneyIntent = new Intent(getApplicationContext(), SaveMoney.class);
                startActivity(saveMoneyIntent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                   // without this icon will not work which is at the left of screen

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_reduce_expense) {
            Intent intent = new Intent(this, ReduceExpenses.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_total_disc) {

            Intent intent = new Intent(this, TotalDiscountReceived.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_edit_profile) {

            Intent intent = new Intent(this, EditProfile.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_medical_records) {

            Intent intent = new Intent(this, MedicalRecord.class);
            startActivity(intent);
        }
        return false;

    }
}
