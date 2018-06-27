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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Paddle extends ImageView{
    //creation and updating of paddle 
	
	public static final int SIZE = 700;
    
    public static final String PADDLE_STANDARD = "images/PaddleStandard.png";
    public static final int BRICKWIDTH=SIZE/12;
    public static final int BRICKHEIGHT=SIZE/24;
	public static final int PADDLE_LONG =1;
	public static final int PADDLE_SHORT=2;
	public static final int BALL_FAST=3;
	public static final int BALL_SLOW =4;
	public static final int COLOR_HITTER=5;
	public static final int STICKY_PADDLE=6;
	
	
	private int yloc;
	private int speed;
	private double[] powerup= {0,0,0,0,0,0};
	private double width;	
	private int height;
	
	public Paddle(int y)
	{
		super();
		yloc=y;
		width=BRICKWIDTH*3;
		height = BRICKHEIGHT/3;
		speed=0;
		
    	Image pViewImage = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE_STANDARD));    	

    	this.setImage(pViewImage);
    	this.setX(SIZE/2-width/2);
    	this.setY(yloc);
    	this.setFitWidth(width);
    	this.setFitHeight(height);		 
	}
	
	public void update(double time) //changes location and updates which powerups are active
	{
		if((this.getX()>0 && speed<0) || (this.getX()<(SIZE-this.getFitWidth()) && speed>0))
			this.setX(time*speed+this.getX());
		for(int a=0; a<powerup.length; a++)
			if(!(powerup[a]<0))
			{
				if(powerup[PADDLE_LONG]<=time*2 && powerup[PADDLE_SHORT]<=time*2)
					this.reset();
				powerup[a]-=time;
			}
	
	}
	
	public void changeWidth(double scale, int p)
	{
		this.setFitWidth(this.width*scale);
		if(this.getFitWidth()+this.getX()>SIZE)
			this.setX(SIZE-this.getFitWidth());
		if(this.getX()<0)
			this.setX(0);
		this.powerup[p]=20;
	}
	
	public void reset()
	{
		this.setFitWidth(width);
	}
	
	public double getSpeed()
	{
		return speed;
	}
	public void setSpeed(int s)
	{
		speed=s;
	}

}
