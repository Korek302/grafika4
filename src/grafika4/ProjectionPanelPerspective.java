package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelPerspective extends JPanel
{
	MainPanel mainPanel;
	
	int[] obs;
	int[] imageCenter;
	
	public ProjectionPanelPerspective(MainPanel mainPanel) 
	{
		super();
		
		this.mainPanel = mainPanel;
		
		setBackground(new Color(123, 123, 123));
		
		this.obs = mainPanel.obs;
		this.imageCenter = mainPanel.imageCenter;
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
		super.paintComponent(g);
		
		this.obs = mainPanel.obs;
		this.imageCenter = mainPanel.imageCenter;
		
		Point[] targetPointArray = new Point[3];
		int[][] pointsAfterTrans = new int[3][3];
		int[][] zbuffer = new int[this.getHeight()][this.getWidth()];
		int[][] cbuffer = new int[this.getHeight()][this.getWidth()];
		
		int x;
		int y;
		
		int i;
		double d = MainPanel.dist2p(obs, imageCenter);
		
    	int centerizerX = this.getWidth()/2 + imageCenter[0];
    	int centerizerY = this.getHeight()/2 + imageCenter[1];
		
		double[][] transMatrix = transMatrix(obs, imageCenter);
		
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
    			double[][] tempP = pToCol(p);
    			
    			tempP = matrixMul(transMatrix, tempP);
    			
    			x = (int) (tempP[0][0] * (d/(d + tempP[2][0])));
    			y = (int) (tempP[1][0] * (d/(d + tempP[2][0])));
    			
    			
    			Point targetPoint = new Point(x + centerizerX, y + centerizerY);
    			
    			//System.out.println(targetPoint.getX() + ", " + (targetPoint.getY()));
    			pointsAfterTrans[i] = new int[]{(int)tempP[0][0], (int)tempP[1][0], (int)tempP[2][0]};
    			targetPointArray[i] = targetPoint;
    			i++;
    		}
    		
    		int maxX = (int) Math.max(targetPointArray[0].getX(), 
    				Math.max(targetPointArray[1].getX(), targetPointArray[2].getX()));
    		int minX = (int) Math.min(targetPointArray[0].getX(), 
    				Math.min(targetPointArray[1].getX(), targetPointArray[2].getX()));
    		
    		int maxY = (int) Math.max(targetPointArray[0].getY(), 
    				Math.max(targetPointArray[1].getY(), targetPointArray[2].getY()));
    		int minY = (int) Math.min(targetPointArray[0].getY(), 
    				Math.min(targetPointArray[1].getY(), targetPointArray[2].getY()));
    		
    		maxX = maxX > this.getWidth() - 1 ? this.getWidth() - 1 : maxX;
    		minX = minX < 0 ? 0 : minX;
    		maxY = maxY > this.getHeight() - 1 ? this.getHeight() - 1 : maxY;
    		minY = minY < 0 ? 0 : minY;
    		
    		int z = 99999;
    		for(int iy = minY; iy < maxY + 1; iy++)
    		{
    			for(int ix = minX; ix < maxX + 1; ix++)
    			{
    				if(PointInTriangle(new Point(ix,iy), targetPointArray[0], targetPointArray[1], targetPointArray[2]))
    				{
    					z = pointsAfterTrans[0][2];
    					if(pointsAfterTrans[1][2] < z)
    					{
    						z = pointsAfterTrans[1][2];
    					}
    					if(pointsAfterTrans[2][2] < z)
    					{
    						z = pointsAfterTrans[2][2];
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
    		for(int j = minY; j < maxY + 1; j++)
    		{
    			for(int k = minX; k < maxX + 1; k++)
    			{
    				g.setColor(new Color(cbuffer[j][k]));
    				g.drawLine(k, j, k, j);
    			}
    		}
    	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
    			
		/*Triangle t_test = new Triangle(
				new Vertex(0, 0, 0, MainPanel.int2RGB(0, 0, 255)),
				new Vertex(0, 0, 0, MainPanel.int2RGB(255, 0, 0)), 
				new Vertex(0, 0, 0, MainPanel.int2RGB(0, 255, 0)));
		Point[] targetPointArray = new Point[]{
				new Point(50, 50), 
				new Point(100,100), 
				new Point(200,200)};
		for(int iy = 0; iy < this.getHeight(); iy++)
		{
			for(int ix = 0; ix < this.getWidth(); ix++)
			{
				linesGoAway(targetPointArray);
				
				if(PointInTriangle(new Point(ix,iy), targetPointArray[0], targetPointArray[1], targetPointArray[2]))
				{
    				cbuffer[iy][ix] = MainPanel.int2RGB(
						gourandShadingSimple(new Point(ix,iy), targetPointArray, 
								new int[]{MainPanel.getRed(t_test.getV1().getColor()), 
										MainPanel.getRed(t_test.getV2().getColor()), 
										MainPanel.getRed(t_test.getV3().getColor())}), 
						gourandShadingSimple(new Point(ix,iy), targetPointArray, 
								new int[]{MainPanel.getGreen(t_test.getV1().getColor()), 
										MainPanel.getGreen(t_test.getV2().getColor()), 
										MainPanel.getGreen(t_test.getV3().getColor())}), 
						gourandShadingSimple(new Point(ix,iy), targetPointArray, 
								new int[]{MainPanel.getBlue(t_test.getV1().getColor()), 
										MainPanel.getBlue(t_test.getV2().getColor()), 
										MainPanel.getBlue(t_test.getV3().getColor())}));
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
		}*/
		
    	
    	
    }
	
	public int[] resize(int sx, int sy, int sz, int[] point)
	{
		double[][] transMatrix = new double[4][4];
		double[][] tempPoint = new double[1][4];
		double[][] newPointTemp = new double[1][4];
		int[] newPoint = new int[4];
		
		for(int i = 0; i < point.length; i++)
		{
			tempPoint[0][i] = point[i];
		}
		
		for(int i = 0; i < transMatrix[0].length; i++)
		{
			for(int j = 0; j < transMatrix.length; j++)
			{
				transMatrix[i][j] = 0;
			}
		}
		transMatrix[0][0] = sx;
		transMatrix[1][1] = sy;
		transMatrix[2][2] = sz;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(tempPoint, transMatrix);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[0][i];
		}
		
		return newPoint;
	}
	
	public int[] translation(double tx, double ty, double tz, int[] point)
	{
		double[][] transMatrix = new double[4][4];
		double[][] tempPoint = new double[4][1];
		double[][] newPointTemp = new double[4][1];
		int[] newPoint = new int[4];
		
		for(int i = 0; i < point.length; i++)
		{
			tempPoint[i][0] = (double)point[i];
		}
		
		for(int i = 0; i < transMatrix[0].length; i++)
		{
			for(int j = 0; j < transMatrix.length; j++)
			{
				transMatrix[i][j] = 0;
			}
		}
		transMatrix[0][0] = 1;
		transMatrix[1][1] = 1;
		transMatrix[2][2] = 1;
		transMatrix[3][3] = 1;
		transMatrix[0][3] = tx;
		transMatrix[1][3] = ty;
		transMatrix[2][3] = tz;
		
		newPointTemp = matrixMul(transMatrix, tempPoint);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int) newPointTemp[i][0];
		}
		
		return newPoint;
	}
	
	public int[] rotateOZ(double angle, int[] point)
	{
		double[][] transMatrix = new double[4][4];
		double[][] tempPoint = new double[4][1];
		double[][] newPointTemp = new double[4][1];
		int[] newPoint = new int[4];
		
		for(int i = 0; i < point.length; i++)
		{
			tempPoint[i][0] = point[i];
		}
		
		for(int i = 0; i < transMatrix[0].length; i++)
		{
			for(int j = 0; j < transMatrix.length; j++)
			{
				transMatrix[i][j] = 0;
			}
		}
		transMatrix[0][0] = Math.cos(angle);
		transMatrix[1][1] = Math.cos(angle);
		transMatrix[0][1] = Math.sin(angle);
		transMatrix[1][0] = -Math.sin(angle);
		transMatrix[2][2] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(transMatrix, tempPoint);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[i][0];
		}
		
		return newPoint;
	}
	
	public int[] rotateOY(double angle, int[] point)
	{
		double[][] transMatrix = new double[4][4];
		double[][] tempPoint = new double[4][1];
		double[][] newPointTemp = new double[4][1];
		int[] newPoint = new int[4];
		
		for(int i = 0; i < point.length; i++)
		{
			tempPoint[i][0] = point[i];
		}
		
		for(int i = 0; i < transMatrix[0].length; i++)
		{
			for(int j = 0; j < transMatrix.length; j++)
			{
				transMatrix[i][j] = 0;
			}
		}
		transMatrix[0][0] = Math.cos(angle);
		transMatrix[2][2] = Math.cos(angle);
		transMatrix[0][2] = -Math.sin(angle);
		transMatrix[2][0] = Math.sin(angle);
		transMatrix[1][1] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(transMatrix, tempPoint);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[i][0];
		}
		
		return newPoint;
	}
	
	public int[] rotateOX(double angle, int[] point)
	{
		double[][] transMatrix = new double[4][4];
		double[][] tempPoint = new double[4][1];
		double[][] newPointTemp = new double[4][1];
		int[] newPoint = new int[4];
		
		for(int i = 0; i < point.length; i++)
		{
			tempPoint[i][0] = point[i];
		}
		
		for(int i = 0; i < transMatrix[0].length; i++)
		{
			for(int j = 0; j < transMatrix.length; j++)
			{
				transMatrix[i][j] = 0;
			}
		}
		transMatrix[2][2] = Math.cos(angle);
		transMatrix[1][1] = Math.cos(angle);
		transMatrix[1][2] = Math.sin(angle);
		transMatrix[2][1] = -Math.sin(angle);
		transMatrix[0][0] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(transMatrix, tempPoint);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[i][0];
		}
		
		return newPoint;
	}
	
	private double[][] matrixMul(double[][] A, double[][] B) 
	{

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) 
        {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) 
        {
            for (int j = 0; j < bColumns; j++) 
            {
                C[i][j] = (double)0;
            }
        }

        for (int i = 0; i < aRows; i++) 
        {
            for (int j = 0; j < bColumns; j++) 
            {
                for (int k = 0; k < aColumns; k++) 
                {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }
	
	private double[][] transMatrix(int[] pO, int[] pC)
	{
		double[][] transMatrix1 = new double[4][4];
		double[][] transMatrix2 = new double[4][4];
		double[][] transMatrix3 = new double[4][4];
		double[][] transMatrix4 = new double[4][4];
		double[][] transMatrixFinal = new double[4][4];
		
		for(int i = 0; i < transMatrix1[0].length; i++)
		{
			for(int j = 0; j < transMatrix1.length; j++)
			{
				transMatrix1[i][j] = 0;
			}
		}
		for(int i = 0; i < transMatrix2[0].length; i++)
		{
			for(int j = 0; j < transMatrix2.length; j++)
			{
				transMatrix2[i][j] = 0;
			}
		}
		for(int i = 0; i < transMatrix3[0].length; i++)
		{
			for(int j = 0; j < transMatrix3.length; j++)
			{
				transMatrix3[i][j] = 0;
			}
		}
		for(int i = 0; i < transMatrix4[0].length; i++)
		{
			for(int j = 0; j < transMatrix4.length; j++)
			{
				transMatrix4[i][j] = 0;
			}
		}
		for(int i = 0; i < transMatrixFinal[0].length; i++)
		{
			for(int j = 0; j < transMatrixFinal.length; j++)
			{
				transMatrixFinal[i][j] = 0;
			}
		}
		transMatrix1[0][0] = 1;
		transMatrix1[1][1] = 1;
		transMatrix1[2][2] = 1;
		transMatrix1[3][3] = 1;
		transMatrix1[0][3] = -pC[0];
		transMatrix1[1][3] = -pC[1];
		transMatrix1[2][3] = -pC[2];
		
		int[] pO1 = translation(-pC[0], -pC[1], -pC[2], pO);
		
		double angleY = -(Math.PI/2) + Math.atan2(pO1[0], pO1[2]);
		transMatrix2[0][0] = Math.cos(angleY);
		transMatrix2[2][2] = Math.cos(angleY);
		transMatrix2[0][2] = -Math.sin(angleY);
		transMatrix2[2][0] = Math.sin(angleY);
		transMatrix2[1][1] = 1;
		transMatrix2[3][3] = 1;
		
		int[] pO2 = rotateOY(angleY, pO1);
		
		double angleX = Math.PI + Math.atan2(pO2[2], pO2[1]);
		transMatrix3[2][2] = Math.cos(angleX);
		transMatrix3[1][1] = Math.cos(angleX);
		transMatrix3[1][2] = Math.sin(angleX);
		transMatrix3[2][1] = -Math.sin(angleX);
		transMatrix3[0][0] = 1;
		transMatrix3[3][3] = 1;
		
		int v[] = new int[4];
		v[0] = 0;
		v[1] = 1;
		v[2] = 0;
		v[3] = 1;
		
		int[] v1 = translation(-pC[0], -pC[1], -pC[2], v);
		int[] v2 = rotateOY(angleY, v1);
		int[] v3 = rotateOX(angleX, v2);
		
		double angleZ = (Math.PI/2) - Math.atan2(v3[0], v3[1]);
		transMatrix4[0][0] = Math.cos(angleZ);
		transMatrix4[1][1] = Math.cos(angleZ);
		transMatrix4[0][1] = Math.sin(angleZ);
		transMatrix4[1][0] = -Math.sin(angleZ);
		transMatrix4[2][2] = 1;
		transMatrix4[3][3] = 1;
		
		transMatrixFinal = matrixMul(matrixMul(matrixMul(transMatrix1, transMatrix2), transMatrix3), transMatrix4);
		
		return transMatrixFinal;
	}
	
	private double[][] pToCol(int[] p)
	{
		double[][] out = new double[4][1];
		for(int i = 0; i < p.length; i++)
		{
			out[i][0] = (double)p[i];
		}
		return out;
	}
	
	public void flatShading(Point[] pList, int[] colorList, Graphics g)
	{
		int color = MainPanel.int2RGB(
				(MainPanel.getRed(colorList[0]) + MainPanel.getRed(colorList[1]) 
				+ MainPanel.getRed(colorList[2]))/3, 
				(MainPanel.getGreen(colorList[0]) + MainPanel.getGreen(colorList[1]) 
				+ MainPanel.getGreen(colorList[2]))/3, 
				(MainPanel.getBlue(colorList[0]) + MainPanel.getBlue(colorList[1]) 
				+ MainPanel.getBlue(colorList[2]))/3);
		g.setColor(new Color(color));
		for(int i = 0; i < this.getHeight(); i++)
		{
			for(int j = 0; j < this.getWidth(); j++)
			{
				if(PointInTriangle(new Point(i, j), pList[0], pList[1], pList[2]))
				{
					g.drawLine(i, j, i, j);
				}
			}
		}
	}
	
	public void flatShadingSingle(Point p, int[] colorList, Graphics g)
	{
		int color = MainPanel.int2RGB(
				(MainPanel.getRed(colorList[0]) + MainPanel.getRed(colorList[1]) 
				+ MainPanel.getRed(colorList[2]))/3, 
				(MainPanel.getGreen(colorList[0]) + MainPanel.getGreen(colorList[1]) 
				+ MainPanel.getGreen(colorList[2]))/3, 
				(MainPanel.getBlue(colorList[0]) + MainPanel.getBlue(colorList[1]) 
				+ MainPanel.getBlue(colorList[2]))/3);
		g.setColor(new Color(color));
		g.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX(), (int)p.getY());
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
	
	public void gourandShadingTriangle(Point[] pointAr, int[] colorAr, Graphics g)
	{
		Point v1;
		Point v2;
		Point v3;
		Point v4;
		int c1;
		int c2;
		int c3;
		int c4;
		
		if(pointAr[0].getY() < pointAr[1].getY() && pointAr[0].getY() < pointAr[2].getY())
		{
			v1 = pointAr[0];
			c1 = colorAr[0];
			if(pointAr[1].getY() > pointAr[2].getY())
			{
				v4 = pointAr[1];
				c4 = colorAr[1];
				v2 = pointAr[2];
				c2 = colorAr[2];
			}
			else
			{
				v4 = pointAr[2];
				c4 = colorAr[2];
				v2 = pointAr[1];
				c2 = colorAr[1];
			}
		}
		else if(pointAr[1].getY() < pointAr[0].getY() && pointAr[1].getY() < pointAr[2].getY())
		{
			v1 = pointAr[1];
			c1 = colorAr[1];
			if(pointAr[0].getY() > pointAr[2].getY())
			{
				v4 = pointAr[0];
				c4 = colorAr[0];
				v2 = pointAr[2];
				c2 = colorAr[2];
			}
			else
			{
				v4 = pointAr[2];
				c4 = colorAr[2];
				v2 = pointAr[0];
				c2 = colorAr[0];
			}
		}
		else //if(pointAr[2].getY() < pointAr[1].getY() && pointAr[2].getY() < pointAr[0].getY())
		{
			v1 = pointAr[2];
			c1 = colorAr[2];
			if(pointAr[1].getY() > pointAr[2].getY())
			{
				v4 = pointAr[1];
				c4 = colorAr[1];
				v2 = pointAr[2];
				c2 = colorAr[2];
			}
			else
			{
				v4 = pointAr[2];
				c4 = colorAr[2];
				v2 = pointAr[1];
				c2 = colorAr[1];
			}
		}
		
		int y = (int)v2.getY();
		double beta = (y - v1.getY())/(v4.getY()-v1.getY());
		int x = (int) (beta*v1.getX() + (1 - beta)*v4.getX());
		
		c3 = (int) (beta*c1 + (1 - beta)*c4);
		v3 = new Point(x, y);
		
		
		gourandShadingSpecialTriangle(new Point[]{v1, v2, v3}, new int[]{c1,c2,c3}, g);
		gourandShadingSpecialTriangle(new Point[]{v4, v3, v2}, new int[]{c4,c3,c2}, g);
	}
	
	public void gourandShadingSpecialTriangle(Point[] pointAr, int[] colorAr, Graphics g)
	{//tylko 1 kolor/zmienna
		Point p1 = pointAr[0];
		Point p2;
		Point p3;
		int c1 = colorAr[0];
		int c2;
		int c3;
		if(pointAr[1].getX() < pointAr[2].getX())
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
		if(pointAr[1].getY() < p1.getY())
		{
			p1 = pointAr[1];
			c1 = colorAr[1];
			if(pointAr[0].getX() < pointAr[2].getX())
			{
				p2 = pointAr[0];
				c2 = colorAr[0];
				p3 = pointAr[2];
				c3 = colorAr[2];
			}
			else
			{
				p2 = pointAr[2];
				c2 = colorAr[2];
				p3 = pointAr[0];
				c3 = colorAr[0];
			}
		}
		if(pointAr[2].getY() < p1.getY())
		{
			p1 = pointAr[2];
			c1 = colorAr[2];
			if(pointAr[1].getX() < pointAr[0].getX())
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
		int yu = (int)p1.getY();
		int yl = (int)p3.getY();
		
		for(int y = yu; y <= yl; y++)
		{
			double beta_y = (y - yu)/(yl-yu);
			int xl = (int) (beta_y*p1.getX() + (1 - beta_y)*p2.getX());
			int xr = (int) (beta_y*p1.getX() + (1 - beta_y)*p3.getX());
			
			int al = (int) (beta_y*c1 + (1 - beta_y)*c2);
			int ar = (int) (beta_y*c1 + (1 - beta_y)*c3);
			
			for(int x = xl; x <= xr; x++)
			{
				double alfa = (x - xl)/(xr - xl);
				int c = (int) (alfa*al + (1 - alfa)*ar);
				g.setColor(new Color(c));
			}
		}
		
	}
	
	public int gourandShadingColor(Point p, Point[] pointAr, int[] colorAr)
	{
		Point v1;
		Point v2;
		Point v3;
		Point v4;
		int c1;
		int c2;
		int c3;
		int c4;
		
		if(pointAr[0].getY() < pointAr[1].getY() && pointAr[0].getY() < pointAr[2].getY())
		{
			v1 = pointAr[0];
			c1 = colorAr[0];
			if(pointAr[1].getY() > pointAr[2].getY())
			{
				v4 = pointAr[1];
				c4 = colorAr[1];
				v2 = pointAr[2];
				c2 = colorAr[2];
			}
			else
			{
				v4 = pointAr[2];
				c4 = colorAr[2];
				v2 = pointAr[1];
				c2 = colorAr[1];
			}
		}
		else if(pointAr[1].getY() < pointAr[0].getY() && pointAr[1].getY() < pointAr[2].getY())
		{
			v1 = pointAr[1];
			c1 = colorAr[1];
			if(pointAr[0].getY() > pointAr[2].getY())
			{
				v4 = pointAr[0];
				c4 = colorAr[0];
				v2 = pointAr[2];
				c2 = colorAr[2];
			}
			else
			{
				v4 = pointAr[2];
				c4 = colorAr[2];
				v2 = pointAr[0];
				c2 = colorAr[0];
			}
		}
		else //if(pointAr[2].getY() < pointAr[1].getY() && pointAr[2].getY() < pointAr[0].getY())
		{
			v1 = pointAr[2];
			c1 = colorAr[2];
			if(pointAr[1].getY() > pointAr[0].getY())
			{
				v4 = pointAr[1];
				c4 = colorAr[1];
				v2 = pointAr[0];
				c2 = colorAr[0];
			}
			else
			{
				v4 = pointAr[0];
				c4 = colorAr[0];
				v2 = pointAr[1];
				c2 = colorAr[1];
			}
		}
		
		int y = (int)v2.getY();
		double beta;
		if(v4.getY()-v1.getY() == 0)
		{
			beta = (y - v1.getY())/0.0001;
		}
		else
		{
			beta = (y - v1.getY())/(v4.getY()-v1.getY());
		}
		
		int x = (int) (beta*v1.getX() + (1 - beta)*v4.getX());
		
		c3 = (int) (beta*c1 + (1 - beta)*c4);
		v3 = new Point(x, y);
		
		if(p.getY() < v3.getY())
		{
			return gourandShadingColorSpecialTriangle(p, new Point[]{v1, v3, v2}, new int[]{c1,c3,c2});
		}
		else
		{
			return gourandShadingColorSpecialTriangle(p, new Point[]{v4, v2, v3}, new int[]{c4,c2,c3});
		}
	}
	
	public int gourandShadingColorSpecialTriangle(Point p, Point[] pointAr, int[] colorAr)
	{
		Point p1 = pointAr[0];
		Point p2 = pointAr[1];
		Point p3 = pointAr[2];
		int c1 = colorAr[0];
		int c2 = colorAr[1];
		int c3 = colorAr[2];
		
		int c;
		int x = (int) p.getX();
		int y = (int) p.getY();
		
		int yu = (int)p1.getY();
		int yl = (int)p3.getY();
		
		double beta_y;
		if((yl-yu) == 0)
		{
			beta_y = (y - yu)/0.0001;
		}
		else
		{
			beta_y = (y - yu)/(yl-yu);
		}
		
		int xl = (int) (beta_y*p1.getX() + (1 - beta_y)*p2.getX());
		int xr = (int) (beta_y*p1.getX() + (1 - beta_y)*p3.getX());
		
		int al = (int) (beta_y*c1 + (1 - beta_y)*c2);
		int ar = (int) (beta_y*c1 + (1 - beta_y)*c3);
		
		
		double alfa;
		if((xr - xl) == 0)
		{
			alfa = (x - xl)/0.0001;
		}
		else
		{
			alfa = (x - xl)/(xr - xl);
		}
		c = (int) (alfa*al + (1 - alfa)*ar);

		return c;
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
}
