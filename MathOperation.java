import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public abstract class MathOperation{
    
    protected  double result=0;

    public double get_result()
    {   return result;
    }
    public abstract boolean isBinary();
    public abstract boolean isUnary();
    public abstract boolean isSimpleValue();
    public abstract double calc(); /// ?\
    public abstract double calc(double op1); 
    public abstract double calc(double op1, double op2); 
    
    // public abstract double calc(double op);
    // public abstract double calc(double op1, double op2);
    public abstract double get_op();
    public abstract void set_op(double dummy);
    public abstract double get_op1();
    public abstract void set_op1(double dummy);
    public abstract double get_op2();
    public abstract void set_op2(double dummy); 
    public abstract void initSimpleValue(String ValueName, Reactor[] Reactors, ArrayList <Constant>  Constants);
    
}
