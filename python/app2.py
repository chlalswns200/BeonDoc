from flask import Flask
from flask import request
from flask import jsonify

import os
import warnings

import numpy as np
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import accuracy_score
import re
import datasets
from datasets import load_metric
from transformers import AutoTokenizer, AutoModelForSequenceClassification, TrainingArguments, Trainer
from sklearn.model_selection import train_test_split
from transformers import EarlyStoppingCallback
import torch
from torch.utils.data import Dataset, DataLoader
import joblib 

warnings.filterwarnings(action='ignore')


from sklearn.ensemble import ExtraTreesClassifier


import torch
from torch import nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
import gluonnlp as nlp
import numpy as np
from tqdm import tqdm, tqdm_notebook
from kobert.utils import get_tokenizer
from kobert.pytorch_kobert import get_pytorch_kobert_model
from transformers import AdamW
from transformers.optimization import get_cosine_schedule_with_warmup
import random

from hanspell import spell_checker


#----------klue bert -------------------------------------------------

model_checkpoint = "klue/bert-base"
batch_size = 8
task = "nli"
RANDOM_SEED = 17


klue_tokenizer = AutoTokenizer.from_pretrained(model_checkpoint, use_fast=False)

class KlueBERTDataset(Dataset):
    def __init__(self, dataset, sent_key, label_key, bert_tokenizer):
        
        self.sentences = [ bert_tokenizer(i,truncation=True,return_token_type_ids=False) for i in dataset[sent_key] ]
        
        if not label_key == None:
            self.mode = "train"
        else:
            self.mode = "test"
            
        if self.mode == "train":
            self.labels = [np.int64(i) for i in dataset[label_key]]
        else:
            self.labels = [np.int64(0) for i in dataset[sent_key]]

    def __getitem__(self, i):
        if self.mode == "train":
            self.sentences[i]["label"] = self.labels[i]
            # self.sentences[i]["target"] = self.labels[i]
            return self.sentences[i]

        else:
            return self.sentences[i]

    def __len__(self):
        return (len(self.labels))


model = AutoModelForSequenceClassification.from_pretrained("model/Klue_BERT_Model")

reloaded_trainer = Trainer(
                    model = model,
                    tokenizer = klue_tokenizer
                    )

#----------klue bert -------------------------------------------------


#----------et -------------------------------------------------
TfidfVectorizer = joblib.load("model/Extra_Tree_Model/TfidfVectorizer.pkl")
et = joblib.load("model/Extra_Tree_Model/ET_Model.pkl")

tfidf_vectorizer = TfidfVectorizer
# # #----------et -------------------------------------------------


# # #----------kobert -------------------------------------------------
device = torch.device('cpu')

## Setting parameters
max_len = 64
batch_size = 64
warmup_ratio = 0.1
num_epochs = 8
max_grad_norm = 1
log_interval = 100
learning_rate =  5e-5

#bert 모델, vocab 불러오기
bertmodel, vocab = get_pytorch_kobert_model()

class KOBERTDataset(Dataset):
    def __init__(self, dataset, sent_idx, label_idx, bert_tokenizer, max_len,
                 pad, pair):
        transform = nlp.data.BERTSentenceTransform(
            bert_tokenizer, max_seq_length=max_len, pad=pad, pair=pair)

        self.sentences = [transform([i[sent_idx]]) for i in dataset]
        self.labels = [np.int32(i[label_idx]) for i in dataset]

    def __getitem__(self, i):
        return (self.sentences[i] + (self.labels[i], ))

    def __len__(self):
        return (len(self.labels))


kobert_tokenizer = get_tokenizer()
tok = nlp.data.BERTSPTokenizer(kobert_tokenizer, vocab, lower=False)
#BERTDataset 클래스 이용, TensorDataset으로 만들어주기

class BERTClassifier(nn.Module):
    def __init__(self,
                 bert,
                 hidden_size = 768,
                 num_classes = 74, 
                 dr_rate=None,
                 params=None):
        super(BERTClassifier, self).__init__()
        self.bert = bert
        self.dr_rate = dr_rate
        self.classifier = nn.Linear(hidden_size , num_classes)
        if dr_rate:
            self.dropout = nn.Dropout(p=dr_rate)
    
    def gen_attention_mask(self, token_ids, valid_length):
        attention_mask = torch.zeros_like(token_ids)
        for i, v in enumerate(valid_length):
            attention_mask[i][:v] = 1
        return attention_mask.float()

    def forward(self, token_ids, valid_length, segment_ids):
        attention_mask = self.gen_attention_mask(token_ids, valid_length)
        _, pooler = self.bert(input_ids = token_ids, token_type_ids = segment_ids.long(), attention_mask = attention_mask.float().to(token_ids.device))
        if self.dr_rate:
            out = self.dropout(pooler)
        return self.classifier(out)


kobert = torch.load('model/Kobert_Model/Kobert_Model.pt',map_location=device)
kobert.load_state_dict(torch.load('model/Kobert_Model/Kobert_Model.pt_state_dict.pt',map_location=device))
kobert.eval()

def calc_accuracy(X,Y):
    max_vals, max_indices = torch.max(X, 1)
    train_acc = (max_indices == Y).sum().data.cpu().numpy()/max_indices.size()[0]
    return train_acc


#----------kobert -------------------------------------------------

#----------------------api-------------------------------------

app = Flask(__name__)

columns = ['string']

@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'

# 진단 api
# 입력받은 데이터와 기초문진 데이터를 바탕으로 예상되는 질병 3개와 확률을 받는다
@app.route('/api/start', methods=['POST'])
def test():
    input = request.get_json("string") # 데이터를 입력 받은 뒤 저장
    input = input['string'] # 모델에 맞게 형식 변환
    input = pd.DataFrame([input],columns=columns) # 모델에 맞게 형식 변환
    input['target'] = 0 # 모델에 맞게 형식 변환
    input['string'] = input['string'].apply(lambda x: spell_checker.check(x).checked) # 맞춤법 검사
    
#-------------------여기서부터 함수 사용------------------------------

#------------klue--------------
    data_test = KlueBERTDataset(input, "string", "target", klue_tokenizer)
    klue_pred_test = reloaded_trainer.predict(data_test)
    klue_pred_test = klue_pred_test[0]
#------------klue--------------

#------------et--------------
    x_test = tfidf_vectorizer.transform(input['string'])
    y_test = input['target']
    et_pred_test = et.predict_proba(x_test)
#------------et--------------

#------------kobert--------------
    input.to_csv('test.tsv', sep='\t', index=None)

    dataset_real_test = nlp.data.TSVDataset('test.tsv', field_indices=[0,1],num_discard_samples=1)
    real_test_set = KOBERTDataset(dataset_real_test, 0, 1, tok, max_len, True, False)
    real_test_dataloader = torch.utils.data.DataLoader(real_test_set, batch_size=batch_size, num_workers=4)

    test_acc = 0
    kobert_test_label = []
    proba_list = []

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(tqdm_notebook(real_test_dataloader)):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)
        valid_length= valid_length
        label = label.long().to(device)
        out = kobert(token_ids, valid_length, segment_ids)
        test_acc += calc_accuracy(out, label)
        kobert_test_label.append(label.tolist())
        proba_list.append(out)

    prob_list = []
    line = []
    for i in range(len(proba_list)):
        for j in range(len(proba_list[i])):
            line.append(proba_list[i][j].tolist())
            
    prob_list.append(line)  
    norm_kobert_test_prob = prob_list/np.linalg.norm(prob_list)
#------------kobert--------------
    #----------앙상블 결과 -------------------------------------------------
    norm_klue_test_prob = klue_pred_test/np.linalg.norm(klue_pred_test)
    norm_kobert_test_prob = norm_kobert_test_prob.reshape(1,74)
    norm_et_test_prob = et_pred_test/np.linalg.norm(et_pred_test)
    test_prob = (norm_klue_test_prob+norm_kobert_test_prob+norm_et_test_prob)/3
    pred = np.argmax(test_prob,1)
    prob = np.max(test_prob,1)
    target_encoder = joblib.load("model/Extra_Tree_Model/Target_Encoder.pkl")
    target_encoder.inverse_transform(pred)
    predict = (norm_klue_test_prob+norm_kobert_test_prob+norm_et_test_prob)/3
    predict = predict.reshape(-1,)
    index_list = []
    prob_list = []
    for i in range(3):
        index = np.argmax(predict)
        prob = np.max(predict)
        predict[index] = -1
        index_list.append(index)
        prob_list.append(prob)
    
    target_encoder = joblib.load("model/Extra_Tree_Model/Target_Encoder.pkl")
    predict = target_encoder.inverse_transform(index_list)
    predict = predict.tolist()

    # 확률 변환
    for i in prob_list:
        i = i*100
        i = str(round(i,2))
        predict.append(i)

    os.remove('test.tsv')
    
    return jsonify(predict)  # 결과 반환

if __name__ == '__main__':
    app.run()


#----------------------api-------------------------------------