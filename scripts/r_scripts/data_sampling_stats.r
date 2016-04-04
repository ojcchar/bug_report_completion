
rm(list=ls())
library(plyr)
library(pwr)

#---------------------------------------


input_file= 'C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/all_data.csv'
out_folder = 'C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/sample/'
out_file =paste(out_folder,'data_sample.csv', sep = "")
systems= c('eclipse' ,
           'facebook',
           'firefox',
           'httpd')


coders = c("oscar", "juan", "laura", "fiorella")
num_coders = length(coders)

set.seed(14568)

#---------------------------------------

data_all = read.csv(input_file, sep = ";", header = TRUE)

##data filtering
data_all = subset(data_all, (obs_behavior != "" | exp_behavior != "" | steps_2_repr != "")  ) #at least one type of information
data_all = subset(data_all, !(obs_behavior == "" & exp_behavior != "" & steps_2_repr == "")) #no change requests, only bugs

#general stats
cols <- c('project','issue_id', 'obs_behavior', 'exp_behavior', 'steps_2_repr', 'coder1', 'coder2' )
filtered_data = data_all[,(names(data_all) %in% cols)]
ddply(filtered_data, .(project), summary)
summary(data_all)

#---------------------------------------

out_file =paste(out_folder,'all_filtered.csv', sep = "")
write.table(filtered_data, out_file, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)

#---------------------------------------

sampling <- function (data1){
  
  #-----------------------
  #sample size
  
  prob = 0.5
  error = 0.05
  confidence_level = 1-error
  population = nrow(data1)
  z_val = qnorm(confidence_level + error/2)
  
  sample_size = z_val^2 * prob * (1-prob) / error^2  #nomal_distribution
  num_instances = ceiling(sample_size/(1+((sample_size-1)/population))) #correction for finite population
  num_ins_coder = num_instances / num_coders
  
  #if there is no equal number of instances among coders
  if ( (num_instances %% num_coders) !=0 ){
    num_instances  = num_instances + (num_instances %% num_coders)
    num_ins_coder = num_instances / num_coders  
  }
  
  
  #cat(num_instances, num_ins_coder, "\n")
  
  #--------------------------
  
  #get sample
  sample_data = data1[sample(1:nrow(data1), num_instances),]
  
  #assign coders
  sample_data$coder1 <- ""
  sample_data$coder2 <- ""
  for (eval in 1:num_coders){
    
    base= (eval-1)*num_ins_coder
    ini = base + 1
    end = base + num_ins_coder
    
    # cat(ini, end, "\n")
    
    sample_data[c(ini:end),]$coder1 = coders[eval]
    sample_data[c(ini:end),]$coder2 = coders[(eval %% num_coders)+1]
  }
  
  #return the sample
  sample_data
}

#call the sampling for each project
sample = ddply(data_all, .(project), sampling)

#coder factors
sample$coder1 <- as.factor(sample$coder1)
sample$coder2 <- as.factor(sample$coder2)

#----------------------------------------
#stats of the sample

filtered_data = sample[,(names(sample) %in% cols)]
ddply(filtered_data, .(project), summary)

nrow(filtered_data)
summary(filtered_data)


#---------------------------------------
#write data

cols <- c('project','issue_id', 'issue_url', 'coder1', 'coder2' )
filtered_data = sample[,(names(sample) %in% cols)]
write.table(filtered_data, out_file, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)

#--------------------------------------
#write individual data

for (eval in 1:num_coders){
  coder = coders[eval]
  
  data1 = subset(filtered_data, coder1 == coder) 
  data2 = subset(filtered_data, coder2 == coder) 
  
  data_coder = rbind(data1, data2)
  
  attach(data_coder)
  data_coder <- data_coder[order(project, issue_id),]
  detach(data_coder)
  
  cols <- c('project','issue_id', 'issue_url')
  data_coder = data_coder[,(names(data_coder) %in% cols)]
  out_file_coder =paste(out_folder,'sample_',coder,".csv", sep = "")
  write.table(data_coder, out_file_coder, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
}

