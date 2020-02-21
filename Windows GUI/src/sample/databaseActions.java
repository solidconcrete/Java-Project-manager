package sample;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class databaseActions {
    Connection conn = null;
    Cipher ecipher = null;
    Cipher dcipher = null;
    SecretKey key = null;

    public void connect(){
        try

        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String DB_URL = "jdbc:mysql://localhost:3306/java_kursinis";
            String USER = "root";
            String PASS = "8998";
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("connected");
        }
        catch(Exception e)
        {
            System.out.println("couldn't connect");
        }
    }

    public String createUser(String login, String pass, String name, String surname)
    {
        if (login.length() < 3)
            return "login must contain at least 3 characters";
        if (pass.length() < 5 || !pass.matches(".*\\d.*"))
            return "password must contain at least 5 characters and a number";
        if (name.matches(".*\\d.*") || surname.matches(".*\\d.*"))
            return "name and surname cannot contain numbers";
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user_table (user_id, user_login, user_password, person_name, person_surname, company_title) VALUES (NULL, ?, ?, ?, ?, NULL)");
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, name);
            ps.setString(4, surname);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return "person created";
        } catch (Exception e) {
            if (String.valueOf(e).contains("Duplicate entry"))
                return "user with such login already exists";
            else
                return String.valueOf(e);
        }
    }

    public String createUser(String login, String pass, String title)
    {
        if (login.length() < 3 || login.contains(" "))
            return "login must contain at least 3 characters and no spaces";
        if (pass.length() < 5 || !pass.matches(".*\\d.*"))
            return "password must contain at least 5 characters and a number";
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO user_table (user_id, user_login, user_password, person_name, person_surname, company_title) VALUES (NULL, ?, ?, NULL, NULL, ?)");
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, title);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return "Company created";
        } catch (Exception e) {
            if (String.valueOf(e).contains("Duplicate entry"))
                return "user with such login already exists";
            else
                return String.valueOf(e);
        }
    }

    public String createProject (String projectName, String creatorLogin)
    {
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO project_table (project_id, project_title, created_by) VALUES (NULL, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, projectName);
            ps.setString(2, creatorLogin);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int projectId = rs.getInt(1);

            ps = conn.prepareStatement("INSERT INTO project_user_connections (project_id,user_id) VALUES (?, ?)");
            ps.setInt(1, projectId);
            ps.setInt(2, getIdByLogin(creatorLogin));
            ps.executeUpdate();

            ps.close();
            conn.close();
            return "project created";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    public String createTask (int projectId, String taskTitle)
    {
        try{
            connect();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO task_table (task_id, task_title, task_created_on, task_completed_on"
                    +", task_completed_by, task_parent_project) VALUES (NULL, ?, ?, NULL, NULL, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, taskTitle);
            ps.setString(2, String.valueOf(Calendar.getInstance().getTime()));
            ps.setInt(3, projectId);
            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int taskId = rs.getInt(1);

            ps = conn.prepareStatement("INSERT INTO project_task_connections (project_id, task_id) VALUES(?, ?)");
            ps.setInt(1, projectId);
            ps.setInt(2, taskId);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return "Task created";
        } catch (Exception e)
        {
            e.printStackTrace();
            return String.valueOf(e);
        }
    }

    public String addPersonToProject (int projectID, String personLogin)
    {
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement("SELECT user_id from user_table WHERE user_login = ?");
            ps.setString(1, personLogin);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int personId = rs.getInt(1);

            ps = conn.prepareStatement("SELECT user_id from project_user_connections where user_id = ? and project_id = ?");
            ps.setInt(1, projectID);
            ps.setInt(2, personId);
            rs = ps.executeQuery();
            rs.next();
            if(!rs.equals(null))
            {
                return "peron already in project";
            }
            ps = conn.prepareStatement("INSERT INTO project_user_connections (project_id, user_id) VALUES (?, ?)");
            ps.setInt(1, projectID);
            ps.setInt(2, personId);
            ps.executeUpdate();

            ps.close();
            conn.close();
            return "person added";

        } catch (SQLException e) {
            if (String.valueOf(e).contains("Illegal operation on empty result set"))
                return "user with such login does not exist";
            return String.valueOf(e);
        } catch (Exception e) {
//            e.printStackTrace();
            return String.valueOf(e);
        }
    }



    public ArrayList<String> getProjectWorkers(int projectId)
    {
        try
        {
            connect();
            PreparedStatement ps = conn.prepareStatement("SELECT  user_id FROM project_user_connections WHERE project_id = ?");
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            ArrayList<String > users = new ArrayList<>();

            while(rs.next())
            {
                ps = conn.prepareStatement("SELECT user_login from user_table WHERE user_id = ?");
                ps.setInt(1, rs.getInt(1));
                ResultSet login = ps.executeQuery();
                login.next();
                users.add(login.getString(1));
//                System.out.println(rs.getInt(1));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getProjectTasks(int projectId)
    {
        try
        {
            connect();
            String tasks = "";
            PreparedStatement ps = conn.prepareStatement("SELECT task_title FROM task_table WHERE task_parent_project = ?");
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
//            ArrayList<String> tasks = new ArrayList<>();
            while (rs.next())
            {
//                tasks.add(rs.getString(1));
                tasks += "," + rs.getString(1);
            }
            return  tasks.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /////////////////////////////////////////////////////////////////////////////
    public void addPersonToTask (int taskId, String personLogin)
    {
        try {
            connect();
            PreparedStatement ps = conn.prepareStatement("SELECT user_id from user_table WHERE user_login = ?");
            ps.setString(1, personLogin);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int personId = rs.getInt(1);

            ps = conn.prepareStatement("INSERT INTO task_user_connections (task_id, user_id) VALUES (?, ?)");
            ps.setInt(1, taskId);
            ps.setInt(2, personId);
            ps.executeUpdate();
            System.out.println("person added to task");
            ps.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTaskWorkers(int taskId)
    {
        try
        {
            connect();
            PreparedStatement ps = conn.prepareStatement("SELECT  user_id FROM task_user_connections WHERE task_id = ?");
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            ArrayList<String > users = new ArrayList<>();

            while(rs.next())
            {
                ps = conn.prepareStatement("SELECT user_login from user_table WHERE user_id = ?");
                ps.setInt(1, rs.getInt(1));
                ResultSet login = ps.executeQuery();
                login.next();
                users.add(login.getString(1));
//                System.out.println(rs.getInt(1));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String completeTask(String task, String completedBy)
    {
        try
        {
            connect();
            PreparedStatement ps = conn.prepareStatement("UPDATE task_table SET task_completed_on = ?, task_completed_by = ? WHERE  task_title = ?");
            ps.setString(1, String.valueOf(Calendar.getInstance().getTime()));
            ps.setString(2, completedBy);
            ps.setString(3, task);
            ps.executeUpdate();
            conn.close();
            return "task completed";
        } catch (Exception e) {
            e.printStackTrace();
            return "couldn't complete task";
        }
    }

    public void completeProject (String projectTitle)
    {
        try
        {
            connect();
            PreparedStatement ps = conn.prepareStatement("UPDATE project_table SET completed_on = ? WHERE project_title = ?");
            ps.setString(1, String.valueOf(Calendar.getInstance().getTime()));
            ps.setString(2, projectTitle);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProject (String projectTitle)
    {
        try
        {
            connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM project_table WHERE project_title = ?");
            ps.setString(1, projectTitle);
            ps.executeUpdate();
            ps = conn.prepareStatement("DELETE from project_task_connections WHERE project_id = ?");
            ps.setInt(1, getProjectId(projectTitle));
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String logIn (String login, String password)
    {
        try {
            ResultSet rs= getAllUsers();
            while (rs.next())
            {
                if(rs.getString(2).equals(login)
                        && rs.getString(3).equals(password))
                {
                    if (!rs.getString(6).contains("null"))
                    return "logged in successfully";

                }
            }
            return "wrong login or password";
        } catch (Exception e)
        {
            e.printStackTrace();
            return "wrong login or password";
        }
    }

    public ResultSet getAllUsers() throws SQLException
    {
        connect();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM user_table");
        ResultSet rs = ps.executeQuery();
        return rs;
    }

//    public String encrypt (String unencrypted)
//    {
//        try
//        {
//            ecipher = Cipher.getInstance("DES");
//            dcipher = Cipher.getInstance("DES");
//            key = KeyGenerator.getInstance("DES").generateKey();
//
//            ecipher.init(Cipher.ENCRYPT_MODE, key);
////             dcipher.init(Cipher.DECRYPT_MODE, key);
//
//            String text = unencrypted;
//            String encrypted = encryptor.encrypt(text, ecipher, key);
//            return encrypted;
//        }
//        catch (Exception e)
//        {
//            return String.valueOf(e);
//        }
//    }

//    public String decrypt (String encrypted)
//    {
//        try
//        {
//            dcipher.init(Cipher.DECRYPT_MODE, key);
//            String unencrypted = encryptor.decrypt(encrypted, dcipher, key);
//            return unencrypted;
//        }
//        catch (InvalidKeyException ex)
//        {
//            Logger.getLogger(databaseActions.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "couldn't decrypt";
//    }

    public int getUserId (String login)
    {
        connect();
        try
        {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id FROM user_table WHERE user_login = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            int id;
            rs.next();
            id = rs.getInt(1);
            ps.close();
            conn.close();
            return id;
        } catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }
    public ArrayList<Integer> getUserProjectIds (int userId)
    {
        connect();
//        String projects = "";
        try
        {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT project_id FROM project_user_connections WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> projects = new ArrayList<>();
            while (rs.next())
            {
                projects.add(rs.getInt(1));
//                projects += "," + rs.getInt(1);
            }
            return projects;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getIdByLogin (String login)
    {
        connect();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM user_table WHERE user_login = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public ArrayList<String> getProjectNames(String login)
    {

        int userId = getUserId(login);
        System.out.println("User id: " + userId);
        ArrayList<Integer> projectIds = getUserProjectIds(userId);
        try
        {
            connect();
            ArrayList<String> projectNames = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT project_title FROM project_table WHERE project_id = ?");
            for (Integer i : projectIds)
            {
                System.out.println("i: " + i);
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                rs.next();
                projectNames.add(rs.getString(1));
            }

            return projectNames;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getProjectCreator(String projectName)
    {
        connect();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT created_by FROM project_table WHERE project_title = ?");
            ps.setString(1, projectName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ps = conn.prepareStatement("SELECT company_title FROM user_table WHERE user_login = ?");
            ps.setString(1, rs.getString(1));
            rs = ps.executeQuery();
            rs.next();

            ps.close();
            conn.close();
            return rs.getString(1);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public int getProjectId (String title)
    {
        connect();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT project_id FROM project_table WHERE project_title = ?");
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e) {
        }
        return 0;
    }

    public String checkTask(String taskName)
    {
        connect();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT task_completed_on, task_completed_by FROM task_table WHERE task_title = ?");
            ps.setString(1, taskName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String result = "completed on " + rs.getString(1) + " by " + rs.getString(2);
            conn.close();
            ps.close();
            if (result.contains("null"))
            {
                return "task not completed";
            }
            return  result;
        }
        catch (Exception e) {
            return String.valueOf(e);
        }

    }

    public int[] countActiveFinishedProjects(String loggedIn) throws SQLException {
        connect();
        int[] res = new int[2];
        res[0] = 0;
        res[1] = 0;
        PreparedStatement ps = conn.prepareStatement("SELECT *  from project_table WHERE created_by = ?");
        ps.setString(1, loggedIn);
        ResultSet rs = ps.executeQuery();
        int i = 0;
        while(rs.next())
        {
            i++;
        }
        res[0]= i;
        ps = conn.prepareStatement("SELECT *  from project_table WHERE created_by = ? AND completed_on IS NULL");
        ps.setString(1, loggedIn);
        rs = ps.executeQuery();
        i = 0;
        while(rs.next())
        {
            i++;
        }
        res[0] = res[0] - i;
        res[1] = i;
        ps.close();
        conn.close();
        return res;

    }
}
