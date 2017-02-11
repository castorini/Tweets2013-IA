set -m

for((year=2014;year<=2016;year++)) {
  for((month=1;month<=12;month++)) {
	formatted_link=$(printf "https://archive.org/download/archiveteam-twitter-stream-%s-%02d/archiveteam-twitter-stream-%s-%02d.tar" "$year" "$month" "$year" "$month")
	wget $formatted_link -P "monthData/"
	sleep 3 &
	to_untar=$printf "tar -xvf monthData/archiveteam-twitter-stream-%s-%02d.tar -C monthDataExtracted/%s --warning=no-timestamp" "$year" "$month" "$year")
	$to_untar
	sleep 3 &
  }
}
# Wait for all parallel jobs to finish
while [ 1 ]; do fg 2> /dev/null; [ $? == 1 ] && break; done
