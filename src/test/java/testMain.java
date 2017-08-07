/**
 * Created by ralfidze on 16.07.17.
 */
import org.apache.log4j.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class testMain {
    private static final Logger log = Logger.getLogger(testMain.class);
    public static void main(String[] args) throws InterruptedException, IOException {

        log.info("Выполнен запуск программы!");
        UsersQueryDB usersQueryDB = new UsersQueryDB();
        //System.out.println("Добро пожаловать в модуль синхронизации!\n Введите код действия: \n 1-выгрузка данных из БД в XML \n 2-загрузка данных из файла XML в БД");
        Scanner in = new Scanner(System.in);
        UsersHashSet usersDB = usersQueryDB.getHashSet();
           switch (args[0]) {
               case "upload":
                   //JFileChooser fileSave = new JFileChooser();
                   //FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                  // fileSave.addChoosableFileFilter(xmlFilter);
                   //fileSave.setFileFilter(xmlFilter);
                  //int resSave = fileSave.showSaveDialog(fileSave);
                       usersDB.exportToXML(args[1]);
                       log.info("Синхронизация завершена (выгружены данные в XML) в корневую папку ");
                   break;
               case "sync":
                   JFileChooser fileOpen = new JFileChooser();
                   FileNameExtensionFilter xmlFilterOpen = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                   fileOpen.addChoosableFileFilter(xmlFilterOpen);
                   fileOpen.setFileFilter(xmlFilterOpen);
                   int resOpen = fileOpen.showDialog(null, "Открыть файл");
                   if (resOpen == JFileChooser.APPROVE_OPTION) {
                       File file = fileOpen.getSelectedFile();
                       UsersHashSet usersXml = new UsersHashSet(file.getPath());
                       if(usersXml.isEmpty()) {
                           System.out.println("Вы выбрали некорректный файл! Есть дубликаты!");
                           log.error("Синхронизация не выполнена! Вы выбрали некорректный файл! Есть дубликаты!\"");
                       }
                       else {
                           UsersQueryDB db=new UsersQueryDB();
                        try {
                           Iterator<Users> iterXML = usersXml.iterator();
                           while (iterXML.hasNext()){
                               Users userFromXML = iterXML.next();
                               Iterator<Users> iterDB = usersDB.iterator();
                               while (iterDB.hasNext()) {
                                   Users userFromDB = iterDB.next();
                                   if (userFromDB.equals(userFromXML)){
                                       if(!userFromXML.getDescription().equals(userFromDB.getDescription())){
                                           System.out.println("Надо внести изменение в строку по  натуральному ключу c " + userFromDB.getDescription() + " на " + userFromXML.getDescription());
                                               db.updateUser(userFromXML);
                                       }
                                   }
                                   if(!usersXml.contains(userFromDB)){
                                       System.out.println(userFromDB.getDepCode() + userFromDB.getDepJob() + userFromDB.getDescription() + " - его надо удалить из БД с проверкой если есть такой в БД, если нет то транзакцию не делаем");
                                       db.deleteUser(userFromDB);
                                   }
                               }
                               if (!usersDB.contains(userFromXML)){
                                   System.out.println(userFromXML.getDepCode()+" "+ userFromXML.getDepJob()+ " " + userFromXML.getDescription()+ " - его добавить в БД");
                                   db.insertUser(userFromXML);
                               }
                           }
                            db.con.commit();
                           }
                           catch (SQLException e) {
                               e.printStackTrace();
                               try {db.con.setAutoCommit(false);
                                   db.con.rollback();
                               } catch (SQLException e1) {
                                   e1.printStackTrace();
                               }
                           }
                       }
                   }
                       log.info("Синхронизация с БД завершена (загружены данные из XML)");
                   break;
               default:
                   System.out.println("Не найдены параметры синхронизации!");
                   log.info("Не найдены параметры синхронизации!");
                   break;
               }
    }
}
