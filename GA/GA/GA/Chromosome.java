package GA;

import java.util.Random;
import java.util.ArrayList;  
import java.util.List;  
  
public class Chromosome { 
    public int[] gene=new int[dealData.keyInit().length];//基因序列  
    private double score;//对应的函数得分  
    public static double[][] costData=new double[dealData.keyInit().length][dealData.keyInit().length];
    public double getScore() {  
        return score;  
    }  
  
    public void setScore(double score) {  
        this.score = score;  
    }  
  
     /* 生成一个新染色体 
     */  
    public Chromosome() {  
    	//基因序列的值，0-x表示x个不同的地点，染色体初始化即将这十个值不重复的放进数组
    	int[] a = new int[dealData.keyInit().length];
    	for(int i=0;i<dealData.keyInit().length;i++)
    	{
    		a[i]=i;
    	}
    	Random r=new Random();
    	int max=a.length;
    	for (int i = 0; i < a.length; i++) {  
            int index=r.nextInt(max);
    		gene[i] = a[index];
    		a[index]=a[--max];
        } 
    }  
      
    /** 
     * @param c 
     * @return 
     * @Description: 克隆基因 
     */  
    public static Chromosome clone(final Chromosome c) {  
        if (c == null || c.gene == null) {  
            return null;  
        }  
        Chromosome copy = new Chromosome();  
        for (int i = 0; i < c.gene.length; i++) {  
            copy.gene[i] = c.gene[i];  
        }  
        return copy;  
    }  
            
    /** 
     * @param c1 
     * @param c2 
     * @Description: 遗传产生下一代 
     */  
    //染色体交配功能
    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {  
        if (p1 == null || p2 == null) { //染色体有一个为空，不产生下一代  
            return null;  
        }  
        if (p1.gene == null || p2.gene == null) { //染色体有一个没有基因序列，不产生下一代  
            return null;  
        }  
        if (p1.gene.length != p2.gene.length) { //染色体基因序列长度不同，不产生下一代  
            return null;  
        }  
        Chromosome c1 = clone(p1);  
        Chromosome c2 = clone(p2);  
        //随机产生交叉互换位置区间  
        int size = c1.gene.length;  
        int a = ((int) (Math.random() * size)) % size;  
        int b = ((int) (Math.random() * size)) % size;  
        int min = a > b ? b : a;  
        int max = a > b ? a : b;  
   
        int[] temp_1=new int[c1.gene.length];
        int[] temp_2=new int[max-min+1];
        int[] temp_3=new int[c1.gene.length-max+min-1];
        int[] temp_4=c1.gene;
        int n_2=0;
        int n_3=0;
        //temp_4复制c1的基因序列，在产生子代染色体之前减少对父代染色体的改变，后续修改都在temp_4上进行，最后直接赋值给c1染色体
        //temp_1即即将产生的交配结果，temp_2即保存c2交换片段的数组
        //将c2的交换片段保存在temp_1中
        for (int i = min; i <= max; i++) {
        	temp_1[i] = c2.gene[i];  
        	temp_2[n_2]=c2.gene[i];
        	n_2++;
        }  
        //将temp_4的基因序列中值与交换片段相同的转换成-1
        for(int i=0;i<c1.gene.length;i++)
        {
        	for(int j=min;j<=max;j++)
        	{
        		if(temp_4[i]==c2.gene[j])
        		{
        			temp_4[i]=-1;
        		}
        	}
        }
        //将temp_4中不为-1的基因传入temp_3数组
        for(int i=0;i<c1.gene.length;i++)
        {
        	if(temp_4[i]!=-1)
        	{
        		temp_3[n_3++]=temp_4[i];
        	}
        }
        //将于交换片段不同的基因逆序赋值给temp_1,min前一段和max后一段
        for(int i=0;i<min;i++)
        {
        	temp_1[i]=temp_3[--n_3];//先自减再充当索引
        }
        for(int i=max+1;i<c1.gene.length;i++)
        {
        	temp_1[i]=temp_3[--n_3];
        }
        c1.gene=temp_1;
        
        
        
        List<Chromosome> list = new ArrayList<Chromosome>();  
        list.add(c1);    
        return list;  
    }  
      
    /** 
     * @param num 
     * @Description: 基因num个位置发生变异 
     */  
    public void mutation(int num) {  
        //允许变异  
        int size = gene.length;  
        for (int i = 0; i < num; i++) {  
            //寻找变异位置  
            int at1 = ((int) (Math.random() * size)) % size;  
            int at2 = ((int) (Math.random() * size)) % size;  
            //变异后的值  
            //交换变异位置基因
            int temp=gene[at1];
            gene[at1]=gene[at2];
            gene[at2]=temp;
        }  
    }  
      
    /** 
     * @return 
     * @Description: 将基因转化为对应的数字 
     */  
    //将序列转化成路径长例如[0][1][2][3][4][5][6][7][8][9]，即0-1-2-3------9-0这条路径，通过前面的数组实现转换
    public double getNum() {  
        if (gene == null) {  
            return 0; 
        }
        int sum=0;
        int x_temp;
        int y_temp;
        for(int i=0;i<gene.length;i++)
        {
        	x_temp=gene[i];
        	y_temp=gene[(i+1)%(gene.length)];
        	sum+=costData[x_temp][y_temp];
        }
        return sum;
    }
}  
