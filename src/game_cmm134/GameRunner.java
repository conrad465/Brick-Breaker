package game_cmm134;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

//handles creating scenes and updating the scenes 


public class GameRunner extends Application {
	
	public static final String TITLE = "Brick Breaker";
    public static final int SIZE = 700;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final int BRICKWIDTH=SIZE/12;
    public static final int BRICKHEIGHT=SIZE/24;
	public static final int PADDLE_LONG =1;
	public static final int PADDLE_SHORT=2;
	public static final int BALL_FAST=3;
	public static final int BALL_SLOW =4;
	public static final int COLOR_HITTER=5;
	public static final int STICKY_PADDLE=6;

    public Level myLevel;
    public Level myLevel1=new Level("/Users/conradmitchell/IdeaProjects/BrickBreaker/level1",1); //fix the redundancy
    public Level myLevel2=new Level("/Users/conradmitchell/IdeaProjects/BrickBreaker/level2",1);
    public Level myLevel3=new Level("/Users/conradmitchell/IdeaProjects/BrickBreaker/level3",1);
    Image endscreen = new Image(getClass().getClassLoader().getResourceAsStream("images/YouWin.png"));
	Image startscreen = new Image(getClass().getClassLoader().getResourceAsStream("images/breakout_menu.png"));
	Image gameover = new Image(getClass().getClassLoader().getResourceAsStream("images/GameOver.png"));
    
    private Stage myStage;
    private Scene myScene;
    
    public int score=0;
    public int levelselect=1;
    public int lives=3;
    
    public Paddle myPaddle1;
    public Paddle myPaddle2;
    public Ball myBall;
    public ArrayList<Powerup> myPowerups = new ArrayList<Powerup>();
    public ArrayList<Block> myBlocks = new ArrayList<Block>();
    
    public Text myScore;
    public Text myLives;
    

    
    @Override
    public void start(Stage stage) //called to create stages
    {	
    	myStage=stage;
    	myScene = setupGame(SIZE, SIZE, BACKGROUND, levelselect);
    
    		
    	myStage.setScene(myScene);
    	myStage.setTitle(TITLE);
    	myStage.show();
    	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
    									e -> step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
    }
	//Image endscreen = new Image(getClass().getClassLoader().getResourceAsStream("YouWin.png")); 
	//Image gameover = new Image(getClass().getClassLoader().getResourceAsStream("GameOver.png"));

    
    private Scene setupGame(int width, int height, Paint background, int level)
    {
    	if(level==1 || level==2 || level==3)
    		return nextLevel(level);
    	ImageView sc = new ImageView();
    	Group root = new Group();
    	Scene scene = new Scene(root, width, height, background);
    	if(level==0)
    	{
        	sc = new ImageView(startscreen);
            scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
    	}
    	if(level==4)
    	{
    		sc = new ImageView(endscreen);
    	}
    	if(level==100)
    	{
    		sc = new ImageView(gameover);
    	}
    	sc.setFitWidth(SIZE);
    	sc.setFitHeight(SIZE);
    	root.getChildren().add(sc);
    	
    	return scene;
    }
    
    public Scene nextLevel(int level)
    {
    	Group root = new Group();
    	Scene scene = new Scene(root, SIZE, SIZE, BACKGROUND);

    	if(level==1)
    		myLevel=myLevel1;
    	if(level==2)
    		myLevel=myLevel2;
    	if(level==3)
    		myLevel=myLevel3;
    	
    	myScore = new Text("Score: "+score);
    	myLives = new Text("Lives: "+ lives);
    	
    	myScore.setX(5);
    	myScore.setY(SIZE-5);
    	
    	myLives.setX(SIZE-40);
    	myLives.setY(SIZE-5);
    	
    	myPaddle2 = new Paddle(20);
        myPaddle1 = new Paddle(SIZE-40);
    	myBall = new Ball();
    	myBlocks = myLevel.getBlocks();
    	myPowerups = myLevel.getPowerups();
    	
    	for(int a=0; a<myPowerups.size(); a++)
    	{
    		root.getChildren().add(myPowerups.get(a));
    	}
    	for(int a=0; a<myBlocks.size(); a++)
    	{
    		root.getChildren().add(myBlocks.get(a));
    	}
    	
    	root.getChildren().add(myScore);
    	root.getChildren().add(myLives);
    	root.getChildren().add(myPaddle1);
    	root.getChildren().add(myPaddle2);
    	root.getChildren().add(myBall);
    	
    	scene.setOnKeyPressed(e -> handleKeyInputP(e.getCode()));
    	scene.setOnKeyReleased(e -> handleKeyInputR(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;	
    }

    
    public void step(double timePassed) //updates states and locations of paddles ball bricks and powerups
    {
    	int tempscore=0;
    	if(levelselect!=0 && levelselect<4)
    	{
    		tempscore=myBall.update(timePassed, myBlocks, myPaddle1, myPaddle2, myPowerups); 
    		if(tempscore<0)
    		{
    			lives--;
    			updateText(lives,myLives);
    		}
    		else if(tempscore!=0)
    		{
    			score+=tempscore;
    			updateText(score, myScore);
    		}
    		for(int a=0; a<myPowerups.size(); a++)
	    			myPowerups.get(a).update(timePassed, myPaddle1, myPaddle2, myBall);
    		myPaddle1.update(timePassed);
	    	myPaddle2.update(timePassed);
	    	gameEnd();
    	}
    }
   
    public static void main (String[] args) {
        launch(args);
    }
    
    private void handleKeyInputP (KeyCode code) { //handles moving the paddle 
        if (code == KeyCode.RIGHT) {
            myPaddle1.setSpeed(200);
        }
        else if (code == KeyCode.LEFT) {
            myPaddle1.setSpeed(-200);
        }     
        if(code==KeyCode.D)
        	myPaddle2.setSpeed(200);
        else if(code==KeyCode.A)
        	myPaddle2.setSpeed(-200);
        if(code==KeyCode.SPACE && !(myBall.getPowerups()[STICKY_PADDLE]<=0))
        {
        	myBall.setSpeed(200);
        	myBall.reset();
        	myBall.setY(myBall.getY()-myBall.topOrBottom()*myBall.getFitHeight());
        }

    }
    private void handleKeyInputR(KeyCode code) //handles moving the paddle
    {
    	
    	if(code == KeyCode.RIGHT || code== KeyCode.LEFT)
    		myPaddle1.setSpeed(0);
    	if(code == KeyCode.A || code==KeyCode.D)
    		myPaddle2.setSpeed(0);
        
    }
    
    private void handleMouseInput(double x, double y) //handles navigating out of splash screen and cheat
    {
    	if(levelselect==0)
    	{
	    	levelselect++;
	    	start(myStage);
    	}
    	else
    	{
    		myBall.setX(x);
    		myBall.setY(y);
    	}
    }
    public int gameEnd()
    {
    	boolean win=true;
    	for(int a=0; a<myBlocks.size(); a++)
    	{
    		if(myBlocks.get(a).alive && myBlocks.get(a).color!=5)
	    	{ 	
    			win=false;
	    		break;
    		}
    	}
    	if(win)
    		levelselect++;
    	else if(lives==0)
    		levelselect=100;
    	else
    		return 0;
    	start(myStage);
    	return 0;
    }
    
    public void updateText(int add, Text text)
    {
    	String tempscore=text.getText();
    	int startIndex =tempscore.indexOf(" ")+1;
    	String message = text.getText().substring(0,startIndex);
    	text.setText(message + (add));
    }
}
