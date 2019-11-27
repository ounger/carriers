import java.util.List;
import java.util.Random;

abstract class PathfindingAlgorithm {

	private final World world;

	private boolean diagonalMoving;

	private Node startNode;
	private Node endNode;
	
	PathfindingAlgorithm(World world, boolean diagonalMoving) {
		if(world == null) throw new IllegalArgumentException("Missing world!");
		this.world = world;
		this.startNode = new Node(-1, -1);
		this.endNode = new Node(-1, -1);
		this.diagonalMoving = diagonalMoving;
	}
	
	/**
	 * Execute with random generated Nodes within the world.
	 */
	final List<Node> executeRandom() {
		int startRow;
		int startCol;
		int endRow;
		int endCol;
		Random random = new Random();
		
		do {
			startRow 	= random.nextInt(world.getWorldHeight());
			startCol	= random.nextInt(world.getWorldWidth());
			endRow 		= random.nextInt(world.getWorldHeight());
			endCol 		= random.nextInt(world.getWorldWidth());
		}while(!checkStartAndEnd(new Node(startRow, startCol), new Node(endRow, endCol)));
		
		return execute(new Node(startRow, startCol), new Node(endRow, endCol));
	}
	
	final List<Node> execute(
			Node startNode,
			Node endNode) {
		
		if(checkStartAndEnd(startNode, endNode)) {
			this.startNode = startNode;
			this.endNode = endNode;
			
			return execute();
		}
		else {
			throw new IllegalArgumentException("Check the nodes!");
		}
	}
	
	/**
	 * Solves the single source shortest path problem.
	 * @return The path if it exists.
	 */
	abstract List<Node> execute();
	
	private final boolean checkStartAndEnd(
			Node startNode,
			Node endNode) {
		return	
			!(startNode.getRow() == endNode.getRow() && startNode.getCol() == endNode.getCol()) &&
			world.startOrEndNodeFeasible(startNode) &&
			world.startOrEndNodeFeasible(endNode);
	}
	
	final World getWorld() {
		return world;
	}
	
	final Node getStartNode(){
		return startNode;
	}

	final Node getEndNode(){
		return endNode;
	}

	final boolean isDiagonalMovingAllowed(){
		return diagonalMoving;
	}

}
