/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.ResultSet;
import java.util.List;
import pollweb.data.model.Manager;
import pollweb.data.model.Poll;
import pollweb.data.util.DataException;

/**
 *
 * @author venecia2
 */
public interface PollDAO {
    
    Poll createPoll();
    
    Poll createPoll(ResultSet rs) throws DataException;
    
    Poll getPoll(int poll_key) throws DataException;
    
    List<Poll> getPolls(Manager manager) throws DataException;
        
    List<Poll> getUnsignedPolls() throws DataException;
    
    void storePoll(Poll poll) throws DataException;  
    
    void deletePoll(Poll poll) throws DataException;
}