import numpy as np
import matplotlib.pyplot as plt
from __builtin__ import range as _range
import matplotlib
from datetime import datetime, timedelta
from collections import OrderedDict
import matplotlib.lines as mlines
import matplotlib.dates as mdates
import pandas as pd

# dates = ["2013-04-01", "2016-12-31"]
# start, end = [datetime.strptime(_, "%Y-%m-%d") for _ in dates]
# time = OrderedDict(((start + timedelta(_)).strftime(r"%b'%y"), None) for _ in xrange((end - start).days)).keys()
years = mdates.YearLocator()
months = mdates.MonthLocator(interval=3)
monthsFmt = mdates.DateFormatter("%b")
yearsFmt = mdates.DateFormatter('\n\n%Y')

trecInit = 248404504
iaInit = 234337730

deleteTREC = [1892830,1718977,1479376,1436161,1389720,1249337,1163842,992211,966189,978910,1024663,973743,897354,
782632,706149,689264,686838,678814,805860,718554,755832,825397,714000,850846,639360,590819,668909,678904,703422,
609956,613536,531573,502135,524935,568255,577736,629606,722240,772374,676166,619425,598423,622813,641814,543084]

deletedIA = [1840772,1642331,1412768,1370821,1325967,1191822,1110248,946632,921946,934305,978265,929797,857162,
746841,674187,658113,656472,648750,769793,686387,721853,788474,682205,812191,611277,564919,638595,648594,672366,
583007,586573,508093,479737,501342,542719,552013,601553,690173,738581,646541,592093,572145,595599,613219,517966]

trecRetained = []
iaRetained = []
sum = 0
for i in deleteTREC:
	sum += i
	trecRetained.append((trecInit - sum)/1000000.0)

# print trecRetained

sum = 0
for i in deletedIA:
	sum += i
	iaRetained.append((iaInit - sum)/1000000.0)

# print iaRetained
#
deleteTREC = [round(x/1000000.0,6) for x in deleteTREC]
deletedIA = [round(x/1000000.0,6) for x in deletedIA]

s = pd.date_range('03/31/2013', '12/30/2016', freq='M')
dts = s.to_pydatetime()

red_line = mlines.Line2D([], [], color='blue', marker='o',
                          markersize=15, label=r'$\mathcal{T}$')
green_line = mlines.Line2D([], [], color='green', marker='s',
                          markersize=15, label=r'$\mathcal{A}$')
fig, ax = plt.subplots()
handles, labels = ax.get_legend_handles_labels()
plt.xlabel('Time')
plt.ylabel('Number of Deletions (millions)')
ax.legend(handles, labels)
ax.plot(dts, deleteTREC, 'b-o', dts, deletedIA, 'g-s')
ax.xaxis.set_minor_locator(months)
ax.xaxis.set_minor_formatter(monthsFmt)
ax.xaxis.set_major_locator(years)
ax.xaxis.set_major_formatter(yearsFmt)
plt.setp(ax.xaxis.get_minorticklabels(), rotation=90)
# ax.autoscale_view()
# fig.autofmt_xdate()
plt.legend(handles=[red_line, green_line])
plt.ylim((0, 2.5))
plt.tight_layout()
plt.grid(True, which='both')
plt.savefig("deleted-trec.svg", format='svg', dpi=1200)
plt.show()

# # red dashes, blue squares and green triangles
# x = np.array(xrange(len(deleteTREC)))

# index = [0,8,20,32,44]
#
# labels = []
# for x in index:
# 	labels.append(time[x])
#
# ax.set_xticks(index)
# ax.set_xticklabels(labels)
#
# red_line = mlines.Line2D([], [], color='blue', marker='d',
#                           markersize=15, label='T')
# green_line = mlines.Line2D([], [], color='green', marker='s',
#                           markersize=15, label='A')
# plt.legend(handles=[red_line, green_line])
# plt.grid()
# plt.ylim((0, 2.5))
# plt.savefig("deleted-trec.png")
# plt.show()

# x = np.array(xrange(len(trecRetained)))
fig, ax = plt.subplots()
handles, labels = ax.get_legend_handles_labels()
ax.legend(handles, labels)
plt.xlabel('Time')
plt.ylabel('Total Number of Tweets (millions)')


plt.plot(dts, trecRetained, 'b-o', dts, iaRetained, 'g-s')
ax.xaxis.set_minor_locator(months)
ax.xaxis.set_minor_formatter(monthsFmt)
ax.xaxis.set_major_locator(years)
ax.xaxis.set_major_formatter(yearsFmt)
plt.setp(ax.xaxis.get_minorticklabels(), rotation=90)

red_line = mlines.Line2D([], [], color='blue', marker='o',
                          markersize=15, label=r'$\mathcal{T}$')
green_line = mlines.Line2D([], [], color='green', marker='s',
                          markersize=15, label=r'$\mathcal{A}$')
plt.legend(handles=[red_line, green_line])
plt.ylim((0, 300) )
plt.grid(True, which='both')
plt.tight_layout()
plt.savefig("retained.svg", format='svg', dpi=1200)
plt.show()
