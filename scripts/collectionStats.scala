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


val trec13 = RecordLoader.loadTweets("/collections/tweets/Tweets2013/", sc).repartition(sc.defaultParallelism * 50)
val ia13_union = RecordLoader.loadTweets("ArchivedTweets2013/", sc).repartition(sc.defaultParallelism * 50)

val trec13_count_all = trec13.count
val trec13_count_unique = trec13.distinct().count()
val ia13_count_all = ia13_union.count()
val ia13_count_unique = ia13_union.distinct().count()

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