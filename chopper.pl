#!/usr/bin/perl


# chopper seperates the file log and the data part of
# the STIM output and creates a gnuplot file with these values;
# chopper can create an image with up to 12 plots of different colors; if there are more than 12 important lines

use warnings;
use strict;

#subroutines

#openfile
my $graphlimit=1000; 
my $filename=" ";

sub openfile(){
    $filename=" ";    
    if(scalar(@ARGV)==0){
        die("You have to specify a file name!\n");               
    }
    elsif(scalar(@ARGV)==1){
#        print ("Enter the number of the last line:\n");
        $filename=$ARGV[0];        
        $graphlimit=10000000;
#        chomp($chopline);
    }
    else{
        $filename=$ARGV[0];
        $graphlimit=$ARGV[1];
    }
    open(INFILE,"<$filename") or die "unable to open " . $filename . ".\n";
}

######## main program
openfile();
$filename=$ARGV[0];
my $i=1;
my $line;
my $control = 0;
my @nameparts = split(/\./, $filename);
my $name = $nameparts[0];
#print ($name);
my $logname= $name.".log";
#print ($logname);
my $datname= $name.".dat";
my $gnplname = $name.".gnpl";
my $imagename = $name.".png";
my @plotlabels; # names for the plot lines of gnuplot
my $plottitle = " No Title";
my $plotnumbers = 0;
# 2be extended !
my @plotcolors= ("goldenrod", "#FF3333", "#9900FF", "#6600FF", "#0099FF", "#0000AA", "#009933", "#00FFCC", "#00FF00", "#668014	
 ", "#5E2605", "#000000");
my $colextend = "#000000";
for (my $e=200; $e>0; $e--){
    push(@plotcolors, $colextend);
}

open(LOGFILE, "> $logname") or die ("unable to open " . $filename . ".\n");
while($line= <INFILE>){    
    chomp($line);
    if ($line =~ "#!TITLE&")
    {
        $plottitle = substr($line, 8);        
    }    
    if ($line =~"COMPLETE_SUBSTRATE_LIST#"){
#        print ("found\n");
        @plotlabels = split(/ /, $line);        
        shift(@plotlabels);
        
    }        
    if ($line =~ "VVVVV"){
        $control = 1;
        last;
    }
#    print ($line."\n"); 
    print LOGFILE ($line."\n"); 
}

close(LOGFILE);

my $gnuflag = 0;
open(DATFILE,"> $datname");
open GNPLFILE,"> $gnplname";
while($line= <INFILE>){    
    #only once: prepare the mighty gnpl-file for gnuplot
    if ($gnuflag == 0){  
        print GNPLFILE ("#!/usr/bin/gnuplot\n");
        print GNPLFILE ("#This gnuplot command list was generated automatically from the chopper script\n");
        print GNPLFILE ('set xlabel " time / s "'."\n");
        print GNPLFILE ("set ylabel \"concentration / mM\"\n");
        print GNPLFILE ("set grid linecolor rgb \"#444444\"\n");
        print GNPLFILE ("set terminal png enhanced size 1600,1000 font \"LmRomanDemi10,32\" lw 3\n");
        print GNPLFILE ("set object rectangle \\\n");
        print GNPLFILE ("from screen 0,0 to screen 1,1 fillcolor rgb \"\#F5F5F5\" behind\n");
        print GNPLFILE ("set output \"".$imagename."\"\n");
#        print GNPLFILE ("set title \"".$plottitle."\"\n");
        $plotnumbers = @plotlabels;
#                           print ($plotnumbers);
        if ($plotnumbers > $graphlimit){
            $plotnumbers = $graphlimit;
        }
            
            
        print GNPLFILE ("plot ");
        my $i=0;
        for ($i=0; $i<$plotnumbers-1; $i++){
            print GNPLFILE ("\"".$datname."\" using 1:".($i+2)." with lines lw 2 linecolor rgb \"".$plotcolors[$i]."\" ti \"".$plotlabels[$i]."\",\\\n     ");
        }
        print GNPLFILE ("\"".$datname."\" using 1:".($plotnumbers+1)." with lines lw 2 linecolor rgb \"".$plotcolors[$i]."\" ti \"".$plotlabels[$plotnumbers-1]."\"");
            $gnuflag=1;  
    }       
    chomp($line);
    print DATFILE ($line."\n");       
    if ($control != 1){
        print("NO SIGNALLING LINE FOUND!");                
        die();
    }   
    
}
close (DATFILE);
close (GNPLFILE);
        
