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

val start_year = 2013
val end_year = 2016

val start_month = 1
val end_month = 12


var trecTweets_all = HashMap[String, RDD[org.json4s.JValue]]()
for (this_year <- start_year to end_year) {
for (this_month <- start_month to end_month) {
    if (this_year == 2013 && this_month < 2) {
    //no-op
    } else {
      val trecTweets_east = RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-east/" + this_year +
      "-%02d".format(this_month) + "/", sc)
      val trecTweets_west = RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-west/" + this_year +
      "-%02d".format(this_month) + "/", sc)
      val twt_union = trecTweets_east.union(trecTweets_west)
      trecTweets_all += ((this_year + "-" + this_month) -> twt_union)
    }
  }
}
FileSystem.get(sc.hadoopConfiguration).delete(new Path("deletion"), true)

var pairHashMap = HashMap[String, String]()
val output = trecTweets_all.map(tweet => {
  tweet._2.filter(t => (t \ "delete") != JNothing)
 .map(t => {
  implicit val formats = DefaultFormats
  (t \ "delete" \ "status" \ "id").extract[String]
 })
}.saveAsTextFile("deletion/deletedTwtsTREC-" + tweet._1))
