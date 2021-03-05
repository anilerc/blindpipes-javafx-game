package testing;

public class Starter extends Box{	// Starter box is the start pipe where the pipes start connecting from to the solution. It is static. The pipes must connect from start to be added to pipes arrayList.
									//The start pipe is added to pipes arraylist by default.
	public Starter(String id, String type, String property) {
		super(id, type, property);	// superclass constructor 
		setDirection(property);	// direction where starter is looking is set
		setFromStart(true);	// As starter is in fact the start itself, in our programs logic it is set from start as true. 
	}

	private void setDirection(String property) {	// according to property direction is set
		
		if(property.equals("Vertical")) {	// If property is vertical, down direction is set to true and the rest to false (as the only start pipes in the level text inputs look downward).
			setDown(true);
			setUp(false);
			setLeft(false);
			setRight(false);
		}else if (property.equals("Horizontal")) {	// If property would have been horizontal, left direction and perhaps other directions for other properties would be set to true.
			setDown(false);
			setUp(false);
			setLeft(true);
			setRight(false);
		}
		
	}
	
	public void setConnection() {	
		
		if(Main.pipes.isEmpty() && !isAdded()) {	// Starter pipe is added to pipes arraylist first by default unless already added and pipes arraylist not empty
			Main.pipes.add(0,Main.findBox(getI(),getJ()));	
			setAdded(true);  
		}else if(!Main.levelFinished)	//else, unless level finished start pipe is not added 
			setAdded(false); 
		
		if(getProperty().equals("Vertical")) {	// If property is vertical, the box just above is checked whether it looks downward and this box looks back upward.  
			if(Main.findBox(getI()+1,getJ()).isUp() && isDown()) 
				setConnected(true);	// this box is connected
			else 
				setConnected(false);	// or else this box is not connected
		}else if(getProperty().equals("Horizontal")) {	// If property would have been horizontal 
			if(Main.findBox(getI(),getJ()-1).isRight() && isLeft()) 	// the box just left is checked whether it looks right and this box looks left towards it.  
				setConnected(true);	// this box is connected
			else 
				setConnected(false);	// or else this box is not connected				
		}
			
	}
}
