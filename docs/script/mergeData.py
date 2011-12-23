import os			# for list of files and folders
import subprocess	# to start gnuplot
import math			# for sqrt

BENCHDIR = "../../out/benchmarks"		# the base-dir, where the Benchmarks are located. no / at the end
LATEXPICDIR = "../report/pics/"			# the dir, where the pics should be saved. must have a / at the end
DATABASES = ["deu_news_2009_10k", "deu_news_2009_100k", "deu_news_2009_300k", "deu_news_2009_1M"]
QUERYLANG = ["mysql", "neo4japi", "neo4jc", "neo4jt", "dexapi", "orientapi"]

class DataRecord:
	DataSet = ""			# deu_news_2009_10k  100k  300k   1M
	QueryNumber = 0			# 1 , 2 , 3;  0 means probably "import"
	QueryLanguage = ""		# mysql, neo4japi, neo4jc, neo4jt, dexapi, orientapi
	Data = []				# stores the time in ms for each single query
	


# generate the gnuplot-Boxplot-Skripts
def generateBoxPNG(query_num, db_name):
	if os.path.isdir(LATEXPICDIR) == False:
		os.mkdir(LATEXPICDIR)
	
	fgnu = open("./boxdata_q" + str(query_num) + "_" + db_name + ".gnu", 'w')
	
	gp = ""
	gp += "### config png output" + "\n"
	gp += "set terminal png size 1024,768 nocrop font 'Arial,13' truecolor" + "\n"
	gp += "set output '" + LATEXPICDIR + "q" + str(query_num) + "_" + db_name + ".png'"  + "\n"
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
	gp += "# define missing values." + "\n"
	gp += "set datafile missing 'NaN'" + "\n"
	gp += "# plot the boxplot" + "\n"
	gp += "plot for [i=1:6] '" + "boxdata_q" + str(query_num) + "_" + db_name + ".dat" + "' using (i*0.5):i ti col" + "\n"
	
	fgnu.write(gp)
	fgnu.close()
	
	# execute gnuplot and produce png's
	# subprocess.call(["gnuplot", "boxdata_q" + str(query_num) + "_" + db_name + ".gnu"])
	


def generateHistoPNG(query_num):
	if os.path.isdir(LATEXPICDIR) == False:
		os.mkdir(LATEXPICDIR)
	
	fgnu = open("./histodata_q" + str(query_num) + ".gnu", 'w')
	
	gp = "### config png output" + "\n"
	gp += "set terminal png size 1024,768 nocrop font 'Arial,13' truecolor       # gnuplot recommends setting terminal before output" + "\n"
	gp += "set output '" + LATEXPICDIR + "histogram_query" + str(query_num) + ".png' " + "\n"
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
	gp += "set datafile missing 'NaN'" + "\n"
	gp += "# plot the histogram" + "\n"
	gp += "plot for [c=1:6] 'histodata_q" + str(q_num) + ".dat" +  "' using c*2:c*2+1:xticlabels(1)  ti col" + "\n"
	
	fgnu.write(gp)
	fgnu.close()
	
	# execute gnuplot and produce png's
	# subprocess.call(["gnuplot", "histodata_q" + str(query_num) + ".gnu"])
	

def mean (values):
    return sum(values, 0.0) / len(values)
	

def stddev (values):
	m = mean(values)
	summe = 0
	for v in values:
		summe += (m-v) * (m-v)
	return math.sqrt(summe / len(values) )
	

################################################
# read out the data from a folder-structure 
################################################

allRecords = []

# iterate over all db sizes
for db in DATABASES:
	# go into directory ./deu_news_2009_* and get all the directories
	if os.path.isdir(BENCHDIR + os.sep + db):
		for setting in os.listdir(BENCHDIR + os.sep + db):
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
	
			# for every single conducted benchmark, find the latest one (folder = timestamp)
			latest = ""
			for folder in os.listdir(BENCHDIR + os.sep + db + os.sep + setting):
				if folder > latest:
					latest = folder
			
			# read out the latest folder --> there should be one file
			benchFile = os.listdir(BENCHDIR + os.sep + db + os.sep + setting + os.sep + latest)[0]
			f = open(BENCHDIR + os.sep + db + os.sep + setting + os.sep + latest + os.sep + benchFile , 'r')
			# one column of numbers, split them by line break and gat a list of values
			timeList = f.read().split("\n")
			# remove last element. is always '' and store the data
			el.Data = timeList[:-1]
			for i in range(0, len(el.Data)):
				el.Data[i] = int(el.Data[i])
			
			
			f.close()
			
			allRecords.append(el)
			
"""
for u in allRecords:
	print u.DataSet
	print u.QueryNumber 
	print u.QueryLanguage 
	print u.Data
exit()
"""

numRows = 0
for rec in allRecords:
	if len(rec.Data) > numRows:
		numRows = len(rec.Data)
	

################################################
# rearrange the data  and  make Boxplots
################################################

# iterate over all database names
for db in DATABASES:
	# test on all query numbers
	for q_num in range(1,4):
		# array of rows. initialize them with empty lists to append values
		outData = []
		for i in range(0, numRows+1): 
			outData.append([])
		atLeastOneLang = False
		
		for lang in QUERYLANG:
			foundsome = False
			# check on all dataSets
			for ds in allRecords:
				# test if current dataset belongs to the iterated db and if dataset belongs to this query-number
				if ds.DataSet == db and ds.QueryNumber == q_num and ds.QueryLanguage == lang:
					foundsome = True
					atLeastOneLang = True
					# add the Query-Language as Header
					outData[0].append(ds.QueryLanguage)
					# add the data
					for i in range(0, numRows):
						# Test, if value exists, if not, append "-"
						try:
							outData[i+1].append( ds.Data[i] )
						except:
							outData[i+1].append( "NaN" )
		
			if foundsome == False:
				outData[0].append(lang)
				for i in range(0, numRows):
					outData[i+1].append( "NaN" )
		
		if atLeastOneLang == True:
			# create the corresponding gnu-plot data file
			fout = open("./boxdata_q" + str(q_num) + "_" + db + ".dat", 'w')
			fout.write("# Data generated by mergeData.py \n")
			
			for oneline in outData:
				for ele in oneline:
					fout.write( "%11s" % str(ele) )
				fout.write("\n")
				
			fout.write("\n")
			fout.close
			
			generateBoxPNG(q_num, db)
					


######################################################
# rearrange the data  and  make Histogram-Plots
######################################################

# test on all query numbers
for q_num in range(1,4):
	# array of rows. initialize
	outData = [ ["Dataset"],["10k"],["100k"],["300k"],["1M"] ]
	atLeastOneElement = False
	# iterate over all database names
	for lang in QUERYLANG:
		# add the Query-Language as Header
		outData[0].append(lang)
		outData[0].append(lang)
				
		for db in DATABASES:
			foundsome = False
			
			ind = 99
			if db == "deu_news_2009_10k":
				ind = 1
			elif db == "deu_news_2009_100k":
				ind = 2
			elif db == "deu_news_2009_300k":
				ind = 3
			elif db == "deu_news_2009_1M":
				ind = 4
			
			# check on all dataSets
			for ds in allRecords:
				# test if current dataset belongs to the iterated db and if dataset belongs to this query-number
				if ds.DataSet == db and ds.QueryNumber == q_num and ds.QueryLanguage == lang:
					foundsome = True
					atLeastOneElement = True
					# add mean and stddev 
					outData[ind].append(mean(ds.Data))
					outData[ind].append(stddev(ds.Data))
					
			if foundsome == False:
				# add mean and stddev 
				outData[ind].append("NaN")
				outData[ind].append("NaN")
	
	if atLeastOneElement == True:
		# create the corresponding gnu-plot data file
		# create the corresponding gnu-plot data file
		fout = open("./histodata_q" + str(q_num) + ".dat", 'w')
		fout.write("# Data generated by mergeData.py \n")
		
		for oneline in outData:
			for ele in oneline:
				fout.write( "%20s" % str(ele) )
			fout.write("\n")
			
		fout.write("\n")
		fout.close
		
		generateHistoPNG(q_num)
		

#######################################
# execute all gnu-files
#######################################
# gives some gnuplot errors
# subprocess.call("gnuplot *.gnu", shell=True)
