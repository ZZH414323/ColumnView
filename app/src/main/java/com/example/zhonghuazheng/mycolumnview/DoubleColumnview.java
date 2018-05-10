package com.example.zhonghuazheng.mycolumnview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhonghua.zheng on 2018/4/24.
 */

public class DoubleColumnview extends View {
    Paint paint, mPaint, linePaint;//画柱状图的画笔和下面文字的画笔以及线的画笔
    Activity activity;
    private Rect mBound;//画上面文字正方形区域
    private int mStartWidth, mHeight, mWidth;//文字开始位置，屏幕高度，屏幕宽度
    private int mSize = 10;//柱状图宽度
    private int btmWith, btmHight;//图片的长度和宽度
    private List<Float> list = new ArrayList<>();
    private int len;//坐标高度
    private int spacing = 20;//图片和线的间距
    private int bitmapArray[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private List<String> paintColorList = new ArrayList<>();//画笔颜色集合
    private Float maxValue = Float.valueOf(1);//最大的值
    private int padding = 20;//数字和柱状图的间距


    public DoubleColumnview(Context context) {
        super(context);

    }

    public DoubleColumnview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.activity = (Activity) context;
        //柱状图画笔
        paint = new Paint();
        //文字画笔
        mPaint = new Paint();
        //上面文字的画笔
        linePaint = new Paint();
        //设置画笔颜色
        paint.setColor(Color.RED);
        linePaint.setColor(Color.parseColor("#ECEBEB"));
        //设置画笔抗锯齿
        paint.setAntiAlias(true);
        //线宽
        paint.setStrokeWidth(1);
        linePaint.setStrokeWidth(1);
        //画字的正方形区域
        mBound = new Rect();

    }

    public void setData(List<Databean> listBean) {

        paintColorList.clear();
        list.clear();
        maxValue = Float.valueOf(1);
        setList(listBean);
        mStartWidth = getWidth() / 5;
        invalidate();
    }

    private void setList(List<Databean> list) {
            for (int i = 0; i < list.size(); i++) {
                if (compare(list.get(i).getLow_value(), list.get(i).getTimecode_avg_result())) {
                    paintColorList.add("#FF9F52");
                } else if (compare(list.get(i).getTimecode_avg_result(), list.get(i).getHigh_value())) {
                    paintColorList.add("#C11920");
                } else {
                    paintColorList.add("#0073CF");
                }

                this.list.add(Float.parseFloat(list.get(i).getTimecode_avg_result()));
                maxValue = maxValue > Float.parseFloat(list.get(i).getTimecode_avg_result()) ? maxValue : Float.parseFloat(list.get(i).getTimecode_avg_result());
            }

    }

    private boolean compare(String value, String avg) {
        return Float.parseFloat(value) > Float.parseFloat(avg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        len = height;
        //设置测量高度和宽度（必须要调用，不然无效果）
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        mWidth = dm.widthPixels;
//        mHeight = dm.heightPixels;
        mWidth = getWidth();
        mStartWidth = mWidth / 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画图标
        drawImageView(canvas);
    }

    private void drawImageView(Canvas canvas) {
        //为了先测量字所占的正方形空间
        mPaint.setTextSize(30);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#626262"));
        mPaint.getTextBounds("00.00", 0, "00.00".length(), mBound);
        maxValue = maxValue * (len - btmHight - spacing) / (len - btmHight - spacing - mBound.height() - padding * 3);//y轴得最大值


        for (int i = 1; i < 5; i++) {
            //画柱状图下的图标
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), bitmapArray[i - 1]);
            btmWith = bitmap.getWidth();
            btmHight = bitmap.getHeight();
            canvas.drawBitmap(bitmap, mStartWidth - btmWith / 2, len - btmHight, paint);

            //画左边柱状图和左边字
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(paintColorList.get(2 * i - 2)));
            RectF rectF = new RectF();
            rectF.left = mStartWidth - mBound.width() / 2;
            rectF.right = mStartWidth - mBound.width() / 2 + mSize;
            rectF.bottom = len - btmHight - spacing;
            rectF.top = len - btmHight - spacing - (len - btmHight - spacing) * list.get(2 * i - 2) / maxValue;
            if (!(list.get(2 * i - 2) == 0.0)) {
                canvas.drawRoundRect(rectF, 6, 6, paint);
                canvas.drawText(list.get(2 * i - 2) + "", rectF.left,
                        rectF.top - padding, mPaint);
            }

            //画右边柱状图和字
            paint.setColor(Color.parseColor(paintColorList.get(2 * i - 1)));
            rectF.left = mStartWidth + mBound.width() / 2 - mSize;
            rectF.right = mStartWidth + mBound.width() / 2;
            rectF.top = len - btmHight - spacing - (len - btmHight - spacing) * list.get(2 * i - 1) / maxValue;
            if (!(list.get(2 * i - 1) == 0.0)) {
                canvas.drawRoundRect(rectF, 6, 6, paint);
                canvas.drawText(list.get(2 * i - 1) + "", rectF.left,
                        rectF.top - padding, mPaint);
            }
            mStartWidth += mWidth / 5;
        }

        canvas.drawLine(20, len - btmHight - spacing, mWidth - 20, len - btmHight - spacing, linePaint);
    }

}
