import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public abstract class BinaryOperation extends MathOperation{

    // superclass for all operations like +,-,*,/ , LOG_X(Y), X^Y, NRT_X(y), etc...
    protected  double op1 = 0 ; //operands
    protected  double op2 = 0 ;    
    
    @Override public double get_op1()
    {   return op1;         
    }
    @Override public void set_op1(double dummy)
    {   op1 = dummy;        
    }
    @Override public double get_op2()
    {   return op2;         
    }
    @Override public void set_op2(double dummy)
    {   op2 = dummy;        
    }
    @Override public  boolean isBinary()
    {   return true;
    }
    @Override public  boolean isUnary()
    {   return false;
    }
    @Override public  boolean isSimpleValue()
    {   return false;
    }
    
    @Override public double calc()
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
        return 0;
    }/// ?        
    @Override public double calc(double op1)
            {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
        return 0;
            }/// ? 
    @Override public double get_op()
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
        return 0;
    }      
    @Override public void set_op(double dummy)
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");     
    }
    @Override public  void initSimpleValue(String ValueName, Reactor[] Reactors, ArrayList <Constant>  Constants)
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
    }
}

