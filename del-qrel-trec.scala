import org.warcbase.spark.matchbox._
import org.warcbase.spark.matchbox.TweetUtils._
import org.warcbase.spark.rdd.RecordRDD._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.text.SimpleDateFormat
import java.util.TimeZone
import scala.collection.mutable.HashMap
import scala.collection.mutable.Set
import org.apache.spark.rdd.RDD
import scala.io.Source
import org.apache.hadoop.fs._

// val trecFeb = sc.textFile("trecFeb")
// val trecMarch = sc.textFile("trecMarch")
// var original_ids = trecFeb.union(trecMarch)
var rel_docs = sc.textFile("qrels.tweet13.rel.txt")
var qrels = sc.textFile("qrels.tweets13.txt")

println("Done reading original ids")

val start_year = 2013
val end_year = 2016

val start_month = 1
val end_month = 12

FileSystem.get(sc.hadoopConfiguration).delete(new Path("deletion"), true)
var qrelCount = new HashMap[String, Long]()
var relCount = new HashMap[String, Long]()

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
      println("Processing:" + this_year + "-" + this_month)

      val output = twt_union.filter(t => (t \ "delete") != JNothing)
      .map(t => {
        implicit val formats = DefaultFormats
        (t \ "delete" \ "status" \ "id").extract[String]
       })

       val intersection_reldocs = rel_docs.intersection(output)
       val intersection_qrels = qrels.intersection(output)
       rel_docs = rel_docs.subtract(intersection_reldocs)
       qrels = qrels.subtract(intersection_qrels)
       relCount += (this_year + "-" + this_month -> intersection_reldocs.count())
       qrelCount += (this_year + "-" + this_month -> intersection_qrels.count())
       output.saveAsTextFile("deletion/" + this_year + "-" + this_month)
    }
  }
}