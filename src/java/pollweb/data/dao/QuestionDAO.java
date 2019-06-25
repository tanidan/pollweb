/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.ResultSet;
import java.util.List;
import pollweb.data.impl.QuestionImpl;
import pollweb.data.model.Poll;
import pollweb.data.model.Question;
import pollweb.data.util.DataException;

/**
 *
 * @author venecia2
 */
public interface QuestionDAO { 
    
    Question createQuestion();
    
    Question createQuestion(ResultSet rs) throws DataException;
    
    Question getQuestion(int question_ID) throws DataException;
        
    List<Question> getQuestionsByPoll(Poll poll) throws DataException;
    
    void storeQuestion (QuestionImpl question) throws DataException;   
    
}
