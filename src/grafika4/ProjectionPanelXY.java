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
	boolean line_caught;
	ArrayList<int[]> checker;
	int curr_x;
	int curr_y;
	MainPanel mainPanel;
	
	int centerizerX;
	int centerizerY;
	
	public ProjectionPanelXY(MainPanel mainPanel) 
	{
		super();
		setBackground(new Color(123, 123, 123));
		setLayout(null);
		line_caught = false;
		checker = new ArrayList<int[]>();
		curr_x = 0;
		curr_y = 0;
		
		centerizerX = this.getWidth()/2;
    	centerizerY = this.getHeight()/2;
    	
		this.mainPanel = mainPanel;
		
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
    	}
    }
	boolean isWithinStripe(int x, int y, int[] p)
	{
		double a = 0;
		a = (((p[0] - p[2]) == 0) ? (p[1] - p[3]) / 0.0001 : (p[1] - p[3])/(p[0] - p[2]));
		
		double b = p[1] - a * p[0];
		double c = (double)y + a * (double)x;
		double yBothLine = ( b + c ) / 2;
		double xBothLine = ( yBothLine - b ) / 2;

		if(p[0] > p[2])
		{
			if(x > p[0])
			{
				return false;
			}
			if(x < p[2])
			{
				return false;
			}
		}
		else
		{
			if(x < p[0])
			{
				return false;
			}
			if(x > p[2])
			{
				return false;
			}
		}
		
		double d = Math.sqrt((yBothLine - y)*(yBothLine - y) + (xBothLine - x)*(xBothLine - x));
		
		if(d > 20)
		{
			return false;
		}
		
		return true;
	}
	/*boolean isWithinStripe(int x, int y, int[] p)
	{
		double a = 0;
		a = ((((double)p[0] - (double)p[2]) == 0) ? ((double)p[1] - (double)p[3]) / 0.001 : ((double)p[1] - (double)p[3])/((double)p[0] - (double)p[2]));
		//System.out.println(a);
		double b = (double)p[1] - a * (double)p[0];
		//System.out.println(b);
		double c = (double)y + a * (double)x;
		double yBothLine = ( b + c ) / 2;
		double xBothLine = ( yBothLine - b ) / 2;

		if(p[0] > p[2])
		{
			if(x > p[0])
			{
				return false;
			}
			if(x < p[2])
			{
				return false;
			}
		}
		else
		{
			if(x < p[0])
			{
				return false;
			}
			if(x > p[2])
			{
				return false;
			}
		}
		
		double d = Math.sqrt((yBothLine - (double)y)*(yBothLine - (double)y) + (xBothLine - (double)x)*(xBothLine - (double)x));
		//System.out.println(d);
		if(d > 20)
		{
			return false;
		}
		
		return true;
		
		if(p[0] > p[2])
		{
			if(x > p[0])
			{
				return false;
			}
			if(x < p[2])
			{
				return false;
			}
		}
		else
		{
			if(x < p[0])
			{
				return false;
			}
			if(x > p[2])
			{
				return false;
			}
		}
		
		if(p[1] > p[3])
		{
			if(y > p[1])
			{
				return false;
			}
			if(y < p[3])
			{
				return false;
			}
		}
		else
		{
			if(y < p[1])
			{
				return false;
			}
			if(y > p[3])
			{
				return false;
			}
		}
		
		
		// if AC is horizontal
		if (p[0] == p[2]) return x == p[0];
		// if AC is vertical.
		if (p[1] == p[3]) return y == p[1];
		// match the gradients
		return (p[0] - p[2])*(p[1] - p[3]) == (p[2] - x)*(p[3] - y);
		
		
		int dxc = x - p[0];
		int dyc = y - p[1];

		int dxl = p[2] - p[0];
		int dyl = p[3] - p[1];

		int cross = dxc * dyl - dyc * dxl;
		
		if (Math.abs(cross) != 0 )
			  return false;
		
		if (Math.abs(dxl) >= Math.abs(dyl))
			  return dxl > 0 ? 
			    p[0] <= x && x <= p[2] :
			    p[2] <= x && x <= p[0];
			else
			  return dyl > 0 ? 
			    p[1] <= y && y <= p[3] :
			    p[3] <= y && y <= p[1];
	}*/

	
	public boolean contains(int x, int y, ArrayList<int[]> points) 
	{
	      int i;
	      int j;
	      boolean result = false;
	      for (i = 0, j = points.size() - 1; i < points.size(); j = i++) 
	      {
	        if ((points.get(i)[1] > y) != (points.get(j)[1] > y) &&
	            (x < (points.get(j)[0] - points.get(i)[0]) * (y - points.get(i)[1]) / (points.get(j)[1]-points.get(i)[1]) 
	            		+ points.get(i)[0])) 
	        {
	          result = !result;
	        }
	      }
	      return result;
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
		//System.out.println("clicked X: " + arg0.getX());
		//System.out.println("clicked Y: " + (this.getHeight() - arg0.getY()));
		
		/*if(contains(arg0.getX(), arg0.getY(), checker))
		{
			line_caught = true;
			curr_x = arg0.getX();
			curr_y = arg0.getY();
		}*/
		
		for(int[] p : checker)
		{
			if(isWithinStripe(arg0.getX(), this.getHeight() - arg0.getY(), p))
			{
				line_caught = true;
				curr_x = arg0.getX();
				curr_y = arg0.getY();
				
				/*System.out.println("Line x1: " + p[0]);
				System.out.println("Line y1: " + p[1]);
				System.out.println("Line x2: " + p[2]);
				System.out.println("Line y2: " + p[3]);*/
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		line_caught = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(line_caught)
		{
			int x_diff = arg0.getX() - curr_x;
			int y_diff = arg0.getY() - curr_y;
			
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
		
		mainPanel.repaintAll();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{}
}
