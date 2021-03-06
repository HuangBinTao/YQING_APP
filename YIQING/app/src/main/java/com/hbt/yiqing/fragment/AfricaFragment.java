package com.hbt.yiqing.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hbt.yiqing.R;
import com.hbt.yiqing.adapter.RecChinaAdapter;
import com.hbt.yiqing.entity.YQInfo;
import com.hbt.yiqing.utils.DataUtils_world;
import com.hbt.yiqing.utils.ThreadUtils;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AfricaFragment extends Fragment {

    private String c_name = "非洲";
    RecyclerView recycle;

    public AfricaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_asian, container, false);
        Log.i("6678", c_name);
        recycle = v.findViewById(R.id.recycle);
        initRecycler();
        return v;
    }


    public void initRecycler(){
        List<YQInfo> nations = null;
        while(nations==null){
            nations = DataUtils_world.getNationsByContinents("非洲");
//            System.out.println("nations null");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(AfricaFragment.this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(layoutManager);
        recycle.setAdapter(new RecChinaAdapter(nations));

    }//initRecycler end

}
