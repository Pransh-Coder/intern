package com.example.intern.socialnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterPersonDetails extends RecyclerView.Adapter<RecyclerAdapterPersonDetails.ViewHolder> {

    public int lastSelectedPosition = -1;
    Context context;
    List<PersonDetails_pojo> personDetails_pojoList = new ArrayList<>();

    public RecyclerAdapterPersonDetails(Context context, List<PersonDetails_pojo> personDetails_pojoList) {
        this.context = context;
        this.personDetails_pojoList = personDetails_pojoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list, parent, false);
        return new RecyclerAdapterPersonDetails.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Name.setText("Name :" + personDetails_pojoList.get(position).getName());
        holder.occupation.setText("Occupation : " + personDetails_pojoList.get(position).getOccupation());
        holder.Age.setText("Age : " + personDetails_pojoList.get(position).getAge());

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.checkBox.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return personDetails_pojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name, occupation, Age;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Name);
            occupation = itemView.findViewById(R.id.occupation);
            Age = itemView.findViewById(R.id.Age);
            checkBox = itemView.findViewById(R.id.select_option);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    Toast.makeText(context, "" + Name.getText(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
