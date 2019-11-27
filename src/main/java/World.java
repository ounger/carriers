import java.util.Random;

public class World{
	
	private static World INSTANCE;

	private static float DEFAULT_SOLID_PROBABILITY = 0.2f;
	
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 200;

	private NodeType[][] nodes;
	
	private World() {}
	
	static World getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new World();
			INSTANCE.setRandomSize();
		}
		return INSTANCE;
	}
	
	public void setRandomSize() {
		setSize(
				new Random().nextInt(MAX_SIZE - (MIN_SIZE + 1) + 1) + (MIN_SIZE + 1), //at least 2 to avoid (1,1)
				new Random().nextInt(MAX_SIZE - MIN_SIZE + 1) + MIN_SIZE	
		);
	}
	
	public void setSize(int width, int height) throws IllegalArgumentException{
		if(width == 1 && height == 1)
			throw new IllegalArgumentException("Length of at least "
					+ "one side has to be > 1.");
		if(!inBounds(width, height)) {		
			throw new IllegalArgumentException("Size out of bounds "
					+ "[" + MIN_SIZE + ", " + MAX_SIZE + "]");
		}
		nodes = new NodeType[width][height];
		generateEmptyTerrain();
	}
	
	private boolean inBounds(int width, int height) {
		return 	(
				width 	>= 	MIN_SIZE 	&& 
				width 	<= 	MAX_SIZE 	&& 
				height 	>= 	MIN_SIZE 	&& 
				height 	<= 	MAX_SIZE
				);
	}
	
	public void generateEmptyTerrain() {
		generateTerrainAuto(0f);
	}
	
	public void generateTerrainAuto(){
		generateTerrainAuto(DEFAULT_SOLID_PROBABILITY);
	}
	
	public void generateTerrainAuto(float solidProbability){ 
		if(solidProbability > 1) solidProbability = 1;
		if(solidProbability < 0) solidProbability = 0;
		for(int row = 0; row < nodes[0].length; row++) {
			for(int col = 0; col < nodes.length; col++) {
				if(Math.random() < solidProbability)
					nodes[col][row] = NodeType.SOLID;
				else
					nodes[col][row] = NodeType.GROUND;
			}
		}
	}
	
	public boolean inWorld(Node node) {
		return (node.getRow() >= 0 					&&
				node.getRow() < getWorldHeight() 	&&
				node.getCol() >= 0					&&
				node.getCol() < getWorldWidth()
		);
	}

	public boolean startOrEndNodeFeasible(Node node){
		return inWorld(node) && getNodeTypeAt(node.getRow(), node.getCol()) == NodeType.GROUND;
	}
	
	public int getWorldWidth() {
		return nodes.length;
	}
	
	public int getWorldHeight() {
		return nodes[0].length;
	}
	
	public NodeType getNodeTypeAt(int row, int col){
		return nodes[col][row];
	}
}




