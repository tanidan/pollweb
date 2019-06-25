/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;

import java.util.List;
import pollweb.data.model.Manager;
import pollweb.data.model.Poll;

/**
 *
 * @author venecia2
 */
public class ManagerImpl extends PersonImpl implements Manager {
        
    public ManagerImpl() {
        this.key = 0;
        this.password = "";
        this.email = "";
    }
}
