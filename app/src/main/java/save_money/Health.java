package save_money;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.intern.R;

import java.util.Timer;
import java.util.TimerTask;

import save_money.Offers.DentalCategoryOffers;
import save_money.Offers.DieticianOffers;
import save_money.Offers.EyeClinicOffers;
import save_money.Offers.HomeopathicOffers;
import save_money.Offers.Patholgy_LaborartoryOffers;
import save_money.Offers.PhysiotherapyOffers;

public class Health extends AppCompatActivity {

    //ViewPager
    ViewPager viewPager;
    LinearLayout dental,eyeClinic,homeopathy,dietician,pathology,physiotherapy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        //ViewPager
        viewPager =findViewById(R.id.viewpager);
        dental = findViewById(R.id.dental);
        eyeClinic = findViewById(R.id.eye);
        homeopathy = findViewById(R.id.homeopathy);
        dietician = findViewById(R.id.dietician);
        pathology = findViewById(R.id.pathology);
        physiotherapy = findViewById(R.id.physiotherapy);

        //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Autoslide(),1000,3000);

        dental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, DentalCategoryOffers.class);
                startActivity(intent);
            }
        });
        eyeClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, EyeClinicOffers.class);
                startActivity(intent);
            }
        });
        homeopathy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, HomeopathicOffers.class);
                startActivity(intent);
            }
        });
        dietician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, DieticianOffers.class);
                startActivity(intent);
            }
        });
        pathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, Patholgy_LaborartoryOffers.class);
                startActivity(intent);
            }
        });
        physiotherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Health.this, PhysiotherapyOffers.class);
                startActivity(intent);
            }
        });
    }
    public  class Autoslide extends TimerTask {              //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
        @Override
        public void run() {
            //Runs the specified action on the UI thread.  UI thread-The default, primary thread created anytime an Android application is launched. It is in charge of handling all user interface and activities, unless otherwise specified. Runnable is an interface meant to handle sharing code between threads.
            Health.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0)
                    {
                        viewPager.setCurrentItem(1);
                    }
                    else  if(viewPager.getCurrentItem()==1)
                    {
                        viewPager.setCurrentItem(2);
                    }
                    else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}
