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

val start_year = 2013
val end_year = 2016

val start_month = 1
val end_month = 12

var del_count = new HashMap[String, Long]()

for (this_year <- start_year to end_year) {
for (this_month <- start_month to end_month) {
    if (this_year == 2013 && this_month < 2) {
    //no-op
    } else {
      val del = sc.textFile("deletion-ia/" + this_year + "-" + this_month)
      del_count += (this_year + "-" + this_month -> del.count())
    }
  }
}