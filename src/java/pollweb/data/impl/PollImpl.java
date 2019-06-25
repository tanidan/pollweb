/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;

import pollweb.data.model.Manager;
import pollweb.data.model.Poll;

/**
 *
 * @author venecia2
 */
public class PollImpl implements Poll{
    
    int key;
    String title;
    Manager manager;
    String openText;
    String closeText;
    boolean isReserved;
    
    public PollImpl(){}

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Manager getManager() {
        return manager;
    }

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public String getOpenText() {
        return openText;
    }

    @Override
    public void setOpenText(String openText) {
        this.openText = openText;
    }

    @Override
    public String getCloseText() {
        return closeText;
    }

    @Override
    public void setCloseText(String closeText) {
        this.closeText = closeText;
    }

    @Override
    public boolean isReserved() {
        return isReserved;
    }

    @Override
    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }
}
