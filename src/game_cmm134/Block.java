package game_cmm134;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//creates rectangluar bricks imageviews for the main scene for each level
//call constructor for powerups 
//handles when the block is hit and updating color


public class Block extends ImageView {
	
    public static final int SIZE = 700;
    public static final int BRICKWIDTH=SIZE/12;
    public static final int BRICKHEIGHT=SIZE/24;
    
    public static final String UBRICK ="images/PaddleStandard.png";
    public static final String YBRICK="images/YellowBrick.png";
    public static final String PBRICK="images/PurpleBrick.png";
    public static final String GBRICK="images/GreenBrick.png";
    public static final String BBRICK="images/BlueBrick.png";
 

	int xloc; 
	int yloc;
	int color;
	boolean alive=true;
	
	public Block(int x, int y, int c)  
	{
		super();
		xloc=x;
		yloc=y;
		color=c;

		Image blockimage = updateImage();
    	setImage(blockimage);
		setX(xloc*BRICKWIDTH);
    	setY((yloc+2)*BRICKHEIGHT);
    	setFitHeight(BRICKHEIGHT);
    	setFitWidth(BRICKWIDTH);
  
    }
	
	public void hit() 
	{
		if(color<5)
		{
			color--;
			setImage(updateImage());
		}
	}
	
	public Image updateImage() //change color of the blocks based on their current strength as indicated by color int
	{
		if(color==0)
		{
			this.setX(1000);
			this.setY(1000);
			this.alive=false;
		}
		Image blockimage = new Image(getClass().getClassLoader().getResourceAsStream(PBRICK));
		if(color==1) {
			blockimage = new Image(getClass().getClassLoader().getResourceAsStream(YBRICK));
		}
		else if(color==2) {
			blockimage = new Image(getClass().getClassLoader().getResourceAsStream(GBRICK));
		}
		else if(color==3) {
			blockimage = new Image(getClass().getClassLoader().getResourceAsStream(BBRICK));
		} 
		else if(color==5)
			blockimage= new Image(getClass().getClassLoader().getResourceAsStream(UBRICK));
		return blockimage;
	}
}
