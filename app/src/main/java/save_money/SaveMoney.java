package save_money;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.intern.FoodActivity;
import com.example.intern.Gifts;
import com.example.intern.Holiday;
import com.example.intern.HomeActivity;
import com.example.intern.Lifestyle;
import com.example.intern.R;
import com.example.intern.ServicesActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SaveMoney extends AppCompatActivity {

    //ViewPager
    ViewPager viewPager;
    CardView health, lifeStyle, food, services, gifs, holiday;
    ImageView backImg,homeIMG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_money);

        //ViewPager
        viewPager = findViewById(R.id.viewpager);

        health = findViewById(R.id.health);
        gifs = findViewById(R.id.gifts);
        lifeStyle = findViewById(R.id.resturant);
        food = findViewById(R.id.food);
        holiday = findViewById(R.id.holiday);
        services = findViewById(R.id.services);
        backImg = findViewById(R.id.services_button_back);
        homeIMG = findViewById(R.id.homeIMG);
        //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Autoslide(), 1000, 3000);

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Health.class);
                startActivity(intent);
            }
        });

        gifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Gifts.class);
                startActivity(intent);
            }
        });

        lifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Lifestyle.class);
                startActivity(intent);
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, FoodActivity.class);
                startActivity(intent);
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, ServicesActivity.class);
                startActivity(intent);
            }
        });

        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, Holiday.class);
                startActivity(intent);
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        homeIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveMoney.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public class Autoslide extends TimerTask { //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
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
    }
}
