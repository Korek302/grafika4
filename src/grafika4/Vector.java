package grafika4;

public class Vector 
{
	private double x;
	private double y;
	private double z;
	
	public Vector() 
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector(double newX, double newY, double newZ)
	{
		x = newX;
		y = newY;
		z = newZ;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public void setX(double newX)
	{
		x = newX;
	}
	
	public void setY(double newY)
	{
		y = newY;
	}
	
	public void setZ(double newZ)
	{
		z = newZ;
	}
}
