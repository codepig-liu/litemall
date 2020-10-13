package org.linlinjava.litemall.db.util;

/**
 * 功能描述：
 *
 * @ClassName: LatitudeUtils
 * @Description: TODO
 * @Author: Administrator
 * @Date: 2020/10/10 16:16
 * @Version: 1.0.0
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;



public class LatitudeUtils {

    public static final String KEY_1 = "7zQ2pTVOh6GWTqfhtalYNphu1MTYPYSQ";
    // 7zQ2pTVOh6GWTqfhtalYNphu1MTYPYSQ
    // 7d9fbeb43e975cd1e9477a7e5d5e192a
    // String key2 = "8ec6cd98bd56554407a207d597c0f3e5";
    // String key3 = "be025dc280e1e3f7ffb95fe42a01fab2";
    // String key4 = "327db7009617d6806b9c38e819ea06ac";

    /**
     * 返回输入地址的经纬度坐标
     * key lng(经度),lat(纬度)
     */
    public static Map<String,String> getGeocoderLatitude(String address){
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
//       如果有代理，要设置代理，没代理可注释
//      System.setProperty("http.proxyHost","192.168.1.188");
//      System.setProperty("http.proxyPort","3128");
        URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+ KEY_1);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while((res = in.readLine())!=null){
                sb.append(res.trim());
            }
            String str = sb.toString();
            Map<String,String> map = null;
            if(!StringUtils.isEmpty(str)){
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if(lngStart > 0 && lngEnd > 0 && latEnd > 0){
                    String lng = str.substring(lngStart+5, lngEnd);
                    String lat = str.substring(lngEnd+7, latEnd);
                    map = new HashMap<String,String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    public static void main(String args[]){
//        Map<String, String> json = LatitudeUtils.getGeocoderLatitude("北京市朝阳区管庄西里76号楼");
//        System.out.println("lng : "+json.get("lng"));
//        System.out.println("lat : "+json.get("lat"));
//
//
//    }

}
