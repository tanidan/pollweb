/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.model;

import java.util.Date;

/**
 *
 * @author venecia2
 */
public interface Question {
    
    int getKey();

    void setKey(int key);
    
    int getPosition();

    void setPosition(int position);
    
    String getType();

    void setType(String type);

    String getText();

    void setText(String Text);
    
    String getAnswer();
    
    void setAnswer(String answer);
    
    String getNote();
    
    void setNote(String note);
    
    boolean isMandatory();
    
    void setMandatory(boolean mandatory);
    
    Poll getPoll();
    
    void setPoll(Poll poll);
    
    void setUserAnswer(String userAnswer);
    
    String getUserAnswer();
    
}
