
rm(list=ls())
library(arules)

#------------------------------------------------
# parameters
rule_size = 5
min_confidence = 0.5

ob_items = c("P_OB_BECOMES=1", "P_OB_COND_EXAMPLE=1", "P_OB_DESCRIPTION=1", "P_OB_ERROR_TERMS_PARAGRAPH=1", "P_OB_LOG_CONTAINS=1", "P_OB_OBSERVED_BEHAVIOR_MULTI=1", "P_OB_TREAT_AS=1", "S_OB_ACTION_PAST_FINE=1", "S_OB_ACTION_SUBJECT=1", "S_OB_ADV_FREQ=1", "S_OB_ADV_TIME_NEG=1", "S_OB_ADV_TIME_POS=1", "S_OB_AFTER_NEG=1", "S_OB_AFTER_POS=1", "S_OB_AFTER_TIME=1", "S_OB_ALLOW=1", "S_OB_AS_EXPECTED=1", "S_OB_AS_IF=1", "S_OB_AS_RESULT=1", "S_OB_ASSUME=1", "S_OB_ATTACH_REF=1", "S_OB_BECAUSE=1", "S_OB_BECOMES_ADJECTIVE=1", "S_OB_BEFORE=1", "S_OB_BEING=1", "S_OB_BUT=1", "S_OB_BUT_NEG=1", "S_OB_CHAR_THEN=1", "S_OB_COMP_ADJ=1", "S_OB_COND_NEG=1", "S_OB_COND_POS=1", "S_OB_DESPITE=1", "S_OB_DUE_NEG=1", "S_OB_END_UP=1", "S_OB_ERROR_AS_SUBJECT=1", "S_OB_ERROR_COND=1", "S_OB_ERROR_HOW=1", "S_OB_ERROR_NOUN_PHRASE=1", "S_OB_EXCEPT=1", "S_OB_FOR_TIME=1", "S_OB_FOUND=1", "S_OB_HAPPENS=1", "S_OB_IMPOSSIBLE_TO=1", "S_OB_INSTEAD_OF=1", "S_OB_KEEP_VERB=1", "S_OB_LEADS_TO=1", "S_OB_LEADS_TO_NEG=1", "S_OB_LEAVES_GERUND=1", "S_OB_NEG_ADV_ADJ=1", "S_OB_NEG_AFTER=1", "S_OB_NEG_AUX_VERB=1", "S_OB_NEG_COND=1", "S_OB_NEG_VERB=1", "S_OB_NO_LONGER=1", "S_OB_NO_MATTER=1", "S_OB_NO_NOUN=1", "S_OB_NON_TERM=1", "S_OB_NOT_VERB=1", "S_OB_NOTHING_HAPPENS=1", "S_OB_NOTICE=1", "S_OB_NOUN_NOT=1", "S_OB_OBS_BEHAVIOR=1", "S_OB_OBSERVE=1", "S_OB_ONLY=1", "S_OB_OUTPUT=1", "S_OB_OUTPUT_VERB=1", "S_OB_PASSIVE_VOICE=1", "S_OB_POS_AFTER=1", "S_OB_POS_COND=1", "S_OB_PROBLEM_IN=1", "S_OB_PROBLEM_IS=1", "S_OB_SAME_PROBLEM_WHEN=1", "S_OB_SEEMS=1", "S_OB_SEEMS_TO_BE=1", "S_OB_SEEMS_TO_NEG_VERB=1", "S_OB_SIMPLE_PRESENT=1", "S_OB_STILL=1", "S_OB_SUDDENLY_NEGATIVE=1", "S_OB_TOO=1", "S_OB_TRY_PRESENT=1", "S_OB_UNABLE_TO=1", "S_OB_UNFORTUNATELY=1", "S_OB_UNTIL_NEG=1", "S_OB_VERB_ERROR=1", "S_OB_VERB_NO=1", "S_OB_VERB_TO_BE_NEGATIVE=1", "S_OB_VERY_ADJ=1", "S_OB_WHY=1", "S_OB_WITHOUT=1", "S_OB_WORKS_BUT=1", "S_OB_WORKS_FINE=1")
eb_items = c("P_EB_EXP_BEHAVIOR_MULTI=1", "S_EB_BEFORE=1", "S_EB_BEHAVE_DIFFERENT=1", "S_EB_BETTER_TO=1", "S_EB_BUT_CORRECT=1", "S_EB_CAN=1", "S_EB_CAN_QUESTION=1", "S_EB_CONSIDER_ACTION=1", "S_EB_CORRECT_IS=1", "S_EB_COULD=1", "S_EB_COULD_QUESTION=1", "S_EB_EXP_BEHAVIOR=1", "S_EB_EXPECTED=1", "S_EB_IMPERATIVE=1", "S_EB_INSTEAD_OF_EXP_BEHAVIOR=1", "S_EB_MAKE_SENSE=1", "S_EB_MUST=1", "S_EB_NECESSARY=1", "S_EB_NEED_TO=1", "S_EB_NEG_NOUN_COND=1", "S_EB_NORMALLY=1", "S_EB_REQUIRES=1", "S_EB_SHOULD=1", "S_EB_SUPPOSED_TO=1", "S_EB_USED_TO=1", "S_EB_WANT=1", "S_EB_WAS_ABLE_TO=1", "S_EB_WHY_FIRST_PLACE=1", "S_EB_WHY_NEG=1", "S_EB_WOULD_BE=1", "S_EB_WOULD_LIKE=1")
sr_items = c("P_SR_ACTIONS_INF=1", "P_SR_ACTIONS_MULTI_OBS_BEHAVIOR=1", "P_SR_ACTIONS_PAST=1", "P_SR_ACTIONS_PRESENT=1", "P_SR_COND_CODE=1", "P_SR_COND_SEQUENCE=1", "P_SR_CONTINOUS_PRESENT_PAST=1", "P_SR_HAVE_SEQUENCE=1", "P_SR_LABELED_CODE_FRAGS=1", "P_SR_LABELED_LIST=1", "P_SR_LABELED_PARAGRAPH=1", "P_SR_SIMPLE_PAST=1", "P_SR_TO_REPRO=1", "S_SR_ACTIONS_PRESENT_PERFECT=1", "S_SR_ACTIONS_SEPARATOR=1", "S_SR_AFTER=1", "S_SR_BY_ACTION=1", "S_SR_CODE_REF=1", "S_SR_COND_OBS=1", "S_SR_COND_SEQUENCE=1", "S_SR_COND_THEN_SEQ=1", "S_SR_CONTINOUS_PRESENT=1", "S_SR_IMPERATIVE_SEQUENCE=1", "S_SR_IMPERATIVE_SUBORDINATES=1", "S_SR_MENU_NAV=1", "S_SR_MENU_SELECT=1", "S_SR_PURPOSE_ACTION=1", "S_SR_SIMPLE_PAST=1", "S_SR_SIMPLE_PRESENT_SUBORDINATES=1", "S_SR_TRIGGERS=1", "S_SR_TRY=1", "S_SR_WHEN_AFTER=1")

#------------------------------------------------
#functions

read_data <- function(prefix){
  
  input_file = paste("C:/Users/ojcch/Documents/Repositories/Git/bug_report_completion/code/bug_report_patterns/test_data/association_rules/",prefix,"_patterns.csv", sep = "")
  data_all = read.csv(input_file, sep = ";", header = TRUE)
  data_all[] <- lapply(data_all, factor)
  
  data_all
}

print_top_rules <- function(rules){
  
  n = 50
  if (length(rules)<50) {
    n=length(rules)
  }
  inspect(rules[1:n])
  
}


extract_rules <- function (data1, items, remove_redundant){
  num_trans = nrow(data1)
  
  #extract the rules
  rules <- apriori(data1, parameter = list(minlen=2, maxlen=rule_size, supp=1/num_trans, conf=min_confidence), appearance = list(both=items, default="none"))
  
  #sort
  rules.sorted <- sort(rules, by="support")
  
  if (remove_redundant) {
    
    # find redundant rules
    subset.matrix <- is.subset(rules.sorted, rules.sorted)
    subset.matrix[lower.tri(subset.matrix, diag=T)] <- NA
    redundant <- colSums(subset.matrix, na.rm=T) >= 1
    
    # remove redundant rules
    rules.pruned <- rules.sorted[!redundant]
    
    subset.matrix = NULL
    redundant = NULL
    
    rules.pruned
  }else{
    rules.sorted
  }
  
}

get_items <- function(label){
  
  items = NULL
  if (label == "ob") {
    items = ob_items
  }else if (label == "eb") {
      items = eb_items
  }else if (label == "sr") {
      items = sr_items
  }
  
  items
}


#------------------------------------------------
#rule extraction

label = "eb"
data = read_data(label)
items = get_items(label)
rules = extract_rules(data, items, TRUE)
print_top_rules(rules)
summary(rules)

