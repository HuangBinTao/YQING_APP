package com.hbt.yiqing.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hbt.yiqing.R;

import java.util.ArrayList;
import java.util.List;

public class ToolBarUtils {

    private List<TextView> textViews = new ArrayList<>();

    public void createToolBar(LinearLayout container, String[] strs){
        for (int i = 0;i < strs.length; i++) {
            TextView textView = (TextView) View.inflate(container.getContext(), R.layout.toolbar_item, null);
            textView.setText(strs[i]);
//            textView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.selector,0,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            textViews.add(textView);
            container.addView(textView, params);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnToolBarClickListener.onToolBarClick(finalI);
                }
            });

        }
        changeColor(0);
    }//createToolBar over

    public void changeColor(int position){
        for (TextView t:textViews) {
//            t.setSelected(false);
            t.setTextColor(0xFF393C3A);
        }
//        textViews.get(position).setSelected(true);
        textViews.get(position).setTextColor(0xFFE73F09);
    }
    //1.
    public interface OnToolBarClickListener{
        void onToolBarClick(int position);
    }

    //2.
    OnToolBarClickListener mOnToolBarClickListener;

    //3.
    public void setmOnToolBarClickListener(OnToolBarClickListener mOnToolBarClickListener) {
        this.mOnToolBarClickListener = mOnToolBarClickListener;
    }





}
