package themazecat;

public class Entity{

	//coordonnées de l'entité
	int x, y;
	
	//type de l'entité
	String type;
	
	//sprite de l'entité
	Mazesprite entity;
	
	//Constructeur
	public Entity(int x0, int y0, String type0) {
		
		//initialisation des coordonnées et du type
		x = x0;
		y = y0;
		type = type0;
		
		
		//initialisation du sprite
		if(type == "CAT") entity = new Mazesprite("img/cat.png");
		if(type == "MOUSE") entity = new Mazesprite("img/mouse.png");
	}

	//Déplacements
	public void move(String vector)
	{
		//modifier les coordonnées de l'entité
		if(vector=="UP") y = y-1;
		if(vector=="DOWN") y = y+1;
		if(vector=="LEFT") x = x-1;
		if(vector=="RIGHT") x = x+1;
	}
}
