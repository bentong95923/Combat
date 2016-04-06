package main.game;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader {

	private BufferedImage imgToLoad;
	
	public BufferedImage loadingImage(String path) {
		
		try {
			imgToLoad = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgToLoad;
	}
}
