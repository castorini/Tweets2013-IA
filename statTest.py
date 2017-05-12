# -*- coding: utf-8 -*-
import argparse
from scipy import stats


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Query reformulation')
    parser.add_argument('-one', dest='one', action='store',
                    help='path to first file')
    parser.add_argument('-two', dest='two', action='store',
                    help='path to second file')
    parser.add_argument('--p30', dest='p30', action='store_true',
                    help='choose if it\'s p30', default=False)    
    
    args = parser.parse_args()

    tail1 = []
    tail2 = []
    with open(args.one) as f:
        for line in f:
            det = line.strip().split()

            if det[1] == "all":
                continue

            if args.p30:
                if det[0] != "P_30":
                    continue
                else:

                    tail1.append(float(det[2]))
            else:
                tail1.append(float(det[2]))

    with open(args.two) as g:
        for line in g:
            det = line.strip().split()

            if det[1] == "all":
                continue

            if args.p30:
                if det[0] != "P_30":
                    continue
                else:
                    tail2.append(float(det[2]))
            else:
                tail2.append(float(det[2]))


    print tail1
    print tail2
    paired_sample = stats.ttest_ind(tail1, tail2, equal_var = False)
    print paired_sample

    print "The t-statistic is %.3f and the p-value is %.4f." % paired_sample