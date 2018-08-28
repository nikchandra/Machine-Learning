
# coding: utf-8

# In[81]:

import sklearn


# In[82]:

X = [[0,0],[0,1],[1,0],[1,1]]


# In[83]:

Y = [0, 1, 1, 0]


# In[84]:

from sklearn.neural_network import MLPClassifier


# In[85]:

model = sklearn.neural_network.MLPClassifier(
    activation='relu', max_iter=10000, hidden_layer_sizes=(4,2))


# In[86]:

model.fit(X, Y)


# In[87]:

print('score:', model.score(X, Y))
print('predictions:', model.predict(X))
print('expected:', ([0, 1, 1, 0]))


# In[ ]:




# In[ ]:



