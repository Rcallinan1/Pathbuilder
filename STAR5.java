package Players.STAR5;
import Interface.*;

import java.util.*;


/**
 * A player module class which implements the PlayerModulePart1 interface.
 *
 * Created by Noor Mohammad on 3/20/2017.
 */
public class STAR5 implements PlayerModule {

    /**
     * A board of empty nodes with coordinates
     */
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
    private int playerId;

    /**
     * A map of all the nodes and its coordinates
     */
    private Graph graph;

    /**
     * A new graph with full player assignment
     */
    private Graph copygraph;

    /**
     * A new board of nodes with players assigned
     */
    private ArrayList<Node> copyboard;

    private PlayerMove last;

    private List<PlayerMove> oppmove;

    /**
     * Method called to initialize a player module. Required task for part 1.
     * Note that for tournaments of multiple games, only one instance of each PlayerModule is created.
     * The initPlayer method is called at the beginning of each game,
     * and must be able to reset the player for the next game.
     *
     * @param dim      - size of the smaller dimension of the playing area for one player.
     *                 The grid of nodes for that player is of size dim x (dim +1)
     * @param playerId - id ( 1 or 2 ) for this player
     */
    @Override
    public void initPlayer(int dim, int playerId) {
        if (dim < 3 || dim > 20) {
            throw new IllegalArgumentException("The value for DIM is not in the legal range [3,20]");
        }
        oppmove = new ArrayList<>();
        board = new ArrayList<Node>();
        graph = new Graph();
        this.playerId = playerId;
        start1 = new ArrayList<>();
        finish1 = new ArrayList<>();
        start2 = new ArrayList<>();
        finish2 = new ArrayList<>();
        copyboard = new ArrayList<Node>();
        copygraph = new Graph();
        this.dimension = dim * 2;
        //Initialize board to have a size of (2 * dim + 1) x (2 * dim + 1)
        for (int r = 0; r <= dim * 2; r++) {
            for (int c = 0; c <= dim * 2; c++) {
                Coordinate coordinate = new Coordinate(r, c);
                Node current = new Node(coordinate, 0);
                graph.put(coordinate, current);
                board.add(current);
            }
        }
        //Place the start and finish nodes for players 1 and 2
        for (Node node : board) {
            int col = node.getCoordinate().getCol();
            int row = node.getCoordinate().getRow();
            if (col == 0 || col == dim * 2) {
                if ((row % 2) != 0) {
                    Coordinate coordinate = new Coordinate(row, col);
                    if (col == 0) {
                        start1.add(coordinate);
                    } else {
                        finish1.add(coordinate);
                    }
                    node.assign(1);
                    graph.put(coordinate, node);
                }
            } else if (row == 0 || row == dim * 2) {
                if ((col % 2) == 1) {
                    Coordinate coordinate = new Coordinate(row, col);
                    node.assign(2);
                    graph.put(coordinate, node);
                    if (row == 0) {
                        start2.add(coordinate);
                    } else {
                        finish2.add(coordinate);
                    }
                }
            }
        }
        for (int r = 0; r <= dim * 2; r++) {
            for (int c = 0; c <= dim * 2; c++) {
                Coordinate coordinate = new Coordinate(r, c);
                if ((r % 2) != 0 && (c % 2) == 0) {
                    Node current = new Node(coordinate, 1);
                    copyboard.add(current);
                    copygraph.put(coordinate, current);
                } else if ((c % 2) != 0 && (r % 2) == 0) {
                    Node current = new Node(coordinate, 2);
                    copyboard.add(current);
                    copygraph.put(coordinate, current);
                } else {
                    Node current = new Node(coordinate, 0);
                    copyboard.add(current);
                    copygraph.put(coordinate, current);
                }

            }
        }
        for (Node node : copyboard) {
            int r = node.getCoordinate().getRow();
            int c = node.getCoordinate().getCol();
            Coordinate north = new Coordinate(r - 2, c);
            Coordinate south = new Coordinate(r + 2, c);
            Coordinate east = new Coordinate(r, c + 2);
            Coordinate west = new Coordinate(r, c - 2);
            if (node.getPlayerId() == 1) {
                if (copygraph.containsKey(north)) {
                    if (copygraph.get(north).getPlayerId() == 1) {
                        node.addNeighbor(copygraph.get(north),1);
                    }
                }
                if (copygraph.containsKey(east)) {
                    if (copygraph.get(east).getPlayerId() == 1) {
                        node.addNeighbor(copygraph.get(east),1);
                    }
                }
                if (copygraph.containsKey(south)) {
                    if (copygraph.get(south).getPlayerId() == 1) {
                        node.addNeighbor(copygraph.get(south),1);
                    }
                }
                if (copygraph.containsKey(west)) {
                    if (copygraph.get(west).getPlayerId() == 1) {
                        node.addNeighbor(copygraph.get(west),1);
                    }
                }
            } else if (node.getPlayerId() == 2) {
                if (copygraph.containsKey(north)) {
                    if (copygraph.get(north).getPlayerId() == 2) {
                        node.addNeighbor(copygraph.get(north),1);
                    }
                }
                if (copygraph.containsKey(east)) {
                    if (copygraph.get(east).getPlayerId() == 2) {
                        node.addNeighbor(copygraph.get(east),1);
                    }
                }
                if (copygraph.containsKey(south)) {
                    if (copygraph.get(south).getPlayerId() == 2) {
                        node.addNeighbor(copygraph.get(south),1);
                    }
                }
                if (copygraph.containsKey(west)) {
                    if (copygraph.get(west).getPlayerId() == 2) {
                        node.addNeighbor(copygraph.get(west),1);
                    }
                }
            }
        }
    }


    /**
     * Method called after every move of the game. Used to keep internal game state current.
     * Required task for Part 1.
     * Note that the engine will only call this method after verifying the validity of the current move.
     * Thus, you do not need to verify the move provided to this method. It is guaranteed to be a valid move.
     *
     * @param m - PlayerMove representing the most recent move
     */
    @Override
    public void lastMove(PlayerMove m) {
        if (m.getPlayerId() != playerId) {
            last = m;
        }
        if (m.getPlayerId() != playerId) {
            oppmove.add(m);
        }
        int c = m.getCoordinate().getCol();
        int r = m.getCoordinate().getRow();
        Coordinate north = new Coordinate(r - 1, c);
        Coordinate south = new Coordinate(r + 1, c);
        Coordinate east = new Coordinate(r, c + 1);
        Coordinate west = new Coordinate(r, c - 1);

        for (Node node : board) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(1);
                        West.assign(1);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(1);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 0) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(1);
                        South.assign(1);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(1);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                } else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(2);
                        West.assign(2);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(2);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 1) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(2);
                        South.assign(2);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(2);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                }
            }
        }
        for (Node node : copyboard) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                Node East = copygraph.get(east);
                Node West = copygraph.get(west);
                Node North = copygraph.get(north);
                Node South = copygraph.get(south);
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West);
                        West.addNeighbor(East);
                        East.addNeighbor(node);
                        West.addNeighbor(node);
                        node.assign(1);
                        node.addNeighbor(East);
                        node.addNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 0) {
                        North.assign(1);
                        South.assign(1);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South);
                        South.addNeighbor(North);
                        North.addNeighbor(node);
                        South.addNeighbor(node);
                        node.addNeighbor(North);
                        node.addNeighbor(South);
                        node.assign(1);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
                else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        East.assign(2);
                        West.assign(2);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West);
                        West.addNeighbor(East);
                        East.addNeighbor(node);
                        West.addNeighbor(node);
                        node.assign(2);
                        node.addNeighbor(East);
                        node.addNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 1) {
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        North.assign(2);
                        South.assign(2);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South);
                        South.addNeighbor(North);
                        North.addNeighbor(node);
                        South.addNeighbor(node);
                        node.addNeighbor(North);
                        node.addNeighbor(South);
                        node.assign(2);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
            }
        }
    }
    public void undoMove(PlayerMove m) {
        int c = m.getCoordinate().getCol();
        int r = m.getCoordinate().getRow();
        Coordinate north = new Coordinate(r - 1, c);
        Coordinate south = new Coordinate(r + 1, c);
        Coordinate east = new Coordinate(r, c + 1);
        Coordinate west = new Coordinate(r, c - 1);

        for (Node node : board) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        if (East.getNeighbors().isEmpty()){
                            if (!start1.contains(East) && !finish1.contains(East))
                            East.assign(0);
                        }
                        if (West.getNeighbors().isEmpty()){
                            if (!start1.contains(West) && !finish1.contains(West))
                                West.assign(0);
                        }
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.removeNeighbor(node);
                        West.removeNeighbor(node);
                        node.removeNeighbor(East);
                        node.removeNeighbor(West);
                        node.assign(0);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 0) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        if (North.getNeighbors().isEmpty()){
                            if (!start1.contains(North) && !finish1.contains(North))
                                North.assign(0);
                        }
                        if (South.getNeighbors().isEmpty()){
                            if (!start1.contains(South) && !finish1.contains(South))
                                South.assign(0);
                        }
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.removeNeighbor(node);
                        South.removeNeighbor(node);
                        node.removeNeighbor(South);
                        node.removeNeighbor(North);
                        node.assign(0);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                } else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        if (East.getNeighbors().isEmpty()){
                            if (!start2.contains(East) && !finish2.contains(East))
                                East.assign(0);
                        }
                        if (West.getNeighbors().isEmpty()){
                            if (!start2.contains(West) && !finish2.contains(West))
                                West.assign(0);
                        }
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.removeNeighbor(node);
                        West.removeNeighbor(node);
                        node.removeNeighbor(East);
                        node.removeNeighbor(West);
                        node.assign(0);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 1) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        if (North.getNeighbors().isEmpty()){
                            if (!start2.contains(North) && !finish2.contains(North))
                                North.assign(0);
                        }
                        if (South.getNeighbors().isEmpty()){
                            if (!start2.contains(South) && !finish2.contains(South))
                                South.assign(0);
                        }
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.removeNeighbor(node);
                        South.removeNeighbor(node);
                        node.removeNeighbor(South);
                        node.removeNeighbor(North);
                        node.assign(0);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                }
            }
        }

        for (Node node : copyboard) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        Node East = copygraph.get(east);
                        Node West = copygraph.get(west);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West,1);
                        West.addNeighbor(East,1);
                        East.removeNeighbor(node);
                        West.removeNeighbor(node);
                        node.assign(0);
                        node.removeNeighbor(East);
                        node.removeNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 0) {
                        Node North = copygraph.get(north);
                        Node South = copygraph.get(south);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South,1);
                        South.addNeighbor(North,1);
                        North.removeNeighbor(node);
                        South.removeNeighbor(node);
                        node.removeNeighbor(North);
                        node.removeNeighbor(South);
                        node.assign(0);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
                else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = copygraph.get(east);
                        Node West = copygraph.get(west);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West,1);
                        West.addNeighbor(East,1);
                        East.removeNeighbor(node);
                        West.removeNeighbor(node);
                        node.assign(0);
                        node.removeNeighbor(East);
                        node.removeNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 1) {
                        Node North = copygraph.get(north);
                        Node South = copygraph.get(south);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South,1);
                        South.addNeighbor(North,1);
                        North.removeNeighbor(node);
                        South.removeNeighbor(node);
                        node.removeNeighbor(North);
                        node.removeNeighbor(South);
                        node.assign(0);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
                else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = copygraph.get(east);
                        Node West = copygraph.get(west);
                        East.assign(2);
                        West.assign(2);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West);
                        West.addNeighbor(East);
                        East.addNeighbor(node);
                        West.addNeighbor(node);
                        node.assign(2);
                        node.addNeighbor(East);
                        node.addNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 1) {
                        Node North = copygraph.get(north);
                        Node South = copygraph.get(south);
                        North.assign(2);
                        South.assign(2);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South);
                        South.addNeighbor(North);
                        North.addNeighbor(node);
                        South.addNeighbor(node);
                        node.addNeighbor(North);
                        node.addNeighbor(South);
                        node.assign(2);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
            }
        }
    }
    public void tempMove(PlayerMove m){
        if (m.getPlayerId() != playerId) {
            last = m;
        }
        if (m.getPlayerId() != playerId) {
            oppmove.add(m);
        }
        int c = m.getCoordinate().getCol();
        int r = m.getCoordinate().getRow();
        Coordinate north = new Coordinate(r - 1, c);
        Coordinate south = new Coordinate(r + 1, c);
        Coordinate east = new Coordinate(r, c + 1);
        Coordinate west = new Coordinate(r, c - 1);

        for (Node node : board) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(1);
                        West.assign(1);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(1);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 0) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(1);
                        South.assign(1);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(1);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                } else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(2);
                        West.assign(2);
                        East.addNeighbor(West, 1);
                        West.addNeighbor(East, 1);
                        East.addNeighbor(node, 1);
                        West.addNeighbor(node, 1);
                        node.addNeighbor(East, 1);
                        node.addNeighbor(West, 1);
                        node.assign(2);
                        graph.put(east, East);
                        graph.put(west, West);
                    } else if ((c % 2) == 1) {
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(2);
                        South.assign(2);
                        North.addNeighbor(South, 1);
                        South.addNeighbor(North, 1);
                        North.addNeighbor(node, 1);
                        South.addNeighbor(node, 1);
                        node.addNeighbor(North, 1);
                        node.addNeighbor(South, 1);
                        node.assign(2);
                        graph.put(north, North);
                        graph.put(south, South);
                    }
                }
            }
        }
        for (Node node : copyboard) {
            if (node.getCoordinate().equals(m.getCoordinate())) {
                Node East = copygraph.get(east);
                Node West = copygraph.get(west);
                Node North = copygraph.get(north);
                Node South = copygraph.get(south);
                if (m.getPlayerId() == 1) {
                    if ((c % 2) == 1) {
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West);
                        West.addNeighbor(East);
                        East.addNeighbor(node);
                        West.addNeighbor(node);
                        node.assign(1);
                        node.addNeighbor(East);
                        node.addNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 0) {
                        North.assign(1);
                        South.assign(1);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South);
                        South.addNeighbor(North);
                        North.addNeighbor(node);
                        South.addNeighbor(node);
                        node.addNeighbor(North);
                        node.addNeighbor(South);
                        node.assign(1);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
                    }
                }
                else if (m.getPlayerId() == 2) {
                    if ((c % 2) == 0) {
                        East.assign(2);
                        West.assign(2);
                        East.removeNeighbor(West);
                        West.removeNeighbor(East);
                        East.addNeighbor(West);
                        West.addNeighbor(East);
                        East.addNeighbor(node);
                        West.addNeighbor(node);
                        node.assign(2);
                        node.addNeighbor(East);
                        node.addNeighbor(West);
                        copygraph.put(east, East);
                        copygraph.put(west, West);
                        copygraph.put(node.getCoordinate(),node);
                    } else if ((c % 2) == 1) {
                        North.assign(2);
                        South.assign(2);
                        North.removeNeighbor(South);
                        South.removeNeighbor(North);
                        North.addNeighbor(South);
                        South.addNeighbor(North);
                        North.addNeighbor(node);
                        South.addNeighbor(node);
                        node.addNeighbor(North);
                        node.addNeighbor(South);
                        node.assign(2);
                        copygraph.put(north, North);
                        copygraph.put(south, South);
                        copygraph.put(node.getCoordinate(),node);
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
     * Generates the next move for this player.
     * Note that it is recommended that updating internal game state does NOT occur inside of this method.
     * See lastMove. An initial, working version of this method is required for Part 2.
     * It may be refined subsequently.
     *
     * @return a PlayerMove object representing the next move.
     */
    @Override
    public PlayerMove move() {
        List<PlayerMove> allmoves = allLegalMoves();
        Random rand = new Random();
        int i = rand.nextInt(allmoves.size());
        PlayerMove playerMove = allmoves.get(0);
        ArrayList<Coordinate> finish = new ArrayList<>();
        ArrayList<Coordinate> start = new ArrayList<>();
        if (playerId == 1){
            finish = finish1;
            start = start1;
        }
        else{
            finish = finish2;
            start = start2;
        }
        int min = 100;
        int temp = 0;
        for (PlayerMove move : allmoves){
            tempMove(move);
            if (hasWonGame(playerId)){
                undoMove(move);
                return move;
            }
            undoMove(move);
        }
        if (last != null) {
            if (fewestSegmentsToVictory(last.getPlayerId()) <= 4) {
                int c = last.getCoordinate().getCol();
                int r = last.getCoordinate().getRow();
                Coordinate north = new Coordinate(r - 2, c);
                Coordinate south = new Coordinate(r + 2, c);
                Coordinate east = new Coordinate(r, c + 2);
                Coordinate west = new Coordinate(r, c - 2);
                Coordinate northeast = new Coordinate(r - 1, c + 1);
                Coordinate northwest = new Coordinate(r - 1, c - 1);
                Coordinate southeast = new Coordinate(r + 1, c + 1);
                Coordinate southwest = new Coordinate(r + 1, c - 1);
                PlayerMove North = new PlayerMove(north, playerId);
                PlayerMove South = new PlayerMove(south, playerId);
                PlayerMove East = new PlayerMove(east, playerId);
                PlayerMove West = new PlayerMove(west, playerId);
                PlayerMove NorthEast = new PlayerMove(northeast, playerId);
                PlayerMove SouthEast = new PlayerMove(southeast, playerId);
                PlayerMove NorthWest = new PlayerMove(northwest, playerId);
                PlayerMove SouthWest = new PlayerMove(southwest, playerId);
                for (PlayerMove move : allLegalMoves()){
                    PlayerMove t = new PlayerMove(move.getCoordinate(),last.getPlayerId());
                    temp = fewestSegmentsToVictory(last.getPlayerId());
                    tempMove(t);
                    if (hasWonGame(last.getPlayerId())){
                        undoMove(t);
                        return move;
                    }
                    if (fewestSegmentsToVictory(last.getPlayerId()) < temp){
                        playerMove = move;
                    }
                    undoMove(t);

                }
//                if (allLegalMoves().contains(North)) {
//                    tempMove(North);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = North;
//                    }
//                    undoMove(North);
//                }
//                if (allLegalMoves().contains(South)) {
//                    tempMove(South);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = South;
//                    }
//                    undoMove(South);
//                }
//                if (allLegalMoves().contains(East)) {
//                    tempMove(East);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = East;
//                    }
//                    undoMove(East);
//                }
//                if (allLegalMoves().contains(West)) {
//                    tempMove(West);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = West;
//                    }
//                    undoMove(West);
//                }
//                if (allLegalMoves().contains(NorthEast)) {
//                    tempMove(NorthEast);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = NorthEast;
//                    }
//                    undoMove(NorthEast);
//                }
//                if (allLegalMoves().contains(NorthWest)) {
//                    tempMove(NorthWest);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = NorthWest;
//                    }
//                    undoMove(NorthWest);
//                }
//                if (allLegalMoves().contains(SouthEast)) {
//                    tempMove(SouthEast);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = SouthEast;
//                    }
//                    undoMove(SouthEast);
//                }
//                if (allLegalMoves().contains(SouthWest)) {
//                    tempMove(SouthWest);
//                    temp = fewestSegmentsToVictory(last.getPlayerId());
//                    if (temp < min) {
//                        min = temp;
//                        playerMove = SouthWest;
//                    }
//                    undoMove(SouthWest);
//                }
            }
        }
        if (last != null) {
            temp = fewestSegmentsToVictory(last.getPlayerId());
            tempMove(playerMove);
            if (temp != fewestSegmentsToVictory(last.getPlayerId())) {
                undoMove(playerMove);
                return playerMove;
            }
            undoMove(playerMove);
        }
        min = 100;
        for (PlayerMove move : allmoves){
            int c = move.getCoordinate().getCol();
            int r = move.getCoordinate().getRow();
            Coordinate north = new Coordinate(r - 1, c);
            Coordinate south = new Coordinate(r + 1, c);
            Coordinate east = new Coordinate(r, c + 1);
            Coordinate west = new Coordinate(r, c - 1);
            for (Coordinate f : finish){
                if (graph.canReachBFS(north,f)) {
                    for (Coordinate s : start) {
                        if (copygraph.canReachBFS(move.getCoordinate(), s)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),s).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(south,f)) {
                    for (Coordinate s : start) {
                        if (copygraph.canReachBFS(move.getCoordinate(), s)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),s).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(east,f)) {
                    for (Coordinate s : start) {
                        if (copygraph.canReachBFS(move.getCoordinate(), s)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),s).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(west,f)) {
                    for (Coordinate s : start) {
                        if (copygraph.canReachBFS(move.getCoordinate(), s)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),s).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
            }
            for (Coordinate s : start){
                if (graph.canReachBFS(north,s)) {
                    for (Coordinate f : finish) {
                        if (copygraph.canReachBFS(move.getCoordinate(), f)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),f).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(south,s)) {
                    for (Coordinate f : finish) {
                        if (copygraph.canReachBFS(move.getCoordinate(), f)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),f).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(east,s)) {
                    for (Coordinate f : finish) {
                        if (copygraph.canReachBFS(move.getCoordinate(), f)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),f).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
                if (graph.canReachBFS(west,s)) {
                    for (Coordinate f : finish) {
                        if (copygraph.canReachBFS(move.getCoordinate(), f)) {
                            temp = copygraph.searchBFS(move.getCoordinate(),f).size();
                            if (temp < min){
                                playerMove = move;
                            }
                        }
                    }
                }
            }
        }
        return playerMove;
    }


    /**
     * Indicates that the other player has been invalidated. Required task for Part 2.
     */
    @Override
    public void otherPlayerInvalidated() {

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

    /**
     * Part 2 task that computes the fewest segments that a given player needs to add to complete a winning path.
     * This ignores the possibility that the opponent might block the path.
     * <p>
     * Precondition: you may assume that the other player has not already won the game.
     * That is, you may assume that a winning path still exists for the player of interest.
     *
     * @param i - the player of interest
     * @return the fewest number of segments to add to complete a path
     */
    public int fewestSegmentsToVictory(int i) {
        int num = 0;
        int temp = 100;
        if (i == 1){
            for (Coordinate start: start1){
                for (Coordinate finish : finish1){
                    num = copygraph.displayShortestPath(start,finish);
                    if (num < temp && num != 0) {
                        temp = num;
                    }
                }
            }
        }

        else if (i == 2){
            for (Coordinate start: start2){
                for (Coordinate finish : finish2){
                    num = copygraph.displayShortestPath(start,finish);
                    if (num < temp && num != 0) {
                        temp = num;
                    }
                }
            }
        }
        return temp;
    }

    /**
     * Part 3 task that computes whether the given player is guranteed with optimal strategy
     * to have won the game in no more than the given number of total moves,
     * also given whose turn it is currently.
     * PRECONDITION: you may assume that numMoves is non-negative
     * @param playerId - player to determine winnable status for
     * @param whoseTurn - player whose turn it is currently
     * @param numMoves - num of total moves by which the player of interest must be able to guarantee
     *                 a win after the specified number of total moves.
     * @return boolean indicating whether it is possible for the indicated player to guarantee a win
     *      after the specified number of total moves
     */
    public boolean isWinnable(int playerId, int whoseTurn, int numMoves) {
        PathConfig init = new PathConfig(graph,start1,start2,finish1,finish2
                ,dimension,whoseTurn,playerId,numMoves);

        // create the backtracker with the debug flag
//        boolean debug = args[1].equals("true");
        Backtracker bt = new Backtracker(false);

        // start the clock
        double start = System.currentTimeMillis();

        // attempt to solve the puzzle
        Optional<Configuration> sol = bt.solve(init);

        // compute the elapsed time
        System.out.println("Elapsed time: " +
                (System.currentTimeMillis() - start)/1000.0 + " seconds.");

        // indicate whether there was a solution, or not
        if (sol.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

}
