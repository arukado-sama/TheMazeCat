package themazecat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jsfml.graphics.*;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.*;

public class Maze {

	private RenderWindow mazewindow;
	
	private char squares[][];
	
	private int size1;
	
	private int size2;
	
	private int WINDOW_HEIGHT;
	private int WINDOW_WIDTH;
	
	private int nbMouses;
	
	private Mazesprite maze;
	
	private Entity cat;
	
	private Entity mouses[];
	

	public Maze() throws FileNotFoundException {
		
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
					mazewindow.close();
				
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
			}
		}
	}
	
	public void initMaze() throws FileNotFoundException
	{
		saveSizes();
		
		mazewindow = new RenderWindow();
		
		mazewindow.create(new VideoMode(WINDOW_WIDTH, WINDOW_HEIGHT, 32), "The Maze Cat");
		
		mazewindow.setFramerateLimit(60);
		
		maze = new Mazesprite("img/maze.png");
		
		maze.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
	}
	
	public void animation()
	{	
		Mazesprite wall = new Mazesprite("img/wall.png");
		Mazesprite trap = new Mazesprite("img/trap.png");
		
		mazewindow.clear();
		
		mazewindow.draw(maze);
				
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
			mouses[i].entity.setPosition(mouses[i].x*40+20, mouses[i].y*40+20);
			mazewindow.draw(mouses[i].entity);
		}
		
		cat.entity.setPosition(cat.x*40+20, cat.y*40+20);
		
		mazewindow.draw(cat.entity);
		
		mazewindow.display();
	}
	
	public void saveSizes() throws FileNotFoundException
	{
		nbMouses = 0;
		
		Scanner lines = new Scanner(new File("maze.txt"));
		
		int line = 0;
		int number = 0;
		
		while(lines.hasNextLine())
		{
			line++;
			lines.nextLine();
		}
		
		lines.close();
		
		Scanner chars = new Scanner(new File("maze.txt"));
		
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
		
		Scanner wall = new Scanner(new File("maze.txt"));
		
		wall.useDelimiter("");
		
		while((wall.hasNextLine())&&(i<size1))				
			for(char c : wall.next().toCharArray())
			{	
				squares[i][j]=c;
					
				if(c=='C')
				{
					cat = new Entity(j, i, "CAT");
				}
				
				if(c=='M')
				{
					mouses[nbm] = new Entity(j, i, "MOUSE");
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
	}
	
	public void startMouses()
	{
		for(int i=0; i<nbMouses; i++)
		{
			mouses[i].start();
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
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		Maze testmaze = new Maze();
		
		while(testmaze.isOpen())
		{
			testmaze.keyboard();
			
			testmaze.animation();
		}
	}

}
