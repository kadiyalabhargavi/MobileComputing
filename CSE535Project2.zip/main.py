# -*- coding: utf-8 -*-
"""
Created on Thu Jan 28 00:44:25 2021

@author: chakati
"""
import cv2
import shutil
import os
import csv
import pandas as pd
from scipy import spatial

from const import ENUMS
from frameextractor import frameExtractor
from handshape_feature_extractor import HandShapeFeatureExtractor

OUTPUT_FILENAME = "Results.csv"
BASE = os.path.dirname(os.path.abspath(__file__))

# import the handfeature extractor class

# =============================================================================
# Get the penultimate layer for trainig data
# =============================================================================
# your code goes here
# Extract the middle frame of each gesture video

trainpath = os.path.join(BASE, 'traindata')
if os.path.exists(trainpath+'/images'):
    shutil.rmtree(trainpath+'/images', ignore_errors=False, onerror=None)

if not os.path.exists(trainpath+'/images'):
    os.mkdir(trainpath+'/images')

trainsavepath = os.path.join(trainpath, 'images')

count = 0

truerows = []
print(trainsavepath)
for gesturefolder in sorted(os.listdir(trainpath)):
        print(gesturefolder)
        if '.DS_Store' in gesturefolder or 'images' in gesturefolder:
            continue
        _gesture = gesturefolder.split('_')[0]
        print(_gesture)
        truerows.append(["%#05d.png" % (count+1), _gesture, ENUMS[_gesture]])
        _ = os.path.join(trainpath, gesturefolder)
        print(_)
        frameExtractor(_, trainsavepath, count)
        count += 1

train_df = pd.DataFrame(
    truerows,
    columns=['filename', 'gesturename', 'target']
)



# =============================================================================
# Get the penultimate layer for test data
# =============================================================================
# your code goes here 
# Extract the middle frame of each gesture video


testpath = os.path.join(BASE, 'test')
if os.path.exists(testpath+'/images'):
    shutil.rmtree(testpath+'/images', ignore_errors=False, onerror=None)

if not os.path.exists(testpath+'/images'):
    os.mkdir(testpath+'/images')

testsavepath = os.path.join(testpath, 'images')

count = 0

for file in sorted(os.listdir(testpath)):
    if '.DS_Store' in file or 'images' in file:
        continue
    _ = os.path.join(testpath, file)
    frameExtractor(_, testsavepath, count)
    count += 1


# =============================================================================
# Recognize the gesture (use cosine similarity for comparing the vectors)
# =============================================================================


feature_obj = HandShapeFeatureExtractor()


def parallel_process_feature(filename):
    img = cv2.imread(filename)
    result = feature_obj.extract_feature(img)
    return result[0]


def cosine_similarity(true_vector, pred_vector):
    result = 1 - spatial.distance.cosine(true_vector, pred_vector)
    return result


train_df['featurevector'] = train_df['filename'].apply(
    lambda filename: parallel_process_feature(trainsavepath+'/'+filename)
)

with open(OUTPUT_FILENAME, 'w',newline='') as csvfile:
    csvwriter = csv.writer(csvfile)
    for file in sorted(os.listdir(testsavepath)):
        _ = os.path.join(testsavepath, file)
        class_vector = parallel_process_feature(_)
        train_df['prediction'] = train_df['featurevector'].apply(
            lambda x: cosine_similarity(x, class_vector)
        )
        output = train_df.loc[train_df['prediction'].idxmax()]['target']
        csvwriter.writerow([output])
