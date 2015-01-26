package themazecat;

import java.io.IOException;

import java.nio.file.Paths;

import org.jsfml.graphics.*;

public class Mazesprite extends Sprite{

	private Texture objImage;
	
	public Mazesprite(String ImageFileName) {
		
		try{
			
			objImage = new Texture();
			
			objImage.loadFromFile(Paths.get(ImageFileName));
			
			this.setTexture(objImage);
			
			this.setOrigin(this.getGlobalBounds().width/2, this.getGlobalBounds().height/2);
		}
		
		catch(IOException e){
			e.printStackTrace();
		}
		
	}

}
