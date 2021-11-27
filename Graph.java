import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

//A class for the Vertices and their information

class Vertex {
	public String name; // Vertex name
	public Map<String, Vertex> adj; // Adjacent vertices
	public Vertex prev; // Previous vertex on shortest path
	public float dist; // Distance of path

	public Vertex(String nm) {
		name = nm; // constructor initialize name
		adj = new HashMap<String, Vertex>(); // create blank adj list
		reset(); // call reset below
	}

	public void reset() { // sets dist to largest int value
		dist = Graph.INFINITY;
		prev = null; // sets previous on the path to null
	}

	public float getDist() { //getter to get distance
		return dist;
	}

}

// Graph class with all the functions and main methods.

public class Graph {
	public static final float INFINITY = Float.MAX_VALUE; // sets variable infinity to int max value

	// hashmap of edges with string as concat of head, tail name and weight as
	// float
	Map<String, Float> edgeMap = new HashMap<String, Float>();

	// hashmap of vertices with string as name of vertex and value as vertex object
	Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

	// list of edges that are down
	List<String> downEdges = new ArrayList<String>();
	List<String> downVertices = new ArrayList<String>();

	List<String> rechableVerticesforVertex = new ArrayList<String>();

	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}

	// takes vertex and adds it to the vertex map if it isn't already present
	private Vertex getVertex(String vertexName) {
		Vertex x = vertexMap.get(vertexName);
		if (x == null) {
			x = new Vertex(vertexName);
			vertexMap.put(vertexName, x);
		}
		return x;
	}

	// check if given vertices have an edge
	boolean isEdge(String head, String tail) {
		if (edgeMap.containsKey(head + tail)) {
			return true;
		} else
			return false;
	}

	// check if given name is a vertex
	boolean isVertex(String v) {
		Vertex x = vertexMap.get(v);
		if (x == null)
			return false;
		else
			return true;
	}

	// adds a link between 2 vertices, takes source and dest names as string along
	// with weight
	public void addConnection(String sourceName, String destName, String weight) {
		Vertex v = getVertex(sourceName); // copies vertex obj with associated from hashmap into v
		Vertex w = getVertex(destName); // same as above for w
		v.adj.put(w.name, w); // adds w to adj list of v
		w.adj.put(v.name, v); // adds v to adj list of w
		insertEdge(v.name, w.name, weight); // calls insertedge
	}

	// adds entry into edgeMap hashmap which is a list of edges
	public void insertEdge(String head, String tail, String weight) {
		edgeMap.put(head + tail, Float.parseFloat(weight));
		/*
		 * puts an edge from head to tail along with weight (data entry into hashmap,
		 * key is concat of strings of names of head and tail)
		 */
		edgeMap.put(tail + head, Float.parseFloat(weight));
		/* same as above but reverse --> replicating the bidirectional theme */
	}

	// prints entire graph
	public void printGraph() {

		// stores in alphabetical order, no duplicates
		TreeSet<String> vertices = new TreeSet<String>();

		for (Vertex v : vertexMap.values()) {
			vertices.add(v.name);
		}

		// iteration to print
		for (String v : vertices) {
			System.out.print(v);

			if (isDownVertex(v))
				System.out.print(" DOWN");

			System.out.println();
			TreeSet<String> adjList = new TreeSet<String>();

			for (Vertex w : getVertex(v).adj.values()) {
				adjList.add(w.name);
			}

			for (String w : adjList) {
				float distance = edgeMap.get(v + w);

				System.out.print("  " + w + " " + String.valueOf(distance));
				if (isDownEdge(v, w))
					System.out.print(" DOWN");
				System.out.println();
			}

		}

	}

	// add an edge to graph
	public void addEdge(String tail, String head, float weight) {

		Vertex v = getVertex(tail);
		Vertex w = getVertex(head);

		if (isEdge(tail, head)) { // check if it's an edge

			if (edgeMap.get(v.name + w.name) != weight) { // if already edge exists and weigth doesn't match with

				edgeMap.put(v.name + w.name, weight); // update
			}
		} else { // if it's not an edge, add it to edgemap only in one direction
			edgeMap.put(tail + head, weight);
			v.adj.put(w.name, w); // put tail in head's adj list

		}

	}

	// delete edge
	public void deleteEdge(String tail, String head) {
		if (isEdge(tail, head)) { // check if it's edge or print edge doesn't exist
			edgeMap.remove(tail + head); // remove entry from edgemap
			Vertex v = getVertex(tail); // obtain head vertex
			v.adj.remove(head); // remove tail from head's adj list
		} else
			System.out.println("Edge doesn't exist");
	}

	// check if edge is down
	public boolean isDownEdge(String head, String tail) {
		// iterate through downedge string array
		if (downEdges.contains(head + tail)) // check with each element
			return true; // return true of match -- end of function
		else
			return false;
	}

	// check if vertex is down
	public boolean isDownVertex(String vertex) {

		if (downVertices.contains(vertex)) // check with each element
			return true; // return true of match -- end of function
		else
			return false;
	}

	// mark edge as down
	public void edgeDown(String tail, String head) {
		if (isEdge(tail, head)) { // check if it is an edge
			if (!isDownEdge(tail, head)) { // check if it's not already down
				downEdges.add(tail + head);// else add to the end of the array
			} else
				System.out.println("edge already down");
		} else
			System.out.println("Not an Edge");
	}

	// mark edge as not down
	public void edgeUp(String tail, String head) {
		if (isEdge(tail, head)) { // check if edge
			if (isDownEdge(tail, head)) {
				downEdges.remove(tail + head); // remove from down list
			} else
				System.out.println("Edge already active");
		} else
			System.out.println("not an edge");
	}

	// mark vertex as down
	public void vertexDown(String vertex) {
		if (isVertex(vertex)) {
			if (!isDownVertex(vertex)) {
				downVertices.add(vertex); // add to down list
			} else
				System.out.println("vertex already down");

		} else
			System.out.println("vertex not present");
	}

	//reactivates down vertex
	public void vertexUp(String vertex) {
		if (isVertex(vertex)) {
			if (isDownVertex(vertex)) {
				downVertices.remove(vertex); // remove from down list
			} else {
				System.out.println("vertex already active");
			}
		} else
			System.out.println("Not a vertex");
	}

	// Start shortest path algorithm
	public void dijkstra(String start, String end) {

		if (isVertex(start) && isVertex(end)) { // check if they are vertices and if they are down or not
			if (isDownVertex(start) || isDownVertex(end)) {
				System.out.println("one of the vertices unavailable");
			} else {
				// DIJKSTRA'S BEGIN
				clearAll(); // all distances infinite and all prev nill
				Vertex startVertex = getVertex(start);
				Vertex endVertex = getVertex(end);

				startVertex.dist = 0; // set source distance to 0

				PriorityQueue<Vertex> distances = new PriorityQueue<>((v1, v2) -> {
					if (v1.getDist() > v2.getDist())
						return 1;
					else if (v1.getDist() < v2.getDist())
						return -1;
					return 0;
				}); // create priority Queue (min binary heap) which sorts based on distance

				distances.add(startVertex); // add source to min-heap

				while (distances.size() > 0) {
					Vertex u = distances.poll(); // retrieves top of the heap (the min element)

					for (Vertex v : u.adj.values()) {

						// skips for down vertices and edges

						if (isDownVertex(v.name))
							continue;

						if (isDownEdge(u.name, v.name))
							continue;

						if (v.dist > u.dist + edgeMap.get(u.name + v.name)) {
							// update distances based on shortest path
							v.dist = u.dist + edgeMap.get(u.name + v.name);
							v.prev = u;
							distances.add(v);
						}
					}

				}
				printPath(endVertex); // prints path from start to end
				System.out.print(" ");

				// helper lines to print only to 2 point accuracy after decimal
				String weightOutput = String.valueOf(sumWeight(endVertex));
				int dotIndex = weightOutput.indexOf(".");
				int lastIndex = 0;
				if (weightOutput.length() > dotIndex + 2) {
					lastIndex = dotIndex + 2;
				} else if (weightOutput.length() > dotIndex + 1) {
					lastIndex = dotIndex + 1;
				} else {
					lastIndex = dotIndex;
				}
				System.out.print(
						weightOutput.substring(0, dotIndex) + "." + weightOutput.substring(dotIndex + 1, lastIndex));

			}
		} else
			System.out.println("vertices not found");
	}

	// iteration to sum the weights from start to end
	float sumWeight(Vertex dest) {
		float sum = 0;
		while (dest.prev != null) {
			sum += edgeMap.get(dest.prev.name + dest.name);
			dest = dest.prev;
		}
		return sum;
	}

	// a recursive call to print the path from start to end provided to dijkstras
	void printPath(Vertex dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
		}
		System.out.print(" " + dest.name);
	}

	// function based on DFS to print all the reachable vertices from a vertex
	// (depending on unavailable edges and vertices in graph)
	// it has time complexity of O(V + E)
	public void reachable() {
		for (Vertex v : vertexMap.values()) {
			if (!isDownVertex(v.name)) {
				rechableVerticesforVertex.add(v.name);
				rechableVerticesforVertex.clear();
				System.out.println();
				System.out.println(v.name);
				rechableFromVertex(v);
			}

		}
	}

	private void rechableFromVertex(Vertex v) {
		for (Vertex w : v.adj.values()) {
			if (isDownVertex(w.name) || isDownEdge(v.name, w.name))
				continue;
			else if (rechableVerticesforVertex.contains(w.name))
				return;
			else {
				System.out.println("  " + w.name);
				rechableVerticesforVertex.add(w.name);
				rechableFromVertex(w);
			}
		}
	}

	// main method reads input from text file and adds to the graph.

	public static void main(String[] args) throws Exception {
		// creates empty graph
		Graph g = new Graph();
		try {
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);

			// Read the edges and insert
			String line;
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					String weight = st.nextToken();
					g.addConnection(source, dest, weight); //function adds edge between given input in the text file
				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line " + line);
				}
			}
			graphFile.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		//a loop to keep requesting user input until the user enters "quit" which ends the loop.

		while (true) {
			System.out.println();
			Scanner in = new Scanner(System.in);
			String input = in.nextLine();
			String[] split = input.split(" ");

			if (split[0].equalsIgnoreCase("print"))
				g.printGraph();

			else if (split[0].equalsIgnoreCase("path"))
				g.dijkstra(split[1], split[2]);

			else if (split[0].equalsIgnoreCase("edgedown"))
				g.edgeDown(split[1], split[2]);

			else if (split[0].equalsIgnoreCase("vertexdown"))
				g.vertexDown(split[1]);

			else if (split[0].equalsIgnoreCase("vertexup"))
				g.vertexUp(split[1]);

			else if (split[0].equalsIgnoreCase("reachable"))
				g.reachable();

			else if (split[0].equalsIgnoreCase("edgeup"))
				g.edgeUp(split[1], split[2]);

			else if (split[0].equalsIgnoreCase("deleteedge"))
				g.deleteEdge(split[1], split[2]);

			else if (split[0].equalsIgnoreCase("addedge"))
				g.addEdge(split[1], split[2], Float.parseFloat(split[3]));

			else if (split[0].equalsIgnoreCase("quit"))
				break;
		}
	}
}
