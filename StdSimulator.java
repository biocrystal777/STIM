import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class StdSimulator{
    
    private RefCont playerrefs;
    private double timeint=0.1; // time interval ins seconds
    private double duration=600; // duration in seconds
    private CopyOnWriteArrayList <Substrate> AllSubs = new CopyOnWriteArrayList <Substrate> ();
    private CopyOnWriteArrayList <Enzyme> AllEnzs = new CopyOnWriteArrayList <Enzyme> (); 
    private double ptime = 0;
    
    public RefCont get_playerrefs()
    {   return playerrefs;
    }    
    public void set_playerrefs(RefCont dummy)
    {   playerrefs = dummy;
    }
    
    /// methods
    
    public void DoSimul()
    {   
        AllEnzs = playerrefs.get_AllEnzs();
        AllSubs = playerrefs.get_AllSubs();
        System.out.print("time ");
        for (Substrate i : AllSubs) 
            {   System.out.print(i.get_SubstrateName() +" ");
            }
        System.out.println("\n\n\n");
        System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVV"); // checkline
            // for an output shortening script (chopper.pl) to transform the output into 
            // format readable by gnuplot
        simoutput();
        while (ptime <= duration)
            {   
                simcycle();                
                simoutput();
                //System.out.println(ptime + ", ");
                ptime += timeint;
            }    
    }
    
    private void simcycle()
    {   // to be parallelized
        for (Enzyme enz : AllEnzs)
            {   enz.CalcContribs(timeint);
            }
        // perhaps to be done parallelly later
        for (Enzyme enz : AllEnzs)
            {   enz.ApplyContribs();                
            }
    }
    
    private void simoutput()        
    {   
        String output = Double.toString(ptime);
        System.out.print( output.concat(" "));
        for (Substrate subs : AllSubs)
            {   output = Double.toString(subs.get_Concentration());
                System.out.print(output.concat(" "));
            }
        System.out.println();
    }
}
