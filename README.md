## Dijkstra's Algorithm implementation

## Design

This program is written in JAVA, compiled in JAVA compiler.

This program only has a single file.

This program has 2 classes the Graph class and Vertex class.
Vertex class has name, weight, adjacent list, distance variables. Also a constructor and reset function. To store edges a Hashmap has been used that stores keys as a concat of names of the vertices and the value as the weight of the vertex, with unique entries based on direction and since it is a hashmap it can be easily traversed with in built functions.

Graph class has all the main logic and the main methods of the program.

It has data structures to store vertexMap, edgeMap, downVertices & downEdges.

vertexMap is hashmap that stores the name of a vertex and the vertex object info in key value pairs.
edgeMap is a hashmap that stores concat of names of vertices to represent an edge between them and weight as key-value pairs.

## Functions

The clear all function calls reset function on all the vertex which will be used every time Dijkstra’s implemented.

getVertex returns a vertex, either creates a new one if unavailable or returns existing one.

isEdge and isVertex are boolean functions which tells if it's an edge or a vertex, these are helper functions.

addConnection adds an edge between 2 vertices(bi-directional) and adds it to each other's adjacency list, this is done and called only when the file is read. 

Print graph has treeset data structures to store strings which are names of vertices and retrieved as they'll be sorted by treeset. An iteration is called over the vertex map and if condition prints DOWN if a vertex is marked down and another inner loop prints all adjacency list vertices and if the connection between them is active or not (prints "DOWN").

addEdge retrieves string and calls getvertex on them, updates weight if it already exits or adds new edge if it's unavailable.

deleteEdge removes the given head an tail from the list of edges.

isDownEdge and isDownVertex are helper function to determine if they are active or inactive.

EdgeDown declares edge as down by adding it to downedges list.
edgeUp reverses it.
vertexdown does the same as edgedown but for vertices.
vertexUp reverses it.

# Dijkstra's
The dijkstra function finds the shortest path using the dijkstras algorithm.
Uses an inbuilt priorityqueue that sorts vertices based on their distance variable value. Skips edges or evrtices that are made unavailable using the edgeDown and vertexDown functions.
In the end a recursive function with endvertex as paramater calculates total weigth and prints shortest path generated using dijkstras.

# Reachable
Reachable and reachablefrom are based on DFS. Used to find all the reachable vertices from a vertex. has a time complexity of O(V + E). The reachable vertices function doesn't print the list in an alphabetical order.

The main method has functions to read the input file provided as cmd line argument, split it and provide it as string input to the program, followed by a loop that takes user input which ends upon giving "quit" as input.