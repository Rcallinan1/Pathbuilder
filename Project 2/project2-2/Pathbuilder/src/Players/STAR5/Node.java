package Players.STAR5;
import Interface.Coordinate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a node in a graph.
 * 
 * @author atd Aaron T Deever
 * @author sps Sean P Strout
 * edited by Noor Mohammad
 */
public class Node {
    /** playerId associated with this node */
    private Integer playerId;

    /** The coordinate associated with this node */
    private Coordinate coordinate;

    /** Neighbors of this node are stored as a list (adjacency list) */
    private List<Edge> neighbors;

    public Node(Coordinate coordinate,Integer playerId) {
        this.playerId = playerId;
        this.coordinate = coordinate;
        this.neighbors = new LinkedList<>();

    }
//
//    public Node(Node n){
//        this.playerId = n.playerId;
//        this.coordinate = new Coordinate(n.getCoordinate().getRow(),n.getCoordinate().getCol());
//        for (Edge e : n.neighbors){
//            this.neighbors.add(new Edge(e));
//        }
//    }
    /**
     * Get the Coordinate coordinate associated with this object.
     * @return coordinate.
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    /**
     * Get the Integer playerId associated with this object.
     * @return playerId.
     */
    public Integer getPlayerId() {
        return this.playerId;
    }

    /**
     * Occupy the node with the playerId
     * @param playerId - playerId being assigned to the node
     */
    public void assign(Integer playerId){
        this.playerId = playerId;
    }


    /**
     * Add a neighbor to this node.  When no weight is specified, a
     * weight of 0 is assigned to the connecting edge.
     * @param n node to add as neighbor.
     */
    public void addNeighbor(Node n) {
        addNeighbor(n, 0); // just set weight to 0 if not specified
    }

    /**
     * Add a neighbor to this node.  Weight of connecting edge is specified.
     * @param n node to add as neighbor
     * @param weight weight of the edge
     */
    public void addNeighbor(Node n, Integer weight) {
        Edge e = new Edge(this, n, weight);
        neighbors.add(e);
    }

    /**
     * Method to return a list for this node containing all
     * of its neighbor nodes.
     *
     * @return a list of neighboring nodes
     */
    public List<Node> getNeighbors() {
        List<Node> l = new LinkedList<Node>();
        for(Edge e: getEdges()) {
            l.add(e.getToNode());
        }
        return l;
    }

    /**
     * Method to return a list for this node containing all
     * of its outgoing edges.
     *
     * @return a list of outgoing edges
     */
    public List<Edge> getEdges() {
        return new LinkedList<Edge>(neighbors);
    }

    /**
     * Method to generate a string associated with the node, including the 
     * playerId of the node and the coordinate followed by the names of its neighbors.
     * Overrides Object toString method.
     * 
     * @return string associated with the node.
     */
    @Override
    public String toString() {
        String result;
        if (getPlayerId().equals(0)){
            result = "Empty, ";
        }
        else {
            result = "Player " + getPlayerId() + ", ";
        }
        result += coordinate + ": ";

        for(Edge nbr : neighbors) {
            result = result + " (" + nbr.getToNode().getCoordinate() + ", "
                    + nbr.getWeight() + "), ";
        }
        // Remove last comma and space, or just spaces
        // in the case of no neighbors
        return (result.substring(0, result.length()-2));
    }

    /**
     *  Two Nodes are equal if they have the same name.
     *  @param other The other object to check equality with
     *  @return true if equal; false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Node) {
            Node n = (Node) other;
            result = this.coordinate.equals(n.coordinate);
        }
        return result;
    }
}
