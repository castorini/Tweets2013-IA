import numpy as np
import matplotlib.pyplot as plt
from __builtin__ import range as _range
import matplotlib
from datetime import datetime, timedelta
from collections import OrderedDict
import matplotlib.lines as mlines

dates = ["2013-04-01", "2016-12-31"]
start, end = [datetime.strptime(_, "%Y-%m-%d") for _ in dates]
time = OrderedDict(((start + timedelta(_)).strftime(r"%b'%y"), None) for _ in xrange((end - start).days)).keys()

relDocInit = 19656
qrelInit = 129264

delRelDoc = [0,0,0,37,47,32,39,37,31,25,29,23,27,29,29,26,28,27,31,28,22,22,21,32,12,15,32,24,26,22,24,29,15,27,19,23,27,9,17,21,20,30,30,20,38]
delQrels = []

relDocRetained = []
relsRetained = []

sum = 0
for i in delRelDoc:
	sum += i
	trecRetained.append(1 - (sum * 1.0)/relDocInit)

print trecRetained

sum = 0
for i in delQrels:
	sum += i
	iaRetained.append(1 - (sum * 1.0)/qrelInit)

print iaRetained

# red dashes, blue squares and green triangles
x = np.array(xrange(len(deleteTREC)))

fig, ax = plt.subplots()
handles, labels = ax.get_legend_handles_labels()
ax.legend(handles, labels)


plt.xlabel('Time')
plt.ylabel('Fraction of reldocs')
plt.plot(x, deleteTREC, 'r-*', x, deletedIA, 'g-o')
index = np.arange(min(x), max(x)+1, 6)

labels = []
for x in index:
	labels.append(time[x])

ax.set_xticks(index)
ax.set_xticklabels(labels)

red_line = mlines.Line2D([], [], color='red', marker='*',
                          markersize=15, label='TREC')
green_line = mlines.Line2D([], [], color='green', marker='o',
                          markersize=15, label='IA')
plt.legend(handles=[red_line, green_line])
plt.savefig("deleted-trec.png")
plt.show()

x = np.array(xrange(len(trecRetained)))
fig, ax = plt.subplots()
handles, labels = ax.get_legend_handles_labels()
ax.legend(handles, labels)
plt.xlabel('Time')
plt.ylabel('Fraction of tweets')
plt.plot(x, trecRetained, 'r-*', x, iaRetained, 'g-^')

index = np.arange(min(x), max(x)+1, 6)
labels = []
for x in index:
	labels.append(time[x])
ax.set_xticks(index)
ax.set_xticklabels(labels)


red_line = mlines.Line2D([], [], color='red', marker='*',
                          markersize=15, label='TREC')
green_line = mlines.Line2D([], [], color='green', marker='^',
                          markersize=15, label='IA')
plt.legend(handles=[red_line, green_line])
plt.savefig("retained.png")
plt.show()