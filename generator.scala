import scala.io.Source
import scala.xml.XML
import scala.xml._
import java.io._
import java.io.PrintWriter

object Generator {
    case class Settings(htmlOutputFile:String, xmlInputFile:String, templateStart:String, templateEnd:String)

    var settings = Settings("Posts.html", "settings\\posts.xml", """<!-- generated content starts -->""", """<!-- generated content ends -->""")

    val fw = new FileWriter(settings.htmlOutputFile, true)
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

    def htmlComment(text: String):String = {
        ("""<!--""" + text + """-->""")
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
            val file = new File(name)
            if(file.delete()) println(file.getName() + " is deleted!") else println("Delete operation is failed.")
        }
        catch {
            case e:Exception => e.printStackTrace()
        }
    }

    def mapFile(inFile:String, outFile:String) = {
        val pw = new PrintWriter(outFile)
        val in = Source.fromFile(inFile)
        for(c <- in) pw.print(c)
        in.close
        pw.close
    }

    def textOfFile(file:String):String = {
        var result = new StringBuffer()
        val in = Source.fromFile(file)
        for(c <- in) result.append(c)
        return result.toString()
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

    def readConfiguration() = {
        val config = XML.loadFile("config.xml")

        val inputFile = (config \\ "inputfile").text
        val outputFile = (config \\ "outputfile").text
        val templateStart = (config \\ "startcommenttext").text
        val templateEnd = (config \\ "endcommenttext").text
        val in = if(inputFile.equals("")) Nil else inputFile
        val out = if(outputFile.equals("")) Nil else outputFile
        val ts = if(templateStart.equals("")) Nil else templateStart
        val te = if(templateEnd.equals("")) Nil else templateEnd
        println("Input file name: " + inputFile + "!")
        println("Output file name: " + outputFile + "!")
        println("Start template: " + templateStart + "!")
        println("End template: " + templateEnd + "!")

        (in, out, ts, te)
        //List(inputFile, outputFile, templateStart, templateEnd).map(a => if(a.equals("")) None else Some(a))
    }

    def setConfiguration(args:Array[String]) = {
        if(args.length != 0) {
            args.length match {
                case 1 => settings = settings.copy(htmlOutputFile = args(0))
                case 2 => settings = settings.copy(htmlOutputFile = args(0), templateStart = args(1))
                case 3 => settings = settings.copy(htmlOutputFile = args(0), templateStart = args(1), templateEnd = args(2))
                case 4 => settings = settings.copy(htmlOutputFile = args(0), 
                                                   templateStart = args(1), 
                                                   templateEnd = args(2), 
                                                   xmlInputFile = args(3))
            }
        }
        printSettings
    }

    def printSettings = {
        println("Input file name: " + settings.xmlInputFile)
        println("Output file name: " + settings.htmlOutputFile)
        println("Start template: " + settings.templateStart)
        println("End template: " + settings.templateEnd)
    }

    def main(args: Array[String]): Unit = {
        setConfiguration(args)
        // todo - foreach configuration setting do this
        clearTemplateOfFile(settings.htmlOutputFile, settings.templateStart, settings.templateEnd)
        val endshtml = settings.xmlInputFile.endsWith("html")
        println(endshtml + " " + settings.xmlInputFile);
        if(endshtml) {
            insertInside(settings.htmlOutputFile,textOfFile(settings.xmlInputFile), settings.templateStart)
        } else {
            val posts = XML.loadFile(settings.xmlInputFile)
            val result = displayContent(posts, 2)
            insertInside(settings.htmlOutputFile, result, settings.templateStart)
        }

        // val sets = readConfiguration
        // sets match {
        //     case (in, Nil, Nil, Nil) => {
        //         settings = settings.copy(xmlInputFile = in.toString)
        //     }
        //     case (Nil, out, Nil, Nil) => {
        //         settings = settings.copy(htmlOutputFile = out.toString)
        //     }
        //     case (in, out, ts, Nil) => {
        //         settings = settings.copy(xmlInputFile = in.toString, htmlOutputFile = out.toString, templateStart = ts.toString)
        //     }
        //     case (in, out, Nil, te) => {
        //         settings = settings.copy(xmlInputFile = in.toString, htmlOutputFile = out.toString, templateEnd = te.toString)
        //     }
        //     case (in, out, ts, te) => {
        //         settings = settings.copy(xmlInputFile = in.toString, htmlOutputFile = out.toString, templateStart = ts.toString, templateEnd = ts.toString)
        //     }
        // }
        // printSettings
    }
}