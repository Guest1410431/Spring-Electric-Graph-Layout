package Utilities;

import java.awt.Color;
import java.awt.Graphics;

public class ForceVector
{
	private float xForce;
	private float yForce;
	
	public ForceVector()
	{
		xForce = 0;
		yForce = 0;
	}	
	public ForceVector(float xForce, float yForce)
	{
		this.xForce = xForce;
		this.yForce = yForce;
	}

	public float getxForce()
	{
		return xForce;
	}

	public float getyForce()
	{
		return yForce;
	}
	
	public void setVector(float xForce, float yForce)
	{
		this.xForce = xForce;
		this.yForce = yForce;
	}
	public void render(Graphics g, float xPos, float yPos, float xOffset, float yOffset)
	{
		g.setColor(Color.GREEN);
		g.drawLine((int)(xPos + xOffset), (int)(yPos + yOffset), (int)((xPos + xOffset) + xForce*10), (int)((yPos + yOffset) + yForce*10));
	}
	public void addVector(ForceVector vector)
	{
		this.xForce += vector.xForce;
		this.yForce += vector.yForce;
	}
}
