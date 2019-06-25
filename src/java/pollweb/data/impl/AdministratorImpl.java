/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;

import java.util.List;
import pollweb.data.model.Administrator;
import pollweb.data.model.Manager;

/**
 *
 * @author venecia2
 */
public class AdministratorImpl extends ManagerImpl implements Administrator{
    
    List<Manager> managers;

    @Override
    public List<Manager> getManagers() {
        return managers;
    }

    @Override
    public void setManagers(List<Manager> managers) {
        this.managers=managers;
    }
    
}
