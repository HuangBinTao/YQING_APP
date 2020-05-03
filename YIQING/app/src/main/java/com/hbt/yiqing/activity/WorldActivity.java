package com.hbt.yiqing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hbt.yiqing.R;
import com.hbt.yiqing.fragment.AfricaFragment;
import com.hbt.yiqing.fragment.AsianFragment;
import com.hbt.yiqing.fragment.AustrliaFragment;
import com.hbt.yiqing.fragment.EuropeFragment;
import com.hbt.yiqing.fragment.NorthFragment;
import com.hbt.yiqing.fragment.SouthFragment;
import com.hbt.yiqing.utils.DataUtils_world;
import com.hbt.yiqing.utils.ThreadUtils;
import com.hbt.yiqing.utils.ToolBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorldActivity extends AppCompatActivity {

    List<Fragment> fragments = new ArrayList<>();
    String continents_name[] = new String[]{"亚洲", "非洲", "欧洲", "北美", "南美", "澳洲"};
    @BindView(R.id.bottom)
    LinearLayout bottom;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.page)
    ViewPager page;
    @BindView(R.id.world_total_btn)
    Button worldTotalBtn;
    private ToolBarUtils toolBarUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        ButterKnife.bind(this);
        ThreadUtils.runInThread(new Runnable() {
            //获取全球各国疫情数据
            @Override
            public void run() {
                DataUtils_world.getAllNations();
            }
        });
        initFragment();
        handlePage();
        createToolBar();
    }//onCreate end

    @OnClick(R.id.world_total_btn)
    void worldTotalBtnClick(){
        System.out.println("点击弹出");
        AlertDialog dialog = new AlertDialog.Builder(WorldActivity.this).create();
        dialog.setTitle("全球疫情总计");
        View vs = View.inflate(WorldActivity.this,R.layout.alert_dialog,null);
        TextView tv_confirm = vs.findViewById(R.id.confirm);
        TextView tv_curConfirm = vs.findViewById(R.id.curConfirm);
        TextView tv_heal = vs.findViewById(R.id.heal);
        TextView tv_dead = vs.findViewById(R.id.dead);
        tv_confirm.setText(tv_confirm.getText()+String.valueOf(DataUtils_world.t_confirm));
        tv_curConfirm.setText(tv_curConfirm.getText()+String.valueOf(DataUtils_world.t_cur_confirm));
        tv_heal.setText(tv_heal.getText()+String.valueOf(DataUtils_world.t_heal));
        tv_dead.setText(tv_dead.getText()+String.valueOf(DataUtils_world.t_dead));
        dialog.setView(vs);
        dialog.show();
    }//worldTotalBtnClick end

    private void initFragment() {
        fragments.add(new AsianFragment());
        fragments.add(new AfricaFragment());
        fragments.add(new EuropeFragment());
        fragments.add(new NorthFragment());
        fragments.add(new SouthFragment());
        fragments.add(new AustrliaFragment());
    }

    private void handlePage() {
        page.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        page.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolBarUtils.changeColor(position);
                title.setText(continents_name[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }//handlePage end

    void createToolBar() {
        toolBarUtils = new ToolBarUtils();
        toolBarUtils.createToolBar(bottom, continents_name);
        toolBarUtils.setmOnToolBarClickListener(new ToolBarUtils.OnToolBarClickListener() {
            @Override
            public void onToolBarClick(int position) {
                toolBarUtils.changeColor(position);
                title.setText(continents_name[position]);
                page.setCurrentItem(position);
            }
        });
    }//createToolBar end
}//class end
