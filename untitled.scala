import org.warcbase.spark.matchbox._
import org.warcbase.spark.matchbox.TweetUtils._
import org.warcbase.spark.rdd.RecordRDD._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.text.SimpleDateFormat
import java.util.TimeZone
import scala.collection.mutable.HashMap
import org.apache.spark.rdd.RDD
import scala.io.Source
import org.apache.hadoop.fs._


val trec13 = RecordLoader.loadTweets("/collections/tweets/Tweets2013/", sc)
val trec13_count_all = trec13.count
val trec13_count_unique = trec13.distinct().count
    
val ia13_feb = RecordLoader.loadTweets("02ex", sc)
val ia13_march = RecordLoader.loadTweets("03ex", sc)
val ia13_union = ia13_feb.union(ia13_march)

val ia13_count_all = ia13_union.count
val ia13_count_unique = ia13_union.distinct().count

val common13 = trec13.intersection(ia13_union)
val overlap13 =  common13.count / ia13_count_unique.toFloat

val deletes_trec13 = trec13.filter(t => (t \ "delete") != JNothing)
.map(t => {
implicit val formats = DefaultFormats
(t \ "delete" \ "status" \ "id").extract[String]
})

val deletes_ia13 = ia13_union.filter(t => (t \ "delete") != JNothing)
.map(t => {
implicit val formats = DefaultFormats
(t \ "delete" \ "status" \ "id").extract[String]
})

println("### Collection stats: ####")
println("--- counts---")
println("TREC-2013-all (T):" + trec13_count_all + " IA-2013-all (I):" + ia13_count_all)

println("\n---unique counts---")
println("TREC-2013-unique:" + trec13_count_unique + " IA-2013-unique:" + ia13_count_unique)

println("\n---overlap---")
println("TREC-2013: " + overlap13)

println("\n---deletes---")
println("TREC-deletes-all: " + deletes_trec13.count + " TREC-deletes-unique: " + deletes_trec13.distinct().count)
println("IA-deletes-all: " + deletes_ia13.count + " IA-deletes-unique: " + deletes_ia13.distinct().count)


// Do the same for TREC15 collection

val trec15_east = RecordLoader.loadTweets("/collections/tweets/TREC2015-tweets/us-east", sc)
val trec15_west = RecordLoader.loadTweets("/collections/tweets/TREC2015-tweets/us-west", sc)
val trec15_union = trec15_east.union(trec15_west)
val trec15_count_all = trec15_union.count
val trec15_count_unique = trec15.distinct().count
    
val ia15_union = RecordLoader.loadTweets("07ex", sc).map(tweet => (tweet.id, tweet.createdAt))
	.filter(tuple => {
		 val createdAt = tuple._2
		 val dateIn = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
		 val dateOut = new SimpleDateFormat("yyyy-MM-dd")
		 val d = try {
		   dateOut.format(dateIn.parse(createdAt))
		 } catch {
		   case e: Exception => null
		 }
		 val yearMonth = year + "-" + "%02d".format(7)
		 d != null && d.equals(yearMonth)
	})


val ia15_count_all = ia15_union.count
val ia15_count_unique = ia15_union.distinct().count

val common15 = trec15.intersection(ia15_union)
val overlap15 =  common15.count / ia15_count_unique.toFloat

val deletes_trec15 = trec15.filter(t => (t \ "delete") != JNothing)
.map(t => {
implicit val formats = DefaultFormats
(t \ "delete" \ "status" \ "id").extract[String]
})

val deletes_ia15 = ia15_union.filter(t => (t \ "delete") != JNothing)
.map(t => {
implicit val formats = DefaultFormats
(t \ "delete" \ "status" \ "id").extract[String]
})

println("### Collection stats: ####")
println("--- counts---")
println("TREC-2015-all (T):" + trec15_count_all + " IA-2015-all (I):" + ia15_count_all)

println("\n---unique counts---")
println("TREC-2015-unique:" + trec15_count_unique + " IA-2015-unique:" + ia15_count_unique)

println("\n---overlap---")
println("TREC-2015: " + overlap15)

println("\n---deletes---")
println("TREC-deletes-all: " + deletes_trec15.count + " TREC-deletes-unique: " + deletes_trec15.distinct().count)
println("IA-deletes-all: " + deletes_ia15.count + " IA-deletes-unique: " + deletes_ia15.distinct().count)