input_file = 'C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/patterns_frequency.csv'
data_all = read.csv(input_file, sep = ";", header = TRUE)


#----------------------------------

summary(data_all)

#----------------------------------

a =subset(data_all,type_info == 'EB')
summary(a$num_sent_paragraph)

a =subset(data_all,type_info == 'OB')
summary(a$num_sent_paragraph)

a =subset(data_all,type_info == 'SR')
summary(a$num_sent_paragraph)

#----------------------------------

a =subset(data_all,granularity == 'P')
summary(a$num_sent_paragraph)

a =subset(data_all,granularity == 'S')
summary(a$num_sent_paragraph)

#----------------------------------

boxplot(data_all$num_sent_paragraph)

#-------------------------------

quantile(data_all$num_sent_paragraph, .90)
quantile(data_all$num_sent_paragraph, .95)