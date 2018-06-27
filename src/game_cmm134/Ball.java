package game_cmm134;
import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView{

	
	public static final int SIZE = 700;
    public static final int BRICKWIDTH=SIZE/12;
    public static final int BRICKHEIGHT=SIZE/24;

    public static final int PADDLE_LONG =1;
	public static final int PADDLE_SHORT=2;
	public static final int BALL_SPEED_CHANGE=3;
	public static final int COLOR_HITTER=5;
	public static final int STICKY_PADDLE=6;
	public static final int STANDARD_SPEED=150;
    public static final String BALL_STANDARD = "images/BallStandard.png";
    public static final String BALL_SPEEDY="images/BallFast.png";
    public static final String BALL_SLUGGISH="images/SlowBall.png";
    public static final String BALL_RAINBOW="images/RainbowBall.png";
	public final Image ballimagestandard = new Image(getClass().getClassLoader().getResourceAsStream(BALL_STANDARD));  
	public final Image ballimageslow = new Image(getClass().getClassLoader().getResourceAsStream(BALL_SLUGGISH));  
	public final Image ballimagefast = new Image(getClass().getClassLoader().getResourceAsStream(BALL_SPEEDY));  
	public final Image ballimagerainbow = new Image(getClass().getClassLoader().getResourceAsStream(BALL_RAINBOW));  

	
	private double direction;
	private final double pi = Math.PI;
	private double speed;
	private double[] powerup = {0,0,0,0,0,0,0};
	private double paddleloc=0;
	private double topbottom=0;
	
	
	public Ball()
	{
		super();
		direction=pi/2.5;
		speed=STANDARD_SPEED; // should be a global variable of main
		this.setImage(ballimagestandard);
		this.setX(SIZE/2);
		this.setY(SIZE-BRICKHEIGHT-90);
		this.setFitHeight(15);
		this.setFitWidth(15);
	}
	
	
	public int update(double time, ArrayList<Block> blocks, Paddle p1, Paddle p2, ArrayList<Powerup> powerups)
	{
		int points=0;
		if(getX()>SIZE || getX()<0) 
			direction = pi-direction;
		points=10*checkBlocks(blocks, powerups);
		checkPaddles(p1,p2);
		powerupChecker(time);
		points+=checkInBounds(p1,p2);
		this.setX(this.getX()+speed*time*Math.cos(direction));
		this.setY(this.getY()-speed*time*Math.sin(direction));
		return points;
	}
	
	public void changeSpeed(double scale, int p) 
	{
		if(scale>1)
			this.setImage(ballimagefast);
		else
			this.setImage(ballimageslow);
		speed=speed*scale;
		powerup[BALL_SPEED_CHANGE]=20;
	}
	
	public void reset() //called when a powerup is no longer active to make the ball standard again 
	{
		this.speed=this.STANDARD_SPEED;
		this.setImage(ballimagestandard);
		paddleloc=0;
	}
	
	public double releaseAngle(Paddle p1) //calculates the angle of release based on intersection of ball and paddle
	{
		return ((p1.getFitWidth()-(this.getX()-p1.getX()))/p1.getFitWidth()+.1)*(pi-pi/3);
	}
	
	public void checkPaddles(Paddle p1,Paddle p2) //checks intersection with paddles
	{
		if(p1.getBoundsInParent().intersects(this.getBoundsInParent()))
		{
			if(!(powerup[STICKY_PADDLE]<=0))
			{
				topbottom=1;
				this.speed=0;
				if(paddleloc==0)
					paddleloc=p1.getX()-this.getX();
				this.setX(p1.getX()-paddleloc);
			}	
			direction = releaseAngle(p1);
		}
				
		if(p2.getBoundsInParent().intersects(this.getBoundsInParent()))
		{
			if(!(powerup[STICKY_PADDLE]<=0))
			{
				topbottom=-1;
				if(paddleloc==0)
					paddleloc=p2.getX()-this.getX();
				this.speed=0;
				this.setX(p2.getX()-paddleloc);
			}
			direction =2*pi- releaseAngle(p2);
		}
	}
	
	public int checkBlocks(ArrayList<Block> blocks, ArrayList<Powerup> powerups) //checks intersection with all blocks and
	{																			 //and handles calls update on blocks and powerups
		int blockshit=0;
		for(int a=0; a<blocks.size(); a++)
		{
			if((blocks.get(a).getBoundsInParent().intersects(this.getBoundsInParent())))
			{
				if(!(this.powerup[COLOR_HITTER]<=0))
					for(int b=0; b<blocks.size(); b++)
					{
						if(b!=a && blocks.get(b).color==blocks.get(a).color)
						{
							blocks.get(b).hit();
							if(blocks.get(a).color!=5)
								blockshit++;
							if(blocks.get(b).color==0)	
								powerups.get(b).setSpeed(100);
						}
					}
				
				if(getY()+getFitHeight()/1.5>=blocks.get(a).getY() && 
						getY()<=blocks.get(a).getY()+blocks.get(a).getFitHeight()-getFitHeight()/1.5)
					direction = pi-direction;
				else
					direction =  pi*2-direction;
				
				blocks.get(a).hit();
				if(blocks.get(a).color==0)	
					powerups.get(a).setSpeed(100);
				if(blocks.get(a).color!=5)
					blockshit++;
				break;	
			}
		}

		return blockshit;
	}
	
	public int checkInBounds(Paddle p1,Paddle p2) //checks if ball is in play or out
	{
		if(this.getY()<0)
		{	
			this.setX(p2.getX()+p2.getFitWidth()/2);
			this.setY(p2.getY()+this.getFitHeight());
			this.powerup[STICKY_PADDLE]=3;
			return -1;
		}
		else if(this.getY()>SIZE)
		{
			this.setX(p1.getX()+p1.getFitWidth()/2);
			this.setY(p1.getY()-this.getFitHeight());
			this.powerup[STICKY_PADDLE]=3;
			return -1;
		}
		return 0;
	}
	
	public void powerupChecker(double time) //updates which powerups are active on the ball
	{

		for(int a=0; a<powerup.length; a++)
			if(powerup[a]>0)
				powerup[a]-=time;
		
				if(powerup[BALL_SPEED_CHANGE]<0)
				{
					reset();
					powerup[BALL_SPEED_CHANGE]=0;
				}
				if(powerup[STICKY_PADDLE]<0)
				{
					reset();
					powerup[STICKY_PADDLE]=0;
				}
				if(powerup[COLOR_HITTER]<0)
				{
					reset();
					powerup[COLOR_HITTER]=0;
				}
				else if(powerup[COLOR_HITTER]>0)
				{
					this.setImage(ballimagerainbow);
				}
		
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double[] getPowerups()
	{
		return powerup;
	}
	public double topOrBottom()
	{
		return topbottom;
	}
	public void setSpeed(int s)
	{
		speed=s;
	}
	

}
