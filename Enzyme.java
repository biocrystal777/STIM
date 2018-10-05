/* 
 * main_class for the  Simulation Tool for Interesting Metabolisms
 *
 * Version 2011-12-01
 */
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
public class Enzyme{

    // Most important contents of the class
    private int Index;
    
    private String EnzymeName = "Enzyme"; // read from File

    private double[] ReactionArray=null; // Read from File

    // A reactor contains only the reference of all these substrates, that
    // are affected by the enzyme and the stoichiometric proportion number
    // 
    
    // made from ReactionArray
    private Reactor[] Reactors=null;     
    // All constants that are specific for this enzyme 
    // (contains the Name (String) and the value (double)
    // made from File
    private ArrayList <Constant> Constants = new ArrayList<Constant>();
    // If this enzymes uses predefined Standardexpressions for kinetic formulas,
    // some constants may be obligatory
    
    private ArrayList  <KinChar> KinChars = new ArrayList <KinChar> (); // 
    
    // private formulatrees ; 
    // contains information about the reaction rate formula after
    // parsing     

    // Things that could be added later;

    private String EcNumber = "0.0.0.0";
    private double Concentration = 0; // in mg/ml

    // getter & setter
    
    // EnzymeName 
    public String get_EnzymeName(){
        return EnzymeName;
    }
    public void set_EnzymeName(String dummy){
        if (dummy != null && dummy.trim().length() > 0){ 
            EnzymeName = dummy;
        }
    }    
    // Index
    public int get_Index(){
        return Index;
    }
    public void set_Index(int dummy){      
        Index = dummy;       
    }
    public double[] get_ReactionArray ()
    {   return ReactionArray;
    }
    public void set_ReactionArray(double[] dummy, int AllSubsSize)
    {   if (dummy.length == AllSubsSize)
            {  ReactionArray = dummy;
            }
        else
            {   System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!! Warning: The File does not contain a proper ReactionArray for this Enzyme! ");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
    }
    public ArrayList<Constant> get_Constants()
    {
        return Constants;
    }
    public void set_Constants(ArrayList<Constant> dummy)
    {
        Constants = dummy;
    }         
    
    public ArrayList<KinChar> get_KinChars()
    {
        return KinChars;
    }

    public void set_KinChars(ArrayList<KinChar> dummy)
    {
        KinChars = dummy;
    }

    /*
     * Additional Features 2b added later - perhaps
     *
     */


    // EcNumber
    public String get_EcNumber()
    {
        return EcNumber;
    }
    public void setEcNumber(String dummy){
        if (dummy != null && dummy.trim().length() > 0)
            { 
                this.EcNumber = dummy;
            }
    }
    // Concentration
    public double get_Concentration(){
        return Concentration;
    }
    public void set_Concentration(double dummy){
        this.Concentration = dummy;  
    }  
      
    // parsing methods

    // creates the Reactor of this enzyme, needs the bunch of references to all substrates
    public void ReactionArrayToReactors(CopyOnWriteArrayList <Substrate> AllSubstrates)
    {        
        // Speed could be increased (just a bit) by using hashes instead of double loops
        int subs_counter=0;
        System.out.println(AllSubstrates.size() + ReactionArray.length); //
        Substrate testsubstrate = AllSubstrates.get(0); //
    
        if (AllSubstrates.size() == ReactionArray.length)
            {
                int n = ReactionArray.length;
                for (int i = n ;  i>0 ; i--)
                    {   if (ReactionArray[i-1] != 0)
                            {   
                                subs_counter++;
                            }                
                    }

                Reactors = new Reactor[subs_counter];
                int newi=0;
                for (int i = n ; i>0 ; i--)
                    {   int a= n-i;
                        if(ReactionArray[a] != 0)                    
                            {   Substrate buffer = AllSubstrates.get(a);
                                Reactors[newi] = new Reactor();                      
                                Reactors[newi].set_AffSubs(buffer);
                                Reactors[newi].set_StoichVal(ReactionArray[a]); 
                                newi++;             
                            }
                    }                
                // System.out.println("Reactor-Test: ");        
                // for (int i=0 ; i < subs_counter ; i++)
                //     {   Reactor testreactor;
                //         testreactor = Reactors[i];                
                //         System.out.println( Reactors[i].get_AffSubs().get_SubstrateName() + " : " + Reactors[i].get_StoichVal());
                //     }
                // System.out.println("Reactor-Test-end");
            } 
        else 
            { 
                System.out.println(" Reaction Array does not fit to the Substrate list above.") ; 
            }
    }
    
    public void ParseFormulaDescriptors(){
        int n = KinChars.size();
        KinChar mykin;
        int  fdswitch = 0;
        for (int i = n ; i>0 ; i--)
            {   int a= n-i;
                mykin = KinChars.get(a);
                mykin.initialize(Reactors, Constants);
                fdswitch =  mykin.get_PredefIndicator();                   
            }         
    }
    

    /// methods for simulation
    
    // calculates the contribution for the concentration changes of each affected substrate during a 
    // given time interval
    public void CalcContribs(double timeint)
    {    
        double ReactionRate;        
        // here, the condition can be evaluated, if an enzyme reaction is 
        // described by more than one analytical expression
        int Charno = 0;
        //  get the right kinchar
        KinChar myKinChar = KinChars.get(Charno);
        
        // evaluate the formula of this KinChar and return the result
        ReactionRate = myKinChar.EvalReRa();  
        //        System.out.println(EnzymeName + ReactionRate);//
        if (Thermodynamics.ffdir(Reactors) == true)
            {
                for  (Reactor r : Reactors)
                    { 
                        r.set_Contrib (r.get_StoichVal() * ReactionRate*timeint);
                    }
            }
        else
            {
                for  (Reactor r : Reactors)
                    { 
                        r.set_Contrib ( (-1) * r.get_StoichVal() * ReactionRate*timeint);
                    }
            }
    }
    // Change
    public void ApplyContribs()
    {   
        for (Reactor r : Reactors)
            {   r.ApplyContrib();
            }
        
        }
    
}//end Enzyme