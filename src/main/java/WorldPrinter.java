import java.util.List;

public class WorldPrinter {
	
	static void printWorldWithPointsAndPath( 
			PathfindingAlgorithm pfa, List<Node> path) {
		if(pfa.getWorld() == null) 
			throw new IllegalArgumentException("main.World is missing!");
		
		printHeader("Generated main.World with Path and Points ", pfa);
		
		System.out.print("|");
		for(int i = 0; i < pfa.getWorld().getWorldWidth(); i++)
			System.out.print("==");
		System.out.println("=|");
		for(int row = 0; row < pfa.getWorld().getWorldHeight(); row++) {
			System.out.print("| ");
			for(int col = 0; col < pfa.getWorld().getWorldWidth(); col++) {
				if(pfa != null && row == pfa.getStartRow() && col == pfa.getStartCol()) {
					System.out.print(NodeType.START.asChar() + " ");
				}
				else if(pfa != null && row == pfa.getEndRow() && col == pfa.getEndCol()) {
					System.out.print(NodeType.END.asChar() + " ");
				}
				else if(path == null || !path.contains(new Node(row, col))) {
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
			points = "S(" + pfa.getStartRow() + "," + pfa.getStartCol() + "), " +
					"S(" + pfa.getEndRow() + "," + pfa.getEndCol() + ")";
		}
		System.out.println("main.World:width = " +
				pfa.getWorld().getWorldWidth() +
				", " +
				"main.World:height = " +
				pfa.getWorld().getWorldHeight() + " " +
				points);
	}
	
}
