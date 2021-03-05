package testing;

import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Pipe extends Box{	// Pipes are the bread and butter of this game. They are to be dragged and connected from starter to end box. They have properties of : vertical, horizontal and curved decribed as 00, 01, 10 and 11. 

	public Pipe(String id, String type, String property) {
		super(id, type, property);	// superclass constructor 
		setDirection(property);	// direction 
		
		// I put the event generators for mouse events here in the constructor, to not write them for each and every box again and again.
		getImageView().setOnMouseMoved(boxOnMouseHoveredEventHandler); //when mouse hovers over this node, the cursor will become a hand
		getImageView().setOnMousePressed(boxOnMousePressedEventHandler);//when mouse is pressed on the node of this box, the original scene x and y values will be set. 
		getImageView().setOnMouseDragged(boxOnMouseDraggedEventHandler);//while mouse drags the node of this box, the node will be translated with the mouse
		getImageView().setOnMouseReleased(boxOnMouseReleasedEventHandler); //when mouse releases the node of this box, the box's place in the grid will be updated  
		
		
	}
	
	private void setDirection(String property) {	// the directions the pipes openings are looking are set in boolean values according to it's property
		
		switch (property) {	// according to property of pipe
			case "Vertical":	//vertical property pipe's up and down directions are set true and the rest false
				setDown(true);
				setUp(true);
				setLeft(false);
				setRight(false);
				break;
			case "Horizontal":	//horizontal property pipe's left and right directions are set true and the rest false
				setDown(false);
				setUp(false);
				setLeft(true);
				setRight(true);
				break;
			case "00": //00 property pipe's up and left directions are set true and the rest false
				setUp(true);
				setDown(false);
				setLeft(true);
				setRight(false);
				break;
			case "01":	//01 property pipe's up and right directions are set true and the rest false
				setUp(true);
				setDown(false);
				setLeft(false);
				setRight(true);
				break;
			case "10":	//10 property pipe's down and left directions are set true and the rest false
				setUp(false);
				setDown(true);
				setLeft(true);
				setRight(false);
				break;
			case "11":	//11 property pipe's down and right directions are set true and the rest false
				setUp(false);
				setDown(true);
				setRight(true);
				setLeft(false);
				break;
			default :	//by default all directions are set to false
				setDown(false);
				setUp(false);
				setLeft(false);
				setRight(false);
				break;
		}
	}	
	
	// Here is where the magic (I mean AI) happens.
	public void setConnection() {	// Looking at both sides of the pipes, connection is set
		switch (getProperty()) {	// according to pipe's property
			case "Vertical":
				if(Main.findBox(getI()+1,getJ()).isUp() && Main.findBox(getI()-1,getJ()).isDown()) {// if the box just above is looking down to this box or if the box just below is looking above is checked
					setConnected(true);	// connection is set
					if(Main.findBox(getI()+1,getJ()).isFromStart()) // If the box just above has a connection from start
						setFromStart(true);	// this pipe is also now connected from the start
					else if(Main.findBox(getI()-1,getJ()).isFromStart())	// If the box just below has a connection from start 
						setFromStart(true);	// then this pipes is you may guess is from start also now
					else 
						setFromStart(false);	// else then this pipe is not connected from start				
										
				}else {	// else then this pipe is not connected from start nor anything else		
					setFromStart(false);
					setConnected(false);
	
				}				
				break;		/*
				 		*		Read the comments for Vertical property and I believe with all my heart you can understand what'll happen with the rest of the properties 
				 	*
				 *
			*
		*/
			case "Horizontal":
				if(Main.findBox(getI(),getJ()+1).isLeft() && Main.findBox(getI(),getJ()-1).isRight()) {	// box just left is checked if it's looking right or if box just right is checked if it's looking left to this pipe
					setConnected(true);
					if(Main.findBox(getI(),getJ()+1).isFromStart()) 
						setFromStart(true);
					else if(Main.findBox(getI(),getJ()-1).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);						
				}else {	
					setFromStart(false);
					setConnected(false);
				}
				break;
			case "00": 
				if(Main.findBox(getI()-1,getJ()).isDown() && Main.findBox(getI(),getJ()-1).isRight()) {	// box just above is checked if it's looking below or if box just left is checked if it's looking right to this pipe
					setConnected(true);
					if(Main.findBox(getI()-1,getJ()).isFromStart()) 
						setFromStart(true);
					else if(Main.findBox(getI(),getJ()-1).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);						
				}else {	
					setFromStart(false);
					setConnected(false);
				}
				break;
			case "01":
				if(Main.findBox(getI()-1,getJ()).isDown() && Main.findBox(getI(),getJ()+1).isLeft()) {	// box just above is checked if it's looking below or if box just right is checked if it's looking left to this pipe
					setConnected(true);
					if(Main.findBox(getI()-1,getJ()).isFromStart()) 
						setFromStart(true);
					else if(Main.findBox(getI(),getJ()+1).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);						
						
				}else {	
					setFromStart(false);
					setConnected(false);
					
				}
				break;
			case "10":
				if(Main.findBox(getI()+1,getJ()).isUp()&& Main.findBox(getI(),getJ()-1).isRight()) {	// box just below is checked if it's looking up or if box just left is checked if it's looking right to this pipe
					setConnected(true);
					if(Main.findBox(getI()+1,getJ()).isFromStart()) 
						setFromStart(true);
					else if(Main.findBox(getI(),getJ()-1).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);												
				}else {	
					setFromStart(false);
					setConnected(false);
					
				}
				break;
			case "11":
				if(Main.findBox(getI()+1,getJ()).isUp() && Main.findBox(getI(),getJ()+1).isLeft()) {	// box just below is checked if it's looking up or if box just right is checked if it's looking left to this pipe
					setConnected(true);
					if(Main.findBox(getI()+1,getJ()).isFromStart()) 
						setFromStart(true);
					else if(Main.findBox(getI(),getJ()+1).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);	
				}else {	
					setFromStart(false);
					setConnected(false);
					
				}
				break;
			default :
				setConnected(false);
				break;
		}
		if(isConnected() && isFromStart() && !isAdded()) {	// When pipe is connected and has a connection from start and wasn't added before, it's added to pipes arrayList
			Main.pipes.add((Main.findBox(getI(),getJ())));
			setAdded(true);  
		}else if(!Main.levelFinished)	// else if level not yet finished added boolean value is set to false for preventing any complications.
			setAdded(false); 
		
	}
}
