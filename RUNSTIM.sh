#/!usr/bin/bash

# this script is used to create a simulation with an inputfile "*.stim"
# transfer the output to a gnuplot-readable format and create a gnuplot image
# of these data; the only parameter given to this script is the input file itself.


function help()

{
    echo "help has to be added"
}

# parameter evaluation

if [ ! "$1" = "" ] && [ ! $1 = "-h" ]
then
    FILENAME=$1
else
    echo "You must specify a stim-file to process"
    exit
fi

if [ "$1" = "-h" ]
then
    help
fi 

if [ ! "$2" = "" ] 
then
    LIMIT=$2
    CHOPL="1"
    else 
    LIMIT="1000"
    CHOPL="0"
fi

NAMEPART=`echo $FILENAME | cut -f 1 -d "."`

GNPLNAME=${NAMEPART}.gnpl

# remove existing files
if [ -f ${NAMEPART}.firstoutput ]
then
    echo "Old ${NAMEPART}.firstoutput is removed.\n" 
    rm ${NAMEPART}.firstoutput
fi

if [ -f ${NAMEPART}.log ]
then    
    rm ${NAMEPART}.log
    echo "Old ${NAMEPART}.log is removed.\n" 
fi

if [ -f ${NAMEPART}.dat ]
then
    rm ${NAMEPART}.dat
    echo "Old ${NAMEPART}.dat is removed.\n"
fi

if [ -f ${NAMEPART}.png ]
then
    rm ${NAMEPART}.png
    echo "Old ${NAMEPART}.png is removed.\n"
fi

if [ -f GNPLNAME ]
then
    rm $GNPLNAME
    echo "Old $GNPLNAME is removed.\n"
fi 

# simulation

java STIM -i $FILENAME > "${NAMEPART}.firstoutput"

# seperate log and data and create the gnuplot file
if [ '$CHOPL' = "1" ] 
then
    ./chopper.pl firstoutput $LIMIT
else
    ./chopper.pl ${NAMEPART}.firstoutput
fi
rm "${NAMEPART}.firstoutput"
# run gnuplot 
chmod 777 $GNPLNAME
gnuplot <./$GNPLNAME
eog ${NAMEPART}.png &

