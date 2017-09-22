package Graph;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import Utilities.ForceVector;

public class Graph
{
	private float xOffset;
	private float yOffset;

	private int maxNodes;

	private ArrayList<Node> nodes;
	private LinkedList<Node> dfs_linked;
	private LinkedList<Node> bfs_linked;

	public Graph(int maxNodes)
	{
		this.maxNodes = maxNodes;
		nodes = new ArrayList<Node>();
	}

	public void init(String graphName)
	{
		String[] nodesFromFile = readInFile(graphName + "-Nodes");
		String[] edgesFromFile = readInFile(graphName + "-Edges");

		for (int i = 0; i < nodesFromFile.length; i++)
		{
			addNode(new Item(nodesFromFile[i].replaceAll(" ", "")));
		}
		for (int i = 0; i < edgesFromFile.length; i++)
		{
			String[] row = edgesFromFile[i].split(" ");

			addEdge(row[0], row[1], Integer.parseInt(row[2]), false);
		}
	}

	public void addNode(Item item)
	{
		if (nodes.size() < maxNodes)
		{
			nodes.add(new Node(item, item.getValue()));
		}
		else
		{
			System.out.println("Too many nodes added");
		}
	}

	public void addEdge(String startNodeKey, String endNodeKey, float edgeWeight, boolean directed)
	{
		Node startNode = getNodeByNodeKey(startNodeKey);
		Node endNode = getNodeByNodeKey(endNodeKey);

		if (directed)
		{
			startNode.addEdge(new Edge(startNode, endNode, edgeWeight));
		}
		else
		{
			startNode.addEdge(new Edge(startNode, endNode, edgeWeight));
			endNode.addEdge(new Edge(endNode, startNode, edgeWeight));
		}
	}

	private Node getNodeByNodeKey(String nodeKey)
	{
		for (Node node : nodes)
		{
			if (node.getNodeKey().equals(nodeKey))
			{
				return node;
			}
		}
		System.out.println("Node: " + nodeKey + " -> not found");
		return null;
	}

	// Returns the lines of the file as elements in a String[]
	private String[] readInFile(String fileName)
	{
		ArrayList<String> fileLines = new ArrayList<String>();

		FileReader fileReader = null;

		try
		{
			fileReader = new FileReader(new File("res/" + fileName + ".txt"));
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File not found in resource folder");
		}
		BufferedReader reader = new BufferedReader(fileReader);

		try
		{
			String line = reader.readLine();

			while (line != null)
			{
				fileLines.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		String[] fileRead = new String[fileLines.size()];

		return fileLines.toArray(fileRead);
	}

	public LinkedList<Node> dfs()
	{
		dfs_linked = new LinkedList<Node>();
		_dfs(nodes.get(0));

		resetNodes();

		return dfs_linked;
	}

	// Time complexity : O(n), where n = (|nodes| + |edges|)
	private void _dfs(Node node)
	{
		if (node == null)
		{
			return;
		}
		node.setVisited(true);
		dfs_linked.add(node);

		for (Edge edge : node.getEdges())
		{
			if (!edge.getEnd().isVisited())
			{
				_dfs(edge.getEnd());
			}
		}
	}

	public LinkedList<Node> bfs()
	{
		bfs_linked = new LinkedList<Node>();
		_bsf(nodes.get(0));

		resetNodes();

		return bfs_linked;
	}

	private void _bsf(Node source)
	{
		LinkedList<Node> nextToVisit = new LinkedList<Node>();

		nextToVisit.add(source);

		while (!nextToVisit.isEmpty())
		{
			Node node = nextToVisit.remove();

			if (node.isVisited())
			{
				continue;
			}
			node.setVisited(true);
			bfs_linked.add(node);

			for (Edge edge : node.getEdges())
			{
				nextToVisit.add(edge.getEnd());
			}
		}
	}

	// Resets all the "visited tags" for the nodes to false
	public void resetNodes()
	{
		for (Node node : nodes)
		{
			node.setVisited(false);
		}
	}

	// Matrix representation of the graphs
	// AKA, ugliest code I've written to date
	public String toString()
	{
		String stringBuilder = "    ";

		for (int i = 0; i < nodes.size(); i++)
		{
			int nodeKey = Integer.parseInt(nodes.get(i).getNodeKey());
			stringBuilder += nodeKey + ((nodeKey < 10) ? " " : "");
		}
		stringBuilder += "\n";

		for (int i = 0; i < nodes.size(); i++)
		{
			int nodeKey = Integer.parseInt(nodes.get(i).getNodeKey());
			stringBuilder += nodeKey + ((nodeKey < 10) ? " | " : "| ");

			for (int h = 0; h < nodes.size(); h++)
			{
				if (nodesConnected(nodes.get(i), nodes.get(h)))
				{
					stringBuilder += "1 ";
				}
				else
				{
					stringBuilder += "- ";
				}
			}
			stringBuilder += "\n";
		}
		return stringBuilder;
	}

	private boolean nodesConnected(Node iNode, Node hNode)
	{
		for (Edge edge : iNode.getEdges())
		{
			if (edge.getEnd().equals(hNode))
			{
				return true;
			}
		}
		return false;
	}

	public void update()
	{
		float K = 50.0f; // Natural Spring Length
		float C = 1.0f; // Relative Attraction/Repulsion Force

		ForceVector resultVector = new ForceVector();

		for (Node iNode : nodes)
		{
			resultVector.setVector(0, 0);

			for (Node jNode : nodes)
			{
				if (iNode == jNode)
				{
					continue;
				}
				resultVector.addVector(calculateRepelVector(iNode, jNode, K, C));

				if (iNode.isConnected(jNode))
				{
					resultVector.addVector(calculateAttractVector(iNode, jNode, K));
				}
			}
			System.out.println("RESULT -- i: " + iNode.getNodeKey() + " | x: " + resultVector.getxForce() + " | y: "
					+ resultVector.getyForce());
			iNode.setForceVector(resultVector);
		}
		System.out.println("------------------------------------------------------------------------");
		for (Node node : nodes)
		{
			node.update();
		}
	}

	// F[r](i, j) = (-C*K*K)/(|x(i) - x(j)|)
	// Where i, j are nodes, i != j,
	// x(n) is the position of the node n,
	// C is relative strength of attraction,
	// K is the natural spring length
	private ForceVector calculateRepelVector(Node iNode, Node jNode, float K, float C)
	{
		float xForce = (-C * K * K) / Math.abs(iNode.getxPos() - jNode.getxPos());
		float yForce = (-C * K * K) / Math.abs(iNode.getyPos() - jNode.getyPos());

		// System.out.println("REPEL - i: " + iNode.getNodeKey() + " | j: " +
		// jNode.getNodeKey() + " | xPos: " + iNode.getxPos() + " | yPos: " +
		// iNode.getyPos());
		// System.out.println("REPEL - i: " + iNode.getNodeKey() + " | j: " +
		// jNode.getNodeKey() + " | x: " + xForce + " | y: " + yForce);

		return new ForceVector(xForce, yForce);
	}

	// F[a](i, j) = ((x(i)-x(j))*(x(i)-x(j)))/K
	// Where x(n) is the position of the node n,
	// K is the natural spring length,
	// Nodes i, j are connected via an edge (i<->j)
	private ForceVector calculateAttractVector(Node iNode, Node jNode, float K)
	{
		float xDistance = iNode.getxPos() - jNode.getxPos();
		float yDistance = iNode.getyPos() - jNode.getyPos();
		
		float xForce = (xDistance * xDistance) / K;
		float yForce = (yDistance * yDistance) / K;

		// System.out.println("ATTRACT - i: " + iNode.getNodeKey() + " | j: " +
		// jNode.getNodeKey() + " | xPos: " + iNode.getxPos() + " | yPos: " +
		// iNode.getyPos());
		// System.out.println("ATTRACT - i: " + iNode.getNodeKey() + " | j: " +
		// jNode.getNodeKey() + " | x: " + xForce + " | y: " + yForce);

		return new ForceVector(xForce, yForce);
	}

	public void debug(int x, int y)
	{
		for (Node node : nodes)
		{
			if (node.contains(x, y, xOffset, yOffset))
			{
				node.setDebug(true);
			}
			else
			{
				node.setDebug(false);
			}
		}
	}

	public void render(Graphics g, float offsetX, float offsetY)
	{
		this.xOffset = offsetX;
		this.yOffset = offsetY;

		for (Node node : nodes)
		{
			node.render(g, offsetX, offsetY);
		}
	}
}
