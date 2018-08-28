No Machine Learning libraries are used in this implementation

Command to run program
--------------------------------
py naivebayes.py pathToTrainingData pathToTestingData

example 
py naivebayes.py 20news-bydate/20news-bydate-train 20news-bydate/20news-bydate-test

requirements
---------------------------------
stop_words.txt need to be in the same directory as naivebayes.py
The data set can be downloaded from the link http://qwone.com/~jason/20Newsgroups/20news-bydate.tar.gz

The program runs on 5 subfolders
----------------------------------
alt.atheism
comp.graphics
talk.politics.guns
sci.space
rec.sport.hockey

The output will be as follows
----------------------------------
Testing on comp.graphics-- Error Rate:0.005136986301369863
Testing on alt.atheism-- Error Rate:0.0020833333333333333
Testing on sci.space-- Error Rate:0.011824324324324325
Testing on rec.sport.hockey-- Error Rate:0.013422818791946308
Testing on talk.politics.guns-- Error Rate:0.0

Assumptions
-------------------
- each word is independent of the other
- stop words have no role in classification and can be ignored