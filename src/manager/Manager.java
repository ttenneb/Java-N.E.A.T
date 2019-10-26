/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import NN.*;
import java.awt.event.KeyEvent;
import engine.*;

import java.io.IOException;
import java.util.Random;
/**
 *
 * @author bennett
 */
public class Manager extends Game {

     public static Engine engine;

     public static GameActor Player;

     public static Random random;

     public int score = 0;
     public int max = 0;
     public int distance = 0;
     public int counter = 0;
     public static boolean running = false;
     public static boolean showbest = false;
     public static InputMap input;
     //used to store all unique nodes and connections across all genomes
     public static int testCounter = 1;
     public static Genome test;
     public static Genome best;
     public static Vector[] emitters;
     private int spawncounter = 0;
     private boolean spacedown = false;

     @Override
     public void update(Input i) {
          if (running == true) {
               counter++;
               if (i.isKey(KeyEvent.VK_UP)) {
                    Player.Location.setY(Player.getLocation().getY() - 1);
               }
               if (i.isKey(KeyEvent.VK_DOWN)) {
                    Player.Location.setY(Player.getLocation().getY() + 1);
               }
               if (i.isKey(KeyEvent.VK_RIGHT)) {
                    Player.Location.setX(Player.getLocation().getX() + 1);
               }
               if (i.isKey(KeyEvent.VK_LEFT)) {
                    Player.Location.setX(Player.getLocation().getX() - 1);
               }
               if(i.isKey(KeyEvent.VK_Z)){
                    showbest = true;
               }if(i.isKey(KeyEvent.VK_X)){
                    showbest = false;
               }
               if(i.isKey(KeyEvent.VK_1)){
                    engine.UPDATE = 1/60.0;
               }else if(i.isKey(KeyEvent.VK_2)){
                    engine.UPDATE = 1/120.0;
               }else if(i.isKey(KeyEvent.VK_3)){
                    engine.UPDATE = 1/180.0;
               }

               if(counter % 45 == 0){
                    GameActor Projectile = new GameActor();
                    Vector ProjectileVelocity = new Vector();
                    Projectile.setImage(new Image(16, 16, 0xff0000));
                    Projectile.setDisplay(true);
                    Projectile.generateHitbox();
                    Projectile.setLocation(SpawnGen());
                    ProjectileVelocity = Vector.subtract(Player.getLocation(), Projectile.getLocation());
                    ProjectileVelocity.Normalize();
                    ProjectileVelocity.multiply(2);
                    Projectile.setVelocity(ProjectileVelocity);
                    ObjectManager.Objects.add(Projectile);
                    ObjectManager.Actors.add(Projectile);
                    score += 10;
               }
               for (int j = 0; j < ObjectManager.Actors.size(); j++){
                    if(0 < ObjectManager.checkHitbox(Player, ObjectManager.Actors.get(j)))  {
                         running = false;
                         ObjectManager.Objects.clear();
                         ObjectManager.Actors.clear();
                         counter = 0;
                    }
               }
               //TODO STOP MOTION WHEN BOUNDS ARE MET
               input.UpdateInput(Player);
          }
          if(test == null){
               System.out.println("guh");
          }
          int[] outputs = test.feed(input);
          if (outputs[0] == 1 && !(Player.getY() <= 10)) {
               Player.Location.setY(Player.getLocation().getY() - 1);
               distance++;
          }
          if (outputs[1] == 1 && !(Player.getY() >= 220)) {
               Player.Location.setY(Player.getLocation().getY() + 1);
               distance++;
          }
          if (outputs[2] == 1 && !(Player.getX() >= 300)) {
               Player.Location.setX(Player.getLocation().getX() + 1);
               distance++;
          }
          if (outputs[3] == 1&& !(Player.getX() <= 5)) {
               Player.Location.setX(Player.getLocation().getX() - 1);
               distance++;
          }
          for (int j = 0; j<outputs.length; j++ ){
          }
     }

     @Override
     public void renderer(Renderer r) {
          r.clear();
          int count = 0;
          for (int i =0; i < EvolutionManager.Genomes.size(); i++){
               count+=EvolutionManager.Genomes.get(i).size();

          }
          r.drawText("Score: " + score, engine.getWidth() - 120, 0, 0x00ff00);
          r.drawText("Generation: " + EvolutionManager.Generation, engine.getWidth() - 120, 10, 0x00ff00);
          r.drawText("Genome: " + EvolutionManager.GenomeCount + "/"+count, engine.getWidth() - 120, 20, 0x00ff00);
          r.drawText("Max: " + max, engine.getWidth() - 120, 30, 0x00ff00);
          r.drawText("Species: " + (EvolutionManager.CurrentSpecies + 1) +"/"+ EvolutionManager.Genomes.size(), engine.getWidth() - 120, 40, 0x00ff00);
          r.drawText("Av. Dist.: " + (EvolutionManager.avgDistance), engine.getWidth() - 120, 50, 0x00ff00);
          r.drawText("Count: " + testCounter, engine.getWidth() - 120, 60, 0x00ff00);
          r.drawText("Prnt Size: " + EvolutionManager.Parents.size(), engine.getWidth() - 120, 70, 0x00ff00);
          r.drawText("Sp 1 Size: " + EvolutionManager.Genomes.get(0).size(), engine.getWidth() - 120, 80, 0x00ff00);
          r.drawText("Sp L Size: " + EvolutionManager.Genomes.get(EvolutionManager.Genomes.size()-1).size(), engine.getWidth() - 120, 90, 0x00ff00);
          r.drawText("Otpt. Vls: " + Double.toString(test.outputTrueValue[0]).substring(0, 3) + "," + Double.toString(test.outputTrueValue[1]).substring(0, 3) + ","+ Double.toString(test.outputTrueValue[2]).substring(0, 3) + ","+ Double.toString(test.outputTrueValue[3]).substring(0, 3), engine.getWidth() - 120, 100, 0x00ff00);

         for (int j = 0; j < ObjectManager.Objects.size(); j++) {
               if (ObjectManager.Objects.get(j).isDisplay()) {
                    r.drawImage((int) ObjectManager.Objects.get(j).getX(), (int) ObjectManager.Objects.get(j).getY(), ObjectManager.Objects.get(j).getImage());
               }
          }
          for (int i = 0; i < ObjectManager.Actors.size(); i++){
               if((ObjectManager.Actors.get(i).getX() > engine.getWidth() + 5 || ObjectManager.Actors.get(i).getX() < -20) || (ObjectManager.Actors.get(i).getY() > engine.getHeight() || ObjectManager.Actors.get(i).getY() < -20)){
                    ObjectManager.Actors.remove(i);
               }
               ObjectManager.Actors.get(i).update();
          }
          for (int i = 0; i < input.getMap().length; i++){
               for (int j = 0; j < input.getMap()[0].length; j ++){
                    if(input.getMap()[i][j] == 1){
                         r.drawImage(i*input.getMapSize(), j*input.getMapSize() + 240, new Image(input.getMapSize(),input.getMapSize(),0x0000ff), true);
                    }
                    else if(input.getMap()[i][j] == -4){
                         r.drawImage(i*input.getMapSize(), j*input.getMapSize() + 240, new Image(input.getMapSize(),input.getMapSize(), 0x00ff00/*0xffff00ff*/), true);
                    }
                    else if(input.getMap()[i][j] == .5){
                         r.drawImage(i*input.getMapSize(), j*input.getMapSize() + 240, new Image(input.getMapSize(),input.getMapSize(),0xff0000), true);
                    }else if(input.getMap()[i][j] == 1.1){
                         r.drawImage(i*input.getMapSize(), j*input.getMapSize() + 240, new Image(input.getMapSize(),input.getMapSize(),0xf8ffff), true);
                    }
               }
          }

          r.drawGenome(test);


          if(!running){
               distance = distance/10;
               //System.out.println(distance);
               if(test.getFitness() < (distance + score - 2*test.getConnections().size() - 3*EvolutionManager.Genomes.get(EvolutionManager.CurrentSpecies).size())) {
                    test.setFitness(distance + score - 2*test.getConnections().size() - 3*EvolutionManager.Genomes.get(EvolutionManager.CurrentSpecies).size());
               }
               distance = 0;
               if(test.getConnections().size() == 0 ){
                    test.setFitness(0);
               }
               if(test.getFitness() < 0){
                    test.setFitness(0);
               }
               //System.out.println(test.getFitness());
               if(max < score){
                    max = score;
                    best = test;
                    try{
                         EvolutionManager.Save("test");
                    }catch (Exception e){

                    }

               }
               score = 0;
               spawncounter = 0;
               if(showbest == false) {
                    if (testCounter == 1) {
                         testCounter = 1;
                         if ((test = EvolutionManager.Next()) == null) {

                              int i = EvolutionManager.Generation++;
                              test = EvolutionManager.Next();
                         }
                    } else {
                         testCounter++;
                    }
               }else{
                    test = best;
               }
               ObjectManager.Objects.clear();
               ObjectManager.Actors.clear();
               Player = new GameActor();
               Player.setX(320/2);
               Player.setY(240/2);
               Player.setDisplay(true);
               Player.setImage(new Image(16, 16, 0x0000ff));
               Player.generateHitbox();
               ObjectManager.Objects.add(Player);
               ObjectManager.Actors.add(Player);
               random = new Random();
               running = true;
               if(test.getConnections().size() == 0){
                    test.setFitness(0);
                    running = false;
               }
          }
     }

     public static void main(String[] args) throws IOException {
          engine = new Engine(new Manager());
          Player = new GameActor();
          Player.setX(320/2);
          Player.setY(240/2);
          Player.setDisplay(true);
          Player.setImage(new Image(16, 16, 0x0000ff));
          Player.generateHitbox();
          ObjectManager.Objects.add(Player);
          ObjectManager.Actors.add(Player);
          random = new Random();
          running = true;
          input = new InputMap(Player);
          //creating default topology

          emitters = new Vector[6];
          emitters[0] = new Vector(0,0);
          emitters[1] = new Vector(engine.getWidth()/2, 0);
          emitters[2] = new Vector(engine.getWidth(), 0);
          emitters[3] = new Vector(0, engine.getHeight());
          emitters[4] = new Vector(engine.getWidth()/2, engine.getHeight());
          emitters[5] = new Vector(engine.getWidth(), engine.getHeight());
          for (int i = 0; i < 20; i++){
               for (int j = 0; j < 15; j++){
                    EvolutionManager.defualtNodes.add(new Neuron(input.getMap()[i][j], "Input"));
               }
          }
          for (int i = 0; i < 4; i++){
               EvolutionManager.defualtNodes.add(new Neuron("Output"));
          }
          EvolutionManager.Start(100);
          EvolutionManager.speciate();
          test = EvolutionManager.Next();
          engine.start();
     }
     private Vector SpawnGen(){
          Vector Location = new Vector();
          switch(spawncounter % 6){

               case 0: Location = new Vector(emitters[0]);
                    break;
               case 1: Location =  new Vector(emitters[1]);
                    break;
               case 2: Location =  new Vector(emitters[2]);
                    break;
               case 3: Location =  new Vector(emitters[3]);
                    break;
               case 4: Location =  new Vector(emitters[4]);
                    break;
               case 5: Location =  new Vector(emitters[5]);
                    break;
               default: System.out.println("guh");
                    break;

          }
          spawncounter++;

          return Location;
     }
}
