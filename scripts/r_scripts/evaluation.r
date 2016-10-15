
rm(list=ls())
library(plyr)
library(pwr)

#----------- parameters ----------------------------

args <- commandArgs(TRUE)
if (length(args)==0) {
  stop("At least one argument must be supplied", call.=FALSE)
}

granularities = c('B','P','S')
prediction_folder = 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output'
goldset_folder = 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output'
out_folder = 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output/eval'

prediction_folder = args[1]
goldset_folder = args[2]
out_folder = args[3]
granularities = unlist(strsplit(args[4], ","))

cat('Processing:',prediction_folder,'\n')

set.seed(644480808)

##---------- functions ---------------------------------
compute_sample_size <- function (population_size){
  population_size
}

compute_sample_size2 <- function (population_size){
  
  prob = 0.5
  error = 0.05
  confidence_level = 1-error
  z_val = qnorm(confidence_level + error/2)
  
  sample_size = z_val^2 * prob * (1-prob) / error^2  #nomal_distribution
  sample_size = ceiling(sample_size/(1+((sample_size-1)/population_size))) #correction for finite population
  
  #cat('Sample size:',population_size,'->',sample_size,'\n')
  
  sample_size
  
}

compute_numbers <- function (data1, cols){
  
  new_cols = c(cols,"system", "bug_id", "instance_id")
  
  filtered_data = data1[,(names(data1) %in% new_cols)]
  colnames(filtered_data) <- c("system", "bug_id", "instance_id", "gs", "pr")
  
  tp_data = subset(filtered_data, pr=="x" & gs =='x')
  tn_data = subset(filtered_data,  pr=="" & gs =='')
  fp_data = subset(filtered_data, pr=="x" & gs =='')
  fn_data = subset(filtered_data, pr=="" & gs =='x')
  
  #stats
  
  tp = nrow(tp_data)
  tn = nrow(tn_data)
  fp = nrow(fp_data)
  fn = nrow(fn_data)
  
  #---------------------------
  
  if (tp!=0) {
    tp_data$type ="tp"
  } 
  if (tn!=0) {
    tn_data$type ="tn"
  } 
  if (fp!=0) {
    fp_data$type ="fp"
  } 
  if (fn!=0) {
    fn_data$type ="fn"
  }
  
  #---------------------------
  #sample for revision
  
  num_sam = compute_sample_size(fp)
  if (fp <num_sam) {
    num_sam = fp
  }
  fp_data_sample = fp_data[sample(1:nrow(fp_data), num_sam),]
  num_sam = compute_sample_size(fn)
  if (fn <num_sam) {
    num_sam = fn
  }
  fn_data_sample = fn_data[sample(1:nrow(fn_data), num_sam),]
  
  #-------------------------
  
  all_data = rbind(tp_data, tn_data, fp_data, fn_data)
  all_data_sample = rbind(fp_data_sample, fn_data_sample)
  
  list("stats" = c(tp, tn, fp, fn), "data" = all_data, "data_sample" = all_data_sample)
  
}

##------------------------------------------------------------

compute_stats <- function(data2){
  
  b = compute_numbers(data2, c('is_eb.x','is_eb.y' ))
  c = compute_numbers(data2, c('is_sr.x','is_sr.y' ))
  
  results = t(data.frame(b$stats, c$stats))
  results = cbind(c( "eb", "sr"), results)
  colnames(results) <- c("label", "tp", "tn", "fp", "fn")
  
  list("res_stats" = results, "data_eb" = b$data, "data_sr" = c$data, "data_eb_sample" = b$data_sample, "data_sr_sample" = c$data_sample)
}

compute_stats2 <- function(data2){
  
  b = compute_numbers(data2, c('is_eb.x','is_eb.y' ))
  c = compute_numbers(data2, c('is_sr.x','is_sr.y' ))
  
  results = t(data.frame(b$stats, c$stats))
  results = cbind(c( "eb", "sr"), results)
  colnames(results) <- c("label", "tp", "tn", "fp", "fn")
  
  results
}

##----------------------------------------------------------------------

for (granularity in granularities) {
  
    cat('Processing granularity:',granularity,'\n')
   
    pr_file= paste(prediction_folder,'/output-prediction-',granularity,'.csv', sep = "")
    gs_file= paste(goldset_folder,'/gold-set-',granularity,'.csv', sep = "")
    
    #---------------------------------------
    
    out_file_all =paste(out_folder,'/stats_all_',granularity,'.csv', sep = "")
    out_file_all_eb =paste(out_folder,'/data_all_eb_',granularity,'.csv', sep = "")
    out_file_all_sr =paste(out_folder,'/data_all_sr_',granularity,'.csv', sep = "")
    out_file_all_eb_sample =paste(out_folder,'/data_all_eb_sample_',granularity,'.csv', sep = "")
    out_file_all_sr_sample =paste(out_folder,'/data_all_sr_sample_',granularity,'.csv', sep = "")
    out_file_sys =paste(out_folder,'/stats_sys_',granularity,'.csv', sep = "")
    
    #---------------------------------------
    
    gold_set = read.csv(gs_file, sep = ";", header = TRUE, colClasses=c(rep("factor",6)))
    prediction_set = read.csv(pr_file, sep = ";", header = TRUE, colClasses=c(rep("factor",6)))
    
    #---------------------------------------
    
    overlap = merge(gold_set, prediction_set, by=c("system","bug_id","instance_id"))
    
    cols = c('is_eb.x','is_sr.x' )
    filtered_data = overlap[,(names(overlap) %in% cols)]
    a = summary(filtered_data)
    t(a)
    
    cols = c('is_eb.y','is_sr.y' )
    filtered_data = overlap[,(names(overlap) %in% cols)]
    a = summary(filtered_data)
    t(a)
    
    #-----------------------------------

    all_data_stats = compute_stats(overlap)
    sys_data_stats = ddply(overlap, .(system), compute_stats2)
    
    write.table(all_data_stats$res_stats, out_file_all, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    write.table(all_data_stats$data_eb, out_file_all_eb, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    write.table(all_data_stats$data_sr, out_file_all_sr, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    write.table(sys_data_stats, out_file_sys, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    
    #-----------------------------------
    write.table(all_data_stats$data_eb_sample, out_file_all_eb_sample, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    write.table(all_data_stats$data_sr_sample, out_file_all_sr_sample, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    
    #-----------------------------------
}

cat('Done!\n')