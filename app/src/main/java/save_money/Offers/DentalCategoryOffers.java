package save_money.Offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.intern.HomeActivity;
import com.example.intern.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import save_money.Health;
import save_money.Offers_Pojo;
import save_money.RecyclerAdapterOffers;
import save_money.SaveMoney;
import save_money.ViewPagerAdapter;
import save_money.giveMeFakeData;

public class DentalCategoryOffers extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    //ViewPager
    ViewPager viewPager;

    List<Offers_Pojo> offers_pojoList = new ArrayList<>();

    ImageView chat,backimg,homeImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental_category_offers);

        recyclerView = findViewById(R.id.recyclerView_dental);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        requestQueue = Volley.newRequestQueue(this);
        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        chat = findViewById(R.id.chat);
        backimg = findViewById(R.id.backimg);
        homeImg = findViewById(R.id.home);

        //Initialise ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Autoslide(), 1000, 3000);

        RecyclerAdapterOffers recyclerAdapterOffers = new RecyclerAdapterOffers(DentalCategoryOffers.this, giveMeFakeData.giveMeFakeData());
        recyclerView.setAdapter(recyclerAdapterOffers);

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

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DentalCategoryOffers.this, Health.class);
                startActivity(intent);
            }
        });
        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DentalCategoryOffers.this, HomeActivity.class);
                startActivity(intent);
            }
        });
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
    public void openWhatsApp(){
        try {
            String text = "Thank you for Connecting with PSbyPrarambh.\n" +
                    "Will you be willing to share your feedback with us? \n" +
                    "Please type “Feedback” in reply here.";// Replace with your message.

            String toNumber = "919737824882"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
