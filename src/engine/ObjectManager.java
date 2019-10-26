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
public class ObjectManager
{
    public static ArrayList<GameObject> Objects = new ArrayList<>();
    public static ArrayList<GameActor> Actors = new ArrayList<>();
    
    public static int checkHitbox(GameObject a, GameObject b)
    {
         
        int collisions = 0;
        for (int i = 0; i < a.getHitbox().size(); i++) 
        {
            for (int j = 0; j < b.getImage().getP().length; j++) 
            {
                if((int)(a.getHitbox().get(i).getX() + a.getX()) == (int)(j%b.getImage().getW() + b.getX())&&(int)( a.getHitbox().get(i).getY() + a.getY()) == (int)(j/b.getImage().getW() + b.getY()) && b.getImage().getP()[j] != 0xffff00ff)
                {
                    collisions++;
                }
            }    
        }
        return collisions;
    }
    public static int CheckCollisions(GameObject a)
    {
        int collisions = 0;
        for (int j = 1; j < Objects.size(); j++) 
        {
        collisions += checkHitbox(a, Objects.get(j));
        }
        if(collisions > 0)
        System.out.println(a + ", " + collisions);
        return collisions;
    }
    public static void UpdatePlayerFocusedMotion(int mx, int my)
    {
        for (int i = 1; i < Objects.size(); i++) 
        {
            Objects.get(i).setX((int)Objects.get(i).getX() + mx);
            Objects.get(i).setY((int)Objects.get(i).getY() + my);
        }
    }
}

