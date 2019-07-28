package GA;
//https://blog.csdn.net/zzq900503/article/details/11555375

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64.Decoder;

 //对无效地点做过滤
public class getData {
 
	private static final String BAIDU_AK = "你的ak";
	
	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 */
	public static Map<String, String> getLatitude(String address) {
		try {
			// 将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");
			URL resjson = new URL("http://api.map.baidu.com/geocoder?address="
					+ address + "&output=json&key=" + BAIDU_AK);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			if(str!=null&&!str.equals("")){
				Map<String, String> map = null;
				int infoStart = str.indexOf("result\":");
				int infoEnd = str.indexOf("]");
				if (infoStart > 0 && infoEnd > 0) {
					String lng = str.substring(infoStart + 8, infoEnd);
					if(lng.equals("["))
					{
						System.out.println(URLDecoder.decode( address, "UTF-8" )+"无法通过api解析！");
					}
				}
			}
			if(str!=null&&!str.equals("")){
				Map<String, String> map = null;
				int lngStart = str.indexOf("lng\":");
				int lngEnd = str.indexOf(",\"lat");
				int latEnd = str.indexOf("},\"precise");
				if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
					String lng = str.substring(lngStart + 5, lngEnd);
					String lat = str.substring(lngEnd + 7, latEnd);
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
	public static double backDis(Map<String, String> A,Map<String, String> B) {
		try {
			URL resjson = new URL
					("http://api.map.baidu.com/routematrix/v2/driving?output=json&origins="+A.get("lat")+","+A.get("lng")+"&destinations="+B.get("lat")+","+B.get("lng")+"&ak="+BAIDU_AK);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					resjson.openStream()));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			if(str!=null&&!str.equals("")){
				double temp=0;
				int disStart = str.indexOf("value\":");
				int disEnd = str.indexOf("},\"duration");
				if (disStart > 0  && disEnd > 0) {
					String dis = str.substring(disStart + 7, disEnd);
					temp=format(dis);
					if(temp<0.2)
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