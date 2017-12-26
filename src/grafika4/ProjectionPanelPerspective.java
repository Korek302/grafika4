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
		Point[] targetPointArray = new Point[3];
		
		//int d = 500;
		int f = 1000;
		double x;
		double y;
		
		int i;
		
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
    			
    			/*x = ((p[0] - obs[0]) * ((double)f/(double)p[2])) + obs[0];
    			y = ((p[1] - obs[1]) * ((double)f/(double)p[2])) + obs[1];
    			
    			Point obsPoint = new Point((int)x, (int)y);
    			
    			targetPointArray[i] = obsPoint;
    			i++;*/
    			
    			/*int[] p1 = translation(-p[0],-p[1],-p[2],p);
    			int[] obs1 = translation(-p[0],-p[1],-p[2],obs);
    			
    			double angleY = Math.PI - Math.atan2(obs1[0], obs1[2]);
    			int[] obs2 = rotateOY(angleY, obs1);
    			int[] p2 = rotateOY(angleY, p1);
    			
    			double angleX = (-Math.PI/2) - Math.atan2(obs2[2], obs2[1]);
    			int[] obs3 = rotateOX(angleX, obs2);
    			int[] p3 = rotateOX(angleX, p2);
    			
    			int[] dv = 	new int[4];
    			dv[0] = p[0] - obs[0];
    			dv[1] = p[1] - obs[1];
    			dv[2] = p[2] - obs[2];
    			dv[3] = 1;
    			
    			int[] dv1 = translation(-p[0],-p[1],-p[2],dv);
    			int[] dv2 = rotateOY(angleY, dv1);
    			int[] dv3 = rotateOX(angleX, dv2);
    			
    			double angleZ = (Math.PI/2) - Math.atan2(dv3[0], dv3[1]);
    			int[] dv4 = rotateOZ(angleZ, dv3);
    			int[] obs4 = rotateOZ(angleZ, obs3);
    					
    			x = dv4[0]*(-(double)obs4[2]/(double)(obs4[2] + dv4[2]));
    			y = dv4[1]*(-(double)obs4[2]/(double)(obs4[2] + dv4[2]));
    			
    			Point obsPoint = new Point((int)x, (int)y);
    			
    			targetPointArray[i] = obsPoint;
    			i++;*/
    			
    			double[][] rotMatrixX = new double[4][4];
    			double[][] rotMatrixY = new double[4][4];
    			double[][] rotMatrixZ = new double[4][4];
    			
    			double[][] transMatrix = new double[4][4];
    			
    			double angleX;
    			double angleY;
    			double angleZ;
    			
    			int[] p1 = translation(-p[0],-p[1],-p[2],p);
    			int[] obs1 = translation(-p[0],-p[1],-p[2],obs);
    			
    			angleY = Math.PI - Math.atan2(obs1[0], obs1[2]);
    			int[] obs2 = rotateOY(angleY, obs1);
    			
    			angleX = (-Math.PI/2) - Math.atan2(obs2[2], obs2[1]);
    			int[] obs3 = rotateOX(angleX, obs2);
    			
    			int[] dv = 	new int[4];
    			dv[0] = p[0] - obs[0];
    			dv[1] = p[1] - obs[1];
    			dv[2] = p[2] - obs[2];
    			dv[3] = 1;
    			
    			angleZ = 0;
    			
    			for(int k = 0; k < rotMatrixX[0].length; k++)
    			{
    				for(int j = 0; j < rotMatrixX.length; j++)
    				{
    					rotMatrixX[k][j] = 0;
    				}
    			}
    			for(int k = 0; k < rotMatrixY[0].length; k++)
    			{
    				for(int j = 0; j < rotMatrixY.length; j++)
    				{
    					rotMatrixY[k][j] = 0;
    				}
    			}
    			for(int k = 0; k < rotMatrixZ[0].length; k++)
    			{
    				for(int j = 0; j < rotMatrixZ.length; j++)
    				{
    					rotMatrixZ[k][j] = 0;
    				}
    			}
    			rotMatrixX[0][0] = 1;
    			rotMatrixX[1][1] = Math.cos(angleX);
    			rotMatrixX[1][2] = Math.sin(angleX);
    			rotMatrixX[2][1] = -Math.sin(angleX);
    			rotMatrixX[2][2] = Math.cos(angleX);
    			rotMatrixX[3][3] = 1;
    			
    			rotMatrixY[0][0] = Math.cos(angleY);
    			rotMatrixY[0][2] = -Math.sin(angleY);
    			rotMatrixY[1][1] = 1;
    			rotMatrixY[2][0] = Math.sin(angleY);
    			rotMatrixY[2][2] = Math.cos(angleY);
    			rotMatrixY[3][3] = 1;
    			
    			rotMatrixZ[0][0] = Math.cos(angleZ);
    			rotMatrixZ[0][1] = Math.sin(angleZ);
    			rotMatrixZ[1][0] = -Math.sin(angleZ);
    			rotMatrixZ[1][1] = Math.cos(angleZ);
    			rotMatrixZ[2][2] = 1;
    			rotMatrixZ[3][3] = 1;
    			
    			double[][] e = new double[4][1];
    			e[0][0] = p[0] - obs[0];
    			e[1][0] = p[1] - obs[1];
    			e[2][0] = p[2] - obs[2];
    			e[3][0] = 1;
    			
    			transMatrix = matrixMul(matrixMul(rotMatrixX, rotMatrixY), rotMatrixZ);
    			
    			double[][] dd = matrixMul(transMatrix, e);
    			
    			int ez = 200;
    			
    			x = (ez/dd[2][0]) * dd[0][0];
    			y = (ez/dd[2][0]) * dd[1][0];
    			
    			System.out.println(x + ", " + y);
    			
    			Point targetPoint = new Point((int)x + 500/*HALO tu jest na sztywno*/, (int)y + 100);
    			
    			targetPointArray[i] = targetPoint;
    			i++;
    			
    			/*x = p[0] * (d / ((double)d + (double)p[2]));
    			y = p[1] * (d / ((double)d + (double)p[2]));
    			
    			targetPointArray[i] = new Point((int)x, (int)y);
    			
    			i++;*/
    		}
    		
    		/*System.out.println((int)targetPointArray[0].getX() + " " +  (int)targetPointArray[0].getY() + " " +  
    				(int)targetPointArray[1].getX() + " " +  (int)targetPointArray[1].getY());
    		System.out.println((int)targetPointArray[1].getX() + " " +  (int)targetPointArray[1].getY() + " " +  
    				(int)targetPointArray[2].getX() + " " +  (int)targetPointArray[2].getY());
    		System.out.println((int)targetPointArray[2].getX() + " " + (int)targetPointArray[2].getY()
    				 + " " +  
    				(int)targetPointArray[0].getX() + " " +  (int)targetPointArray[0].getY());
    		*/
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
