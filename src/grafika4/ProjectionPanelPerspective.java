package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.peer.PanelPeer;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.AncestorListener;

@SuppressWarnings("serial")
public class ProjectionPanelPerspective extends JPanel
{
	int[] obs;
	
	public ProjectionPanelPerspective() 
	{
		super();
		setBackground(new Color(123, 123, 123));
		obs = new int[4];
		obs[0] = 500;
		obs[1] = 500;
		obs[2] = 500;
		obs[3] = 1;
	}
	
	@Override
    protected void paintComponent(Graphics g) 
    {
		super.paintComponent(g);
		
		Point[] targetPointArray = new Point[3];
		ArrayList<int[]> checker = new ArrayList<int[]>();
		
		double x;
		double y;
		
		int i;
		
    	int centerizerX = this.getWidth()/2;
    	int centerizerY = this.getHeight()/2;
		
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
    			dv[0] = 0;
    			dv[1] = 1;
    			dv[2] = 0;
    			dv[3] = 1;
    			
    			int[] dv1 = translation(-p[0],-p[1],-p[2],dv);
    			int[] dv2 = rotateOY(angleY, dv1);
    			int[] dv3 = rotateOX(angleX, dv2);
    			
    			angleZ = (Math.PI/2) - Math.atan2(dv3[1], dv3[0]);
    			
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
    			e[0][0] = -p[0] + obs[0];
    			e[1][0] = -p[1] + obs[1];
    			e[2][0] = -p[2] + obs[2];
    			e[3][0] = 1;
    			
    			
    			
    			transMatrix = matrixMul(matrixMul(rotMatrixX, rotMatrixY), rotMatrixZ);
    			
    			/*transMatrix[3][0] = transMatrix[3][0] - p[0];
    			
    			transMatrix[3][1] = transMatrix[3][1] - p[1];
    			
    			transMatrix[3][2] = transMatrix[3][2] - p[2];*/
    			
    			double[][] dd = matrixMul(transMatrix, e);
    			
    			int ez = 400;
    			int ex = 600;
    			int ey = 0;
    			
    			//double alfa = Math.PI/1.9;
    			//double ez = 200 * 1/Math.tan(alfa/2);
    			
    			x = (ez/dd[2][0]) * dd[0][0] - ex;
    			y = (ez/dd[2][0]) * dd[1][0] - ey;
    			
    			
    			
    			Point targetPoint = new Point((int)x + centerizerX/*???stala???*/, 
    					/*this.getHeight() -*/ (int)y /*-*/ + centerizerY);
    			
    			System.out.println(targetPoint.getX() + ", " + targetPoint.getY());
    			
    			targetPointArray[i] = targetPoint;
    			i++;
    		}
    		
    		int[] arr1 = {(int)targetPointArray[0].getX(), (int)targetPointArray[0].getY(), 
    				(int)targetPointArray[1].getX(), (int)targetPointArray[1].getY()};
    		int[] arr2 = {(int)targetPointArray[1].getX(), (int)targetPointArray[1].getY(), 
    				(int)targetPointArray[2].getX(), (int)targetPointArray[2].getY()};
    		int[] arr3 = {(int)targetPointArray[2].getX(), (int)targetPointArray[2].getY(), 
    				(int)targetPointArray[0].getX(), (int)targetPointArray[0].getY()};
    		
    		if(!checker.contains(arr1))
    		{
    			g.drawLine((int)targetPointArray[0].getX(), (int)targetPointArray[0].getY(), 
    					(int)targetPointArray[1].getX(), (int)targetPointArray[1].getY());
    			checker.add(arr1);
    		}
    		if(!checker.contains(arr2))
    		{
	    		g.drawLine((int)targetPointArray[1].getX(), (int)targetPointArray[1].getY(), 
	    				(int)targetPointArray[2].getX(), (int)targetPointArray[2].getY());
	    		checker.add(arr2);
    		}
    		if(!checker.contains(arr3))
    		{
	    		g.drawLine((int)targetPointArray[2].getX(), (int)targetPointArray[2].getY(), 
	    				(int)targetPointArray[0].getX(), (int)targetPointArray[0].getY());
	    		checker.add(arr3);
    		}
    		
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
