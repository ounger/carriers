import java.util.*;

class AStarAlgorithm extends PathfindingAlgorithm{

	private static final float SQRT_TWO = 1.4142f;

	private PriorityQueue<AStarNode> openList;
	private HashSet<AStarNode> closedList;

	AStarAlgorithm(World world, boolean diagonalMoving){
		super(world, diagonalMoving);
	}

	@Override
	List<Node> execute(){

		openList = new PriorityQueue<>();
		closedList = new HashSet<>();

		AStarNode startNode = new AStarNode(getStartNode().getRow(), getStartNode().getCol(), 0f);
		startNode.f = 0f;
		startNode.g = 0f;

		openList.offer(startNode);

		do{
			AStarNode currentNode = openList.poll();
			if(currentNode == null || (currentNode.getRow() == getEndNode().getRow() && currentNode.getCol() == getEndNode().getCol())){
				return createPath(currentNode);
			}
			closedList.add(currentNode);
			expandNode(currentNode);
		}while(!openList.isEmpty());

		return null;
	}

	private List<Node> createPath(AStarNode endNode){
		List<Node> path = new ArrayList<>();
		AStarNode pointer = endNode;
		path.add(endNode);
		do{
			pointer = pointer.predecessor;
			path.add(pointer);
		}while(pointer.predecessor != null);

		Collections.reverse(path);
		return path;
	}

	private void expandNode(AStarNode currentNode){
		for(AStarNode successor : getSuccessors(currentNode)){

			if(closedList.contains(successor)) continue;

			float tentativeG = currentNode.g + successor.cost;

			if(openList.contains(successor) && tentativeG >= successor.g) continue;

			successor.predecessor = currentNode;
			successor.g = tentativeG;
			successor.f = tentativeG + successor.h;

			if(openList.contains(successor)){
				openList.remove(successor);
				openList.offer(successor);
			}else{
				openList.offer(successor);
			}
		}
	}

	private List<AStarNode> getSuccessors(AStarNode currentNode){
		AStarNode left 		= new AStarNode(currentNode.getRow(), currentNode.getCol() - 1, 1f);
		AStarNode right 	= new AStarNode(currentNode.getRow(), currentNode.getCol() + 1, 1f);
		AStarNode top 		= new AStarNode(currentNode.getRow() - 1, currentNode.getCol(), 1f);
		AStarNode bottom 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol(), 1f);

		AStarNode[] neighbours;

		if(isDiagonalMovingAllowed()){
			AStarNode tl 	= new AStarNode(currentNode.getRow() - 1, currentNode.getCol() - 1, SQRT_TWO);
			AStarNode tr 	= new AStarNode(currentNode.getRow() - 1, currentNode.getCol() + 1, SQRT_TWO);
			AStarNode bl 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol() - 1, SQRT_TWO);
			AStarNode br 	= new AStarNode(currentNode.getRow() + 1, currentNode.getCol() + 1, SQRT_TWO);
			neighbours = new AStarNode[]{tl, tr, bl, br, left, right, top, bottom};
		}else{
			neighbours = new AStarNode[]{left, right, top, bottom};
		}

		List<AStarNode> successors = new ArrayList<>();

		for(AStarNode neighbour : neighbours){
			if(getWorld().inWorld(neighbour) &&
					!closedList.contains(neighbour) &&
					!openList.contains(neighbour) &&
					(getWorld().getNodeTypeAt(neighbour.getRow(), neighbour.getCol()) == NodeType.GROUND ||
							(neighbour.getRow() == getEndNode().getRow() && neighbour.getCol() == getEndNode().getCol()))
			){
				successors.add(neighbour);
			}
		}
		return successors;
	}

	class AStarNode extends Node implements Comparable<AStarNode>{
		private float f = Float.MAX_VALUE;
		private float g = Float.MAX_VALUE;
		private float h;
		private float cost;
		private AStarNode predecessor;

		private AStarNode(final int row, final int col, float cost){
			super(row, col);
			this.h = heuristic();
			this.cost = cost;
		}

		@Override
		public int compareTo(AStarNode other){
			return Float.compare(f, other.f);
		}

		private float heuristic(){
			float dx = Math.abs(getEndNode().getRow() - getRow());
			float dy = Math.abs(getEndNode().getCol() - getCol());
			if(isDiagonalMovingAllowed()){
				float D = 1;
				float D2 = SQRT_TWO;
				return D * Math.max(dx, dy) + (D2-D) * Math.min(dx, dy);
//				return 0f;
			}else{
				return calcEuclidianDistance(dx, dy);
			}
		}

		private float calcEuclidianDistance(float dx, float dy){
			return (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		}

		@Override
		public int hashCode(){
			final int prime = 31;
			int result = 1;
			result = prime * result + getCol();
			result = prime * result + getRow();
			return result;
		}

		@Override
		public boolean equals(Object obj){
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(getClass() != obj.getClass())
				return false;
			AStarNode other = (AStarNode) obj;
			if(getCol() != other.getCol())
				return false;
			return getRow() == other.getRow();
		}

	}
}

