import os		# for list of files
import subprocess


class DataRecord:
	DataSet = ""			# deu_news_2009_10k  100k  300k   1M
	QueryNumber = 0			# 1 , 2 , 3
	QueryLanguage = ""		# mysql, neo4japi, neo4jc, neo4jt, dexapi, orientapi
	Data = []				# 


# generate the gnuplot-Boxplot-Skripts
def generateBoxPNG(query_num, db_name):
	fgnu = open("./boxdata_q" + str(query_num) + "_" + db_name + ".gnu", 'w')
	
	gp = ""
	gp += "### config png output" + "\n"
	gp += "set terminal png size 1024,768 nocrop font 'Arial,13' truecolor       # gnuplot recommends setting terminal before output" + "\n"
	gp += "set output 'q" + str(query_num) + "_" + db_name + ".png'"  + "\n"
	gp += "### config the output layout" + "\n"
	gp += "# set the title of the plot" + "\n"
	gp += "set title 'Query " + str(query_num) + " - " + db_name + "'" + "\n"
	gp += "# x-axis scaling should be done automatically" + "\n"
	gp += "set auto x" + "\n"
	gp += "set ylabel 'time [msec]'"  + "\n"
	gp += "set xtics border in scale 0,0 nomirror norotate  offset character 0, 0, 0" + "\n"
	gp += "set xtics  norangelimit" + "\n"
	gp += "set xtics  ('' 0)" + "\n"
	gp += "# rotated the tics on the x-axis by 45 degrees" + "\n"
	gp += "# set xtic rotate by -45 scale 0" + "\n"
	gp += "# set y-axis range" + "\n"
	gp += "# set yrange [000.1:600] # noreverse nowriteback" + "\n"
	gp += "set auto y" + "\n"
	gp += "# logarithmic y-axis" + "\n"
	gp += "# set log y" + "\n"
	gp += "# set the position of the legend" + "\n"
	gp += "set key left top" + "\n"
	gp += "### config the graphtype specific layout" + "\n"
	gp += "# plot the data as a boxplot" + "\n"
	gp += "set style data boxplot" + "\n"
	gp += "set style boxplot outliers separation 0.2 labels x " + "\n"
	gp += "# width of the boxes" + "\n"
	gp += "set boxwidth 0.1" + "\n"
	gp += "# solid: alpha-channel | border: -2(no) -1 (black) 0 (dashed) 1(red), ..." + "\n"
	gp += "set style fill solid 1 border -1" + "\n"
	gp += "# keine Legende anzeigen" + "\n"
	gp += "# unset key" + "\n"
	gp += "### plot data" + "\n"
	gp += "# define missing values. should not happen" + "\n"
	gp += "set datafile missing '-'" + "\n"
	gp += "# plot the boxplot" + "\n"
	gp += "plot for [i=1:6] '" + "boxdata_q" + str(query_num) + "_" + db_name + ".dat" + "' using (i*0.5):i ti col" + "\n"
	
	fgnu.write(gp)
	fgnu.close()
	
	# execute gnuplot and produce png's
	subprocess.call(["gnuplot", "boxdata_q" + str(query_num) + "_" + db_name + ".gnu"])


def generateHistoPNG(query_num):
	fgnu = open("./histodata_q" + str(query_num) + ".gnu", 'w')
	
	gp = "### config png output" + "\n"
	gp += "set terminal png size 1024,768 nocrop font 'Arial,13' truecolor       # gnuplot recommends setting terminal before output" + "\n"
	gp += "set output 'histogram_query" + str(query_num) + ".png' " + "\n"
	gp += "### config the output layout " + "\n"
	gp += "# set the title of the plot" + "\n"
	gp += "set title 'Query " + str(query_num) + "'" + "\n"
	gp += "# x-axis labeling should be done automatically" + "\n"
	gp += "# set auto x" + "\n"
	gp += "# rotated the tics on the x-axis by 45 degrees" + "\n"
	gp += "# set xtic rotate by -45 scale 0" + "\n"
	gp += "# set y-axis range" + "\n"
	gp += "# set yrange [0:300]" + "\n"
	gp += "set auto y" + "\n"
	gp += "# set the position of the legend" + "\n"
	gp += "set key left top" + "\n"
	gp += "### config the graphtype specific layout" + "\n"
	gp += "# plot the data as a histogram" + "\n"
	gp += "set style data histogram" + "\n"
	gp += "# style of the historam should be a histogram with errorbars aka std_dev" + "\n"
	gp += "# gap 2 = 2 boxes-width space between clusters" + "\n"
	gp += "# set style histogram cluster gap 2 " + "\n"
	gp += "set style histogram errorbars gap 4 linewidth 1" + "\n"
	gp += "# width of the boxes" + "\n"
	gp += "set boxwidth 1.0" + "\n"
	gp += "# solid: alpha-channel | border: -2(no) -1 (black) 0 (dashed) 1(red), ..." + "\n"
	gp += "set style fill solid 1 border -1" + "\n"
	gp += "### plot data" + "\n"
	gp += "# define missing values. should not happen" + "\n"
	gp += "set datafile missing '-'" + "\n"
	gp += "# plot the histogram" + "\n"
	gp += "plot for [c=1:6] 'histodata_q" + str(q_num) + ".dat" +  "' using c*2:c*2+1:xticlabels(1)  ti col" + "\n"
	
	fgnu.write(gp)
	fgnu.close()
	
	# execute gnuplot and produce png's
	subprocess.call(["gnuplot", "histodata_q" + str(query_num) + ".gnu"])
	


################################################
# read out the data from a folder-structure 
################################################

databases = ["deu_news_2009_10k", "deu_news_2009_100k", "deu_news_2009_300k", "deu_news_2009_1M"]
allRecords = []

# iterate over all db sizes
for db in databases:
	# go into directory ./deu_news_2009_* and get all the directories
	if os.path.isdir("." + os.sep + db):
		for setting in os.listdir("." + os.sep + db):
			el = DataRecord()
			el.DataSet = db
			
			# get the query number
			if "query1" in setting or "query 1" in setting:
				el.QueryNumber = 1
			elif "query2" in setting or "query 2" in setting:
				el.QueryNumber = 2
			elif "query3" in setting or "query 3" in setting:
				el.QueryNumber = 3
	
			# get the database-language system
			if "mysql" in setting:
				el.QueryLanguage = "mysql"
			elif "neo4j" in setting and "cypher" in setting: 
				el.QueryLanguage = "neo4jc"
			elif "neo4j" in setting and "traverser" in setting: 
				el.QueryLanguage = "neo4jt"
			elif "neo4j" in setting and "native" in setting: 
				el.QueryLanguage = "neo4japi"
			elif "orientdb" in setting: 
				el.QueryLanguage = "orientapi"
			elif "dex" in setting: 
				el.QueryLanguage = "dexapi"
	
			# for every singel conducted benchmark, find the latest one (folder = timestamp)
			latest = ""
			for folder in os.listdir("." + os.sep + db + os.sep + setting):
				if folder > latest:
					latest = folder
			
			# read out the latest folder --> there should be one file
			benchFile = os.listdir("." + os.sep + db + os.sep + setting + os.sep + latest)[0]
			f = open("." + os.sep + db + os.sep + setting + os.sep + latest + os.sep + benchFile , 'r')
			# one column of numbers, split them by line break and gat a list of values
			timeList = f.read().split("\n")
			# remove first element. is always '' and store the data
			el.Data = timeList[1:]
			f.close()
			
			allRecords.append(el)



################################################
# rearrange the data  and  make Boxplots
################################################

numRows = len(allRecords[0].Data)

# iterate over all database names
for db in databases:
	# test on all query numbers
	for q_num in range(1,4):
		# array of rows. initialize them with empty lists to append values
		outData = []
		for i in range(0, numRows+1): 
			outData.append([])
		
		# check on all dataSets
		for ds in allRecords:
			
			# test if current dataset belongs to the iterated db
			if ds.DataSet == db:
				
				# create the corresponding gnu-plot data file
				fout = open("./boxdata_q" + str(q_num) + "_" + ds.DataSet + ".dat", 'w')
				fout.write("# Data generated by mergeData.py \n")
				
				# if dataset belongs to this query-number
				if ds.QueryNumber == q_num:
					# add the Query-Language as Header
					outData[0].append(ds.QueryLanguage)
					# add the data
					for i in range(0, numRows):
						# Test, if value exists, if not, append "-"
						try:
							outData[i+1].append( ds.Data[i] )
						except:
							outData[i+1].append( "-" )
					
					for oneline in outData:
						for ele in oneline:
							fout.write( "%11s" % str(ele) )
						fout.write("\n")
	
					fout.write("\n")
					generateBoxPNG(q_num, ds.DataSet)
					fout.close


######################################################
# rearrange the data  and  make Histogram-Plots
######################################################

# iterate over all database names
# test on all query numbers
for q_num in range(1,4):
	# array of rows. initialize
	outData = [ ["Dataset"],["10k"],["100k"],["300k"],["1M"] ]
	# create the corresponding gnu-plot data file
	fout = open("./histodata_q" + str(q_num) + ".dat", 'w')
	fout.write("# Data generated by mergeData.py \n")
					
	for db in databases:
		# check on all dataSets
		for ds in allRecords:
			# test if current dataset belongs to the iterated db
			if ds.DataSet == db:
				# if dataset belongs to this query-number
				if ds.QueryNumber == q_num:
				
					if db == "deu_news_2009_10k":
						# add the Query-Language as Header
						outData[0].append(ds.QueryLanguage)
						# add the Query-Language as Header
						outData[0].append(ds.QueryLanguage)
						
						outData[1].append( 10 )
						outData[1].append( 11 )
					elif db == "deu_news_2009_100k":
						outData[2].append( 100 )
						outData[2].append( 101 )
					elif db == "deu_news_2009_300k":
						outData[3].append( 300 )
						outData[3].append( 301 )
					elif db == "deu_news_2009_1M":
						outData[4].append( 1000 )
						outData[4].append( 1001 )
					
	for oneline in outData:
		for ele in oneline:
			fout.write( "%11s" % str(ele)  )
		fout.write("\n")

	fout.write("\n")
	generateHistoPNG(q_num)
	fout.close




