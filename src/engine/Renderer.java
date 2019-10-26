/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import NN.Genome;

import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Stephen Garcia
 */
public class Renderer 
{
    private int pW, pH;
    private int[] p;
    Random r = new Random();
    public static Font defualt;
    public Renderer(Engine engine) throws IOException
    {
      pW = engine.getWidth();
      pH = engine.getHeight();
      p = ((DataBufferInt)engine.getW().getImage().getRaster().getDataBuffer()).getData();
      defualt = new Font("/font.png");
    }
    
    public void clear()
    {
        for(int i = 0; i < p.length; i++)
        {
            p[i] = 0;
        }
    }
    //edited to restrain rendering to specific cords!!!!!
    public void  setPixel(int x, int y, int color)
    {
        if((x < 0 || x >= pW || y < 0 || y >= pH || color == 0xffff00ff))
        {
            return;
        }

        p[x+y*pW] = color;

    }
    public void drawText(String text, int offx, int offy, int color)
    {
        text = text.toUpperCase();
        int offset = 0;
        for (int i = 0; i < text.length(); i++) 
        {
            int unicode = text.codePointAt(i) - 32;
            
            for (int y = 0; y < defualt.getFontImage().getH(); y++) 
            {
                for (int x = 0; x < defualt.getWidth()[unicode]; x++) 
                {
                    if(defualt.getFontImage().getP()[x + defualt.getOffset()[unicode] +y*defualt.getFontImage().getW()] == 0xffffffff)
                    {
                        setPixel(x + offx + offset, y + offy, color);
                    }
                }
            }
            offset += defualt.getWidth()[unicode];
        }
    }
    public void drawLine(int x1, int y1, int x2, int y2, int color)
    {

        double slope = (double)(y2-y1)/(double)(x2-x1);
        //System.out.println(slope);
        for (int i = 0; i < x2 - x1; i ++ )
        {
            setPixel(x1+i, (int)Math.round(y1+i*slope), color);
        }
    }
    public void drawGenome(Genome g){
        //TODO CHANGE COLOR BASED ON VALUES
        for (int i = 0; i < g.getConnections().size(); i++){
            int x1 = 0,x2 = 0;
            int y1= 0,y2 = 0;
            if(g.getConnections().get(i).getIn().getType().equals("Input")){
                x1 = (g.getNodes().indexOf(g.getConnections().get(i).getIn()) % 20) * 4;
                y1 = (g.getNodes().indexOf(g.getConnections().get(i).getIn()) / 20) * 4 + 240;
            }else if(g.getConnections().get(i).getIn().getType().equals("Hidden")){
                x1 = g.getConnections().get(i).getIn().getX();
                y1 = g.getConnections().get(i).getIn().getY();
            }
            if(g.getConnections().get(i).getOut().getType().equals("Output")){
                x2 = 300;
                y2 = 260+(g.getNodes().indexOf(g.getConnections().get(i).getOut())-300)*10;
            }else if(g.getConnections().get(i).getOut().getType().equals("Hidden")){
                x2 = g.getConnections().get(i).getOut().getX();
                y2 = g.getConnections().get(i).getOut().getY();
            }
            drawLine(x1,y1,x2,y2, 0xff0000);
        }
        for (int i = 300; i < g.getNodes().size(); i++){
            if(g.getNodes().get(i).getType().equals("Output")){
                drawImage(300, 260 + (i-300)*10, new Image(4,4,0x00ff00), true);
            }else if(g.getNodes().get(i).getType().equals("Hidden")){
                drawImage(g.getNodes().get(i).getX(), g.getNodes().get(i).getY(), new Image(4, 4, 0x0000ff), true);
            }
        }
    }
    public void drawImage(int x, int y, Image image, boolean hud)
    {
        if(x <= -image.getW()){return;}
        if(y <= -image.getH()){return;}

        if(x >= pW){return;}
        if(y >= pH){return;}

        int newx = 0, newy = 0;
        int newW = image.getW(), newH = image.getH();

        if(x < 0){newx -= x;}
        if(y < 0){newy -= y;}

        if(newW+x > pW){newW -= newW + x - pW;}
        if(newH+y > pH){newH -= newH + y - pH;}

        for (int i = newy; i < newH; i++)
        {
            for (int j = newx; j < newW; j++)
            {
                if ((x+j < 320 && y+i < 240) || hud == true) {
                    setPixel(x + j, y + i, image.getP()[j + i * image.getW()]);
                }
            }
        }
    }
    public void drawImage(int x, int y, Image image)
    {
        if(x <= -image.getW()){return;}
        if(y <= -image.getH()){return;}

        if(x >= pW){return;}
        if(y >= pH){return;}

        int newx = 0, newy = 0;
        int newW = image.getW(), newH = image.getH();

        if(x < 0){newx -= x;}
        if(y < 0){newy -= y;}

        if(newW+x > pW){newW -= newW + x - pW;}
        if(newH+y > pH){newH -= newH + y - pH;}

        for (int i = newy; i < newH; i++)
        {
            for (int j = newx; j < newW; j++)
            {
                if (x+j < 320 && y+i < 240){
                    setPixel(x+j, y+i, image.getP()[j + i*image.getW()]);
                }
            }
        }
    }
}
