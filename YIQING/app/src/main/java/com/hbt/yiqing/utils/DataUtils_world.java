package com.hbt.yiqing.utils;

import com.hbt.yiqing.entity.YQInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class DataUtils_world {
    public static int t_confirm = 0;
    public static int t_cur_confirm = 0;
    public static int t_heal = 0;
    public static int t_dead = 0;


    public static List<YQInfo> getAll_nations() {
        return all_nations;
    }

    private static List<YQInfo> all_nations = null;
    private static String json = null;

    public static List<YQInfo> getAllNations(){
        if (json == null){
            json = getJsonFromNet();
            all_nations = convertToNation(json);
        }
        return  all_nations;
    }

    static List<YQInfo> convertToNation(String json){
        List<YQInfo> allnation = new LinkedList<>();
        JSONObject obj = JSONObject.fromString(json);
//        if (obj == null){
//            System.out.println("obj == null");
//        }else {
//            System.out.println("obj != null");
//            String s = obj.getString("msg");
//            System.out.println("546466426"+s+"456456546546");
//        }
        JSONArray nations = obj.getJSONArray("newslist");
        for (int i = 0;i < nations.length();i++){
            JSONObject nation = nations.getJSONObject(i);
            String continent = nation.getString("continents");
            String name = nation.getString("provinceName");
            int confirm = nation.getInt("confirmedCount");
            int cur_confirm = nation.getInt("currentConfirmedCount");
            int heal = nation.getInt("curedCount");
            int dead = nation.getInt("deadCount");
            YQInfo info = new YQInfo();
            info.setContinent(continent);
            info.setName(name);
            info.setConfirm(confirm);
            info.setNowConfirm(cur_confirm);
            info.setHeal(heal);
            info.setDead(dead);
            allnation.add(info);

            //统计世界疫情总计
            t_confirm+=confirm;
            t_cur_confirm+=cur_confirm;
            t_heal+=heal;
            t_dead+=dead;
        }

        return allnation;
    }//convertToNation end

    public static List<YQInfo> getNationsByContinents(String continent){
        List<YQInfo> allnation = null;
        while(allnation == null){
            allnation = getAll_nations();
        }
        List<YQInfo> state_nations = new LinkedList<>();
        for (YQInfo nation : allnation){
            if(continent.equals(nation.getContinent())){
                state_nations.add(nation);
            }
        }
        return  state_nations;
    }//getNationsByContinents end


    static String getJsonFromNet(){
        try {
            BufferedReader reader = null;
            String result = null;
            StringBuffer sbf = new StringBuffer();
            URL url = new URL("http://api.tianapi.com/txapi/ncovabroad/index?key=47428373ea52ec42fd0b2372e0ab78a1");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
//            result = sbf.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
            result = sbf.toString();
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }//getJsonFromNet end
}
