package com.hbt.yiqing.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

import com.hbt.yiqing.R;
import com.hbt.yiqing.activity.MainActivity;
import com.hbt.yiqing.utils.DataUtils;
import com.hbt.yiqing.utils.ShowData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {
    public void setTouchMap(MainActivity.TouchMap touchMap) {
        this.touchMap = touchMap;
    }

    private MainActivity.TouchMap touchMap;

	/**
     * 线程池，用于加载xml
     */
    private ExecutorService mThreadPool;
    
    /**
     * 地图画笔
     */
    private Paint mPaint;
    
    /**
     * SVG地图资源Id
     */
    private int mMapResId = -1;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    // ...

	/**
     * 初始化画笔、线程池、解析自定义属性
     */
	private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
		mPaint = new Paint();
		// 设置抗锯齿
        mPaint.setAntiAlias(true);
        // 初始化线程池
        initThreadPool();
        // 解析自定义属性
        getMapResource(context, attrs, defStyleAttr);
	}

    /**
     * 初始化线程池
     */
    private void initThreadPool() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        };
        mThreadPool = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(10), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

	/**
     * 解析自定义属性
     */
    private void getMapResource(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MapView, defStyleAttr, 0);
        int resId = a.getResourceId(R.styleable.MapView_map, -1);
        a.recycle();
        setMapResId(resId);
    }

	/**
     * 设置地图资源Id
     */
    public void setMapResId(int resId) {
        mMapResId = resId;
        executeLoad();
    }


	//...

	@Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mThreadPool != null) {
            // 释放线程池
            mThreadPool.shutdown();
        }
    }

    public List<ProvinceItem> getmItemList() {
        return mItemList;
    }

    private List<ProvinceItem> mItemList;

    /**
     * 整个地图的最大矩形边界
     */
    private RectF mMaxRect;

    /**
     * 省份区块颜色列表
     */
    private int[] mColorArray = new int[] { 0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1 };

    // ...

    /**
     * 执行加载
     */
    private void executeLoad() {
        if (mMapResId <= 0) {
            return;
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 获取xml文件输入流
                InputStream inputStream = getResources().openRawResource(mMapResId);
                // 创建解析实例
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    // 解析输入流，得到Document实例
                    Document doc = builder.parse(inputStream);
                    // 获取根节点，即vector节点
                    Element rootElement = doc.getDocumentElement();
                    // 获取所有的path节点
                    NodeList items = rootElement.getElementsByTagName("path");

                    // 以下四个变量用来保存地图四个边界，用于确定缩放比例(适配屏幕)
                    float left = -1;
                    float right = -1;
                    float top = -1;
                    float bottom = -1;

                    // 解析path节点
                    List<ProvinceItem> list = new ArrayList<>();
                    for (int i = 0; i < items.getLength(); i++) {
                        Element element = (Element) items.item(i);
                        // 获取pathData内容
                        String pathData = element.getAttribute("android:pathData");
                        String province = element.getAttribute("android:province");
//                        String color_str = ShowData.getColorByProvince_test(province);
                        int color_str = DataUtils.getColorByProvinceName(province, MainActivity.TOTALCHANGE);
                        // 将pathData转换为path
                        Path path = PathParser.createPathFromPathData(pathData);

                        // 封装成ProvinceItem对象
                        ProvinceItem provinceItem = new ProvinceItem(path);
//                        provinceItem.setDrawColor(mColorArray[i % mColorArray.length]);
                        provinceItem.setDrawColor(color_str);
                        System.out.println(color_str);
//                        provinceItem.setDrawColor(Integer.parseInt(color_str, 16));
                        provinceItem.setProvince_name(province);

                        RectF rectF = new RectF();
                        // 计算当前path区域的矩形边界
                        path.computeBounds(rectF, true);
                        // 判断边界，最终获得的就是整个地图的最大矩形边界
                        left = left < 0 ? rectF.left : Math.min(left, rectF.left);
                        right = Math.max(right, rectF.right);
                        top = top < 0 ? rectF.top : Math.min(top, rectF.top);
                        bottom = Math.max(bottom, rectF.bottom);
                        list.add(provinceItem);
                    }
                    // 解析完成，保存节点列表和最大边界
                    mItemList = list;
                    mMaxRect = new RectF(left, top, right, bottom);
                    // 通知重新布局和绘制
                    post(new Runnable() {
                        @Override
                        public void run() {
                            requestLayout();
                            invalidate();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private ProvinceItem mSelectItem;

    /**
     * 地图缩放比例
     */
    private float mScale = 1f;

    // ...

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mMaxRect != null) {
            // 获取缩放比例
            double mapWidth = mMaxRect.width();
            mScale = (float) (width / mapWidth);
        }

        // 应用测量数据
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItemList != null) {
            // 使地图从画布左上角开始绘制（图片本身可能存在边距）
            canvas.translate(-mMaxRect.left, -mMaxRect.top);
            // 设置画布缩放，以(mMaxRect.left, mMaxRect.top)为基准进行缩放
            // 因为当前该点对应屏幕左上(0, 0)点
            canvas.scale(mScale, mScale, mMaxRect.left, mMaxRect.top);
            // 绘制所有省份区域，并设置是否选中状态
            for (ProvinceItem provinceItem : mItemList) {
                provinceItem.drawItem(canvas, mPaint, mSelectItem == provinceItem);
            }
        }
    }


    //bintao_事件处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将事件分发给所有的区块，如果事件未被消费，则调用View的onTouchEvent，这里会默认范围false
        if(mMaxRect == null){
            Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_LONG).show();
            return false;
        }
        if (handleTouch((int) (event.getX() / mScale + mMaxRect.left), (int) (event.getY() / mScale + mMaxRect.top), event)) {
            return true;
        }
        return super.onTouchEvent(event);
//        return ;
    }

    /**
     * 派发触摸事件
     */
    private boolean handleTouch(int x, int y, MotionEvent event) {
        if (mItemList == null) {
            return false;
        }

        boolean isTouch = false;

        ProvinceItem selectItem = null;
        System.out.println("即将for");
        for (ProvinceItem provinceItem : mItemList) {
            // 依次派发事件
            if (provinceItem.isTouch(x, y, event)) {
                // 选中省份区块
                selectItem = provinceItem;
                isTouch = true;
                touchMap.touch(provinceItem.getProvince_name());
                break;
            }
        }

        if (selectItem != null && selectItem != mSelectItem) {
            mSelectItem = selectItem;
            // 通知重绘
            postInvalidate();
        }
        return isTouch;
    }


}//class end
