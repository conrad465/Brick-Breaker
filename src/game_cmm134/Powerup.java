package game_cmm134;
import javafx.animation.KeyFrame;
import java.util.*;
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


public class Powerup extends ImageView {
	//creation and updating of powerups
	
    public static final int SIZE = 700;
    public static final int BRICKWIDTH=SIZE/12;
    public static final int BRICKHEIGHT=SIZE/24;
	public static final int PADDLE_LONG =1;
	public static final int PADDLE_SHORT=2;
	public static final int BALL_SPEED_CHANGE=3;
	public static final int COLOR_HITTER=5;
	public static final int STICKY_PADDLE=6;
    public static final String POWERUP_IMAGE="images/PowerUp.png";
    public final Image powerupimage = new Image(getClass().getClassLoader().getResourceAsStream(POWERUP_IMAGE));

	private double direction;
	private final double pi = Math.PI;
	private double speed=0;
	private int power;


	
	public Powerup(double x, double y, int p)
	{
		super();
		if(p!=0)
		{
			direction = pi*3/2;
			this.setImage(powerupimage);
			this.setX(x*BRICKWIDTH);
			this.setY((2+y)*BRICKHEIGHT);
			this.setFitHeight(30);
			this.setFitWidth(40);
			power=p;
		}
			
	}
	
		public void update(double time, Paddle p1, Paddle p2, Ball b1)	{ //when called updates necessary objects to be in the specified state
		if(p1.getBoundsInParent().intersects(this.getBoundsInParent()))
		{
			if(power==PADDLE_LONG)
			{
				p1.changeWidth(1.2,1);
				p2.changeWidth(1.2,1);
			}
			if(power==PADDLE_SHORT)
			{
				p1.changeWidth(.75,2);
				p2.changeWidth(.75,2);

			}
			if(power==BALL_SPEED_CHANGE)
			{
				double scale=(Math.random()*1.2)+.3;
				b1.changeSpeed(scale,3);
			}
			if(power==COLOR_HITTER)
			{
				b1.getPowerups()[5]=20;
			}
			if(power==STICKY_PADDLE)
				b1.getPowerups()[6]=20;
			this.setY(SIZE*3);
			this.setX(SIZE*3);
		}
		this.setY(this.getY()+speed*time);
	}
		public void setSpeed(double s)
		{
			speed=s;
		}
}
