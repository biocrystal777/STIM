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

my $KPTSa1 = 3082.3; # all in mM or without dimension
my $KPTSa2 = =0.01;  # 


# steady-state concentrations

