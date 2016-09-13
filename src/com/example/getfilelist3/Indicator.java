package com.example.getfilelist3;

import com.example.getfilelist3.R.color;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class Indicator extends LinearLayout{
	private Paint mPaint;			//用于画指示符
	
	private int mTop;				//指示符的top
	private int mLeft;				//指示符的left
	private int mWidth;			//指示符的width
	private int mHeight=5;			//指示符的高度
	private int mColor;			//指示符的颜色
	private int mChildCount;		//子Item的个数，用于计算指示符的宽度

	public Indicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT); 		/*必须设置背景，否则Ondraw不执行,因为LinearLayout
														继承ViewGroup,而ViewGroup默认不走Ondraw方法（因为ViewGroup不需要绘制，是子item需要绘制*/
		
		//获取自定义属性，指示符的颜色
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
		//mColor=getResources().getColor(R.color.yellow);		会报错？？
		mColor=ta.getColor(R.styleable.Indicator_color, 0x0000FF);				//获取自定义属性
		ta.recycle();															//调用结束后必须调用该方法，否则会影响下次使用
		
		
		//初始化paint
		mPaint=new Paint();
		//mPaint.setColor(mColor);
		
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);			//抗锯齿
	}

	@Override
	protected void onFinishInflate() {			//当View中(Indicator)所有的子控件均被映射成xml后触发
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mChildCount=getChildCount();			//获取子item的个数
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mTop=getMeasuredHeight();				//测量的高度即指示符顶部的位置（实际的view，与屏幕无关)
		int width=getMeasuredWidth();			//获取的测量的总宽度
		int height=mTop+mHeight;				//重新定义一下测量的高度
		if(mChildCount==0)	mWidth=0;
		else
			mWidth=width/mChildCount;				//指示符的宽度为总宽度/item的个数
		
		setMeasuredDimension(width, height);	//最后我们把调整后的height值保存起来，让它默认去layout吧。
	}
	
	/*
	 * 指示符滚动
	 * @param position 现在的位置
	 * @param offset	偏移量0~1
	 * */
	public void Scroll(int position,float offset){
		mLeft=(int) ((position+offset)*mWidth);
		invalidate();		
	}

	@Override
	protected void onDraw(Canvas canvas) { 
		Rect rect=new Rect(mLeft, mTop, mLeft+mWidth, mTop+mHeight);
		canvas.drawRect(rect, mPaint);
		super.onDraw(canvas);
	}
	
}
