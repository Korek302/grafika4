package grafika4;

import java.awt.Event;
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
	ProjectionPanelYZ panel3;
	ProjectionPanelXY panel4;
	
	static ArrayList<Vertex> vertexList;
	static ArrayList<Triangle> triangleList;
	static ArrayList<Integer> vertexIndicesList;
	
    public MainPanel() 
    {
    	setLayout(null);
    	panel1 = new ProjectionPanelPerspective();
    	panel2 = new ProjectionPanelXZ(this);
    	panel3 = new ProjectionPanelYZ(this);
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
		
		int mul = 100;
		
		for(; offset < 4*numberOfVertices + currOffset; offset += 4)
		{
			vertexList.add(new Vertex((int)(mul*Double.parseDouble(file[offset + 1])), 
					(int)(mul*Double.parseDouble(file[offset+2])),
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
    
    private String readFile(String path) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}
}
