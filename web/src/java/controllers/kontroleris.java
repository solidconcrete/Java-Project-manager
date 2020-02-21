/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import database.databaseActions;
import com.google.gson.Gson;
import entities.User.User;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
public class kontroleris {

    @RequestMapping (value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String controllerLogin (@RequestBody String user) throws Exception
    {
        Gson pareser = new Gson();
        User paduotas = (User) pareser.fromJson(user, User.class);
        databaseActions actions = new databaseActions();
        try
        {
//            User prisijunge = actions.logIn(paduotas.getLogin(), paduotas.getPassword());
//            return pareser.toJson(prisijunge);
            String prisijunge = actions.logIn(paduotas.getLogin(), paduotas.getPassword());
            return prisijunge;
        }
        catch(Exception e)
        {
            return "couldn't login with connector";
        }
    }
            //for Android must later return ArrayList
    @RequestMapping(value = "getProjectWorkers", method = RequestMethod.GET)
    @ResponseBody
    public String controllerGetUsers() throws Exception
    {
        String a="";
        databaseActions test = new databaseActions();
        for (String s : test.getProjectWorkers(2))
        {
            a += s;
        }
        return a;
    }
    
    
    
                //USING AN ARTIFICIAL USER
                // later android will pass the user to method//
   @RequestMapping (value = "getProjects", method = RequestMethod.GET)
    @ResponseBody
    public String controllerGetUserProjects(@RequestBody String token) throws Exception
    {
//        Gson pareser = new Gson();
//        databaseActions actions = new databaseActions();
        return "aaaaaa";
//        try
//        {
////            User testUser = new User("user1", "pass1");
////            return actions.getProjectNames(testUser).toString();
////            return actions.getProjectNames(token);
//            return "fuck off";
//        }
//        catch(Exception e)
//        {
//            return String.valueOf(e);
////            return null;
//        }
    }
    
    @RequestMapping(value = "gp_{token}")
    @ResponseBody
    public String gp (@PathVariable String token)
    {
        databaseActions actions = new databaseActions();
        try {
                return actions.getProjectNames(token);
        } catch (Exception e) {
            return String.valueOf(e);
        }
    }
    
    @RequestMapping(value = "gc_{title}")
    @ResponseBody
    public String gc (@PathVariable String title)
    {
        databaseActions actions = new databaseActions();
        try
        {
            return actions.getProjectCreator(title);
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    @RequestMapping(value = "getTasks_{projectTitle}")
    @ResponseBody
    public String getTasks (@PathVariable String projectTitle)
    {
        databaseActions actions = new databaseActions();
        try
        {   
            return actions.getProjectTasks(actions.getProjectId(projectTitle));
        }
        catch (Exception e) {
        }
        return null;
    }
    
    @RequestMapping(value = "checkTask_{taskName}")
    @ResponseBody
    public String checkTask (@PathVariable String taskName)
    {
        databaseActions actions = new databaseActions();
//        return taskName;
        return actions.checkTask(taskName);
    }
    
    @RequestMapping (value = "completeTask_{user}_{task}")
    @ResponseBody
    public String completeTask (@PathVariable ("user") String user, @PathVariable ("task") String task)
    {
        databaseActions actions = new databaseActions();
        return actions.completeTask(task, user);
        
    }
    
    @RequestMapping(value = "addTask_{project}_{task}")
    @ResponseBody
    public String addTask (@PathVariable ("project") String project, @PathVariable ("task") String task)
    {
        databaseActions actions = new databaseActions();
        try
        {   
            int id = actions.getProjectId(project);
            String status = actions.createTask(id, task);
            if (status.contains("Task created"))
            {
                return "Task created";
            }
        }
        catch (Exception e) {
        }
        return "task not created";
    }

    @RequestMapping (value = "registerPerson", method = RequestMethod.POST)
    @ResponseBody
    public String registerPerson(@RequestBody String personData)
    {
        databaseActions actions = new databaseActions();
        String[] data = personData.split(",");
        try 
        {
            return actions.createUser(data[0], data[1], data[2], data[3]);
        }
        catch (Exception e)
        {
            
        }
        return null;
    }
    
    @RequestMapping (value = "registerCompany", method = RequestMethod.POST)
    @ResponseBody
    public String registerCompany(@RequestBody String companyData)
    {
        databaseActions actions = new databaseActions();
        String[] data = companyData.split(",");
        try 
        {
            return actions.createUser(data[0], data[1], data[2]);
        }
        catch (Exception e)
        {
            return String.valueOf(e);
        }
    }
                    //Must be changed for Android
    @RequestMapping (value = "createProject", method = RequestMethod.POST)
    @ResponseBody
    public String createProject (@RequestBody String projectDetails)
    {
        databaseActions actions = new databaseActions();
        String[] data = projectDetails.split(",");
        try
        {
            return actions.createProject(data[0], data[1]);
        }
        catch (Exception e)
        {
        }
        return "couldn't create project";
    }
                         //Must be changed for Android
    @RequestMapping (value = "addPersonToProject", method = RequestMethod.POST)
    @ResponseBody String addPersonToProject (@RequestBody String details)
    {
        databaseActions actions = new databaseActions();
        String[] data = details.split(",");
//        try
//        {
            return actions.addPersonToProject(Integer.parseInt(data[0]), data[1]);
//        }
//        catch (Exception e)
//        {
//        }
//        return "couldn't create project";
    }
    
}
