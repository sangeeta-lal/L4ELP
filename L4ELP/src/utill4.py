
import re
from nltk.stem.porter import PorterStemmer
from nltk.corpus import stopwords

#=======================================================================#
# == This is a utility file used to create various utility function ====#
#=======================================================================#

def clean(val):
   # val ="hello + remove a*b a<>b abc(10) int a[20] int b=10^10 int fun(){print hello c/d /*smd*/ //dfdn @override<?:?>};"
    val = re.sub(r"[\+\*%-/&|^=!]", " ", val)
    val = re.sub(r"[<>\{\}\(\)\[\]]", " ", val)
    val = re.sub(r"[@#$_\\\'\":;\.,\?0-9]", " ", val)
    val = re.sub(r" +"," ", val)
    return val

def camel_case_convert(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    s1= re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
    s2 = s1.split("_")
    
    final= " "
    for s in s2:
        final = final+" "+s
    final = final.strip()
    return final    
    
def stem_it(val):
    #print "val=", val@1
    temp = " ".join(PorterStemmer().stem_word(word) for word in val.split(" "))
    #print "temp=", temp
    return temp

def  remove_stop_words(text):
    stop_list = stopwords.words('english')
    text = ' '.join([word for word in text.split() if word not in stop_list])
    return text



