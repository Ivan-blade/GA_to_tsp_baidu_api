package GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class dealData{
	public static void costInit(){
		for(int i=0;i<keyInit().length;i++)
		{
			for(int j=0;j<keyInit().length;j++)
			{
				Chromosome.costData[i][j]=getData.backDis(locLatitudes.get(i), locLatitudes.get(j));
			}
		}
	}
	
	public static List<Map<String, String>> locLatitudes=new ArrayList<Map<String,String>>();
	
	public static void LatInit() {
		for(int i=0;i<keyInit().length;i++)
		{
			locLatitudes.add(getData.getLatitude(keyInit()[i]));
		}
	} 
	
	
	public static String[] keyInit() {
		String[] location={"����","��³ľ��","����","�人","����","�Ͼ��ʵ��ѧ����У��","��"};
		return location;
	}
}