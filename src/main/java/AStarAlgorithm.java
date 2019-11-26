import java.util.*;

class AStarAlgorithm extends PathfindingAlgorithm{
	
	private PriorityQueue<Node> openList;
	private Set<Node> closedList;
	
	AStarAlgorithm(World world) {
		super(world);
	}
	
	@Override
	List<Node> execute() {
		
		openList = new PriorityQueue<>();
		closedList = new HashSet<>();
		
		Node startNode = new Node(getStartRow(), getStartCol());
		startNode.setFValue(0f);
		openList.offer(startNode);
		
		do {
			Node currentNode = openList.poll();
			if(	currentNode.getRow() == getEndRow() &&
				currentNode.getCol() == getEndCol()) {
				return createPath(currentNode);
			}
			closedList.add(currentNode);
			expandNode(currentNode);
		}while(!openList.isEmpty());
		
		return null;
	}
	
	private void expandNode(Node node) {
		for(Node successor : getSuccessors(node)) {
			if(closedList.contains(successor)) {
				continue;
			}
			
			float tentativeG = node.calcGValue(getStartRow(), getStartCol()) + 1;
			
			if( openList.contains(successor) && 
				tentativeG >= successor.getGValue(getStartRow(), getStartCol())) {
				continue;
			}
			
			successor.setPredecessor(node);
			successor.setGValue(tentativeG);
			
			float f = tentativeG + successor.calcHValue(getEndRow(), getEndCol());
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
	
	private List<Node> getSuccessors(Node node){
		List<Node> successors = new ArrayList<>();
		Node left 	= new Node(node.getRow(), node.getCol() - 1);
		Node right 	= new Node(node.getRow(), node.getCol() + 1);
		Node top 	= new Node(node.getRow() - 1, node.getCol());
		Node bottom = new Node(node.getRow() + 1, node.getCol());
		
		Node[] neighbours = {left, right, top, bottom};
		
		for(int i = 0; i < neighbours.length; i++) {
			Node neighbour = neighbours[i];
			if(	getWorld().inWorld(
					neighbour.getRow(), 
					neighbour.getCol())
				&&
				(getWorld().getNodeTypeAt(
						neighbour.getRow(),
						neighbour.getCol()) == NodeType.GROUND
						||
						(neighbour.getRow() == getEndRow()
						&&
						neighbour.getCol() == getEndCol()))){
				successors.add(neighbour);
			}
		}
		
		return successors;
	}
	
	private List<Node> createPath(Node endNode){
		List<Node> path = new ArrayList<>();
		Node pointer = endNode;
		path.add(endNode);
		do {
			pointer = pointer.getPredecessor();
			path.add(pointer);
		}while(pointer.hasPredecessor());
		
		Collections.reverse(path);
		return path;
	}
}

