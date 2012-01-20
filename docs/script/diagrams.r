# replace this to your needs
path <- "C:\\Install\\Devel\\Java\\Projects\\IR_15\\docs\\results\\cold caches\\";
dataset <- "10M"
query <- "3"

file <- paste(path, dataset, "_query", query, ".csv", sep="")

# create title and filenames
title <- paste("deu_news_2009_", dataset, " Query ", query, sep="")
bpFile <- paste(dataset, "_query", query,"_boxplot.pdf", sep="")
perfFile <- paste(dataset, "_query", query,"_perf.pdf", sep="")

# read data
data <- read.csv(file=file, sep=";", head=TRUE)

# create boxplot
boxplot(data, main=title,ylab="Time [ms]")
savePlot(filename=bpFile, type="pdf", device = dev.cur(), restoreConsole = TRUE)

# create line diagram
g_range <- range(0, data$MySQL, data$Neo4j, data$DEX)
plot(data$Neo4j, type="o", col="blue", ylim=g_range, xlab="#run", ylab="Time [ms]")
lines(data$MySQL, type="o", col="red", pch=22, lty=1)
lines(data$DEX, type="o", col="green", pch=23, lty=1)
title(main=title)
legend(1, g_range[2], c("Neo4j", "MySQL", "DEX"), cex=0.8, col = c("blue", "red", "green"), pch=21:23, lty=1)
savePlot(filename=perfFile, type="pdf", device = dev.cur(), restoreConsole = TRUE)

# use this when OrientDB is involved
g_range <- range(0, data$MySQL, data$Neo4j, data$DEX, data$OrientDB)
plot(data$Neo4j, type="o", col="blue", ylim=g_range, xlab="#run", ylab="Time [ms]")
lines(data$MySQL, type="o", col="red", pch=22, lty=1)
lines(data$DEX, type="o", col="green", pch=23, lty=1)
title(main=title)
lines(data$OrientDB, type="o", col="black", pch=24, lty=1)
legend(1, g_range[2], c("Neo4j", "MySQL", "DEX", "OrientDB"), cex=0.8, col = c("blue", "red", "green"), pch=21:24, lty=1)
savePlot(filename=perfFile, type="pdf", device = dev.cur(), restoreConsole = TRUE)














