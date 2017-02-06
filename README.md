# Texamine
To examine if the Trec13 tweets were from Spritzer

### Tweets'13 results

Month  | Overlap
-------|--------
Feb    | 99.01
March  | 97.54

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
 
### TREC'16:
2016-08-01-00.gz to 2016-08-11-23.gz
 
### To-do:
Fix the versioning
 
Automate it across the collections
