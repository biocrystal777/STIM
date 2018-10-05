import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
public class STIM{
    
    // args from calling this program
    
    private static String inputfilename = "...\n";
    private static String outputfilename = "STIMconctable";
    //
    //        getter & setter
    
    //filename
    String get_inputfilename()
    {   return inputfilename;
    }
    void set_inputfilename(String dummy)
    {   inputfilename= dummy;       
    }
    //
    
    // methods ////////////////////////
    

    //  main
    public static void main(String[] args) {
        if (args.length != 0) 
            { evalargs(args);
            }
        basicrun();           
    }//end main
    
    // basicrun
    public static void basicrun() 
    {   // main method for creating a conc table 
        StimFileOpener myfile = new StimFileOpener();
        myfile.OpenFile(inputfilename);
        RefCont PlayerRefs = myfile.get_playerrefs();
        StdSimulator mysimul = new StdSimulator();
        mysimul.set_playerrefs(PlayerRefs);
        mysimul.DoSimul();
    } //end basicrun
    
    //begin evalargs
    private static void evalargs(String[] args){   
        // reacts on and sets flags for modifiers from commandline
        // 
        
        String help =("Sorry, there's no help available yet.");
        for (int i=args.length-1; i>=0; i--){
            
            /// -h calls help and does not finishs the program
            if (args[0].equals("-h")) 
                {   System.out.println(help);
                    System.exit(0);
                }
            /// -i Specifies input filename
            else if (args[i].equals("-i"))
                {  if(i!=args.length-1)
                        { inputfilename = args[i+1];
                        }
                }
            /// -o Specifies outputname
            else if (args[i].equals("-o"))
                {  if(i!=args.length-1)
                        {  outputfilename = args[i+1];
                        }
                }
            else if (args[i].equals("-S"))
                {  System.out.println("Death and decay shall overcome this world!\n The mastery of our dark lord, is near.\n Hail to Satan!!!");
                    System.exit(0);
                }
        }
    } //end evalargs
}   

