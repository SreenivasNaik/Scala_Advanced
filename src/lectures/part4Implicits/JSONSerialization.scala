package lectures.part4Implicits

import java.util.Date

object JSONSerialization extends App {

  /*
  *
  * */
  case class User(name:String,age:Int,email:String)
  case class Post(content:String,createdAt:Date)
  case class Feed(user:User,posts:List[Post])

  /*
    1. Intermidate data types Int,StringList,Date
    2. type classes for conversion to intermediate data types
    3. serialize to JSON
   */

  sealed trait JSONValue{
    def stringify:String
  }
  final case class JSONString(value:String) extends JSONValue{
    override def stringify: String = "\""+value+"\""
  }
  final case class JSONNumber(value:Int) extends JSONValue{
    override def stringify: String = value.toString
  }
  final case class JSONArray(value:List[JSONValue]) extends JSONValue{
    override def stringify: String = value.map(_.stringify).mkString("[",",","]")
  }
  final case class JSONObject(values:Map[String,JSONValue]) extends JSONValue{
    override def stringify: String = values.map{
      case (key,value) =>"\""+key+"\" :"+value.stringify
    }.mkString("{",",","}")
  }

  val data = JSONObject(Map(
    "user"-> JSONString("Sreenu"),
    "post" -> JSONArray(List(
      JSONString("Scala Rocks")
    ,
    JSONNumber(121)
  ))))

  println(data.stringify)

  // type classes
  /*  1. Type classes
  *   2. Type classes Instances - implicits
  *   3. pimp library to user type class instances
  * */

  // 2.1 Type class
  trait JSONConverter[T]{
    def convert(value:T):JSONValue
  }

  // 2.3 Conversion
  implicit class JSONOps[T](value:T){
    def toJson(implicit converter: JSONConverter[T]):JSONValue =
      converter.convert(value)
  }

  // 2.2

  implicit object StringConverter extends JSONConverter[String]{
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int]{
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }
  // custom data types
  implicit object UserConverter extends JSONConverter[User]{
    override def convert(value: User): JSONValue = JSONObject(Map(
      "name"->JSONString(value.name),
      "age"->JSONNumber(value.age),
      "email"->JSONString(value.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post]{
    override def convert(value: Post): JSONValue = JSONObject(Map(
      "content"->JSONString(value.content),
      "created"->JSONString(value.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed]{
    override def convert(value: Feed): JSONValue = JSONObject(Map(
      "user" -> value.user.toJson,
      "posts" -> JSONArray(value.posts.map(_.toJson))

    ))
  }

  // call stringify on results

  val now = new Date(System.currentTimeMillis())
  val sreenu = User("Sreenu",23,"sds")
  val feed = Feed(sreenu,List(
    Post("Hello",now),
    Post("look",now)
  ))

  println(feed.toJson.stringify)


}
