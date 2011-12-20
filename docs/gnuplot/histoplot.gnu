### config png output
set terminal png size 1024,768 nocrop enhanced font "Arial,13" truecolor       # gnuplot recommends setting terminal before output
set output 'histogram_quer1.png' 


### config the output layout 
# set the title of the plot
set title "Query 1"
# x-axis labeling should be done automatically
# set auto x
# rotated the tics on the x-axis by 45 degrees
# set xtic rotate by -45 scale 0
# set y-axis range
set yrange [0:300]
# set the position of the legend
set key left top
# bottom margin
# set bmargin 3


### config the graphtype specific layout
# plot the data as a histogram
set style data histogram
# style of the historam should be a histogram with errorbars aka std_dev
# gap 2 = 2 boxes-width space between clusters
# set style histogram cluster gap 2 
set style histogram errorbars gap 4 linewidth 1
# width of the boxes
set boxwidth 1.0
# solid: alpha-channel | border: -2(no) -1 (black) 0 (dashed) 1(red), ...
set style fill solid 1 border -1


### plot data
# define missing values. should not happen
set datafile missing '-'
# plot the histogram
plot for [c=1:8] 'histo.dat' using c*2:c*2+1:xticlabels(1)  ti col

# pause until ENTER
# pause -1
# pause for 5 seconds
# pause -1




