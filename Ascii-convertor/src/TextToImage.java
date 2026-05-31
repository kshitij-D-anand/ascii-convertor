import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class TextToImage {
	
	private Scanner sc;
	private BufferedImage img;
	private Graphics2D graphics2d;
	private Font font;
	private FontMetrics fm;
	private Color fontColor, bgColor;
	private List<String> lines;
	private char[][] characters;
	
	public TextToImage(File input){
		initialize(input);
	}
	
	private void initialize(File input) {
		try {
			sc = new Scanner(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		graphics2d = img.createGraphics();
		font = new Font("consolas", Font.PLAIN, 1);
		lines = new ArrayList<>();
		fm = graphics2d.getFontMetrics(font);
		do {
			lines.add(sc.nextLine());
		} while(sc.hasNextLine());
		
		characters = new char[lines.size()][];
		for(int i = 0; i < lines.size();i++) {
			characters[i] = lines.get(i).toCharArray();
		}
	}
	
	public void convertToImage(File output) {
		int width = characters[0].length * font.getSize();
		int height = characters.length * font.getSize();
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		graphics2d = img.createGraphics();
		graphics2d.setFont(font);
		graphics2d.setColor(bgColor);
		graphics2d.fillRect(0, 0, width, height);
		graphics2d.setColor(fontColor);
		
		setRenderingHints();
		
		for(int i = 0;i < characters.length;i++) {
			for(int j = 0;j < characters[i].length;j++) {
				graphics2d.drawString(Character.toString( characters[i][j]), j*font.getSize(), fm.getAscent() + font.getSize()*i);
			}
		}
		
		try{
			ImageIO.write(img, "png", output);
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		graphics2d.dispose();
		sc.close();
	}

	private void setRenderingHints() {
		graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		graphics2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
	}
	
	public void setSize(int size) {
		font = new Font(font.getName(), Font.PLAIN, size);
	}
	
	public void setStyle(String style) {
		font = new Font(style, Font.PLAIN, font.getSize());
	}
	
	public void setColor(Color color) {
		fontColor = color;
	}
	
	public void setBackground(Color color) {
		bgColor = color;
	}
}
