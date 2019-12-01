import java.util.*;

class AStar extends PathfindingAlgorithm{

	private PriorityQueue<AStarNode> openList;
	private HashSet<AStarNode> closedSet;

	AStar(World world, boolean diagonalMoving){
		super(world, diagonalMoving);
	}

	@Override
	List<Node> execute(){
		openList = new PriorityQueue<>(
				Comparator.comparingInt(aStarNode -> aStarNode.f)
		);
		closedSet = new HashSet<>();

		AStarNode startNode = new AStarNode(getStartNode().getRow(), getStartNode().getCol(), 0);
		startNode.f = 0;
		startNode.g = 0;

		openList.offer(startNode);

		do{
			AStarNode currentNode = openList.poll();
			if(currentNode == null || (currentNode.getRow() == getEndNode().getRow() && currentNode.getCol() == getEndNode().getCol())){
				return createPath(currentNode);
			}
			closedSet.add(currentNode);
			expandNode(currentNode);
		}while(!openList.isEmpty());

		return null;
	}

	private List<Node> createPath(AStarNode endNode){
		List<Node> path = new ArrayList<>();
		AStarNode pointer = endNode;
		path.add(endNode);
		while(pointer.predecessor != null){
			pointer = pointer.predecessor;
			path.add(pointer);
		}
		Collections.reverse(path);
		return path;
	}

	private void expandNode(AStarNode currentNode){
		for(AStarNode successor : getSuccessors(currentNode)){
			if(closedSet.contains(successor))
				continue;
			int tentativeG = currentNode.g + successor.cost;
			if(tentativeG < successor.g){
				successor.predecessor = currentNode;
				successor.g = tentativeG;
				successor.f = successor.g + successor.h;
				if(!openList.contains(successor)){
					openList.offer(successor);
				}
			}
		}
	}

	private List<AStarNode> getSuccessors(AStarNode currentNode){
		AStarNode left 		= new AStarNode(currentNode.getRow(), currentNode.getCol() - 1, 10);
		AStarNode right 	= new AStarNode(currentNode.getRow(), currentNode.getCol() + 1, 10);
		AStarNode top 		= new AStarNode(currentNode.getRow() - 1, currentNode.getCol(), 10);
		AStarNode bottom 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol(), 10);

		AStarNode[] neighbours;

		if(isDiagonalMovingAllowed()){
			AStarNode tl 	= new AStarNode(currentNode.getRow() - 1, currentNode.getCol() - 1, 14);
			AStarNode tr 	= new AStarNode(currentNode.getRow() - 1, currentNode.getCol() + 1, 14);
			AStarNode bl 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol() - 1, 14);
			AStarNode br 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol() + 1, 14);
			neighbours = new AStarNode[]{tl, tr, bl, br, left, right, top, bottom};
		}else{
			neighbours = new AStarNode[]{left, right, top, bottom};
		}

		List<AStarNode> successors = new ArrayList<>();

		for(AStarNode neighbour : neighbours){
			if(getWorld().inWorld(neighbour) &&
					(getWorld().getNodeTypeAt(neighbour.getRow(), neighbour.getCol()) == NodeType.GROUND ||
							neighbour.equals(getEndNode()))
			){
				successors.add(neighbour);
			}
		}
		return successors;
	}

	int heuristic(Node node){
		if(isDiagonalMovingAllowed()){
			return calcChebyshevDistance(node.getRow(), node.getCol(), getEndNode().getRow(), getEndNode().getCol());
		}else{
			return calcManhattanDistance(node.getRow(), node.getCol(), getEndNode().getRow(), getEndNode().getCol());
		}
	}

	private int calcManhattanDistance(int row1, int col1, int row2, int col2){
		int dx = Math.abs(row1 - row2);
		int dy = Math.abs(col1 - col2);
		return dx + dy;
	}

	private int calcChebyshevDistance(int row1, int col1, int row2, int col2){
		int dx = Math.abs(row1 - row2);
		int dy = Math.abs(col1 - col2);
		int D = 1;
		int D2 = 1;
		return D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);
	}

	private class AStarNode extends Node{
		private int f = 1_000_000;
		private int g = 1_000_000;
		private int h;
		private int cost;
		private AStarNode predecessor;

		private AStarNode(int row, int col, int cost){
			super(row, col);
			this.h = heuristic(this);
			this.cost = cost;
		}
	}
}

