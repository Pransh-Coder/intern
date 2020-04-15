/*
package com.example.intern.socialnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
*/
/*import com.example.intern.database.FireStoreUtil;*//*


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocialRecycler extends RecyclerView.Adapter<SocialRecycler.ViewHolder> {
	private static String TAG = SocialRecycler.class.getSimpleName();
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private List UIDs;
	
	public SocialRecycler(Context context, String pin_code){
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				FireStoreUtil.getUserClusterReference(context, pin_code).get().addOnSuccessListener(snapshot -> {
					UIDs = Arrays.asList(snapshot.get("u"));
					notifyDataSetChanged();
				});
			}
		});
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_social_network, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		if(UIDs != null){
			holder.textView.setText(UIDs.get(position).toString());
		}
	}
	
	@Override
	public int getItemCount() {
		if(UIDs != null)return UIDs.size();
		else return 0;
	}
	
	class ViewHolder extends RecyclerView.ViewHolder{
		TextView textView;
		ViewHolder(@NonNull View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.tv_user_UID);
		}
	}
}
*/
