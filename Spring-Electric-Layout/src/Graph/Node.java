package Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import Utilities.ForceVector;

public class Node
{
	private static final int SIZE = 32;
	private static final float MOVE_SPEED = 0.001f;

	private float xPos;
	private float yPos;

	private ForceVector forceVector;

	private boolean debug;

	private boolean visited;

	private String nodeKey;

	private Item item;

	private ArrayList<Edge> edges;

	public Node()
	{
		this.nodeKey = "";
		this.item = new Item("");
		init();
	}

	public Node(Item item)
	{
		nodeKey = item.getValue();
		this.item = item;
		init();
	}

	public Node(String nodeKey)
	{
		this.nodeKey = nodeKey;
		item = new Item(nodeKey);
		init();
	}

	public Node(Item item, String nodeKey)
	{
		this.nodeKey = nodeKey;
		this.item = item;
		init();
	}

	private void init()
	{
		debug = false;
		visited = false;
		edges = new ArrayList<Edge>();
		xPos = (float) (Math.random() * 500) + 50;
		yPos = (float) (Math.random() * 500) + 50;
		forceVector = new ForceVector(0, 0);
	}

	public String getNodeKey()
	{
		return nodeKey;
	}

	public Item getItem()
	{
		return item;
	}

	// Checks to make sure that duplicate edges are handled
	// Adds edge to the list, then sorts by weight
	public void addEdge(Edge edge)
	{
		// TODO: Duplicate handling
		edges.add(edge);

		edges.sort(new Comparator<Edge>()
		{
			public int compare(Edge edge1, Edge edge2)
			{
				return Integer.compare((int) (edge1.getLength()), (int) (edge2.getLength()));
			}
		});
	}

	public ArrayList<Edge> getEdges()
	{
		return edges;
	}

	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public String toString()
	{
		String stringBuilder = "";

		stringBuilder += "[" + item.getValue() + "]";

		for (Edge edge : edges)
		{
			stringBuilder += "->" + edge.getEnd().getItem().getValue();
		}
		return stringBuilder;
	}

	public float getxPos()
	{
		return xPos;
	}

	public float getyPos()
	{
		return yPos;
	}

	public void setForceVector(ForceVector forceVector)
	{
		this.forceVector = forceVector;

		// System.out.println("IN VECTOR  - x: " + forceVector.getxForce() + " | y: " + forceVector.getyForce());
		// System.out.println("SET VECTOR - x: " + this.forceVector.getxForce() + " | y: " + this.forceVector.getyForce());
	}

	public boolean isConnected(Node node)
	{
		for (Edge edge : edges)
		{
			if (edge.getEnd().equals(node))
			{
				return true;
			}
		}
		return false;
	}

	public ForceVector getForceVector()
	{
		return forceVector;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public boolean contains(int x, int y, float xOffset, float yOffset)
	{
		float distance = (((xPos + xOffset)+(SIZE/2) - x) * ((xPos + xOffset)+(SIZE/2) - x)) + 
				(((yPos + yOffset)+(SIZE/2) - y) * ((yPos + yOffset)+(SIZE/2) - y));

		if (Math.sqrt(distance) < SIZE)
		{
			return true;
		}
		return false;
	}
	
	public void update()
	{
		this.xPos += (MOVE_SPEED * forceVector.getxForce());
		this.yPos += (MOVE_SPEED * forceVector.getyForce());
	}

	public void render(Graphics g, float offsetX, float offsetY)
	{
		g.setColor(Color.RED);
		g.drawOval((int) (xPos - (SIZE / 2) + offsetX), (int) (yPos - (SIZE / 2) + offsetY), SIZE, SIZE);
		g.setColor(Color.BLACK);
		//g.drawOval((int)(xPos - offsetX), (int)(yPos - offsetY), SIZE, SIZE);
		g.drawString(nodeKey, (int) (xPos + offsetX), (int) (yPos + offsetY));

		for (Edge edge : edges)
		{
			edge.render(g, offsetX, offsetY);
		}
		this.forceVector.render(g, xPos, yPos, offsetX, offsetY);

		if (true) // debug
		{
			DecimalFormat df = new DecimalFormat("#.##");
			g.setColor(new Color(255, 250, 216));
			g.fillRect((int) (xPos + offsetX), (int) (yPos + offsetY), 100, 75);
			g.setColor(Color.BLACK);
			g.drawRect((int) (xPos + offsetX), (int) (yPos + offsetY), 100, 75);
			
			g.drawString("X Force: " + df.format(this.forceVector.getxForce()), (int)(xPos+offsetX)+10, (int)(yPos+offsetY)+20);
			g.drawString("Y Force: " + df.format(this.forceVector.getyForce()), (int)(xPos+offsetX)+10, (int)(yPos+offsetY)+35);
			g.drawString("X Pos: " + df.format(xPos), (int)(xPos+offsetX)+10, (int)(yPos+offsetY)+50);
			g.drawString("Y Pos: " + df.format(yPos), (int)(xPos+offsetX)+10, (int)(yPos+offsetY)+65);
		}
	}
}
