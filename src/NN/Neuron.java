package NN;

import java.util.ArrayList;

public class Neuron {
    private int innovation;
    private double value;
    private ArrayList<Double> input = new ArrayList<>();
    private double bias = 0;
    private String type;
    private int connectionCounter;
    private int inputs = 0;
    private boolean inputed = false;

    private int displayX;
    private int displayY;
    public Neuron(double value, String type){
        this.type = type;
        this.value = value;
        innovation = EvolutionManager.globalNodes.size();
        EvolutionManager.globalNodes.add(this);
    }
    public Neuron(String type){
        this.type = type;
        innovation = EvolutionManager.globalNodes.size();
        EvolutionManager.globalNodes.add(this);
    }
    public Neuron(int i, String type, int x, int y){
        this.type = type;
        innovation = i;
        displayX = x;
        displayY = y;
    }
    public Neuron(String type, Connection c, Genome g){
        this.type = type;
        ArrayList<Integer> Inputs = new ArrayList<>();
        ArrayList<Integer> Outputs = new ArrayList<>();
        for (int i = 0; i < g.getConnections().size(); i ++){
            //if(g.getConnections().get(i).getOut() )
        }
        if(EvolutionManager.globalConnections.get(c.getInnovation()).getSplit() == -1){
            innovation = EvolutionManager.globalNodes.size();
            c.setSplit(innovation);
            EvolutionManager.globalConnections.get(c.getInnovation()).setSplit(innovation);
            EvolutionManager.globalNodes.add(this);
        }else{
            innovation = EvolutionManager.globalConnections.get(c.getInnovation()).getSplit();
            c.setSplit(innovation);
        }
        g.getConnections().add(new Connection(c.getIn(), this, 1, g));
        g.getConnections().add(new Connection(this, c.getOut(), c.getWeight(), g));

        c.calculateMidpoint(g);

        displayX = (int)c.getMidpoint().getX();
        displayY = (int)c.getMidpoint().getY();

        g.getConnections().remove(c);
    }

    public void calculateValue()
    {
        value = sigmoid(this.input);
        /*(this.getType().equals("Output")){
            value = this.input;
        }else {
            value = sigmoid(this.input - bias);
        }*/
    }
    public void addInput(double i, double weight){
        //System.out.println(input.size());
       input.add(i*weight);
       this.connectionCounter--;
       if(connectionCounter == 0){
           inputed = true;
           calculateValue();
       }
    }

    public static int Output(ArrayList<Connection> Connections){
        return 0;
    }
    private double sigmoid(ArrayList<Double> z) {
        double x =0;
        for (int i = 0; i < z.size(); i++){
            x=+z.get(i);
        }
        double y = 1/( 1 + Math.pow(Math.E,(-1*x)));
        boolean simplfy = true;
        for (int i = 0; i < inputs; i++){
            if(z.get(i) > 0){
                simplfy = false;
            }
        }
        if(y < 0.05 && simplfy == true){
            y = -4;
        }
        //System.out.println(this.getType() + ", " + x + ", " + y);
        return (y);
    }
    public void setValue(double v){
        this.value = v;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
    public void addConnectionCount(){
        connectionCounter++;
        inputs++;
    }
    public boolean isInputed(){
        if(connectionCounter == 0){
            return true;
        }else{
            return inputed;
        }
    }

    public int getX(){
        return displayX;
    }
    public int getY(){
        return displayY;
    }
    public void clear(){
        input.clear();
        value = 0;
        connectionCounter = 0;
        inputed = false;
        inputs = 0;
    }

    public int getConnectionCounter() {
        return connectionCounter;
    }

    public int getInnovation() {
        return innovation;
    }
}
