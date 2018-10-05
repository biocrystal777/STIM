import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class SimpleValue extends MathOperation{

    protected  double op = 0 ; //operand
    protected  double result=0;
    protected  boolean ConstantFlag;
    protected  Substrate RightSubs; // Substrate reference, if the Object is a Variable
    protected double ConstantValue=0;
    
    // Variables have to be enclosed in squared brackets
    private Pattern variable = Pattern.compile("\\[.*\\]");  
    private Pattern lsqrbrack = Pattern.compile("\\s*\\[.*");
    private Pattern rsqrbrack = Pattern.compile(".*]\\s*");
    // Constants don't have brackets
    
    @Override public void initSimpleValue(String ValueName, Reactor[] Reactors, ArrayList<Constant>  Constants)
    {  // remove whitespaces
        int end;
        while (ValueName.charAt(0) == ' ') 
            {   ValueName = ValueName.substring(1);
            }
        //        System.out.println("ValueName " + ValueName);//
        //        ValueName = ValueName + "    ";
        end = ValueName.length() - 1;        
        while (ValueName.charAt(end) == ' ')
            {   ValueName = ValueName.substring(0, end); //
                System.out.println(ValueName) ; //
                //  System.out.println("dsafsafsafsafsa======"); //
                // last char is not part of the substring
                end = ValueName.length() - 1;            
            }  
        Matcher m_variable = variable.matcher(ValueName);
        if (m_variable.matches())
            {   // Get Reference of the Variable Value
                Substrate subs;
                int SubsIndex; // Index from Reactors[]
                int ValueIndex =  parseVariableIndex(ValueName); // Index from
                for (Reactor re : Reactors)
                    {   subs=re.get_AffSubs();
                        SubsIndex = subs.get_SubstrateIndex();
                        if (ValueIndex == SubsIndex)
                            {   RightSubs = subs;
                                System.out.println("found reference to " + SubsIndex) ; //
                                break;
                            }
                    }
            }
        else                  
            {   // this object is not considered as a variable => it is a
                // constant => set its value to the corresponding input from file
                // 
                boolean found=false;
                for (Constant co : Constants) 
                    {   String name = co.get_ConstantName();
                        if (name.equals(ValueName))
                            {   ConstantValue = co.get_ConstantValue();
                                ConstantFlag = true;
                                found = true;
                            }
                    }
                if (found == false)
                    {
                        System.out.println("Your Constant name was not found. Please specify this name and its value in your input file");
                    }
            }
    }
    
    private int parseVariableIndex(String input)
    {   int index = -1;        
        Matcher m_lsqrbrack = lsqrbrack.matcher(input);
        Matcher m_rsqrbrack = rsqrbrack.matcher(input);        
        int i=0;        
            int le;
            if (m_lsqrbrack.matches() && m_rsqrbrack.matches())
                //true, if [] are present
                {   // rm outer whitespaces
                    //   System.out.println("true");
                    while (input.charAt(0)==' ')
                        {   input = input.substring(1);                        
                        }
                    le = input.length();
                    while (input.charAt(le-1)==' ')
                        {   input = input.substring(0,le-1);
                            le = input.length();
                        }                
                    // rm brackets
                    input = input.substring(1);
                    input = input.substring(0, input.length()-1);		
                    // rm inner whitespaces
                    while (input.charAt(0)==' ')
                        {   input = input.substring(1);                        
                        }
                    le = input.length();
                    while (input.charAt(le-1)==' ')
                        {   input = input.substring(0,le-1);
                            le = input.length();
                        }
                    // parse RefNumber
                    try
                        {       
                            index = Integer.parseInt(input);
                        }                 
                    catch(NumberFormatException ex)
                        {   System.err.println("An error occured while allocating the Variable" + input +"to its analytical context");
                            ex.printStackTrace();
                        }
                    if (index==-1)
                        {   System.out.println("WARNING:An error occured while allocating the Variable" + input +"to its analytical context");
                        }
                }
            return index;
        }
    
    

    
        public boolean isConstant()
        {  return ConstantFlag;
        }
        public void set_ConstantFlag(boolean dummy)
        {   ConstantFlag = dummy;
        }  
        public double get_result()
        {   return result;
        }

        public double calc()
        {  
            if (ConstantFlag==true) 
                {
                    result = ConstantValue;
                }
            else 
                {
                    result = RightSubs.get_Concentration();
                }
            return result;
        }
    
        public boolean isUnary()
        {   return false;
        }
        public boolean isBinary()
        {   return false;
        }
        public boolean isSimpleValue(){
            return true;
        }

        @Override public  double calc(double op1, double op2)
            {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
                return 0;
            }        
        @Override public double calc(double op1)
            {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
                return 0;
            }
        @Override public double get_op()
            {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
                return 0;
            }

        @Override public void set_op(double op)
            {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");  
            }

        @Override public  double get_op1()
            {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");  
                return 0;
            }
        @Override public  void set_op1(double dummy)
            {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");  
            }
        @Override public double get_op2()
            {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");  
                return 0;
            }
        @Override public  void set_op2(double dummy)
            {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
            }
    }