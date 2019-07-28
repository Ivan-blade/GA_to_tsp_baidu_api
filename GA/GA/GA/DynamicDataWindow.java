
package GA;

/* DataWindow.java */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 动态绘制数据曲线
 *http://bookshadow.com/weblog/2014/05/16/java-swing-line-chart/
 * @author tyhj_sf@163.com
 */
public class DynamicDataWindow extends JFrame {

	private static final long serialVersionUID = 3158195564992240333L;
	private static final int MAX_SAMPLES = 100000;
	private double yMax = 0.0;
	private double yMin = Double.MAX_VALUE;
    private int index = 0;
    private long[] time = new long[MAX_SAMPLES];
    private double[] val = new double[MAX_SAMPLES];
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataPanel;
    private javax.swing.JPanel coordinatePanel;
    private javax.swing.JTextArea dataTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
   
    DateFormat fmt = DateFormat.getDateTimeInstance();
    
    private int left;
    private int top;
    private int right;
    private int bottom;

    private int y0;   //y轴起点位置
    private int yn;//y轴末端位置
    private int x0;//x轴起点位置
    private int xn;//x轴末端位置
    
    private double eqdiviY;//设置Y等分量
    private double vscale; 
    
    private int tickX;//标度间隔像素
    private int timescale;//以秒为基准的时间倍率
    private double timeMargin;//标度间隔
    private double tscale;// 每一个像素代表的毫秒数
    private int temp;//

    /** Creates new form DataWindow */
    public DynamicDataWindow() {
        initComponents();
        initDisplay();
    }

    public DynamicDataWindow(String ieee) {
        initComponents();
        setTitle(ieee);
        initDisplay();
    }
    
    private void initDisplay() {
		left = dataPanel.getX() + 10; // get size of pane
		top = dataPanel.getY() + 30;
		right = left + dataPanel.getWidth() - 20;
		bottom = top + dataPanel.getHeight() - 20;

		y0 = bottom - 40; // y轴起点位置
		yn = top + 40;// y轴末端位置
		x0 = left + 50;// x轴起点位置
		xn = right; // x轴末端位置

		eqdiviY = 125.0;// 设置Y等分量
		vscale = (yn - y0) / eqdiviY;

		tickX = 25;// 标度间隔像素
		timescale = 1;
		//timeMargin = timescale*1000.0;// 标度间隔为1秒
		timeMargin = timescale*100.0;// 标度间隔为100毫秒
		tscale = 1.0 / (timeMargin / tickX); // 每一个像素代表的毫秒数
	}
    //设置y轴标度范围
    public void setyMaxMin(double y) {
    	if (y>this.yMax) {
    		this.yMax = y*1.1;
		}
    	if (y<this.yMin) {
    		this.yMin = y*0.9;
		}
	}

    //向坐标系添加数据并刷新曲线
    public void addData(long t, double v) {
    	setyMaxMin(v);
        time[index] = t;
        val[index++] = v;
        dataTextArea.append(fmt.format(new Date(t)) + "    value = " + v + "\n");
        dataTextArea.setCaretPosition(dataTextArea.getText().length());
        repaint();
    }

    
    // Graph the sensor values in the dataPanel JPanel
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        left = dataPanel.getX() + 10; // get size of pane
		top = dataPanel.getY() + 30;
		right = left + dataPanel.getWidth() - 20;
		bottom = top + dataPanel.getHeight() - 20;

		y0 = bottom - 40; // y轴起点位置
		yn = top + 40;// y轴末端位置
		x0 = left + 50;// x轴起点位置
		xn = right; // x轴末端位置

		eqdiviY = 125.0;// 设置Y等分量
		vscale = (yn - y0) / eqdiviY;

//		tickX = 25;// 标度间隔像素
		timeMargin = timescale*1000;// 标度间隔为1秒
		tscale = 1.0 / (timeMargin / tickX); // 每一个像素代表的毫秒数
		
        // 绘制x轴，为时间轴
        g.setColor(Color.WHITE);
        g.drawLine(x0, yn, x0, y0);//绘制无标度的Y轴
        g.drawLine(x0, y0, xn, y0);//绘制无标度的X轴
        for (int xt = x0 + tickX; xt < xn; xt += tickX) {  
            g.drawLine(xt, y0 + 5, xt, y0 - 5);//绘制标度
            int time = (xt - x0) / tickX;//计算每个标度的时间
            g.drawString(Integer.toString(timescale*time), xt - (time < 10 ? 3 : 7) , y0 + 20);//绘制每个标度对应数值
            if (xt >= xn-tickX) {
            	g.drawString("时间（s）", xt - 25 , y0 + 40);//绘制轴名
			}
        }

        // 绘制y轴，为数据轴
        g.setColor(Color.WHITE);
        double tickY=20;//标度间隔
        for (double vt = 0; vt <= eqdiviY; vt += tickY) {    
            int v = y0 + (int)(vt * vscale);//
            g.drawLine(x0 - 5, v, x0 + xn, v);//绘制标度
            g.drawString(Float.toString((float)(yMin+(vt/eqdiviY)*(yMax-yMin))), x0 - 38 , v + 5);//绘制每个标度对应数值
            if (vt >= 120) {
            	g.drawString("数据", x0 - 30 , v-15);//绘制轴名
			}
        }

        // 绘制数据曲线
        g.setColor(Color.BLUE);
        int xp = -1;//前一个坐标值的x;
        int vp = -1;//前一个坐标值的y;
        for (int i = 0; i < index; i++) {
            int x = x0 + (int)((time[i] - time[0]) * tscale);
            int v = y0 + (int)(((val[i]-yMin)/(yMax-yMin)) *(yn-y0));
            if (xp > 0) {//时间不为负
                g.drawLine(xp, vp, x, v);//绘制当前折线
            }
            xp = x;
            vp = v;
        }
        g.drawString(Double.toString(val[(index-1)<0? 0 : (index-1)]),xp-5 , vp-5);//标记最后一个数据值
        //调整x轴标度间距
        if (xp>=xn) {
        	timescale*=2;
        	temp=timescale/100;
        	while(temp>0){
        		tickX+=9;
        		temp/=10;
        	}
		}
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // //GEN-BEGIN:initComponents
    private void initComponents() {

        dataPanel = new javax.swing.JPanel();
//        coordinatePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTextArea = new javax.swing.JTextArea();

        dataPanel.setBackground(new java.awt.Color(0, 0, 0, 220));
        dataPanel.setMinimumSize(new java.awt.Dimension(400, 250));
        dataPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().add(dataPanel, java.awt.BorderLayout.CENTER);      
        
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(400, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 100));

        dataTextArea.setColumns(20);
        dataTextArea.setEditable(false);
        dataTextArea.setRows(4);
        jScrollPane1.setViewportView(dataTextArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.SOUTH);

        pack();
    }// //GEN-END:initComponents


}