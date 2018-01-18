package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelXY extends JPanel implements ActionListener, MouseListener
{
	boolean line_caught;
	int curr_x;
	int curr_y;
	MainPanel mainPanel;
	int maxX;
	int maxY;
	int minX;
	int minY;
	
	public ProjectionPanelXY(MainPanel mainPanel) 
	{
		super();
		setLayout(null);
		setBackground(new Color(123, 123, 123));
		
		maxX = 0;
		minY = 0;
		maxX = this.getWidth();
		minY = this.getHeight();
		
		line_caught = false;
		curr_x = 0;
		curr_y = 0;
		
		this.mainPanel = mainPanel;
		
		addMouseListener(this);
		
		JLabel label = new JLabel("XY");
		label.setBounds(450, 300, 50, 50);
		
		add(label);
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
		super.paintComponent(g);
		
		Point[] targetPointArray = new Point[3];
		int[][] zbuffer = new int[this.getHeight()][this.getWidth()];
		int[][] cbuffer = new int[this.getHeight()][this.getWidth()];
		int i;

		int centerizerX = this.getWidth()/2;
    	int centerizerY = this.getHeight()/2;
		
		for(int j = 0; j< this.getHeight(); j++)
		{
			for(int k = 0; k < this.getWidth(); k++)
			{
				zbuffer[j][k] = 99999;
				cbuffer[j][k] = MainPanel.int2RGB(123,123,123);
			}
		}
		
    	for(Triangle t : MainPanel.triangleList)
    	{
    		int[][] pointList = new int[3][4];
    		pointList[0][0] = t.getV1().getX();
    		pointList[0][1] = t.getV1().getY();
    		pointList[0][2] = t.getV1().getZ();
    		pointList[0][3] = 1;
    		pointList[1][0] = t.getV2().getX();
    		pointList[1][1] = t.getV2().getY();
    		pointList[1][2] = t.getV2().getZ();
    		pointList[1][3] = 1;
    		pointList[2][0] = t.getV3().getX();
    		pointList[2][1] = t.getV3().getY();
    		pointList[2][2] = t.getV3().getZ();
    		pointList[2][3] = 1;
    		
    		i = 0;
    		for(int[] p : pointList)
    		{
    			Point targetPoint = new Point(p[0] + centerizerX, p[1] + centerizerY);
    			checkBounds(targetPoint);
    			targetPointArray[i] = targetPoint;
    			i++;
    		}
    		
    		int z = 99999;
    		for(int iy = 0; iy < this.getHeight(); iy++)
    		{
    			for(int ix = 0; ix < this.getWidth(); ix++)
    			{
    				if(PointInTriangle(new Point(ix,iy), targetPointArray[0], targetPointArray[1], targetPointArray[2]))
    				{
    					z = pointList[0][2];
    					if(pointList[1][2] < z)
    					{
    						z = pointList[1][2];
    					}
    					if(pointList[2][2] < z)
    					{
    						z = pointList[2][2];
    					}
    						
		    			if(zbuffer[iy][ix] > z)
		    			{
		    				linesGoAway(targetPointArray);
		    				
		    				zbuffer[iy][ix] = z;
		    				if(MainPanel.shading == 1)
		    				{
			    				cbuffer[iy][ix] = MainPanel.int2RGB(
			    						gourandShading(new Point(ix,iy), targetPointArray, 
			    								new int[]{MainPanel.getRed(t.getV1().getColor()), 
			    										MainPanel.getRed(t.getV2().getColor()), 
			    										MainPanel.getRed(t.getV3().getColor())}), 
			    						gourandShading(new Point(ix,iy), targetPointArray, 
			    								new int[]{MainPanel.getGreen(t.getV1().getColor()), 
			    										MainPanel.getGreen(t.getV2().getColor()), 
			    										MainPanel.getGreen(t.getV3().getColor())}), 
			    						gourandShading(new Point(ix,iy), targetPointArray, 
			    								new int[]{MainPanel.getBlue(t.getV1().getColor()), 
			    										MainPanel.getBlue(t.getV2().getColor()), 
			    										MainPanel.getBlue(t.getV3().getColor())}));
		    				}
		    				if(MainPanel.shading == 0)
		    				{
			    				cbuffer[iy][ix] = flatShadingColor(new Point(ix,iy), new int[]{t.getV1().getColor(), 
			    						t.getV2().getColor(), 
			    						t.getV3().getColor()});
		    				}
		    			}
    				}
    			}
    		}
    		for(int j = 0; j < this.getHeight(); j++)
    		{
    			for(int k = 0; k < this.getWidth(); k++)
    			{
    				g.setColor(new Color(cbuffer[j][k]));
    				g.drawLine(k, j, k, j);
    			}
    		}
    	}
    }
	
	boolean isWithinBound(int x, int y)
	{
		boolean out = true;
		if(x > maxX || x < minX || y > maxY || y < minY)
		{
			out = false;
		}
		return out;
	}
	
	void linesGoAway(Point[] targetPointArray)
	{
		if(targetPointArray[0].getX() == targetPointArray[1].getX())
		{
			targetPointArray[0].setLocation(new Point((int)targetPointArray[0].getX() + 1, (int)targetPointArray[0].getY()));
		}
		if(targetPointArray[0].getX() == targetPointArray[2].getX())
		{
			targetPointArray[0].setLocation(new Point((int)targetPointArray[0].getX() + 1, (int)targetPointArray[0].getY()));
		}
		if(targetPointArray[2].getX() == targetPointArray[1].getX())
		{
			targetPointArray[1].setLocation(new Point((int)targetPointArray[1].getX() + 1, (int)targetPointArray[1].getY()));
		}
		
		if(targetPointArray[0].getY() == targetPointArray[1].getY())
		{
			targetPointArray[0].setLocation(new Point((int)targetPointArray[0].getX(), (int)targetPointArray[0].getY() + 1));
		}
		if(targetPointArray[0].getY() == targetPointArray[2].getY())
		{
			targetPointArray[0].setLocation(new Point((int)targetPointArray[0].getX(), (int)targetPointArray[0].getY() + 1));
		}
		if(targetPointArray[2].getY() == targetPointArray[1].getY())
		{
			targetPointArray[1].setLocation(new Point((int)targetPointArray[1].getX(), (int)targetPointArray[1].getY() + 1));
		}
		
		if(areColinear(targetPointArray[0], targetPointArray[1], targetPointArray[2]))
		{
			targetPointArray[1].setLocation(new Point((int)targetPointArray[1].getX() + 1, (int)targetPointArray[1].getY()));
		}
	}
	
	boolean areColinear(Point p1, Point p2, Point p3)
	{
		double result1 = (p2.getY() - p1.getY())/(p2.getX() - p1.getX());
		double result2 = (p3.getY() - p2.getY())/(p3.getX() - p2.getX());
		
		return result1 == result2;
	}
	
	float sign (Point p1, Point p2, Point p3)
	{
	    return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}
	
	boolean PointInTriangle (Point pt, Point v1, Point v2, Point v3)
	{
	    boolean b1, b2, b3;

	    b1 = sign(pt, v1, v2) < 0;
	    b2 = sign(pt, v2, v3) < 0;
	    b3 = sign(pt, v3, v1) < 0;

	    return ((b1 == b2) && (b2 == b3));
	}

	public int flatShadingColor(Point p, int[] colorList)
	{
		int color = MainPanel.int2RGB(
				(MainPanel.getRed(colorList[0]) + MainPanel.getRed(colorList[1]) 
				+ MainPanel.getRed(colorList[2]))/3, 
				(MainPanel.getGreen(colorList[0]) + MainPanel.getGreen(colorList[1]) 
				+ MainPanel.getGreen(colorList[2]))/3, 
				(MainPanel.getBlue(colorList[0]) + MainPanel.getBlue(colorList[1]) 
				+ MainPanel.getBlue(colorList[2]))/3);
		return color;
	}
	
	public int gourandShading(Point p, Point[] pointAr, int[] colorAr)
	{
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,0);
		Point p3 = new Point(0,0);
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		
		if(pointAr[0].getY() == pointAr[1].getY())
		{
			p1 = pointAr[2];
			p2 = pointAr[1];
			p3 = pointAr[0];
			return gourandShadingSimple(p, new Point[]{p1, p2, p3}, new int[]{c1,c2,c3});
		}
		else if(pointAr[0].getY() == pointAr[2].getY())
		{
			p1 = pointAr[1];
			p2 = pointAr[2];
			p3 = pointAr[0];
			return gourandShadingSimple(p, new Point[]{p1, p2, p3}, new int[]{c1,c2,c3});
		}
		else if(pointAr[2].getY() == pointAr[1].getY())
		{
			p1 = pointAr[0];
			p2 = pointAr[1];
			p3 = pointAr[2];
			return gourandShadingSimple(p, new Point[]{p1, p2, p3}, new int[]{c1,c2,c3});
		}
		else
		{
			if(pointAr[0].getY() < pointAr[1].getY() && pointAr[0].getY() < pointAr[2].getY())
			{
				p1 = pointAr[0];
				c1 = colorAr[0];
				if(pointAr[1].getY() < pointAr[2].getY())
				{
					p2 = pointAr[1];
					c2 = colorAr[1];
					p3 = pointAr[2];
					c3 = colorAr[2];
				}
				else
				{
					p2 = pointAr[2];
					c2 = colorAr[2];
					p3 = pointAr[1];
					c3 = colorAr[1];
				}
			}
			
			if(pointAr[1].getY() < pointAr[0].getY() && pointAr[1].getY() < pointAr[2].getY())
			{
				p1 = pointAr[1];
				c1 = colorAr[1];
				if(pointAr[2].getY() < pointAr[0].getY())
				{
					p2 = pointAr[2];
					c2 = colorAr[2];
					p3 = pointAr[0];
					c3 = colorAr[0];
				}
				else
				{
					p2 = pointAr[0];
					c2 = colorAr[0];
					p3 = pointAr[2];
					c3 = colorAr[2];
				}
			}
			
			if(pointAr[2].getY() < pointAr[0].getY() && pointAr[2].getY() < pointAr[1].getY())
			{
				p1 = pointAr[2];
				c1 = colorAr[2];
				if(pointAr[1].getY() < pointAr[0].getY())
				{
					p2 = pointAr[1];
					c2 = colorAr[1];
					p3 = pointAr[0];
					c3 = colorAr[0];
				}
				else
				{
					p2 = pointAr[0];
					c2 = colorAr[0];
					p3 = pointAr[1];
					c3 = colorAr[1];
				}
			}
			
			int y = (int) p2.getY();
			int x = (int) (p1.getX() - (p1.getX() - p3.getX()) * (p1.getY() - y)/(p1.getY() - p3.getY()));
			int c4 = (int) (c1 - (c1 - c3) * (p1.getY() - y)/(p1.getY() - p3.getY()));
			
			Point p4 = new Point(x,y);
	
			if(p.getY() > p4.getY())
			{
				return gourandShadingSimple(p, new Point[]{p1, p2, p4}, new int[]{c1,c2,c4});
			}
			else
			{
				return gourandShadingSimple(p, new Point[]{p3, p4, p2}, new int[]{c3,c4,c2});
			}
		}
	}
	
	public int gourandShadingSimple(Point p, Point[] pointAr, int[] colorAr)
	{
		int c1 = colorAr[0];
		int c2 = colorAr[1];
		int c3 = colorAr[2];
		
		double ca = (c1 - (c1-c2) * (pointAr[0].getY() - p.getY())/(pointAr[0].getY() - pointAr[1].getY()));
		double cb = (c1 - (c1-c3) * (pointAr[0].getY() - p.getY())/(pointAr[0].getY() - pointAr[2].getY()));
		
		double xa = (pointAr[0].getX() - (pointAr[0].getX()-pointAr[1].getX()) * (pointAr[0].getY() - p.getY())/(pointAr[0].getY() - pointAr[1].getY()));
		double xb = (pointAr[0].getX() - (pointAr[0].getX()-pointAr[2].getX()) * (pointAr[0].getY() - p.getY())/(pointAr[0].getY() - pointAr[2].getY()));
		return (int) (cb - (cb-ca) * (xb - p.getX())/(xb-xa));
	}
	
	private void checkBounds(Point p)
	{
		if(p.getX() > maxX)
		{
			maxX = (int) p.getX();
		}
		if(p.getX() < minX)
		{
			minX = (int) p.getX();
		}
		if(p.getY() > maxY)
		{
			maxY = (int) p.getX();
		}
		if(p.getY() < minY)
		{
			minY = (int) p.getX();
		}
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
		line_caught = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if(isWithinBound(arg0.getX(), arg0.getY()))
		{
			line_caught = true;
			curr_x = arg0.getX();
			curr_y = arg0.getY();
			System.out.println("caught!");
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(line_caught)
		{
			int x_diff = arg0.getX() - curr_x;
			int y_diff = arg0.getY() - curr_y;
			
			maxX += x_diff;
			minX += x_diff;
			maxY += y_diff;
			minY += y_diff;
			
			for(Vertex v : MainPanel.vertexList)
			{
				v.setX(v.getX() + x_diff);
				v.setY(v.getY() + y_diff);
			}
			
			MainPanel.triangleList.clear();
			for(int i = 0; i < MainPanel.vertexIndicesList.size(); i += 3)
			{
				
				MainPanel.triangleList.add(new Triangle(
						MainPanel.vertexList.get(MainPanel.vertexIndicesList.get(i)),
						MainPanel.vertexList.get(MainPanel.vertexIndicesList.get(i+1)),
						MainPanel.vertexList.get(MainPanel.vertexIndicesList.get(i+2))));
			}
			
			curr_x = arg0.getX();
			curr_y = arg0.getY();
		}
		
		line_caught = false;
		mainPanel.repaintAll();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{}
}
