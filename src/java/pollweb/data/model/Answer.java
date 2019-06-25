/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.model;

import java.io.File;

/**
 *
 * @author venecia2
 */
public interface Answer {
    
    int getKey();

    void setKey(int newKey);  
    
    User getUser();

    void setUser(User newUser);
    
    Poll getPoll();

    void setPoll(Poll newPoll);
    
    File getFile();

    void setFile(File newFile);
}
