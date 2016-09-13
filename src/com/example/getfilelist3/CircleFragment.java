package com.example.getfilelist3;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CircleFragment extends Fragment {
	private View view;
	private double pro;
	//private int degree;

	
	public CircleFragment(double pro) {
		super();
		this.pro = pro;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.circle_frag, container, false);
		if(pro!=0)
			Refresh();
		return view;
	}
	public void Refresh(){
		if(view==null) return;
		
		Circle circle=(Circle) view.findViewById(R.id.circle);
		
		
		Log.e("setSweepValueBefore", Double.toString(pro));
		double degree=pro*360;
		double invalidate=pro*100;
		Log.i("degree", Double.toString(degree));
		Log.i("invalidate", Double.toString(invalidate));
		circle.setSweepValue((int)degree);
		Log.e("setSweepValue", Double.toString(pro));
		circle.InvalidateView();
		int text=(int)invalidate;
		circle.mShowText=text+"%";
	}

	public void setDegree(double pro) {
		this.pro = pro;
		Refresh();
	}
	
}
