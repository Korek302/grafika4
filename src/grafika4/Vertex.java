package grafika4;

public class Vertex 
{
	private int x;
	private int y;
	private int z;
	
	public Vertex() 
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vertex(int newX, int newY, int newZ)
	{
		x = newX;
		y = newY;
		z = newZ;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getZ()
	{
		return z;
	}
}
