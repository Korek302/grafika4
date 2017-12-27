package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelXY extends JPanel 
{
	public ProjectionPanelXY() 
	{
		super();
		setBackground(new Color(123, 123, 123));
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
		super.paintComponent(g);

    	int centerizerX = this.getWidth()/2;
    	int centerizerY = this.getHeight()/2;
		ArrayList<int[]> checker = new ArrayList<int[]>();
		
    	for(Triangle t : MainPanel.triangleList)
    	{
    		Point[] pointList = new Point[3];
    		pointList[0] = new Point(t.getV1().getX() + centerizerX, this.getHeight() - t.getV1().getY() - centerizerY);
    		pointList[1] = new Point(t.getV2().getX() + centerizerX, this.getHeight() - t.getV2().getY() - centerizerY);
    		pointList[2] = new Point(t.getV3().getX() + centerizerX, this.getHeight() - t.getV3().getY() - centerizerY);
    		
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
    	}
    }
}
