package save_money.Offers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.intern.R;
import com.example.intern.qr.QRScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import save_money.Offers_Pojo;
import save_money.RecyclerAdapterOffers;
import save_money.ViewPagerAdapter;
import save_money.giveMeFakeData;

public class DentalCategoryOffers extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    ImageButton mProceedButton;
    //ViewPager
    ViewPager viewPager;

    List<Offers_Pojo> offers_pojoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental_category_offers);
        mProceedButton = findViewById(R.id.btn_proceed);
        recyclerView = findViewById(R.id.recyclerView_dental);
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

        RecyclerAdapterOffers recyclerAdapterOffers = new RecyclerAdapterOffers(DentalCategoryOffers.this, giveMeFakeData.giveMeFakeData());
        recyclerView.setAdapter(recyclerAdapterOffers);
        recyclerAdapterOffers.setListener(new RecyclerAdapterOffers.ClickListener() {
            @Override
            public void isSelected() {
                mProceedButton.setVisibility(View.VISIBLE);
            }
        });
        mProceedButton.setOnClickListener(v->{
            Intent intent = new Intent(DentalCategoryOffers.this, QRScanner.class);
            startActivity(intent);
        });

        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DentalCategory",response);
            }
            RecyclerAdapterOffers recyclerAdapterOffers = new RecyclerAdapterOffers(DentalCategoryOffers.this, giveMeFakeData.giveMeFakeData());
            recyclerView.setAdapter(recyclerAdapterOffers);

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError_Dental",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParams();
            }
        };
        requestQueue.add(stringRequest);*/
    }

    public class Autoslide extends TimerTask {              //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
        @Override
        public void run() {
            //Runs the specified action on the UI thread.  UI thread-The default, primary thread created anytime an Android application is launched. It is in charge of handling all user interface and activities, unless otherwise specified. Runnable is an interface meant to handle sharing code between threads.
            DentalCategoryOffers.this.runOnUiThread(new Runnable() {
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
