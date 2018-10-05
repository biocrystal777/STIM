import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public abstract class UnaryOperation extends MathOperation{
    
    // superclass for all operations like , EXP(x) ,  LN(x), SQRT(x), ....
    protected  double op = 0 ; //operand
    
    
    public double get_op()
    {   return op;         
    }
    public void set_op(double dummy)
    {   op = dummy;        
    }
    public  boolean isBinary()
    {   return false;
    }
    public  boolean isUnary()
    {   return true;
    }
    public  boolean isSimpleValue()
    {   return false;
    }

    @Override public double calc()
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
        return 0;
    }/// ?        
    @Override public double calc(double op1, double op2)    
    {   System.out.println("WARNING:THIS METHOD MUST NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
        return 0;
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
        //    public abstract boolean isSimpleValue();
    }
    @Override public  void initSimpleValue(String ValueName, Reactor[] Reactors, ArrayList <Constant>  Constants)
    {   System.out.println("WARNING:THIS METHOD MAY NOT USED BY A BINARY OBJECT!\nKILL YOUR POGRAMMER FOR THIS SACRILEGE!");
    }
}