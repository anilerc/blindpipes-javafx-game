package testing;


public class PipeStatic extends Box{	// PipeStatic is the same with Pipe except one big difference, which is they can not be dragged or swapped.

	public PipeStatic(String id, String type, String property) {
		super(id, type, property);	// superclass constructor 
		setDirection(property);
	}
	/*
	 	* 
	 		* 
	 			* 
	 				* 				
	 					* 
	 						* ********** Read class Pipe's comments. The explanations go for here also as the code below is literally the same. **********
	 					* 
	 				* 
	 			* 
	 		* 
	 	* 
	 */
	private void setDirection(String property) {	
		
		switch (property) {	
			case "Vertical":	
				setDown(true);
				setUp(true);
				break;
			case "Horizontal":	
				setLeft(true);
				setRight(true);
				break;
			case "00": 
				setUp(true);
				setLeft(true);
				break;
			case "01":	
				setUp(true);
				setRight(true);
				break;
			case "10":	
				setDown(true);
				setLeft(true);
				break;
			case "11":	
				setDown(true);
				setRight(true);
				break;
			default :	
				setDown(false);
				setUp(false);
				setLeft(false);
				setRight(false);
				break;
		}
	}	
	
	public void setConnection() {	
		switch (getProperty()) {	
			case "Vertical":
				if(Main.findBox(getI()+1,getJ()).isUp() && Main.findBox(getI()-1,getJ()).isDown()) {
					setConnected(true);
					if(Main.findBox(getI()+1,getJ()).isFromStart()) 
						setFromStart(true);						
					else if(Main.findBox(getI()-1,getJ()).isFromStart())
						setFromStart(true);
					else 
						setFromStart(false);					
										
				}else {	
					setFromStart(false);
					setConnected(false);
	
				}				
				break;
			case "Horizontal":
				if(Main.findBox(getI(),getJ()+1).isLeft() && Main.findBox(getI(),getJ()-1).isRight()) {
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
				if(Main.findBox(getI()-1,getJ()).isDown() && Main.findBox(getI(),getJ()-1).isRight()) {
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
				if(Main.findBox(getI()-1,getJ()).isDown() && Main.findBox(getI(),getJ()+1).isLeft()) {
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
				if(Main.findBox(getI()+1,getJ()).isUp()&& Main.findBox(getI(),getJ()-1).isRight()) {
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
				if(Main.findBox(getI()+1,getJ()).isUp() && Main.findBox(getI(),getJ()+1).isLeft()) {
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
		if(isConnected() && isFromStart() && !isAdded()) {
			Main.pipes.add((Main.findBox(getI(),getJ())));
			setAdded(true);  
		}else if(!Main.levelFinished)
			setAdded(false); 
		
	}
}
