package com.hbt.yiqing.utils;

import com.hbt.yiqing.activity.MainActivity;
import com.hbt.yiqing.entity.YQInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataUtils {

    private static YQInfo my_info = null;

    public static YQInfo getChina(){
        if (my_info == null){
            String json = getJsonFromNet();
            YQInfo china = convertToChina(json);
            convertToCity(json, china);
            my_info = china;
            return my_info;
        }else return  my_info;

    }


    public static YQInfo getProvinceByName(String province_name){
        List<YQInfo> provinces = getChina().getChildren();
        for(YQInfo province:provinces){
            if(province.getName().equals(province_name)){
                return province;
            }
        }
        return null;
    }

    public static int getColorByProvinceName(String province_name, int type){
        if (my_info == null){
            getChina();
        }
        List<YQInfo> provinces = my_info.getChildren();
        for(YQInfo province:provinces){
            System.out.println(province_name+" "+province.getName());
            if (province.getName().equals(province_name)){
                return type == MainActivity.TOTALCHANGE?getColorByInt(province.getConfirm()):getColorByInt(province.getNowConfirm());
            }
        }
        return 1;
    }

    static int getColorByInt(int num){
        if(num > 10000){
            return 0xFF500606;
        }else if(num > 1000){
            return 0xFFC01818;
        }else if(num > 100){
            return 0xFFE65151;
        }else if (num > 10){
            return 0xFFE78585;
        }else if (num > 0){
            return 0xFFECE6D3;
        }else{
            return 0xFFF7F5F3;
        }
    }

    static List<YQInfo> convertToCity(String json, YQInfo parent){
        //获取中国数据
        JSONObject china = (JSONObject)JSONObject.fromString(json).getJSONObject("data").getJSONArray("areaTree").get(0);
        //获取中国的省市
        JSONArray provinces = china.getJSONArray("children");
//        System.out.println(provinces.length());
        //遍历各省
        for(int i = 0; i < provinces.length(); i++) {
            JSONObject province = provinces.getJSONObject(i);

                //显示省的总计数据
                JSONObject total = province.getJSONObject("total");
                String p_name = province.getString("name");
                int p_confirm = total.getInt("confirm");
                int p_cur_confirm = total.getInt("nowConfirm");
                int p_dead = total.getInt("dead");
                int p_heal = total.getInt("heal");
                int p_suspect = total.getInt("suspect");
                YQInfo info = new YQInfo();
                info.setName(p_name);
                info.setConfirm(p_confirm);
                info.setNowConfirm(p_cur_confirm);
                info.setDead(p_dead);
                info.setHeal(p_heal);
                info.setSuspect(p_suspect);
                parent.getChildren().add(info);

//                System.out.println(province_str+": "+"累积确诊："+p_confirm + " 现存确诊："+p_cur_confirm);
                //显示省内各市数据
                JSONArray cities = province.getJSONArray("children");
                for (int j = 0; j < cities.length(); j++) {
                    JSONObject city = cities.getJSONObject(j);
                    JSONObject city_total = city.getJSONObject("total");
                    String c_name = city.getString("name");
                    int c_confirm = city_total.getInt("confirm");
                    int c_cur_confirm = city_total.getInt("nowConfirm");
                    int c_dead = city_total.getInt("dead");
                    int c_heal = city_total.getInt("heal");
                    int c_suspect = city_total.getInt("suspect");
                    YQInfo c_info = new YQInfo();
                    c_info.setName(c_name);
                    c_info.setConfirm(c_confirm);
                    c_info.setNowConfirm(c_cur_confirm);
                    c_info.setDead(c_dead);
                    c_info.setHeal(c_heal);
                    c_info.setSuspect(c_suspect);
                    info.getChildren().add(c_info);
//                    System.out.println("城市： "+city_name+" 累积确诊： "+c_confirm+" 现存确诊： "+c__cur_confirm);
                }


        }
        return null;
    }//convertToCity end

    //转化为中国
    static YQInfo convertToChina(String json){
        //获取中国数据
        JSONObject china = (JSONObject)JSONObject.fromString(json).getJSONObject("data").getJSONArray("areaTree").get(0);
        //获取中国累积和现存
        JSONObject china_obj = china.getJSONObject("total");
        String name = china.getString("name");
        int confirm = china_obj.getInt("confirm");
        int cur_confirm = china_obj.getInt("nowConfirm");
        int dead = china_obj.getInt("dead");
        int heal = china_obj.getInt("heal");
        int suspect = china_obj.getInt("suspect");
        YQInfo info = new YQInfo();
        info.setName(name);
        info.setConfirm(confirm);
        info.setNowConfirm(cur_confirm);
        info.setDead(dead);
        info.setHeal(heal);
        info.setSuspect(suspect);
//        System.out.println(confirm+" "+cur_confirm);
        return info;
    }

    static String getJsonFromNet(){
        try {
            BufferedReader reader = null;
            String result = null;
            StringBuffer sbf = new StringBuffer();
            URL url = new URL("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5");
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
            result = sbf.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
//            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }//getJsonFromNet end









}//class end
