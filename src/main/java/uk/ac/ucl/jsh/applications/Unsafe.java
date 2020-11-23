package uk.ac.ucl.jsh.applications;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Unsafe implements Application{

    Application application;   

    public Unsafe(Application application){
        this.application = application;
    }

    
	@Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) {
        try{
            this.application.exec(appArgs, input, output);
        } catch (Exception e){
            e.printStackTrace();
        }       
    }
}