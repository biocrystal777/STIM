import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class Thermodynamics{
    
    private static final double T = 303.15; // Temperature in Kelvin 
    private static final double R = 0.008314; // Universal gas constant in kJ/(mol*K)        
    // checks, whether a reaction is done in suspected forward direction or not
    static boolean ffdir(Reactor[] Reactors){
        boolean forward = true;               
	//Determinate Q and DeltaG0prime
        double Q = 1 ;
        double DeltaG0prime = 0;
        for (Reactor r : Reactors){           
            Substrate S = r.get_AffSubs();
            double conc = S.get_Concentration();
            double stoich = r.get_StoichVal();
            Q *= Math.pow(conc, stoich);
            DeltaG0prime += stoich*S.get_DeltaG0();
        }
        // calculate DeltaG for the current state;
        double DeltaG =  DeltaG0prime + (R * T * Math.log(Q));
        // set  ffdir = false, if reaction would be endergonic;
        if (DeltaG > 0){
            forward = false;            
            //    System.out.println("Thermotrigger");
        }       
        
        return forward;
    }
        
}
