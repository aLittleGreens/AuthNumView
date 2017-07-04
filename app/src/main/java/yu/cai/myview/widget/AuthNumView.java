package yu.cai.myview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

import yu.cai.myview.R;

/**
 * Created by 蔡宇奎 on 2017-7-4.
 */

public class AuthNumView extends View {
    private static final String TAG = "CustomTitleView";
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;
    /**
     * 背景颜色设置
     */
    private int mBackgroundColor;
    /**
     * 点数设置
     */
    private int mPointNum;
    /**
     * 线数设置
     */
    private int mLineNum;

    private int mLineColor;

    private int mPointColor;

    // 点数设置
    private static final int DEFOULT_POINT_NUM = 100;
    // 线段数设置
    private static final int DEFOULT_LINE_NUM = 2;

    private static final int DEFOULT_TEXT_COLOR = Color.BLACK;

    private static final int DEFOULT_LINE_COLOR = Color.BLUE;

    private static final int DEFOULT_POINT_COLOR =Color.BLUE;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.YELLOW;

    private static final int DEFAULT_TEXT_NUM = 4;

    private Paint mPaint;
    private Rect mBound;
    private Random random = new Random();

    public AuthNumView(Context context) {
        this(context, null);
    }

    public AuthNumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuthNumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AuthNumView);
//        mTitleText = a.getString(R.styleable.AuthNumView_titleText);
        mTitleTextColor = a.getColor(R.styleable.AuthNumView_titleTextColor, DEFOULT_TEXT_COLOR);
        mTitleTextSize = a.getDimensionPixelSize(R.styleable.AuthNumView_titleTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mBackgroundColor = a.getColor(R.styleable.AuthNumView_backgroundColor,DEFAULT_BACKGROUND_COLOR);
        mLineNum = a.getInt(R.styleable.AuthNumView_line_num,DEFOULT_LINE_NUM);
        mPointNum = a.getInt(R.styleable.AuthNumView_point_num,DEFOULT_POINT_NUM);
        mLineColor = a.getColor(R.styleable.AuthNumView_lineColor,DEFOULT_LINE_COLOR);
        mPointColor = a.getColor(R.styleable.AuthNumView_pointColor,DEFOULT_POINT_COLOR);

        mTitleText = randomText();
        a.recycle();
        initPaint();
        initEvent();
    }

    private void initEvent() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
//                invalidate();//只能在UI线程操作
                postInvalidate();//可以子线程中执行
            }
        });
    }

    private String randomText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <DEFAULT_TEXT_NUM; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void initPaint() {
        mPaint = new Paint();
//        mPaint.setColor(mTitleTextColor);
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"widthMeasureSpec:"+widthMeasureSpec+",heightMeasureSpec:"+heightMeasureSpec);
        //属性设置为wrap_content时，，默认结果是match_content
        /**
         * MeasureSpec的specMode,一共三种类型：
         EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.e(TAG, "widthMode: "+widthMode+",heightMode:"+heightMode );
        Log.e(TAG, "widthSize: "+widthSize+",heightSize:"+heightSize );//测量的宽高

        int width;
        int height ;
        if(widthMode==MeasureSpec.EXACTLY){//如果是固定宽高或者是match_content
            width = widthSize;
        }else{
            width = mBound.width() + getPaddingLeft() + getPaddingRight();
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            height = mBound.height()+getPaddingBottom()+getPaddingTop();
        }
        Log.e(TAG,"width:"+width+",height"+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 背景
         */
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        /**
         * 写字
         */
        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);

        /**
         *  画线
         */
        //划线
        mPaint.setColor(mLineColor);
        int [] line;
        for(int i = 0; i < mLineNum; i ++)
        {
            //设置线宽
            mPaint.setStrokeWidth(5);
            line = getLine(getMeasuredHeight(), getMeasuredWidth());
            canvas.drawLine(line[0], line[1], line[2], line[3], mPaint);
        }
        /**
         * 画点
         */
        mPaint.setColor(mPointColor);
        // 绘制小圆点
        int [] point;
        int randomInt;
        for(int i = 0; i < mPointNum; i ++)
        {
            //随机获取点的大小
            randomInt = random.nextInt(10);
            point = getPoint(getMeasuredHeight(), getMeasuredWidth());
            canvas.drawCircle(point[0], point[1], randomInt, mPaint);
        }


    }

    // 随机产生点的圆心点坐标
    private int[] getPoint(int height, int width) {
        int[] tempCheckNum = { 0, 0, 0, 0 };
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }

    //随机产生划线的起始点坐标和结束点坐标
    private int[] getLine(int height, int width) {
        int[] tempCheckNum = { 0, 0, 0, 0 };
        for (int i = 0; i < 4; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    //获取验证码
    public String getAuthCode() {
        return mTitleText;
    }
}
