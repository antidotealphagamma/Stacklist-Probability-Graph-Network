package com.vogella.maven.quickstart;

import java.io.*;
import java.util.*;

public class FrequencyTable extends TreeMap<String,Integer> {
  
  public FrequencyTable() {
    super();
  }

    // constructor
  public ArrayList<String> generic = new ArrayList();
  
    public FrequencyTable(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        int byteCode = in.read();
        String line = "";
        String cvsSplitBy = ",";
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(filename));
        String companyName, toolName;
        while ((line = br.readLine()) != null) {
          //split and store in new array each iteration
          String[] array = line.split(cvsSplitBy);
          companyName = array[1];
          toolName = array[2];
          if (this.containsKey(toolName)) {
            int count = this.get(toolName);
            this.put(toolName, count+1);
            //this.put(toolName, companyName);
            } else {
                this.put(toolName, 1);
            }
        } 
    }

    // prints out the frequency of each byteCode in the table
    public void print() {
      int sum;
      sum = 0;
      
        for (String byteCode : this.keySet()) {
            int count = this.get(byteCode);
            sum += count;
            byteCode = "\""+byteCode+"\"";
            generic.add(byteCode);
        }
        //System.out.printf("Total count of tool mentions: %d\n", sum);
        //System.out.println(generic);
    }
    public ArrayList<String> generate() {
      for (String byteCode : this.keySet()) {
        byteCode = "\""+byteCode+"\"";
        generic.add(byteCode);
      }
      //System.out.println(generic);
      return generic;
    }
}
