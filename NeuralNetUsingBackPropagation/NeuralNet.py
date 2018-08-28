#**
 # @author Nikhil Kumar Chandra
 # @author Shruti Harihar
 #/
import numpy as np
import pandas as pd
import re
import random
import sys

#Class to create a Neuron Node
class Neuron:
	def __init__(self,index,layer):
		self.layer=layer
		self.inweights=[]
		self.index=index
		self.adjList = []
		self.revadjList = []
		self.input = 0
		self.output = 0
		self.bias = False
		self.delta = 0		

#Method to parse the data and find the input and output columns of the data
def parse_data(data):
	columns = len(data.columns.values)
	class_num = data.columns.values[columns - 1].split('_')[0]
	for i in data.columns.values:
		if class_num in i:
			output.append(i)
		else:
			input.append(i)

#Method to create the nodes in the NeuralNet
def Create_nodes(input,output,hidden_no,hidden_val):
	count=0
	Node_List=[]
	for i in input:
		a = Neuron(count,0)
		Node_List.append(a)
		count=count+1

	for i in range(1,hidden_no+1):
		for j in range(0,hidden_val[i-1]):
			b = Neuron(count,i)
			Node_List.append(b)
			count=count+1

			a = Neuron(count,i)
			Node_List.append(a)
			count=count+1

			
			b.bias = True
			b.adjList.append(a)
			a.revadjList.append(b)
			b.output = 1
			weights[b.index,a.index] = random.uniform(0,1)
	for i in output:
		b = Neuron(count,hidden_no+1)
		Node_List.append(b)
		count=count+1

		a = Neuron(count,hidden_no+1)
		Node_List.append(a)
		count=count+1

		
		b.bias = True
		b.adjList.append(a)
		a.revadjList.append(b)
		b.output = 1
		weights[b.index,a.index] = random.uniform(0,1)
	return Node_List

#Method to add adges between the nodes of NeuralNet
def Create_edges(node_List):
	for node in node_List:
		if node.bias == False: 
			for next in node_List:
				if next.bias == False: 
					if node.layer == next.layer - 1:
						node.adjList.append(next)
						next.revadjList.append(node)
						weights[node.index,next.index] = random.uniform(0,1)

#Method to find the Sigmoid Function
def sigmoid(x):
	return 1/(1 + np.exp(-x))

#Method used for Front Propogation in the neural network
def frontPropogation(rowData,node_List):
	
	for node in node_List:
		if node.layer == 0:
			node.output = rowData[node.index]
		else:
			break

	for node in node_List:
		if node.layer == 0 or node.bias == True:
			for adjNode in node.adjList:
				adjNode.input = adjNode.input + weights[node.index,adjNode.index] * node.output
		else:
			node.output = sigmoid(node.input)
			for adjNode in node.adjList:
				adjNode.input = adjNode.input + weights[node.index,adjNode.index] * node.output

#Method used to update weights
def weightUpdate(node):
	for adjNode in node.adjList:
		delta = learning_rate * adjNode.delta * node.output
		weights[node.index, adjNode.index] = weights[node.index, adjNode.index] + delta


#Helper Function for BackPropogation
def extraSum(node):
	sum = 0
	for i in node.adjList:
		sum = sum + (weights[node.index, i.index] * i.delta)
	return sum

#Helper Function for the BackPropogation
def differentation(node):
	return node.output * (1 - node.output )

#Method used for Back Propogation in the NeralNetwork
def backPropagation(rowData,node_List, outLayer,outputList):
	l = len(outputList)
	for node in reversed(node_List):
		if(node.layer == outLayer):
			node.delta = differentation(node) * (rowData[outputList[l-1]] - node.output)
			if(node.bias == True):
				weightUpdate(node)
				l = l + 1
			l = l -1
		else:

			node.delta = differentation(node) * extraSum(node)
			weightUpdate(node)

#Method used for calculating the Mean Square Error
def meanSquareError(rowData, node_List, outLayer,outputList):
	sum= 0 
	number = 0
	l= len(outputList)
	for node in reversed(node_List):
		if(node.bias == False and node.layer == outLayer):
			
			sum = sum + (rowData[outputList[l-1]]-node.output)**2
			number = number  + 1
			l=l-1
	
	return (sum/number)

#Method used to print the output as required
def printoutput(node_List,outputlayer):
	flag = 0
	layer = 0
	for node in node_List:
		
		if(node.layer != 0 and node.layer != outputlayer):
			if node.layer == layer + 1:
				print
				print "---------HIDDENLAYER : ",node.layer,"------------"
				layer = layer + 1
		elif node.layer == outputlayer and flag == 0:
			print
			print "--------------OutputLayer----------------"
			flag = 1
		if node.layer != 0:
			print
			print "  -Neuron-",node.index," weights:",
		if node.bias == True:
			print "biasNueron"
		

		for prev in node.revadjList:
			print "    ", weights[prev.index, node.index],
	print
	print


#Load the data in the dataframe
data = pd.read_csv(sys.argv[1],sep = "\s+|,\s+|,", engine='python')
train_percent = float(sys.argv[2])  
epoch = int(sys.argv[3]) 
hidden_no = int(sys.argv[4]) 
hidden_val = [] 
arglen = 5 + hidden_no
error_tolerance = 0
for i in range(5,arglen):
	hidden_val.append(int(sys.argv[i]))
	
#print "Please wait for long period of time if you are training on cencus_income_output.txt otherwise change the epoch value to 3-6 "
learning_rate = 0.3

weights = {}
input=[]
output=[]

parse_data(data)

node_List = Create_nodes(input,output,hidden_no,hidden_val)


Create_edges(node_List)


data_train = data.sample( frac = train_percent/100.0 )
data_test = data.loc[~data.index.isin(data_train.index)]

for i in range(0,epoch): 
	sumerror = 0 
	for index, row in data_train.iterrows():

		frontPropogation(row,node_List)

		error = meanSquareError(row,node_List,hidden_no + 1,output)
		sumerror = sumerror + error
		
		backPropagation(row,node_List,hidden_no + 1,output)
		
		for node in node_List:
			node.input = 0

	MSE = sumerror/len(data_train.index)
	if(MSE <= error_tolerance):
		break;

printoutput(node_List, hidden_no + 1)

print "--------------MeanSquareError----------------------"
print "Total training error =  ",MSE



sumerror = 0 
for index, row in data_test.iterrows():

	frontPropogation(row,node_List)

	error = meanSquareError(row,node_List,hidden_no + 1,output)
	sumerror = sumerror + error
	
	backPropagation(row,node_List,hidden_no + 1,output)
	
	for node in node_List:
		node.input = 0

MSE = sumerror/len(data_test.index)
print "Total test error = ",MSE

