package com.example.intern.vendortest;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.R;
import com.example.intern.databinding.VendotDashboardBinding;

public class VendorMain extends AppCompatActivity {
	RecyclerView mTransRecycler;
	Button mMonthly, mYearly;
	private VendotDashboardBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = VendotDashboardBinding.inflate(getLayoutInflater());
		setContentView(R.layout.login_ui);
//		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.home_activity_background));
/*		mTransRecycler = findViewById(R.id.vendor_trans_recycler);
		mMonthly = findViewById(R.id.btn_vendor_trans_month);
		mYearly = findViewById(R.id.btn_vendor_trans_year);
		mMonthly.setOnClickListener(v->{
			mMonthly.setBackground(getResources().getDrawable(R.drawable.button_outlined_selected));
			mYearly.setBackground(getResources().getDrawable(R.drawable.button_oulined_not_selected));
			//TODO: Change data in recycler View
		});
		mYearly.setOnClickListener(v->{
			mYearly.setBackground(getResources().getDrawable(R.drawable.button_outlined_selected));
			mMonthly.setBackground(getResources().getDrawable(R.drawable.button_oulined_not_selected));
			//TODO:Change data in recycler view
		});*/
	}
	
	//For debugging Recycler View in Transaction History
/*	@Override
	protected void onStart() {
		super.onStart();
		List<String> custNum = Arrays.asList("20", "3", "14", "6");
		List<String> amount = Arrays.asList("300", "350", "1400", "6000");
		List<String> datnTime = Arrays.asList("20/1/20", "30/1/20", "14/2/20", "6/5/20");
		VendorTrHistRecAdapter adapter = new VendorTrHistRecAdapter(custNum, amount, datnTime);
		mTransRecycler.setLayoutManager(new LinearLayoutManager(this));
		mTransRecycler.setAdapter(adapter);
	}*/
	
	/*	@Override
	protected void onStart() {
		super.onStart();
		ArrayList<Entry> values = new ArrayList<>();
		values.add(new Entry(0, 200));
		values.add(new Entry(1,400));
		values.add(new Entry(2 , 300));
		values.add(new Entry(3, 600));
		LineDataSet set;
		final ArrayList<String> months = new ArrayList<>();
		months.add("Jan");months.add("Feb");months.add("Mar");months.add("Apr");months.add("May");months.add("Jun");
		months.add("Jul");months.add("Aug");months.add("Sep");months.add("Oct");months.add("Nov");months.add("Dec");
		XAxis xAxis = binding.vendorDashChart.getXAxis();
		xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
		if(binding.vendorDashChart.getData() != null && binding.vendorDashChart.getData().getDataSetCount() > 0){
			set = (LineDataSet) binding.vendorDashChart.getData().getDataSetByIndex(0);
			set.setValues(values);
			binding.vendorDashChart.notifyDataSetChanged();
		}else{
			set = new LineDataSet(values, "Amount");
			set.setColor(Color.GREEN);
			set.disableDashedLine();
			set.setDrawFilled(true);
			set.setLineWidth(2f);
			ArrayList<ILineDataSet> dataSets = new ArrayList<>();
			dataSets.add(set);
			LineData data = new LineData(dataSets);
			binding.vendorDashChart.setData(data);
			binding.vendorDashChart.notifyDataSetChanged();
		}
	}*/
}
