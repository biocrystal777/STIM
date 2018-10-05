import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class Reactor{
    // reference for the affected substrate, indicated in the ReactionArray
    private Substrate AffSubs; 
    // stoichiometric value from ReactionArr
    private double StoichVal=0;
    // contribution of concentration change in a single time step
    private double Contrib = 0; 

    //  getter & setter
    
    // AffectedSubstrate
    public Substrate get_AffSubs()
    {   return AffSubs;
    }
    public void set_AffSubs(Substrate dummy)
    {   AffSubs = dummy;
    }
    // StoichiometricValue
    public double get_StoichVal()
    {   return StoichVal;        
    }
    public void set_StoichVal(double dummy)
    {   if(dummy != 0)
            {   StoichVal = dummy;
            }
        else
            {   StoichVal = dummy;
                System.out.println("!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!  Warning: A Substrate was designated as an active substrate,\n!!!! although its Stoichiometric Value is 0 at this Enzyme !");
                System.out.println("!!!!!!!!!!!!!!!!!"); 
            }
    }
    
    public double get_Contrib()
    {   return Contrib;
    }
    public void set_Contrib(double dummy)
    {    Contrib = dummy;
    }
     

    public void ApplyContrib()
    {   
        double conc = AffSubs.get_Concentration();
        conc += Contrib;
        AffSubs.set_Concentration(conc);
        //        System.out.println(conc);//
    }
    
}
