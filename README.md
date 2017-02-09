# Texamine
To examine if the Trec13 tweets were from Spritzer

### Tweets'13 results

Month  | Overlap
-------|--------
Feb    | 92.00
March  | 97.54
avg    | 94.77


Track/BM25 | MAP  | P30
-----------|------|------
MB2013     |0.2212|0.4194
MB2014     |0.3634|0.6030

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
