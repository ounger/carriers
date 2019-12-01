public class Dijkstra extends AStar{

	Dijkstra(World world, boolean diagonalMoving) {
		super(world, diagonalMoving);
	}

	@Override
	int heuristic(Node node){
		return 0;
	}
}
