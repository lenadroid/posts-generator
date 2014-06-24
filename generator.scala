import scala.xml.XML
import scala.xml._

object Generator {

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

    def printStartTag(n: Node, tl: Int = 0) = {
        println(tab(tl) + "<" + ((n \ ("@tag")).text) + " class='"+ n.label +"'>")
    }

    def printEndTag(n: Node, tl: Int = 0) = {
        println(tab(tl) + "</" + ((n \ ("@tag")).text) + ">") 
    }

    def printTextContent(text:String, tl: Int = 0) = {
        println(tab(tl) + text)
    }

    def printTextNode(n:Node, tl: Int = 0) = {
        printStartTag(n, tl)
        val text = textOfNode(n)
        text match {
            case Some(s) => printTextContent(s, tl + 1)
            case _ => print()
        }
        printEndTag(n, tl)
    }

    def tab(tl:Int = 0):String = {
        ("   " * tl)
    }

    def displayContent(element: Node, tl:Int = 0):Unit = {
        printStartTag(element, tl)
        element.child.filterNot(c => isNodeEmpty(c)).map { c =>
            textOfNode(c) match {
                case Some(s) => printTextNode(c, tl + 1)
                case _ => displayContent(c, tl + 1)
            } 
        }
        printEndTag(element, tl)
    }

    def main(args: Array[String]): Unit = {
        val config = XML.loadFile("posts.xml")
        displayContent(config)
    }
}