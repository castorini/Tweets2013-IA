package ca.uwaterloo.cs.texamine

import org.warcbase.spark.matchbox._
import org.warcbase.spark.matchbox.TweetUtils._
import org.warcbase.spark.rdd.RecordRDD._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.text.SimpleDateFormat
import java.util.TimeZone

import scala.io.Source
import java.io.IOException

import org.apache.log4j._
import org.apache.hadoop.fs._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
/**
  * Created by rdsequie on 11/02/17.
  */
class deleteAnalysisTREC {
  val log = Logger.getLogger(getClass().getName())
  def main(argv: Array[String]) {
    val conf = new SparkConf().setAppName("Delete Analysis of the Tweets")
    val sc = new SparkContext(conf)

    val start_year = 2013
    val end_year = 2016

    val start_month = 1
    val end_month = 12

    val trecTweets_east_ini =  RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-east/2013-02/", sc)
    val trecTweets_west_ini =  RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-west/2013-02/", sc)
    var trecTweets_all = trecTweets_east_ini.union(trecTweets_west_ini)


    for (this_year <- start_year to end_year) {
      for (this_month <- start_month to end_month) {
        if (this_year == 2013 && (this_month <= 2)) {
          //no-op
        } else {
          val tempTweets_east = RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-east/" + this_year +
            "-%02d".format(this_month) + "/", sc)
          val tempTweets_west = RecordLoader.loadTweets("/collections/tweets/TweetsCrawl/us-west/" + this_year +
            "-%02d".format(this_month) + "/", sc)
          trecTweets_all = trecTweets_all.union(tempTweets_east).union(tempTweets_west)
        }
      }
    }

    trecTweets_all.filter(tweet => (tweet \ "delete") != JNothing)
    .map(tweet => {
     implicit val formats = DefaultFormats
      ((tweet \ "delete" \ "status" \ "id").extract[String])
    })
    .saveAsTextFile("deletedTwtsTREC")

  }
}
