
# coding: utf-8

# In[263]:

import numpy as np
import pandas as pd


# In[264]:

df = pd.read_csv("http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data", names=["sepal_length","sepal_width","petal_length","petal_width", "class"]) 


# In[265]:

df.head()


# In[266]:

from sklearn.preprocessing import StandardScaler


# In[267]:

scaler = StandardScaler()


# In[268]:

X = df[['sepal_length', 'sepal_width', 'petal_length', 'petal_width']]


# In[269]:

Y = df['class']


# In[270]:

from sklearn.model_selection import train_test_split


# In[271]:

X_train, X_test, Y_train, Y_test = train_test_split(X, Y)


# In[272]:

scaler.fit(X_train)


# In[273]:

X_train = scaler.transform(X_train)
X_test = scaler.transform(X_test)


# In[274]:

from sklearn.neural_network import MLPClassifier


# In[275]:

mlp = MLPClassifier(hidden_layer_sizes=(10,10,10), max_iter=1000)


# In[276]:

mlp.fit(X_train,Y_train)


# In[277]:

predictions = mlp.predict(X_test)


# In[278]:

predictions


# In[279]:

from sklearn.metrics import classification_report,confusion_matrix


# In[280]:

print(confusion_matrix(Y_test,predictions))


# In[281]:

print(classification_report(Y_test,predictions))

