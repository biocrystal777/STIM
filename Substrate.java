import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

class Substrate{
    private int SubstrateIndex;
    private String SubstrateName;
    private double Concentration; // in mM
    private double DeltaG0; // =^ "Delta G-Null-Strich", pH 7.0
    
    
    // getter  & setter
    //Substrateindex
    public int get_SubstrateIndex()
    {   return SubstrateIndex;
    }
    public void set_SubstrateIndex(int dummy) // conditions 2b added
    {   this.SubstrateIndex=dummy;
    }
    // Substratename
    public String get_SubstrateName()
    {   return SubstrateName;
    }
    public void set_SubstrateName(String dummy){
        if (dummy != null && dummy.trim().length() > 0)
            {  this.SubstrateName = dummy;
            }
    }
    // Concentration
    public double get_Concentration()
    {   return Concentration;
    }
    public void set_Concentration(double dummy)
    {   if (dummy > 0){	
	this.Concentration = dummy;
	}
	else{
	Concentration = 0.000000000000000000001;
	}	
    }	
    // DeltaG0
    public double get_DeltaG0()
    {   return DeltaG0;
    }
    public void set_DeltaG0(double dummy)
    {   this.DeltaG0 = dummy;  
    }
}
