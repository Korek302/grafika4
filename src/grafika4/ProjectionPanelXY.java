package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelXY extends JPanel implements ActionListener, MouseMotionListener, MouseListener
{
	boolean imgCen_caught;
	boolean obs_caught;
	ArrayList<int[]> checker;
	int curr_x;
	int curr_y;
	MainPanel mainPanel;
	
	int centerizerX ;
	int centerizerY;

	int[] obs;
	int[] imageCenter;
	
	public ProjectionPanelXY(MainPanel mainPanel) 
	{
		super();
		setBackground(new Color(123, 123, 123));
		setLayout(null);
		
		centerizerX = this.getWidth()/2;
    	centerizerY = this.getHeight()/2;
		
		imgCen_caught = false;
		obs_caught = false;
		
		checker = new ArrayList<int[]>();
    	
		this.mainPanel = mainPanel;
		
		this.obs = mainPanel.obs;;
		this.imageCenter = mainPanel.imageCenter;
		
		curr_x = 0;
		curr_y = 0;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		JLabel label = new JLabel("XY");
		label.setBounds(450, 300, 50, 50);
		
		add(label);
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
		super.paintComponent(g);

		centerizerX = this.getWidth()/2;
    	centerizerY = this.getHeight()/2;
    	
		checker.clear();
		
    	for(Triangle t : MainPanel.triangleList)
    	{
    		Point[] pointList = new Point[3];
    		pointList[0] = new Point(t.getV1().getX() + centerizerX, t.getV1().getY() + centerizerY);
    		pointList[1] = new Point(t.getV2().getX() + centerizerX, t.getV2().getY() + centerizerY);
    		pointList[2] = new Point(t.getV3().getX() + centerizerX, t.getV3().getY() + centerizerY);
    		
    		int[] arr1 = {(int)pointList[0].getX(), (int)pointList[0].getY(), 
    				(int)pointList[1].getX(), (int)pointList[1].getY()};
    		int[] arr2 = {(int)pointList[1].getX(), (int)pointList[1].getY(), 
    				(int)pointList[2].getX(), (int)pointList[2].getY()};
    		int[] arr3 = {(int)pointList[2].getX(), (int)pointList[2].getY(), 
    				(int)pointList[0].getX(), (int)pointList[0].getY()};
    		
    		if(!checker.contains(arr1))
    		{
    			g.drawLine((int)pointList[0].getX(), (int)pointList[0].getY(), 
    					(int)pointList[1].getX(), (int)pointList[1].getY());
    			checker.add(arr1);
    		}
    		if(!checker.contains(arr2))
    		{
	    		g.drawLine((int)pointList[1].getX(), (int)pointList[1].getY(), 
	    				(int)pointList[2].getX(), (int)pointList[2].getY());
	    		checker.add(arr2);
    		}
    		if(!checker.contains(arr3))
    		{
	    		g.drawLine((int)pointList[2].getX(), (int)pointList[2].getY(), 
	    				(int)pointList[0].getX(), (int)pointList[0].getY());
	    		checker.add(arr3);
    		}
    		
    		g.drawRect(obs[0] - 10, obs[1] - 10 , 20, 20);
    		g.drawRect(imageCenter[0] - 10, imageCenter[1] - 10, 20, 20);
    		g.drawRect(imageCenter[0] - 100, imageCenter[1] - 100, 200, 200);
    	}
    }
	boolean isWithinPoint(int x, int y, int[] p)
	{
		boolean out = false;
		if(Math.abs(p[0] - x) < 10 && Math.abs(p[1] - y) < 10)
			out = true;
		return out;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		obs_caught = false;
		imgCen_caught = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if(isWithinPoint(arg0.getX(), arg0.getY(), obs))
		{
			obs_caught = true;
			curr_x = arg0.getX();
			curr_y = arg0.getY();
		}
		if(isWithinPoint(arg0.getX(), arg0.getY(), imageCenter))
		{
			imgCen_caught = true;
			curr_x = arg0.getX();
			curr_y = arg0.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		imgCen_caught = false;
		obs_caught = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(obs_caught)
		{
			int x_diff = arg0.getX() - curr_x;
			int y_diff = arg0.getY() - curr_y;
			
			obs[0] = obs[0] + x_diff;
			obs[1] = obs[1] + y_diff;
			
			curr_x = arg0.getX();
			curr_y = arg0.getY();
			
			mainPanel.obs = obs;
		}
		
		if(imgCen_caught)
		{
			int x_diff = arg0.getX() - curr_x;
			int y_diff = arg0.getY() - curr_y;
			
			imageCenter[0] = imageCenter[0] + x_diff;
			imageCenter[1] = imageCenter[1] + y_diff;
			
			curr_x = arg0.getX();
			curr_y = arg0.getY();
			
			mainPanel.imageCenter = imageCenter;
		}
		
		mainPanel.repaintXYandPP();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{}
}
