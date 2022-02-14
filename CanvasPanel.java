import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.w3c.dom.ranges.RangeException;
public class CanvasPanel extends JPanel{
	//the length of individual cells in pixels
    private  int sideLength = 20;
    //number of cells in a row
    private static  int x;
    //number of rows
    private static  int y;
    //cells keep tracks of the status of all the cells 
    static CanvasPanelCells cells;
    int d;
    //keep track of the coordinate of the start and end point
    private static int startPointX, startPointY, endPointX, endPointY;
	//the cells is used to update the gui
	private final static int UNPROCESSED = 1;
	private final static int BARRIER = 2;
	private final static int START = 3;
	private final static int END = 4;
	private final static int PROCESSED = 5;
	private final static int PROCESSING = 6;
	//this is used to keep track of the status of the cells
	//duplicate??
	private final static int STATUS_PROCESSED = 1;
	private final static int STATUS_PROCESSING = 2;
	private final static int STATUS_UNPROCESSED = 3;
	private final static int STATUS_SPECIAL = 4;
	private final static int STATUS_BARRIER = 5;
    
	private static class Coordinate {
		private int x, y;
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
	}

	
    public CanvasPanel(int x, int y, int startX, int startY, int endX, int endY) {
    	this.cells =  new CanvasPanelCells(x,y);
    	this.x = x;
		this.y = y;
		this.endPointX = endX;
		this.endPointY = endY;
		this.startPointX = startX;
		this.startPointY = startY;
		setEndPoint(endPointX, endPointY);
		setStartPoint(startPointX, startPointY);
		setPreferredSize(new Dimension(x*sideLength,y*sideLength));

        addMouseListener(new Mouse());
    }
    
    public static void setEndPoint(int x, int y) {
    	cells.setStatus(x, y, STATUS_SPECIAL);
		cells.setCell(x, y, END); 
    }
    
    public static void setStartPoint(int x, int y) {
    	cells.setStatus(x, y, STATUS_SPECIAL);
    	cells.setDistance(x, y, 0);
		cells.setCell(x, y, START);
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        try {
            createCanvas(x,y,g,sideLength);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createCanvas(int x, int y, Graphics g, int sideLength) throws InterruptedException {
        int coordX=1;
        int coordY=1;
        for(int i=0; i<x;i++) {
            for(int j=0; j<y;j++) {
                paintRectangle(g,intToColor(this.cells.getCell(i, j)),coordX,coordY,sideLength-1,sideLength-1);
                coordY=coordY+sideLength;
            }
            coordY=1;
            coordX=coordX+sideLength;
        }
    }
    
    //convert the int values from CanvasPanelCells to color
	//1 being default/unprocessed; 
	//2 being barrier; 
	//3 being start point; 
	//4 being end point; 
	//5 being path;
	//6 being already processed;
    private Color intToColor(int x) {
    	Color returnColor = null;
    	switch(x) {
    		case 1:
    			returnColor = Color.LIGHT_GRAY;
    			break;
    		case 2:
    			returnColor = Color.BLACK;
    			break;
    		case 3:
    			returnColor = Color.GREEN;
    			break;
    		case 4:
    			returnColor = Color.RED;
    			break;
    		case 5:
    			returnColor = Color.MAGENTA;
    			break;
    		case 6:
    			returnColor = Color.CYAN;
    			break;  			 		
    	}
    	return returnColor;
    }
    
    private int colorToInt(Color c) {
    	if(c == Color.LIGHT_GRAY) {
    		return UNPROCESSED;
    	}
    	if(c == Color.BLACK) {
    		return BARRIER;
    	}
    	if(c == Color.GREEN) {
    		return START;
    	}
    	if(c == Color.RED) {
    		return END;
    	}
    	if(c == Color.MAGENTA) {
    		return PROCESSED;
    	}
    	if(c == Color.CYAN) {
    		return PROCESSING;
    	}
		return 0;
    }
    
    //this function converts the coordinates of the mouse click to the corresponding index of the cell
    private Coordinate mouseClickLocationToCellIndex(int x, int y) {
    	Coordinate c = new Coordinate(x/sideLength, y/sideLength);
       	return c;
    }

    //start from the start point
  	//for every cell 
    public static void dikstraStart() {
    	dikstraAlgo(startPointX+1, startPointY);
    	dikstraAlgo(startPointX-1, startPointY);
    	dikstraAlgo(startPointX, startPointY+1);
    	dikstraAlgo(startPointX, startPointY-1);
    	dikstraAlgo(startPointX+1, startPointY+1);
    	dikstraAlgo(startPointX+1, startPointY-1);
    	dikstraAlgo(startPointX-1, startPointY+1);
    	dikstraAlgo(startPointX-1, startPointY-1);
    	getPath();
    }
    
  	private static boolean dikstraAlgo(int x, int y) {
  		if(isChecked(endPointX, endPointY)) {
  			//System.out.println("endPoint termination");
  			//System.out.println(cells.getStatus(endPointX-1, endPointY+1));
  			return false;
  		}
  		
 		//coords within bound
  		if(!isCoordsValid(x,y)) {
  			//System.out.println("out of bounds termination");
  			return false;
  		}
  		
  		//cell isnt processed/barrier
  		if(!isCellValid(x,y)) {
  			//System.out.println("cell termination");
  			return false;
  		}
  		
  		//System.out.println("x = " + x + "y = " + y);
  		
  		//set cell to processed if all adjacent cells are processing
  		//set cell to processing otherwise

  		cells.setStatus(x,y,STATUS_PROCESSING);
  		if(!((x==endPointX)&&(y==endPointY))) {
  			cells.setCell(x, y, PROCESSING);
  		}
		
		Coordinate c = getSmallestAdjacentCell(x,y);
		
		//if a new smallest distance have emerged
		if(cells.getDistanceCell(c.x,c.y)+1<cells.getDistanceCell(x, y)) {
  			cells.setDistance(x,y, cells.getDistanceCell(c.x,c.y)+1);
		}
		
  		//east
  		if(dikstraAlgo(x+1,y)) {
  			return true;
  		}
  		//west
  		if(dikstraAlgo(x-1,y)) {
  			return true;
  		}
  		//north
  		if(dikstraAlgo(x,y+1)) {
  			return true;
  		}
  		//south
  		if(dikstraAlgo(x,y-1)) {
  			return true;
  		}
  		//north east
  		if(dikstraAlgo(x+1,y+1)) {
  			return true;
  		}
  		//south east
  		if(dikstraAlgo(x+1,y-1)) {
  			return true;
  		}
  		//north west
  		if(dikstraAlgo(x-1,y+1)) {
  			return true;
  		}
  		//south west
  		if(dikstraAlgo(x-1,y-1)) {
  			return true;
  		}
  		return false;
    }
  	
  	private boolean setBlockage(int x, int y) {
  		try {
  			this.cells.setCell(x, y, BARRIER);
  			this.cells.setStatus(x, y, STATUS_BARRIER);
  		}catch(RangeException e){
  			return false;
  		}
  		return true;
  	}
  	
  	static void reset() {
		for(int i = 0; i<x; i++) {
			for (int j = 0; j<y; j++) {
				cells.setCell(i, j, UNPROCESSED);
				cells.setStatus(i, j, STATUS_UNPROCESSED);
				cells.setDistance(i, j, Integer.MAX_VALUE);
			}
		}
  	}
  	
  	//returns the coord of the adjacent cell with the smallest distance
  	private static Coordinate getSmallestAdjacentCell(int x, int y) {
  		int smallestDistance = Integer.MAX_VALUE;
  		Coordinate returnCoord = null;

  		if(isCoordsValid(x+1,y) && cells.getDistanceCell(x+1, y)<smallestDistance) {
  			returnCoord = new Coordinate(x+1, y);
  			smallestDistance = cells.getDistanceCell(x+1, y);
  		}
  		if(isCoordsValid(x-1,y) && cells.getDistanceCell(x-1, y)<smallestDistance) {
  			returnCoord = new Coordinate(x-1, y);
  			smallestDistance = cells.getDistanceCell(x-1, y);
  		}
  		if(isCoordsValid(x,y+1) && cells.getDistanceCell(x, y+1)<smallestDistance) {
  			returnCoord = new Coordinate(x, y+1);
  			smallestDistance = cells.getDistanceCell(x, y+1);
  		}
  		if(isCoordsValid(x,y-1) && cells.getDistanceCell(x, y-1)<smallestDistance) {
  			returnCoord = new Coordinate(x, y-1);
  			smallestDistance = cells.getDistanceCell(x, y-1);
  		}
  		if(isCoordsValid(x+1,y+1) && cells.getDistanceCell(x+1, y+1)<smallestDistance) {
  			returnCoord = new Coordinate(x+1, y+1);
  			smallestDistance = cells.getDistanceCell(x+1, y+1);
  		}
  		if(isCoordsValid(x+1,y-1) && cells.getDistanceCell(x+1, y-1)<smallestDistance) {
  			returnCoord = new Coordinate(x+1, y-1);
  			smallestDistance = cells.getDistanceCell(x+1, y-1);
  		}
  		if(isCoordsValid(x-1,y+1) && cells.getDistanceCell(x-1, y+1)<smallestDistance) {
  			returnCoord = new Coordinate(x-1, y+1);
  			smallestDistance = cells.getDistanceCell(x-1, y+1);
  		}
  		if(isCoordsValid(x-1,y-1) && cells.getDistanceCell(x-1, y-1)<smallestDistance) {
  			returnCoord = new Coordinate(x-1, y-1);
  			smallestDistance = cells.getDistanceCell(x-1, y-1);
  		}
  		return returnCoord;
    }
  	
  	private static Coordinate[] getPath(){
  		Coordinate c = getSmallestAdjacentCell(endPointX, endPointY);
  		int pointerX, pointerY;
  		Coordinate[] path=new Coordinate[cells.getDistanceCell(c.x, c.y)+100];
  		pointerX = c.x;
  		pointerY = c.y;
  		int counter = 0;
  		while(cells.getCell(pointerX, pointerY)!=START) {
  			path[counter] = getSmallestAdjacentCell(pointerX, pointerY);
  			cells.setCell(pointerX, pointerY, PROCESSED);
  			c = getSmallestAdjacentCell(pointerX, pointerY);
  			pointerX = c.x;
  			pointerY = c.y;
  			counter++;
  		}
  		return path;
  	}
  	
  	//return true if all the adjacent cells to the given cell are all processing/processed
    private static boolean isChecked(int x, int y) {
    	//System.out.println("isChecked");
  		if(isCoordsValid(x+1,y)) {
  			if(cells.getStatus(x+1,y)==STATUS_UNPROCESSED) {
  				return false;
  			}
  		}
  		if(isCoordsValid(x-1,y)) {
  			if(cells.getStatus(x-1,y)==STATUS_UNPROCESSED) {
  				return false;
  			}  		
  		}
  		if(isCoordsValid(x,y+1)) {
  			if(cells.getStatus(x,y+1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	  		
  		}
  		if(isCoordsValid(x,y-1)) {
  			if(cells.getStatus(x,y-1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	  		
  		}
  		if(isCoordsValid(x+1,y+1)) {
  			if(cells.getStatus(x+1,y+1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	  		
  		}
  		if(isCoordsValid(x+1,y-1)) {
  			if(cells.getStatus(x+1,y-1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	  		}
  		if(isCoordsValid(x-1,y+1)) {
  			if(cells.getStatus(x-1,y+1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	
  		}
  		if(isCoordsValid(x-1,y-1)) {
  			if(cells.getStatus(x-1,y-1)==STATUS_UNPROCESSED) {
  				return false;
  			}  	
  		}
    	//System.out.println("returnBool = " + returnBool);
  		return true;
    }
    
    //return true if coordinate is in the matrix
    private static boolean isCoordsValid(int i, int j) {
    	return i>=0&&i<x&&j>=0&&j<y;
    }
    
    //return true if the cell is yet to be processed
    private static boolean isCellValid(int i, int j) {
    	//System.out.println("i = " + i+ "j = " + j);
    	return cells.getStatus(i, j)==STATUS_SPECIAL||cells.getStatus(i, j)==STATUS_UNPROCESSED;
    }
    
    private static boolean isEnd(int x, int y) {
    	return x==endPointX&&y==endPointY;
    }

    private static void paintRectangle(Graphics g,Color color,int x, int y,int width,int height) throws InterruptedException {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    public CanvasPanelCells getCells() {
    	return this.cells;
    }
    
    public int getNumberofSquareX() {
    	return x;
    }
    
    public int getNumberofSquareY() {
    	return y;
    }
    
    public int getSideLength() {
    	return sideLength;
    }

    class Mouse extends MouseAdapter{
        public void mouseClicked (MouseEvent e) {
        	getCells().setCell(mouseClickLocationToCellIndex(e.getX(), e.getY()).getX(), mouseClickLocationToCellIndex(e.getX(), e.getY()).getY(), colorToInt(GuiFrame.getCurrentColor()));
        	if(colorToInt(GuiFrame.getCurrentColor())==3) {
        		startPointX=mouseClickLocationToCellIndex(e.getX(), e.getY()).getX();
        		startPointY=mouseClickLocationToCellIndex(e.getX(), e.getY()).getY();
        		setStartPoint(startPointX,startPointY);
        	}
        	if(colorToInt(GuiFrame.getCurrentColor())==4) {
        		endPointX=mouseClickLocationToCellIndex(e.getX(), e.getY()).getX();
        		endPointY=mouseClickLocationToCellIndex(e.getX(), e.getY()).getY(); 
        		setEndPoint(endPointX,endPointY);
        	}
        	if(colorToInt(GuiFrame.getCurrentColor())==2) {
        		setBlockage(mouseClickLocationToCellIndex(e.getX(), e.getY()).getX(),mouseClickLocationToCellIndex(e.getX(), e.getY()).getY());
        	}
        	repaint();
        }
    }
}