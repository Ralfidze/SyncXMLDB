import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by zakirovrn on 19.07.2017.
 */
public class UsersQueryDB {
    public String getStrURL() {
        return strURL;
    }

    public void setStrURL(String strURL) {
        this.strURL = strURL;
    }

    private  String strURL = "";

    public String getStrUser() {
        return strUser;
    }

    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }

    private String strUser = "";

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    private String strPassword = "";
    Connection con=null;
    UsersQueryDB(){
        Properties prop = new Properties();
        InputStream fis=null;
        try {
            ClassLoader cl=this.getClass().getClassLoader();
            fis = cl.getResourceAsStream("config.properties");
            prop.load(fis);
        }
        catch(FileNotFoundException ex){
            System.out.println("File config.properties not found!");
        }
        catch(IOException ex){
            System.out.println("File config.properties not found!");
        }
        this.strURL = prop.getProperty("jdbc.url");
        this.strUser = prop.getProperty("jdbc.username");
        this.strPassword = prop.getProperty("jdbc.password");
        try {
            con = DriverManager.getConnection(this.strURL,this.strUser,this.strPassword);
            con.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(prop.getProperty("jdbc.driver")).newInstance();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public UsersHashSet getHashSet(){
        UsersHashSet uSH=new UsersHashSet();
        String sql="select * from users";

        try {
            if (con == null) {
                System.out.println("�� ������� ������������ � ���� ������!");
            }
            else
            {
                Statement stmt = con.createStatement();
                ResultSet res=stmt.executeQuery(sql);
                while (res.next()){
                    Users u=new Users(res.getString("depcode"),res.getString("depjob"),res.getString("description"));
                    uSH.add(u);
                }
                stmt.close();
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("�� ������� ������������ � ���� ������!");
        }
        return uSH;
    }
    public void insertUser(Users userFromXML) throws  SQLException{
        try {
            PreparedStatement itemStatement = con.prepareStatement("INSERT INTO users VALUES (nextval('user_ids'),?,?,?)"); // � ���� �� id �� ��������� �����
            itemStatement.setString(1,userFromXML.getDepCode());
            itemStatement.setString(2,userFromXML.getDepJob());
            itemStatement.setString(3,userFromXML.getDescription());

            itemStatement.execute();

        }
        catch (SQLException ex){
            System.out.println(ex);
        }
    }

    public void deleteUser(Users userFromDB) throws SQLException{
        try {
                PreparedStatement itemStatement = con.prepareStatement("delete from users where depcode = ? and depjob = ? ");
                itemStatement.setString(1,userFromDB.getDepCode());
                itemStatement.setString(2,userFromDB.getDepJob());
                itemStatement.execute();
        }
        catch ( SQLException ex){
            System.out.println(ex);

        }
    }

    public void updateUser(Users u) throws SQLException{

        try {
            PreparedStatement itemStatement = con.prepareStatement("UPDATE users set description=? where depcode=? and depjob=?");
            itemStatement.setString(1, u.getDescription());
            itemStatement.setString(2, u.getDepCode());
            itemStatement.setString(3,u.getDepJob());

            itemStatement.executeUpdate();
        }
        catch ( SQLException ex){
            System.out.println(ex);

        }
    }
}
