import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		World world = World.getInstance();
		world.setSize(20, 20);
		world.generateTerrainAuto(1f);
		world.generateTerrainAuto(0.25f);
		
		PathfindingAlgorithm astar = new AStarAlgorithm(world, true);
		List<Node> path = astar.executeRandom();
		WorldPrinter.printWorldWithPointsAndPath(astar, null);

		System.out.println("Found path -> " + (path != null));
		if(path != null) {
			for(int i = 0; i < path.size(); i++) {
				Node node = path.get(i);
				System.out.print(
						"(" + node.getRow() + "," + node.getCol() + ") "
				);
			}
			System.out.println("\n");
			WorldPrinter.printWorldWithPointsAndPath(astar, path);
		}
	}
}
