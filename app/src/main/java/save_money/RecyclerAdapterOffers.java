package save_money;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterOffers extends RecyclerView.Adapter<RecyclerAdapterOffers.ViewHolder> {

    Context context;
    List<Offers_Pojo> offers_pojoList = new ArrayList<>();
    public int lastSelectedPosition = -1;

    public RecyclerAdapterOffers(Context context, List<Offers_Pojo> offers_pojoList) {
        this.context = context;
        this.offers_pojoList = offers_pojoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.shopName.setText(offers_pojoList.get(position).getShopName());
        holder.area.setText("Area: "+offers_pojoList.get(position).getArea());
        holder.offer.setText("Offer: "+offers_pojoList.get(position).getOffers());

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.checkBox.setChecked(lastSelectedPosition == position);

        //if true, your checkbox will be selected, else unselected
      /*  holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(holder.checkBox.isChecked()){
                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                }else{
                    // something
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return offers_pojoList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName,area,offer;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView) ;
            shopName=itemView.findViewById(R.id.shopName);
            area=itemView.findViewById(R.id.area);
            offer=itemView.findViewById(R.id.offer);
            checkBox = itemView.findViewById(R.id.select_option);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(context, ""+shopName.getText(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
