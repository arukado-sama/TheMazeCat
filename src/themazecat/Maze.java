package themazecat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jsfml.graphics.*;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.*;

public class Maze {

	RenderWindow mazewindow;
	
	char squares[][];
	
	int size1;
	
	int size2;
	
	int WINDOW_HEIGHT;
	int WINDOW_WIDTH;
	
	int nbMouses;
	
	int catchedMouses;

	boolean reload;
	boolean quit;
	
	String filepath;
	
	Mazesprite maze;
	
	Entity cat;
	
	Entity mouses[];
	

	public Maze(String path) throws FileNotFoundException {
		
		reload = false;
		quit = false;
		filepath = path;
		
		initMaze();
		
		initSquaresEntities();
	}
	
	public boolean isOpen()
	{
		return mazewindow.isOpen();
	}
	
	public void keyboard()
	{
		for(Event event : mazewindow.pollEvents())
		{
			if(event.type == Event.Type.CLOSED)
				mazewindow.close();
			
			if(event.type == Event.Type.KEY_PRESSED)
			{
				if(Keyboard.isKeyPressed(Key.ESCAPE))
				{
					quit = true;
					mazewindow.close();
				}
				
				if(Keyboard.isKeyPressed(Key.UP))
					if(squares[cat.y-1][cat.x]!='#')
						cat.move("UP");
					
				if(Keyboard.isKeyPressed(Key.DOWN))
					if(squares[cat.y+1][cat.x]!='#')
						cat.move("DOWN");
						
				if(Keyboard.isKeyPressed(Key.LEFT))
					if(squares[cat.y][cat.x-1]!='#')
						cat.move("LEFT");
				
				if(Keyboard.isKeyPressed(Key.RIGHT))
					if(squares[cat.y][cat.x+1]!='#')
						cat.move("RIGHT");
				
				if(Keyboard.isKeyPressed(Key.C))
				{
					JFileChooser choosenfile = new JFileChooser();
					
					if(choosenfile.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
					{
						mazewindow.close();
						
						String file = choosenfile.getSelectedFile().getAbsolutePath();
						
						filepath = file;
						
						reload = true;
					}
					
					else choosenfile.cancelSelection();
				}
				
				if(Keyboard.isKeyPressed(Key.R))
				{
					mazewindow.close();
					
					reload = true;
				}
			}
		}
	}
	
	public void initMaze() throws FileNotFoundException
	{
		saveSizes();
		
		mazewindow = new RenderWindow();
		
		mazewindow.create(new VideoMode(WINDOW_WIDTH, WINDOW_HEIGHT+40, 32), "The Maze Cat");
		
		mazewindow.setFramerateLimit(60);
		
		maze = new Mazesprite("img/maze.png");
		
		maze.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
	}
	
	public void animation()
	{	
		Mazesprite wall = new Mazesprite("img/wall.png");
		Mazesprite trap = new Mazesprite("img/trap.png");
		Mazesprite menu = new Mazesprite("img/menu.png");
		
		mazewindow.clear();
		
		mazewindow.draw(maze);
		
		menu.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT+20);
		
		mazewindow.draw(menu);
				
		for(int i=0; i<size1; i++)
			for(int j=0; j<size2; j++)
			{
				if(squares[i][j]=='#')
				{
					wall.setPosition(j*40+20, i*40+20);
					mazewindow.draw(wall);
				}
				
				if(squares[i][j]=='T')
				{
					trap.setPosition(j*40+20, i*40+20);
					mazewindow.draw(trap);
				}
			}
		
		for(int i=0; i<nbMouses; i++)
		{	
			if(mouses[i].type!="DEAD")
			{
				mouses[i].entity.setPosition(mouses[i].x*40+20, mouses[i].y*40+20);
				mazewindow.draw(mouses[i].entity);
			}
		}
		
		cat.entity.setPosition(cat.x*40+20, cat.y*40+20);
		
		mazewindow.draw(cat.entity);
		
		mazewindow.display();
	}
	
	public void saveSizes() throws FileNotFoundException
	{
		nbMouses = 0;
		
		Scanner lines = new Scanner(new File(filepath));
		
		int line = 0;
		int number = 0;
		
		while(lines.hasNextLine())
		{
			line++;
			lines.nextLine();
		}
		
		lines.close();
		
		Scanner chars = new Scanner(new File(filepath));
		
		chars.useDelimiter("");
		
		while(chars.hasNextLine())				
			for(char c : chars.next().toCharArray())
			{
				number++;
				
				if(c=='M') nbMouses++;
			}
		
		chars.close();
		
		size1 = line;
		size2 = number/line-1;
		
		WINDOW_HEIGHT = size1*40;
		WINDOW_WIDTH = size2*40;
	}
	
	public void saveSquaresEntities() throws FileNotFoundException
	{	
		System.out.println(size1+" "+size2);
		
		int i=0, j=0, nbm=0;
		
		Scanner wall = new Scanner(new File(filepath));
		
		wall.useDelimiter("");
		
		while((wall.hasNextLine())&&(i<size1))				
			for(char c : wall.next().toCharArray())
			{	
				squares[i][j]=c;
					
				if(c=='C')
				{
					cat = new Entity(j, i, "CAT");
					squares[i][j]=' ';
				}
				
				if(c=='M')
				{
					mouses[nbm] = new Entity(j, i, "MOUSE");
					squares[i][j]=' ';
					nbm++;
				}
				
				j++;
				
				if(j==size2)
				{
					i++;
					j=0;
					wall.next();
				}
			}
		
		wall.close();
		
		printMaze();
	}
	
	public void initSquaresEntities() throws FileNotFoundException
	{
		squares = new char[size1][];
		
		for(int i=0; i<size1; i++)
			squares[i]=new char[size2];
		
		mouses = new Entity[nbMouses];
		
		saveSquaresEntities();
		
		catchedMouses = 0;
	}
	
	public void winlose()
	{
		if(catchedMouses == nbMouses)
		{
			Mazesprite win = new Mazesprite("img/win.png");
			
			win.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
			
			mazewindow.draw(win);
			
			mazewindow.display();
			
			try
			{
		        Thread.sleep(3000);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
		    }
			
			mazewindow.close();
			
			reload = true;
		}
	}
	
	public void printMaze()
	{
		for(int i=0; i<size1; i++)
		{	
			for(int j=0; j<size2; j++)
			{
				System.out.print(squares[i][j]);
			}
			
			System.out.print("\n");
		}
	}
}
