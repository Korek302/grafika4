package grafika4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener
{
	ProjectionPanelPerspective panel1;
	ProjectionPanelXZ panel2;
	ProjectionPanelZY panel3;
	ProjectionPanelXY panel4;
	
	static ArrayList<Vertex> vertexList;
	static ArrayList<Triangle> triangleList;
	static ArrayList<Integer> vertexIndicesList;
	static ArrayList<Double> vnList;
	
	static int shading;
	
	int[] obs;
	int[] imageCenter;
	int[] light;
	
	JButton flatShadingButton;
	JButton gouraudShadingButton;
	JButton cameraChangeButton;
	JButton lightChangeButton;
	
	JTextField xCameraTF;
	JTextField yCameraTF;
	JTextField zCameraTF;
	
	JTextField xLightTF;
	JTextField yLightTF;
	JTextField zLightTF;
	
    public MainPanel() 
    {
    	setLayout(null);
    	
    	obs = new int[4];
		obs[0] = -500;
		obs[1] = -500;
		obs[2] = 500;
		obs[3] = 1;
		
		imageCenter = new int[4];
		imageCenter[0] = -50;
		imageCenter[1] = -50;
		imageCenter[2] = 50;
		imageCenter[3] = 1;
		
		light = new int[4];
		light[0] = 500;
		light[1] = 500;
		light[2] = -500;
		light[3] = 1;
		
		shading = 0;
    	
    	panel1 = new ProjectionPanelPerspective(this);
    	panel2 = new ProjectionPanelXZ(this);
    	panel3 = new ProjectionPanelZY(this);
    	panel4 = new ProjectionPanelXY(this);
    	
    	panel1.setBounds(0, 0, 490, 340);
    	panel2.setBounds(500, 0, 490, 340);
    	panel3.setBounds(0, 350, 490, 340);
    	panel4.setBounds(500, 350, 490, 340);
    	
    	flatShadingButton = new JButton("Flat");
    	gouraudShadingButton = new JButton("Gouraud");
    	
    	flatShadingButton.setBounds(1000, 60, 120, 50);
    	gouraudShadingButton.setBounds(1000, 10, 120, 50);
    	
    	xCameraTF = new JTextField(Integer.toString(obs[0]));
    	yCameraTF = new JTextField(Integer.toString(obs[1]));
    	zCameraTF = new JTextField(Integer.toString(obs[2]));
    	
    	xCameraTF.setBounds(1000, 130, 120, 30);
    	yCameraTF.setBounds(1000, 160, 120, 30);
    	zCameraTF.setBounds(1000, 190, 120, 30);
    	
    	cameraChangeButton = new JButton("Change camera");
    	cameraChangeButton.setBounds(1000, 225, 120, 30);
    	
    	xLightTF = new JTextField(Integer.toString(light[0]));
    	yLightTF = new JTextField(Integer.toString(light[1]));
    	zLightTF = new JTextField(Integer.toString(light[2]));
    	
    	xLightTF.setBounds(1000, 270, 120, 30);
    	yLightTF.setBounds(1000, 300, 120, 30);
    	zLightTF.setBounds(1000, 330, 120, 30);
    	
    	lightChangeButton = new JButton("Change light");
    	lightChangeButton.setBounds(1000, 360, 120, 30);
	
    	vertexList = new ArrayList<Vertex>();
    	triangleList = new ArrayList<Triangle>();
    	vertexIndicesList = new ArrayList<Integer>();
    	
    	int offset = 0;
    	
    	String formFile = null;
		
    	try 
		{
			formFile = readFile("res/snowman2.txt");
		} 
		catch (IOException e) 
		{
			System.out.println("Error while loading file (figura.txt)");
		}

		String[] file = formFile.split("\\r?\\n|\\ ");
		
		int numberOfVertices = Integer.parseInt(file[0]);
		offset++;
		int currOffset = offset;
		
		double mul = 0.4;
		
		for(; offset < 4*numberOfVertices + currOffset; offset += 4)
		{
			vertexList.add(new Vertex((int)(mul*Double.parseDouble(file[offset + 1])), 
					-(int)(mul*Double.parseDouble(file[offset+2])),
					(int)(mul*Double.parseDouble(file[offset+3]))));
		}
		
		int numberOfTriangles = Integer.parseInt(file[offset]);
		offset++;
		
		currOffset = offset;
		for(; offset < 4*numberOfTriangles + currOffset; offset += 4)
		{
			triangleList.add(new Triangle(vertexList.get(Integer.parseInt(file[offset + 1]) - 1),
					vertexList.get(Integer.parseInt(file[offset + 2]) - 1),
					vertexList.get(Integer.parseInt(file[offset + 3]) - 1)));
			
			vertexIndicesList.add(Integer.parseInt(file[offset + 1]) - 1);
			vertexIndicesList.add(Integer.parseInt(file[offset + 2]) - 1);
			vertexIndicesList.add(Integer.parseInt(file[offset + 3]) - 1);
		}
		
		for(Triangle t : triangleList)
		{
			Vector v1 = new Vector(t.getV1().getX() - t.getV2().getX(),
					t.getV1().getY() - t.getV2().getY(),
					t.getV1().getZ() - t.getV2().getZ());
			Vector v2 = new Vector(t.getV3().getX() - t.getV2().getX(),
					t.getV3().getY() - t.getV2().getY(),
					t.getV3().getZ() - t.getV2().getZ());
			t.setVn(vectorProd(v1, v2));
		}
		
		initLightModel();
    	
    	add(panel1);
    	add(panel2);
    	add(panel3);
    	add(panel4);
    	
    	gouraudShadingButton.addActionListener(this);
    	flatShadingButton.addActionListener(this);
    	cameraChangeButton.addActionListener(this);
    	lightChangeButton.addActionListener(this);
    	
    	add(gouraudShadingButton);
    	add(flatShadingButton);
    	add(cameraChangeButton);
    	add(lightChangeButton);
    	
    	add(xCameraTF);
    	add(yCameraTF);
    	add(zCameraTF);
    	
    	add(xLightTF);
    	add(yLightTF);
    	add(zLightTF);
    }
    
    public void repaintAll()
    {
    	panel1.repaint();
    	panel2.repaint();
    	panel3.repaint();
    	panel4.repaint();
    }
    
    public void repaintXYandPP()
    {
    	panel1.repaint();
    	panel4.repaint();
    }
    
    private String readFile(String path) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}
    private void phongLightModel(ArrayList<Vertex> verList)
    {
    	double Lr = 0;
    	double Lg = 0;
    	double Lb = 0;
    	
    	double Sr = 0;
    	double Sg = 0;
    	double Sb = 0;
    	
    	double g = 1;
    	
    	double kdr = 0.5;
    	double kdg = 0.5;
    	double kdb = 0.5;
    	
    	double ksr = 0.5;
    	double ksg = 0.5;
    	double ksb = 0.5;
    	
    	double kar = 0.3;
    	double kag = 0.3;
    	double kab = 0.3;
    	
    	double Er = 100;
    	double Eg = 20;
    	double Eb = 20;
    	
    	double Ar = 200;
    	double Ag = 50;
    	double Ab = 50;
    	
    	for(Vertex v : verList)
    	{
    		double distToL = dist2p(new int[]{v.getX(), v.getY(), v.getZ()}, light);
    		
    		Lr = Sr + kdr*lightAttenuation(distToL)*
    				Er*vectorDotProd(v.getVn(), v.getVl()) 
    				+ ksr*lightAttenuation(distToL)*Er*
    				Math.pow(vectorDotProd(v.getVl(), new Vector(-v.getVo().getX(), v.getVo().getY(), -v.getVo().getZ())), g) 
    				+ kar * Ar;
    		
    		Lg = Sg + kdg*lightAttenuation(distToL)*
    				Eg*vectorDotProd(v.getVn(), v.getVl()) 
    				+ ksg*lightAttenuation(distToL)*Eg*
    				Math.pow(vectorDotProd(v.getVl(), new Vector(-v.getVo().getX(), v.getVo().getY(), -v.getVo().getZ())), g) 
    				+ kag * Ag;
    		
    		Lb = Sb + kdb*lightAttenuation(distToL)*
    				Eb*vectorDotProd(v.getVn(), v.getVl()) 
    				+ ksb*lightAttenuation(distToL)*Eb*
    				Math.pow(vectorDotProd(v.getVl(), new Vector(-v.getVo().getX(), v.getVo().getY(), -v.getVo().getZ())), g) 
    				+ kab * Ab;
    		
    		//System.out.println("Lr: " + Lr + "Lg: " + Lg + "Lb: " + Lb + "      " + lightAttenuation(distToL));
    		v.setColor(int2RGB((int)(Lr), (int)(Lg), (int)(Lb)));
    	}
    }
    private double lightAttenuation(double dist)
    {
    	double c0 = 1;
    	double c1 = 0;
    	double c2 = 0;
    	return Math.min(1/(c2*dist*dist + c1*dist + c0), 1);
    }
    public static double dist2p(int[] p1, int[] p2)
	{
		return Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * 
				(p2[1] - p1[1]) + (p2[2] - p1[2]) * (p2[2] - p1[2]));
	}
    
    public static double vectorDotProd(Vector v1, Vector v2)
    {
    	return v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ()*v2.getZ();
    }
    
    public Vector vectorProd(Vector v1, Vector v2)
    {
    	return new Vector(v1.getY()*v2.getZ() - v1.getZ()*v2.getY(), 
    			v1.getZ()*v2.getX() - v1.getX()*v2.getZ(),
    			v1.getX()*v2.getY() - v1.getY()*v2.getX());
    }
    
    private void initLightModel()
    {
    	int j = 0;
		for(Vertex v : vertexList)
		{
			//Vn
			ArrayList<Triangle> adjTriangles = new ArrayList<Triangle>();
			int l = 0;
			for(int k = 0; k < vertexIndicesList.size(); k += 3)
			{
				if(vertexIndicesList.get(k) == j || 
						vertexIndicesList.get(k + 1) == j ||
						vertexIndicesList.get(k + 2) == j)
				{
					adjTriangles.add(triangleList.get(l));
				}
				l++;
			}
			
			double sumOfX = 0;
			double sumOfY = 0;
			double sumOfZ = 0;
			double lengthOfSum = 0;
			
			for(Triangle t : adjTriangles)
			{
				sumOfX += t.getVn().getX();
				sumOfY += t.getVn().getY();
				sumOfZ += t.getVn().getZ();
			}
			
			lengthOfSum = dist2p(new int[]{0,0,0}, 
					new int[]{(int)sumOfX, (int)sumOfY, (int)sumOfX});
			
			v.setVn(new Vector(sumOfX/lengthOfSum, sumOfY/lengthOfSum, sumOfZ/lengthOfSum));
			
			//Vo
    		double distToO = dist2p(new int[]{v.getX(), v.getY(), v.getZ()}, obs);
    		v.setVo(new Vector((obs[0]-v.getX())/distToO, 
    				(obs[1]-v.getY())/distToO, 
    				(obs[2]-v.getZ())/distToO));
			
    		//Vl
    		double distToL = dist2p(new int[]{v.getX(), v.getY(), v.getZ()}, light);
    		v.setVl(new Vector((light[0]-v.getX())/distToL, 
    				(light[1]-v.getY())/distToL, 
    				(light[2]-v.getZ())/distToL));
			
			j++;
		}
		
		phongLightModel(vertexList);
    }
    
    static int int2RGB( int red, int green, int blue)
	{
		red = red & 0x000000FF;
		green = green & 0x000000FF;
		blue = blue & 0x000000FF;
		return (red << 16) + (green << 8) + blue;
	}
	static int getRed(int rgb)
	{
		return (rgb >> 16) & 0xFF;
	}
	
	static int getGreen(int rgb)
	{
		return (rgb >> 8) & 0xFF;
	}
	
	static int getBlue(int rgb)
	{
		return rgb & 0xFF;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if(source == flatShadingButton)
		{
			shading = 0;
			System.out.println("Click!");
			repaintAll();
		}
		if(source == gouraudShadingButton)
		{
			shading = 1;
			System.out.println("Click!");
			repaintAll();
		}
		if(source == cameraChangeButton)
		{
			System.out.println("Click!");
			obs[0] = Integer.parseInt(xCameraTF.getText());
			obs[1] = Integer.parseInt(yCameraTF.getText());
			obs[2] = Integer.parseInt(zCameraTF.getText());
			if(obs[0] < 0)
				imageCenter[0] = -Math.abs(imageCenter[0]);
			else
				imageCenter[0] = Math.abs(imageCenter[0]);
			if(obs[1] < 0)
				imageCenter[1] = -Math.abs(imageCenter[1]);
			else
				imageCenter[1] = Math.abs(imageCenter[1]);
			if(obs[2] < 0)
				imageCenter[2] = -Math.abs(imageCenter[2]);
			else
				imageCenter[2] = Math.abs(imageCenter[2]);
			
			//initLightModel();
			repaintAll();
		}
		if(source == lightChangeButton)
		{
			System.out.println("Click!");
			light[0] = Integer.parseInt(xLightTF.getText());
			light[1] = Integer.parseInt(yLightTF.getText());
			light[2] = Integer.parseInt(zLightTF.getText());
			
			initLightModel();
			repaintAll();
		}
	}
}
