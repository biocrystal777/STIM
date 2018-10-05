import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
///////  Openfileclass
class StimFileOpener{  
    private File myfile;
    private FileReader myreader;
    private BufferedReader mybufreader;
    private CopyOnWriteArrayList <Enzyme> AllEnzymes = new CopyOnWriteArrayList <Enzyme> ();
    private int enz_count=0;
    private CopyOnWriteArrayList <Substrate> AllSubstrates = new CopyOnWriteArrayList <Substrate> ();
    private int subs_count=0;
    private RefCont playerrefs = new RefCont(); /** 
                                  * Object[] playerrefs
                                  * Contains all references to active players
                                  *specified in a STIM file.
                                  * The container is always a 
                                  *
                                  * java.util.concurrent.CopyOnWriteArrayList.
                                  * 
                                  * Object[0] = List for all enzymes
                                  * Object[1] = List for all substrates
                                  * Object[2] = .....
                                  * Later features like inhibitors can be added by
                                  * extending this array's length. Object
                                  *
                                  * 
                                  */  
    //Patterns  for reading the file
    private final Pattern commenthasher = Pattern.compile("#");
    private final Pattern title_phrase = Pattern.compile("#!TITLE&");
    private final Pattern substratelist_begin = Pattern.compile ("<substrate_list>\\s*");
    private final Pattern substratelist_end = Pattern.compile ("\\s*</substrate_list>");
    private final Pattern enzymelist_begin = Pattern.compile ("<enzyme_list>\\s*");
    private final Pattern enzymelist_end = Pattern.compile ("\\s*</enzyme_list>");
    private final Pattern enzymeblock_begin = Pattern.compile ("<enzyme_block>\\s*");
    private final Pattern enzymeblock_end = Pattern.compile ("\\s*</enzyme_block>");
    private final Pattern ident_begin = Pattern.compile ("<ident>\\s*");
    private final Pattern ident_end = Pattern.compile ("\\s*</ident>");
    private final Pattern index_begin = Pattern.compile ("<index>\\s*");
    private final Pattern index_end = Pattern.compile ("\\s*</index>");
    private final Pattern ReactionArray_begin = Pattern.compile ("<reaction_array>\\s*");
    private final Pattern ReactionArray_end = Pattern.compile ("\\s*</reaction_array>");
    private final Pattern conc_begin = Pattern.compile ("<conc>\\s*");
    private final Pattern conc_end = Pattern.compile ("\\s*</conc>");
    private final Pattern deltag_begin = Pattern.compile ("<deltag>\\s*");
    private final Pattern deltag_end = Pattern.compile ("\\s*</deltag>");
    private final Pattern const_begin = Pattern.compile ("<const>\\s*");
    private final Pattern const_end = Pattern.compile ("\\s*</const>");
    private final Pattern name_begin = Pattern.compile ("<name>\\s*");
    private final Pattern name_end = Pattern.compile ("\\s*</name");
    private final Pattern kin_begin = Pattern.compile ("<kin>\\s*");
    private final Pattern kin_end = Pattern.compile ("\\s*</kin>");
    private final Pattern form_begin = Pattern.compile ("<form>\\s*");
    private final Pattern form_end = Pattern.compile ("\\s*</form>");
    private final Pattern cond_begin = Pattern.compile ("<cond>\\s*");
    private final Pattern cond_end = Pattern.compile ("\\s*</cond>");
    private final Pattern val_begin = Pattern.compile ("<val>\\s*");
    private final Pattern val_end = Pattern.compile ("\\s*</val>");
    private final Pattern ArraySep = Pattern.compile("\\s+");
    // getter & setter
  
    // for Object[] playerref:
    public RefCont get_playerrefs()
    {  return playerrefs;
    }
    
    public void set_playerrefs(RefCont dummy)
    {        playerrefs = dummy;
    }    
    
    // opens a file to read it and direct it to a parsing method    
    public void OpenFile(String inputfilename)
    {  // opens a file to read it and direct it to a parsing method
        if (inputfilename.equals("")==false)
            {
                
                try // 
                    {   myfile = new File(inputfilename);
                        myreader = new FileReader(myfile);
                    }
                catch (FileNotFoundException ex)
                    {   System.err.println("Your inputfilename was not found.");
                        ex.printStackTrace();
                    }  
                catch (IOException ex) 
                    {   ex.printStackTrace();
                    }
                finally
                    { if(myfile.exists()==false || myfile.isDirectory()==true)
                            {  
                                System.out.println("Please give me another file name or type q or Q to quit");                    
                                inputfilename=UsrInteraction.AddInputByUser();
                                System.out.print("\n");
                                if ((inputfilename.equals("q")==true) || (inputfilename.equals("Q")==true))
                                    {  System.out.println("Thanks for using STIM, your Simulation  Tool for Interesting Metabolisms");
                                        System.exit(0);
                                    }
                                else
                                    {  this.OpenFile(inputfilename);
                                    }
                            }                       
                        else           
                            {   mybufreader = new BufferedReader(myreader);
                                System.out.println("File " + inputfilename +" has been found and is being parsed...\n");
                                ParseStimFile();                                
                            }        
                    }
            }        
        else 
            {   System.out.println("Please specify a file name or type q for quit:");
                this.OpenFile(UsrInteraction.AddInputByUser());
            }
        
    }// end OpenFile
  
    

    private void ParseStimFile()
    {   // Parse the content of an opened file, creates the 
        // all player objects (enzymes, substrates etc..) and puts their references
        // into the array playerrefs
        String currentline;
        String[] splitstring=null;
        boolean[] mode_flag= new boolean[2]; /* modeflag has the same length as the number of lists in the RefCont playerrefs
                                              * if [i] true, mode i is used for parsing
                                              * this part of the file
                                              * 0=Enzymes
                                              * 1=Substrates
                                              * .....
                                              * Only one mode should be active at once 
                                              */      
        for (int i=mode_flag.length; i>0 ; i-- )
            { mode_flag[i-1]=false;                
            }        
        try {
            while ( (currentline=mybufreader.readLine() ) != null)
                {   // looks for the Title line  and writes this line to the standard output
                    Matcher m_title_phrase = title_phrase.matcher(currentline);
                    if ((currentline.length() != 0) && m_title_phrase.find() ){
                        System.out.println(currentline);
                    }                
                    
                    // kill comments and return a "naked expression" to currentline
                    splitstring = commenthasher.split(currentline, -1);
                    currentline = splitstring[0];
                    
                    Matcher m_enzymelist_begin = enzymelist_begin.matcher(currentline);
                    Matcher m_enzymelist_end = enzymelist_end.matcher(currentline);
                    Matcher m_substratelist_begin = substratelist_begin.matcher(currentline);
                    Matcher m_substratelist_end = substratelist_end.matcher(currentline);
                    if (currentline.length() == 0)
                        {   continue;
                        } 
                    else if (m_enzymelist_begin.find())
                        {   mode_flag[0]=true; 
                            System.out.println("\n\n\n Begin Parsing of Enzymelist... \n\n\n");

                            continue;
                        }
                    else if (m_enzymelist_end.find())
                        {   mode_flag[0]=false;
                            System.out.println("\n\n End Parsing of Enzymelist...");
                            continue;
                        }                   
                    else if (m_substratelist_begin.find())
                        {   mode_flag[1]=true;
                            System.out.println("\n\n\n Begin Parsing of Substratelist...\n\n\n");
                            continue;
                        }
                    else if (m_substratelist_end.find())
                        {   mode_flag[1]=false;
                            System.out.println("\n\n End Parsing of Substratelist...\n\n\n");
                            // for automatic evaluation of the output:
                            System.out.print("COMPLETE_SUBSTRATE_LIST# ");
                            for (Substrate S : AllSubstrates){
                                System.out.print(S.get_SubstrateName() + " ");                    
                            }
                            System.out.println("\n\n\n");
                            continue;
                        }                    
                    // This has to be modified, if the program is extendend
                    // => New read methods and modes have to be added
                    else if(mode_flag[0]==true && mode_flag[1]==false)
                        {   // enzymeparsing                               
                            AllEnzymes.add(enz_count, ParseEnzymeBlock(currentline));
                            enz_count++;
                            continue;
                        }
                    else if (mode_flag[0]==false && mode_flag[1]==true)
                        {   // substrateparsing                            
                            AllSubstrates.add(subs_count, ParseSubstrateLine(currentline));
                            subs_count++;
                        }   
                    // if no mode is active
                    else if ((mode_flag[0]==false && mode_flag[1]==false))
                        { continue;
                        }
                    // if more than one modes is active
                    else if (mode_flag[0]==false && mode_flag[1]==false)
                        {                             System.out.println("Sorry, a mode in your file was not properly closed.\nYour File could not be parsed.");
                            break;                          
                        }
                    
                    continue;
                }
        }
        
        //                currentline = currentline.replaceFirst("#");

        catch(IOException e)
            { e.printStackTrace();
            }        
        // set playerrefs...
        
        playerrefs.set_AllEnzs(AllEnzymes);
        playerrefs.set_AllSubs(AllSubstrates);
        // end ParseFile
    }
    
    // begin ParseSubstrateLine
    public Substrate ParseSubstrateLine(String currentline)
    {        // Creates a new Substrate based on the given input and returns its reference;
        Substrate newsubstrate;
        newsubstrate = new Substrate();
        
        // read & set Substrateindex
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        int SubstrateIndex=IndexFromCurrentLine(currentline);

        newsubstrate.set_SubstrateIndex(SubstrateIndex);

        // read & set substratename
        String SubstrateName = NameFromCurrentLine(currentline); 
        newsubstrate.set_SubstrateName(SubstrateName);
        
        System.out.println("Substrate " + SubstrateName + " found...");
        System.out.println("Substrate index is set to " + newsubstrate.get_SubstrateIndex());
        // read & set Concentration
        double Concentration=ConcentrationFromCurrentLine(currentline);   
        
        newsubstrate.set_Concentration(Concentration);
        System.out.println("Setting its concentration to " + Concentration + " microM");
        // read & set DeltaG ==> Standard enthalpy of formation;
        
        double DeltaG=DeltaGFromCurrentLine(currentline);         
        newsubstrate.set_DeltaG0(DeltaG);
        System.out.println(SubstrateName + "'s Standard enthalpy of formation is " + DeltaG + " kJ/mol");        
        System.out.println("*++++++++++++++++++++++++++++++++++++++++++++\n");
        return newsubstrate;
    }
    
    private Enzyme ParseEnzymeBlock(String currentline) 
    {    // Creates a new Enzyme based on the block at the current position of the BufferedReader mybufreader;
         //Returns the enzyme's reference;
        Enzyme newenzyme = new Enzyme();     
        String[] splitstring=null;
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        try
            {
                while ( (currentline=mybufreader.readLine() ) != null)
                    {   // kill comments and return a "naked expression" to currentline
                        splitstring = commenthasher.split(currentline, -1);
                        currentline = splitstring[0];
                        Matcher m_enzymeblock_end = enzymeblock_end.matcher(currentline);
                      
                        if (currentline.length() == 0)
                            { //  System.out.println("!!!!");//
                                continue; 
                            } 
                        else if (m_enzymeblock_end.find())
                            {   
                       
                                break;
                            }
                        else
                            {   //System.out.println("+++ ++++ +++");//
                                newenzyme = ParseEnzymeLine(currentline, newenzyme);              
                            }
                    }
                // String formula is parsed to a tree of operations
                //
            }   
        catch(IOException e)
            { e.printStackTrace();
            }               

        // Finally, process the FormulaDescriptors to a traversable tree structure, if 
        // a non-predescribed formula is used;        
        newenzyme.ParseFormulaDescriptors();    

        System.out.println("New Enzyme found - Index " + newenzyme.get_Index() + ".");
        System.out.println("The  name of the enzyme is " + newenzyme.get_EnzymeName() + ".");
        System.out.print("The Enzyme's reactions: ");
        
        double[] testarray = newenzyme.get_ReactionArray();
        int n= testarray.length;
        for(int i=n; i>0; i--)
            {   System.out.print(testarray[n-i] + " / ");
            }
        System.out.println("\nConstants:");
        List<Constant> testconstants = new ArrayList<Constant>();        
        testconstants = newenzyme.get_Constants();       
        n=testconstants.size();
        Constant testconstant;        
        for(int i=n; i>0; i--)
            {   testconstant = testconstants.get(i-1);
                System.out.println(testconstant.get_ConstantName() + " = " + testconstant.get_ConstantValue());
            }              
        System.out.println("Kinetic Characteristics of this enzyme:");
        List<KinChar> testkinchars = new ArrayList<KinChar>();        
        testkinchars = newenzyme.get_KinChars();
        n=testkinchars.size();
        KinChar testkinchar;  
        //        System.out.println(n + "!!!!!!!");//
        for(int i=n; i>0; i--)
            {   testkinchar = testkinchars.get(i-1);
                System.out.println(testkinchar.get_FormulaDescriptor());
            }  
        System.out.println("*++++++++++++++++++++++++++++++++++++++++++++\n");
        return newenzyme;
    }

    private Enzyme ParseEnzymeLine(String currentline, Enzyme newenzyme)
    {      
        Matcher m_ident_begin = ident_begin.matcher(currentline);
        Matcher m_ident_end = ident_end.matcher(currentline);
        Matcher m_index_begin = index_begin.matcher(currentline);     
        Matcher m_index_end = index_end.matcher(currentline);     
        Matcher m_ReactionArray_begin = ReactionArray_begin.matcher(currentline);     
        Matcher m_ReactionArray_end =  ReactionArray_end.matcher(currentline);     
        // final Matcher m_conc_begin=  conc_begin.matcher(currentline);     
        // final Matcher m_conc_end = conc_end.matcher(currentline);        
        Matcher m_const_begin =  const_begin.matcher(currentline);     
        Matcher m_const_end = const_end.matcher(currentline);     
        Matcher m_kin_begin = kin_begin.matcher(currentline);     
        Matcher m_kin_end = kin_end.matcher(currentline);     
        Matcher m_form_begin = form_begin.matcher(currentline); 
        Matcher m_form_end= form_end.matcher(currentline);
        // Matcher m_cond_begin = cond_begin.matcher(currentline); 
        // Matcher m_cond_end = cond_end.matcher(currentline);
        // Matcher m_val_begin = val_begin.matcher(currentline); 
        // Matcher m_val_end = val_end.matcher(currentline);
        // Buffers used for extending existing Constant and KinChar Lists of the enzyme

     

        //  parse the currentline and put it into data structures
        ArrayList<Constant> newenzconsts = new ArrayList<Constant>(6); 
        ArrayList <KinChar> newenzkinchars = new ArrayList<KinChar>(5);
        if (m_index_begin.find() && m_index_end.find()    )  
            {   newenzyme.set_Index(IndexFromCurrentLine(currentline));   
            }        
        else if (m_ident_begin.find() && m_ident_end.find()           )
            {   newenzyme.set_EnzymeName(NameFromCurrentLine(currentline));
            }        
        else if (m_ReactionArray_begin.find() && m_ReactionArray_end.find())
            {   newenzyme.set_ReactionArray(ReactionArrayFromCurrentLine(currentline), AllSubstrates.size());
                // Reaction arrays are transformed to Reactor
                // Objects, that contain only releavant Substrate
                // References (i.e. ref != 0);                
                newenzyme.ReactionArrayToReactors(AllSubstrates);
            }
        else if (m_const_begin.find() && m_const_end.find())
            {   // addtolist
                Constant newconstant = new Constant();
                newenzconsts = newenzyme.get_Constants();                
                newconstant = ConstantFromCurrentLine(currentline);
                newenzconsts.add(newconstant);
                newenzyme.set_Constants(newenzconsts);
            }
        else if (m_kin_begin.find()  && m_kin_end.find() )
            {   // add ; 
                // add();              
                KinChar newkinchar = new KinChar();
                newenzkinchars = newenzyme.get_KinChars();           
                newkinchar = KinCharFromCurrentLine(currentline);
                newenzkinchars.add(newkinchar);
                newenzyme.set_KinChars(newenzkinchars);                
            }
    
        return newenzyme;
    }        

    private int IndexFromCurrentLine(String currentline)
    {   //  returns the index of a player, currentline is an uncommented line in the substrate list
        int index=-1;       
        String[] splitstring;                

        final Matcher m_index_begin = index_begin.matcher(currentline);
        final Matcher m_index_end = index_end.matcher(currentline);   

        if (m_index_begin.find() && m_index_end.find()) 
            {
                splitstring = index_begin.split(currentline);    
                splitstring = index_end.split(splitstring[1]);
                
                try
                    {   index = Integer.parseInt(splitstring[0]);
                    }
                
                catch(NumberFormatException ex) 
                    {   System.err.println("An error occured while parsing the index  ...");
                        ex.printStackTrace();
                    }     
            }   
        else 
            {   System.out.println("The index could not be set. ");
            }
        //        System.out.println(index);
        return index;
    } //end IndexFromCurrenLine
    
    private String NameFromCurrentLine(String currentline)
    {   //returns the Name of Substrate or Enzyme in a "naked" fileline
        // called by the <ident> tag
        String Name;
        String[] splitstring;
        final Matcher m_ident_begin = ident_begin.matcher(currentline);
        final Matcher m_ident_end = ident_end.matcher(currentline);
        if (m_ident_begin.find() && m_ident_end.find())
            {   splitstring = ident_begin.split(currentline);
                splitstring = ident_end.split(splitstring[1]);             
                Name = splitstring[0];
            } else
            {   System.out.println("The Name of this molecule could not be read : Please specify a name now:");
                Name = UsrInteraction.AddInputByUser();                
            }
        return Name;
    }   //end NameFromCurrentLine

    private String ConstNameFromCurrentLine(String currentline)
    {  // gets the names of constants in <name> tags
        String Name;
        String[] splitstring;
        final Matcher m_name_begin = name_begin.matcher(currentline);
        final Matcher m_name_end = name_end.matcher(currentline);
        if (m_name_begin.find()) 
            {   splitstring = name_begin.split(currentline);
                splitstring = name_end.split(splitstring[1]);             
                Name = splitstring[0];
            } else
            {   System.out.println("The Name of a constant of this Enzyme could not be read - Specify a name now:");
                Name = UsrInteraction.AddInputByUser();                
            }
        return Name;
    }   //end NameFromCurrentLine    
    
    private double ConcentrationFromCurrentLine(String currentline)
    {   // returns the initial Concentration of a Substrate regarding the
        // given input
        double Concentration=-1;
        String[] splitstring;
        final Matcher m_conc_begin = conc_begin.matcher(currentline);
        final Matcher m_conc_end = conc_end.matcher(currentline);
        if (m_conc_begin.find() && m_conc_end.find())
            {   splitstring = conc_begin.split(currentline);
                splitstring = conc_end.split(splitstring[1]);
                try
                    {  Concentration = Double.parseDouble(splitstring[0]);
                    }
                catch(NumberFormatException ex) 
                    {   System.err.println("An error occured while parsing the information about initial concentraion");
                        ex.printStackTrace();
                    }
            } else
            {    System.out.println("Substrate" + "without a proper corresponding proper name: Please give me an initial value in [microM]:");
                try
                    {  Concentration = Integer.parseInt(UsrInteraction.AddInputByUser());
                    }
                catch(NumberFormatException ex)
                    {  System.err.println("An error occured while parsing the information about initial concentraion");
                        ex.printStackTrace();
                    }
            }
        return Concentration;
    } // end ConcentrationFromCurrentLine
    
    private double DeltaGFromCurrentLine(String currentline)
    {   // returns the standard free enthalpy of a substrate 
        // if these data exist
        double DeltaG=0;
        String[] splitstring;
        Matcher m_deltag_begin = deltag_begin.matcher(currentline);
        Matcher m_deltag_end = deltag_end.matcher(currentline);         
        if (m_deltag_begin.find() && m_deltag_end.find())            
            {   splitstring = deltag_begin.split(currentline);
                splitstring = deltag_end.split(splitstring[1]);            
                try
                    {   DeltaG = Double.parseDouble(splitstring[0]);
                    }
                catch(NumberFormatException ex)
                    {   System.err.println("An error occured while parsing the information about the standard enthalpy of formation");
                        ex.printStackTrace();
                    }
            }        
        return  DeltaG;
    } //end DeltaFromCurrentLine
    

    private double[] ReactionArrayFromCurrentLine(String currentline){

        double[] ReactionArray; 
        Matcher m_ReactionArray_begin = ReactionArray_begin.matcher(currentline);
        Matcher m_ReactionArray_end = ReactionArray_end.matcher(currentline);
      
        int length = 0;
        String[] splitstring;
        if (m_ReactionArray_begin.find() && m_ReactionArray_end.find())
            {   splitstring = ReactionArray_begin.split(currentline);
                splitstring = ReactionArray_end.split(splitstring[1]);
               
                splitstring =  ArraySep.split(splitstring[0]);                
                for (int i=splitstring.length; i>0 ;i-- )
                    {    
                        if ((splitstring[i-1] != "") && (splitstring[i-1] != null) )
                            {  length++;
                            }
                    }
                try
                    {                       
                        ReactionArray = new double[length];
                        // Does avoiding the doubled loop with hashes make sense?
                        for (int i=splitstring.length; i>0; i--)
                            {                             
                                ReactionArray[length-1]=Double.parseDouble(splitstring[i-1]);
                                length--;
                            }
                    }
                catch(NumberFormatException ex)
                    {   ReactionArray= new double[0];
                        System.err.println("\nAn error occured while parsing the reaction array");
                        ex.printStackTrace();
                    }
            }
        
        else 
            {
                ReactionArray= new double[0];
                System.out.println("\n");
            }      
        return ReactionArray;
    }


    private Constant ConstantFromCurrentLine(String currentline)
    {
        Constant myconstant = new Constant();
        String constdata;
        String constname = "no_name";
        double constvalue = 0;
        String[] splitstring;
        Matcher m_const_begin = const_begin.matcher(currentline);
        Matcher m_const_end = const_end.matcher(currentline);
        if (m_const_begin.find() && m_const_end.find())
            {   splitstring = const_begin.split(currentline);
                splitstring = const_end.split(splitstring[1]);
                constdata = splitstring[0];
                // read the constant's name
                Matcher m_name_begin = const_begin.matcher(currentline);
                Matcher m_name_end = const_end.matcher(currentline);
                if (m_name_begin.find() && m_name_end.find())
                    {   splitstring = name_begin.split(constdata);
                        splitstring = name_end.split(splitstring[1]);
                        constname = splitstring[0];
                        myconstant.set_ConstantName(constname);
                    }                 
                else                       
                    {   System.out.println("The name of a constant could not be read.");
                    }
                // read the constant's value
                Matcher m_val_begin = val_begin.matcher(currentline);
                Matcher m_val_end = val_end.matcher(currentline);
                if (m_val_begin.find() && m_val_end.find())
                    {   splitstring = val_begin.split(constdata);
                        splitstring = val_end.split(splitstring[1]);
                        try
                            {       
                                constvalue = Double.parseDouble(splitstring[0]);
                                myconstant.set_ConstantValue(constvalue);
                            }                 
                        catch(NumberFormatException ex)
                            {   System.err.println("An error occured while parsing the value of the constant" + constname);
                                ex.printStackTrace();
                            }
                    }
                else                       
                    {   System.out.println("The value of " + constname + "  could not be read.");    
                    }                    
            }        
        System.out.println(myconstant.get_ConstantName());
        System.out.println(myconstant.get_ConstantValue());
        return myconstant;
    }

    private KinChar KinCharFromCurrentLine(String currentline)
    {   KinChar mykinchar = new KinChar();
        String FormDescPlusCond = "Katzenklo";
        String[] splitstring;
        Matcher m_kin_begin = kin_begin.matcher(currentline);
        Matcher m_kin_end = kin_end.matcher(currentline);     
        
        if (m_kin_begin.find() && m_kin_end.find() ) 
            {   splitstring = kin_begin.split(currentline);
                splitstring = kin_end.split(splitstring[1]);             
                FormDescPlusCond  = splitstring[0];
            } else
            {   System.out.println("The kinetic information of this molecule could not be parsed.");
            }    
        
        mykinchar.set_FormulaDescriptor(createFormulaDescriptor(FormDescPlusCond));
        mykinchar.set_ConditionDescriptor(createConditionDescriptor(FormDescPlusCond));
     
       
        return mykinchar;
    }    
    
    private String createFormulaDescriptor(String FormDescPlusCond)
    {
        Matcher m_form_begin = form_begin.matcher(FormDescPlusCond);
        Matcher m_form_end = form_end.matcher(FormDescPlusCond);
        String[] splitstring;
        String FormulaDescriptor = "";
        
        if (m_form_begin.find() && m_form_end.find() ) 
            {   splitstring = form_begin.split(FormDescPlusCond);
                splitstring = form_end.split(splitstring[1]);             
                FormulaDescriptor  = splitstring[0];
            } else
            {   System.out.println("The formula information of this molecule could not be read.");
            }        
        return FormulaDescriptor;        
    }
    
    private String createConditionDescriptor(String FormDescPlusCond) 
    {   String CondDesc = "";
        String[] splitstring;
        final Matcher m_cond_begin = cond_begin.matcher(FormDescPlusCond);
        final Matcher m_cond_end = cond_end.matcher(FormDescPlusCond);
        if (m_cond_begin.find() && m_cond_end.find() ) 
            {   splitstring = cond_begin.split(FormDescPlusCond);
                splitstring = cond_end.split(splitstring[1]);
                CondDesc = splitstring[0];
            } else
            { //  System.out.println("The Condition of this Kinectic Characteristicum could not be read.\nThis doesn't matter, if the enzyme is characterized by only one single kinetic formula\nthatwill be applied in all cases."); // To be added together with condition feature
            }     
        return CondDesc;
    }

} // end FileOpener