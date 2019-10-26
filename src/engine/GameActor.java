/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author bgarc
 */
public class GameActor extends GameObject
{
    public Vector Acceleration;
    public Vector Velocity;
    public GameActor()
    {
        super();
        Acceleration = new Vector();
        Velocity = new Vector();
    }

    public Vector getAcceleration() {
        return Acceleration;
    }

    public void setAcceleration(Vector Acceleration) {
        this.Acceleration = Acceleration;
    }

    public Vector getVelocity() {
        return Velocity;
    }

    public void setVelocity(Vector Velocity) {
        this.Velocity = Velocity;
    }
    public void update()
    {
        this.Velocity.add(Acceleration);
        this.Location.add(Velocity);
        
        
    }
}
