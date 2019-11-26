import java.util.List;
import java.util.Random;

abstract class PathfindingAlgorithm {

	private final World world;
	
	private int startRow 	= -1;
	private int startCol 	= -1;
	private int endRow 		= -1;
	private int endCol 		= -1;
	
	PathfindingAlgorithm(World world) {
		if(world == null) throw new IllegalArgumentException("Missing world!");
		this.world = world;
	}
	
	/**
	 * Execute with random generated Points within the world.
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
		}while(!checkStartAndEnd(startRow, startCol, endRow, endCol));
		
		return execute(startRow, startCol, endRow, endCol);
	}
	
	final List<Node> execute(
			int startRow, 	int startCol,
			int endRow, 	int endCol) {
		
		if(checkStartAndEnd(startRow, startCol, endRow, endCol)) {
			this.startRow = startRow;
			this.startCol = startCol;
			this.endRow = endRow;
			this.endCol = endCol;
			
			return execute();
		}
		else {
			throw new IllegalArgumentException("Check the points!");
		}
	}
	
	/**
	 * Solves the single source shortest path problem.
	 * @return The path if it exists.
	 */
	abstract List<Node> execute();
	
	private final boolean checkStartAndEnd(
			int startRow, 	int startCol,
			int endRow, 	int endCol) {
		return	
			!(startRow == endRow && startCol == endCol) &&
			world.inWorld(startRow, startCol) && 
			world.inWorld(endRow, endCol);
	}
	
	final World getWorld() {
		return world;
	}
	
	final int getStartRow() {
		return startRow;
	}
	
	final int getStartCol() {
		return startCol;
	}
	
	final int getEndRow() {
		return endRow;
	}
	
	final int getEndCol() {
		return endCol;
	}
	
}
