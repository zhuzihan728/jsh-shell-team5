package uk.ac.ucl.jsh.applications;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



public class Tail extends Head{ 
    
    @Override
    public void writeToshell(ArrayList<String> lines, int headLines, OutputStreamWriter writer) throws IOException {
        int limit = headLines;
        if(headLines>lines.size()){
            limit = lines.size();
        }
        for(int i =limit;i>=1;i--){
            writer.write(lines.get(lines.size()-i));
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
    
}