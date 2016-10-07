package res;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class ResourceLoader 
{
	public static final String MINECRAFTIA = "/res/font/Minecraftia.ttf";
	
	public static final String MENU_ARROW = "/res/img/menu/arrow.png",
							   MENU_CART = "/res/img/menu/cart.png",
							   MENU_SETTINGS = "/res/img/menu/gear.png",
							   MENU_SAVE = "/res/img/menu/save.png",
							   MENU_LOAD = "/res/img/menu/load.png",
							   MENU_SHOP = "/res/img/menu/shop.png";
	
	public static final String SHOP_ARROW = "/res/img/shop/shop arrow.png",
							   SHOP_BG = "/res/img/shop/stone.png";
	
	public static final String PLAYER_STEP = "/res/img/sprite/playerstep.png",
							   PLAYER_STILL = "/res/img/sprite/playerstill.png",
							   FAST_PLAYER_STEP = "/res/img/sprite/fastplayerstep.png",
							   FAST_PLAYER_STILL = "/res/img/sprite/fastplayerstill.png",
							   BIG_PLAYER_STEP = "/res/img/sprite/bigplayerstep.png",
							   BIG_PLAYER_STILL = "/res/img/sprite/bigplayerstill.png",
							   ARCHER_STEP = "/res/img/sprite/archerstep.png",
							   ARCHER_STILL = "/res/img/sprite/archerstill.png";
	
	public static final String ENEMY_STEP = "/res/img/sprite/enemystep.png",
							   ENEMY_STILL = "/res/img/sprite/enemystill.png";
	
	public static final String BACKGROUND = "/res/img/misc/defaultbg.png",
							   BUTTON = "/res/img/misc/tile.png",
							   BUTTON_CLICKED = "/res/img/misc/tileclicked.png",
							   TITLE = "/res/img/misc/title.PNG",
							   CASTLE = "/res/img/misc/tower.png",
							   PLANE = "/res/img/misc/plane.png";
	
	
	public static BufferedImage flipImage(BufferedImage image)
	{
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage flipped = op.filter(image, null);
		
		return flipped;
	}
	
	public static BufferedImage loadImage(String path)
	{
		return new ResourceLoader().getImg(path);
	}
	
	public static Font loadFont(String path)
	{
		return new ResourceLoader().getFont(path);
	}
	
	public BufferedImage getImg(String path)
	{
		try {
			InputStream in = getClass().getResourceAsStream(path);
			
			BufferedImage i = ImageIO.read(in);
			in.close();
			return i;
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Font getFont(String path)
	{
		Font font = null;
		try {
			InputStream in = getClass().getResourceAsStream(path);
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			GraphicsEnvironment gnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			gnv.registerFont(font);
			in.close();
		} catch(IOException e)
		{
			font = new JLabel().getFont();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		return font;
	}
}
