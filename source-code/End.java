package testing;

//Fatih Said Duran 150119029
//Anılcan Erciyes 150119520

public class End extends Box{ 	// End box is where the pipes with a connection from the start should connect finally. When this is done level will be finished. It is static.

	public End(String id, String type, String property) {
		super(id, type, property);	// superclass constructor 
		setDirection(property);	// direction where end is looking is set
	}

	private void setDirection(String property) {	// according to property direction is set
		
		if(property.equals("Vertical")) {	// If property is vertical, down direction is set to true and the rest is set to false (as the only vertical pipes in the level text inputs look downward).
			setDown(true);
			setUp(false);
			setLeft(false);
			setRight(false);
		}else if (property.equals("Horizontal")) {// If property is horizontal, left direction is set to true and the rest is set to false (as the only horizontal pipes in the level text inputs look leftward).
			setDown(false);
			setUp(false);
			setLeft(true);
			setRight(false);
		}
		
	}
	
	public void setConnection() {	// Here the final connection happens. If there's also a connection from start, then the level is finished
		if(getProperty().equals("Horizontal")) {	// If property is horizontal
			if(Main.findBox(getI(),getJ()-1).isRight())	{	//	the box just left is checked whether it looks rightward. 
				setConnected(true);
				if(Main.findBox(getI(),getJ()-1).isFromStart()) {	// If the box just above has also a connection from start
					setFromStart(true);	// connection from start is established
					Main.levelFinished = true;	// level is finished!
				}else 
					setFromStart(false);	// no connection from start
				
			}else
				setConnected(false);	// no connection
		}else if(getProperty().equals("Vertical")) {	// If property is vertical  
			if(Main.findBox(getI()+1,getJ()).isUp()) {	// the box just below is checked whether it looks upward. 
				setConnected(true);		// this box is connected
				if(Main.findBox(getI()+1,getJ()).isFromStart()){	// If the box just above has also a connection from start
					setFromStart(true);	// connection from start is established
					Main.levelFinished = true;	// level is finished!
				}else	
					setFromStart(false);
			}else	// or else this box is not connected
				setConnected(false);	// no connection
		}
		if(isFromStart() && isConnected() && !isAdded()) {	// When pipe is connected and has a connection from start and wasn't added before, it's added to pipes arrayList
			Main.pipes.add((Main.findBox(getI(),getJ())));
			setAdded(true);  
		}else if(!Main.levelFinished)	// else if level not yet finished added boolean value is set to false for preventing any complications.
			setAdded(false); 	
	}
}
