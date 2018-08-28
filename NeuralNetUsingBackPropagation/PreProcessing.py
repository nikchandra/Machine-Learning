#**
 #* @author Nikhil Kumar Chandra
 #* @author Shruti Harihar
 #*/
import numpy as np
import pandas as pd
import math
import re 
import sys

#Load the data file. 
data = pd.read_csv(sys.argv[1], sep = "\s+|,\s+|,", engine='python',header = None)

rows = len(data.index)
columns = len(data.columns.values)

#Remove any missing values in the data
miss=['?']
for i in range(columns):
	data = data[ ~data[i].isin(miss) ]

data = data.drop_duplicates()
data = data.dropna()
length = len(data.columns)

#Normalize the numerical data
a = len(data.columns) - 1
i = 0 
while (a > 0):
	if(isinstance(data[i][0],(int,float,np.int64))):
		#normalize(data,i)
		data[i] = (data[i] - data[i].mean())/(data[i].max() - data[i].min())
	a = a - 1
	i = i + 1

#Standerdize the Numeric class attribute
if(isinstance(data[len(data.columns ) - 1][0],(int,float,np.int64))):
		#normalize(data,i)
	for k  in range(0,len(data.index ) ):
		if( data[len(data.columns ) - 1][k] > data[len(data.columns ) - 1].mean()):
			data[len(data.columns ) - 1][k] = 1
		else:
			data[len(data.columns ) - 1][k] = 0

#Standerdize the categorical data
df = pd.get_dummies(data)

#Print the Post Processsed data into a output file
df.to_csv('output.txt',index = False)







