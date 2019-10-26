package NN;

import engine.Vector;

import java.util.Random;

public class Connection {
    private Random random = new Random();
    private Neuron in;
    private Neuron out;
    private boolean enabled;
    private double weight;
    private int innovation = -1;
    private int split = -1;
    private Vector displayMidpoint;

    private final double weightShift = .05;
    public Connection(Connection c, double w){
        this.in = c.in;
        this.out = c.out;
        this.weight = w;
        this.split = c.split;
        this.innovation = c.getInnovation();
        this.displayMidpoint = c.displayMidpoint;
        this.enabled = c.enabled;
    }
    public Connection(Neuron i, Neuron o, double w, Genome g){
        in = i;
        out = o;
        weight = w;
        for (int j = 0; j < EvolutionManager.globalConnections.size(); j++){
            if(i.getInnovation() == EvolutionManager.globalConnections.get(j).getIn().getInnovation() && o.getInnovation() == EvolutionManager.globalConnections.get(j).getOut().getInnovation()){
                this.innovation = EvolutionManager.globalConnections.get(j).getInnovation();
            }
        }
        if(innovation == -1){
            innovation = EvolutionManager.globalConnections.size();
            EvolutionManager.globalConnections.add(this);
        }
        calculateMidpoint(g);
    }
    public Connection(int i, int in, int out, int split, double weight){
        innovation = i;
        //TODO test and make sure innovation number matches with array spot
        this.in = EvolutionManager.globalNodes.get(in);
        this.out = EvolutionManager.globalNodes.get(out);
        this.split = split;
        this.weight = weight;
    }
    public void setWeight(double w){
        weight = w;
    }
    public void shiftWeight(){
        int chance = random.nextInt(3);
        if(chance == 0){
            weight -= weightShift;
        }else{
            weight += weightShift;
        }

    }

    public Neuron getIn() {
        return in;
    }

    public Neuron getOut() {
        return out;
    }

    public double getWeight() {
        return weight;
    }
    public Vector getMidpoint(){
        return displayMidpoint;
    }
    public void calculateMidpoint(Genome g){
        int x1,x2;
        int y1,y2;
        if(in.getType().equals("Input")){
            x1 = (g.getNodes().indexOf(in) % 20) * 4;
            y1 = (g.getNodes().indexOf(in) / 20) * 4 + 240;
        }else{
            x1 = in.getX();
            y1 = in.getY();
        }
        if(out.getType().equals("Output")){
            x2 = 300;
            y2 = 260+(g.getNodes().indexOf(out)-300)*10;
        }else{
            x2 = out.getX();
            y2 = out.getY();
        }
        displayMidpoint =  new Vector((x1+x2)/2, (y1+y2)/2);
    }

    public int getInnovation() {
        return innovation;
    }

    public int getSplit() {
        return split;
    }

    public void setSplit(int split) {
        this.split = split;
    }
}
