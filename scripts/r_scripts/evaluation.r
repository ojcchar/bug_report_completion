
rm(list=ls())
library(plyr)
library(pwr)

#---------------------------------------
granularities = c('B','P','S')

for (granularity in granularities) {
     
    
    gs_file= 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/all_data_only_bugs_coded_data.csv'
    pr_file= paste('C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output/output-prediction-',granularity,'.csv', sep = "")
    
    if(granularity != "B"){
      gs_file= paste('C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/gold-set-',granularity,'.csv', sep = "")
    }
    
    out_folder = 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output/'
    out_file_all =paste(out_folder,'stats_all_',granularity,'.csv', sep = "")
    out_file_sys =paste(out_folder,'stats_sys_',granularity,'.csv', sep = "")
    
    
    #---------------------------------------
    
    gold_set = read.csv(gs_file, sep = ";", header = TRUE)
    prediction_set = read.csv(pr_file, sep = ";", header = TRUE)
    
    
    
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
    
    #---------------------------------------
    
    compute_numbers <- function (data1, cols){
      
      filtered_data = data1[,(names(data1) %in% cols)]
      colnames(filtered_data) <- c("gs", "pr")
      
      tp = nrow(subset(filtered_data, pr=="x" & gs =='x'))
      tn = nrow(subset(filtered_data,  pr=="" & gs ==''))
      fp = nrow(subset(filtered_data, pr=="x" & gs ==''))
      fn = nrow(subset(filtered_data, pr=="" & gs =='x'))
      
      c(tp, tn, fp, fn)
      
    }
    
    
    compute_stats_all_labels <- function(data2){
      
      a = compute_numbers(data2, c('is_ob.x','is_ob.y' ))
      b = compute_numbers(data2, c('is_eb.x','is_eb.y' ))
      c = compute_numbers(data2, c('is_sr.x','is_sr.y' ))
      
      results = t(data.frame( a, b, c))
      results = cbind(c("ob", "eb", "sr"), results)
      colnames(results) <- c("label", "tp", "tn", "fp", "fn")
      
      results 
    }
    
    
    compute_stats <- function(data2){
      
      b = compute_numbers(data2, c('is_eb.x','is_eb.y' ))
      c = compute_numbers(data2, c('is_sr.x','is_sr.y' ))
      
      results = t(data.frame( b, c))
      results = cbind(c( "eb", "sr"), results)
      colnames(results) <- c("label", "tp", "tn", "fp", "fn")
     
      results 
    }
    
    
    #-----------------------------------
    
    all_data_stats = compute_stats(overlap)
    sys_data_stats = ddply(overlap, .(system), compute_stats)
    
    write.table(all_data_stats, out_file_all, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    write.table(sys_data_stats, out_file_sys, append=FALSE, sep=";", row.names = FALSE, col.names = TRUE)
    

}