package com.hbt.yiqing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hbt.yiqing.R;
import com.hbt.yiqing.entity.YQInfo;

import java.util.List;

public class RecChinaAdapter extends RecyclerView.Adapter {
    List<YQInfo> infos;
    public static int kk = 0;

    public RecChinaAdapter(List<YQInfo> infos){
        this.infos = infos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setLayoutParams(params);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.china_item,layout,true);
        ViewHolder holder = new ViewHolder(view);
//        System.out.println("onCreateViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder hold = (ViewHolder)holder;
        hold.self.setBackgroundColor(position%2==0?0xFFEAF5DD:0xFFDAF0BF);
        YQInfo info = infos.get(position);
        hold.t_name.setText(String.valueOf(info.getName()));
        hold.t_confirm.setText(String.valueOf(info.getConfirm()));
        hold.t_curConfirm.setText(String.valueOf(info.getNowConfirm()));
        hold.t_suspect.setText(String.valueOf(info.getSuspect()));
        hold.t_heal.setText(String.valueOf(info.getHeal()));
        hold.t_dead.setText(String.valueOf(info.getDead()));
    }


    @Override
    public int getItemCount() {
//        System.out.println("size:"+infos.size());
        return infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View self;
        TextView t_name;
        TextView t_confirm;
        TextView t_curConfirm;
        TextView t_suspect;
        TextView t_heal;
        TextView t_dead;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            System.out.println("new holder");
            self = itemView;
            t_name = itemView.findViewById(R.id.name);
            t_confirm = itemView.findViewById(R.id.confirm);
            t_curConfirm = itemView.findViewById(R.id.curConfirm);
            t_suspect = itemView.findViewById(R.id.suspect);
            t_heal = itemView.findViewById(R.id.heal);
            t_dead = itemView.findViewById(R.id.dead);
        }
    }

}
