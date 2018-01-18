package grafika4;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Gui 
{
	JFrame frame;
	BufferedImage currImage;
	
	public static void main(String[] args)
	{
		Gui gui = new Gui();
		gui.GUI();
	}
	
	private void GUI()
	{
		frame = new JFrame();
		MainPanel panel = new MainPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Grafika4");
		frame.setSize(1150, 730);
		frame.getContentPane().add(panel);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		frame.setVisible(true);
	}
}
