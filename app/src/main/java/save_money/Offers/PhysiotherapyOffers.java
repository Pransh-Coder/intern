package save_money.Offers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.intern.HomeActivity;
import com.example.intern.R;

import save_money.Health;

public class PhysiotherapyOffers extends AppCompatActivity {

    ImageView chat,backimg,homeImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiotherapy_offers);

        chat = findViewById(R.id.chat);
        backimg = findViewById(R.id.backimg);
        homeImg = findViewById(R.id.home);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhysiotherapyOffers.this, Health.class);
                startActivity(intent);
            }
        });
        homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhysiotherapyOffers.this, HomeActivity.class);
                startActivity(intent);
            }
        });
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
