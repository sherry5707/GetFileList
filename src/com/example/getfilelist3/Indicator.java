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
	private Paint mPaint;			//���ڻ�ָʾ��
	
	private int mTop;				//ָʾ����top
	private int mLeft;				//ָʾ����left
	private int mWidth;			//ָʾ����width
	private int mHeight=5;			//ָʾ���ĸ߶�
	private int mColor;			//ָʾ������ɫ
	private int mChildCount;		//��Item�ĸ��������ڼ���ָʾ���Ŀ��

	public Indicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT); 		/*�������ñ���������Ondraw��ִ��,��ΪLinearLayout
														�̳�ViewGroup,��ViewGroupĬ�ϲ���Ondraw��������ΪViewGroup����Ҫ���ƣ�����item��Ҫ����*/
		
		//��ȡ�Զ������ԣ�ָʾ������ɫ
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
		//mColor=getResources().getColor(R.color.yellow);		�ᱨ����
		mColor=ta.getColor(R.styleable.Indicator_color, 0x0000FF);				//��ȡ�Զ�������
		ta.recycle();															//���ý����������ø÷����������Ӱ���´�ʹ��
		
		
		//��ʼ��paint
		mPaint=new Paint();
		//mPaint.setColor(mColor);
		
		mPaint.setColor(mColor);
		mPaint.setAntiAlias(true);			//�����
	}

	@Override
	protected void onFinishInflate() {			//��View��(Indicator)���е��ӿؼ�����ӳ���xml�󴥷�
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mChildCount=getChildCount();			//��ȡ��item�ĸ���
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mTop=getMeasuredHeight();				//�����ĸ߶ȼ�ָʾ��������λ�ã�ʵ�ʵ�view������Ļ�޹�)
		int width=getMeasuredWidth();			//��ȡ�Ĳ������ܿ��
		int height=mTop+mHeight;				//���¶���һ�²����ĸ߶�
		if(mChildCount==0)	mWidth=0;
		else
			mWidth=width/mChildCount;				//ָʾ���Ŀ��Ϊ�ܿ��/item�ĸ���
		
		setMeasuredDimension(width, height);	//������ǰѵ������heightֵ��������������Ĭ��ȥlayout�ɡ�
	}
	
	/*
	 * ָʾ������
	 * @param position ���ڵ�λ��
	 * @param offset	ƫ����0~1
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
