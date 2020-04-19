package save_money;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.intern.databinding.ActivitySaveMoneyBinding;
import com.example.intern.mainapp.MainApp;
import com.example.intern.FoodActivity;
import com.example.intern.Gifts;
import com.example.intern.Holiday;
import com.example.intern.Lifestyle;
import com.example.intern.R;
import com.example.intern.ServicesActivity;
import com.example.intern.mainapp.MainApp;

import java.util.Timer;
import java.util.TimerTask;

public class SaveMoney extends AppCompatActivity {
    ActivitySaveMoneyBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //ViewPager
       // viewPager = findViewById(R.id.viewpager);

/*        health = findViewById(R.id.health);
        gifs = findViewById(R.id.gifts);
        lifeStyle = findViewById(R.id.resturant);
        food = findViewById(R.id.food);
        holiday = findViewById(R.id.holiday);
        services = findViewById(R.id.services);
        services_button_back = findViewById(R.id.services_button_back);
        home = findViewById(R.id.home);*/

        /* //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);*/

        /*Timer timer = new Timer();


        public class SaveMoney extends AppCompatActivity {
        ActivitySaveMoneyBinding binding;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySaveMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Initialise ViewPager Adapter
/*        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new Autoslide(), 1000, 3000);*/

        binding.health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Health.class);
                startActivity(intent);
            }
        });

        binding.gifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Gifts.class);
                startActivity(intent);
            }
        });

        binding.lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Lifestyle.class);
                startActivity(intent);
            }
        });

        binding.food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, FoodActivity.class);
                startActivity(intent);
            }
        });
        binding.services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, ServicesActivity.class);
                startActivity(intent);
            }
        });

        binding.holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Holiday.class);
                startActivity(intent);
            }
        });

        binding.servicesButtonBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, MainApp.class);
                startActivity(intent);
            }
        });
        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, MainApp.class);
                startActivity(intent);
            }
        });

    }

/*    public class Autoslide extends TimerTask { //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(SaveMoney.this, MainApp.class);
            startActivity(intent);finish();
        });
        binding.back.setOnClickListener(v -> onBackPressed());
    }
    
    /* public class Autoslide extends TimerTask { //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
        @Override
        public void run() {
            //Runs the specified action on the UI thread.  UI thread-The default, primary thread created anytime an Android application is launched. It is in charge of handling all user interface and activities, unless otherwise specified. Runnable is an interface meant to handle sharing code between threads.
            SaveMoney.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }*/
}
