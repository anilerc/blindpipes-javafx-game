package testing;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.text.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {	/* Main class is where the display is shown and updated... */

	public static Stage primaryStage;	// primarystage is the stage where content will be shown
	public static GridPane gridPane;	// gridpane is where the boxes imageView nodes are put
	
	public static int  N = 4;	//grid row size	
	public static final double  S = 108; //box pixel size	
	
	public static ArrayList<Box> boxes = new ArrayList<>();	//boxes arraylist is for where all the boxes of each level is held
	public static ArrayList<Box> pipes = new ArrayList<>();	//pipes arraylist is the arraylist where the pipes which have connected from starter pipe are held. If pipes reach the end the level is finished and a ball will roll through the pipes 
		
	public static int level;	// level is the integer which indicates what level the player is in
	public static boolean levelFinished = false;	// levelFinihed is boolean value which indicates whether the level is finished or not 

	public static int permission = 1;	// permission is for what levels the player can play. Permission increases by completing levels
    public static boolean animationresetter;	//animationresetter is the boolean value of which indicates whether the ball animation should play

    public static int numberOfMoves;	//numberOfMoves is the number of moves taken in one level

    public static File file;	// file is for the level text file 
    public static boolean nextLevelButtonUsed = false;	// nextLevelButtonUsed boolean value is for whether the next level button should be activated or not 

    
    private Parent content() throws FileNotFoundException {  // Content is where the box objects are created according to read input level text file and added to boxes arraylist. With the boxes grid is updated.            
        
        BackgroundImage backgroundImage= new BackgroundImage(new Image("background.png"),	// the background is image is created
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                  BackgroundSize.DEFAULT);
        
        //if (!(nextLevelButtonUsed)) {
        	boxes.clear();		//boxes arraylist is cleared to be refilled according to the new input from level text file
        	file = new File("level"+level+".txt");	//	new file of level text file is taken according to what level in 
        	levelFinished=false;	//level will not be finished as a new level has been started 
        //}
        ArrayList<String> infoForBoxes = InputFileReadingService.inputFileReader(file);    // We read the file and take the input as a arraylist of strings                   
        
        String[] split;	
       
        for(String infoForBox : infoForBoxes) {	// The info for boxes is split from commas and according to the strings new box objects will be created.
	        split = infoForBox.split(",");
	        switch (split[1]) {	// According the second input in line the subclass of the box object is defined.
	        	case "Starter":
	        		boxes.add(new Starter(split[0],split[1],split[2]));	// we construct boxes with the first string for the id, the second string for the type and the third string for the property of the box object 
	        		break;
	        	case "Empty":
	        		boxes.add(new Empty(split[0],split[1],split[2]));
	        		break;
	        	case "Pipe":
	        		boxes.add(new Pipe(split[0],split[1],split[2]));
	        		break;
	        	case "End":
	        		boxes.add(new End(split[0],split[1],split[2]));
	        		break;
	        	case "PipeStatic":
	        		boxes.add(new PipeStatic(split[0],split[1],split[2]));
	        		break;
	        	default :
	        		boxes.add(new Box(split[0],split[1],split[2]));
	        		System.out.println("Couldn't find type");
	        		break;
	        }        
        }   
        
        gridPane = new GridPane();	//Gridpane is created for the boxes to be shown in
        gridPane.setPrefSize(N*S,N*S); //432x432 size in this case
        gridPane.setBackground(new Background(backgroundImage));
        
        updateGrid();	// grid is updated according to the new input
        
        return gridPane;	// gridpane is returned as content
    }

    public void start(Stage primaryStage) throws Exception{	// Start sets the stage according to the buttons pressed


    	VBox vBox = new VBox();			// Buttons will be presented in a Vbox
        vBox.setAlignment(Pos.CENTER);
        
        Button levelOne = new Button("Level 1");	// all the level buttons are created level 2 and onward are disabled until permission is checked
        levelOne.setPrefHeight(60);
        levelOne.setPrefWidth(300);

        Button levelTwo = new Button("Level 2");
        levelTwo.setPrefHeight(60);
        levelTwo.setPrefWidth(300);
        levelTwo.setDisable(true);
        
        Button levelThree = new Button("Level 3");
        levelThree.setPrefHeight(60);
        levelThree.setPrefWidth(300);
        levelThree.setDisable(true);

        Button levelFour = new Button("Level 4");
        levelFour.setPrefHeight(60);
        levelFour.setPrefWidth(300);
        levelFour.setDisable(true);
        Button levelFive = new Button("Level 5");
        levelFive.setPrefHeight(60);
        levelFive.setPrefWidth(300);
        levelFive.setDisable(true);

        vBox.getChildren().addAll(levelOne, levelTwo, levelThree, levelFour, levelFive);
        vBox.setSpacing(15);

        vBox.setPrefSize(300, 300);

        Scene mainMenu = new Scene(vBox);	// vBox is put to mainmenu scene

        if (!(nextLevelButtonUsed)) {

            primaryStage.setScene(mainMenu);	// primary stage is set to main menu by default

            Main.primaryStage = primaryStage;
            levelOne.setOnAction(e -> {		// level button on pressed event handlers below:
                numberOfMoves = 0;	// # of moves is reset
                level = 1;	// level is set to the level button pressed
                try {
                    primaryStage.setScene(new Scene(content()));	// New scene of the level is set. If file is not found an exception is catched
                } catch (FileNotFoundException ex) {	
                    ex.printStackTrace();
                }
            });

            if (permission >= 2) {	// according to permission, level button is set to be available
                levelTwo.setDisable(false);
            }
            Main.primaryStage = primaryStage;	// everything is merely the same as level 1 button
            levelTwo.setOnAction(e -> {
                numberOfMoves = 0;
                level = 2;
                try {
                    primaryStage.setScene(new Scene(content()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            if (permission >= 3) {
                levelThree.setDisable(false);
            }
            Main.primaryStage = primaryStage;
            levelThree.setOnAction(e -> {
                numberOfMoves = 0;
                level = 3;
                try {
                    primaryStage.setScene(new Scene(content()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            if (permission >= 4) {
                levelFour.setDisable(false);
            }
            Main.primaryStage = primaryStage;
            levelFour.setOnAction(e -> {
                numberOfMoves = 0;
                level = 4;
                try {
                    primaryStage.setScene(new Scene(content()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            if (permission >= 5) {
                levelFive.setDisable(false);
            }
            Main.primaryStage = primaryStage;
            levelFive.setOnAction(e -> {
                numberOfMoves = 0;
                level = 5;
                try {
                    primaryStage.setScene(new Scene(content()));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
        } else {	// If 'next button' was pressed level is increased by one and # of moves is reset and the new level scene is set.
            numberOfMoves = 0;
            level++;
            primaryStage.setScene(new Scene(content()));
        }


        primaryStage.setResizable(false);	// We don't want our stage to be resizable because everything would be a mess as our level displays aren't resizable.
        primaryStage.setTitle("JavaFX Term Project | Ball Roll Game");	//Title is printed in the corner
        primaryStage.show();	//Stage is shown

    }

    public static void main(String[] args) {   	// main just launches arguments
        launch(args);
    }   
 
    public static void finishLevel() { 	// in finishlevel if level is finished according to the level in permission is increased and animation is reset so that the ball can animate.
 
    	if(levelFinished && level<=permission) {   			
                if(level==permission)permission++;               
                animationresetter = true;
    	}else
    		animationresetter = false;
    }
    

    public static void sideBar() {	// Side bar is displayed below while pipe game is being played. User will have buttons to go back to the menu, go to next level if permitted, can see what level they are and can see how many numbers he/she has moved.
  
    	Button back = new Button("Main Menu");	// Main menu button at bottom left corner
        back.setPrefHeight(32);
        back.setPrefWidth(220);
        gridPane.add(back, 0, 4);

        back.setOnAction(e -> {			
            nextLevelButtonUsed = false;
            animationresetter = false;

            Main main = new Main();
            try {				// when the button's pressed we go back to the main menu
            	main.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        Button nextLevel = new Button("Next Level");	// nect level button
        nextLevel.setDisable(true);


        if	(permission > level) 		// If user has permission, nextlevel button will not be disabled 
        		nextLevel.setDisable(false);
        
        
        back.setPrefHeight(32);
        back.setPrefWidth(220);

        if (level != 5) {		// as long as they aren't at the last level next level button is displayed
            gridPane.add(nextLevel, 1, 4);
            nextLevel.setOnAction(e -> {
                nextLevelButtonUsed = true;
                boxes.clear();
                file = new File("level"+(level+1)+".txt");	// the next level file's is taken when pressed
                levelFinished=false;
                Main main = new Main();
                try {
                    main.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });
        } 
        if(level==5 && levelFinished) {		// When the final level is finished, user is informed.
        Text gameFinished = new Text(" Game Completed!");
        gameFinished.setFill(Color.LIMEGREEN);
        gameFinished.setStroke(Color.DARKGREEN);
        Font font = new Font(STYLESHEET_CASPIAN, 13);
        gameFinished.setFont(font);
        gridPane.add(gameFinished, 1, 4);        
        }
        Text leveltxt = new Text(" Level : "+level);	// the level on display is written
        gridPane.add(leveltxt, 2, 4);
        Text numOmovestxt = new Text(" # of moves : " + numberOfMoves);	// number of moves taken is shown
        gridPane.add(numOmovestxt, 3, 4);


    }
    
    public static void ballAnimation() {	//When the level is successfully finished a ball wil roll from starter pipe to end pipe through the connecte pipes.
    	if(levelFinished) {
	    	Circle ball = new Circle();	// ball created
	        ball.setRadius(18);
	        ball.setFill(Color.GOLD);
	        ball.setStroke(Color.DARKSLATEGREY);
	        ball.setManaged(false);
	        ball.setCenterY(54);
	        ball.setCenterX(54);
	        for(int i = 0; i<boxes.size();i++) {
				if(boxes.get(i).getType().equals("Starter")) {
					gridPane.add(ball, boxes.get(i).getJ(), boxes.get(i).getI());	//ball placed to starter box's place
					break;
				}
	        }
	        ball.toFront();
	        
	        Polyline polyline = new Polyline();	   // polyline is used for connecting the  connected pipes places     
	        for(Box pipe : pipes) {	// the solution's connected pipes center dots are added to the polyline
	        	polyline.getPoints().add((pipe.getImageView().getLayoutX()+54));
	        	polyline.getPoints().add((pipe.getImageView().getLayoutY()+54));
	        }
	        
	        PathTransition pt = new PathTransition();	// path transition is created for where the ball will roll through
	        if(animationresetter) {
	            pt.setNode(ball);
	            pt.setDuration(Duration.seconds(1));	// ball will roll in 1s through the polyline path drawn
	            pt.setPath(polyline);
	            
	            pt.play();
	            
	        }
            pipes.clear();	// pipes arraylist are cleared as the current pipes set makes no use as the level is finished 
    	}
    }    
   
	public static void updateGrid() {	// update grid is where the game is updated. Grid is updated after swapping, connections are updated, solution pipes rraylist is updated, if level is finished ball animation is done and the sidebar is printed below.
    	gridPane.getChildren().clear();	// the gird is cleared to be refilled according to the new swapping
    	if(!levelFinished)	// If the level isn't finished yet pipes are cleared to be refilled according to the new pipe positions
    		pipes.clear();
    	Collections.sort(boxes);	// boxes are sorted according to their id values (boxes are resorted as the swap action as displaced the boxes in the arraylist
    	
    	int l=0;
    	for(int r =0;r<4;r++) {
    		for(int c =0;c<4;c++) {
	    		boxes.get(l).setIJ(r,c);	// i j (row column) values of the boxes in the grid are updated
	    		gridPane.add(boxes.get(l).getImageView(),c,r);	// boxes are added to the gridpane
	    		boxes.get(l).getImageView().toBack();
	    		if(boxes.get(l++).getProperty().equals("Free"))
	    			boxes.get(l-1).getImageView().setVisible(false);
    		}	
    	}
    	
    	
      	for(l=0;l<16;l++)	// boxes (pipes) connections are set. pipes arraylist is filled here.     
      		boxes.get(l).setConnection();      
      	for(l=0;l<16;l++)	//Connections are set twice because in two for loops because the upper pipes do not successfully establish a connection from start with the pipes below in the grid.    
      		boxes.get(l).setConnection(); //This is because how the for loop doesn't connect all the pipe at once so we set Connections in a for loop twice to successfully establish our logical connections. 
 
      	
        finishLevel();// After connections have been established within the pipes, whether the level has been successful or not the finish level method will update permission and animation resetter.

        
        l=0;							// 		For debugging purposes really.         								
        for(int r =0;r<4;r++) {			//	All the boxes in the grid are printed whether they have a connection from start or not
        	for(int c =0;c<4;c++) {
        		System.out.print(boxes.get(l++).isFromStart()+" ");
        	}
        	System.out.println();	        		
        }
        System.out.println();
        
        
        if(levelFinished) {System.out.println("\n***** GG *****\n");}	//  we're informed of the level being finished 
        
        ballAnimation();	// ball animation is played 

        sideBar();    // side bar is displayed.    
               
    }
	
	// Finder methods below :
	
    public static Box findBox(int i, int j) { // With the given row and column (i and j) values, box index is found and then the box itself is returned from arraylist. 
    	if((0<=i && i<N)&&(0<=j && j<N))
    		return boxes.get(findIndex(i,j));
    	else 	// If no box was found a default empty box is returned 
    		return new Empty("17","Empty","Free");
    	
    }
    
    public static int findIndex(int i, int j) {  // i and j is converted to 10 based numeric system to find index of a box.
    	return i*N + j;	// knowing index = i*N^1 + j*N^0, we compute index
    }
     
    public static int[] findIJ(int index) { 	// according to the index value in boxes list taken, i and j (row and column) values in grid are returned in an array.
    	if(index<(N*N+2)){	// index must not past 16
	    	int[] ij = new int[2];
	    	for(int i=1;i>=0;i--) {	// index value is converted to N based numeric system to be represented as i and j.
	    		ij[i]= index / (int)Math.pow(N, i);	// knowing index = i*N^1 + j*N^0, we compute i and j
	    		index %= (int)Math.pow(N, i);
	    	}	    		
	    	return ij;
    	}else {
    		System.out.println(index+" is out of bounds.");	// If index value taken was out of bounds user is informed.
    		return null;
    	}
    }
   
    
    public static Box findBox(String id) {	// Box is found from the boxes arraylist using its string id
		for(int i = 0; i<boxes.size();i++) 
			if(boxes.get(i).getId().equals(id)) 
				return boxes.get(i);
		
		return new Empty(id,"Empty","Free");			
	}
 
}
