
import scala.xml.XML
import scala.xml._
import java.io._

object Generator {

    def fw = new FileWriter("Posts.html", true)

    def isNodeEmpty(n: NodeSeq): Boolean = {
        n.text.replaceAll("\\s+$", "") == ""
    }

    def isEmpty(node: Node) = {
        node.child.filter {c => !c.isInstanceOf[Text] || !c.text.trim.isEmpty}.isEmpty
    }

    def textOfNode(n: Node): Option[String] = n match {
        case Elem(_, _, _, _, Seq(Text(t))) => Some(t)
        case _ => None
    }

    def getStartTag(n: Node, tl: Int = 0) = {
        val attrs = n.attributes.filterNot(_.key == "tag").foldLeft(List[String]())((l, a) => (a.key + "='" + a.value + "' ") :: l)
        (tab(tl) + "<" + ((n \ ("@tag")).text) + " class='"+ n.label +"' " + attrs.mkString + ">")
    }

    def getEndTag(n: Node, tl: Int = 0) = {
        (tab(tl) + "</" + ((n \ ("@tag")).text) + ">") 
    }

    def getTextContent(text:String, tl: Int = 0) = {
        (tab(tl) + text)
    }

    def printTextNode(n:Node, tl: Int = 0) = {
        print(getStartTag(n, tl), true)
        val text = textOfNode(n)
        text match {
            case Some(s) => print(getTextContent(s, tl + 1), true)
            case _ => print("")
        }
        print(getEndTag(n, tl), true)
    }

    def tab(tl:Int = 0):String = {
        ("   " * tl)
    }

    def displayContent(element: Node, tl:Int = 0):Unit = {
        print(getStartTag(element, tl), true)
        element.child.filterNot(c => isNodeEmpty(c)).map { c =>
            textOfNode(c) match {
                case Some(s) => printTextNode(c, tl + 1)
                case _ => displayContent(c, tl + 1)
            } 
        }
        print(getEndTag(element, tl), true)
    }

    def print(s:String, toFile:Boolean = false):Unit = {
        if(toFile) {
            val fw = new FileWriter("Posts.html", true)
            try {
              fw.write(s + "\n")
            }
            finally fw.close() 
        }
        else {
            println(s)
        }
    }

    def main(args: Array[String]): Unit = {
        new File("Posts.html").delete() 
        val config = XML.loadFile("posts.xml")
        displayContent(config)
    }
}