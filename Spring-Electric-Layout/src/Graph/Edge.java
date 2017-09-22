package Graph;

import java.awt.Color;
import java.awt.Graphics;

public class Edge
{
	private float length;
	
	private Node start;
	private Node end;

	public Edge(Node start, Node end, float length)
	{
		this.length = length;
		this.start = start;
		this.end = end;
	}
	// Returns the length of the edge
	public float getLength()
	{
		return length;
	}
	// Returns the start node of the edge
	public Node getStart()
	{
		return start;
	}
	// Returns the destination of the edge
	public Node getEnd()
	{
		return end;
	}
	public void render(Graphics g, float offsetX, float offsetY)
	{
		g.setColor(Color.BLUE);
		g.drawLine((int)(start.getxPos()+offsetX), (int)(start.getyPos()+offsetY), (int)(end.getxPos()+offsetX), (int)(end.getyPos()+offsetY));
	}
}
