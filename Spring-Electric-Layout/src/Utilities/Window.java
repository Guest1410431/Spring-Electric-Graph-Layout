package Utilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Graph.Graph;
import Graph.Node;

public class Window extends JPanel
{
	private final int WIDTH = 600;
	private final int HEIGHT = 600;

	private boolean mouseClicked = false;
	private boolean paused = true;
	
	private float offsetX = 0;
	private float offsetY = 0;
	private float initialX;
	private float initialY;
	
	private Graph graph;

	private JFrame frame;
	
	public Window()
	{
		// Set up Graph
		initGraph();

		// Set up Window
		initWindow();
		
		/*LinkedList<Node>linked_dfs = graph.dfs();
		for(Node n : linked_dfs)
		{
			System.out.println(n.toString());
		}*/
	}

	private void initGraph()
	{
		graph = new Graph(100);
		graph.init("Top-Sort");
	}

	private void initWindow()
	{
		setBackground(Color.WHITE);

		frame = new JFrame();

		frame.setTitle("Spring Electric Layout");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		frame.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_P || key == KeyEvent.VK_SPACE)
				{
					paused = !paused;
				}
				if(key == KeyEvent.VK_S)
				{
					graph.update();
				}
				if (key == KeyEvent.VK_ESCAPE) // key bind to close the application
				{
					System.exit(1);
				}
			}

			public void keyReleased(KeyEvent e)
			{
			}

			public void keyTyped(KeyEvent e)
			{
			}
		});
		frame.addMouseListener(new MouseListener()
		{
			public void mouseReleased(MouseEvent e)
			{
				mouseClicked = false;
			}
			public void mousePressed(MouseEvent e)
			{
				initialX = e.getX();
				initialY = e.getY();
				
				mouseClicked = true;
			}
			public void mouseExited(MouseEvent e)
			{
				
			}
			public void mouseEntered(MouseEvent e)
			{
				
			}
			public void mouseClicked(MouseEvent e)
			{
				
			}
		});
		frame.addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{
				if(paused)
				{
					graph.debug(e.getX(), e.getY());
				}
			}

			public void mouseDragged(MouseEvent e)
			{
				if(mouseClicked)
				{
					offsetX = e.getX()-initialX;
					offsetY = e.getY()-initialY;
				}
			}
		});
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Render here
		graph.render(g, offsetX, offsetY);
	}

	public void update()
	{
		// Update here
		if(!paused)
		{
			graph.update();
		}
		repaint();
	}
}











