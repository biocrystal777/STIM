import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;

public class FormulaTree{
    // Binary TreeStructure for creating a formula object in a KinChar; 
    // creates a tree from a formula;
    
    private MathOperation ThisNode;
    int operatorcode; // indicates the operator Type for ThisNode
    FormulaTree LeftNode = null;
    FormulaTree RightNode = null;
    // Content of each Node: one Operation, based on the formulas above
  
    // Patterns for formula creation
    final Pattern varref_begin = Pattern.compile("\\s*\\[\\s*");
    final Pattern varref_end = Pattern.compile("\\s*\\]\\s*");
    final Pattern block_begin = Pattern.compile("\\s*\\(\\s*");
    final Pattern block_end = Pattern.compile("\\s*\\)\\s*");
    // basical operations
    final Pattern plus = Pattern.compile("\\s*\\+\\s*");
    final Pattern minus = Pattern.compile("\\s*-\\s*");
    final Pattern times = Pattern.compile("\\s*\\*\\s*");
    final Pattern div =  Pattern.compile("\\s*\\/\\s*");
    // patterns for recognizing 
    final Pattern lrmbrack = Pattern.compile("^\\s*\\(.*");
    final Pattern rrmbrack = Pattern.compile(".*\\)\\s*$");
    
    // additional operations 2b added later
    //   final Pattern ln =  Pattern.compile(""); //LN(x) e.g.
    // final Pattern log =  Pattern.compile(""); //LOG_n(x), n e N
    //        final Pattern exp = Pattern.compile(""); // EXP
    //        final Pattern pot = Pattern.compile(""); // POT x^y
    //        final Pattern sqrt = Pattern.compile(""); // SQRT
    //        final Pattern nrt = Pattern.compile(""); // 'nth' root:  NRT_n(x), n e N
    
    final String var_brackopen = "[";
    final String var_brackclose = "]";
    final String br_begin = "(";
    final String br_end = ")";
    final String plus_sign = "+";   // code 1
    final String minus_sign = "-";  // code 2
    final String times_sign = "*";  // code 3
    final String div_sign = "/";    // code 4
    final String pow_sign = "^";    // code 5
     
    public MathOperation get_ThisNode()
    {   return ThisNode;  
    }
    // public void set_ThisNode(MathOperation dummy)
    // {   ThisNode = dummy;
    // }
    

    public int FindLowPrioOp(String OnThisString)
    {   // Returns the position of the operator with lowest priority
        //        System.out.println("Im now in FindLowPrioOp");//
        // Each operator in this string gets a priority value;
        // Priority of +|- is the lowest = 0;
        int prio_dashcalc = 0;    
        // The next higher priority group is *|/  ; prio_point_calc
        // Now always prio_pointcalc > prio_dashcalc :        
        int prio_pointcalc = 1;
        // Next higher operator ^ has to have more priority
        // => count all *|/ in String;
        boolean go =true;
        int l=0; 
        int pos=0;
        while (go==true)
            {  
                pos = OnThisString.indexOf(times_sign, pos+1);
                if (pos==-1)
                    {   go=false;                    
                              
                    }
                else 
                    {   l++;
                    }
            }
        go = true;
        pos=0;
        while (go==true)
            {  
                pos = OnThisString.indexOf(div_sign, pos+1);
                if (pos==-1)
                    {   go=false;                    
                    }
                else 
                    {   l++;
                    }
            }
        
        // If there are l *|/, set prio_pow = l+1
        // => prio_pow > prio_pointcalc > prio_dashcalc
        int prio_pow=l+1;        
        // Bracket embraceed terms have always highest priority;
        // Count all ^ ;
        go = true;
        pos=0;
        int k=0;        
        while (go==true)
            {   
                pos = OnThisString.indexOf(pow_sign, pos+1);
                if (pos==-1)
                    {   go=false;                    
                    }
                else 
                    {   k++;
                    }
            }
        // and set prio_brack = (prio_pow * (k+1))
        //  => prio_brack > prio_pow > prio_pointcalc > prio_dashcalc
        int  prio_brack = (k+1)*prio_pow;
        //        System.out.println("My priobrack is " + prio_brack);
        // Now every operator get's his own priority value:
        // priority(op o) = prio_xxx + number of open brackets * prio_brack 
        // Non-operator positions and the positions in multichar-operators (SQRT, EXP, ...) except the
        // first one have priority -1;

        // number of brackets
        int a=0;
        go = true;
        while (go==true)
            {   
                pos = OnThisString.indexOf("(", pos+1);
                if (pos==-1)
                    {   go=false;                    
                    }
                else 
                    {   a++;
                    }
            }
        //        System.out.println(a + " bracket terms used"); //
        int le= OnThisString.length();
        int[] priorities = new int[le+1]; // 
             
        int o=0; // pos counter
        int lowestpos=le; // last value is for initialization
        priorities[lowestpos] = (a+1)*prio_brack*2;  
        String sub;
        int br_bonus = 0;
        //        System.out.println("Priority at 0 is : " + priorities[lowestpos]);//
        while (o < le)
            // Get Position of op with lowest priority :
            { //  sub = OnThisString.substring(o,o+1);
      
                if (OnThisString.charAt(o) ==' ')
                    {   priorities[o]=-1;                        
                    }
                else if (OnThisString.charAt(o) =='(')
                    {   priorities[o]=-1;
                        br_bonus += prio_brack; 
                    }
                else if (OnThisString.charAt(o) ==')')
                    {   priorities[o]=-1;
                        br_bonus -= prio_brack;
                        
                    }
                else if (OnThisString.charAt(o) =='+')
                    {   priorities[o]=br_bonus;                       
                        if ((priorities[o]< priorities[lowestpos]) && (priorities[o] > -1 ))
                            {   lowestpos = o;
                            }
                        
                    } 
                
                else if (OnThisString.charAt(o) =='-')
                    {   priorities[o]=br_bonus;                       
                        if ((priorities[o]< priorities[lowestpos]) && (priorities[o] > -1))
                            {   lowestpos = o;
                            }
                        
                    } 
                else if (OnThisString.charAt(o) =='*')
                    {   priorities[o]=br_bonus + prio_pointcalc;                       
                        if ((priorities[o] < priorities[lowestpos]) && (priorities[o] > -1))
                            {   lowestpos = o;
                            }
                        
                    }
                else if (OnThisString.charAt(o) =='/')                
                    {   priorities[o]=br_bonus + prio_pointcalc; 
                        // System.out.println("here at o = " + o);
                        //System.out.println("here prios at " + priorities[o] +  " are " + priorities[o]); 
                        //System.out.println(                        priorities[lowestpos]);
                        //                      
                        if ((priorities[o] < priorities[lowestpos]))// && (priorities[o] > -1)) // Why the hell does this not get true!?!?!?!
                            {   lowestpos = o;
                                //      System.out.println("here"); //
                            }
                        
                    }
                else if (OnThisString.charAt(o) =='^')                
                    {   priorities[o]=br_bonus + prio_pow;                       
                        if ((priorities[o] < priorities[lowestpos]) && (priorities[o] > -1))
                            {   lowestpos = o;
                            }
                    }
                else  
                    {   priorities[o] = -1;
                    }
                //                System.out.println("prios at " + o +  " are " + priorities[o]);
                //                System.out.println("lowest pos is " + lowestpos);
                o++;
            }
        
        return lowestpos;
    }
    

    private void createThisNode(String FormulaDescriptor, Reactor[] Reactors, ArrayList<Constant> Constants)
    {   //Find the operator with lowest priority:
        int pos = FindLowPrioOp(FormulaDescriptor);
        // pos is bigger than the String length, if 
        // no operator exists on the string;
        if (FormulaDescriptor.length() <= pos)
            { pos = 0 ;
            }

            
        //        System.out.println("The lowest pos is: " + pos);        
        //
        if (FormulaDescriptor.charAt(pos) == '+')
            {   ThisNode = new Addition();
                operatorcode = 1;                
                //System.out.println("Create This Node finished: Addition was created");//
            } 
        // only binary minus; unary minus 2b added
        else if (FormulaDescriptor.charAt(pos) == '-')
            {   ThisNode = new Subtraction(); 
                operatorcode = 2;
                //System.out.println("Create This Node finished: Substraction was created");//
            } 
        else if (FormulaDescriptor.charAt(pos) == '*')
            {   ThisNode = new Multiplication();                
                operatorcode = 3;
                //System.out.println("Create This Node finished: Multiplication was created");//
            }
        else if (FormulaDescriptor.charAt(pos) == '/')                
            {   ThisNode = new Division();                
                operatorcode = 4;
                //System.out.println("Create This Node finished: Division was created");//
            }
        else if (FormulaDescriptor.charAt(pos) == '^')                
            {   ThisNode = new Power();
                operatorcode = 5;
                //System.out.println("Create This Node finished: Power was created");//
            }     
        // 2b extended, when other operations are added
        else 
            {   ThisNode = new SimpleValue();
                ThisNode.initSimpleValue(FormulaDescriptor, Reactors, Constants);
                // initialize the simple values with variables or constants 
                // constants
            }
    }
    
    // recursive creation of the tree from given FormulaDescriptor
    
    
    public void createFormulaTree(String FormulaDescriptor, Reactor[] Reactors, ArrayList<Constant> Constants)   
    {

        // create ThisNode from the FormulaDescriptor   
        //        System.out.println("New Node;");  // 
        createThisNode(FormulaDescriptor, Reactors, Constants);
        //
        if (ThisNode.isBinary())
            {
                // split the FormDescriptor at this operator and return the left and right string, if the Operator is Binary
                String[] newoperands = DualSplitAtOp(FormulaDescriptor);
                String LeftSubstring = newoperands[0];
                String RightSubstring = newoperands[1];
            
                // create left and right subtree with left and right substring, if the Operator is Binary
                if ( (LeftSubstring!=null) && (RightSubstring!=null))
                    {   LeftNode = new FormulaTree();
                        RightNode = new FormulaTree();
                        LeftNode.createFormulaTree(LeftSubstring, Reactors, Constants);
                        RightNode.createFormulaTree(RightSubstring, Reactors, Constants);
                    }
                else
                    {   System.out.println("A Binary Operator has only one operand!");    
                    }
            }        
        // Return The Operand if the is Unary;
        else if (ThisNode.isUnary())           
            {   String newoperand = SingleSplitAtOp(FormulaDescriptor);
                
                // create only left subtree, if the Operator is Unary; 
                if (newoperand != null)            
                    {   LeftNode = new FormulaTree();             
                        LeftNode.createFormulaTree(newoperand,  Reactors,  Constants);
                    }
            }        
        // Dont create subtrees in other cases; The existing references
        // for LeftNode and RightNode will be still null, if ThisNode
        // is a simple value;   

        // set constants and Variables;
 
    }


    private String[] DualSplitAtOp(String formfrag)
    {  // System.out.println(formfrag + "  ***"); //
	int pos = FindLowPrioOp(formfrag);    
        String[] splitstring = new String[2];
        splitstring[0] = formfrag.substring(0, pos) ; 
        splitstring[1] = formfrag.substring(pos+1);
        // remove dispensable brackets:
        splitstring[0] = rmbrackets(splitstring[0]);
        splitstring[1] = rmbrackets(splitstring[1]);
        return splitstring;
    }
    

    private String SingleSplitAtOp(String formfrag)
    {   
        int pos  = FindLowPrioOp(formfrag); 
        int i = pos;
        // remove operator word
        while (formfrag.charAt(i)!='(')
            {   i++;                        
            }
        if (formfrag.charAt(i) == '(' )
            {   formfrag = formfrag.substring(i);
            }
        // remove brackets
        formfrag = rmbrackets(formfrag);
        return formfrag;
        
    }
        
    private String rmbrackets(String formfrag)
    {   // remove outer, dispensable brackets        

        //   System.out.println(formfrag);//
        Matcher m_lrmbrack = lrmbrack.matcher(formfrag);
        Matcher m_rrmbrack = rrmbrack.matcher(formfrag);        
        int i=0; 
	int le;
	boolean b;
        if (m_lrmbrack.matches() && m_rrmbrack.matches())
           
            //true, if dispensable brackets exist
            {   // rm outer whitespaces
                //   System.out.println("true");
                while (formfrag.charAt(0)==' ')
                    {   formfrag = formfrag.substring(1);                        
                    }
                le = formfrag.length();
                while (formfrag.charAt(le-1)==' ')
                    {   formfrag = formfrag.substring(0,le-1);
                        le = formfrag.length();
                    }                
		// test, if the outer brackets belong together 
		// to avoid false eval of (a+b)*(c+d) e.g.
		b = OuterBracketsMate(formfrag);
		//
		if ( ( formfrag.charAt(0) == '(' ) &&
		     (formfrag.charAt(le-1) ==')') && b )
                    {   // remove brackets
			formfrag = formfrag.substring(1);
			formfrag = formfrag.substring(0, formfrag.length()-1);		
                        //remove inner whitespaces
			while (formfrag.charAt(0)==' ')
			    {   formfrag = formfrag.substring(1);                        
			    }
			le = formfrag.length();
			while (formfrag.charAt(le-1)==' ')
			    {   formfrag = formfrag.substring(0,le-1);
				le = formfrag.length();
			    }        
			// additional dispensable brackets?
			formfrag = rmbrackets(formfrag);
		    }   
	    }
        //	System.out.println(formfrag);
	return formfrag;
    }
    // calculate formula value;
    
    private boolean OuterBracketsMate(String test)	
    {   boolean result=false;
	int counter=0;
	//	int i = 0;
	// for each left bracket add 1, for each right subtract one;
	// if outer brackets belong together, last char ')' has to be 1
	//
	int le = test.length();
	if (( test.charAt(0) == '(' ) &&
	    (test.charAt(le-1) ==')') )
	    {   for (int i=0; i<le; i++)
		    {   if ((i==le-1) && (counter == 1))
			    {   result = true;
			    }
			else if (test.charAt(i) == '(')	
			    {   counter++;
			    }
			else if (test.charAt(i) == ')')		
			    {   counter--;
			    }
		    }
	    }
	else
	    {	System.out.println("Error occured while parsing the file. Please check your formulas.");
	    }	
	return result;
    } 
    
    public double evalFormula()
    // evaluate with postorder traversal
    {   double result = 0;        
        if ( ThisNode.isBinary() )
            {
                result = ThisNode.calc(LeftNode.evalFormula(), RightNode.evalFormula());
            }
        
        else if (ThisNode.isUnary())
            {   result = ThisNode.calc(LeftNode.evalFormula());
            }
        else if (ThisNode.isSimpleValue())
            {   result = ThisNode.calc();
            }             
        return result;	
    }    
    
    
    // public static void main (String args[])
    // {
    //     //      System.out.println("Give me a Formula, using +, -, *, / and () :\nConstants are abcdef = 12345:");
    //     double a=1;
    //     double b=2;
    //     double c=3;
    //     double d=4;
    //     double e=5;
    //     FormulaTree tester = new FormulaTree();
    //     String usrform;
    //     usrform = "( a + b ) / c";
    //     //usrform = UsrInteraction.AddInputByUser();
    //     MathOperation testop;
    //     double res;
    //     System.out.println("your formula is: " + usrform);
        
    //     //         tester.createFormulaTree(usrform);
    //     //        testop = tester.get_ThisNode() ;        
    //     //        System.out.println("The value of your formula is " + testop.get_result());
        
        
    //     //    res =  evalFormula();
    //     //     System.out.println("Your result is " + res)
        
	
    // }
}    