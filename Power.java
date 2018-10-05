import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class Power extends BinaryOperation
{
    
    public double calc(double op1, double op2)
    {   result = Math.pow(op1, op2);
        return result;
    }
}