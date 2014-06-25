import scala.xml.XML
import scala.xml._
import java.io._
import scala.io.Source
import java.io.PrintWriter

object Generator {

    val fw = new FileWriter("Posts.html", true)
    val newLine = System.getProperty("line.separator")

    def isNodeEmpty(n: NodeSeq): Boolean = {
        n.text.replaceAll("\\s+$", "") == ""
    }

    def isEmpty(node: Node): Boolean = {
        node.child.filter {c => !c.isInstanceOf[Text] || !c.text.trim.isEmpty}.isEmpty
    }

    def textOfNode(n: Node): Option[String] = n match {
        case Elem(_, _, _, _, Seq(Text(t))) => Some(t)
        case _ => None
    }

    def getStartTag(n: Node, tl: Int = 0):String = {
        val attrs = n.attributes.filterNot(_.key == "tag").foldLeft(List[String]())((l, a) => (a.key + "='" + a.value + "' ") :: l)
        (tab(tl) + "<" + ((n \ ("@tag")).text) + " class='"+ n.label +"' " + attrs.mkString + ">")
    }

    def getEndTag(n: Node, tl: Int = 0):String = {
        (tab(tl) + "</" + ((n \ ("@tag")).text) + ">")
    }

    def emptyTag(n: Node, tl: Int) = {
        getStartTag(n, tl) + newLine + getEndTag(n, tl)
    }

    def tagWithContent(n:Node, tl:Int, content: String) = {
        getStartTag(n, tl) + newLine + content + newLine + getEndTag(n, tl)
    }

    def getTextContent(text:String, tl: Int = 0) = {
        (tab(tl) + text)
    }

    def printTextNode(n:Node, tl: Int = 0) = {
        val text = textOfNode(n)
        text match {
            case Some(s) => tagWithContent(n, tl, getTextContent(s, tl + 1))
            case _ => emptyTag(n, tl)
        }
    }

    def tab(tl:Int = 0):String = {
        ("   " * tl)
    }

    def display(element:Node, tl:Int): String = {
        element.child.filterNot(c => isNodeEmpty(c)).
        foldLeft ("") ((acc, item) => textOfNode(item) match {
                                        case Some(s) => acc + printTextNode(item, tl + 1) + newLine
                                        case _ => acc + tagWithContent(item, tl + 1, display(item, tl + 2)) + newLine})
    }

    def displayContent(element: Node, tl:Int = 0):String = {
        tagWithContent(element, tl, display(element, tl))
    }

    def print(s:String, toFile:Boolean = false):Unit = {
        if(toFile) fw.write(s + "\n") else println(s)
    }

    def deleteFile(name: String) = {
        try {
            val file = new File("Posts.html")
            if(file.delete()) println(file.getName() + " is deleted!") else println("Delete operation is failed.")
        }
        catch {
            case e:Exception => e.printStackTrace()
        }
    }

    def mapFile(inFile:String, outFile:String) {
        val pw = new PrintWriter(outFile)
        val in = Source.fromFile(inFile)
        for(c <- in) pw.print(c)
        in.close
        pw.close
    }

    def insertInside(file: String, content: String, template:String) = {
        val tempFile = "temp.txt"
        val pw = new PrintWriter(tempFile)
        try { 
            val source = Source.fromFile(file)
            val lines = source.getLines
            for(line <- lines) {
                if(line.trim().equalsIgnoreCase(template)) {
                    pw.println(line)
                    pw.println(content)
                }
                else pw.println(line)
            }
            pw.close
            mapFile(tempFile,file)
            new File(tempFile).delete()
        }
        catch {
            case e:Exception => e.printStackTrace()
        }
    }

    def clearTemplateOfFile(file:String, templateStart:String, templateEnd:String) = {
        val tempFile = "temp.txt"
        val pw = new PrintWriter(tempFile)
        var insideTemplate = false
        try { 
            val source = Source.fromFile(file)
            val lines = source.getLines
            for(line <- lines) {
                if(line.trim().equalsIgnoreCase(templateStart)) {
                    insideTemplate = true
                    pw.println(line)
                    pw.write("")
                }
                if(line.trim().equalsIgnoreCase(templateEnd)) insideTemplate = false
                if(!insideTemplate) pw.println(line)
            }
            pw.close
            mapFile(tempFile,file)
            new File(tempFile).delete()
        }
        catch {
            case e:Exception => e.printStackTrace()
        }
    }

    def main(args: Array[String]): Unit = {
        clearTemplateOfFile("Posts.html", "<template>", "</template>")
        val config = XML.loadFile("posts.xml")
        val result = displayContent(config, 2)
        insertInside("Posts.html", result, "<template>")
    }
}