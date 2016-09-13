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
	private int mCircleXY;			//Բ�ĵ�λ��
	private float mRadius;
	private float length;
	private RectF mARectF;
	private float startAngle;		//δ��ֵ
	private float msweepAngle;		//����Ҫ�����angle
	private float mDrawingAngle=0;	//���ڻ��ƵĽǶ�
	private int mSeq;				//��ʾ���ǵڼ����ƶ�
	private int mRate=2;				//ÿ���ƶ��ĽǶ�
	public String mShowText;		//δ��ֵ
	private int mShowTextSize;		//δ��ֵ
	
	private Handler handler=new Handler();		//����handler�������runnable����
	Runnable drawRunnable=new Runnable() {		//��ʱ��
		
		@Override
		public void run() {
			if(mDrawingAngle>=msweepAngle){
				mDrawingAngle=msweepAngle;
				invalidate();
				//�Ƴ���ǰRunnable
				handler.removeCallbacks(drawRunnable);	//�Ƴ���ʱ��
			}else{
				mDrawingAngle=mSeq*mRate;
				mSeq++;
				//���Դ������߳���Ϣ�ĺ���
				handler.postDelayed(drawRunnable, 3);	//5����ִ��һ��runnable
				invalidate();							//ʵ��Ҫ�����¾���ˢ��(����ondraw)
			}
		}
	};
	
	public Circle(Context context, AttributeSet attrs) {
		super(context, attrs);
		length=getResources().getDisplayMetrics().widthPixels;		//�õ���Ļ���
		mCircleXY=(int) (length/2);
		mRadius=length/4;
		startAngle=270;
		msweepAngle=0;
		
		//����Բ����
		mCirclePaint=new Paint();
		mCirclePaint.setColor(getResources().getColor(R.color.lightblue));
		//���û��߻���
		mArcPaint=new Paint();
		mArcPaint.setColor(getResources().getColor(R.color.lightskyblue));
		mArcPaint.setStrokeWidth((float) 20.0);	//�����߿�
		mArcPaint.setStyle(Style.STROKE);		//���ÿ���Ч��
		//�����ı�����
		mTextPaint=new Paint();
		mTextPaint.setColor(getResources().getColor(R.color.white));
		mTextPaint.setTextSize(58);
		
		//mShowText="Android Proportion";
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.Indicator, 0, 0);
		mShowText=ta.getString(R.styleable.Circle_text);
		ta.recycle();
		mShowTextSize=7;
		//���ܸ�0.1��0.9������ƫ�ƣ�
		//���û��߾��ε����ϡ��ҡ�������
		mARectF=new RectF();//((float)(length*0.1), (float)(length*0.1), (float)(length*0.9), (float)(length*0.9));
		mARectF.left=mCircleXY-mRadius-20;
		mARectF.top=mARectF.left;
		mARectF.right=mCircleXY+mRadius+20;
		mARectF.bottom=mARectF.right;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ����Բ
		canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);
		//���ƻ���  ��Ҫʵ�ֶ�̬չʾ������
			canvas.drawArc(mARectF, startAngle, mDrawingAngle, false, mArcPaint);				
		//��������
			//����ı���Ŀ�Ⱥ͸߶�
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
		handler.postDelayed(drawRunnable, 0);		//0ms���ú���ö�ʱ��
	}
}
