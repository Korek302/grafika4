package grafika4;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	ProjectionPanelPerspective panel1;
	ProjectionPanelXZ panel2;
	ProjectionPanelZY panel3;
	ProjectionPanelXY panel4;
	
	static ArrayList<Vertex> vertexList;
	static ArrayList<Triangle> triangleList;
	static ArrayList<Integer> vertexIndicesList;
	static ArrayList<Triangle> cameraPiramid;
	
	int[] obs;
	int[] imageCenter;
	
    public MainPanel() 
    {
    	setLayout(null);
    	
    	obs = new int[4];
		obs[0] = -500;
		obs[1] = 500;
		obs[2] = 500;
		obs[3] = 1;
		
		imageCenter = new int[4];
		imageCenter[0] = -50;
		imageCenter[1] = 50;
		imageCenter[2] = 50;
		imageCenter[3] = 1;
    	
    	panel1 = new ProjectionPanelPerspective(this);
    	panel2 = new ProjectionPanelXZ(this);
    	panel3 = new ProjectionPanelZY(this);
    	panel4 = new ProjectionPanelXY(this);
    	
    	panel1.setBounds(0, 0, 490, 340);
    	panel2.setBounds(500, 0, 490, 340);
    	panel3.setBounds(0, 350, 490, 340);
    	panel4.setBounds(500, 350, 490, 340);
	
    	vertexList = new ArrayList<Vertex>();
    	triangleList = new ArrayList<Triangle>();
    	vertexIndicesList = new ArrayList<Integer>();
    	
    	int offset = 0;
    	
    	String formFile = null;
		
    	try 
		{
			formFile = readFile("res/suzanne.txt");
		} 
		catch (IOException e) 
		{
			System.out.println("Error while loading file (figura.txt)");
		}

		String[] file = formFile.split("\\r?\\n|\\ ");
		
		int numberOfVertices = Integer.parseInt(file[0]);
		offset++;
		int currOffset = offset;
		
		double mul = 100;
		
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
    	
    	add(panel1);
    	add(panel2);
    	add(panel3);
    	add(panel4);
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
}
