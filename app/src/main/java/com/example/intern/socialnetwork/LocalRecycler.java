package com.example.intern.socialnetwork;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;

import java.util.ArrayList;


public class LocalRecycler extends RecyclerView.Adapter<LocalRecycler.Viewholder> {
    private Context context;
    private ArrayList<mLocalNetwork> listitem;

    public LocalRecycler(Context context, ArrayList<mLocalNetwork> listitem) {
        this.context = context;
        this.listitem=listitem ;
    }

    @NonNull
    @Override
    public LocalRecycler.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listrow,parent,false);
        Log.i("In view holder","invoked");
        return new Viewholder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
    final mLocalNetwork localNetwork=listitem.get(position);
    if(listitem.isEmpty())
        Toast.makeText(context,"EMPTY",Toast.LENGTH_LONG).show();
        holder.un.setText("Name :"+localNetwork.getun());
        holder.Occ.setText("Occupation :"+localNetwork.getOcc());
        holder.age.setText("Age :"+localNetwork.getDob());
        Log.i("In Bind view holder","invoked");

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView un;
        public TextView Occ;
        public TextView age;
        public int id;
        public Viewholder(@NonNull View view,Context ctx) {
            super(view);
            context=ctx;
            un = view.findViewById(R.id.unname);
            Occ = view.findViewById(R.id.occ);
            age = view.findViewById(R.id.age);

        }

    }
    }

