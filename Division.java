import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class Division extends BinaryOperation
{
  
    
    public double calc(double op1, double op2)
    {   
       
        try
            {
                
                result = op1/op2;
                return result;
            }
        catch(ArithmeticException e)
            {
                System.err.println("A try to divide your expression by 0 failed; Make sure, that all your relevant input values are not 0");
                e.printStackTrace();
               
            }
        if (op2==0)
            {
                op2=0.00000000001;
                result = op1/op2;
            }
        return result;
    }
}
