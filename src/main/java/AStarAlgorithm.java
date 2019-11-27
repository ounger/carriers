import java.util.*;

class AStarAlgorithm extends PathfindingAlgorithm{
	
	private PriorityQueue<AStarNode> openList;
	private Set<AStarNode> closedList;
	
	AStarAlgorithm(World world) {
		super(world);
	}
	
	@Override
	List<Node> execute() {
		
		openList = new PriorityQueue<>();
		closedList = new HashSet<>();
		
		AStarNode startNode = new AStarNode(getStartNode().getRow(), getStartNode().getCol());
		startNode.setFValue(0f);
		openList.offer(startNode);
		
		do {
			AStarNode currentNode = openList.poll();
			if(	currentNode.getRow() == getEndNode().getRow() &&
				currentNode.getCol() == getEndNode().getCol()) {
				return createPath(currentNode);
			}
			closedList.add(currentNode);
			expandNode(currentNode);
		}while(!openList.isEmpty());
		
		return null;
	}
	
	private void expandNode(AStarNode node) {
		for(AStarNode successor : getSuccessors(node)) {
			if(closedList.contains(successor)) {
				continue;
			}
			
			float tentativeG = node.calcGValue(getStartNode().getRow(), getStartNode().getCol()) + 1;
			
			if( openList.contains(successor) && 
				tentativeG >= successor.getGValue(getStartNode().getRow(), getStartNode().getCol())) {
				continue;
			}
			
			successor.setPredecessor(node);
			successor.setGValue(tentativeG);
			
			float f = tentativeG + successor.calcHValue(getEndNode().getRow(), getEndNode().getCol());
			if (openList.contains(successor)) {
	        	openList.remove(successor);
	        	successor.setFValue(f);
	        	openList.offer(successor);
	        }
	        else {
	        	successor.setFValue(f);
	        	openList.offer(successor);
	        }
		}
	}
	
	private List<AStarNode> getSuccessors(AStarNode node){
		List<AStarNode> successors = new ArrayList<>();
		AStarNode left 	= new AStarNode(node.getRow(), node.getCol() - 1);
		AStarNode right 	= new AStarNode(node.getRow(), node.getCol() + 1);
		AStarNode top 	= new AStarNode(node.getRow() - 1, node.getCol());
		AStarNode bottom = new AStarNode(node.getRow() + 1, node.getCol());
		
		AStarNode[] neighbours = {left, right, top, bottom};
		
		for(int i = 0; i < neighbours.length; i++) {
			AStarNode neighbour = neighbours[i];
			if(	getWorld().inWorld(neighbour)
				&&
				(getWorld().getNodeTypeAt(
						neighbour.getRow(),
						neighbour.getCol()) == NodeType.GROUND
						||
						(neighbour.getRow() == getEndNode().getRow()
						&&
						neighbour.getCol() == getEndNode().getCol()))){
				successors.add(neighbour);
			}
		}
		
		return successors;
	}
	
	private List<Node> createPath(AStarNode endNode){
		List<Node> path = new ArrayList<>();
		AStarNode pointer = endNode;
		path.add(endNode);
		do {
			pointer = pointer.getPredecessor();
			path.add(pointer);
		}while(pointer.hasPredecessor());
		
		Collections.reverse(path);
		return path;
	}

	class AStarNode extends Node implements Comparable<AStarNode> {
		private float f;
		private float g;
		private float h;

		private AStarNode predecessor;

		AStarNode(final int row, final int col) {
			super(row, col);
		}

		void setFValue(float newFValue) {
			this.f = newFValue;
		}

		void setGValue(float newGValue) {
			this.g = newGValue;
		}

		float getGValue(int startRow, int startCol) {
			if (g == 0f) g = calcGValue(startRow, startCol);
			return g;
		}

		float calcGValue(int startRow, int startCol) {
			return calcEuclidianDistance(startRow, startCol);
		}

		public void setHValue(float newHValue) {
			this.h = newHValue;
		}

		public float getHValue(int endRow, int endCol) {
			if (h == 0f) h = calcHValue(endRow, endCol);
			return h;
		}

		float calcHValue(int endRow, int endCol) {
			return calcEuclidianDistance(endRow, endCol);
		}

		AStarNode getPredecessor() {
			return predecessor;
		}

		boolean hasPredecessor() {
			return predecessor != null;
		}

		public float getFValue() {
			return f;
		}

		@Override
		public int compareTo(AStarNode other) {
			return new Float(this.f).compareTo(new Float(other.f));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getCol();
			result = prime * result + getRow();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AStarNode other = (AStarNode) obj;
			if (getCol() != other.getCol())
				return false;
			if (getRow() != other.getRow())
				return false;
			return true;
		}

		float calcEuclidianDistance(
				int row, int col) {
			int absRowDistance = Math.abs(this.getRow() - row);
			int absColDistance = Math.abs(this.getCol() - col);
			return (float) Math.sqrt(
					Math.pow(absRowDistance, 2)
							+
							Math.pow(absColDistance, 2));
		}

		void setPredecessor(AStarNode pre) {
			this.predecessor = pre;
		}

		public AStarNode predecessor() {
			return predecessor;
		}

	}

}

