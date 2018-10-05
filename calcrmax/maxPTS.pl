#!/usr/bin/perl


# chopper seperates the file log and the data part of
# the STIM output and creates a gnuplot file with these values;
# chopper can create an image with up to 12 plots of different colors; if there are more than 12 important lines

use warnings;
use strict;

#subroutines



# main program

my $Enzymename = "PTS";
my $ri = 100; # flux balance value
my $rmax=0; # target value

# constant optimized Parameters

my $KPTSa1 = 1; # all in mM or without dimension
my $KPTSa2 = 0.01;  # 
my $KPTSa3 = 1;
my $KPTSg6p = 0.5;
my $nPTSg6p = 4;

# steady-state concentrations in mM

my $Cexglc = 0.0556;
my $Cpep = 2.67;
my $Cpyr = 2.67;
my $Cg6p = 3.48;

# intermediate results;
my $peppyr = $Cpep / $Cpyr;
my $a = $KPTSa1 + ($KPTSa2 * $peppyr) +( $KPTSa3 * $Cexglc) + ($Cexglc * $peppyr);
my $b =  1 + (($Cg6p ** $nPTSg6p) / $KPTSg6p);
my $c = $Cexglc * $peppyr;

my $factor = $c / ($b * $a);
$rmax = $ri / $factor;

#final results, appending the result rmax to file.
print ("factor : " . $factor);
print ("rmax of " . $Enzymename .  " is  " . $rmax . "\n");
print ("Want to append to file (y/n)?\n");
my $resp =  <stdin>;
if ($resp eq "y"){
    open(DATFILE,"> rmaxvals");
    print DATFILE ("rmax of " . $Enzymename .  " is  " . $rmax . "\n");
    close (DATFILE);
}
