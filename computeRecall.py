trecFeb = set(open("trecFeb").readlines())
trecMarch = set(open("trecMarch").readlines())
archivedFeb = set(open("archivedFeb").readlines())
archivedMarch = set(open("archivedMarch").readlines())

febRecovered = len(trecFeb.intersection(archivedFeb)) / float(len(trecFeb))
marchRecovered = len(trecMarch.intersection(archivedMarch)) / float(len(trecMarch))

print "feb:{0} march:{1}", febRecovered, marchRecovered