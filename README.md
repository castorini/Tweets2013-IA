# Tweets2013 Collection from the Internet Archive

This is the documentation corresponding to the experiments reported in the short paper titled, 
"Finally, a Downloadable Test Collection of Tweets". You can find the paper [here](link-goes-here).

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

========================================

#### To index the tweets collection:

First, rename and copy the files to a single directory. Since we are repeating the experiments here for TREC2013, first copy
the the unzipped files corresponding to Feb first into this new directory. Now, move the march file, but prepend with march

```
while read -r file; do new_file=$(rev <<< "$file" | sed 's~/~_~' | rev); new2=$(rev <<< "$new_file" | sed 's~/~_~' | rev) ; echo $file "../../twitter-tools/twitterData/March$new2"; done < <(find . -type f)
```

Delete the tweets before indexing. We do not know how far Jimmy waited before deleting the tweets. It seems like up until June. The collection size is 243.

```
for d in */ ; do   cat $d/*; done > twt-id-deleted.txt
cat twt-id-deleted.txt | wc -l
```
should give you 15722282

That is 259057030 - 15722282 = 243334748 is the total number of statuses we have.

Now, repeat the overlap experiments after deleting the Trec delete tweets.

### TREC'15:

2015-07-19-00.gz to 2015-07-29-23.gz

Month  | Overlap
-------|--------
Jul    | 97.68%

 
### TREC'16:
2016-08-01-00.gz to 2016-08-11-23.gz
Archives not available
 
### To-do:
Fix the versioning
 
Automate it across the collections

#### Delete Analysis:

```
spark-submit --jars lib/warcbase-core-0.1.0-SNAPSHOT-fatjar.jar --class ca.uwaterloo.cs.texamine.deleteAnalysisTREC target/sigir17-1.0-SNAPSHOT.jar --num-executors 50 --executor-cores 10 --executor-memory 40G 
```

For IA:

Download 2013 collection

```
./extract.sh
```

Rename and copy it to HDFS. 
As of now, 2014 adn 2015 is perfect. Make more space and extract 2016 too



# Revisiting

Since some things seem to have gone wrong, I will be repeating the experiments here

### Overlap analysis:

Things that we need:
 - number of json statuses delivered, unique statuses <- for Feb and March 2013
 - number of 'delete' tags
 - do the above for both trec and archive collection
 - reprensent them in set notation
 
The collections used:

TREC: /collections/tweets/Tweets2013/
IA: 

 Table goes here. 
 
 ### Dealing with Spark
 Joblistener exceptions are common and do not affect the job. Ignore them!
 
 For large files, repartition them first.;
 ```
 val trec13 = RecordLoader.loadTweets("/collections/tweets/Tweets2013/", sc).repartition(sc.defaultParallelism * 50)
 ```

### Ad-hoc retrieval

Report effective measures from 5 sources:
1. Thrift: waiting for Jimmy to send the API details
2. T - D (this is done, just update the tables)
3. D - T (done, update the tables)
4. deletion on T (up to today)
5. deltion on A (up to today)

```
sh target/appassembler/bin/SearchTweets -index tweets2013-IA-index-del -bm25 -topics topics.microblog2013.txt -output run.ia.del.mb13.txt
```
 ### Stat tests:
 
 Pairs		 		             | p-value(MAP)|p-value (P30)
------------------------|-------------|--------------
T-D (13/02 - 13/06)	    |0.7993				   |0.9999
A-D (13/02 - 13/06)	    |0.4162				   |0.7374
T-D (13/02 - 16/12)			  |0.5051				   |0.8957
A-D (13/02 - 16/12)			  |0.2658				   |0.7385
T-D\* (13/02 - 16/12)			  |0.8982				   |0.8957
A-D\* (13/02 - 16/12)			  |0.5469				   |0.7385
\*: with modified qrels
