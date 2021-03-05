package testing;

// Fatih Said Duran 150119029
// Anılcan Erciyes 150119520

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;


public class Box implements Comparable {	// Box class is the superclass of all the tiles of the game. All methods and variables concerning all types of boxes is in this class 

	double orgSceneX, orgSceneY;			//the original scene x and y coordinates mouse was clicked on node. Used for computing offset
    double orgTranslateX, orgTranslateY;	//the original translate x and y values when mouse was clicked. used for computing translation
	
    private String id;	// id is the number of the box from 1 to 16
    private String type;	// type is what the box is (a pipe or just empty? etc.)
    private String property;	// property is what kind of type is the box (curved pipe, free empty box? etc.)
    
    private int i;	// the row of the box in grid
    private int j;	// the column of the box in grid

	private Image image;	// the image of the box
    private ImageView imageView;	// the node the image will be shown in

    private String to;	// to is the string which indicates in which direction a dropped box is released
    private boolean onX=false; // boolean value which indicates horizontal translation off of original x coordinates is taken place.
    private boolean onY=false; // boolean value which indicates vertical translation off of original y coordinates is taken place.
    
    private boolean up;	// direction boolean values (up,down,right and left) are what define if the box has a pipe opening for connection that direction    
    private boolean down;
    private boolean right;
    private boolean left;

    private boolean fromStart;	// boolean value of whether the box (pipe) is has connection coming from the starter pipe
    private boolean added=false;	// boolean value of whether the box (pipe) was already added to pipes arrayList 
    
    private boolean connected;	// boolean value for understanding if the box (pipe) is attached to other pipes from all openings
    
    public Box(String id, String type, String property) {	// Box objects are constructed with an id, the type of box it is, and the property of it.
        this.id = id;
        this.type = type;
        this.property = property;
        this.image = new Image((property.equals("none") ? "" : property) + type +".png");	// according to the input taken from level text, an image png is used according to it's type and property   
		this.imageView = new ImageView(image);	// image is put to an imageView node for this object
		imageView.setId(this.id);	// id of box is given to imageView node, to be later shared if dragged
		
		this.i = Main.findIJ(Integer.parseInt(getId()))[0];	//	i and j (row and column values in grid) are set according to id
		this.j = Main.findIJ(Integer.parseInt(getId()))[1];
    }   
   
    @Override
	public int compareTo(Object o) {	// boxes are compared according to their id value
    	if(o instanceof Box) {
	    	if(Integer.parseInt(this.id)>((Box)o).getIntId())	
				return 1;
			else if(Integer.parseInt(this.id)<((Box)o).getIntId())
				return -1;
			else
				return 0;		
    	}
    	else {
    		System.out.println("\n^^^^ERRROR^^^^");
    		return -2;
    	}
    }

	public void setConnection() {		// default setConnection method for boxes
		if(getProperty().equals("Empty"))	// If it's empty box as it's not a pipe it will not be connected
				this.connected=false;
	}	
	
	EventHandler<MouseEvent> boxOnMouseHoveredEventHandler = //When mouse passes over a box's node a mouse event is handled which the mouse cursor is turned to hand or to default
	        new EventHandler<MouseEvent>() {
		public void handle(MouseEvent t) {
			if(getProperty().equals("Free") || Main.levelFinished) { // If the level is finished and the box the mouse is hovering over is free, then the mouse cursor is set to default and the box is made transparent
				getImageView().setMouseTransparent(true);
				getImageView().setCursor(Cursor.DEFAULT);
			}
			else if (!Main.levelFinished) {	//If the box is any other box and the level isn't finished, cursor is set to hand
				getImageView().setMouseTransparent(false);            
				getImageView().setCursor(Cursor.HAND);
			}
		}
	};
	
	EventHandler<MouseEvent> boxOnMousePressedEventHandler = //when mouse is pressed on the node of a box, a mouse event is handled which the scene x and y coordinates of the mouse when clicked is taken and put to orgSceneX and orgSceneY, and the initial translation is put to orgTranslateX and orgTranslateY to compute translation of the node translation with the mouse in the scene later when mouse is dragged.                    
	        new EventHandler<MouseEvent>() {
		@Override
        public void handle(MouseEvent t) {
			if(getProperty().equals("Free") || Main.levelFinished)	// If level is finished or the box is free, imageView node is set transparent to the mouse
        		getImageView().setMouseTransparent(true);
			else if (!Main.levelFinished){	//If the level hasn't finished yet
				getImageView().setMouseTransparent(false);    // box's node's transparency is set to false        
				
				orgSceneX = t.getSceneX();	// the scene x and y values (the x and y values in the stage when mouse pressed event is triggered) is put to orgSceneX and orgSceneY
				orgSceneY = t.getSceneY();
            
				orgTranslateX = ((ImageView)(t.getSource())).getTranslateX();	//orgTranslateX and orgTranslateY are set to the translation of the mouse when event is triggered 
				orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();
			}
		}
	};
	
	EventHandler<MouseEvent> boxOnMouseDraggedEventHandler = // While mouse is dragged over box's node, a mouse event is handled which will translate imageView node according to the scene x and y values of the mouse dragging
	        new EventHandler<MouseEvent>() {
	 
	        @Override
	        public void handle(MouseEvent t) {
	        	if(getProperty().equals("Free") || Main.levelFinished)	// If level is finished or box is free, imageView node is set transparent to the mouse
	        		getImageView().setMouseTransparent(true);
				else if (!Main.levelFinished){	// if level isn't finished yet
					getImageView().setMouseTransparent(false);	// box's node's transparency is set to false   				
	        		getImageView().toFront();	//the box's imageView node which will be in motion will brought to front to pass over the free boxes without being covered.        		        			        		 

	        		double offsetX = t.getSceneX() - orgSceneX;	// The offsetX and offsetY values are computed with the current scene x and y values of the mouse's distance offset with the original scene x and y values 
	        		double offsetY = t.getSceneY() - orgSceneY;

	        		double newTranslateX = orgTranslateX + offsetX; // the offset values are added to the translation the box's imageView will make
	        		double newTranslateY = orgTranslateY + offsetY;		
	        		
	        		outX(t.getSceneX());	//according to whether the scene x and y values are out of the bounds of the box, the direction (is it on x or on y direction) will be determined and the result will be put to boolean values : onX, onY. 
	        		outY(t.getSceneY());
	        		
	        		if(freeXDirection(t.getSceneX(),t.getSceneY())) { 	// whether x or y direction is free (logic computations are explained in the methods comments) imageView node of the box will translate with mouse cursor.     	      				
	        			((ImageView)(t.getSource())).setTranslateX(newTranslateX);
	        			((ImageView)(t.getSource())).setTranslateY((getI())*108-((ImageView)(t.getSource())).getLayoutY());	 //The box can only slide vertically or horizontally, not diagonally; to assure this while translating horizontally y value is fixed to the box's default y value and vice versa.       			
	        		}else if(freeYDirection(t.getSceneX(),t.getSceneY())) { 
	        			((ImageView)(t.getSource())).setTranslateY(newTranslateY);
	        				((ImageView)(t.getSource())).setTranslateX((getJ())*108-((ImageView)(t.getSource())).getLayoutX());
	        		}	
	        	}
	        }
	    };
	
	EventHandler<MouseEvent> boxOnMouseReleasedEventHandler = // When mouse is released over the node of a box, a mouse event is handled which the box being dragged and the box under it will be swapped and the grid will be updated according so. 
		        new EventHandler<MouseEvent>() {
			public void handle(MouseEvent t) {					
			if(!Main.levelFinished) {	//If level isn't finished  yet

				swapBoxes(indexOfBoxBelow(t.getSceneX(),t.getSceneY()));	// The box dragged and the box below where the mouse cursor was released are swapped (their id and layout x and y values are swapped) 
	            
	            Main.numberOfMoves++; // number of movements is incremented.
	            ((ImageView)(t.getSource())).toFront();	//the box released is sent to front not to get covered over.
	            Main.updateGrid();	// gridpane and we can say everything about the level is updated

	            // Below code is for fixing the imageView node of the box in its new place in the grid.
	            if(to.equals("right")) {	// According 'to' what direction the box was swapped : 
	            	((ImageView)(t.getSource())).setTranslateX((getJ()+1)*108-((ImageView)(t.getSource())).getLayoutX()); //the imageView node's layout values are translated to the exact fitting layout values of the gridspace for decency. 
	            	((ImageView)(t.getSource())).setTranslateY(getI()*108-((ImageView)(t.getSource())).getLayoutY());
	            	((ImageView)(t.getSource())).setTranslateX(-0.1); // the node slips out for some reason, translating a tenth of a pixel back does the job so that the box doesn't mess up the grid
	            }else if(to.equals("up")) {
	            	((ImageView)(t.getSource())).setTranslateX(getJ()*108-((ImageView)(t.getSource())).getLayoutX());
	            	((ImageView)(t.getSource())).setTranslateY((getI()-1)*108-((ImageView)(t.getSource())).getLayoutY());
	            	((ImageView)(t.getSource())).setTranslateY(0.1);
	            }if(to.equals("left")) {
	            	((ImageView)(t.getSource())).setTranslateX((getJ()-1)*108-((ImageView)(t.getSource())).getLayoutX());
	            	((ImageView)(t.getSource())).setTranslateY(getI()*108-((ImageView)(t.getSource())).getLayoutY());
	            	((ImageView)(t.getSource())).setTranslateX(0.1);
	            }else if(to.equals("down")) {
	            	((ImageView)(t.getSource())).setTranslateX(getJ()*108-((ImageView)(t.getSource())).getLayoutX());
	            	((ImageView)(t.getSource())).setTranslateY((getI()+1)*108-((ImageView)(t.getSource())).getLayoutY());
	            	((ImageView)(t.getSource())).setTranslateY(-0.1);
	            }else if(to.equals("")){	// If a swap happened in no special direction the imageView node is fixed to its previous gridspace.
	            	((ImageView)(t.getSource())).setTranslateX(getJ()*108-((ImageView)(t.getSource())).getLayoutX());
	            	((ImageView)(t.getSource())).setTranslateY(getI()*108-((ImageView)(t.getSource())).getLayoutY());
	            }
			}
		}
	};
	    
	public int indexOfBoxBelow(double dx, double dy) {	// the index of the box which the mouse has released over is returned. The direction which the box is to the dragged box's original grid space is also determined. 
		
		if(dx<getJ()*108 && Main.findBox(getI(),getJ()-1).getProperty().equals("Free")) { // the mouse was released to free box on the left
			System.out.println("left : " + Main.findBox(getI(),getJ()-1).getId());
			to="left";
			return Main.findIndex(getI(),getJ()-1); // index is found and returned according to the i and j values
		}else if(dx>getJ()*108+108 && Main.findBox(getI(),getJ()+1).getProperty().equals("Free")) {//.. on the right
			System.out.println("right : " + Main.findBox(getI(),getJ()+1).getId());
			to="right";
			return Main.findIndex(getI(),getJ()+1);
		}else if(dy<getI()*108 && Main.findBox(getI()-1,getJ()).getProperty().equals("Free")) {	//.. upward
			System.out.println("up : " + Main.findBox(getI()-1,getJ()).getId());
			to="up";
			return Main.findIndex(getI()-1,getJ());
		}else if(dy>getI()*108+108 && Main.findBox(getI()+1,getJ()).getProperty().equals("Free")) {//.. downward			
			System.out.println("down : " + Main.findBox(getI()+1,getJ()).getId());
			to="down";
			return Main.findIndex(getI()+1,getJ());
		}else{
			System.out.println("in place : " + getId()); // perhaps the box was released to where it previously was
			to="";
			return getIntId()-1;
		}
	}
	    
	// for x    
	public boolean freeXDirection(double dx, double dy) { // the scene x and y values of the cursor is taken and then whether the box can translate horizontally with the mouse is determined. 
		double x = dx-orgSceneX%108; // The box's x and y values if they were translated with the mouse cursor is calculated. 
		double y = dy-orgSceneY%108; //orgSceneX%108 and orgSceneY%108 gives us where the mouse was first pressed on within the box. Subtracting this from where the current mouse scene x and y are gives us the x and y values the would be. I say would be because this computations and determinations for the box coordinates all are happening before any translation taking place.
		if(((((x>=getJ()*108-108 && x<=getJ()*108) && Main.findBox(getI(),getJ()-1).getProperty().equals("Free")) || ((x<=(getJ()*108+108) && x>=getJ()*108) && Main.findBox(getI(),getJ()+1).getProperty().equals("Free"))) || ((x+108 <=((getJ()*108)+108) && x>=getJ()*108))) && (x+108<432 && x>0)) { // in English : if box's (would be) x is in bounds of left neighboring box space AND which that box's property is free OR if box's x is in bounds of right neighboring box space AND which that box's property is free OR simply if the box's x is inbounds of it's own box space AND also to top it all off if the box's x is in the boundaries of the whole grid ... (read next if statement below)         
			if((y<=((getI()*108)+108) && y+108>=getI()*108) && (!onY || (!inOwnSpace(x,y) && onX)) && dy-dy%108==getI()*108) { //... AND if box's Y (yes y) is in bounds of the box's original lower and upper y values (or simply if the box is aligned horizontally with it's original space) AND the box is not onY direction OR isn't in its own space AND is onX direction, AND the box's row coordinate is in the row which it was originally in; the x direction is free to translate through. Amen                    
				return true;
			}else {
				return false;
			}	
		}
		else
			return false;
	}
	// for y   
	public boolean freeYDirection(double dx, double dy) {// the scene x and y values of the cursor is taken and then whether the box can translate vertically with the mouse is determined. 
		double x = dx-orgSceneX%108; //really explained it in freeXDirection method,the same things apply but just think of everything vertically.
		double y = dy-orgSceneY%108;
		if(((((y>=getI()*108-108 && y<=getI()*108) && Main.findBox(getI()-1,getJ()).getProperty().equals("Free")) || ((y<=(getI()*108+108) && y>=getI()*108) && Main.findBox(getI()+1,getJ()).getProperty().equals("Free"))) || ((y+108<=((getI()*108)+108) && y>=getI()*108)))  && (y+108<432 && y>0)) {
			if((x<=((getJ()*108)+108) && x+108>=getJ()*108) && (!onX || (!inOwnSpace(x,y) && onY)) && dx-dx%108==getJ()*108) {
				return true;
			}else {	
				return false;
			}
		}else
			return false;
	}
	
	public boolean inOwnSpace(double x, double y) {		// x and y values are determined whether they're within the bounds of the box's original place
		if((x<=((getJ()*108)+108) && x+108>=getJ()*108) && (y<=((getI()*108)+108) && y+108>=getI()*108)) {
			return true;
		}else
			return false;	
	}
	
	public void outX(double x) { //(x input value is mouse scene x value for better understanding) x value is determined whether it is out of the x bounds of the box's original space
		x = x-x%108; // x is rounded to the current x coordinate in which the box is in the grid
		if(x<getJ()*108 || x>getJ()*108+108)
			onX =  true;
		else
			onX = false;
			
	}
	
	public void outY(double y) { //(y input value is mouse scene y value for better understanding) y value is determined whether it is out of the y bounds of the box's original space
		y = y-y%108; // y is rounded to the current y coordinate the box is in the grid
		if(y<getI()*108 || y>getI()*108+108) 
			onY = true;
		else
			onY = false;
	}

	public void swapBoxes(int i) {	// the box with taken index is swapped with this box
		
		System.out.println("\n"+getId() +" is swapped with " + (i+1)+"\n");
		if(isNeighbor(i)) {		// if the box to be swapped is neighboring with this box :			
			double xt = Main.boxes.get(i).getImageView().getLayoutX();	// the layout x and y of taken box is put temporarily to xt and yt
			double yt = Main.boxes.get(i).getImageView().getLayoutY();	
			Main.boxes.get(i).getImageView().relocate(getImageView().getLayoutX(),getImageView().getLayoutY());	// the layout x and y of taken box is set to this box's layout x and y
			getImageView().relocate(xt,yt);// the layout x and y of this box is set to xt and yt						
			
			String t = ""+(i+1);	// id is put to temporarily to string t
			Main.boxes.get(i).setId(getId());	// id of taken box is set to this box's id
			setId(t); 	//id of this box is set to id taken

		}		
	}    
	    
	public boolean isNeighbor(int i) {	// Checks if the given i indexed box from the boxes arraylist is horizontally or vertically a neighbor of this box.
		int sI = Main.boxes.get(i).getI();	// sI for the I value (the row) of the i indexed box
		int sJ = Main.boxes.get(i).getJ();	// sJ for the J value (the column) of the j indexed box
		int tI = getI();	// tI for I value of this box
		int tJ = getJ();	// tJ for J value of this box
		if(((sI==tI)&&((sJ==tJ+1)||(sJ==tJ-1)))||((sJ==tJ)&&((sI==tI+1)||(sI==tI-1))))	// if just I value is one up or down, or if  just J value is one right or left from this box
			return true;
		else
			return false;
	}
	
	
	public int getIntId() {		// returns integer value of string variable Id
		return Integer.parseInt(getId());
	}
	
	public boolean isUp() {		//getters and setters...
		return up;
	}

    public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public boolean isDown() {
		return down;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public Node getNode() {
        return imageView;
    }
    
    public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }

    public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public boolean isFromStart() {
		return fromStart;
	}

	public void setFromStart(boolean fromStart) {
		this.fromStart = fromStart;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}
	
	public void setIJ(int i,int j) {		
		this.i = i;
		this.j = j;
		
	}

	public void setId(String id) {
		this.id = id;
	}


}
