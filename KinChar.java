import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class KinChar{
    
    // =Kinetic Characeteristicum

    // Formula
    //  These Standardexpressions are identified with and expression like 
    // ' %MICHAELIS% ' , ' %TWOSUBSSTANDARD% ', etc.
    //
    // as raw String
    // First only one expression or attribute, later formulas with additional
    // syntax for conditions in a second String => more expressions for describing the Characteristic
    // information directly read from file
    private String FormulaDescriptor; 
    private FormulaTree Formula;    
    // Conditions have to be defined are defined, if
    // an enzyme uses more than one
    // 0 = null => no Formula => Tree has to be created
    private int PredefIndicator=0;

    // if more than one expression it can be decided, which formula should be applied under certain conditions
    private String ConditionDescriptor=null;
    
    //  getter & setter
    
    // Formula
    public String get_FormulaDescriptor()
    {   return FormulaDescriptor;
    }
    public void set_FormulaDescriptor(String dummy)
    {   // conditions 2b added
        FormulaDescriptor = dummy;
    }
    public FormulaTree get_Formula()
    {   return Formula;
    }

    public void set_Formula(FormulaTree dummy)
    {
        Formula = dummy;
    }
    // Condition
    public String get_ConditionDescriptor()
    {   return ConditionDescriptor;        
    }
    public void set_ConditionDescriptor(String dummy)
    {   if(dummy != null)
            {   ConditionDescriptor = dummy;
            }
        else
            {   ConditionDescriptor = dummy;
                System.out.println("!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!  Warning: A Condition could not be read properly\n!!!! because the expression was void or syntacticly incorrect");
                System.out.println("!!!!!!!!!!!!!!!!!"); 
            }
    }
    public int get_PredefIndicator()
    {   return PredefIndicator;        
    }
    
    public void set_PredefIndicator(int dummy)
    {   PredefIndicator = dummy;        
    }
    // 


    
    // Further methods
    // reactors contain substrate/variable references
    //
    public void initialize(Reactor[] Reactors, ArrayList<Constant> Constants)
    {  // Sets the switch Predefindicator to a value corresponding to its formula; If it is a user spec formula, the String is put into a tree of operators and Predefindicator is set to 0.
        final Pattern predefseq = Pattern.compile("\\s*%.*%\\s*");
        //        final Pattern seq1 = Pattern.compile("\\s*%EXPRESSION1%\\s*");
        //        final Pattern seq2 = Pattern.compile(\\s*%EXPRESSION2%\\s*");
        Matcher m_predefseq = predefseq.matcher(FormulaDescriptor); //
        if (m_predefseq.find()) 
            { //
                //  if (seq1.find(FormulaDescriptor)) 
                //    {   PredefIndicator = 1;
                //    } 
                //  else if (seq2.find(FormulaDescriptor))
                //    {   PredefIndicator = 2 ; 
                //    }
                //       ....
                System.out.println("Enzyme kinetics are described with code " + PredefIndicator);
            }
        else 
            {
                FormulaDescriptorToFormulaTree(Reactors, Constants);
                
                // 0 => explicit term
                // 1,2,3,4... => Number of formula
                
                
            }
    }
        
    public void  FormulaDescriptorToFormulaTree(Reactor[] Reactors, ArrayList<Constant> Constants)
    {  
        Formula = new FormulaTree();
        Formula.createFormulaTree(FormulaDescriptor, Reactors, Constants);
    }
    
    public double EvalReRa()        
    {   double result = 0;
        if (PredefIndicator == 0) 
            // evaluate the formula tree by postorder traversal
            {   result = Formula.evalFormula();
            }
        
        // else : use the given  formulas
        
        return result;
    }

        
}//end Enzyme

