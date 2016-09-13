package com.example.getfilelist3;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.getfilelist3.MemoryUtil.Constants;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity {
	private ViewPager mContainer;
	private Indicator mIndicator;
	CircleFragment circleFragment;
	private List<Fragment> mFragmentList=new ArrayList<Fragment>();
	private FragmentAdapter mFragmentApdater;
	private List<Map<String, String>> SizeList;
	private double pro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setMyActionBar();
		InitSize();
		findViewById();
		InitViews();	
	}

	private void setMyActionBar() {
		// 设置标题栏
		//设置actionBar-标题栏
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("文件管理");
		actionBar.setDisplayShowHomeEnabled(false); // 设置没有logo
		// 设置背景色
		Resources r = getResources();
		Drawable mDrawable = r.getDrawable(R.color.orange);
		actionBar.setBackgroundDrawable(mDrawable);
	}

	private void findViewById() {
		mContainer=(ViewPager) findViewById(R.id.container);
		mIndicator=(com.example.getfilelist3.Indicator) findViewById(R.id.indicator);
		
	}
	
	private void InitViews() {
		circleFragment=new CircleFragment(pro);
		mFragmentList.add(circleFragment);
		
		//circleFragment=getFragmentManager().findFragmentById(R.layout.circle_frag);
		
		mFragmentApdater=new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
		mContainer.setAdapter(mFragmentApdater);
		mContainer.setCurrentItem(0);
		
		mContainer.setOnPageChangeListener(new OnPageChangeListener() {
			/** 
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。 
             */
			@Override
			public void onPageSelected(int position) {
				if(position==0)
					circleFragment.setDegree(pro);
					
			}
			 /** 
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比 
             * offsetPixels:当前页面偏移的像素位置 
             */  
			@Override
			public void onPageScrolled(int position, float offset, int offsetPixels) {
				mIndicator.Scroll(position, offset);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void InitSize() {
		
		//异步线程
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				MemoryUtil memoryUtil=new MemoryUtil(MainActivity.this);
				SizeList=new ArrayList<Map<String,String>>();
				
				long size=memoryUtil.getAMediaTotalSize(Constants.Audio);
				Log.i("Audio", getSize(size));
				Map<String, String> imMap1=new HashMap<String, String>();
				imMap1.put("Audio", getSize(size));
				SizeList.add(imMap1);
				
				size=memoryUtil.getAMediaTotalSize(Constants.Images);
				Log.i("Images", getSize(size));
				Map<String, String> imMap2=new HashMap<String, String>();
				imMap2.put("Images", getSize(size));
				SizeList.add(imMap2);
				
				size=memoryUtil.getAMediaTotalSize(Constants.Video);
				Log.i("Video", getSize(size));
				Map<String, String> imMap3=new HashMap<String, String>();
				imMap3.put("Video", getSize(size));
				SizeList.add(imMap3);
				
				long size2=memoryUtil.getRomTotalSize();
				Log.i("RomT", getSize(size2));
				Map<String, String> imMap4=new HashMap<String, String>();
				imMap4.put("RomT", getSize(size2));
				SizeList.add(imMap4);
				
				long size3=memoryUtil.getRomAvailableSize();
				Log.i("RomA", getSize(size3));
				Map<String, String> imMap5=new HashMap<String, String>();
				imMap5.put("RomA", getSize(size3));
				SizeList.add(imMap5);
				
				Log.i("size3", Long.toString(size3));
				Log.i("size2", Long.toString(size2));
				/*NumberFormat nf=NumberFormat.getPercentInstance();
				nf.setMinimumFractionDigits(2);
				String degreestr=nf.format(size3/size2);*/
				double pro=(double)size3/size2;
				Log.i("pro", Double.toString(pro));
				if(circleFragment!=null)
					circleFragment.setDegree(pro);
					//circleFragment.setDegree(270);
			}
		}).start();
	}
	private String getSize(Long size){
		DecimalFormat df = new DecimalFormat("###.##");
		float f=(float)size/(float)(1024*1024*1024);
		if(f<1.0){
			
			float f2 = ((float) size / (float) (1024*1024));
			if(f2<1.0){
				float f3=((float) size / (float) 1024);
				return df.format(new Float(f3).doubleValue()) + "KB";
			}
			return df.format(new Float(f2).doubleValue()) + "MB";
		}
		return df.format(new Float(f).doubleValue()) + "GB";
	}
}
