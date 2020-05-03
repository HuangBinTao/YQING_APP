package com.hbt.yiqing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hbt.yiqing.R;
import com.hbt.yiqing.adapter.RecChinaAdapter;
import com.hbt.yiqing.component.MapView;
import com.hbt.yiqing.component.ProvinceItem;
import com.hbt.yiqing.entity.YQInfo;
import com.hbt.yiqing.utils.DataUtils;
import com.hbt.yiqing.utils.ThreadUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int NOWCHANGE = 0;
    public static final int TOTALCHANGE = 1;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.now_radio_btn)
    RadioButton nowRadioBtn;
    @BindView(R.id.total_radio_btn)
    RadioButton totalRadioBtn;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.china_name)
    TextView chinaName;
    @BindView(R.id.china_confirm)
    TextView chinaConfirm;
    @BindView(R.id.china_curConfirm)
    TextView chinaCurConfirm;
    @BindView(R.id.china_suspect)
    TextView chinaSuspect;
    @BindView(R.id.china_heal)
    TextView chinaHeal;
    @BindView(R.id.china_dead)
    TextView chinaDead;
    @BindView(R.id.provincename)
    TextView provincename;
    @BindView(R.id.provinceconfirm)
    TextView provinceconfirm;
    @BindView(R.id.provincecurConfirm)
    TextView provincecurConfirm;
    @BindView(R.id.provincesuspect)
    TextView provincesuspect;
    @BindView(R.id.provinceheal)
    TextView provinceheal;
    @BindView(R.id.provincedead)
    TextView provincedead;
    @BindView(R.id.jump_btn)
    Button jumpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.jump_btn)
    void jumpToWorld(){
        Intent intent = new Intent(MainActivity.this, WorldActivity.class);
        startActivity(intent);
    }
    //初始化中国总数据
    private void setChinaData() {

        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                YQInfo china = DataUtils.getChina();
//                chinaName.setText(china.getName());
                ThreadUtils.runInUIThread(new Runnable() {
                    @Override
                    public void run() {
                        chinaConfirm.setText(String.valueOf(china.getConfirm()));
                        chinaCurConfirm.setText(String.valueOf(china.getNowConfirm()));
                        chinaSuspect.setText(String.valueOf(china.getSuspect()));
                        chinaHeal.setText(String.valueOf(china.getHeal()));
                        chinaDead.setText(String.valueOf(china.getDead()));
                    }
                });

            }
        });


    }

    //初始化省份数据
    private void setProvinceData(String province_name) {
        YQInfo province = DataUtils.getProvinceByName(province_name);
        provincename.setText(province.getName());
        provinceconfirm.setText(String.valueOf(province.getConfirm()));
        provincecurConfirm.setText(String.valueOf(province.getNowConfirm()));
        provincesuspect.setText(String.valueOf(province.getSuspect()));
        provinceheal.setText(String.valueOf(province.getHeal()));
        provincedead.setText(String.valueOf(province.getDead()));
    }

    private void initData() {
        //为recycle设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(layoutManager);
        //为map设置事件触发接口
        map.setTouchMap(getTouchMap());
        //设置中国总数据
        setChinaData();

    }


    @OnCheckedChanged(R.id.now_radio_btn)
    void nowChange() {
        if (nowRadioBtn.isChecked()) {
            List<ProvinceItem> provinces = map.getmItemList();
            if (provinces == null) return;
            for (ProvinceItem province : provinces) {
                province.setDrawColor(DataUtils.getColorByProvinceName(province.getProvince_name(), NOWCHANGE));
            }
            map.postInvalidate();
        }

    }

    @OnCheckedChanged(R.id.total_radio_btn)
    void totalChange() {
        if (totalRadioBtn.isChecked()) {
            List<ProvinceItem> provinces = map.getmItemList();
            if (provinces == null) return;
            for (ProvinceItem province : provinces) {
                province.setDrawColor(DataUtils.getColorByProvinceName(province.getProvince_name(), TOTALCHANGE));
            }
            map.postInvalidate();
        }

    }

    //自定义接口
    public interface TouchMap {
        public void touch(String province_name);
    }

    public TouchMap getTouchMap() {
        return new TouchMap() {
            @Override
            public void touch(String province_name) {
                RecChinaAdapter.kk = 0;
                System.out.println("执行touch：点击" + province_name);
                setProvinceData(province_name);
                YQInfo province = DataUtils.getProvinceByName(province_name);
                LinkedList<YQInfo> cities = ((LinkedList<YQInfo>) province.getChildren());
                recycle.setAdapter(new RecChinaAdapter(cities));
            }
        };
    }
}
