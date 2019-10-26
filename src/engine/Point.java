/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author Stephen Garcia
 */
public class Point 
{
    int x, y, rgb;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRgb() {
        return rgb;
    }
    public Point(int x, int y, int rgb)
    {
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
