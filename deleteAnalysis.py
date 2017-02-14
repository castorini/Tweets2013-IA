import glob

trecFeb = set(open("trecT13Feb").readlines())
trecMarch = set(open("trecT13March").readlines())
originalTwtIDs = trecFeb.union(trecMarch)

start_year = 2013
end_year = 2016

start_month = 1
end_month = 12

deletionDir = "deletion"
deletedSet = set([])
for year in xrange(start_year, end_year + 1):
	for month in xrange(start_month, end_month + 1):
		if year == 2013 and month < 2:
			continue
		else:
			thisFilePath = deletionDir + "/deletedTwtsTREC-" + str(year) + "-" + str(month)
			thisDelSet = set()
			for file in glob.glob(thisFilePath + "/part*"):
				thisDelSet = thisDelSet.union(set(open(file).readlines()))
			# thisDelSet = set(open(deletionDir + "/deletedTwtsTREC-" + str(year) + "-" + str(month)).readlines())
			deletedSet.add(originalTwtIDs.intersection(thisDelSet))
			print str(year) + "-" + str(month), len(deletedSet)
			# originalTwtIDs -= deletedSet