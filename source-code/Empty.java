package testing;

// Fatih Said Duran 150119029
// Anılcan Erciyes 150119520
 
import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Empty extends Box{	// 		Empty class is for boxes that do not have pipes. They do not connect nor contribute to the solution. There are two kinds, 'Free' and 'None'.
								//	'Free' empty boxes can dragged on over to solve the level. Empty 'none' boxes just consume space and complicate the solution, they can be dragged like pipes but can't be droppped on.

	public Empty(String id, String type, String property) {
		super(id, type, property);	// superclass constructor 
		setDirection();	// The direction the empty box looks at is set (which is false)
			
		
		// I put the event generators for mouse events here in the constructor, to not write them for each and every box again and again.
		getImageView().setOnMouseMoved(boxOnMouseHoveredEventHandler); //when mouse hovers over this node, the cursor will become a hand
		getImageView().setOnMousePressed(boxOnMousePressedEventHandler);//when mouse is pressed on the node of this box, the original scene x and y values will be set. 
		getImageView().setOnMouseDragged(boxOnMouseDraggedEventHandler);//while mouse drags the node of this box, the node will be translated with the mouse
		getImageView().setOnMouseReleased(boxOnMouseReleasedEventHandler); //when mouse releases the node of this box, the box's place in the grid will be updated  
		
		
	}

	
	
	private void setDirection() {	// as box is empty (not looking at anywhere. All directions are set to false
		setDown(false);
		setUp(false);
		setLeft(false);
		setRight(false);
	}
		
}

