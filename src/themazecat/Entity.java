package themazecat;

public class Entity{

	int x, y;
	
	String type;
	
	Mazesprite entity;
	
	public Entity(int x0, int y0, String type0) {
		
		x = x0;
		y = y0;
		type = type0;
		
		if(type == "CAT") entity = new Mazesprite("img/cat.png");
		
		if(type == "MOUSE") entity = new Mazesprite("img/mouse.png");
	}

	public void move(String vector)
	{
		if(vector=="UP") y = y-1;
		if(vector=="DOWN") y = y+1;
		if(vector=="LEFT") x = x-1;
		if(vector=="RIGHT") x = x+1;
	}
}
