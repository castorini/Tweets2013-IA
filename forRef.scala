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


    val conf = new SparkConf().setAppName("Examine Tweets")
    val sc = new SparkContext(conf)

    val year = "2013"

    val trecTweets = RecordLoader.loadTweets("/collections/tweets/Tweets2013/", sc)
    val archivedTweetsMarch = RecordLoader.loadTweets("03ex", sc)
    var archivedTweetsFeb = RecordLoader.loadTweets("02ex", sc)

    val trecFeb = trecTweets.map(tweet => (tweet.id, tweet.createdAt))
      .filter(tuple => {
        val createdAt = tuple._2
        val dateIn = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
        val dateOut = new SimpleDateFormat("yyyy-MM")
        val d = try {
          dateOut.format(dateIn.parse(createdAt))
        } catch {
          case e: Exception => null
        }
        val yearMonth = year + "-" + "%02d".format(2) 
        d != null && d.equals(yearMonth)
      })
       .map(twt => twt._1)
      .saveAsTextFile("trecFeb")

    // val trecMarchSet = Set()
     val trecMarch = trecTweets.map(tweet => (tweet.id, tweet.createdAt))
       .filter(tuple => {
         val createdAt = tuple._2
         val dateIn = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
         val dateOut = new SimpleDateFormat("yyyy-MM")
         val d = try {
           dateOut.format(dateIn.parse(createdAt))
         } catch {
           case e: Exception => null
         }
         val yearMonth = year + "-" + "%02d".format(3)
         d != null && d.equals(yearMonth)
       })
       .map(twt => twt._1)
       .saveAsTextFile("trecMarch")


    val archivedFeb =  archivedTweetsFeb.map(tweet => (tweet.id, tweet.createdAt))
      .filter(tuple => {
        val createdAt = tuple._2
        val dateIn = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
        val dateOut = new SimpleDateFormat("yyyy-MM")
        val d = try {
          dateOut.format(dateIn.parse(createdAt))
        } catch {
          case e: Exception => null
        }
        val yearMonth = year + "-" + "%02d".format(2) 
        d != null && d.equals(yearMonth)
      })
       .map(twt => twt._1)
      .saveAsTextFile("archivedFeb")

    // val archivedMarchSet = Set()
     val archivedMarch =  archivedTweetsMarch.map(tweet => (tweet.id, tweet.createdAt))
       .filter(tuple => {
         val createdAt = tuple._2
         val dateIn = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
         val dateOut = new SimpleDateFormat("yyyy-MM")
         val d = try {
           dateOut.format(dateIn.parse(createdAt))
         } catch {
           case e: Exception => null
         }
         val yearMonth = year + "-" + "%02d".format(3)
         d != null && d.equals(yearMonth)
       })
       .map(twt => twt._1)
       .saveAsTextFile("archivedMarch")

  }
}