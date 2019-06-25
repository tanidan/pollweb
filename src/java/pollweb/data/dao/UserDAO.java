/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.ResultSet;
import java.util.List;
import pollweb.data.impl.UserImpl;
import pollweb.data.model.User;
import pollweb.data.util.DataException;

/**
 *
 * @author venecia2
 */
public interface UserDAO {

    User createUser();

    User createUser(ResultSet rs) throws DataException;

    User getUser(int user_key) throws DataException;

    User getUser(String email, String password) throws DataException;

    List<User> getUsers() throws DataException;

    void storeUser(UserImpl user) throws DataException;
    
    void deleteUser(User user) throws DataException;
}
