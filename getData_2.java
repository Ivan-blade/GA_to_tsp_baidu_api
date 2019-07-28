package GA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

 //对无效地点做过滤
public class getData_2 {
 
	private static final String GAODE_AK = "你的ak";
	
	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 */
	public static Map<String, String> getLatitude(String address) {
		try {
			// 将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");
			URL resjson = new URL("https://restapi.amap.com/v3/geocode/geo?address="
					+ address + "&output=JSON&key=" + GAODE_AK);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			//System.out.println(str);
			if(str!=null&&!str.equals("")){
				Map<String, String> map = null;
				int infoStart = str.indexOf("location\":");
				int infoEnd = str.indexOf(",\"level");
				if (infoStart > 0 && infoEnd > 0) {
					String info= str.substring(infoStart + 10, infoEnd);
					//System.out.println(info);
					int lngEnd=info.indexOf(",");
					String lng = info.substring(1, lngEnd);
					String lat = info.substring(lngEnd + 1,info.length()-1);
					map = new HashMap<String, String>();
					map.put("lng", lng);
					map.put("lat", lat);
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//将返回数据改成double类型单位改成千米
	private static double format(String temp) {
		Double one=Double.parseDouble(temp);
		return (one/1000);
	}
	
	//返回两地距离
	@SuppressWarnings("null")
	public static double backDis(Map<String, String> A,Map<String, String> B) {
		try {
			URL resjson = new URL
					("https://restapi.amap.com/v3/direction/driving?&output=JSON&extensions=all&origin="+A.get("lng")+","+A.get("lat")+"&destination="+B.get("lng")+","+B.get("lat")+"&key="+GAODE_AK);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			//System.out.println(str);
			if(str!=null&&!str.equals("")){
				double temp=0;
				int disStart = str.indexOf("distance\":");
				//System.out.println(disStart);
				int disEnd = str.indexOf(",\"duration");
				//System.out.println(disEnd);
				if (disStart > 0  && disEnd > 0) {
					//System.out.println("距离计算开始...");
					String dis = str.substring(disStart + 11, disEnd-1);
					//System.out.println(dis);
					temp=format(dis);
					//System.out.println(temp);
					if(temp<2)
					{
						temp=0;
						return temp;
					}else {
						return temp;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Double) null;
	}

}
