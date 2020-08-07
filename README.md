# Tweets2013 Collection from the Internet Archive

This is the documentation corresponding to the experiments reported in the short paper titled, 
"Finally, a Downloadable Test Collection of Tweets". You can find the paper [here](https://cs.uwaterloo.ca/~jimmylin/publications/Sequiera_Lin_SIGIR2017.pdf).

#### Download the collection:

Download the the tweet datasets from the following sources:

- [ArchiveTeam JSON Download of Twitter Stream 2013-02](https://archive.org/details/archiveteam-twitter-stream-2013-02)
- [ArchiveTeam JSON Download of Twitter Stream 2013-03](https://archive.org/details/archiveteam-twitter-stream-2013-03)


#### Verify the checksum!
``` 
md5sum archiveteam-twitter-stream-2013-02.tar .
md5sum archiveteam-twitter-stream-2013-03.tar .  
```

#### Extract 
```
tar -xvf archiveteam-twitter-stream-2013-02.tar 
tar -xvf archiveteam-twitter-stream-2013-03.tar
```

#### Combine
Copy the extracted contents into a folder named `ArchivedTweets2013`
 
#### Copy to HDFS:

Use `hadoop fs -put ArchivedTweets2013 .` 

#### Collection stats:
To obtain the collection stats as presented in Table 1 and Table 2 in the paper:

1. Clone and setup warcbase. Follow [this](https://github.com/lintool/warcbase) for detailed instructions.
2. Start a spark-shell:
```
spark-shell   --jars lib/warcbase-core-0.1.0-SNAPSHOT-fatjar.jar --num-executors 50 \
 --executor-cores 10 --executor-memory 40G 
```
3. Run [collectionStats.scala](/scripts/collectionStats.scala)

##### Table1: Collection Statistics:

Source                    | Count
--------------------------|---------
&#124;T&#124;           | 259,035,603
&#124;A&#124;           | 246,615,368
&#124;T U A&#124;       | 260,382,756
&#124;T and A&#124; | 245,268,215
&#124;T - A&#124;       | 13,767,388
&#124;A - T&#124;       | 1,347,153


##### Table2: Overlap Analysis:
Collection                                     | Overlap
-----------------------------------------------|---------
1 - &#124;(T - A)&#124;/&#124;T&#124;          | 94.69%
1 - &#124;(A - T)&#124;/&#124;A&#124;          | 99.45%
&#124;T and A&#124;/&#124;T&#124;              | 94.69%


### Deletion Stats:
Run the following to obtain the results as in Table 3:
```
python deletionAnalysis.py
```

Source| Count
------|-------
&#124;T&#124; |259,035,603
&#124;A&#124; |246,615,368
&#124;D(13/02-13/03)&#124; |10,631,099
&#124;D(13/04-13/06)&#124; |5,091,183
&#124;D(13/07-13/12)&#124; |7,197,460
&#124;D(14/01-14/12)&#124; |96,98,613
&#124;D(15/01-15/12)&#124; |7,928,857
&#124;D(16/01-16/12)&#124; |7,496,871
&#124;T − D(13/02-13/03)&#124; |248,404,504
&#124;A − D(13/02-13/03)&#124; |234,337,730
&#124;T − D(13/02-13/06)&#124; |243,313,321
&#124;A − D(13/02-13/06)&#124; |230,893,086
&#124;T − D(13/02-13/12)&#124; |236,115,861
&#124;A − D(13/02-13/12)&#124; |223,695,626
&#124;T − D(13/02-14/12)&#124; |226,417,248
&#124;A − D(13/02-14/12)&#124; |213,997,013
&#124;T − D(13/02-15/12)&#124; |218,488,391
&#124;A − D(13/02-15/12)&#124; |206,068,156
&#124;T − D(13/02-16/12)&#124; |210,991,520
&#124;A − D(13/02-16/12)&#124; |198,571,285


The delete list used in the paper can be downloaded from [here](https://drive.google.com/drive/folders/0B2u_nClt6NbzckdycjRGY0Vqc2c?usp=sharing)

### Missing qrels

Source                        | missing reldocs  |missing qrels
------------------------------|------------------|--------------
&#124;T − D(13/02-13/12)&#124;| 220 (1.12%)      |1,820 (1.41%)
&#124;A − D(13/02-13/12)&#124;| 209 (1.06%)      | 1,707 (1.32%)
&#124;T − D(13/02-14/12)&#124;| 539 (2.74%)      | 4,456 (3.45%)
&#124;A − D(13/02-14/12)&#124;| 513 (2.61%)      | 4,190 (3.24%)
&#124;T − D(13/02-15/12)&#124;| 816 (4.15%)      | 6,576 (5.09%)
&#124;A − D(13/02-15/12)&#124;| 776 (3.95%)      | 6,193 (4.79%)
&#124;T − D(13/02-16/12)&#124;| 1,095 (5.57%)    | 8,500 (6.58%)
&#124;A − D(13/02-16/12)&#124;| 1,042 (5.30%)    | 7,997 (6.19%)

### To index the tweets collection:

Create .bz2 delete list for Internet Archive:
```
cd deletes-ia
for d in */ ; do   cat $d/*; done > ../delete-list-13-ia-now.txt
bzip2 delete-list-13-ia-now.txt
```

Create .bz2 delete list for Trec Microblog:
```
cd deletes-trec
for d in */ ; do   cat $d/*; done > ../delete-list-13-trec-now.txt
bzip2 delete-list-13-trec-now.txt
```

##### Index

Clone and build [Anserini](https://github.com/lintool/Anserini)
```
git clone https://github.com/castorini/Anserini.git
cd Anserini && mvn clean package appassembler:assemble
```
Checkout to `twitter-search`
```
git checkout twitter-search
```
Index the IA collections:
```
sh target/appassembler/bin/IndexTweets -collection <path of IA collection> -deletes delete-list-13-ia-now.txt -index \
tweets2013-IA-index-del -optimize -store
```

##### Search
```
sh target/appassembler/bin/SearchTweets -index tweets2013-IA-index-del -bm25 -topics topics.microblog2013.txt -output run.ia.del.mb13.txt
```

#### Evaluate

Download the latest `trec_eval` code:
```
wget http://trec.nist.gov/trec_eval/trec_eval_latest.tar.gz
tar -xvf trec_eval_latest.tar.gz  
cd trec_eval.9.0/ 
make
```
Evaluate on different configuations to obtain the results as shown in Table 5 of the paper:
```
eval/trec_eval.9.0/trec_eval qrels.mb.txt run.a-d.mb.txt
```
