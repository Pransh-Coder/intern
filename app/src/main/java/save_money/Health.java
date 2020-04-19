package save_money;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intern.R;

public class Health extends AppCompatActivity {
    LinearLayout dental, eyeClinic, homeopathy, dietician, pathology, physiotherapy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        //ViewPager
        dental = findViewById(R.id.dental);
        eyeClinic = findViewById(R.id.eye);
        homeopathy = findViewById(R.id.homeopathy);
        dietician = findViewById(R.id.dietician);
        pathology = findViewById(R.id.pathology);
        physiotherapy = findViewById(R.id.physiotherapy);
        

        dental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, DentalCategoryOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        eyeClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, EyeClinicOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        homeopathy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, HomeopathicOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        dietician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, DieticianOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        pathology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, Patholgy_LaborartoryOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
        physiotherapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Health.this, PhysiotherapyOffers.class);
                startActivity(intent);*/
                showWaitDialog();
            }
        });
    }

/*    public class Autoslide extends TimerTask {              //TimerTask()-A task that can be scheduled for one-time or repeated execution by a Timer.
        @Override
        public void run() {
            //Runs the specified action on the UI thread.  UI thread-The default, primary thread created anytime an Android application is launched. It is in charge of handling all user interface and activities, unless otherwise specified. Runnable is an interface meant to handle sharing code between threads.
            Health.this.runOnUiThread(new Runnable() {
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
    
    private void showWaitDialog(){
        new AlertDialog.Builder(this).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
    }
}
