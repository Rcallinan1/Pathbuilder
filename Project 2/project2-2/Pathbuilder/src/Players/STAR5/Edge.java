package Players.STAR5;

/**
 * Class representing an edge in a graph.
 *
 * @author atd:  Aaron T Deever
 *
 */
public class Edge {

    private Node fromNode;
    private Node toNode;
    private int weight;

    /**
     * Constructor.
     *
     * @param from source node of the directed edge
     * @param to   destination node of the directed edge
     * @param wt   weight of the directed edge
     */
    public Edge(Node from, Node to, int wt) {
        fromNode = from;
        toNode = to;
        weight = wt;
    }

    public Edge(Edge e){
        this.fromNode = new Node(e.fromNode);
        this.toNode = new Node(e.toNode);
        this.weight = e.weight;
    }


    /**
     * Method to access source node of directed edge
     *
     * @return source node of the directed edge
     */
    public Node getFromNode() {
        return fromNode;
    }

    /**
     * Method to access destination node of directed edge
     *
     * @return destination node of the directed edge
     */
    public Node getToNode() {
        return toNode;
    }

    /**
     * Method to access weight of directed edge
     *
     * @return weight of the directed edge
     */
    public int getWeight() {
        return weight;
    }

}

