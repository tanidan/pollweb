/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.ResultSet;
import java.util.List;
import pollweb.data.impl.ManagerImpl;
import pollweb.data.model.Manager;
import pollweb.data.util.DataException;

/**
 *
 * @author venecia2
 */
public interface ManagerDAO {

    
    Manager createManager();

    ManagerImpl createManager(ResultSet rs) throws DataException;

    Manager getManager(int manager_key) throws DataException;

    Manager getManager(String email, String password) throws DataException;

    List<Manager> getManagers() throws DataException;

    void storeManager(ManagerImpl manager) throws DataException;
    
    void deleteManager(Manager manager) throws DataException;
    
    
}
