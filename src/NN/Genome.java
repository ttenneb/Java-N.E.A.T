package NN;


import javax.print.attribute.standard.PrinterMakeAndModel;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays.*;
public class Genome {

    public static double MutationRate = .2;
    static Random random = new Random();

    private ArrayList<Neuron> Nodes = new ArrayList<>();
    private ArrayList<Connection> Connections = new ArrayList<>();
    private double fitness = -1;
    public double[] outputTrueValue = new double[4];
    public Genome(){
        Nodes = new ArrayList<>(EvolutionManager.defualtNodes);
        /*int x = random.nextInt(4);
        Connections.add(new Connection(EvolutionManager.globalNodes.get(168), EvolutionManager.globalNodes.get(300 + x), 1, this));*/
    }
    public Genome(Genome g){
        this.Nodes = new ArrayList<>(g.Nodes);
        this.Connections = new ArrayList<>(g.Connections);
        this.fitness = g.fitness;
    }
    public Genome(Genome genome1, Genome genome2){
        Genome offspring = new Genome();
        Genome fitter;
        Genome lessfit;
        if(genome1.getFitness() >= genome2.getFitness()){
            fitter = genome1;
            lessfit = genome2;
        }else{
            fitter = genome2;
            lessfit = genome1;
        }
        for (int i = 0; i < fitter.getConnections().size(); i++){
            int chance = random.nextInt(10);
            boolean match = false;
            for (int j = 0; j < lessfit.getConnections().size(); j++){
                if(fitter.getConnections().get(i).getInnovation() == lessfit.getConnections().get(j).getInnovation()){
                    match = true;
                    if(chance >= 5){
                        offspring.getConnections().add(fitter.getConnections().get(i));
                    }else{
                        offspring.getConnections().add(lessfit.getConnections().get(j));
                    }
                    break;
                }
            }
            if(match == false){
                offspring.getConnections().add(fitter.getConnections().get(i));
            }
        }
        for (int i = 304; i < fitter.getNodes().size(); i++){
            offspring.getNodes().add(fitter.getNodes().get(i));
        }
        for (int i = 0; i < lessfit.getConnections().size(); i ++){
            if(java.util.Arrays.asList(offspring.getConnections()).indexOf(lessfit.getConnections().get(i)) == -1){
                boolean in =false , out = false;
                for (int j = 0; j < offspring.getNodes().size(); j++){
                    if(offspring.getNodes().get(j).getInnovation() == lessfit.getConnections().get(i).getIn().getInnovation()){
                        in = true;
                    }
                    if(offspring.getNodes().get(j).getInnovation() == lessfit.getConnections().get(i).getOut().getInnovation()){
                        out = true;
                    }
                }
                if(in == true && out == true && random.nextInt(10) == 0){
                    offspring.getConnections().add(lessfit.getConnections().get(i));
                }
            }
        }

        this.getConnections().addAll(offspring.getConnections());
        this.getNodes().addAll(offspring.getNodes());
    }
    public void Mutate(){
            int mutation = random.nextInt(4) + 1;

            if(mutation == 1 && Connections.size() >= 1){
                Connections.get(random.nextInt(Connections.size())).shiftWeight();
            }else if(mutation == 2 && Connections.size() >= 1){
                int i;
                i = random.nextInt(Connections.size());
                Nodes.add(new Neuron("Hidden", Connections.get(i), this));

            }else if(mutation == 3) {
                int i = random.nextInt(299), j;
                if ((random.nextInt(2) == 0)&&(Nodes.size() >= 305)) {
                    i = random.nextInt(Nodes.size() - 304) + 304;
                }
                j = random.nextInt(Nodes.size() - 300) + 300;
                if(Nodes.get(j).getType().equals("Input")){
                    //System.out.println("Node, " + j + "/" + Nodes.size() + " Connection, " + Connections.size());
                }
                this.getConnections().add(new Connection(Nodes.get(i), Nodes.get(j), 1, this));
            }else if(mutation >= 4){
                if(this.getConnections().size() > 0) {
                    int i = random.nextInt(this.getConnections().size());

                    if (this.getConnections().get(i).getIn().getType().equals("Input") && this.getConnections().get(i).getOut().getType().equals("Output")) {
                       // System.out.println("removed guh");
                        this.getConnections().remove(i);
                    }
                }
            }
    }

    public int[] feed(InputMap input){
        for (int i = 0; i < this.getNodes().size(); i++){
            getNodes().get(i).clear();
        }
        int[] outputs = new int[4];
        int c = 0;
        for (int i = 0; i < 15; i++){
            for (int j = 0; j< 20; j++){
                Nodes.get(c).setValue(input.getMap()[j][i]);
                c++;
            }
        }
        calculateNodeInputs();
        for (int i = 0; i < Connections.size(); i++){
            if(Connections.get(i).getIn().getType().equals("Input")){
                Connections.get(i).getOut().addInput(Connections.get(i).getIn().getValue(), Connections.get(i).getWeight());
            }
        }
        int counter = 0;
        while((!Nodes.get(300).isInputed() || !Nodes.get(301).isInputed() || !Nodes.get(302).isInputed() || !Nodes.get(303).isInputed()) && counter < 50) {
            counter++;
           System.out.println(counter);
            for (int i = 0; i < Connections.size(); i++) {
                if (Connections.get(i).getIn().isInputed()) {
                    Connections.get(i).getOut().addInput(Connections.get(i).getIn().getValue(), Connections.get(i).getWeight());
                }
            }
        }
        for(int i = 0; i < 4;i++){
            outputTrueValue[i] = Nodes.get(i+300).getValue();
            if(Nodes.get(i+300).getValue() > EvolutionManager.ActivationRate){
                outputs[i] = 1;
            }
        }
        return outputs;
    }
    public void setNodes(ArrayList<Neuron> N){
        Nodes = N;
    }
    public double getFitness(){
        return fitness;
    }
    private void calculateNodeInputs(){
        for (int i = 0; i < Connections.size(); i++){
            Connections.get(i).getOut().addConnectionCount();
        }
    }

    public ArrayList<Neuron> getNodes() {
        return Nodes;
    }

    public ArrayList<Connection> getConnections() {
        return Connections;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
