import java.util.List;

public class WorldPrinter {
	
	static void printWorldWithPointsAndPath( 
			PathfindingAlgorithm pfa, List<AStarNode> path) {
		if(pfa.getWorld() == null) 
			throw new IllegalArgumentException("World is missing!");
		
		printHeader("Generated World with Path and Points ", pfa);
		
		System.out.print("|");
		for(int i = 0; i < pfa.getWorld().getWorldWidth(); i++)
			System.out.print("==");
		System.out.println("=|");
		for(int row = 0; row < pfa.getWorld().getWorldHeight(); row++) {
			System.out.print("| ");
			for(int col = 0; col < pfa.getWorld().getWorldWidth(); col++) {
				if(pfa != null && row == pfa.getStartNode().getRow() && col == pfa.getStartNode().getCol()) {
					System.out.print(NodeType.START.asChar() + " ");
				}
				else if(pfa != null && row == pfa.getEndNode().getRow() && col == pfa.getEndNode().getCol()) {
					System.out.print(NodeType.END.asChar() + " ");
				}
				else if(path == null || !path.contains(new AStarNode(row, col))) {
					System.out.print(
							pfa.getWorld().getNodeTypeAt(row, col).asChar() + " ");
				}
				else {
					System.out.print(NodeType.PATH.asChar() + " ");
				}
			}
			System.out.println("| ");
		}
		System.out.print("|");
		for(int i = 0; i < pfa.getWorld().getWorldWidth(); i++)
			System.out.print("==");
		System.out.println("=|");
		System.out.println("");
	}
	
	private static void printHeader(String text, PathfindingAlgorithm pfa) {
		System.out.println(text);
		String points = "";
		if(pfa != null) {
			points = "S(" + pfa.getStartNode().getRow() + "," + pfa.getStartNode().getCol() + "), " +
					"S(" + pfa.getEndNode().getRow() + "," + pfa.getEndNode().getCol() + ")";
		}
		System.out.println("World:width = " +
				pfa.getWorld().getWorldWidth() +
				", " +
				"World:height = " +
				pfa.getWorld().getWorldHeight() + " " +
				points);
	}
	
}
