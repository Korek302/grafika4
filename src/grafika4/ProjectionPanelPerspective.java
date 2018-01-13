package grafika4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
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
		ArrayList<int[]> checker = new ArrayList<int[]>();
		
		int x;
		int y;
		
		int i;
		double d = dist2p(obs, imageCenter);
		
    	int centerizerX = this.getWidth()/2 + imageCenter[0];
    	int centerizerY = this.getHeight()/2 + imageCenter[1];
		
		double[][] transMatrix = transMatrix(obs, imageCenter);
    			
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
    	checker.clear();
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
	
	public double dist2p(int[] p1, int[] p2)
	{
		return Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * 
				(p2[1] - p1[1]) + (p2[2] - p1[2]) * (p2[2] - p1[2]));
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
}
