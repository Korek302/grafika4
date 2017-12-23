package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelXY extends JPanel 
{
	public ProjectionPanelXY() 
	{
		super();
		setBackground(new Color(123, 123, 0));
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
    	for(Triangle t : MainPanel.triangleList)
    	{
    		Point[] pointList = new Point[3];
    		pointList[0] = new Point(t.getV1().getX(), t.getV1().getY());
    		pointList[1] = new Point(t.getV2().getX(), t.getV2().getY());
    		pointList[2] = new Point(t.getV3().getX(), t.getV3().getY());
    		
    		g.drawLine((int)pointList[0].getX(), (int)pointList[0].getY(), 
    				(int)pointList[1].getX(), (int)pointList[1].getY());
    		g.drawLine((int)pointList[1].getX(), (int)pointList[1].getY(), 
    				(int)pointList[2].getX(), (int)pointList[2].getY());
    		g.drawLine((int)pointList[2].getX(), (int)pointList[2].getY(), 
    				(int)pointList[0].getX(), (int)pointList[0].getY());
    	}
    }
}
