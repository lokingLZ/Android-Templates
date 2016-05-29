package loking.example.Collector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CollectorView extends View{
	private static String TAG = "CollectorView";
	private int collectorViewWidthPixel, collectorViewHeightPixel;
	private Paint mPaint;
	private Handler mHandler;
	private Runnable mRunnable = new Runnable() {
		public void run() {
			if(true){
				/* 线程 */
				mHandler.postDelayed(mRunnable, 100);
				CollectorView.this.invalidate();
			}
		}
	};

	public CollectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mHandler = new Handler();
		this.mPaint = new Paint();
		Log.e(TAG, getClass().getSimpleName()+" createCollectorView");
	}
	
	@Override/* 获取控件大小 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		collectorViewWidthPixel = this.getMeasuredWidth();
		collectorViewHeightPixel = this.getMeasuredHeight();
	}

	@Override/* 绘图 */
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		initCanvas(canvas, Color.GREEN);
	}

	/* 初始化画板 */
	private void initCanvas(Canvas canvas, int background) {
		mPaint.setColor(background);
		canvas.drawRect(0, 0,collectorViewWidthPixel,collectorViewHeightPixel,mPaint);
	}


	@Override/* 屏幕事件 */
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e(TAG, getClass().getSimpleName()+" onTouchEvent");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://按下
			mHandler.post(mRunnable);
			break;

		case MotionEvent.ACTION_UP://放开
			
			break;
			
		case MotionEvent.ACTION_MOVE://拖动
			mHandler.removeCallbacks(mRunnable);
			break;
			
		default:
			
			break;
		}
		return true;
	}
}
