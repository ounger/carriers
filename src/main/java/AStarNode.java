public class AStarNode extends Node implements Comparable<AStarNode>{
	private float f;
	private float g;
	private float h;
	
	private AStarNode successor;
	private AStarNode predecessor;
	
	public AStarNode(final int row, final int col) {
		super(row, col);
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
		return calcEuclidianDistance(getRow(), getCol(), startRow, startCol);
	}
	
	public void setHValue(float newHValue) {
		this.h = newHValue;
	}
	
	public float getHValue(int endRow, int endCol) {
		if(h == 0f) h = calcHValue(endRow, endCol);
		return h;
	}
	
	public float calcHValue(int endRow, int endCol) {
		return calcEuclidianDistance(getRow(), getCol(), endRow, endCol);
	}
	
	public void setSuccessor(AStarNode successor) {
		this.successor = successor;
	}
	
	public AStarNode getSuccessor() {
		return successor;
	}
	
	public boolean hasSuccessor() {
		return successor != null;
	}
	
	public AStarNode getPredecessor() {
		return predecessor;
	}
	
	public boolean hasPredecessor() {
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
		if (getRow()!= other.getRow())
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
	
	public void setPredecessor(AStarNode pre) {
		this.predecessor = pre;
	}
	
	public AStarNode predecessor() {
		return predecessor;
	}
}



