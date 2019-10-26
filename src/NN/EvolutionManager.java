package NN;


import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class EvolutionManager {
    public static ArrayList<ArrayList<Genome>> Genomes = new ArrayList<>();
    public static ArrayList<Connection> globalConnections = new ArrayList<>();
    public static ArrayList<Neuron> globalNodes = new ArrayList<>();
    public static ArrayList<Neuron> defualtNodes = new ArrayList<>();
    public static ArrayList<Genome> Parents = new ArrayList<>();
    public static File file;
    public static Random random = new Random();
    public static int Generation = 0;
    public static int GenomeCount = 0;
    public static double distanceCoefficient = .1;
    private static double distanceThreshold = .75;
    public static double ActivationRate = .6;
    private static int InitialMutationRate = 4;
    //private static int MutationRate = 2;
    public static double avgDistance = 0;
    public static int CurrentSpecies = 0;
    private static int speciesSave = 2;
    public static int ConnectionMutationBias = 1;
    private static double c1=1.5,c2=1.5,c3=0,c4=1;
    private static int MaxBias = 0;
    private static int Size;

    public static int zerocount;
    public static void Start(int populationSize){
        Size = populationSize;
        Genomes.add(new ArrayList<>());
        for (int i = 0; i < populationSize; i++){
            Genomes.get(0).add(new Genome());
            for (int j = 0; j < InitialMutationRate; j++){
                Genomes.get(0).get(i).Mutate();
            }
        }
    }
    public static Genome Next(){
        for (int j = 0; j < Genomes.size(); j ++) {
            CurrentSpecies  = j;
            for (int i = 0; i < Genomes.get(j).size(); i++) {
                if (Genomes.get(j).get(i).getFitness() == -1) {
                    GenomeCount++;
                    return Genomes.get(j).get(i);
                }
            }
        }
        GenomeCount = 0;
        Kill();
        return null;
    }
    public static void Save(String name) throws java.io.IOException{
        file = new File(name + ".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Nodes:"+(globalNodes.size()-304));
        bw.newLine();
        for (int i = 304; i < globalNodes.size(); i++){
            bw.write(globalNodes.get(i).getInnovation() + "," + globalNodes.get(i).getType()+","+globalNodes.get(i).getX()+","+globalNodes.get(i).getY());
            bw.newLine();
        }
        bw.write("Connections:"+globalConnections.size());
        bw.newLine();
        for (int i = 0; i < globalConnections.size(); i++){
            bw.write(globalConnections.get(i).getInnovation() +","+globalConnections.get(i).getIn().getInnovation() +"," + globalConnections.get(i).getOut().getInnovation()+","+globalConnections.get(i).getSplit()+","+globalConnections.get(i).getWeight());
            bw.newLine();
        }
        for (int i = 0; i < Genomes.size(); i++){
            for (int n = 0; n <Genomes.get(i).size(); n++){
                bw.write("Genome: " + Genomes.get(i).get(n).getFitness());
                bw.newLine();
                //TODO handle speciation and storing it in files
                for(int j =304; j< Genomes.get(i).get(n).getNodes().size(); j++){
                    bw.write(Genomes.get(i).get(n).getNodes().get(j).getInnovation() +",");
                }
                bw.newLine();
                for (int j=0; j< Genomes.get(i).get(n).getConnections().size(); j++){
                    bw.write(Genomes.get(i).get(n).getConnections().get(j).getInnovation() + "/" + Genomes.get(i).get(n).getConnections().get(j).getWeight()+",");
                }
                bw.newLine();
            }
        }
        bw.write("end");
        bw.flush();
        bw.close();
    }
    public static void Read(String name) throws java.io.IOException {
        Genomes.add(new ArrayList<>());
        file = new File(name);
        String verify;
        String[] data;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        verify = br.readLine();
        if(verify.substring(0,1).equals("N")){
            for (int i = 0; i< Integer.parseInt(verify.substring(6)); i++){
                data = br.readLine().split(",");
                globalNodes.add(new Neuron(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]),Integer.parseInt(data[3])));
            }
        }
        verify = br.readLine();
        if(verify.substring(0,1).equals("C"))
        {
            for (int i = 0; i < Integer.parseInt(verify.substring(12)); i++){
                data = br.readLine().split(",");
                globalConnections.add(new Connection(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), Double.parseDouble(data[4])));
            }
        }
        while((verify = br.readLine()).equals("Genome:")){
            Genome g = new Genome();
            data = br.readLine().split(",");
            for (int i = 0; i < data.length; i++){
                //TODO this might have to be change refrecning global nodes myb i should create new object?
                g.getNodes().add(globalNodes.get(Integer.parseInt(data[i])));
            }
            data = br.readLine().split(",");
            for (int i = 0; i<data.length;i++){
                String[] sdata = data[i].split("/");
                g.getConnections().add(new Connection(globalConnections.get(Integer.parseInt(sdata[0])), Double.parseDouble(sdata[1])));
            }
            Genomes.get(0).add(g);
        }

    }
    public static void speciate(){
        int count = 0;
        zerocount = 0;
        Genomes.add(new ArrayList<>());
        Genomes.get(1).add(Genomes.get(0).get(0));
        Genomes.get(0).remove(0);
        for(int i = 0; i < Size - 1; i++){
            if(Genomes.get(0).get(i).getConnections().size() == 0){
                zerocount++;
            }
            int placed = 0;
            double lowestDistance = -1;
            for (int j = 1; j < Genomes.size(); j++){
                double distance = distance(Genomes.get(j).get(0), Genomes.get(0).get(i));
                if(distance != 0){
                    count++;
                }
                avgDistance += distance;
                if(distanceThreshold < distance){
                    if (lowestDistance == -1 || lowestDistance > distance(Genomes.get(j).get(0), Genomes.get(0).get(i))){
                        placed = j;
                        lowestDistance = distance(Genomes.get(j).get(0), Genomes.get(0).get(i));
                    }
                }
            }
            if(placed == 0){
                Genomes.add(new ArrayList<>());
                Genomes.get(Genomes.size()-1).add(Genomes.get(0).get(i));
            }else{
                Genomes.get(placed).add(Genomes.get(0).get(i));
            }
        }
        avgDistance = avgDistance/count;
        Genomes.remove(0);

    }
    public static void Kill(){
       Genome Max = null;
       int ss;
           for (int i = 0; i < Genomes.size(); i++) {
               ss = Genomes.get(i).size()/4;
               if(ss == 0){
                   ss= 1;
               }
               Genome[] max = new Genome[ss];

               for (int n = 0; n < max.length; n++) {
                   for (int j = 0; j < Genomes.get(i).size(); j++){
                       if(max[n] == null){
                          max[n] = Genomes.get(i).get(j);
                       }else if (max[n].getFitness() < Genomes.get(i).get(j).getFitness() && java.util.Arrays.asList(max).indexOf(Genomes.get(i).get(j)) == -1){
                            max[n] = Genomes.get(i).get(j);
                       }
                   }
               }
               for (int j = 0; j < ss; j++) {
                   Parents.add(new Genome(max[j]));
               }
           }

        Genomes.clear();
        Reproduce();
    }
    public static void Reproduce(){
        //TODO parent array has 0
        Genomes.add(new ArrayList<>());
       // System.out.println(Parents.size());
            while (Genomes.get(0).size() < Size) {
                int x = random.nextInt(Parents.size());
                int y = random.nextInt(Parents.size());
                Genomes.get(0).add(new Genome(Parents.get(x), Parents.get(y)));
            }
        Parents.clear();
        Mutate();
    }
    public static void Mutate(){
        for (int i = 0; i < Genomes.get(0).size(); i++){
            int x = 0;//random.nextInt(2);
            if(x == 0){
                Genomes.get(0).get(i).Mutate();
            }
        }
        speciate();
    }
    public static double distance(Genome genome1, Genome genome2){
        Genome Larger;
        Genome Smaller;
        if(genome1.getConnections().size()>= genome2.getConnections().size()){
            Larger = genome1;
            Smaller = genome2;
        }else{
            Larger = genome2;
            Smaller = genome1;
        }
        int matches = 0;
        for (int i = 0; i < Larger.getConnections().size(); i++){
            for (int j = 0; j < Smaller.getConnections().size(); j++){
                if(Larger.getConnections().get(i).getInnovation() == Smaller.getConnections().get(j).getInnovation()){
                    matches++;
                    break;
                }
            }
        }
        if(Larger.getConnections().size() == 0){
            return 0;
        }
        return (double)matches/(double)Larger.getConnections().size();
    }
    public static double[] findDisjointedandExcess(Genome genome1, Genome genome2){
        double[] DE = new double[3];
        int counter = 0;
        for (int i = 0; i < genome1.getNodes().size(); i++){
            if(genome2.getNodes().size() <= i){
                DE[1]++;
            }else{
                boolean found = false;
                for (int j = 0; j < genome2.getNodes().size(); j++){
                    if(genome1.getNodes().get(i).getInnovation() == genome2.getNodes().get(j).getInnovation()){
                        found = true;
                    }
                }
                if(found == false){
                    DE[0]++;
                }
            }
        }
        for (int i = 0; i < genome1.getConnections().size(); i++){
            if(genome2.getConnections().size() <= i){
                DE[1]++;
            }else{
                boolean found = false;
                for (int j = 0; j < genome2.getConnections().size(); j++){
                    if(genome1.getConnections().get(i).getInnovation() == genome2.getConnections().get(j).getInnovation()){
                        found = true;
                        counter++;
                        DE[2] += Math.abs(genome1.getConnections().get(i).getWeight() - genome2.getConnections().get(j).getWeight());
                    }
                    if(found == false){
                        DE[0]++;
                    }
                }
            }
        }
        if(counter > 0)
            DE[2] = DE[2]/counter;
        return DE;
    }
    public static void deleteSave(){
        file.delete();
    }

}
