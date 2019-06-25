/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;
import pollweb.data.model.Poll;
import pollweb.data.model.User;

/**
 *
 * @author venecia2
 */
public class UserImpl extends PersonImpl implements User{
    
    private Poll poll;

    public UserImpl() {
        this.key = 0;
        this.password = "";
        this.email = "";
        this.poll = null;
    }
    
    @Override
    public Poll getPoll() {
        return poll;
    }

    @Override
    public void setPoll(Poll newPoll) {
        poll = newPoll;
    }
}
