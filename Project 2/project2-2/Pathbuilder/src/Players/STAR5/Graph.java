package Players.STAR5;
import Interface.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;


/**
 * Graph class. Holds representation of a graph as well as functions to
 * interact with the graph.
 *
 * @author atd Aaron T Deever
 * @author sps Sean Strout
 * edited by Noor Mohammad
 */
public class Graph extends HashMap<Coordinate,Node> {
    /** graph is represented using a map (dictionary) */
    private Map<Coordinate,Node> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    /**
     * Method to generate a string associated with the graph.  The string
     * comprises one line for each node in the graph. Overrides
     * Object toString method.
     *
     * @return string associated with the graph.
     */
    public String toString() {
        String result = "";
        for (Coordinate coordinate : this.keySet()) {
            result = result + this.get(coordinate) + "\n";
        }
        return result;
    }

    /**
     * Method to check if a given String node is in the graph.
     * @param coordinate - the coordinate of the node
     * @return boolean true if the graph contains that key; false otherwise
     */
    public boolean isInGraph(Coordinate coordinate) {
        return this.containsKey(coordinate);
    }

    /**
     * For a given start and finish node, we simply want to know whether
     * a path exists, or not, between them.  This is the precursor to
     * searchBFS().
     * @param start the coordinate associated with the node from which
     *              to start the search
     * @param finish the coordinate associated with the destination node
     * @return boolean true if a path exists; false otherwise
     */
    public boolean canReachBFS(Coordinate start, Coordinate finish) {
        // assumes input check occurs previously
        Node startNode, finishNode;
        startNode = this.get(start);
        finishNode = this.get(finish);

        // use an array list in the fashion of a queue
        List<Node> queue = new ArrayList<>();

        // prime the queue with the starting node
        queue.add(startNode);

        // create a visited set to prevent cycles
        Set<Node> visited = new HashSet<>();
        // add start node to it
        visited.add(startNode);

        // loop until either the finish node is found (path exists), or the
        // queue is empty (no path)
        while (!queue.isEmpty()) {
            Node current = queue.remove(0);
            if (current == finishNode) {
                return true;
            }
            // loop over all neighbors of current
            for (Node nbr : current.getNeighbors()) {
                // process unvisited neighbors
                if (!visited.contains(nbr)) {
                    visited.add(nbr);
                    queue.add(nbr);
                }
            }
        }
        return false;
    }

    /**
     * Method that visits all nodes reachable from the given starting node
     * in breadth-first search fashion using a queue, stopping only if the
     * finishing node is reached or the search is exhausted. A predecessors
     * map keeps track of which nodes have been visited and along what path
     * they were first reached.
     *
     * @param start the coordinate associated with the node from which
     *              to start the search
     * @param finish the coordinate associated with the destination node
     * @return path the path from start to finish. Empty if there is no
     *         such path.
     *
     * Precondition: the inputs correspond to nodes in the graph.
     */
    public List<Node> searchBFS(Coordinate start, Coordinate finish) {

        // assumes input check occurs previously
        Node startNode, finishNode;
        startNode = this.get(start);
        finishNode = this.get(finish);

        // prime the queue with the starting node
        List<Node> queue = new LinkedList<>();
        queue.add(startNode);

        // construct the predecessors data structure
        Map<Node, Node> predecessors = new HashMap<>();
        // put the starting node in, and just assign itself as predecessor
        predecessors.put(startNode, startNode);

        // loop until either the finish node is found, or the
        // dispenser is empty (no path)
        while (!queue.isEmpty()) {
            Node current = queue.remove(0);
            if (current == finishNode) {
                break;
            }
            // loop over all neighbors of current
            for (Node nbr : current.getNeighbors()) {
                // process unvisited neighbors
                if(!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }

        return constructPath(predecessors, startNode, finishNode);
    }

    /**
     * Method to return a path from the starting to finishing node.
     *
     * @param predecessors Map used to reconstruct the path
     * @param startNode starting node
     * @param finishNode finishing node
     * @return a list containing the sequence of nodes comprising the path.
     * An empty list if no path exists.
     */
    private List<Node> constructPath(Map<Node,Node> predecessors,
            Node startNode, Node finishNode) {
        // use predecessors to work backwards from finish to start,
        // all the while dumping everything into a linked list
        List<Node> path = new LinkedList<>();

        if(predecessors.containsKey(finishNode)) {
            Node currNode = finishNode;
            while (currNode != startNode) {
                path.add(0, currNode);
                currNode = predecessors.get(currNode);
            }
            path.add(0, startNode);
        }

        return path;
    }
    /**
     * Method to compute and display the shortest path in a weighted graph
     * from a start node to a finish node.
     *
     * Precondition: the inputs correspond to nodes in the graph.
     *
     * @param start String name of starting node
     * @param finish String name of finishing node
     *
     */

    public int displayShortestPath(Coordinate start, Coordinate finish) {

        // assumes input check occurs previously
        Node startNode, finishNode;
        startNode = this.get(start);
        finishNode = this.get(finish);

        // create a distance map that will hold the shortest path distance
        // to each node from the given startNode.  We will just use the
        // maximum Integer value to represent infinity
        Map<Node, Integer> distance = new HashMap<Node, Integer>();

        // create a predecessor map that will be used to determine
        // the shortest path to each node from the given startNode.
        // If a node is not yet in the map, that is equivalent to the
        // node not having a predecessor, and not being reachable.
        Map<Node, Node> predecessors = new HashMap<Node, Node>();

        dijkstra(startNode, distance, predecessors);

        if(distance.get(finishNode) == Integer.MAX_VALUE) {
//            System.out.println("No path from " + start + " to " + finish);
        }
        else {
//            System.out.println("Minimum distance between " + start + " and " +
//                    finish + " is " + String.valueOf(distance.get(finishNode)));
            return (distance.get(finishNode));
//            List<Node> path = new LinkedList<Node>();
//            Node n = finishNode;
//            while (!n.equals(startNode)) {
//                path.add(0, n);
//                n = predecessors.get(n);
//            }
//            path.add(0, startNode);
//
//            System.out.print("Shortest path: ");
//            for(Node n1 : path) {
//                System.out.print(n1.toString() + " ");
//            }
//            System.out.println();
        }
        return 0;
    }

    /**
     * Dijkstra's algorithm.  Given a weighted graph, and a starting node, computes
     * the shortest path to all other nodes (some may be unreachable).
     *
     * Precondition:  assumes weights are non-negative
     *
     * @param startNode:  the starting node
     * @param distance:  map that holds the minimum distance to any node
     * @param predecessors: map holds predecessor nodes along any shortest path
     */
    private void dijkstra(Node startNode, Map<Node, Integer> distance,
                          Map<Node, Node> predecessors) {

        // initialize distances - we will use Integer.MAX_VALUE to
        // represent infinity
        for(Coordinate coordinate : this.keySet()) {
            distance.put(this.get(coordinate), Integer.MAX_VALUE);
        }
        distance.put(startNode,  0);

        // initialize predecessors - by not yet including any other nodes,
        // they are unvisited and have no predecessor.  Source node is
        // given predecessor of itself.
        predecessors.put(startNode, startNode);

        // our priority queue will just be a list that we search to extract
        // the minimum from at each step (O(n))
        List<Node> priorityQ = new LinkedList<Node>();
        for (Coordinate coordinate : this.keySet()) {
            priorityQ.add(this.get(coordinate));
        }

        // main loop
        while (!priorityQ.isEmpty()) {
            Node U = dequeueMin(priorityQ, distance);

            // return if this node still has distance "infinity" -
            // remaining nodes are inaccessible
            if(distance.get(U) == Integer.MAX_VALUE) {
                return;
            }

            // this loop allows neighbors that have already been finalized
            // to be checked again, but they will never be updated and
            // this doesn't affect overall complexity
            for(Edge e : U.getEdges()) {
                Integer w = e.getWeight();
                Node n = e.getToNode();
                // relaxation
                Integer distViaU = distance.get(U) + w;
                if(distance.get(n) > distViaU) {
                    distance.put(n,  distViaU);
                    predecessors.put(n,  U);
                }
            }
        }
    }

    /**
     * Basic implementation of a priority queue that searches for the minimum.
     */
    private Node dequeueMin(List<Node> priorityQ, Map<Node, Integer> distance) {

        Node minNode = priorityQ.get(0);  // start off with first one
        for (Node n : priorityQ) { // checks first one again...
            if(distance.get(n) < distance.get(minNode)) {
                minNode = n;
            }
        }
        return priorityQ.remove(priorityQ.indexOf(minNode));
    }
}
