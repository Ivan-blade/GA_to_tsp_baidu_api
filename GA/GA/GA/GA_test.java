package GA;
//https://blog.csdn.net/xl_1803/article/details/82429857���ӻ��ο�
import GA.Chromosome;
import GA.DynamicDataWindow;

import GA.GA_test;
//ctrl+F11ֱ�����м��ɣ�
public class GA_test extends GA{  
    /***/
	public static final int GENE_LENGTH = dealData.keyInit().length;
	/**�����Ӧ����ֵ���ޣ��ɻ��� ��λ������*/
    
    public GA_test() {  
        super(GENE_LENGTH);    
    }  
      
    @Override  
    public double changeX(Chromosome chro) {    
        return chro.getNum();  
    }  
  
    @Override  
    public double caculateY(double x) {  
    	double y=20000/x;
    	return y;  
    	
    }  
  
    public static void main(String[] args) {
    	dealData.LatInit();
    	dealData.costInit();
        GA_test test = new GA_test(); 
        test.setDdWindow(new DynamicDataWindow("�Ŵ��㷨���Ż�������"));
        test.caculate();  
        //for(int i=0;i<GENE_LENGTH;i++)
        //{
        //	System.out.print(Chromosome.costData[i][0]+" ");
        //	System.out.print(Chromosome.costData[i][1]+" ");
        //	System.out.print(Chromosome.costData[i][2]+" ");
        //	System.out.print(Chromosome.costData[i][3]+" ");
        //	System.out.print(Chromosome.costData[i][4]+" ");
        //	System.out.print(Chromosome.costData[i][5]+" ");
        //	System.out.print(Chromosome.costData[i][6]+" ");
        //	System.out.println(Chromosome.costData[i][7]);
        //}
    }  
}  
