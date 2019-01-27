from nltk.tokenize import word_tokenize

import torch
import torch.autograd as autograd
import torch.nn as nn
import torch.optim as optim
import gensim
import xml.etree.ElementTree as ET
import sys
import os
import util
import numpy as np
import pickle
from alphabet import Alphabet




class Data:
    def __init__(self):
        self.MAX_SENTENCE_LENGTH = 250
        self.MAX_WORD_LENGTH = -1
        self.number_normalized = True
        self.norm_word_emb = False
        self.norm_char_emb = False
        self.word_alphabet = Alphabet('word')
        #self.eb_alphabet = Alphabet('eb_word')
        self.char_alphabet = Alphabet('character')

        self.feature_name = []
        self.feature_alphabets = []
        self.feature_num = len(self.feature_alphabets)
        self.feat_config = None


        self.label_alphabet = Alphabet('label',True)
        self.tagScheme = "NoSeg" ## BMES/BIO

        self.seg = True

        ### I/O
        self.data_dir = None
        self.train_dir = None
        self.dev_dir = None
        self.test_dir = None
        self.raw_dir = None
        self.feature_dir = ""
        self.featureonly = False

        self.decode_dir = None
        self.dset_dir = None ## data vocabulary related file
        self.model_dir = None ## model save  file
        self.load_model_dir = None ## model load file

        self.word_emb_dir = None
        self.char_emb_dir = None
        self.feature_emb_dirs = []

        self.data_texts = []
        self.train_texts = []
        self.dev_texts = []
        self.test_texts = []
        self.raw_texts = []

        self.data_Ids = []
        self.train_Ids = []
        self.dev_Ids = []
        self.test_Ids = []
        self.raw_Ids = []

        self.pretrain_word_embedding = None
        self.pretrain_char_embedding = None
        self.pretrain_feature_embeddings = []

        self.label_size = 0
        self.word_alphabet_size = 0
        self.char_alphabet_size = 0
        self.label_alphabet_size = 0
        self.feature_alphabet_sizes = []
        self.feature_emb_dims = []
        self.norm_feature_embs = []
        self.word_emb_dim = 50
        self.char_emb_dim = 30

        ###Networks
        self.word_feature_extractor = "LSTM" ## "LSTM"/"CNN"/"GRU"/
        self.use_char = True
        self.char_feature_extractor = "CNN" ## "LSTM"/"CNN"/"GRU"/None
        self.feature_extractor = "None" ##"CNN" ## "LSTM"/"CNN"/"GRU"/None
        self.use_crf = True
        self.nbest = None

        ## Training
        self.average_batch_loss = False
        self.optimizer = "SGD" ## "SGD"/"AdaGrad"/"AdaDelta"/"RMSProp"/"Adam"
        self.status = "train"
        ### Hyperparameters
        self.HP_cnn_layer = 4
        self.HP_iteration = 100
        self.HP_batch_size = 10
        self.HP_char_hidden_dim = 50
        self.HP_hidden_dim = 200
        self.HP_dropout = 0.5
        self.HP_lstm_layer = 1
        self.HP_bilstm = True

        self.HP_gpu = False
        self.HP_lr = 0.015
        self.HP_lr_decay = 0.05
        self.HP_clip = None
        self.HP_momentum = 0
        self.HP_l2 = 1e-8
        self.device = torch.device("cpu")

    def read_hyperparameters(self, config):
        ## read Hyperparameters:
        the_item = 'cnn_layer'
        if the_item in config:
            self.HP_cnn_layer = int(config[the_item])
        the_item = 'epoch'
        if the_item in config:
            self.HP_iteration = int(config[the_item])
        the_item = 'batch_size'
        if the_item in config:
            self.HP_batch_size = int(config[the_item])

        the_item = 'char_hidden_dim'
        if the_item in config:
            self.HP_char_hidden_dim = int(config[the_item])
        the_item = 'hidden_dim'
        if the_item in config:
            self.HP_hidden_dim = int(config[the_item])
        the_item = 'dropout'
        if the_item in config:
            self.HP_dropout = float(config[the_item])
        the_item = 'lstm_layer'
        if the_item in config:
            self.HP_lstm_layer = int(config[the_item])
        the_item = 'bilstm'
        if the_item in config:
            self.HP_bilstm = str2bool(config[the_item])

        the_item = 'gpu'
        if the_item in config:
            self.HP_gpu = config[the_item]
        the_item = 'learning_rate'
        if the_item in config:
            self.HP_lr = float(config[the_item])
        the_item = 'lr_decay'
        if the_item in config:
            self.HP_lr_decay = float(config[the_item])
        the_item = 'clip'
        if the_item in config:
            self.HP_clip = float(config[the_item])
        the_item = 'momentum'
        if the_item in config:
            self.HP_momentum = float(config[the_item])
        the_item = 'l2'
        if the_item in config:
            self.HP_l2 = float(config[the_item])


    def fix_alphabet(self):
        self.word_alphabet.close()
        self.char_alphabet.close()
        self.label_alphabet.close()
        for idx in range(self.feature_num):
            self.feature_alphabets[idx].close()

    def initial_feature_alphabets(self):
        return

    def build_alphabet(self, data_path, feature_path, fileext):

        dataset = [] #(paragraph (sentence list),tags)
        fnames = []
        max_sentence_length = 0
        max_word_length  = 0

        tags = ['ob','eb','sr']

        #datafile = open(data_path,"r")
        #fnames = datafile.readlines()

        checkflag = False
        checkfile = open("/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier/crf-code-removal/experiment/all/crf_top50_patfea/crf-train_all-top-50-mapping","r")
        checklines = checkfile.readlines()
        checkmap = {}
        for line in checklines:
            temp = line.split("\t")
            checkmap[temp[0]+"\t"+temp[1]] = temp[2].strip()

        #file1821 = open("/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier-nn/Bug_report_doc_all_train_1821","r")
        #file1821lines = file1821.readlines()
        #print(file1821lines[1:10])
        #input(" ")
        
        for root, dirs,files in os.walk(data_path):
            for f in files:
                if not f.endswith(fileext):
                    continue
                f = os.path.join(root,f)
                #print(f)
                
                fnames.append(f)

        print("num of files:",len(fnames))
        #input(" ")

        pattern_feamap = {}

        if feature_path != "":
            feainstancefile = open(feature_path+"/instances-SP-F.txt","r")
            feafile = open(feature_path+"/features-sr-SP-F.txt","r")

            feainstances = feainstancefile.readlines()
            patfeatures = feafile.readlines()

            feainstancefile.close()
            feafile.close()

            self.feature_alphabets.append(Alphabet('pattern'))
            self.feature_name.append('pattern')
            self.feature_num = len(self.feature_alphabets)
            #self.pretrain_feature_embeddings = [None]*self.feature_num
            #self.feature_emb_dims = [20]*self.feature_num
            #self.feature_emb_dirs = [None]*self.feature_num
            #self.norm_feature_embs = [False]*self.feature_num
            #self.feature_alphabet_sizes = [0]*self.feature_num
            for i in range(0,len(patfeatures)):
                patfea = patfeatures[i][2:].split(" ")
                patfea = [fea.strip() for fea in patfea]
                if patfea == ['']:
                    patfea = []
                #input(patfea)
                for fea in patfea:
                    self.feature_alphabets[0].add(fea)
                pattern_feamap[feainstances[i].strip()] = patfea

            #input("pattern_alphabet_size:"+str(self.feature_alphabets[0].size()))
            #input("pattern instance size:"+str(len(pattern_feamap)))
            #for key in pattern_feamap.keys():
            #    print(key,pattern_feamap[key])
            #    break
        else:
            self.feature_alphabets.append(Alphabet('pattern'))
            print(self.feature_alphabets[0])
            #input(" ")


        foundpat = 0
        fcount = 0
        for f in fnames:
            #
            #f = f.replace("/users/ljwinnie/Desktop/bug_report/experiment-2016.12/data", "/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier/replication_package/1_data/2_preprocessed_data/0_code_removal_only")
            #f = f.replace(".xml",".parse.xml").replace("\n","")
            #print(f)

            if not f.endswith(".xml"):
                #print("skipping:",f)
                continue

            #if "eclipse" not in f and "firefox" not in f and "openmrs" not in f \
            #and "libreoffice" not in f and "wordpress" not in f:
                #print("skipping:",f)
            #else:
            #    continue

            #if "facebook" in f or "openmrs" in f:
            #    print("skipping:",f)
            #    continue

            #print(f)
            fcount += 1

            tree = ET.parse(f)
            root = tree.getroot()

            for paragraph in root.iter('parg'):
                #print(paragraph.get("id"))
                parg_label = ""
                ptmps = []
                for tag in tags:
                    if paragraph.get(tag).lower() == "x":
                        ptmps.append(tag)
                ptmps.sort()
                parg_label = ("#").join(ptmps)

                sentences = []
                labels = []
                features = []
                instance_ids = []


                for sent in paragraph.findall('st'):
                    #print(sent.get("id"))
                    if sent.text == None:
                        input(f)
                        continue

                    systemname = f.split("/")[-2]
                    bugid = f.split("/")[-1].replace(fileext,"")

                    instance_id = systemname+"-"+bugid+"-"+sent.get("id")
                    instance_ids.append(instance_id)

                    #print(instance_id)
                    if instance_id in pattern_feamap.keys():
                        foundpat = foundpat+1
                        #print(instance_id,pattern_feamap[instance_id])
                        features.append(pattern_feamap[instance_id])
                    else:
                        features.append([])

                    sentences.append(word_tokenize(sent.text))
                    sent_label = ""
                    
                    tmps = []
                    for tag in tags:
                        if sent.get(tag).lower() == "x":
                            tmps.append(tag)
                    tmps.sort()
                    sent_label = ("#").join(tmps)
                    
                    if sent_label == "" and parg_label != "":
                        sent_label = parg_label
                    elif sent_label != "" and parg_label != "" and sent_label != parg_label:
                        templabels = []
                        templabels.extend(sent_label.split("#"))
                        templabels.extend(parg_label.split("#"))
                        templabels = list(set(templabels))
                        templabels.sort()
                        sent_label = ("#").join(templabels)
                        #print(instance_id,sent_label)
                        #input(" ")


                    if sent_label == "":
                        labels.append("O")
                    else:
                        if len(labels) >= 1:
                            if sent_label == labels[-1][2:]:
                                labels.append("I-"+sent_label)
                            else:
                                labels.append("B-"+sent_label)
                        else:
                            labels.append("B-"+sent_label)

                    if checkflag:
                        checkid = f+"\t"+sent.get("id")
                        if checkid in checkmap.keys():
                            if checkmap[checkid][2:]== labels[-1][2:]:
                                print(checkid,labels[-1])
                            else:
                                print(checkid,checkmap[checkid],labels[-1])
                                input(" ")
                        else:
                            print("notfound:", checkid)
                            input(" ")

                templength = max(len(s) for s in sentences)
                if templength > max_sentence_length:
                    max_sentence_length = templength

                for i in range(0,len(sentences)):
                    for j in range(0,len(sentences[i])):
                        tempwordlength = max(len(w) for w in sentences[i])
                        if tempwordlength > max_word_length:
                            max_word_length = tempwordlength

                        word = sentences[i][j]
                        self.word_alphabet.add(word) 

                        #if "eb" in labels[i]:
                            #self.eb_alphabet.add(word.lower())
                        for char in word:
                            self.char_alphabet.add(char)
                        #if sentences[i][j].lower() in embed.index2entity:
                            #new_sentences[i][j] = sentences[i][j].lower()
                        #else:
                            #new_sentences[i][j] = "UNKNOWN"
                            #input(sentences[i][j].lower())

                for l in labels:
                    self.label_alphabet.add(l)

                dataset.append((sentences,labels,features,instance_ids))
                #print((new_sentences,labels))
            #if len(dataset)>600:
                #break
            #input(f)
        #print("# sentences", len(instance_ids))
        #print("# senteces deduplicate", len(set(instance_ids)))
        print("# foundpat",foundpat)
        #print("eb vocabulary size:", self.eb_alphabet.size())
        #for word, index in self.eb_alphabet.iteritems():
            #print(word)
        print("vocabulary size:",self.word_alphabet.size())
        #input(" ")
        #print("num of files:",fcount)
        #input(" ")

            
        self.word_alphabet_size = self.word_alphabet.size()
        self.char_alphabet_size = self.char_alphabet.size()
        self.label_alphabet_size = self.label_alphabet.size()
        self.tagScheme = "BIO"
        self.MAX_SENTENCE_LENGTH = max_sentence_length
        self.MAX_WORD_LENGTH = max_word_length


        return dataset

    def generate_instance(self, data_path, feature_path, fileext):

        dataset = [] #(paragraph (sentence list),tags)
        fnames = []
        max_sentence_length = 0
        max_word_length  = 0

        tags = ['ob','eb','sr']
      
        for root, dirs,files in os.walk(data_path):
            for f in files:
                if not f.endswith(fileext):
                    continue
                f = os.path.join(root,f)

                fnames.append(f)

        print("num of files:",len(fnames))
        #input(" ")

        pattern_feamap = {}

        if feature_path != "":
            feainstancefile = open(feature_path+"/instances-SP-F.txt","r")
            feafile = open(feature_path+"/features-sr-SP-F.txt","r")

            feainstances = feainstancefile.readlines()
            patfeatures = feafile.readlines()

            feainstancefile.close()
            feafile.close()

           
            for i in range(0,len(patfeatures)):
                patfea = patfeatures[i][2:].split(" ")
                patfea = [fea.strip() for fea in patfea]
                if patfea == ['']:
                    patfea = []
                pattern_feamap[feainstances[i].strip()] = patfea

        else:
            print("pattern feature path not found-> pattern feature not used")
            input(" ")


        foundpat = 0
        fcount = 0
        for f in fnames:

            if not f.endswith(".xml"):
                print("skipping:",f)
                continue

            fcount += 1

            tree = ET.parse(f)
            root = tree.getroot()

            systemname = f.split("/")[-1].split("#")[0]
            bugid = f.split("/")[-1].split("_")[1].replace(fileext,"")
            #print("systemname:",systemname)
            #print("bugid:",bugid)
            #input(" ")

            #process title
            title = root.find('title')
            tsentences = [word_tokenize(title.text)]
            tlabels = ['O']
            tinstance_id = systemname+"-"+bugid+"-0"
            tinstance_ids = [tinstance_id]
            tfeatures = []
            if tinstance_id in pattern_feamap.keys():
                foundpat = foundpat+1
                #print(instance_id,pattern_feamap[instance_id])
                tfeatures.append(pattern_feamap[tinstance_id])
            else:
                tfeatures.append([])

            dataset.append((tsentences,tlabels,tfeatures,tinstance_ids))
            #print("dataset:",dataset)
            #input(" ")


            for paragraph in root.iter('parg'):
                #print(paragraph.get("id"))
                parg_label = ""
                ptmps = []
                for tag in tags:
                    if paragraph.get(tag).lower() == "x":
                        ptmps.append(tag)
                ptmps.sort()
                parg_label = ("#").join(ptmps)

                sentences = []
                labels = []
                features = []
                instance_ids = []


                for sent in paragraph.findall('st'):
                    #print(sent.get("id"))
                    if sent.text == None:
                        input(f)
                        continue


                    instance_id = systemname+"-"+bugid+"-"+sent.get("id")
                    instance_ids.append(instance_id)

                    #print(instance_id)
                    if instance_id in pattern_feamap.keys():
                        foundpat = foundpat+1
                        #print(instance_id,pattern_feamap[instance_id])
                        features.append(pattern_feamap[instance_id])
                    else:
                        features.append([])

                    sentences.append(word_tokenize(sent.text))
                    sent_label = ""
                    
                    tmps = []
                    for tag in tags:
                        if sent.get(tag).lower() == "x":
                            tmps.append(tag)
                    tmps.sort()
                    sent_label = ("#").join(tmps)
                    
                    if sent_label == "" and parg_label != "":
                        sent_label = parg_label
                    elif sent_label != "" and parg_label != "" and sent_label != parg_label:
                        templabels = []
                        templabels.extend(sent_label.split("#"))
                        templabels.extend(parg_label.split("#"))
                        templabels = list(set(templabels))
                        templabels.sort()
                        sent_label = ("#").join(templabels)

                    if sent_label == "":
                        labels.append("O")
                    else:
                        if len(labels) >= 1:
                            if sent_label == labels[-1][2:]:
                                labels.append("I-"+sent_label)
                            else:
                                labels.append("B-"+sent_label)
                        else:
                            labels.append("B-"+sent_label)

                templength = max(len(s) for s in sentences)
                if templength > max_sentence_length:
                    max_sentence_length = templength

                for i in range(0,len(sentences)):
                    for j in range(0,len(sentences[i])):
                        tempwordlength = max(len(w) for w in sentences[i])
                        if tempwordlength > max_word_length:
                            max_word_length = tempwordlength

                        word = sentences[i][j]            

                dataset.append((sentences,labels,features,instance_ids))

            #print("dataset:",dataset)
            #input(" ")
                
        print("# foundpat",foundpat)
        print("vocabulary size:",self.word_alphabet.size())
        print("num of files:",fcount)
        #input(" ")

        return dataset

    def load_gold_labels(self, data_path, fileext):
        label_map = {}

        fnames = []
        max_sentence_length = 0
        max_word_length  = 0

        tags = ['ob','eb','sr']

    
        for root, dirs,files in os.walk(data_path):
            for f in files:
                if not f.endswith(fileext):
                    continue

                fnames.append(os.path.join(root,f))

        print("num of files:",len(fnames))
        #input(" ")
        instance_ids = []


        foundpat = 0
        for f in fnames:
            #
            #f = f.replace("/users/ljwinnie/Desktop/bug_report/experiment-2016.12/data", "/shared/mlrdir3/disk1/ljwinnie/bug_report/sentence-classifier/replication_package/1_data/2_preprocessed_data/0_code_removal_only")
            #f = f.replace(".xml",".parse.xml").replace("\n","")
            #print(f)

            if not f.endswith(".xml"):
                print("skipping:",f)
                continue

            tree = ET.parse(f)
            root = tree.getroot()

            for paragraph in root.iter('parg'):
                #print(paragraph.get("id"))
                parg_label = ""
                ptmps = []
                for tag in tags:
                    if paragraph.get(tag).lower() == "x":
                        ptmps.append(tag)
                ptmps.sort()
                parg_label = ("#").join(ptmps)

                sentences = []
                labels = []
                features = []

                for sent in paragraph.findall('st'):
                    #print(sent.get("id"))
                    instance_ids.append(f+"-"+paragraph.get("id")+"-"+sent.get("id"))

                    systemname = f.split("/")[-2]
                    bugid = f.split("/")[-1].replace(fileext,"")

                    instance_id = systemname+"-"+bugid+"-"+sent.get("id")
                    #print(instance_id)
                    if instance_id in pattern_feamap.keys():
                        foundpat = foundpat+1
                        #print(instance_id,pattern_feamap[instance_id])
                        features.append(pattern_feamap[instance_id])
                    else:
                        features.append([])

                    sentences.append(word_tokenize(sent.text))
                    sent_label = ""
                    
                    tmps = []
                    for tag in tags:
                        if sent.get(tag).lower() == "x":
                            tmps.append(tag)
                    tmps.sort()
                    sent_label = ("#").join(tmps)
                    
                    if sent_label == "" and parg_label != "":
                        sent_label = parg_label

                    if sent_label == "":
                        labels.append("O")
                    else:
                        if len(labels) >= 1:
                            if sent_label == labels[-1][2:]:
                                labels.append("I-"+sent_label)
                            else:
                                labels.append("B-"+sent_label)
                        else:
                            labels.append("B-"+sent_label)

                    if checkflag:
                        checkid = f+"\t"+sent.get("id")
                        if checkid in checkmap.keys():
                            if checkmap[checkid][2:]== labels[-1][2:]:
                                print(checkid,labels[-1])
                            else:
                                print(checkid,checkmap[checkid],labels[-1])
                                input(" ")
                        else:
                            print("notfound:", checkid)
                            input(" ")

                templength = max(len(s) for s in sentences)
                if templength > max_sentence_length:
                    max_sentence_length = templength

                for i in range(0,len(sentences)):
                    for j in range(0,len(sentences[i])):
                        tempwordlength = max(len(w) for w in sentences[i])
                        if tempwordlength > max_word_length:
                            max_word_length = tempwordlength

                        word = sentences[i][j]
                        self.word_alphabet.add(word) 
                        for char in word:
                            self.char_alphabet.add(char)
                        #if sentences[i][j].lower() in embed.index2entity:
                            #new_sentences[i][j] = sentences[i][j].lower()
                        #else:
                            #new_sentences[i][j] = "UNKNOWN"
                            #input(sentences[i][j].lower())

                for l in labels:
                    self.label_alphabet.add(l)

                
                dataset.append((sentences,labels,features))
             
        print("# sentences", len(instance_ids))


        return dataset

    def load(self,data_file):
        f = open(data_file, 'rb')
        tmp_dict = pickle.load(f)
        f.close()
        self.__dict__.update(tmp_dict)

    def save(self,save_file):
        f = open(save_file, 'wb')
        pickle.dump(self.__dict__, f, 2)
        f.close()


    def read_instance(self,dataset,word_alphabet,char_alphabet,label_alphabet, feature_alphabets):
        instance_texts = []
        instance_ids = []

        
        for item in dataset: #item is a paragraph in the doc
            words = []
            features = []
            chars = []
            labels = []
            bug_ids = []

            word_ids = []
            feature_ids = []
            char_ids = []
            label_ids = []
            
            
            for sentence in item[0]:
                word_list = [] #words in one sentence
                word_id_list = []

                char_word_list = []
                char_word_id_list = []

                for word in sentence:
                    word_list.append(word)
                    word_id_list.append(word_alphabet.get_index(word))

                    char_list = []
                    char_id_list = []

                    for char in word:
                        char_list.append(char)
                        char_id_list.append(char_alphabet.get_index(char))

                    char_word_list.append(char_list)
                    char_word_id_list.append(char_id_list)

                words.append(word_list)
                word_ids.append(word_id_list)

                chars.append(char_word_list)
                char_ids.append(char_word_id_list)

            for label in item[1]:
                labels.append(label)
                label_ids.append(label_alphabet.get_index(label))

            for feature in item[2]:
                features.append(feature)
                feature_ids.append([feature_alphabets[0].get_index(fea) for fea in feature])

            for bugid in item[3]:
                bug_ids.append(bugid)


            instance_texts.append([words,features,chars,labels,bug_ids]) # each instance is a paragraph
            instance_ids.append([word_ids,feature_ids,char_ids,label_ids])

        return instance_texts, instance_ids

    def read_config(self,config):
        ## read data:
        the_item = 'train_dir'
        if the_item in config:
            self.train_dir = config[the_item]
        the_item = 'dev_dir'
        if the_item in config:
            self.dev_dir = config[the_item]
        the_item = 'test_dir'
        if the_item in config:
            self.test_dir = config[the_item]
        the_item = 'raw_dir'
        if the_item in config:
            self.raw_dir = config[the_item]
        the_item = 'decode_dir'
        if the_item in config:
            self.decode_dir = config[the_item]
        the_item = 'dset_dir'
        if the_item in config:
            self.dset_dir = config[the_item]
        the_item = 'model_dir'
        if the_item in config:
            self.model_dir = config[the_item]
        the_item = 'load_model_dir'
        if the_item in config:
            self.load_model_dir = config[the_item]

        the_item = 'word_emb_dir'
        if the_item in config:
            self.word_emb_dir = config[the_item]
        the_item = 'char_emb_dir'
        if the_item in config:
            self.char_emb_dir = config[the_item]


        the_item = 'MAX_SENTENCE_LENGTH'
        if the_item in config:
            self.MAX_SENTENCE_LENGTH = int(config[the_item])
        the_item = 'MAX_WORD_LENGTH'
        if the_item in config:
            self.MAX_WORD_LENGTH = int(config[the_item])

        the_item = 'norm_word_emb'
        if the_item in config:
            self.norm_word_emb = str2bool(config[the_item])
        the_item = 'norm_char_emb'
        if the_item in config:
            self.norm_char_emb = str2bool(config[the_item])
        the_item = 'number_normalized'
        if the_item in config:
            self.number_normalized = str2bool(config[the_item])


        the_item = 'seg'
        if the_item in config:
            self.seg = str2bool(config[the_item])
        the_item = 'word_emb_dim'
        if the_item in config:
            self.word_emb_dim = int(config[the_item])
        the_item = 'char_emb_dim'
        if the_item in config:
            self.char_emb_dim = int(config[the_item])

        ## read network:
        the_item = 'use_crf'
        if the_item in config:
            self.use_crf = str2bool(config[the_item])
        the_item = 'use_char'
        if the_item in config:
            self.use_char = str2bool(config[the_item])
        the_item = 'word_seq_feature'
        if the_item in config:
            self.word_feature_extractor = config[the_item]
        the_item = 'char_seq_feature'
        if the_item in config:
            self.char_feature_extractor = config[the_item]
        the_item = 'nbest'
        if the_item in config:
            self.nbest = int(config[the_item])

        the_item = 'feature'
        if the_item in config:
            self.feat_config = config[the_item] ## feat_config is a dict


        ## read training setting:
        the_item = 'optimizer'
        if the_item in config:
            self.optimizer = config[the_item]
        the_item = 'ave_batch_loss'
        if the_item in config:
            self.average_batch_loss = str2bool(config[the_item])
        the_item = 'status'
        if the_item in config:
            self.status = config[the_item]

        ## read Hyperparameters:
        the_item = 'cnn_layer'
        if the_item in config:
            self.HP_cnn_layer = int(config[the_item])
        the_item = 'iteration'
        if the_item in config:
            self.HP_iteration = int(config[the_item])
        the_item = 'batch_size'
        if the_item in config:
            self.HP_batch_size = int(config[the_item])

        the_item = 'char_hidden_dim'
        if the_item in config:
            self.HP_char_hidden_dim = int(config[the_item])
        the_item = 'hidden_dim'
        if the_item in config:
            self.HP_hidden_dim = int(config[the_item])
        the_item = 'dropout'
        if the_item in config:
            self.HP_dropout = float(config[the_item])
        the_item = 'lstm_layer'
        if the_item in config:
            self.HP_lstm_layer = int(config[the_item])
        the_item = 'bilstm'
        if the_item in config:
            self.HP_bilstm = str2bool(config[the_item])

        the_item = 'gpu'
        if the_item in config:
            self.HP_gpu = str2bool(config[the_item])
        the_item = 'learning_rate'
        if the_item in config:
            self.HP_lr = float(config[the_item])
        the_item = 'lr_decay'
        if the_item in config:
            self.HP_lr_decay = float(config[the_item])
        the_item = 'clip'
        if the_item in config:
            self.HP_clip = float(config[the_item])
        the_item = 'momentum'
        if the_item in config:
            self.HP_momentum = float(config[the_item])
        the_item = 'l2'
        if the_item in config:
            self.HP_l2 = float(config[the_item])

    def show_data_summary(self):
        print("++"*50)
        print("DATA SUMMARY START:")
        print(" I/O:")
        print("     Tag          scheme: %s"%(self.tagScheme))
        print("     MAX SENTENCE LENGTH: %s"%(self.MAX_SENTENCE_LENGTH))
        print("     MAX   WORD   LENGTH: %s"%(self.MAX_WORD_LENGTH))
        print("     Number   normalized: %s"%(self.number_normalized))
        print("     Word  alphabet size: %s"%(self.word_alphabet_size))
        print("     Char  alphabet size: %s"%(self.char_alphabet_size))
        print("     Label alphabet size: %s"%(self.label_alphabet_size))
        print("     Word embedding  dir: %s"%(self.word_emb_dir))
        print("     Char embedding  dir: %s"%(self.char_emb_dir))
        print("     Word embedding size: %s"%(self.word_emb_dim))
        print("     Char embedding size: %s"%(self.char_emb_dim))
        print("     Norm   word     emb: %s"%(self.norm_word_emb))
        print("     Norm   char     emb: %s"%(self.norm_char_emb))
        print("     Train  file directory: %s"%(self.train_dir))
        print("     Dev    file directory: %s"%(self.dev_dir))
        print("     Test   file directory: %s"%(self.test_dir))
        print("     Raw    file directory: %s"%(self.raw_dir))
        print("     Dset   file directory: %s"%(self.dset_dir))
        print("     Model  file directory: %s"%(self.model_dir))
        print("     Loadmodel   directory: %s"%(self.load_model_dir))
        print("     Decode file directory: %s"%(self.decode_dir))
        print("     Train instance number: %s"%(len(self.train_texts)))
        print("     Dev   instance number: %s"%(len(self.dev_texts)))
        print("     Test  instance number: %s"%(len(self.test_texts)))
        print("     Raw   instance number: %s"%(len(self.raw_texts)))
        print("     FEATURE num: %s"%(self.feature_num))
        #for idx in range(self.feature_num):
        #    print("         Fe: %s  alphabet  size: %s"%(self.feature_alphabets[idx].name, self.feature_alphabet_sizes[idx]))
        #    print("         Fe: %s  embedding  dir: %s"%(self.feature_alphabets[idx].name, self.feature_emb_dirs[idx]))
        #    print("         Fe: %s  embedding size: %s"%(self.feature_alphabets[idx].name, self.feature_emb_dims[idx]))
        #    print("         Fe: %s  norm       emb: %s"%(self.feature_alphabets[idx].name, self.norm_feature_embs[idx]))
        print(" "+"++"*20)
        print(" Model Network:")
        print("     Model        use_crf: %s"%(self.use_crf))
        print("     Model word extractor: %s"%(self.word_feature_extractor))
        print("     Model       use_char: %s"%(self.use_char))
        if self.use_char:
            print("     Model char extractor: %s"%(self.char_feature_extractor))
            print("     Model char_hidden_dim: %s"%(self.HP_char_hidden_dim))
        print(" "+"++"*20)
        print(" Training:")
        print("     Optimizer: %s"%(self.optimizer))
        print("     Iteration: %s"%(self.HP_iteration))
        print("     BatchSize: %s"%(self.HP_batch_size))
        print("     Average  batch   loss: %s"%(self.average_batch_loss))

        print(" "+"++"*20)
        print(" Hyperparameters:")

        print("     Hyper              lr: %s"%(self.HP_lr))
        print("     Hyper        lr_decay: %s"%(self.HP_lr_decay))
        print("     Hyper         HP_clip: %s"%(self.HP_clip))
        print("     Hyper        momentum: %s"%(self.HP_momentum))
        print("     Hyper              l2: %s"%(self.HP_l2))
        print("     Hyper      hidden_dim: %s"%(self.HP_hidden_dim))
        print("     Hyper         dropout: %s"%(self.HP_dropout))
        print("     Hyper      lstm_layer: %s"%(self.HP_lstm_layer))
        print("     Hyper          bilstm: %s"%(self.HP_bilstm))
        print("     Hyper             GPU: %s"%(self.HP_gpu))
        print("DATA SUMMARY END.")
        print("++"*50)
        sys.stdout.flush()

