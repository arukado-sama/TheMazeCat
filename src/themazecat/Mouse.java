package themazecat;

import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

public class Mouse extends Thread{

	Maze maze;
	
	Entity mouse;
	
	boolean catched;
	
	
	public Mouse(Maze maze0, Entity mouse0) {
		
		maze = maze0;
		mouse = mouse0;
		catched = false;
	}
	
	public void move(String vector)
	{
		if(vector=="UP")
			if(maze.squares[mouse.y-1][mouse.x]!='#')
				mouse.move(vector);
		
		if(vector=="DOWN")
			if(maze.squares[mouse.y+1][mouse.x]!='#')
				mouse.move(vector);
		
		if(vector=="LEFT")
			if(maze.squares[mouse.y][mouse.x-1]!='#')
				mouse.move(vector);
		
		if(vector=="RIGHT")
			if(maze.squares[mouse.y][mouse.x+1]!='#')
				mouse.move(vector);
	}
	
	public void seeCat()
	{
		//le chat arrive du haut
		
		if((mouse.x == maze.cat.x)&&(mouse.y-1 == maze.cat.y))
		{
			if(maze.squares[mouse.y+1][mouse.x]!='#')
				move("DOWN");
			
			else
			{
				if(maze.squares[mouse.y][mouse.x-1]=='#')
					move("RIGHT");
				
				if(maze.squares[mouse.y][mouse.x+1]=='#')
					move("LEFT");
				
				if((maze.squares[mouse.y][mouse.x-1]!='#')&&(maze.squares[mouse.y][mouse.x+1]!='#'))
					move("LEFT");
			}
		}
				
		else
				
		//le chat arrive du bas
				
		if((mouse.x == maze.cat.x)&&(mouse.y+1 == maze.cat.y))
		{
			if(maze.squares[mouse.y-1][mouse.x]!='#')
				move("UP");
			
			else
			{
				if(maze.squares[mouse.y][mouse.x-1]=='#')
					move("RIGHT");
				
				if(maze.squares[mouse.y][mouse.x+1]=='#')
					move("LEFT");
				
				if((maze.squares[mouse.y][mouse.x-1]!='#')&&(maze.squares[mouse.y][mouse.x+1]!='#'))
					move("RIGHT");
			}
		}
		
		//le chat arrive de la gauche
		
		if((mouse.x-1 == maze.cat.x)&&(mouse.y == maze.cat.y))
		{
			if(maze.squares[mouse.y][mouse.x+1]!='#')
				move("RIGHT");
			
			else
			{
				if(maze.squares[mouse.y+1][mouse.x]=='#')
					move("UP");
				
				if(maze.squares[mouse.y-1][mouse.x]=='#')
					move("DOWN");
				
				if((maze.squares[mouse.y-1][mouse.x]!='#')&&(maze.squares[mouse.y+1][mouse.x]!='#'))
					move("DOWN");
			}
		}
		
		else
			
		//le chat arrive de la droite
			
		if((mouse.x+1 == maze.cat.x)&&(mouse.y == maze.cat.y))
		{
			if(maze.squares[mouse.y][mouse.x-1]!='#')
				move("LEFT");
				
			else
			{
				if(maze.squares[mouse.y+1][mouse.x]=='#')
					move("UP");
				
				if(maze.squares[mouse.y-1][mouse.x]=='#')
					move("DOWN");
				
				if((maze.squares[mouse.y+1][mouse.x]!='#')&&(maze.squares[mouse.y-1][mouse.x]!='#'))
					move("UP");
			}
		}
		
		else
			
		//le chat arrive d'en bas à droite
				
		if((mouse.x+1 == maze.cat.x)&&(mouse.y+1 == maze.cat.y))
		{
			if(maze.squares[mouse.y-1][mouse.x+1]!='#')
			{
				move("LEFT");
				move("UP");
			}
			
			else
			{
				if(maze.squares[mouse.y+1][mouse.x]=='#')
					move("UP");
				
				if(maze.squares[mouse.y-1][mouse.x]=='#')
					move("DOWN");
			}
		}
		
		else
		
		//le chat arrive d'en haut à gauche
		
		if((mouse.x-1 == maze.cat.x)&&(mouse.y-1 == maze.cat.y))
		{
			move("RIGHT");
			move("DOWN");
		}
		
		else
		
		//le chat arrive d'en bas à gauche
		
		if((mouse.x-1 == maze.cat.x)&&(mouse.y+1 == maze.cat.y))
		{
			move("RIGHT");
			move("UP");
		}
		
		else
		
		//le chat arrive d'en haut à droite
		
		if((mouse.x+1 == maze.cat.x)&&(mouse.y-1 == maze.cat.y))
		{
			move("LEFT");
			move("DOWN");
		}
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("deprecation")
	public void catchedByCatTrap()
	{
		if(((maze.cat.x == mouse.x)&&(maze.cat.y == mouse.y))||(maze.squares[mouse.y][mouse.x]=='T'))
		{
			mouse.type = "DEAD";
			
			catched = true;
			
			maze.catchedMouses++;
			
			stop();
		}
	}
	
	public void run()
	{	
		while(catched==false)
		{
			seeCat();
			catchedByCatTrap();
		}
	}

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		
		String file="";
		
		JFileChooser choosenfile = new JFileChooser();
		
		if(choosenfile.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
		{
			file = choosenfile.getSelectedFile().getAbsolutePath();
		
			Maze testmaze = new Maze(file);
					
			int nb = testmaze.nbMouses;
					
			Mouse mouses[] = new Mouse[nb];
					
			for(int i=0; i<testmaze.nbMouses; i++)
			{
				mouses[i] = new Mouse(testmaze, testmaze.mouses[i]);
				mouses[i].start();
			}
	
			while(!testmaze.quit)
			{
				testmaze.keyboard();
					
				testmaze.animation();
				
				testmaze.winlose();
				
				if(testmaze.reload)
				{
					for(int i=0; i<testmaze.nbMouses; i++)
					{
						if(mouses[i].isAlive())
							mouses[i].stop();
					}
					
					testmaze = new Maze(testmaze.filepath);
					
					nb = testmaze.nbMouses;
					
					mouses = new Mouse[nb];
					
					for(int i=0; i<testmaze.nbMouses; i++)
					{
						mouses[i] = new Mouse(testmaze, testmaze.mouses[i]);
						mouses[i].start();
					}
					
					testmaze.reload = false;
				}
			}
			
			for(int i=0; i<testmaze.nbMouses; i++)
			{
				if(mouses[i].isAlive())
					mouses[i].stop();
			}
		}
		
		else choosenfile.cancelSelection();
	}
}
