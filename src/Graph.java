import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;

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

    public float getDist() {
        return dist;
    }

}

public class Graph {
    public static final int INFINITY = Integer.MAX_VALUE; // sets variable infinity to int max value

    // hashmap of edges with string as concat of head, tail name and weight as float
    Map<String, Float> edgeMap = new HashMap<String, Float>();

    // hashmap of vertices with string as name of vertex and value as vertex object
    Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

    // list of edges that are down
    List<String> downEdges = new ArrayList<String>();
    List<String> downVertices = new ArrayList<String>();

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
        for (Vertex v : vertexMap.values()) { // iterate through hashmap

            System.out.print(v.name);
            if (isDownVertex(v.name))
                System.out.println("  Down");

            System.out.print('\n');

            for (Vertex w : v.adj.values()) { // iterate current vertex's adj list
                float distance = edgeMap.get(v.name + w.name); // get value from edgemap for distance

                System.out.print("--> " + w.name + " " + String.valueOf(distance)); // print distance
                if (isDownEdge(v.name, w.name)) // check if edge is down
                    System.out.print("  Down");

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
        if (isEdge(head, tail)) { // check if it is an edge
            if (!isDownEdge(head, tail)) { // check if it's not already down
                downEdges.add(head + tail);// else add to the end of the array
            } else
                System.out.println("edge already down");
        } else
            System.out.println("Not an Edge");
    }

    // mark edge as not down
    public void edgeUp(String tail, String head) {
        if (isEdge(head, tail)) { // check if edge
            if (isDownEdge(head, tail)) {
                downEdges.remove(head + tail);
            } else
                System.out.println("Edge already active");
        } else
            System.out.println("not an edge");
    }

    // mark vertex as down
    public void vertexDown(String vertex) {
        if (isVertex(vertex)) {
            if (!isDownVertex(vertex)) {
                downVertices.add(vertex);
            } else
                System.out.println("vertex already down");

        } else
            System.out.println("vertex not present");
    }

    public void vertexUp(String vertex) {
        if (isVertex(vertex)) {
            if (isDownVertex(vertex)) {
                downVertices.remove(vertex);
            }
            System.out.println("vertex already active");
        } else
            System.out.println("Not a vertex");
    }

    public void dijkstra(String start, String end) {

        if (isVertex(start) && isVertex(end)) {
            if (isDownVertex(start) || isDownVertex(end)) {
                System.out.println("one of the vertices unavailable");
            } else {
                // DIJKSTRA'S BEGIN
                clearAll(); // all distances infinite and all prev nill
                Vertex startVertex = getVertex(start);
                Vertex endVertex = getVertex(end);

                startVertex.dist = 0;

                PriorityQueue<Vertex> distances = new PriorityQueue<>((v1, v2) -> {
                    if (v1.getDist() > v2.getDist())
                        return 1;
                    else if (v1.getDist() < v2.getDist())
                        return -1;
                    return 0;
                });

                for (Vertex v : vertexMap.values()) {
                    if (isDownVertex(v.name))
                        continue;
                    distances.add(v);
                }

                while (!distances.isEmpty()) {
                    Vertex u = distances.poll();

                    for (Vertex v : u.adj.values()) {
                        if (v.dist > u.dist + edgeMap.get(u.name + v.name)) {
                            if (isDownEdge(u.name, v.name))
                                continue;
                            v.dist = u.dist + edgeMap.get(u.name + v.name);
                            v.prev = u;
                        }
                    }
                }

                printPath(endVertex);
                System.out.print(" ");
                System.out.print(String.valueOf(sumWeight(endVertex)));

            }
        } else
            System.out.println("vertices not found");
    }

    float sumWeight(Vertex dest) {
        float sum = 0;
        while (dest.prev != null) {
            sum += edgeMap.get(dest.prev.name + dest.name);
            dest = dest.prev;
        }
        return sum;
    }

    void printPath(Vertex dest) {
        if (dest.prev != null) {
            printPath(dest.prev);
        }
        System.out.print(" " + dest.name);
    }

    public void reachable() {
        for (Vertex v : vertexMap.values()) {

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
            FileReader fin = new FileReader("Network.txt");
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
        // System.out.println("---------------------------------------------------------");
        // g.addEdge("Duke", "Health", 10);
        // System.out.println("\n");
        // System.out.println("---------------------------------------------------------");
        // g.printGraph();
        // System.out.println("---------------------------------------------------------");
        // g.addEdge("Woodward", "Health", 5);
        // System.out.println("---------------------------------------------------------");
        // g.printGraph();
        // System.out.println("--------------------------------------------------------");
        // g.edgeDown("Education", "Health");
        // g.edgeDown("Belk", "Health");
        // g.vertexDown("Health");
        // g.vertexDown("Duke");
        // System.out.println("marked edges & vertices down");
        // System.out.println("---------------------------------------");
        // g.printGraph();
        g.vertexDown("Woodward");
        g.edgeDown("Duke", "Belk");
        g.dijkstra("Belk", "Duke");
    }
}
