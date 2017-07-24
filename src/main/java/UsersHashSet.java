
//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by ralfidze on 16.07.17.
 */
class UsersHashSet extends HashSet {

    public  UsersHashSet(){}

    public UsersHashSet(String fileName){
        try {
            DocumentBuilder xml= DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = xml.parse(new File(fileName));
            Element root=doc.getDocumentElement();
            Node el=root.getFirstChild();
            while (el!=null){
                if (!"root".equals(el.getNodeName())){
                    Node e=el.getFirstChild();
                    String depCode="", depJob="", desc="";
                    int id=0;
                    while(e!=null){
                        if ("#text".equals(e.getNodeName()) || "id".equals(e.getNodeName())) {
                            e=e.getNextSibling();
                            continue;}
                        if ("depcode".equals(e.getNodeName()) && !"".equals(e.getTextContent())) {
                            depCode=e.getTextContent();
                            e=e.getNextSibling();
                            continue;
                        }
                        if ("depjob".equals(e.getNodeName()) && e.getTextContent()!="") {
                            depJob=e.getTextContent();
                            e=e.getNextSibling();
                            continue;
                        }
                        if ("description".equals(e.getNodeName()) && !"".equals(e.getTextContent())) {
                            desc=e.getTextContent();
                        }
                        Users u = new Users(depCode,depJob,desc);
                        if (!(this.contains(u)))
                            this.add(u);
                        else {
                            this.clear();
                            return;
                        }
                        e=e.getNextSibling();}
                    el=el.getNextSibling();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
        }
    }
    public void exportToXML(String filename) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        // String filename="C:/file.xml";
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(
                    new FileOutputStream(filename), 1024*100), "windows-1251");

            writer.write("<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n");
            Iterator<Users> it = this.iterator();
            writer.write("<data>\n");
            while(it.hasNext()){
                Users u=it.next();
                writer.write("<user>\n");
                //writer.write("\t<id>"+rec.id+"</id>\n");
                writer.write("\t<depcode>"+u.getDepCode()+"</depcode>\n");
                writer.write("\t<depjob>"+u.getDepJob()+"</depjob>\n");
                writer.write("\t<description>"+u.getDescription()+"</description>\n");
                writer.write("</user>\n");
            }
            writer.write("</data>\n");
            writer.flush();
            writer.close();
            System.out.println("Выполнена выгрузка файла завершена");}
        catch(UnsupportedEncodingException e){
            System.out.println("UnsupportedEncodingExceprion");
        }
        catch(FileNotFoundException e){
            System.out.println("FileNotFoundExceprion");
        }
        catch(IOException e){
            System.out.println("IOException");
        }
    }
}
