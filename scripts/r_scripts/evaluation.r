
rm(list=ls())
library(plyr)
library(pwr)

#---------------------------------------
granularities = c('B','P','S')
granularities = c('B')

set.seed(644480808)
num_sample_revision = 10

for (granularity in granularities) {
     
  
    #gs_file= 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/all_data_only_bugs_davies.csv'
    gs_file= 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/all_data_only_bugs_coded_data.csv'
    pr_file= paste('C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output/output-prediction-',granularity,'.csv', sep = "")
    
    if(granularity != "B"){
      gs_file= paste('C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/gold-set-',granularity,'.csv', sep = "")
    }
    
    out_folder = 'C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/output/'
    out_file_all =paste(out_folder,'stats_all_',granularity,'.csv', sep = "")
    out_file_all_eb =paste(out_folder,'data_all_eb_',granularity,'.csv', sep = "")
    out_file_all_sr =paste(out_folder,'data_all_sr_',granularity,'.csv', sep = "")
    out_file_all_eb_sample =paste(out_folder,'data_all_eb_sample_',granularity,'.csv', sep = "")
    out_file_all_sr_sample =paste(out_folder,'data_all_sr_sample_',granularity,'.csv', sep = "")
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
      
      new_cols = c(cols,"system", "bug_id", "instance_id")
      
      filtered_data = data1[,(names(data1) %in% new_cols)]
      colnames(filtered_data) <- c("system", "bug_id", "instance_id", "gs", "pr")
      
      tp_data = subset(filtered_data, pr=="x" & gs =='x')
      tp_data$type ="tp"
      tn_data = subset(filtered_data,  pr=="" & gs =='')
      tn_data$type ="tn"
      fp_data = subset(filtered_data, pr=="x" & gs =='')
      fp_data$type ="fp"
      fn_data = subset(filtered_data, pr=="" & gs =='x')
      fn_data$type ="fn"
      
      
      tp = nrow(tp_data)
      tn = nrow(tn_data)
      fp = nrow(fp_data)
      fn = nrow(fn_data)
      
      #------------------
      #sample for revision
      
      num_sam = num_sample_revision
      if (fp <num_sam) {
        num_sam = fp
      }
      fp_data_sample = fp_data[sample(1:nrow(fp_data), num_sam),]
      num_sam = num_sample_revision
      if (fn <num_sam) {
        num_sam = fn
      }
      fn_data_sample = fn_data[sample(1:nrow(fn_data), num_sam),]
      
      #-------------------------
      
      all_data = rbind(tp_data, tn_data, fp_data, fn_data)
      all_data_sample = rbind(fp_data_sample, fn_data_sample)
      
      list("stats" = c(tp, tn, fp, fn), "data" = all_data, "data_sample" = all_data_sample)
      
    }
    
    
#     compute_stats_all_labels <- function(data2){
#       
#       a = compute_numbers(data2, c('is_ob.x','is_ob.y' ))
#       b = compute_numbers(data2, c('is_eb.x','is_eb.y' ))
#       c = compute_numbers(data2, c('is_sr.x','is_sr.y' ))
#       
#       results = t(data.frame( a$stats, b$stats, c$stats))
#       results = cbind(c("ob", "eb", "sr"), results)
#       colnames(results) <- c("label", "tp", "tn", "fp", "fn")
#       
#       results 
#     }
    
    
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