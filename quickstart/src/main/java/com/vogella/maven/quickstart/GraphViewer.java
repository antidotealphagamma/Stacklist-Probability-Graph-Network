package com.vogella.maven.quickstart;

//import java.math.*;
import java.io.*;
import java.util.*;
//import edu.uci.ics.jung.graph.Graph;
//import edu.uci.ics.jung.graph.SparseMultigraph;

public class ReadCVS {

    //Instance variables
    //private static HashMap<String, Integer> outSize = new HashMap<String, Integer>();
    private static ArrayList<StringBuilder> names = new ArrayList<StringBuilder>();
    private static ArrayList<StringBuilder> probabilities = new ArrayList<StringBuilder>();
    private static Queue<Float> probQueue = new LinkedList<Float>();
    private static Queue<String> nameQueue = new LinkedList<String>();
    private static ArrayList<String> tmpTool = new ArrayList<String>();
    private static ArrayList<String> tmpCmp = new ArrayList<String>();
    private static Random rnd = new Random();
    	

    //Main Program
    public static void main(String[] args) {
        ReadCVS obj = new ReadCVS();
        //System.out.println("starting..");
        obj.run();
        //System.out.println("printing..");
        obj.printProbabilities();
    }
    
    //Create user list mapping with tools
    //Table userMap = new Table();
    HashMap<String, ArrayList<String>> userMap = new HashMap<String, ArrayList<String>>();
    

    
    public void run() {

        String csvFile = "/Users/nielskjer/Desktop/Projects/Graphs/quickstart/custom_stacklist.csv";
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedReader br3 = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            //Create BufferedReader object
            br1 = new BufferedReader(new FileReader(csvFile));
            br2 = new BufferedReader(new FileReader(csvFile));
            br3 = new BufferedReader(new FileReader(csvFile));
            

            //Generate User ArrayList for all tools and store in map
            while ((line = br1.readLine()) != null) {
                String[] inLine = line.split(cvsSplitBy);
                String toolName;
                toolName = inLine[1];
                userMap.put(toolName, new ArrayList<String>());
            }
            
            //Map tools with user lists
            while ((line = br2.readLine()) != null) {
                String[] array = line.split(cvsSplitBy);
                String companyName, toolName;
                companyName = array[0];
                toolName = array[1];
                tmpTool.add(toolName);
                tmpCmp.add(companyName);
                if (userMap.keySet().contains(toolName)) {
                	userMap.get(toolName).add(companyName);
                } 
            }
            
            //Test for debugging
            //System.out.println(userMap.get("Dropbox").size());
            //System.out.println(userMap.get("Salesforce").size());
            

            /*** Calculate Probabilities for all permutations available in the keyset ***/
            while ((line = br3.readLine()) != null) {
            	String[] array = line.split(cvsSplitBy);
            	String toolName;
            	toolName = array[1];
            	for (String tool : userMap.keySet()) {
            		calculateProbability(userMap.get(tool), userMap.get(toolName), tool, toolName);
                	}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println(names);
        //System.out.println(probabilities);
        System.out.println("Done");
        
    }
    
    //private static Random rnd = new Random();
    
    public static<K,V> void swapTwoRandomValues(Map<K,V> map) {
    	if (map.size() <= 1) {
    		throw new IllegalArgumentException("Not enough items!");
    	}
    	
    	//Choose 2 random positions
    	int pos1 = 0, pos2 = 0;
    	while (pos1 == pos2) {
    		pos1 = rnd.nextInt(map.size());
    		pos2 = rnd.nextInt(map.size());
    	}
    	
    	if (pos1 > pos2) {
    		int aux = pos1;
    		pos1 = pos2;
    		pos2 = aux;
    	}
    	
    	//Fetch the entries
    	Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
    	Map.Entry<K, V> entry1 = null;
    	Map.Entry<K, V> entry2 = null;
    	
    	for (int i = 0; i <= pos1; i++) {
    		entry1 = it.next();
    	}
    	for (int i = pos1; i < pos2; i++) {
    		entry2 = it.next();
    	}
    	
    	//Swap Values
    	V tmpValue = entry1.getValue();
    	entry1.setValue(entry2.getValue());
    	entry2.setValue(tmpValue);
    }
    
    

    /* @Params:
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    First two arguments take an ArrayList of users who use a particular tool.
    Last two arguments take the name of the tools, ergo the key of the Hashmap reference.
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    public void calculateProbability(ArrayList<String> a, ArrayList<String> b, String toolNameA,
                                     String toolNameB) {

        //Calculate combined count for each tool
        int combinedCount = 0;
        if (a.size() > b.size()) {
            for (int i = 0; i < a.size(); i++) {
                if (b.contains(a.get(i))) {
                    combinedCount++;
                }
            }
        } else {
            for (int i = 0; i < b.size(); i++) {
                if (a.contains(b.get(i))) {
                    combinedCount++;
                }
            }
        }

        
    /* Probability calculation module
     * ----------------------------
     */
        float p1 = 0, p2 = 0;
        float divisor = combinedCount;
        p1 = (divisor / a.size());
        p2 = (divisor / b.size());

        if (p1 != 0 && p2 != 0 && combinedCount != 0) {

            //Build strings to be added on the ArrayLists
            StringBuilder sb = new StringBuilder();
            sb.append(toolNameA);
            sb.append(": ");
            sb.append(a.size());
            sb.append(", ");
            sb.append(toolNameB);
            sb.append(": ");
            sb.append(b.size());
            
            names.add(sb);
            sb = null;
            //System.out.println("here");

            StringBuilder build = new StringBuilder();

            build.append("(");
            build.append(toolNameA);
            build.append(",");
            build.append(toolNameB);
            build.append(")");
            build.append(" is: ");
            build.append(combinedCount);
            build.append(" ** ");
            build.append(a.size());
            build.append(",");
            build.append(b.size());
            build.append(" -- ");
            build.append("Probability for ");
            build.append(toolNameA);
            build.append(" is: ") ;
            build.append(p1);
            build.append(" and the probability for ");
            build.append(toolNameB);
            build.append(" is: ");
            build.append(p2);
           
            probabilities.add(build);
            build = null;
            
            //Debugging
            //System.out.println(p1);
            //System.out.println(p2);
            
            //Add data elements to individual queues
            storeProbability(p1, p2);
            storeNames(toolNameA, toolNameB);
            
        }
    }
    
    
    //Helper Methods:
    //Store tool probabilities and names in separate Queues[]
    //[][][][][][][][][][][][][][][][][][][][][][][][][][]
    
    //Returns the probability Queue
    public Queue<Float> probabilityQ() {
    	return probQueue;
    }
    
    //Returns the name Queue
    public Queue<String> probablityN() {
    	return nameQueue;
    }
    
    
    //Stores two floating point numbers in our probability queue
    public void storeProbability(float p1, float p2) {
    	//Store probability in private instance variable
    	probQueue.add(p1);
    	probQueue.add(p2);
    }
    
    
    //Stores two strings in our name queue
    public void storeNames(String name1, String name2) {
    	//Store names in private instance variable
    	nameQueue.add(name1);
    	nameQueue.add(name2);
    }
    
    
    public float getProbability() {
    	//LIFO
    	return probQueue.remove();
    }
    
    public String getName() {
    	//LIFO
    	return nameQueue.remove();
    }
    
    public void clearProbability() {
    	//Remove everything in probability queue
    	for (int i = 0; i < probQueue.size(); i++) {
    		probQueue.remove(i);
    	}
    }
    
    public void clearNames() {
    	for (int i = 0; i < nameQueue.size(); i++) {
    		nameQueue.remove(i);
    	}
    }

    public final void putToMap(String key, ArrayList<String> values) {
    	if (userMap.containsKey(key)) {
    		userMap.remove(key);
    		userMap.put(key, values);
    	} else {
    		userMap.put(key, values);
    	}
    }
        

    public void printProbabilities() {
    	
        try {
            //Create FileWriter object
            PrintStream out = new PrintStream(new FileOutputStream("example22.txt"));

            //Write to text file
            out.println("=========================");

            for (int i = 0; i < names.size(); i++) {
                out.println(names.get(i));
                out.println("=================================");
            }
            
            out.println("=================================");
            out.println("=================================");
            out.println("!!Probability Calculations!!");
            out.println("=================================");
            out.println("=================================");

            for (int i = 0; i < probabilities.size(); i++) {
                out.println(probabilities.get(i));
                out.println("=================================");
            }
            
            //Close writer
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
