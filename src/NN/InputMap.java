package NN;

import engine.GameActor;
import engine.GameObject;
import engine.ObjectManager;

public class InputMap
{
    private double[][] Map = new double[20][15];
    //size for display
    private int MapSize = 4;

    public double[][] getMap() {
        return Map;
    }

    public InputMap(GameActor Player){
        for(int i = 0; i < 20; i ++){
            for(int j = 0; j < 15; j ++){
                Map[i][j] = -4;
            }
        }
        Map[(int)Math.floor(Player.getLocation().getX()/16)][(int)Math.floor(Player.getLocation().getY())/16] = 1;
        for(int i = 1; i < ObjectManager.Actors.size(); i ++){
            Map[(int)Math.floor(ObjectManager.Actors.get(i).getX())/16][(int)Math.floor(ObjectManager.Actors.get(i).getY())/16] = .5;
        }

    }
    public void UpdateInput(GameActor Player){
        for(int i = 0; i < 20; i ++){
            for(int j = 0; j < 15; j ++){
                Map[i][j] = -4;
            }
        }
        Map[(int)Math.floor(Player.getLocation().getX()/16)][(int)Math.floor(Player.getLocation().getY())/16] = 1;
        for(int i = 1; i < ObjectManager.Actors.size(); i ++){
            if(!(ObjectManager.Actors.get(i).getX() >= 319) || !(ObjectManager.Actors.get(i).getY() >= 239)) {
                try {
                    Map[(int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16][(int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16) - 1][(int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16) +1][(int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16)][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) - 1] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16)][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) + 1] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16) - 1][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) - 1] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16)+1][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) - 1] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16)-1][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) + 1] = .5;
                    Map[((int) Math.floor(ObjectManager.Actors.get(i).getX()) / 16) + 1][((int) Math.floor(ObjectManager.Actors.get(i).getY()) / 16) + 1] = .5;
                }catch(Exception e){
                }

            }
        }
        //Map[0][0] = 1.1;
    }
    public int getMapSize(){
        return MapSize;
    }

}
