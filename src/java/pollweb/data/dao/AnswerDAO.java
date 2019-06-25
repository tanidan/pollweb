/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.ResultSet;
import java.util.List;
import pollweb.data.impl.AnswerImpl;
import pollweb.data.model.Answer;
import pollweb.data.util.DataException;

/**
 *
 * @author venecia2
 */
public interface AnswerDAO {

    AnswerImpl createAnswer();

    Answer createAnswer(ResultSet rs) throws DataException;

    Answer getAnswer(int answer_key) throws DataException;
    
    List<Answer> getAnswers() throws DataException;
    
    Answer getAnswerByUser(int user_key) throws DataException;
    
    List<Answer> getAnswerByPoll(int poll_id) throws DataException;

    void storeAnswer(AnswerImpl answer) throws DataException;
    
    void deleteAnswer(Answer answer) throws DataException;
}
