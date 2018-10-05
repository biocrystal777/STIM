import java.io.*;
import java.util.*;

public class UsrInteraction
{ // class, that provides methods for additional usr input
  // during runtime
    
    
    // 
    public static String AddInputByUser()
    {   // Method to read a String from the commandline,
        String input = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));        
        try
            {  input = reader.readLine();
            }
        catch (IOException ex) 
            { ex.printStackTrace();
            }
        System.out.println(">>");
        return input;
    }//     

}