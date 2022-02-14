public class CanvasPanelCells {
	private int x;
	private int y;
	//the cells is used to update the gui
	private final int UNPROCESSED = 1;
	private final int BARRIER = 2;
	private final int START = 3;
	private final int END = 4;
	private final int PROCESSED = 5;
	private final int PROCESSING = 6;
	//this is used to keep track of the status of the cells
	//duplicate??
	private final int STATUS_PROCESSED = 1;
	private final int STATUS_PROCESSING = 2;
	private final int STATUS_UNPROCESSED = 3;
	private final int STATUS_SPECIAL = 4;
	private final static int STATUS_BARRIER = 5;
	private int[][] cells; 
	//status will keep track of the state of the cell
	//1 being already processed cell
	//2 being currently processing cell
	//3 being unprocessed cell
	private int[][] status; 
	private int[][] distance; 
	public CanvasPanelCells(int x, int y) {
		this.x = x;
		this.y = y;
		this.cells = new int[x][y];
		this.status = new int[x][y];
		this.distance = new int[x][y];
		for(int i = 0; i<x; i++) {
			for (int j = 0; j<y; j++) {
				this.cells[i][j] = UNPROCESSED;
				this.status[i][j] = STATUS_UNPROCESSED;
				this.distance[i][j] = Integer.MAX_VALUE;
			}

		}
	}
	
	public int getStatus(int x, int y) {
		return this.status[x][y];
	}
	
	public void setStatus(int x, int y, int state) {
		//System.out.println("setStatus x = " + x + "y = " + y + "state = " + state);
		if(getStatus(x,y)!=STATUS_SPECIAL) {
			this.status[x][y] = state;
		}
	}
	
	public int[][] getDistanceArray () {
		return this.distance;
	}
	
	public int getDistanceCell (int x, int y) {
		return this.distance[x][y];
	}
	
	public void setDistance(int x, int y, int d) {
		this.distance[x][y] = d;
	}
	
	public int[][] getCells () {
		return this.cells;
	}
	
	public int getCell (int x, int y) {
		return this.cells[x][y];
	}
	
	public void setCell(int x, int y, int z) {
		if(z==START||z==END) {
			//check if a start/end point already exist
			for(int i = 0; i<this.x; i++) {
				for (int j = 0; j<this.y; j++) {
					if(this.cells[i][j]==z&&(i!=x||j!=y)) {
						this.cells[i][j]=UNPROCESSED;
					}
				}
			}
			this.status[x][y]=z;			
		}
		if(this.cells[x][y]!=START&&this.cells[x][y]!=END) {
			this.cells[x][y]=z;
		}
	}
}
