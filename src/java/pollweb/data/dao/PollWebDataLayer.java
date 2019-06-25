/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.SQLException;
import javax.sql.DataSource;
import pollweb.data.model.Administrator;
import pollweb.data.model.Answer;
import pollweb.data.model.Manager;
import pollweb.data.model.Poll;
import pollweb.data.model.Question;
import pollweb.data.model.User;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

/**
 *
 * @author venecia2
 */
public class PollWebDataLayer extends DataLayer{
    
    public PollWebDataLayer(DataSource datasource) throws SQLException{
        super(datasource);
    }
    
    
    
    @Override
    public void init() throws DataException {
        registerDAO(Administrator.class, new AdministratorDAO_MySQL(this));
        registerDAO(Manager.class, new ManagerDAO_MySQL(this));
        registerDAO(Poll.class, new PollDAO_MySQL(this));
        registerDAO(User.class, new UserDAO_MySQL(this));   
        registerDAO(Question.class, new QuestionDAO_MySQL(this));
        registerDAO(Answer.class, new AnswerDAO_MySQL(this));
    }
    
    public AdministratorDAO getAdministratorDAO(){
        return (AdministratorDAO) getDAO(Administrator.class);
    }
    
    public PollDAO getPollDAO(){
        return (PollDAO) getDAO(Poll.class);
    }
    
    public QuestionDAO getQuestionDAO(){
        return (QuestionDAO) getDAO(Question.class);
    }
    
    public UserDAO getUserDAO(){
        return (UserDAO) getDAO(User.class);
    }
    
    public ManagerDAO getManagerDAO(){
        return (ManagerDAO) getDAO(Manager.class);
    }
    
     public AnswerDAO getAnswerDAO(){
        return (AnswerDAO) getDAO(Answer.class);
    }
    
}
