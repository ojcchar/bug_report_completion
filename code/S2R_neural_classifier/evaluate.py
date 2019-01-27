
import torch
import torch.autograd as autograd
import torch.nn as nn
import torch.optim as optim
import gensim
import sys
import os
import util
import numpy as np
import time
import random
import gc
import argparse
import csv


from data import Data
from seqmodel import SeqModel
from metric import get_ner_fmeasure
from alphabet import Alphabet

import alphabet

seed_num = 42
random.seed(seed_num)
torch.manual_seed(seed_num)
np.random.seed(seed_num)

def predict_check(pred_variable, gold_variable, mask_variable):
    """
        input:
            pred_variable (batch_size, sent_len): pred tag result, in numpy format
            gold_variable (batch_size, sent_len): gold result variable
            mask_variable (batch_size, sent_len): mask variable
    """
    pred = pred_variable.cpu().data.numpy()
    gold = gold_variable.cpu().data.numpy()
    mask = mask_variable.cpu().data.numpy()
    overlaped = (pred == gold)
    right_token = np.sum(overlaped * mask)
    total_token = mask.sum()
    # print("right: %s, total: %s"%(right_token, total_token))
    return right_token, total_token

def batchify_with_label(input_batch_list, gpu, device, pattern_fea_size):
    """
        input: list of words, chars and labels, various length. [[words,chars, labels],[words,chars,labels],...]
            words: word ids for one sentence. (batch_size, sent_len)
            chars: char ids for on sentences, various length. (batch_size, sent_len, each_word_length)
        output:
            zero padding for word and char, with their batch length
            word_seq_tensor: (batch_size, max_sent_len) Variable
            word_seq_lengths: (batch_size,1) Tensor
            char_seq_tensor: (batch_size*max_sent_len, max_word_len) Variable
            char_seq_lengths: (batch_size*max_sent_len,1) Tensor
            char_seq_recover: (batch_size*max_sent_len,1)  recover char sequence order
            label_seq_tensor: (batch_size, max_sent_len)
            mask: (batch_size, max_sent_len)
    """
    batch_size = len(input_batch_list)
    sents = [paragraph[0] for paragraph in input_batch_list] # each element is a paragraph 

    features = [paragraph[1] for paragraph in input_batch_list]

    feature_num = 1
    if pattern_fea_size == 0:
        feature_num = 0

    chars = [paragraph[2] for paragraph in input_batch_list] #batch_size * num of sents * num of words in a sent
    labels = [paragraph[3] for paragraph in input_batch_list] #batch_size * max_seq_len

    sent_seq_lengths = torch.LongTensor(list(map(len, sents))) #batch_size * 1
    max_seq_len = sent_seq_lengths.max() 

    pad_sents = [sents[idx]+ [[0]]* (max_seq_len - len(sents[idx])).item() for idx in range(len(sents))] #batch_size * max_seq_len


    sent_lengths = [list(map(len, sent)) for sent in pad_sents] #batch_size * max_seq_len
    max_sent_len = max(map(max,sent_lengths))
    word_seq_lengths = torch.LongTensor(sent_lengths)

   
    word_seq_tensor = torch.zeros((batch_size,max_seq_len,max_sent_len),dtype = torch.long) #batch_size * max_seq_len * max_sent_len
    mask = torch.zeros((batch_size, max_seq_len), dtype = torch.uint8) #batch_size * max_seq_len
    label_seq_tensor = torch.zeros((batch_size, max_seq_len), dtype = torch.long) #batch_size * max_seq_len


    for idx, (seq, seqlen) in enumerate(zip(pad_sents, word_seq_lengths)):
        for idy, (sent, sentlen) in enumerate(zip(seq, seqlen)):           
            word_seq_tensor[idx,idy,:sentlen] = torch.LongTensor(sent)
           

    word_seq_lengths  = word_seq_lengths.view(batch_size*max_seq_len,) #(batch_size * max_seq_len) * 1

    for idx, (label, seqlen) in enumerate(zip(labels,sent_seq_lengths)):
        label_seq_tensor[idx, :seqlen] = torch.LongTensor(label)
        mask[idx,:seqlen] = torch.Tensor([1])

    word_seq_lengths, word_perm_idx = word_seq_lengths.sort(0, descending=True)

    word_seq_tensor = word_seq_tensor.view(batch_size * max_seq_len, -1) #(batch_size * max_seq_len) * max_sent_len
    word_seq_tensor = word_seq_tensor[word_perm_idx]

    feature_seq_tensors = []
    for idx in range(feature_num):
        feature_seq_tensors.append(torch.zeros((batch_size, max_seq_len, pattern_fea_size)))
    #pattern feature
    if feature_num > 0:
        for i in range(0, len(features)):
            feature = features[i]
            #print("feature of seq:",feature)
            for sid in range(len(feature)):
                #print("feature of sentence:",feature[sid])
                for feaid in feature[sid]:
                    feature_seq_tensors[0][i][sid][feaid] = 1
    
    ### deal with char
    max_word_len = 1


    # pad_chars (batch_size, max_seq_len, max_sent_len)
    if data.use_char:
        pad_chars = []
        for sid in range(len(chars)):
            pad_chars.append([chars[sid][idx] + [[0]] * (max_sent_len-len(chars[sid][idx])) for idx in range(len(chars[sid]))])
            #print(pad_chars[-1])

        pad_chars = [pad_chars[sid] + [[[0]]*max_sent_len] * (max_seq_len - len(pad_chars[sid])).item() for sid in range(len(pad_chars))] #batch_size * max_seq_len * (varied length sents)
       
        length_list = []
        for sid in range(len(pad_chars)):
            length_list.append([list(map(len, pad_char))  for pad_char in pad_chars[sid]])
            temp_max = max(map(max, length_list[-1]))
            if temp_max > max_word_len:
                max_word_len = temp_max
        

        char_seq_tensor = torch.zeros((batch_size, max_seq_len, max_sent_len, max_word_len), dtype = torch.long)
        char_seq_lengths = torch.LongTensor(length_list)

        for idx, (seq, seqlen) in enumerate(zip(pad_chars, char_seq_lengths)):
            for idy, (sent, sentlen) in enumerate(zip(seq, seqlen)):
                for idz, (word, wordlen) in enumerate(zip(sent, sentlen)):
                    char_seq_tensor[idx, idy, idz,:wordlen] = torch.LongTensor(word)

        char_seq_tensor = char_seq_tensor.view(batch_size*max_seq_len,-1) #(batch_size * max_seq_len) * (max_sent_len * max_word_len)
        char_seq_tensor = char_seq_tensor[word_perm_idx].view(batch_size*max_seq_len*max_sent_len, -1)#(batch_size * max_seq_len * max_sent_len) * max_word_len

        char_seq_lengths = char_seq_lengths.view(batch_size*max_seq_len,-1)
        char_seq_lengths = char_seq_lengths[word_perm_idx].view(batch_size*max_seq_len*max_sent_len,)#(batch_size * max_seq_len * max_sent_len) *1

        char_seq_lengths, char_perm_idx = char_seq_lengths.sort(0, descending=True)
        char_seq_tensor = char_seq_tensor[char_perm_idx]
        _, char_seq_recover = char_perm_idx.sort(0, descending=False)

    else:
        char_seq_tensor = torch.zeros(1)
        char_seq_lengths = torch.zeros(1)
        char_seq_recover = torch.zeros(1)


    _, word_seq_recover = word_perm_idx.sort(0, descending=False)

    if gpu:
        word_seq_tensor = word_seq_tensor.to(device)
        for idx in range(feature_num):
            feature_seq_tensors[idx] = feature_seq_tensors[idx].to(device)
        word_seq_lengths = word_seq_lengths.to(device)
        word_seq_recover = word_seq_recover.to(device)
        label_seq_tensor = label_seq_tensor.to(device)
        char_seq_tensor = char_seq_tensor.to(device)
        char_seq_lengths = char_seq_lengths.to(device)
        char_seq_recover = char_seq_recover.to(device)
        mask = mask.to(device)

    return word_seq_tensor,feature_seq_tensors, word_seq_lengths, word_seq_recover, char_seq_tensor, char_seq_lengths, char_seq_recover, label_seq_tensor, mask, batch_size, max_seq_len



def data_initialization(data):

    dataset = data.build_alphabet(data.data_dir, data.feature_dir)

    data.fix_alphabet()
    #data.word_alphabet.save(data.model_dir,"word_alphabet")
    
    data.data_texts, data.data_Ids = data.read_instance(dataset,data.word_alphabet,data.char_alphabet,data.label_alphabet, data.feature_alphabets)



def lr_decay(optimizer, epoch, decay_rate, init_lr):
    lr = init_lr/(1+decay_rate*epoch)
    print(" Learning rate is set as:", lr)
    for param_group in optimizer.param_groups:
        param_group['lr'] = lr
    return optimizer

def train(data):    
    print("Training model...")
    #data.show_data_summary()
    print(data.device)
    model = SeqModel(data).to(data.device)

    loss_function = nn.NLLLoss()
    if data.optimizer.lower() == "sgd":
        optimizer = optim.SGD(model.parameters(), lr=data.HP_lr, momentum=data.HP_momentum,weight_decay=data.HP_l2)
    elif data.optimizer.lower() == "adagrad":
        optimizer = optim.Adagrad(model.parameters(), lr=data.HP_lr, weight_decay=data.HP_l2)
    elif data.optimizer.lower() == "adadelta":
        optimizer = optim.Adadelta(model.parameters(), lr=data.HP_lr, weight_decay=data.HP_l2)
    elif data.optimizer.lower() == "rmsprop":
        optimizer = optim.RMSprop(model.parameters(), lr=data.HP_lr, weight_decay=data.HP_l2)
    elif data.optimizer.lower() == "adam":
        optimizer = optim.Adam(model.parameters(), lr=data.HP_lr, weight_decay=data.HP_l2)
    else:
        print("Optimizer illegal: %s"%(data.optimizer))
        exit(1)

    best_dev = -10
    best_idx = 0

    for idx in range(data.HP_iteration):
        epoch_start = time.time()
        temp_start = epoch_start
        print("Epoch: %s/%s" %(idx,data.HP_iteration))
        if data.optimizer == "SGD":
            optimizer = lr_decay(optimizer, idx, data.HP_lr_decay, data.HP_lr)
        instance_count = 0
        sample_id = 0
        sample_loss = 0
        total_loss = 0
        right_token = 0
        whole_token = 0

        seqlen_list = [len(item[1]) for item in data.train_Ids]
        seqlen = torch.LongTensor(seqlen_list)
        #print(seqlen[1:50])
        seqlen, seq_perm_idx = seqlen.sort(0, descending=True)
        #print(seqlen[1:50])
        seq_perm_idx = seq_perm_idx.numpy()
        seq_perm_idx.astype(int)
        seq_perm_idx.tolist()
        #seq_perm_idx_list = [idx for idx in seq_perm_idx]
        #print(seq_perm_idx[1:50])
        #print(data.train_texts[seq_perm_idx[1]])
        #print(data.train_texts[seq_perm_idx[2]])

        #print(seq_perm_idx_list[1:50])
        data.train_Ids = [data.train_Ids[index] for index in seq_perm_idx]
        #input(" ")

        #random.shuffle(data.train_Ids)
        ## set model in train model
        model.train()
        model.zero_grad()
        batch_size = data.HP_batch_size
        batch_id = 0
        train_num = len(data.train_Ids)
        total_batch = train_num//batch_size+1
        for batch_id in range(total_batch):
            start = batch_id*batch_size
            end = (batch_id+1)*batch_size
            if end >train_num:
                end = train_num
            instance = data.train_Ids[start:end]
            if not instance:
                continue
            #print(batch_id)
            #batchify_start = time.time()
            if config['feature_path']== "":
                batch_word, batch_features, batch_wordlen, batch_wordrecover, batch_char, batch_charlen, batch_charrecover, batch_label, mask, batch_size, max_seq_len  = batchify_with_label(instance, data.HP_gpu, data.device, 0)
            else:
                batch_word, batch_features, batch_wordlen, batch_wordrecover, batch_char, batch_charlen, batch_charrecover, batch_label, mask, batch_size, max_seq_len  = batchify_with_label(instance, data.HP_gpu, data.device, data.feature_alphabets[0].size())

            #batchify_time = time.time()
            #print("batchify_time", str(batchify_time - batchify_start),str(end))

            #print("batch_features:",len(batch_features))

            instance_count += 1
            #print(batch_id,"batch_label before:", batch_label)
            #print("batch_word:", batch_word.device)
            #print("batch_wordlen:",batch_wordlen.device)
            #print("batch_char:", batch_char.device)
            #print("batch_charlen:",batch_charlen.device)


            loss, tag_seq = model.neg_log_likelihood_loss(batch_word,batch_features, batch_wordlen, batch_char, batch_charlen, batch_charrecover, batch_label, mask, batch_size, max_seq_len, batch_wordrecover)
            #print(batch_id,"batch_label after:", batch_label)
            #input(" ")
            
            sample_loss += loss.item()
            total_loss += loss.item()
            #print(batch_id,total_loss)
            

            if end%2048 == 0:

                temp_time = time.time()
                temp_cost = temp_time - temp_start
                temp_start = temp_time
                #right, whole = predict_check(tag_seq, batch_label, mask)
                #right_token += right
                #whole_token += whole
                #print("     Instance: %s; Time: %.2fs; loss: %.4f; acc: %s/%s=%.4f"%(end, temp_cost, sample_loss, right_token, whole_token,(right_token+0.)/whole_token))
                print("     Instance: %s; Time: %.2fs; loss: %.4f;"%(end, temp_cost, sample_loss))

            if sample_loss > 1e8 or str(sample_loss) == "nan":
                print("ERROR: LOSS EXPLOSION (>1e8) ! PLEASE SET PROPER PARAMETERS AND STRUCTURE! EXIT....")
                exit(1)
            sys.stdout.flush()
            sample_loss = 0
            #print("loss backward")
            loss.backward()

            #print("optimizer step")
            optimizer.step()

            #print("zero grad")
            model.zero_grad()
        temp_time = time.time()
        temp_cost = temp_time - temp_start
        #print("     Instance: %s; Time: %.2fs; loss: %.4f; acc: %s/%s=%.4f"%(end, temp_cost, sample_loss, right_token, whole_token,(right_token+0.)/whole_token))
        print("     Instance: %s; Time: %.2fs; loss: %.4f;"%(end, temp_cost, sample_loss))

        epoch_finish = time.time()
        epoch_cost = epoch_finish - epoch_start
        print("Epoch: %s training finished. Time: %.2fs, speed: %.2fst/s,  total loss: %s"%(idx, epoch_cost, train_num/epoch_cost, total_loss))
        print("totalloss:", total_loss)
        if total_loss > 1e8 or str(total_loss) == "nan":
            print("ERROR: LOSS EXPLOSION (>1e8) ! PLEASE SET PROPER PARAMETERS AND STRUCTURE! EXIT....")
            exit(1)
        
        if idx < 5:
            gc.collect()
            continue

        classes = ['ob','eb','sr','ob#sr','ob#eb','eb#sr','ob#eb#sr','O']
        confusion = np.zeros((len(classes),3))

        speed, acc, p, r, f, _, dev_scores = evaluate(data, model, "dev",confusion)
        dev_finish = time.time()
        dev_cost = dev_finish - epoch_finish

        if data.seg:
            current_score = dev_scores[0][-1]
            print("Dev: time: %.2fs, speed: %.2fst/s; acc: %.4f, p: %.4f, r: %.4f, f: %.4f"%(dev_cost, speed, acc, p, r, f))
        else:
            current_score = acc
            print("Dev: time: %.2fs speed: %.2fst/s; acc: %.4f"%(dev_cost, speed, acc))


        if current_score > best_dev:
            if data.seg:
                print("Exceed previous best f score:", best_dev)
            else:
                print("Exceed previous best acc score:", best_dev)
            model_name = data.model_dir +'.'+ str(idx) + ".model"
            print("Save current best model in file:", model_name)
            torch.save(model.state_dict(), model_name)

            optimizer_name = data.model_dir +'.'+ str(idx) + ".opt"
            torch.save(optimizer.state_dict(),optimizer_name)
            best_dev = current_score
            best_idx = idx

            continue

        if idx - best_idx > 25:
            break

        gc.collect()
        continue

        # ## decode test
        speed, acc, p, r, f, _,_ = evaluate(data, model, "test",confusion)
        test_finish = time.time()
        test_cost = test_finish - dev_finish
        if data.seg:
            print("Test: time: %.2fs, speed: %.2fst/s; acc: %.4f, p: %.4f, r: %.4f, f: %.4f"%(test_cost, speed, acc, p, r, f))
        else:
            print("Test: time: %.2fs, speed: %.2fst/s; acc: %.4f"%(test_cost, speed, acc))
        

def get_fmeasure(predicts,targets, classes):
    confusion = np.zeros((len(classes),len(classes)))
    for idx in range(0,len(predicts)):
        if "B-" in predicts[idx] or "I-" in predicts[idx]:
            syslabel = predicts[idx][2:]
        else:
            syslabel = predicts[idx]

        if "B-" in targets[idx] or "I-" in targets[idx]:
            goldlabel = targets[idx][2:]
        else:
            goldlabel = targets[idx]


        #print(classes)
        #print(syslabel)
        sysidx = classes.index(syslabel)
        goldidx = classes.index(goldlabel)
        confusion[sysidx][goldidx] += 1
       


    F1Score = np.zeros(len(classes))
    Precisions = np.zeros(len(classes))
    Recalls = np.zeros(len(classes))

    tp = 0
    totalgold = 0
    totalsys = 0

    for c in range(len(classes)):
        try:
            if classes[c] != "O":
                tp += confusion[c, c]
                totalgold += np.sum(confusion[:,c])
                totalsys += np.sum(confusion[c, :])

            Precisions[c] = confusion[c, c]/np.sum(confusion[c, :])
            Recalls[c] = confusion[c, c]/np.sum(confusion[:,c])
            F1Score[c] = 2*Precisions[c]*Recalls[c]/(Recalls[c]+Precisions[c])
            #F1Score[c] = 2.*confusion[c, c]/(np.sum(confusion[c, :])+np.sum(confusion[:, c]))
        except:
            print("F-score of class:"+c+" not calculated")

    print(classes)
    #print("Confusion: ")
    #print(confusion)
    #print("F1Score: ")
    #for c, score in enumerate(F1Score):
    for c in range(len(classes)):
        print("{}: {:.2f} {:.2f} {:.2f} {:.2f}".format(classes[c], Precisions[c], Recalls[c], F1Score[c], np.sum(confusion[:, c])))

    totalp = (tp+0.0) /totalsys
    totalr = (tp+0.0) /totalgold
    totalf = 2*totalp*totalr/(totalp+totalr)
    print("{}: {:.2f} {:.2f} {:.2f}".format("total", totalp, totalr, totalf))

    return F1Score,Precisions,Recalls

def get_fmeasure_no_combined(predicts,targets, classes,confusion):
    #confusion = np.zeros((len(classes),3))
    for idx in range(0,len(predicts)):
        if "B-" in predicts[idx] or "I-" in predicts[idx]:
            syslabel = predicts[idx][2:]
        else:
            syslabel = predicts[idx]

        if "B-" in targets[idx] or "I-" in targets[idx]:
            goldlabel = targets[idx][2:]
        else:
            goldlabel = targets[idx]

        for sl in syslabel.split("#"):
            sysidx = classes.index(sl)
            confusion[sysidx][1] += 1
            if sl in goldlabel:
                confusion[sysidx][2] += 1

        for gl in goldlabel.split("#"):
            goldidx = classes.index(gl)
            confusion[goldidx][0] += 1
       

    F1Score = np.zeros(len(classes)+1)
    Precisions = np.zeros(len(classes)+1)
    Recalls = np.zeros(len(classes)+1)

    tp = 0
    totalgold = 0
    totalsys = 0

    for c in range(len(classes)):
        try:
            if classes[c] != "O":
                tp += confusion[c, 2]
                totalgold += confusion[c,0]
                totalsys += confusion[c,1]

            Precisions[c] = confusion[c, 2]/confusion[c,1]
            Recalls[c] = confusion[c, 2]/confusion[c,0]
            F1Score[c] = 2*Precisions[c]*Recalls[c]/(Recalls[c]+Precisions[c])
            #F1Score[c] = 2.*confusion[c, c]/(np.sum(confusion[c, :])+np.sum(confusion[:, c]))
        except:
            print("F-score of class:"+c+" not calculated")

    #print(classes)
    #print("Confusion: ")
    #print(confusion)
    #print("F1Score: ")
    #for c, score in enumerate(F1Score):
    #for c in range(len(classes)):
    #    print("{}: {:.2f} {:.2f} {:.2f} {:.2f}".format(classes[c], Precisions[c], Recalls[c], F1Score[c], confusion[c,0]))

    totalp = (tp+0.0) /totalsys
    totalr = (tp+0.0) /totalgold
    totalf = 2*totalp*totalr/(totalp+totalr)
    #print("{}: {:.2f} {:.2f} {:.2f}".format("total", totalp, totalr, totalf))
    F1Score[-1] = totalf
    Recalls[-1] = totalr
    Precisions[-1] = totalp


    return F1Score,Precisions,Recalls

def evaluate(data, model, name, confusion=None, persysm=None, nbest=None):
    if name == "train":
        instances = data.train_Ids
    elif name == "dev":
        instances = data.dev_Ids
        texts = data.dev_texts
    elif name == 'test':
        instances = data.test_Ids
        texts = data.test_texts
    elif name == 'raw':
        instances = data.raw_Ids
        texts = data.raw_texts
    else:
        print("Error: wrong evaluate name,", name)
        exit(1)
    right_token = 0
    whole_token = 0
    nbest_pred_results = []
    pred_scores = []
    pred_results = []
    gold_results = []

    classes = ['ob','eb','sr','ob#sr','eb#ob','eb#sr','eb#ob#sr','O']
    ## set model in eval model
    model.eval()
    batch_size = data.HP_batch_size
    start_time = time.time()
    train_num = len(instances)
    total_batch = train_num//batch_size+1
    for batch_id in range(total_batch):
        start = batch_id*batch_size
        end = (batch_id+1)*batch_size
        if end > train_num:
            end =  train_num
        instance = instances[start:end]
        text = texts[start:end]

        if not instance:
            continue

        if config['feature_path']== "":
            batch_word, batch_features, batch_wordlen, batch_wordrecover, batch_char, batch_charlen, batch_charrecover, batch_label, mask, batch_size, max_seq_len  = batchify_with_label(instance, data.HP_gpu, data.device, 0)
        else:
            batch_word, batch_features, batch_wordlen, batch_wordrecover, batch_char, batch_charlen, batch_charrecover, batch_label, mask, batch_size, max_seq_len  = batchify_with_label(instance, data.HP_gpu, data.device, data.feature_alphabets[0].size())

        if nbest:
            # Jing: 2018/09/18: not fix yet
            scores, nbest_tag_seq = model.decode_nbest(batch_word,batch_features, batch_wordlen, batch_char, batch_charlen, batch_charrecover, mask, nbest)
            nbest_pred_result = recover_nbest_label(nbest_tag_seq, mask, data.label_alphabet, batch_wordrecover)
            nbest_pred_results += nbest_pred_result
            pred_scores += scores[batch_wordrecover].cpu().data.numpy().tolist()
            ## select the best sequence to evalurate
            tag_seq = nbest_tag_seq[:,:,0]
        else:
            tag_seq = model(batch_word, batch_features, batch_wordlen, batch_char, batch_charlen, batch_charrecover, mask ,batch_size, max_seq_len, batch_wordrecover)
        # print("tag:",tag_seq)
        pred_label, gold_label = recover_label(tag_seq, batch_label, mask, data.label_alphabet)
        pred_results += pred_label
        gold_results += gold_label

        #for sent in text:           
        #    print(sent[0],sent[3],sent[4])
        #print(pred_label)
        #print(gold_label)
        #input(" ")
        if persysm != None:
            for idx in range(len(text)):
                sent = text[idx]
                sid = sent[4][0]
                system = sid[:sid.index("-")]
                if system not in persysm:
                    persysm[system] = {"pred":[],"gold":[],"sentence":[],"sentence_id":[]}
                persysm[system]["pred"].extend(pred_label[0])
                persysm[system]["gold"].extend(gold_label[0])
                persysm[system]["sentence"].extend([(" ").join(s) for s in sent[0]])
                persysm[system]["sentence_id"].extend(sent[4])

            #print("persystem:")
            #for system in persysm:
            #    print(persysm)
            #input(" ")


    decode_time = time.time() - start_time
    speed = len(instances)/decode_time

    preds = []
    golds = []

    preds = [p for pred in pred_results for p in pred]
    golds = [g for gold in gold_results for g in gold]

    #print("pred length:",len(preds))
    #print("gold length:", len(golds))
    #input(" ")

    fs,ps, rs = get_fmeasure(preds,golds,classes)
    
    if confusion != None:
        print("not combined results")
        fs,ps, rs = get_fmeasure_no_combined(preds,golds,classes,confusion)
    pred_scores = [fs,ps,rs]

    acc = 0
    p = 0
    r = 0
    f = 0
    #if data.status != "decode" or data.status != "decodeonefold" :
           #acc, p, r, f = get_ner_fmeasure(gold_results, pred_results, data.tagScheme)
    #if nbest:
    #    return speed, acc, p, r, f, nbest_pred_results, pred_scores
    return speed, acc, p, r, f, pred_results, pred_scores


def recover_label(pred_variable, gold_variable, mask_variable, label_alphabet):
    """
        input:
            pred_variable (batch_size, sent_len): pred tag result
            gold_variable (batch_size, sent_len): gold result variable
            mask_variable (batch_size, sent_len): mask variable
    """

    batch_size = gold_variable.size(0)
    seq_len = gold_variable.size(1)
    mask = mask_variable.cpu().data.numpy()
    pred_tag = pred_variable.cpu().data.numpy()
    gold_tag = gold_variable.cpu().data.numpy()
    batch_size = mask.shape[0]
    pred_label = []
    gold_label = []
    for idx in range(batch_size):
        pred = [label_alphabet.get_instance(pred_tag[idx][idy]) for idy in range(seq_len) if mask[idx][idy] != 0]
        gold = [label_alphabet.get_instance(gold_tag[idx][idy]) for idy in range(seq_len) if mask[idx][idy] != 0]
        assert(len(pred)==len(gold))
        pred_label.append(pred)
        gold_label.append(gold)
    return pred_label, gold_label

def str2bool(string):
    if string == "True" or string == "true" or string == "TRUE":
        return True
    else:
        return False

def build_pretrain_embedding(embedding_path, word_alphabet, embedd_dim=100, norm=True):
    #load word embedding
    embed = gensim.models.KeyedVectors.load_word2vec_format(embedding_path, binary = False)
    #word_vectors = torch.FloatTensor(embed.syn0)

    alphabet_size = word_alphabet.size()
    scale = np.sqrt(3.0 / embedd_dim)
    pretrain_emb = np.empty([word_alphabet.size(), embedd_dim])
    match = 0
    not_match = 0

    for word, index in word_alphabet.iteritems():

        try:
            vector = embed.get_vector(word)
            if norm:
                pretrain_emb[index,:] = norm2one(vector)
            else:
                pretrain_emb[index,:] = vector
            match += 1
        except KeyError:
            print(word)
            not_match += 1
            pretrain_emb[index,:] = np.random.uniform(-scale, scale, [1, embedd_dim])

    print("Embedding:\n     pretrain word:%s, match:%s, oov:%s, oov%%:%s"%(len(embed.syn0), match, not_match, (not_match+0.)/alphabet_size))
    return pretrain_emb, embedd_dim

def norm2one(vec):
    root_sum_square = np.sqrt(np.sum(np.square(vec)))
    return vec/root_sum_square

def load_model_decode(data, name, confusion, persysm = None):
    print("Load Model from file: ", data.load_model_dir)
    model = SeqModel(data).to(data.device)
    ## load model need consider if the model trained in GPU and load in CPU, or vice versa
    # if not gpu:
    #     model.load_state_dict(torch.load(model_dir))
    #     # model.load_state_dict(torch.load(model_dir), map_location=lambda storage, loc: storage)
    #     # model = torch.load(model_dir, map_location=lambda storage, loc: storage)
    # else:
    #     model.load_state_dict(torch.load(model_dir))
    #     # model = torch.load(model_dir)
    model.load_state_dict(torch.load(data.load_model_dir))

    print("Decode %s data, nbest: %s ..."%(name, data.nbest))
    start_time = time.time()
    speed, acc, p, r, f, pred_results, pred_scores = evaluate(data, model, name, confusion, persysm, data.nbest)
    end_time = time.time()
    time_cost = end_time - start_time
    if data.seg:
        print("%s: time:%.2fs, speed:%.2fst/s; acc: %.4f, p: %.4f, r: %.4f, f: %.4f"%(name, time_cost, speed, acc, p, r, f))
    else:
        print("%s: time:%.2fs, speed:%.2fst/s; acc: %.4f"%(name, time_cost, speed, acc))
    return pred_results, pred_scores

def decode_one_doc(data, name,confusion = None, persysm = None):
    print("Load Model from file: ", data.load_model_dir)
    model = SeqModel(data).to(data.device)
    ## load model need consider if the model trained in GPU and load in CPU, or vice versa
    # if not gpu:
    #     model.load_state_dict(torch.load(model_dir))
    #     # model.load_state_dict(torch.load(model_dir), map_location=lambda storage, loc: storage)
    #     # model = torch.load(model_dir, map_location=lambda storage, loc: storage)
    # else:
    #     model.load_state_dict(torch.load(model_dir))
    #     # model = torch.load(model_dir)
    model.load_state_dict(torch.load(data.load_model_dir))

    print("Decode %s data, nbest: %s ..."%(name, data.nbest))
    start_time = time.time()
    speed, acc, p, r, f, pred_results, pred_scores = evaluate(data, model, name, confusion, persysm, data.nbest)
    end_time = time.time()
    time_cost = end_time - start_time
    if data.seg:
        print("%s: time:%.2fs, speed:%.2fst/s; acc: %.4f, p: %.4f, r: %.4f, f: %.4f"%(name, time_cost, speed, acc, p, r, f))
    else:
        print("%s: time:%.2fs, speed:%.2fst/s; acc: %.4f"%(name, time_cost, speed, acc))
    return pred_results, pred_scores

def add_features(data):
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
       
        for i in range(0,len(patfeatures)):
            patfea = patfeatures[i][2:].split(" ")
            patfea = [fea.strip() for fea in patfea]
            if patfea == ['']:
                patfea = []
            #input(patfea)
            for fea in patfea:
                self.feature_alphabets[0].add(fea)
            pattern_feamap[feainstances[i].strip()] = patfea
    else:
        self.feature_alphabets.append(Alphabet('pattern'))
        print(self.feature_alphabets[0])
        input(" ")

def config_file_to_dict(input_file):
    config = {}
    fins = open(input_file,'r').readlines()
    for line in fins:
        if len(line) > 0 and line[0] == "#":
            continue
        if "=" in line:
            pair = line.strip().split('#',1)[0].split('=',1)
            item = pair[0]
            if item=="feature":
                if item not in config:
                    feat_dict = {}
                    config[item]= feat_dict
                feat_dict = config[item]
                new_pair = pair[-1].split()
                feat_name = new_pair[0]
                one_dict = {}
                one_dict["emb_dir"] = None
                one_dict["emb_size"] = 10
                one_dict["emb_norm"] = False
                if len(new_pair) > 1:
                    for idx in range(1,len(new_pair)):
                        conf_pair = new_pair[idx].split('=')
                        if conf_pair[0] == "emb_dir":
                            one_dict["emb_dir"]=conf_pair[-1]
                        elif conf_pair[0] == "emb_size":
                            one_dict["emb_size"]=int(conf_pair[-1])
                        elif conf_pair[0] == "emb_norm":
                            one_dict["emb_norm"]=str2bool(conf_pair[-1])
                feat_dict[feat_name] = one_dict
                # print "feat",feat_dict
            else:
                if item in config:
                    print("Warning: duplicated config item found: %s, updated."%(pair[0]))
                config[item] = pair[-1]
    return config


def str2bool(string):
    if string == "True" or string == "true" or string == "TRUE":
        return True
    else:
        return False

if __name__ == '__main__':

    parser = argparse.ArgumentParser(description='Tuning with NCRF++')
    # parser.add_argument('--status', choices=['train', 'decode'], help='update algorithm', default='train')
    parser.add_argument('--config',  help='Configuration File' )
    parser.add_argument('--path',help='Path of Data File')
    parser.add_argument('--outpath',help='Path of Output File')

    args = parser.parse_args()
    data = Data()
    data.HP_gpu = torch.cuda.is_available()
    config = config_file_to_dict(args.config)
    
    data.read_config(config)
    status = data.status.lower()
    #print("Seed num:",seed_num)

    if data.status == 'decode':
        print("MODEL: decode")

        modelnames = []
        model_path = config["load_model_dir"]
        feature_path = config["feature_path"]

        data.dset_dir = config["dset_dir"]
        data.load(data.dset_dir)

        data.word_emb_dir = config["word_emb_dir"]
        #data.word_emb_dim = int(config["word_emb_dim"])
        data.raw_dir = args.path

        data.load_model_dir = config["load_model_dir"]

        #data.show_data_summary()
        #input(" ")

        data.pretrain_word_embedding, data.word_emb_dim = build_pretrain_embedding(data.word_emb_dir, data.word_alphabet, data.word_emb_dim, False)

        print("raw_dir:",data.raw_dir)
        print("model_dir:",data.load_model_dir)
        dataset = data.generate_instance(data.raw_dir, feature_path, ".xml")
        data.raw_texts, data.raw_Ids = data.read_instance(dataset,data.word_alphabet,data.char_alphabet,data.label_alphabet, data.feature_alphabets)

        decode_results, pred_scores = decode_one_doc(data, 'raw')

        with open(args.outpath,'w') as outf:
            writer = csv.writer(outf, delimiter=';', quoting=csv.QUOTE_MINIMAL)
            for instance_text, decode_result in zip(data.raw_texts,decode_results):
                for instance_id, pred in zip(instance_text[-1],decode_result):
                    sent_id = instance_id[instance_id.rfind("-")+1:]
                    instance_id = instance_id[0:instance_id.rfind("-")]
                    bug_id = instance_id[instance_id.rfind("-")+1:]
                    system_id = instance_id[0:instance_id.rfind("-")]
                    writer.writerow([system_id,bug_id,sent_id,pred])
                


















    