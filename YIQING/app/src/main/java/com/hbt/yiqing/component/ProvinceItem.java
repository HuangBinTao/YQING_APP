package com.hbt.yiqing.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.MotionEvent;

public class ProvinceItem {
    /**
     * 省份路径
     */
    private Path mPath;

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    /**
     * 省份短名
     */
    private String province_name;

    /**
     * 区块颜色
     */
    private int mDrawColor;

    /**
     * Path的有效区域
     */
    private Region mRegion;

	public ProvinceItem(Path path) {
        this.mPath = path;
        
        RectF rectF = new RectF();
        // 计算path的边界, exact参数无所谓，该方法不再使用这个参数
        mPath.computeBounds(rectF, true);

        mRegion = new Region();
        // 得到path和其最大矩形范围的交集区域
        mRegion.setPath(mPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

	/**
     * 设置区块绘制颜色
     */
    public void setDrawColor(int drawColor) {
        this.mDrawColor = drawColor;
    }
    public void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            // 选中状态

            paint.clearShadowLayer();
            paint.setStrokeWidth(1);

            // 绘制填充
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mDrawColor);
            canvas.drawPath(mPath, paint);

            // 绘制描边
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            canvas.drawPath(mPath, paint);
        } else {
            // 普通状态

            paint.setStrokeWidth(2);

            // 绘制底色+阴影
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            canvas.drawPath(mPath, paint);

            // 绘制填充
            paint.clearShadowLayer();
            paint.setColor(mDrawColor);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(mPath, paint);
        }
    }

    public boolean isTouch(int x, int y, MotionEvent event) {

        boolean isTouch = mRegion.contains(x, y);

        if (isTouch) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 按下
                    System.out.println(province_name);
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 滑动
                    break;
                case MotionEvent.ACTION_UP:
                    // 抬起
                    break;
                default:
                    break;
            }
        }

        return isTouch;
    }
	// ...
}
