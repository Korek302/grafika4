package grafika4;

public class Triangle 
{
	private Vertex vertex1;
	private Vertex vertex2;
	private Vertex vertex3;
	
	private Vector vn;
	
	public Triangle() 
	{
		vertex1 = new Vertex();
		vertex2 = new Vertex();
		vertex3 = new Vertex();
		vn = new Vector();
	}
	
	public Triangle(Vertex newV1, Vertex newV2, Vertex newV3)
	{
		vertex1 = newV1;
		vertex2 = newV2;
		vertex3 = newV3;
		vn = new Vector();
	}
	
	public Triangle(Vertex newV1, Vertex newV2, Vertex newV3, Vector newVn)
	{
		vertex1 = newV1;
		vertex2 = newV2;
		vertex3 = newV3;
		vn = newVn;
	}
	
	public Vertex getV1()
	{
		return vertex1;
	}
	public Vertex getV2()
	{
		return vertex2;
	}
	public Vertex getV3()
	{
		return vertex3;
	}
	public Vector getVn()
	{
		return vn;
	}
	public void setV1(Vertex v)
	{
		vertex1 = v;
	}
	public void setV2(Vertex v)
	{
		vertex2 = v;
	}
	public void setV3(Vertex v)
	{
		vertex3 = v;
	}
	public void setVn(Vector newVn)
	{
		vn = newVn;
	}
}
