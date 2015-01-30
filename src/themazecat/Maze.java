package themazecat;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import org.jsfml.graphics.*;
import org.jsfml.window.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.*;

public class Maze {

	//fenêtre du jeu
	RenderWindow mazewindow;
	
	//cases du labyrinthe
	char squares[][];
	
	//hauteur de la matrice
	int size1;
	
	//largeur de la matrice
	int size2;
	
	//dimensions de la fenêtre
	int WINDOW_HEIGHT;
	int WINDOW_WIDTH;
	
	//nombre de souris
	int nbMouses;
	
	//nombre de souris attrapées
	int catchedMouses;
	
	//timer du labyrinthe
	long timer;
	
	//temps de jeu
	int time;

	//redémarrage du labyrinthe
	boolean reload;
	
	//fermeture du jeu
	boolean quit;
	
	//chemin complet du fichier labyrinthe
	String filepath;
	
	//sprite du labyrinthe
	Mazesprite maze;
	
	//chat
	Entity cat;
	
	//tableau des souris
	Entity mouses[];
	

	//Constructeur
	public Maze(String path) throws FileNotFoundException {
		
		//pas de redémarrage au lancement d'un labyrinthe
		reload = false;
		
		//pas de sortie du jeu
		quit = false;
		
		//enregistrer le chemin du fichier
		filepath = path;
		
		//initialiser le labyrinthe
		initMaze();
		
		//initialier les cases et entités du labyrinthe
		initSquaresEntities();
		
		//démarrer le timer
		timer = System.currentTimeMillis()/1000;
	}
	
	//Le labyrinthe est-il ouvert ?
	public boolean isOpen()
	{
		return mazewindow.isOpen();
	}
	
	//Contrôle du chat et des menus
	public void keyboard()
	{
		for(Event event : mazewindow.pollEvents())
		{
			//fermeture du jeu si click sur la croix
			if(event.type == Event.Type.CLOSED)
				mazewindow.close();
			
			if(event.type == Event.Type.KEY_PRESSED)
			{
				//fermeture du jeu si Echap
				if(Keyboard.isKeyPressed(Key.ESCAPE))
				{
					quit = true;
					mazewindow.close();
				}
				
				//collisions du chat
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
				
				//charger un nouveau fichier
				if(Keyboard.isKeyPressed(Key.C))
				{
					//fenêtre de sélection de fichier
					JFileChooser choosenfile = new JFileChooser();
					
					//si un fichier est choisi
					if(choosenfile.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
					{
						//fermer le labyrinthe actuel
						mazewindow.close();
						
						//récupérer le chemin du nouveau fichier
						String file = choosenfile.getSelectedFile().getAbsolutePath();
						
						//enregistrer le chemin
						filepath = file;

						//récupérer le temps associé
						Pattern p = Pattern.compile("\\- \\d+");
						Matcher m = p.matcher(filepath);
						m.find();
						p = Pattern.compile("\\d+");
						m = p.matcher(filepath);
						m.find();
						time = Integer.parseInt(m.group());
						
						//redémarrage du labyrinthe
						reload = true;
					}
					
					//sinon retourner au jeu
					else choosenfile.cancelSelection();
				}
				
				//redémarrer le même labyrinthe
				if(Keyboard.isKeyPressed(Key.R))
				{
					//fermer le labyrinthe
					mazewindow.close();
					
					//redémarrage
					reload = true;
				}
			}
		}
	}
	
	//Initialisation du labyrinthe
	public void initMaze() throws FileNotFoundException
	{
		//sauvegarder les tailles de la matrice
		saveSizes();
		
		//créer la fenêtre
		mazewindow = new RenderWindow();
		mazewindow.create(new VideoMode(WINDOW_WIDTH, WINDOW_HEIGHT+40, 32), "The Maze Cat");
		
		//définir le nombre de FPS
		mazewindow.setFramerateLimit(60);
		
		//afficher le fond du labyrinthe
		maze = new Mazesprite("img/maze.png");
		
		//initialiser la position du fond
		maze.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
	}
	
	//Animations des sprites
	public void animation()
	{	
		//initialiser les sprites des éléments immobiles
		Mazesprite wall = new Mazesprite("img/wall.png");
		Mazesprite trap = new Mazesprite("img/trap.png");
		Mazesprite menu = new Mazesprite("img/menu.png");
		
		//nettoyer la fenêtre
		mazewindow.clear();
		
		//dessiner le fond du labyrinthe
		mazewindow.draw(maze);
		
		//initialiser la position du menu
		menu.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT+20);
		
		//dessiner le menu
		mazewindow.draw(menu);
		
		//placer et dessiner les murs et les pièges
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
		
		//dessiner les souris si elles ne sont pas mortes
		for(int i=0; i<nbMouses; i++)
		{	
			if(mouses[i].type!="DEAD")
			{
				mouses[i].entity.setPosition(mouses[i].x*40+20, mouses[i].y*40+20);
				mazewindow.draw(mouses[i].entity);
			}
		}
		
		//initialiser la position du chat
		cat.entity.setPosition(cat.x*40+20, cat.y*40+20);
		
		//dessiner le chat
		mazewindow.draw(cat.entity);
		
		//tout afficher dans l'ordre
		mazewindow.display();
	}
	
	//Sauvegarder les tailles de la matrice du labyrinthe
	public void saveSizes() throws FileNotFoundException
	{
		//initialiser le nombre de souris
		nbMouses = 0;
		
		//lire le fichier labyrinthe
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
				
				//incrémenter le nombre de souris
				if(c=='M') nbMouses++;
			}
		
		//fermer le fichier
		chars.close();
		
		//enregistrer les tailles
		size1 = line;
		size2 = number/line-1;
		
		//initialiser les dimensions de la fenêtre
		WINDOW_HEIGHT = size1*40;
		WINDOW_WIDTH = size2*40;
	}
	
	//Sauvegarde des cases du labyrinthe et création des entités
	public void saveSquaresEntities() throws FileNotFoundException
	{	
		//affichage debug des tailles de la matrice
		System.out.println(size1+" "+size2);
		
		//initialisation des compteurs
		int i=0, j=0, nbm=0;
		
		Scanner wall = new Scanner(new File(filepath));
		
		wall.useDelimiter("");
		
		while((wall.hasNextLine())&&(i<size1))				
			for(char c : wall.next().toCharArray())
			{	
				squares[i][j]=c;
					
				if(c=='C')
				{
					//créer le chat
					cat = new Entity(j, i, "CAT");
					squares[i][j]=' ';
				}
				
				if(c=='M')
				{
					//créer une souris
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
		
		//affichage debug du labyrinthe (sans les entités)
		printMaze();
	}
	
	//Initialisation des matrices et tableaux
	public void initSquaresEntities() throws FileNotFoundException
	{
		squares = new char[size1][];
		
		for(int i=0; i<size1; i++)
			squares[i]=new char[size2];
		
		mouses = new Entity[nbMouses];
		
		//sauvegarde des cases du labyrinthe et création des entités
		saveSquaresEntities();
		
		//initalisation du nombre de souris attrapées
		catchedMouses = 0;
	}
	
	//Victoire et défaite
	public void winlose()
	{
		//si toutes les souris sont attrapées
		if(catchedMouses == nbMouses)
		{
			//initialiser le message de victoire
			Mazesprite win = new Mazesprite("img/win.png");
			
			//initialiser la position du message
			win.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
			
			//dessiner le message
			mazewindow.draw(win);
			
			//afficher sur le labyrinthe
			mazewindow.display();
			
			//attendre 3 sec
			try
			{
		        Thread.sleep(3000);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
		    }
			
			//fermer le labyrinthe
			mazewindow.close();
			
			//redémarrage du même labyrinthe
			reload = true;
		}
		
		//sinon
		else
		{
			//si temps limite atteint
			if(timer<(System.currentTimeMillis()/1000-time))
			{	
				//initialiser le message de défaite
				Mazesprite lose = new Mazesprite("img/lose.png");
				
				//initialiser la position du message
				lose.setPosition(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
				
				//dessiner le message
				mazewindow.draw(lose);
				
				//afficher sur le labyrinthe
				mazewindow.display();
				
				//attendre 3 sec
				try
				{
			        Thread.sleep(3000);
				}
				catch (InterruptedException ie)
				{
					ie.printStackTrace();
			    }
				
				//fermer le labyrinthe
				mazewindow.close();
				
				//redémarrage du même labyrinthe
				reload = true;
			}
		}
	}
	
	//Affichage debug du labyrinthe (sans les entités)
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
