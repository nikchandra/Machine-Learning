
from collections import defaultdict as ddict
from math import *
import glob
import sys

class ClassObject(object):
    def __init__(self, classname):
        self.table = ddict(lambda: 0)
        self.classname = classname
        self.wordsCount = 0
        self.totalWordsCount = 0
        self.classFileCount = 0
    
class NaiveBayesModel(object):
    def __init__(self, trainingFolder, testingFolder):
        self.trainingRootFolder = trainingFolder
        self.testingRootFolder = testingFolder
        self.classes = []
        self.totalValidWords = 0
        self.totalWords = 0
        self.totalFiles = 0
        self.stop_words = self.get_stop_words()
        self.sub_folders = {"alt.atheism", "comp.graphics", "talk.politics.guns", "sci.space", "rec.sport.hockey"};

    def training(self):
        for folder in self.sub_folders:
            classInfo = ClassObject(folder)
            filePaths = glob.glob(self.trainingRootFolder + "/" + folder + "/*")
            for filepath in filePaths:
                file = open(filepath)
                line = file.readline().strip()
                while line != "" and not line.startswith("Lines: "):
                    line = file.readline().strip()

                if line == "" or not line.startswith("Lines: "):
                    continue

                classInfo.classFileCount = classInfo.classFileCount + 1
                linesCount = line.split(": ")
                lines = int(linesCount[1])
                for i in range(lines):
                    newLine = file.readline()
                    
                    if(newLine != "" and len(newLine.strip()) != 0):
                        tokens = newLine.split()
                        for token in tokens:
                            classInfo.totalWordsCount = classInfo.totalWordsCount + 1
                            if token not in self.stop_words:
                                classInfo.wordsCount +=  1
                                classInfo.table[token] += 1
            self.classes.append(classInfo)
        for classobject in self.classes:
            self.totalFiles += classobject.classFileCount
            self.totalValidWords += classobject.wordsCount
            self.totalWords += classobject.totalWordsCount

    def testing(self):
        for folder in self.sub_folders:
            wrongClassified = 0
            totalTestingFilesofTheClass = 0
            filePaths = glob.glob(self.trainingRootFolder + "/" + folder + "/*")
            for filepath in filePaths:
                estimation = []
                for i in range(5):
                    estimation.append(self.classes[i].classFileCount/self.totalFiles)
                file = open(filepath)
                line = file.readline().strip()
                while line != "" and not line.startswith("Lines: "):
                    line = file.readline().strip()

                if line == "" or not line.startswith("Lines: "):
                    continue

                linesCount = line.split(": ")
                lines = int(linesCount[1])
                for i in range(lines):
                    newLine = file.readline()                    
                    if(newLine != "" and len(newLine.strip()) != 0):
                        tokens = newLine.split()
                        for token in tokens:
                            if token not in self.stop_words:
                                for j in range(5):
                                    estimatingClass = self.classes[j]
                                    estimation[j] += log10((estimatingClass.table[token]+1)/(len(estimatingClass.table)+estimatingClass.wordsCount))
                finalEstimate = 0
                maxValue = sys.float_info.max * -1
                for i in range(5):
                    if estimation[i] > maxValue:
                        maxValue = estimation[i]
                        finalEstimate = i
                totalTestingFilesofTheClass += 1
                if not folder == self.classes[finalEstimate].classname:
                    wrongClassified += 1
            errorRate = wrongClassified/totalTestingFilesofTheClass
            print("Testing on " + folder + "-- Error Rate:" + str(errorRate))
            del estimation[:]
            
    def get_stop_words(self):
        stop_words = []
        stopwords = open("stop_words.txt")
        for word in stopwords:
                stop_words.append(word.strip())
        return stop_words
		
                 
                
def main():
    x = NaiveBayesModel(sys.argv[1],sys.argv[2])
    #x = NaiveBayesModel('20news-bydate/20news-bydate-train','20news-bydate/20news-bydate-test')
    x.training()
    x.testing()

if __name__ == "__main__":
    main()
