package save_money.Offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.intern.R;

import java.util.Timer;
import java.util.TimerTask;

import save_money.RecyclerAdapterOffers;
import save_money.ViewPagerAdapter;
import save_money.giveMeFakeData;

public class EyeClinicOffers extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    //ViewPager
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_clinic_offers);

        recyclerView = findViewById(R.id.recyclerView_eyeclinic);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        requestQueue = Volley.newRequestQueue(this);
        //ViewPager
        viewPager = findViewById(R.id.viewpager);

        //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Autoslide(), 1000, 3000);

        RecyclerAdapterOffers recyclerAdapterOffers = new RecyclerAdapterOffers(EyeClinicOffers.this, giveMeFakeData.giveMeFakeData_eyeCLinic());
        recyclerView.setAdapter(recyclerAdapterOffers);
    }
    public class Autoslide extends TimerTask {              //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
        @Override
        public void run() {
            //Runs the specified action on the UI thread.  UI thread-The default, primary thread created anytime an Android application is launched. It is in charge of handling all user interface and activities, unless otherwise specified. Runnable is an interface meant to handle sharing code between threads.
            EyeClinicOffers.this.runOnUiThread(new Runnable() {
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
