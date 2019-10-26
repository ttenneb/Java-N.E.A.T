/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.util.ArrayList;

/**
 *
 * @author Stephen Garcia
 */
public class GameObject 
{
    public Vector Location;
    public Image image;
    public int collisions = 0;
    public ArrayList<Point> Hitbox = new ArrayList();
    public boolean display;
    
    public boolean isDisplay() {
        return display;
    }
    
    public void setDisplay(boolean display) {
        this.display = display;
    }
    public int getCollide() {
        return collisions;
    }
    public void generateHitbox()
    {
        for (int i = 0; i < image.getW(); i++) 
        {
            for (int j = 0; j < image.getH(); j++) 
            {
                  if(image.getP()[i+j*image.getW()] == 0xffff00ff)
                {
                     Hitbox.add(new Point(i,j));
                }
            } 
        }    
       /* for (int i = 1; i < image.getW()-1; i++) 
        {
            for (int j = 1; j < image.getH()-1; j++) 
            {
                if(image.getP()[i+j*image.getW()] != 0xffff00ff)
                {
                    if(image.getP()[(i-1)+j*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    else if(image.getP()[(i+1)+j*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    else if(image.getP()[i+(j-1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    else if(image.getP()[(i)+(j+1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    else if(image.getP()[(i+1)+(j+1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    
                    else if(image.getP()[(i-1)+(j+1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                    
                    else if(image.getP()[(i+1)+(j-1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    
                    }
                    else if(image.getP()[(i-1)+(j-1)*image.getW()] == 0xffff00ff)
                    {
                        Hitbox.add(new Point(i,j));
                    }
                }
            }
        }*/
    }

    public ArrayList<Point> getHitbox() {
        return Hitbox;
    }
    public GameObject()
    {
        Location = new Vector();
    }
    public GameObject(double x, double y, ArrayList<Point> Hitbox)
    {
        Location = new Vector(x,y);
        this.Hitbox = Hitbox;
    }

    public double getX() {
        return Location.getX();
    }

    public void setX(double x) {
        this.Location.setX((int)x);
    }

    public double getY() {
        return Location.getY();
    }

    public void setY(double y) {
        this.Location.setY((int)y);
    }

    public Vector getLocation() {
        return Location;
    }

    public void setLocation(Vector Location) {
        this.Location = Location;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
}
