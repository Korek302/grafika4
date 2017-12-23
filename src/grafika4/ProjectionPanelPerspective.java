package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ProjectionPanelPerspective extends JPanel
{
	int[] obs;
	
	public ProjectionPanelPerspective() 
	{
		super();
		setBackground(new Color(123, 123, 0));
		obs = new int[4];
		obs[0] = 500;
		obs[1] = 500;
		obs[2] = 500;
		obs[3] = 1;
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
    	for(Triangle t : MainPanel.triangleList)
    	{
    		Point[] targetPointArray = new Point[3];
    		
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
    		int f = 1000;
    		int i = 0;
    		for(int[] p : pointList)
    		{
    			/*int x = ((p[0] - obs[0]) * (f/p[2])) + obs[0];
    			int y = ((p[1] - obs[1]) * (f/p[2])) + obs[1];
    			
    			Point obsPoint = new Point(x, y);
    			
    			targetPointArray[i] = obsPoint;
    			i++;*/
    			
    			int[] p1 = translation(-p[0],-p[1],-p[2],p);
    			int[] obs1 = translation(-p[0],-p[1],-p[2],obs);
    			
    			double angle = Math.PI - Math.atan2(obs1[0], obs1[2]);
    			int[] obs2 = rotateOY(angle, obs1);
    			
    			double angle2 = (-Math.PI/2) - Math.atan2(obs2[2], obs2[1]);
    			int[] obs3 = rotateOX(angle2, obs2);
    			
    			int[] dv = 	new int[4];
    			dv[0] = 1;
    			dv[1] = 1;
    			dv[2] = 0;
    			dv[3] = 1;
    			
    			int[] dv1 = translation(p[0],p[1],p[2],dv);
    			int[] dv2 = rotateOY(angle, dv1);
    			int[] dv3 = rotateOX(angle2, dv2);
    			
    			double angle3 = (Math.PI/2) - Math.atan2(dv3[0], dv3[1]);
    			int[] dv4 = rotateOZ(angle3, dv3);
    			
    			int x = dv4[0]*(-obs3[2]/(obs3[2] + dv4[2]));
    			int y = dv4[1]*(-obs3[2]/(obs3[2] + dv4[2]));
    			
    			Point obsPoint = new Point(x, y);
    			
    			targetPointArray[i] = obsPoint;
    			i++;
    		}
    		
    		g.drawLine((int)targetPointArray[0].getX(), (int)targetPointArray[0].getY(), 
    				(int)targetPointArray[1].getX(), (int)targetPointArray[1].getY());
    		g.drawLine((int)targetPointArray[1].getX(), (int)targetPointArray[1].getY(), 
    				(int)targetPointArray[2].getX(), (int)targetPointArray[2].getY());
    		g.drawLine((int)targetPointArray[2].getX(), (int)targetPointArray[2].getY(), 
    				(int)targetPointArray[0].getX(), (int)targetPointArray[0].getY());
    	}
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
	
	public int[] translation(int tx, int ty, int tz, int[] point)
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
		transMatrix[0][0] = 1;
		transMatrix[1][1] = 1;
		transMatrix[2][2] = 1;
		transMatrix[3][3] = 1;
		transMatrix[0][3] = tx;
		transMatrix[1][3] = ty;
		transMatrix[2][3] = tz;
		
		newPointTemp = matrixMul(tempPoint, transMatrix);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[0][i];
		}
		
		return newPoint;
	}
	
	public int[] rotateOZ(double angle, int[] point)
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
		transMatrix[0][0] = Math.cos(angle);
		transMatrix[1][1] = Math.cos(angle);
		transMatrix[0][1] = -Math.sin(angle);
		transMatrix[1][0] = Math.sin(angle);
		transMatrix[2][2] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(tempPoint, transMatrix);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[0][i];
		}
		
		return newPoint;
	}
	
	public int[] rotateOY(double angle, int[] point)
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
		transMatrix[0][0] = Math.cos(angle);
		transMatrix[2][2] = Math.cos(angle);
		transMatrix[0][2] = Math.sin(angle);
		transMatrix[2][0] = -Math.sin(angle);
		transMatrix[1][1] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(tempPoint, transMatrix);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[0][i];
		}
		
		return newPoint;
	}
	
	public int[] rotateOX(double angle, int[] point)
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
		transMatrix[2][2] = Math.cos(angle);
		transMatrix[1][1] = Math.cos(angle);
		transMatrix[1][2] = -Math.sin(angle);
		transMatrix[2][1] = Math.sin(angle);
		transMatrix[0][0] = 1;
		transMatrix[3][3] = 1;
		
		newPointTemp = matrixMul(tempPoint, transMatrix);
		
		for(int i = 0; i < newPoint.length; i++)
		{
			newPoint[i] = (int)newPointTemp[0][i];
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
}
