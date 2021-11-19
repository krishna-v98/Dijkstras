import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Vertex {
    public String name; // Vertex name
    public Map<String, Vertex> adj; // Adjacent vertices
    public Vertex prev; // Previous vertex on shortest path
    public int dist; // Distance of path

    public Vertex(String nm) {
        name = nm; // constructor initialize name
        adj = new HashMap<String, Vertex>(); // create blank adj list
        reset(); // call reset below
    }

    public void reset() { // sets dist to largest int value
        dist = Graph.INFINITY;
        prev = null; // sets previous on the path to null
    }

}

public class Graph {
    public static final int INFINITY = Integer.MAX_VALUE; // sets variable infinity to int max value

    // hashmap of edges with string as concat of head, tail name and weight as float
    Map<String, Float> edgeMap = new HashMap<String, Float>();

    // hashmap of vertices with string as name of vertex and value as vertex object
    Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

    // list of edges that are down
    String[] downEdges = {};
    String[] downVertices = {};

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
        for (Vertex v : vertexMap.values()) { // iterate through hashmap
            System.out.print(v.name); // name
            System.out.print('\n');

            for (Vertex w : v.adj.values()) { // iterate current vertex's adj list
                float distance = edgeMap.get(v.name + w.name); // get value from edgemap for distance
                System.out.print("--> " + w.name + " " + String.valueOf(distance)); // print distance
                if (isDownEdge(v.name, w.name)) // check if edge is down
                    System.out.println("Down");
                System.out.print('\n');
            }

            System.out.print('\n');
            System.out.print('\n');
        }
    }

    // add an edge to graph
    public void addEdge(String tail, String head, float weight) {
        if (isVertex(head) && isVertex(tail)) { // check if given names are vertices in graph and proceed else print not
                                                // found

            Vertex v = getVertex(head);
            Vertex w = getVertex(tail);

            if (isEdge(head, tail)) { // check if it's an edge

                if (edgeMap.get(v.name + w.name) != weight) { // if already edge exists and weigth doesn't match with
                                                              // given input, update it
                    edgeMap.put(v.name + w.name, weight); // update
                    edgeMap.put(w.name + v.name, weight);
                }
            }
            if (!isEdge(head, tail)) { // if it's not an edge, add it to edgemap only in one direction
                edgeMap.put(head + tail, weight);
                v.adj.put(w.name, w); // put tail in head's adj list
                System.out.println(edgeMap.get(head + tail));

            }
            printGraph(); // call this to check
        } else
            System.out.println("1 or more Vertices not found");

    }

    // delete edge
    public void deleteEdge(String tail, String head) {
        if (isEdge(head, tail)) { // check if it's edge or print edge doesn't exist
            edgeMap.remove(head + tail); // remove entry from edgemap
            Vertex v = getVertex(head); // obtain head vertex
            v.adj.remove(tail); // remove tail from head's adj list
        } else
            System.out.println("Edge doesn't exist");
    }

    // check if edge is down
    public boolean isDownEdge(String head, String tail) {
        for (int i = 0; i < downEdges.length; i++) { // iterate through downedge string array
            if (downEdges[i] == head + tail) // check with each element
                return true; // return true of match -- end of function
        }
        return false;
    }

    // mark edge as down
    public void edgeDown(String tail, String head) {
        if (isEdge(head, tail)) { // check if it is an edge
            if (!isDownEdge(head, tail)) { // check if it's not already down
                int y = downEdges.length; // int as length of downedge array
                if (y == 0)
                    downEdges[0] = head + tail; // if y is 0 add first element
                else
                    downEdges[y - 1] = head + tail; // else add to the end of the array
            } else
                System.out.println("edge already down");
        } else
            System.out.println("Not an Edge");
    }

    // mark edge as not down
    public void edgeUp(String tail, String head) {
        if (isEdge(head, tail)) { // check if edge
            if (isDownEdge(head, tail)) {
                for (int i = 0; i < downEdges.length; i++) { // iterate though downedges
                    if (downEdges[i] == head + tail) {
                        downEdges[i] = "nil"; // marks down edge as nil
                        break;
                    }
                }
            } else
                System.out.println("Edge already active");
        } else
            System.out.println("not an edge");
    }

    // void processRequest(Scanner in, Graph g) {
    // System.out.println("Enter operation");
    // System.out.println("1.For add edge --> type addEdge");
    // String command = in.nextLine();

    // if (command == "addEdge") {
    // System.out.println("Enter 1st vertex");
    // String head = in.nextLine();

    // System.out.println("Enter 2nd vertex");
    // String tail = in.nextLine();

    // System.out.println("Enter weight");
    // String weight = in.nextLine();

    // addEdge(tail, head, Float.parseFloat(weight));
    // }
    // }

    public static void main(String[] args) throws Exception {
        // creates empty graph
        Graph g = new Graph();
        try {
            FileReader fin = new FileReader("input.txt");
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
                    g.addConnection(source, dest, weight);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping ill-formatted line " + line);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        g.printGraph();
        Scanner in = new Scanner(System.in);
        System.out.println("enter 1st Vertex");
        String head = in.nextLine();
        System.out.println("enter 2nd Vertex");
        String tail = in.nextLine();
        System.out.println("enter weight");
        String weight = in.nextLine();
        g.addEdge(tail, head, Float.parseFloat(weight));

        in.close();
    }
}
