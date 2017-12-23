package grafika4;

import java.util.ArrayList;
import java.util.Arrays;

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
	
    public MainPanel() 
    {
    	setLayout(null);
    	
    	vertexList = new ArrayList<Vertex>(
    			Arrays.asList(
    					new Vertex(100,100,100),
    					new Vertex(300,100,100),
    					new Vertex(100,100,200),
    					new Vertex(300,100,200),
    					new Vertex(100,200,100),
    					new Vertex(300,200,100),
    					new Vertex(100,200,200),
    					new Vertex(300,200,200)/*,
    					new Vertex(100,200,0),
    					new Vertex(100,200,100)*/))
    					
    					/*new Vertex(100,100,100),
    					new Vertex(200, 100, 100),
    					new Vertex(150,170,100)))*/;
    	triangleList = new ArrayList<Triangle>(
    			Arrays.asList(
    					new Triangle(vertexList.get(0),
    							vertexList.get(4),
    							vertexList.get(6)),
    					new Triangle(vertexList.get(0),
    							vertexList.get(6),
    							vertexList.get(1)),
    					new Triangle(vertexList.get(1),
    							vertexList.get(6),
    							vertexList.get(7)),
    					new Triangle(vertexList.get(1),
    							vertexList.get(7),
    							vertexList.get(3)),
    					new Triangle(vertexList.get(3),
    							vertexList.get(7),
    							vertexList.get(2)),
    					new Triangle(vertexList.get(2),
    							vertexList.get(7),
    							vertexList.get(5)),
    					new Triangle(vertexList.get(2),
    							vertexList.get(5),
    							vertexList.get(0)),
    					new Triangle(vertexList.get(0),
    							vertexList.get(5),
    							vertexList.get(4)),
    					new Triangle(vertexList.get(4),
    							vertexList.get(5),
    							vertexList.get(7)),
    					new Triangle(vertexList.get(4),
    							vertexList.get(7),
    							vertexList.get(6)),
    					new Triangle(vertexList.get(3),
    							vertexList.get(2),
    							vertexList.get(0)),
    					new Triangle(vertexList.get(3),
    							vertexList.get(0),
    							vertexList.get(1))/*,
    					new Triangle(vertexList.get(4),
    							vertexList.get(6),
    							vertexList.get(8)),
    					new Triangle(vertexList.get(5),
    							vertexList.get(7),
    							vertexList.get(9)),
    					new Triangle(vertexList.get(6),
    							vertexList.get(7),
    							vertexList.get(8)),
    					new Triangle(vertexList.get(7),
    							vertexList.get(8),
    							vertexList.get(9)),
    					new Triangle(vertexList.get(4),
    							vertexList.get(5),
    							vertexList.get(9)),
    					new Triangle(vertexList.get(4),
    							vertexList.get(8),
    							vertexList.get(9))*/
    					
    					/*new Triangle(vertexList.get(0),
    							vertexList.get(1),
    							vertexList.get(2))*/));
    	
    	panel1 = new ProjectionPanelPerspective();
    	panel2 = new ProjectionPanelXZ();
    	panel3 = new ProjectionPanelYZ();
    	panel4 = new ProjectionPanelXY();
    	
    	panel1.setBounds(0, 0, 490, 340);
    	panel2.setBounds(510, 0, 490, 340);
    	panel3.setBounds(0, 360, 490, 340);
    	panel4.setBounds(510, 360, 490, 340);
    	add(panel1);
    	add(panel2);
    	add(panel3);
    	add(panel4);
    }
    
}
