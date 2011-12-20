### config png output
#set terminal png size 1024,768 nocrop enhanced font "Arial,13" truecolor       # gnuplot recommends setting terminal before output
#set output 'box_quer1.png' 


### config the output layout 
# set the title of the plot
set title "Query 3 - 10k"
# x-axis labeling should be done automatically
set auto x
# rotated the tics on the x-axis by 45 degrees
# set xtic rotate by -45 scale 0
# set y-axis range
set yrange [000.1:600] # noreverse nowriteback
# logarithmic y-axis
# set log y
# set the position of the legend
set key left top


### config the graphtype specific layout
# plot the data as a boxplot
set style data boxplot 
set style boxplot outliers separation 0.2 labels x 
# width of the boxes
set boxwidth 0.1
# solid: alpha-channel | border: -2(no) -1 (black) 0 (dashed) 1(red), ...
set style fill solid 1 border -1
# keine Legende anzeigen
# unset key


### plot data
# define missing values. should not happen
set datafile missing '-'
# plot the boxplot
plot for [i=1:6] 'boxdata3.dat' using (i*0.5):i ti col


# pause until ENTER
# pause -1
# pause for 5 seconds
pause -1

