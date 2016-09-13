package com.example.getfilelist3;

import android.R.string;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Circle extends View {
	private Paint mCirclePaint;
	private Paint mArcPaint;
	private Paint mTextPaint;
	private int mCircleXY;			//圆心的位置
	private float mRadius;
	private float length;
	private RectF mARectF;
	private float startAngle;		//未赋值
	private float msweepAngle;		//最终要到达的angle
	private float mDrawingAngle=0;	//正在绘制的角度
	private int mSeq;				//表示这是第几次移动
	private int mRate=2;				//每次移动的角度
	public String mShowText;		//未赋值
	private int mShowTextSize;		//未赋值
	
	private Handler handler=new Handler();		//创建handler对象调用runnable对象
	Runnable drawRunnable=new Runnable() {		//定时器
		
		@Override
		public void run() {
			if(mDrawingAngle>=msweepAngle){
				mDrawingAngle=msweepAngle;
				invalidate();
				//移除当前Runnable
				handler.removeCallbacks(drawRunnable);	//移除定时器
			}else{
				mDrawingAngle=mSeq*mRate;
				mSeq++;
				//可以创建多线程消息的函数
				handler.postDelayed(drawRunnable, 3);	//5毫秒执行一次runnable
				invalidate();							//实际要做的事就是刷新(调用ondraw)
			}
		}
	};
	
	public Circle(Context context, AttributeSet attrs) {
		super(context, attrs);
		length=getResources().getDisplayMetrics().widthPixels;		//得到屏幕宽度
		mCircleXY=(int) (length/2);
		mRadius=length/4;
		startAngle=270;
		msweepAngle=0;
		
		//设置圆画笔
		mCirclePaint=new Paint();
		mCirclePaint.setColor(getResources().getColor(R.color.lightblue));
		//设置弧线画笔
		mArcPaint=new Paint();
		mArcPaint.setColor(getResources().getColor(R.color.lightskyblue));
		mArcPaint.setStrokeWidth((float) 20.0);	//设置线宽
		mArcPaint.setStyle(Style.STROKE);		//设置空心效果
		//设置文本画笔
		mTextPaint=new Paint();
		mTextPaint.setColor(getResources().getColor(R.color.white));
		mTextPaint.setTextSize(58);
		
		//mShowText="Android Proportion";
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
		mShowText=ta.getString(R.styleable.Circle_text);
		ta.recycle();
		mShowTextSize=7;
		//不能改0.1和0.9，改了偏移？
		//设置弧线矩形的左、上、右、下坐标
		mARectF=new RectF();//((float)(length*0.1), (float)(length*0.1), (float)(length*0.9), (float)(length*0.9));
		mARectF.left=mCircleXY-mRadius-20;
		mARectF.top=mARectF.left;
		mARectF.right=mCircleXY+mRadius+20;
		mARectF.bottom=mARectF.right;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制圆
		canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);
		//绘制弧线  想要实现动态展示？？？
			canvas.drawArc(mARectF, startAngle, mDrawingAngle, false, mArcPaint);				
		//绘制文字
			//获得文本框的宽度和高度
		Rect bounds=new Rect();
		if(mShowText==null) mShowText="";
		mTextPaint.getTextBounds(mShowText, 0, mShowText.length(), bounds);
		float x=mCircleXY-bounds.width()/2;
		float y=mCircleXY+bounds.height()/2;
		
		
		canvas.drawText(mShowText,0, mShowText.length(),x,y, mTextPaint);
	}

	public void setSweepValue(float sweepValue){
		msweepAngle=sweepValue;  
	}
	
	public void InvalidateView(){
		if(msweepAngle==0) msweepAngle=70;
		handler.postDelayed(drawRunnable, 0);		//0ms调用后调用定时器
	}
}
