package game_cmm134;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
//handles parsing seed files to create blocks and powerups

public class Level {
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	
	
	public Level(String filepath, int b)
	{
		try {
			File file = new File(filepath);
			int x;
			int y; 
			int c;
			int p;
			String temp="";
			Scanner sc = new Scanner(file);
			while(sc.hasNext()) {
				temp = sc.next();
				x=Integer.valueOf(temp.substring(0,temp.indexOf(",")));
				temp=temp.substring(temp.indexOf(",")+1);
				y=Integer.valueOf(temp.substring(0,temp.indexOf(",")));
				temp=temp.substring(temp.indexOf(",")+1);
				c=Integer.valueOf(temp.substring(0,temp.indexOf(",")));
				temp=temp.substring(temp.indexOf(",")+1);
				p=Integer.valueOf(temp.substring(0));
				blocks.add(new Block(x,y,c));
				powerups.add(new Powerup(x,y,p));
			}
			sc.close();
		}
		catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public ArrayList<Block> getBlocks()
	{
		return blocks;
	}
	public ArrayList<Powerup> getPowerups()
	{
		return powerups;
	}

}
