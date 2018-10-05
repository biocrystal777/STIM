public class Constant{
    // Identifier of this Constant (left from '=' in the file)
    private  String ConstantName; 
    // stoichiometric value from ReactionArr
    private double ConstantValue;
    
    //  getter & setter
    
    // AffectedSubstrate
    public String get_ConstantName()
    {   return ConstantName;
    }
    public void set_ConstantName(String dummy)
    {   ConstantName = dummy;
    }
    // StoichiometricValue
    public double get_ConstantValue()
    {   return ConstantValue;  
    }
    public void set_ConstantValue(double dummy)
    {   ConstantValue = dummy;
    }
    
}