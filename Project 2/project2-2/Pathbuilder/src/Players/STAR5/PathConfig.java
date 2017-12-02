package Players.STAR5;

import Interface.Coordinate;
import Interface.PlayerMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Noor Mohammad on 4/21/2017.
 */
public class PathConfig implements Configuration{

//    /**
//     * A board of empty nodes with coordinates
//     */
    private ArrayList<Node> board;

    /**
     * A list of coordinates that represent the location of all the start nodes for player 1
     */
    private ArrayList<Coordinate> start1;


    /**
     * A list of coordinates that represent the location of all the finish nodes for player 1
     */
    private ArrayList<Coordinate> finish1;

    /**
     * A list of coordinates that represent the location of all the start nodes for player 2
     */
    private ArrayList<Coordinate> start2;

    /**
     * A list of coordinates that represent the location of all the finish nodes for player 2
     */
    private ArrayList<Coordinate> finish2;

    /**
     * The dimension of the board
     */
    private int dimension;

    /**
     * id (1 or 2) for the player
     */
    private int Id;

    /**
     * A map of all the nodes and its coordinates
     */
    private Graph graph;

    private PlayerMove last;
    private int playerId;
    private int whoseTurn;
    private int numMoves;

    public PathConfig(ArrayList<Node> board, Graph graph, ArrayList<Coordinate> start1,
                      ArrayList<Coordinate> start2, ArrayList<Coordinate> finish1,
                      ArrayList<Coordinate> finish2, int dimension, int Id, int whoseTurn, int numMoves){
        this.start1 = start1;
        this.start2 = start2;
        this.finish1 = finish1;
        this.finish2 = finish2;
        this.playerId = whoseTurn;
        this.dimension = dimension;
        this.board = new ArrayList<>();
        for (Node n : board){
            Node node = new Node(n.getCoordinate(),n.getPlayerId());
            for (Node temp : n.getNeighbors()){
                Node r = new Node(temp.getCoordinate(),temp.getPlayerId());
                node.addNeighbor(r);
            }
            this.board.add(node);
        }
        this.graph = new Graph();
        for (Coordinate coor : graph.keySet()){
            Coordinate coordinate = new Coordinate(coor.getRow(),coor.getCol());
            Node n = graph.get(coor);
            Node node = new Node(n.getCoordinate(),n.getPlayerId());
            for (Node temp : n.getNeighbors()){
                node.addNeighbor(temp);
            }
            this.graph.put(coordinate,node);
        }
        this.last = null;
//        this.graph = graph;
        this.Id = Id;
        this.whoseTurn = whoseTurn;
        this.numMoves = numMoves;
    }

    protected PathConfig(PathConfig other){
        this.Id = other.Id;
        if (other.whoseTurn == 1){
            this.whoseTurn = 2;
        } else if(other.whoseTurn == 2){
            this.whoseTurn = 1;
        }
        this.last = other.last;
        this.numMoves = other.numMoves - 1;
        this.start1 = other.start1;
        this.start2 = other.start2;
        this.finish1 = other.finish1;
        this.finish2 = other.finish2;
        this.playerId = this.whoseTurn;
        this.dimension = other.dimension;
        this.board = new ArrayList<>();
        for (Node n : other.board){
            Node node = new Node(n.getCoordinate(),n.getPlayerId());
            for (Node temp : n.getNeighbors()){
                Node r = new Node(temp.getCoordinate(),temp.getPlayerId());
                node.addNeighbor(r);
            }
            this.board.add(node);
        }
        this.graph = new Graph();
        for (Coordinate coor : other.graph.keySet()){
            Coordinate coordinate = new Coordinate(coor.getRow(),coor.getCol());
            Node n = other.graph.get(coor);
            Node node = new Node(n.getCoordinate(),n.getPlayerId());
            for (Node temp : n.getNeighbors()){
                Node r = new Node(temp.getCoordinate(),temp.getPlayerId());
                node.addNeighbor(r);
            }
            this.graph.put(coordinate,node);
        }
        this.lastMove(this.last);
    }
    /**
     * Get the collection of successors from the current one.
     *
     * @return All successors, valid and invalid
     */
    public Collection<Configuration> getSuccessors(){
        LinkedList<Configuration> successors = new LinkedList<>();
        Collection<PlayerMove> temp = this.allLegalMoves();
        for (PlayerMove move : this.allLegalMoves()){
            this.last = move;
            PathConfig s = new PathConfig(this);
            s.last = move;
            s.lastMove(s.last);
            successors.add(s);
        }
        return successors;
    }

    /**
     * Is the current configuration valid or not?
     *
     * @return true if valid; false otherwise
     */
    public boolean isValid(){
        return true;
    }

    /**
     * Is the current configuration a goal?
     * @return true if goal; false otherwise
     */
    public boolean isGoal(){
        if (this.numMoves >= 0){
            if (hasWonGame(this.Id)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * Method called after every move of the game. Used to keep internal game state current.
     * Required task for Part 1.
     * Note that the engine will only call this method after verifying the validity of the current move.
     * Thus, you do not need to verify the move provided to this method. It is guaranteed to be a valid move.
     *
     * @param m - PlayerMove representing the most recent move
     */
    public void lastMove(PlayerMove m) {
        int c = m.getCoordinate().getCol();
        int r = m.getCoordinate().getRow();
        Coordinate north = new Coordinate(r - 1, c);
        Coordinate south = new Coordinate(r + 1, c);
        Coordinate east = new Coordinate(r, c + 1);
        Coordinate west = new Coordinate(r, c - 1);

        for (Node node : this.board) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        Node East = this.graph.get(east);
                        Node West = this.graph.get(west);
                        East.assign(1);
                        West.assign(1);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(1);
                        this.graph.put(east, East);
                        this.graph.put(west, West);
                    } else if ((c % 2) == 0) {
                        Node North = this.graph.get(north);
                        Node South = this.graph.get(south);
                        North.assign(1);
                        South.assign(1);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(1);
                        this.graph.put(north, North);
                        this.graph.put(south, South);
                    }
                } else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = this.graph.get(east);
                        Node West = this.graph.get(west);
                        East.assign(2);
                        West.assign(2);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(2);
                        this.graph.put(east, East);
                        this.graph.put(west, West);
                    } else if ((c % 2) == 1) {
                        Node North = this.graph.get(north);
                        Node South = this.graph.get(south);
                        North.assign(2);
                        South.assign(2);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(2);
                        this.graph.put(north, North);
                        this.graph.put(south, South);
                    }
                }
            }
        }
    }

    /**
     * Part 1 task that tests if a player has won the game given a set of PREMOVEs.
     *
     * @param id - player to test for a winning path.
     * @return boolean value indicating if the player has a winning path.
     */
    public boolean hasWonGame(int id) {

        if (id == 1) {
            for (Coordinate start : start1) {
                for (Coordinate finish : finish1) {
                    if (graph.canReachBFS(start, finish)) {
                        return true;
                    }
                }
            }
        } else if (id == 2) {
            for (Coordinate start : start2) {
                for (Coordinate finish : finish2) {
                    if (graph.canReachBFS(start, finish)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Part 2 task that tests if a player can correctly generate all legal moves,
     * assuming that it is that player's turn and given the current game status.
     * <p>
     * Preconditions: you may assume that there is no winner yet based on prior moves.
     * You may also assume that this method will only be called
     * when it is actually your player's turn based on prior moves.
     *
     * @return a List of all legal PlayerMove objects. They do not have to be in any particular order.
     */
    public List<PlayerMove> allLegalMoves() {
        LinkedList<PlayerMove> Legal = new LinkedList<PlayerMove>();
        for (Node node : board) {
            //If player ID is 0 then the slot is empty, meaning that it is a legal position for the next move.
            if (node.getPlayerId() == 0 && node.getCoordinate().getRow() != 0 && node.getCoordinate().getCol() != 0) {
                if (node.getCoordinate().getRow() != dimension && node.getCoordinate().getCol() != dimension) {
                    if (playerId == 1) {
                        if (node.getCoordinate().getRow() % 2 != 0 && node.getCoordinate().getCol() % 2 != 0) {
                            Legal.add(new PlayerMove(node.getCoordinate(), 1));//ID will always be 0
                        }
                        if (node.getCoordinate().getRow() % 2 == 0 && node.getCoordinate().getCol() % 2 == 0) {
                            Legal.add(new PlayerMove(node.getCoordinate(), 1));//ID will always be 0
                        }
                    } else if (playerId == 2) {
                        if (node.getCoordinate().getRow() % 2 != 1 && node.getCoordinate().getCol() % 2 != 1) {
                            Legal.add(new PlayerMove(node.getCoordinate(), 2));//ID will always be 0
                        }
                        if (node.getCoordinate().getRow() % 2 == 1 && node.getCoordinate().getCol() % 2 == 1) {
                            Legal.add(new PlayerMove(node.getCoordinate(), 2));//ID will always be 0
                        }
                    }
                }
            }
            if (node.getPlayerId() == 2 || node.getPlayerId() == 1) {
                Legal.remove(new PlayerMove(node.getCoordinate(), 0));//ID will always be 0
            }
        }
        return Legal;
    }


}
