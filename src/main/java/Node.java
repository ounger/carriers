public class Node implements Comparable<Node>{
	private final int row;
	private final int col;
	
	private float f;
	private float g;
	private float h;
	
	private Node successor;
	private Node predecessor;
	
	public Node(final int row, final int col) {
		this.row = row;
		this.col = col;
	}
	
	public void setFValue(float newFValue) {
		this.f = newFValue;
	}
	
	public void setGValue(float newGValue) {
		this.g = newGValue;
	}
	
	public float getGValue(int startRow, int startCol) {
		if(g == 0f) g = calcGValue(startRow, startCol);
		return g;
	}
	
	public float calcGValue(int startRow, int startCol) {
		return calcEuclidianDistance(this.row, this.col, startRow, startCol);
	}
	
	public void setHValue(float newHValue) {
		this.h = newHValue;
	}
	
	public float getHValue(int endRow, int endCol) {
		if(h == 0f) h = calcHValue(endRow, endCol);
		return h;
	}
	
	public float calcHValue(int endRow, int endCol) {
		return calcEuclidianDistance(this.row, this.col, endRow, endCol);
	}
	
	public void setSuccessor(Node successor) {
		this.successor = successor;
	}
	
	public Node getSuccessor() {
		return successor;
	}
	
	public boolean hasSuccessor() {
		return successor != null;
	}
	
	public Node getPredecessor() {
		return predecessor;
	}
	
	public boolean hasPredecessor() {
		return predecessor != null;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public float getFValue() {
		return f;
	}

	@Override
	public int compareTo(Node other) {
		return new Float(this.f).compareTo(new Float(other.f));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		Node other = (Node) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public static float calcEuclidianDistance(
			int row1, int col1, int row2, int col2) {
		int absRowDistance = Math.abs(row1 - row2);
		int absColDistance = Math.abs(col1 - col2);
		return (float) Math.sqrt(
				Math.pow(absRowDistance, 2)
				+
				Math.pow(absColDistance, 2));
	}
	
	public void setPredecessor(Node pre) {
		this.predecessor = pre;
	}
	
	public Node predecessor() {
		return predecessor;
	}
}



