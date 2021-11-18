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

    // takes vertex and adds it to the vertex map if it isn't already present
    private Vertex getVertex(String vertexName) {
        Vertex x = vertexMap.get(vertexName);
        if (x == null) {
            x = new Vertex(vertexName);
            vertexMap.put(vertexName, x);
        }
        return x;
    }

    boolean isEdge(String head, String tail) {
        if (edgeMap.containsKey(head + tail)) {
            return true;
        } else
            return false;
    }

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

    public void printGraph() {
        for (Vertex v : vertexMap.values()) {
            System.out.print(v.name);
            System.out.print('\n');

            for (Vertex w : v.adj.values()) {
                float distance = edgeMap.get(v.name + w.name);
                System.out.print("--> " + w.name + " " + String.valueOf(distance));
                if (isDownEdge(v.name, w.name))
                    System.out.println("Down");
                System.out.print('\n');
            }

            System.out.print('\n');
            System.out.print('\n');
        }
    }

    public void addEdge(String tail, String head, float weight) {
        if (isVertex(head) && isVertex(tail)) {

            Vertex v = getVertex(head);
            Vertex w = getVertex(tail);

            if (isEdge(head, tail)) {

                if (edgeMap.get(v.name + w.name) != weight) {
                    edgeMap.put(v.name + w.name, weight);
                    edgeMap.put(w.name + v.name, weight);
                }
            }
            if (!isEdge(head, tail)) {
                edgeMap.put(head + tail, weight);
                v.adj.put(w.name, w);
                System.out.println(edgeMap.get(head + tail));

            }
            printGraph();
        } else
            System.out.println("1 or more Vertices not found");

    }

    public void deleteEdge(String tail, String head) {
        if (isEdge(head, tail)) {
            edgeMap.remove(head + tail);
            Vertex v = getVertex(head);
            v.adj.remove(tail);
        } else
            System.out.println("Edge doesn't exist");
    }

    public boolean isDownEdge(String head, String tail) {
        for (int i = 0; i < downEdges.length; i++) {
            if (downEdges[i] == head + tail)
                return true;
        }
        return false;
    }

    public void edgeDown(String tail, String head) {
        if (!isDownEdge(head, tail)) {
            if (isEdge(head, tail)) {
                int y = downEdges.length;
                if (y == 0)
                    downEdges[0] = head + tail;
                else
                    downEdges[y - 1] = head + tail;
            }
        }
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
