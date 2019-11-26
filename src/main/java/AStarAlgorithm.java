import java.util.*;

class AStarAlgorithm extends PathfindingAlgorithm{
	
	private PriorityQueue<AStarNode> openList;
	private Set<AStarNode> closedList;
	
	AStarAlgorithm(World world) {
		super(world);
	}
	
	@Override
	List<AStarNode> execute() {
		
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
	
	private List<AStarNode> createPath(AStarNode endNode){
		List<AStarNode> path = new ArrayList<>();
		AStarNode pointer = endNode;
		path.add(endNode);
		do {
			pointer = pointer.getPredecessor();
			path.add(pointer);
		}while(pointer.hasPredecessor());
		
		Collections.reverse(path);
		return path;
	}
}

