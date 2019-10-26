package engine;


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stephen Garcia
 */
public class Image 
{
    private int w = 0,h = 0;
    private int[] p;
    public BufferedImage image = null;
    public Image(String file) throws IOException
    {
        
        
        image = ImageIO.read(Image.class.getResourceAsStream(file));
        
        w = image.getWidth();
        h = image.getHeight();
        p = image.getRGB(0, 0, w, h, null, 0, w);
        
        image.flush();
    }
    public Image(BufferedImage img) throws IOException
    {
        
        
        image = img;
        
        w = image.getWidth();
        h = image.getHeight();
        p = image.getRGB(0, 0, w, h, null, 0, w);
        
        image.flush();
    }
    public Image(int x, int y, int color)
    {
        w = x+2;
        h = y+2;
        image = new BufferedImage(w+1, h + 1, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < x+1; i++) 
        {
            for (int j = 1; j < y+1; j++) 
            {
                image.setRGB(i, j, color);
            }
        }
       for (int i = 0; i < x+1; i++) 
        {
            image.setRGB(i, 0, 0xffff00ff);
        }
        for (int i = 0; i < y+1; i++) 
        {
            image.setRGB(0, i, 0xffff00ff);
        }
        for (int i = 0; i < y+1; i++) 
        {
            image.setRGB(x+1, i, 0xffff00ff);
        }
        for (int i = 0; i < x+2; i++) 
        {
            image.setRGB(i, y+1, 0xffff00ff);
        }
        
        p = image.getRGB(0, 0, w, h, null, 0, w);
        image.flush();
        
        
    }
    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int[] getP() {
        return p;
    }
public static BufferedImage rotate( BufferedImage img )
{
    int         width  = img.getWidth();
    int         height = img.getHeight();
    BufferedImage   newImage = new BufferedImage( height, width, img.getType() );

    for( int i=0 ; i < width ; i++ )
        for( int j=0 ; j < height ; j++ )
            newImage.setRGB( height-1-j, i, img.getRGB(i,j) );

    return newImage;
}
    
}
