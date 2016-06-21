import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from pylab import *
import numpy as np

#========================================================================================#
# @Sangeeta
# Uses:  This is used to create graph for cross-project  catch-blocks logging prediction
#========================================================================================#

path="F:\\Research\\L4ELP\\result\\"

#path = "E:\\Sangeeta\\Research\\L4ELP\\result\\"

#============= Graph 1 ===========================================#
# Investigation 1: Within vs. cross project logging prediction
#=================================================================#


AVGLF = (83.03 , 88.47,  79.36  ,73.65 , 73.66  ,66.58 , 70.42 , 56.5 , 68.62)
ind = np.arange(9)
width = 0.4

fig, ax = plt.subplots()
barlist = ax.bar(ind, AVGLF, width,color ='r')
# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 13})
barlist[0].set_color('g')
barlist[1].set_color('g')
barlist[2].set_color('g')

ax.set_ylabel('Average LF (%)')
ax.set_xlabel('Source Project -> Target Project')
ax.set_title('INV. 1: Within-project vs. Cross-project Logging Prediction')
ax.set_xticks(ind + width)
labels =  ['TC->TC', 'CS->CS', 'HD->HD', 'CS->TC', 'HD->TC', 'TC->CS', 'HD->CS', 'TC->HD', 'CS->HD']
plt.xticks(ind+width, labels, rotation =340)

red_patch = mpatches.Patch(color='r', label='Cross-project')
green_patch = mpatches.Patch(color='g', label='Within-project')
plt.legend(handles=[red_patch, green_patch])

ylim(0,110)
plt.tight_layout()
#plt.show()
plt.savefig(path+"invest1.pdf")


#============= Graph 2 ===========================================#
# Investigation 2: Single vs.multi-project training
#=================================================================#
plt.close()

AVGLF = ( 73.36, 73.66, 66.58, 70.42, 56.5, 68.62, 72.22, 67.74, 62.77)
ind = np.arange(9)
width = 0.4

fig, ax = plt.subplots()
barlist = ax.bar(ind, AVGLF, width,color ='r')
# add some text for labels, title and axes ticks
plt.rcParams.update({'font.size': 13})
barlist[6].set_color('g')
barlist[7].set_color('g')
barlist[8].set_color('g')

ax.set_ylabel('Average LF (%)')
ax.set_xlabel('Source Project -> Target Project')
ax.set_title('INV. 2: Single vs. Multi-project Training')
ax.set_xticks(ind + width)
labels =  ['CS->TC', 'HD->TC', 'TC->CS', 'HD->CS', 'TC->HD', 'CS->HD', 'CS+HD->TC', 'TC+HD->CS', 'TC+CS->HD']
plt.xticks(ind+width+0.35, labels, rotation =340)

red_patch = mpatches.Patch(color='r', label='Single project')
green_patch = mpatches.Patch(color='g', label='Multi-project')
plt.legend(handles=[red_patch, green_patch])

ylim(0,110)
plt.tight_layout()
#plt.show()
plt.savefig(path+"invest2.pdf")




""



#============= Graph 3  ===========================================#

#==========================3 (A) =================================#
# Investigation 3 (A): Single vs.multi-project training CS->TC
#=================================================================#
#http://matplotlib.org/examples/api/barchart_demo.html
plt.close()


ADA  = ( 50.07, 97.07, 66.06, 50.13, 57.11)   
ADT = ( 78.26, 69.11, 73.4, 74.95, 82.96)
BN = ( 66.36, 82.75, 73.65, 70.39, 77.16)

ind = np.arange(5)
width = 0.2

fig, ax = plt.subplots()
rects1 = ax.bar(ind, ADA, width,color ='r')
rects2 = ax.bar(ind+width, ADT, width, color ='y')
rects3 = ax.bar(ind+width+width, BN, width, color ='g')


# add some text for labels, title and axes ticks
ax.set_ylabel('Values(%)')
ax.set_xlabel('Metric')
ax.set_title('INV. 3(a): Individual Classifier Performance (CS->TC)')
ax.set_xticks(ind + width+width)
ax.set_xticklabels(('AVG. LP', 'AVG. LR', 'AVG. LF', 'AVG. ACC', 'AVG. RA'))

ax.legend((rects1[0], rects2[0], rects3[0] ), ('ADA', 'ADT', 'BN'))
ylim(0,119)
plt.tight_layout()
#plt.show()
plt.savefig(path+"invest3-a.pdf")

#==========================3 (B) =================================#
# Investigation 3 (B): Single vs.multi-project training HD -> CS
#=================================================================#
#http://matplotlib.org/examples/api/barchart_demo.html
plt.close()


NB  = (65.25, 77.9, 70.42, 67.27, 76.8)   
ADT = (88.64, 51.33, 65.01, 72.37, 81.86)


ind = np.arange(5)
width = 0.2

fig, ax = plt.subplots()
rects1 = ax.bar(ind, NB, width,color ='r')
rects2 = ax.bar(ind+width, ADT, width, color ='g')


# add some text for labels, title and axes ticks
ax.set_ylabel('Values(%)')
ax.set_xlabel('Metric')
ax.set_title('INV. 3(b): Individual Classifier Performance (HD->CS)')
ax.set_xticks(ind + width)
ax.set_xticklabels(('AVG. LP', 'AVG. LR', 'AVG. LF', 'AVG. ACC', 'AVG. RA'))

ax.legend((rects1[0], rects2[0] ), ('NB', 'ADT'))
ylim(0,110)
plt.tight_layout()
#plt.show()
plt.savefig(path+"invest3-b.pdf")




#============= Graph 4 ===========================================#
# Investigation 4: Classifier Performance
#=================================================================#
#http://matplotlib.org/examples/api/barchart_demo.html
plt.close()

RF  = ( 88.47 , 51.03 ,50.48)
ADT = (85.54 ,66.58, 65.01)
NB=  (82.81, 66.16, 70.42) 


ind = np.arange(3)
width = 0.2

fig, ax = plt.subplots()
rects1 = ax.bar(ind, RF, width,color ='r')
rects2 = ax.bar(ind+width, ADT, width, color ='y')
rects3 = ax.bar(ind+width+width, NB, width, color ='g')


# add some text for labels, title and axes ticks
ax.set_ylabel('Average LF (%)')
ax.set_xlabel('Source Project -> Target Project')
ax.set_title('INV. 4: Individual Classifier Performance')
ax.set_xticks(ind + width+width)
ax.set_xticklabels(('CS->CS', 'TC->CS', 'HD->CS'))

ax.legend((rects1[0], rects2[0], rects3[0] ), ('RF', 'ADT', 'NB'))

ylim(0,110)
plt.tight_layout()
#plt.show()

plt.savefig(path+"invest4.pdf")


