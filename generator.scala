import scala.xml.XML

object Generator {

    def isNodeEmpty(n: xml.NodeSeq): Boolean = {
        n.text.replaceAll("\\s+$", "") == ""
    }

    def isEmpty(node: xml.Node) = {
        node.child.filter {c => !c.isInstanceOf[xml.Text] || !c.text.trim.isEmpty}.isEmpty
    }

    def textOfNode(n: xml.Node): Option[String] = n match {
        case xml.Elem(_, _, _, _, Seq(xml.Text(t))) => Some(t)
        case _ => None
    }

    def printStartTag(n: xml.Node, tl: Int = 0) = {
        println(tab(tl) + "<" + ((n \ ("@tag")).text) + " class='"+ n.label +"'>")
    }

    def printEndTag(n: xml.Node, tl: Int = 0) = {
        println(tab(tl) + "</" + ((n \ ("@tag")).text) + ">") 
    }

    def printTextContent(text:String, tl: Int = 0) = {
        println(tab(tl) + text)
    }

    def printTextNode(n:xml.Node, tl: Int = 0) = {
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

    def displayContent(element: xml.Node, tl:Int = 0):Unit = {
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