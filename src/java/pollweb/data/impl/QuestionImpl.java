/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;

import pollweb.data.model.Poll;
import pollweb.data.model.Question;

/**
 *
 * @author venecia2
 */
public class QuestionImpl implements Question{
    
    int key;
    int position;// position of question
    String type;
    String text;
    String answer;//answer options
    String note;//explanatory note
    boolean isMandatory;
    Poll poll;
    String userAnswer;
    
    public QuestionImpl() { }


    @Override
    public int getKey() {
       return key;
    }

    @Override
    public void setKey(int key) {
        this.key=key;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position=position;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
       this.type=type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
    
      @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String getAnswer() {
        return answer;
    }

     @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer;
    }
    
    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean isMandatory() {
        return isMandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.isMandatory = mandatory;
    }

    @Override
    public Poll getPoll() {
        return poll;
    }

    @Override
    public void setPoll(Poll poll) {
        this.poll=poll;
    }
    
}
