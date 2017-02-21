# Texamine
To examine if the Trec13 tweets were from Spritzer

First build the package
```
mvn clean package
```

### Tweets'13 results

Month  | Overlap
-------|--------
Feb    | 92.00
March  | 97.54
avg    | 94.77


Track/BM25 | MAP  | P30
-----------|------|------
MB2013     |0.2248|0.4200
MB2014     |0.3718|0.6145

#### Verify checksum!
``` 
md5sum archiveteam-twitter-stream-2013-02.tar . 
```
#### Extract 

```
tar -xvf archiveteam-twitter-stream-2013-02.tar
```
#### Rename
```
 while read -r file; do new_file=$(rev <<< "$file" | sed 's~/~_~' | rev); new2=$(rev <<< "$new_file" | sed 's~/~_~' | rev) ; mv $file "../03ex/$new2"; done < <(find . -type f)
 ```
#### Copy the from HDFS to local:

Use `hadoop fs -get archivedFeb .` 

Automate this script


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
 
