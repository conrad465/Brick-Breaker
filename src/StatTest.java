
public class StatTest {
	
	
	public static void main(String[] args)
	{
		
		boolean winner=false;
		int p1wins=0;
		int p2wins=0;
		double ratio;
		double random1;
		double random2;
		for(int a=0; a<1000000; a++)
		{
			while(!winner)
			{
				random1= (double)Math.random();
				random2= (double)Math.random();
				if(random1>.5 && random2<.5)
				{
					p1wins++;
					winner=true;
				}
				if(random1<.5 && random2>.5)
				{
					p2wins++;
					winner=true;
				}
			}
			winner=false;
		}
		System.out.print("the ratio of p1 and p2 wins is" + ((double)p1wins)/p2wins);
		
	}
	

}
