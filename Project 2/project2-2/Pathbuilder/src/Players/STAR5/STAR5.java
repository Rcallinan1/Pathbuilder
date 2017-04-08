package Players.STAR5;
        import Interface.PlayerModulePart1;
        import Interface.Coordinate;
        import Interface.PlayerModulePart2;
        import Interface.PlayerMove;

        import java.util.*;


/**
 * A player module class which implements the PlayerModulePart1 interface.
 *
 * Created by Noor Mohammad on 3/20/2017.
 */
public class STAR5 implements PlayerModulePart2{

    /**A board of empty nodes with coordinates */
    private ArrayList<Node> board;

    /**A list of coordinates that represent the location of all the start nodes for player 1  */
    private ArrayList<Coordinate> start1;

    /**A list of coordinates that represent the location of all the finish nodes for player 1  */
    private ArrayList<Coordinate> finish1;

    /**A list of coordinates that represent the location of all the start nodes for player 2  */
    private ArrayList<Coordinate> start2;

    /**A list of coordinates that represent the location of all the finish nodes for player 2  */
    private ArrayList<Coordinate> finish2;

    /**id (1 or 2) for the player  */
    private int playerId;

    /**  A map of all the nodes and its coordinates*/
    private Graph graph;

    private int dimension;

    private Graph copygraph;

    private ArrayList<Node> copyboard;
    /**
     * Method called to initialize a player module. Required task for part 1.
     * Note that for tournaments of multiple games, only one instance of each PlayerModule is created.
     * The initPlayer method is called at the beginning of each game,
     * and must be able to reset the player for the next game.
     * @param dim - size of the smaller dimension of the playing area for one player.
     *            The grid of nodes for that player is of size dim x (dim +1)
     * @param playerId - id ( 1 or 2 ) for this player
     */
    @Override
    public void initPlayer(int dim, int playerId) {
        this.dimension = dim * 2;
        if (dim < 3 || dim > 20){
            throw new IllegalArgumentException("The value for DIM is not in the legal range [3,20]");
        }

        board = new ArrayList<Node>();
        graph = new Graph();
        this.playerId = playerId;
        start1 = new ArrayList<>();
        finish1 = new ArrayList<>();
        start2 = new ArrayList<>();
        finish2 = new ArrayList<>();
        copyboard = new ArrayList<Node>();
        copygraph = new Graph();
        //Initialize board to have a size of (2 * dim + 1) x (2 * dim + 1)
        for (int r = 0; r <= dim * 2; r++) {
            for (int c = 0; c <= dim * 2; c++) {
                Coordinate coordinate = new Coordinate(r, c);
                Node current = new Node(coordinate,0);
                graph.put(coordinate,current);
                board.add(current);
            }
        }
        //Place the start and finish nodes for players 1 and 2
        for (Node node : board) {
            int col = node.getCoordinate().getCol();
            int row = node.getCoordinate().getRow();
            if (col == 0 || col == dim * 2){
                if ((row % 2) != 0) {
                    Coordinate coordinate = new Coordinate(row, col);
                    if (col == 0){
                        start1.add(coordinate);
                    }
                    else{
                        finish1.add(coordinate);
                    }
                    node.assign(1);
                    graph.put(coordinate,node);
                }
            }
            else if (row == 0 || row == dim * 2){
                if ((col % 2) == 1) {
                    Coordinate coordinate = new Coordinate(row, col);
                    node.assign(2);
                    graph.put(coordinate,node);
                    if (row == 0){
                        start2.add(coordinate);
                    }
                    else{
                        finish2.add(coordinate);
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
     * @param m - PlayerMove representing the most recent move
     */
    @Override
    public void lastMove(PlayerMove m) {
        int c = m.getCoordinate().getCol();
        int r = m.getCoordinate().getRow();
        Coordinate north = new Coordinate(r-1,c);
        Coordinate south = new Coordinate(r+1,c);
        Coordinate east = new Coordinate(r,c+1);
        Coordinate west = new Coordinate(r,c-1);

        for (Node node : board){
            if (node.getCoordinate().equals(m.getCoordinate())){
                if (m.getPlayerId() == 1){
                    if ((c % 2) == 1){
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(1);
                        West.assign(1);
                        East.addNeighbor(West,1);
                        West.addNeighbor(East,1);
                        node.addNeighbor(East,1);
                        node.addNeighbor(West,1);
                        node.assign(1);
                        graph.put(east,East);
                        graph.put(west,West);
                    }
                    else if ((c % 2) == 0){
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(1);
                        South.assign(1);
                        North.addNeighbor(South,1);
                        South.addNeighbor(North,1);
                        node.addNeighbor(North,1);
                        node.addNeighbor(South,1);
                        node.assign(1);
                        graph.put(north,North);
                        graph.put(south,South);
                    }
                }
                else if (m.getPlayerId() == 2){
                    if ((c % 2) == 0){
                        Node East = graph.get(east);
                        Node West = graph.get(west);
                        East.assign(2);
                        West.assign(2);
                        East.addNeighbor(West,1);
                        West.addNeighbor(East,1);
                        node.addNeighbor(East,1);
                        node.addNeighbor(West,1);
                        node.assign(1);
                        graph.put(east,East);
                        graph.put(west,West);
                    }
                    else if ((c % 2) == 1){
                        Node North = graph.get(north);
                        Node South = graph.get(south);
                        North.assign(2);
                        South.assign(2);
                        North.addNeighbor(South,1);
                        South.addNeighbor(North,1);
                        node.addNeighbor(North,1);
                        node.addNeighbor(South,1);
                        node.assign(1);
                        graph.put(north,North);
                        graph.put(south,South);
                    }
                }
            }
        }
    }

    /**
     * Part 1 task that tests if a player has won the game given a set of PREMOVEs.
     * @param id - player to test for a winning path.
     * @return boolean value indicating if the player has a winning path.
     */
    public boolean hasWonGame(int id) {

        if (id == 1){
            for (Coordinate start : start1) {
                for (Coordinate finish : finish1){
                    if (graph.canReachBFS(start,finish)){
                        return true;
                    }
                }
            }
        }
        else if (id == 2){
            for (Coordinate start : start2) {
                for (Coordinate finish : finish2){
                    if (graph.canReachBFS(start,finish)){
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
     * @return a PlayerMove object representing the next move.
     */
    @Override
    public PlayerMove move() {
        return null;
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
     *
     * Preconditions: you may assume that there is no winner yet based on prior moves.
     * You may also assume that this method will only be called
     * when it is actually your player's turn based on prior moves.
     * @return a List of all legal PlayerMove objects. They do not have to be in any particular order.
     */
    @Override
    public List allLegalMoves() {
        LinkedList<PlayerMove> Legal = new LinkedList<PlayerMove>();
        for (Node node : board){
            //If player ID is 0 then the slot is empty, meaning that it is a legal position for the next move.
            if (node.getPlayerId() == 0 && node.getCoordinate().getRow() != 0&& node.getCoordinate().getCol() != 0 ){
                if (node.getCoordinate().getRow() != dimension && node.getCoordinate().getCol() != dimension ){
                    if (node.getCoordinate().getRow() % 2 != 0 && node.getCoordinate().getCol() % 2 != 0) {
                        Legal.add(new PlayerMove(node.getCoordinate(), 1));//ID will always be 0
                    }
                    if (node.getCoordinate().getRow() % 2 == 0 && node.getCoordinate().getCol() % 2 == 0) {
                        Legal.add(new PlayerMove(node.getCoordinate(), 1));//ID will always be 0
                    }
                }
            }
            if (node.getPlayerId() == 2 || node.getPlayerId() == 1){
                Legal.remove(new PlayerMove(node.getCoordinate(), 0));//ID will always be 0
            }
        }
        return Legal;
    }

    /**
     * Part 2 task that computes the fewest segments that a given player needs to add to complete a winning path.
     * This ignores the possibility that the opponent might block the path.
     *
     * Precondition: you may assume that the other player has not already won the game.
     * That is, you may assume that a winning path still exists for the player of interest.
     * @param i - the player of interest
     * @return the fewest number of segments to add to complete a path
     */
    @Override
    public int fewestSegmentsToVictory(int i) {
        int num = 0;
        int temp = 100;
//        Graph copy = new Graph(this.graph);
        if (i == 1){
            for (Node node : graph.values()) {
                if (node.getPlayerId() == 1) {
                    Coordinate start = node.getCoordinate();
                    for (Coordinate finish : finish1) {
                        if (!start.equals(finish)) {
                            num = graph.displayShortestPath(start,finish);
                            if (num < temp && num != 0) {
                                temp = num;
                            }
                        }
                    }
                }
            }
        }

        else if (i == 2){
            for (Node node : graph.values()) {
                if (node.getPlayerId() == 2) {
                    Coordinate start = node.getCoordinate();
                    for (Coordinate finish : finish2) {
                        if (!start.equals(finish)) {
                            num = graph.displayShortestPath(start, finish);
                            if (num < temp && num != 0){
                                temp = num;
                            }
                        }
                    }
                }
            }
        }
        return temp;
    }
}