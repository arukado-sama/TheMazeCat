package themazecat;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class Mouse extends Thread{

	//labyrinthe
	Maze maze;
	
	//entité associée
	Entity mouse;
	
	//indicateur de souris attrapée
	boolean catched;
	
	//Constructeur
	public Mouse(Maze maze0, Entity mouse0) {
		
		//initialisation du labyrinthe et de la souris associée
		maze = maze0;
		mouse = mouse0;
		
		//la souris n'est pas encore attrapée
		catched = false;
	}
	
	//Contrôle des collisions de la souris
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
	
	//IA de la souris (finie à 62% seulement mais "potable")
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
		
		//la souris marque un temps d'arrêt entre chaque déplacement
		//sinon elle serait impossible à attraper
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	//La souris est attrapée par le chat ou un piège
	@SuppressWarnings("deprecation")
	public void catchedByCatTrap()
	{
		//si la souris est attrapée
		if(((maze.cat.x == mouse.x)&&(maze.cat.y == mouse.y))||(maze.squares[mouse.y][mouse.x]=='T'))
		{
			//la souris est morte
			mouse.type = "DEAD";
			
			//la souris est attrapée
			catched = true;
			
			//le nombre de souris attrapées est incrémenté
			maze.catchedMouses++;
			
			//arrêt du thread de la souris
			stop();
		}
	}
	
	//Lancement du thread
	public void run()
	{	
		//tant que la souris n'est pas attrapée
		while(catched==false)
		{
			//la souris bouge
			seeCat();
			
			//la souris est soumises aux dangers
			catchedByCatTrap();
		}
	}

	//PROGRAMME PRINCIPAL
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		
		//initialisation du chemin du fichier
		String file="";
		
		//demander le fichier
		JFileChooser choosenfile = new JFileChooser();
		
		//si fichier sélectionné
		if(choosenfile.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
		{
			//enregistrer le chemin
			file = choosenfile.getSelectedFile().getAbsolutePath();
		
			//créer un labyrinthe
			Maze testmaze = new Maze(file);
			
			//récupérer le temps associé
			Pattern p = Pattern.compile("\\- \\d+");
			Matcher m = p.matcher(file);
			m.find();
			p = Pattern.compile("\\d+");
			m = p.matcher(file);
			m.find();
			testmaze.time = Integer.parseInt(m.group());
			
			//initialiser le nombre de souris
			int nb = testmaze.nbMouses;
			
			//créer un tableau de souris
			Mouse mouses[] = new Mouse[nb];
			
			//initialiser et lancer les souris
			for(int i=0; i<testmaze.nbMouses; i++)
			{
				mouses[i] = new Mouse(testmaze, testmaze.mouses[i]);
				mouses[i].start();
			}
			
			//tant qu'on ne quitte pas le jeu
			while(!testmaze.quit)
			{
				//contrôle du chat et des menus
				testmaze.keyboard();
				
				//animation des sprites
				testmaze.animation();
				
				//victoire et défaite
				testmaze.winlose();
				
				//si redémarrage du labyrinthe
				if(testmaze.reload)
				{
					//stopper les threads
					for(int i=0; i<testmaze.nbMouses; i++)
					{
						if(mouses[i].isAlive())
							mouses[i].stop();
					}
					
					//recréer le labyrinthe
					testmaze = new Maze(testmaze.filepath);
					
					//récupérer le temps associé
					p = Pattern.compile("\\- \\d+");
					m = p.matcher(testmaze.filepath);
					m.find();
					p = Pattern.compile("\\d+");
					m = p.matcher(testmaze.filepath);
					m.find();
					testmaze.time = Integer.parseInt(m.group());
					
					//réinitialiser le nombre de souris
					nb = testmaze.nbMouses;
					
					//réinitialiser le tableau des souris
					mouses = new Mouse[nb];
					
					//réinitialiser et relancer les souris
					for(int i=0; i<testmaze.nbMouses; i++)
					{
						mouses[i] = new Mouse(testmaze, testmaze.mouses[i]);
						mouses[i].start();
					}
					
					//fin de redémarrage
					testmaze.reload = false;
				}
			}
			
			//stopper les threads à la sortie du jeu
			for(int i=0; i<testmaze.nbMouses; i++)
			{
				if(mouses[i].isAlive())
					mouses[i].stop();
			}
		}
		
		//sinon on ferme le jeu
		else choosenfile.cancelSelection();
	}
}
