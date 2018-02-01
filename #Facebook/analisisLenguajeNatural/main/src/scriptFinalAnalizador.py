import es_nlp as lang_tools
import numpy as np
from entrenador import nbclassifier,known_words,extract_feature
import matplotlib.pyplot as plt

import pandas as pd
#ide = 'PP'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/PP_facebook_comments.csv')
#ide = 'PSOE'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/psoe_facebook_comments.csv')
#ide = 'PODEMOS'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/ahorapodemos_facebook_comments.csv')
#ide = 'Cs'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/cs.ciudadanos_facebook_comments.csv')
#ide = 'JxS'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/juntspelsi_facebook_comments.csv')
#ide = 'ERC'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/Esquerra.ERC_facebook_comments.csv')
#ide = 'CUP'
#df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/unitatpopular_facebook_comments.csv')
ide = 'Vox'
df = pd.read_csv('/Users/franciscocobo/Google Drive/DUIme/#Facebook/scrapingFacebookPython/vox.espana_facebook_comments.csv')

#Aqui cargamos la columna de los datos que nos interesa, la que incluye los comentarios de los posts.
saved_column = df.comment_message #you can also use df['column_name']

#para luego poder hacer plot
yPoints = []
result_array = []

for j in range(1, len(saved_column)):
    exp_count=0;
    exp_value=0;
    t = saved_column[j]
    tokens = lang_tools.tokenize(t) #dividimos el texto en palabras
    for e in tokens:
    	if e in known_words:
    		exp_count+= 1
    		sentiment = nbclassifier.classify(extract_feature(e))
    		exp_value+=float(sentiment)-1

    if exp_count>0:
    	scale_sentiment=exp_value/exp_count
    	if j == 1:
            result_array = scale_sentiment
            yPoints.append(scale_sentiment)
    	else:
    	   result_array = np.append(result_array, scale_sentiment)
    	   yPoints.append(scale_sentiment)

print('Media de valor de comentarios: '+str(np.mean(result_array)))
#{'b', 'g', 'r', 'c', 'm', 'y', 'k', 'w'};

plt.xlabel('Sentimiento').set_fontsize(10)
plt.ylabel('Número de comentarios').set_fontsize(10)
plt.title('Histograma de sentimiento respecto a ' + str(ide) + '. Media: '+ str(np.mean(result_array))).set_fontsize(10)
plt.hist(yPoints)
plt.savefig('sentimientos'+str(ide)+'.png')
plt.show()
#quit()
