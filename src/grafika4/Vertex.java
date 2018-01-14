package grafika4;

public class Vertex 
{
	private int x;
	private int y;
	private int z;
	
	private Vector vn;
	private Vector vl;
	private Vector vo;
	
	private int color;
	
	public Vertex() 
	{
		x = 0;
		y = 0;
		z = 0;
		vn = new Vector();
		vl = new Vector();
		vo = new Vector();
		color = MainPanel.int2RGB(0, 0, 0);
	}
	
	public Vertex(int newX, int newY, int newZ)
	{
		x = newX;
		y = newY;
		z = newZ;
		vn = new Vector();
		vl = new Vector();
		vo = new Vector();
		color = MainPanel.int2RGB(0, 0, 0);
	}
	
	public Vertex(int newX, int newY, int newZ, Vector newVn, Vector newVl, Vector newVo)
	{
		x = newX;
		y = newY;
		z = newZ;
		vn = newVn;
		vl = newVl;
		vo = newVo;
		color = MainPanel.int2RGB(0, 0, 0);
	}
	
	public Vertex(int newX, int newY, int newZ, Vector newVn, Vector newVl, Vector newVo, int newColor)
	{
		x = newX;
		y = newY;
		z = newZ;
		vn = newVn;
		vl = newVl;
		vo = newVo;
		color = newColor;
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
	
	public Vector getVn()
	{
		return vn;
	}
	
	public Vector getVl()
	{
		return vl;
	}
	
	public Vector getVo()
	{
		return vo;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void setX(int newX)
	{
		x = newX;
	}
	
	public void setY(int newY)
	{
		y = newY;
	}
	
	public void setZ(int newZ)
	{
		z = newZ;
	}
	
	public void setVn(Vector newVn)
	{
		vn = newVn;
	}
	
	public void setVl(Vector newVl)
	{
		vl = newVl;
	}
	
	public void setVo(Vector newVo)
	{
		vo = newVo;
	}
	
	public void setColor(int newColor)
	{
		color = newColor;
	}
}
