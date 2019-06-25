/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pollweb.data.impl.ManagerImpl;
import pollweb.data.model.Manager;
import pollweb.data.util.DAO;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

/**
 *
 * @author venecia2
 */
public class ManagerDAO_MySQL extends DAO implements ManagerDAO{
    
    public ManagerDAO_MySQL(DataLayer d) {
        super(d);
    }

    
    private PreparedStatement sManagerByID;
    private PreparedStatement sManagers, sManagersByEmailAndPwd, sManagersByEmail;
    private PreparedStatement iManager, uManager, dManager;
    
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompile all the queries uses in this class
            sManagerByID = connection.prepareStatement("SELECT * FROM manager WHERE ID=?");
            sManagersByEmailAndPwd = connection.prepareStatement("SELECT ID FROM manager WHERE email=? AND password=?");
            sManagers = connection.prepareStatement("SELECT ID FROM manager");

            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iManager = connection.prepareStatement("INSERT INTO manager (email, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uManager = connection.prepareStatement("UPDATE manager SET email=?,password=? WHERE ID=?");
            dManager = connection.prepareStatement("DELETE FROM manager WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing pollweb data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //also closing PreparedStamenents is a good practice...
        try {

            sManagerByID.close();

            sManagersByEmailAndPwd.close();
            sManagersByEmail.close();
            sManagers.close();

            iManager.close();
            uManager.close();
            dManager.close();

        } catch (SQLException ex) {
            Logger.getLogger(ManagerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
    }

    @Override
    public ManagerImpl createManager(ResultSet rs) throws DataException {
    
        ManagerImpl a = new ManagerImpl();
        try {
            a.setKey(rs.getInt("ID"));
            a.setPassword(rs.getString("password"));
            a.setEmail(rs.getString("email"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create manager object form ResultSet", ex);
        }
        return a;
    }

    
    @Override
    public Manager getManager(int manager_key) throws DataException {
        try {
            sManagerByID.setInt(1, manager_key);
            try (ResultSet rs = sManagerByID.executeQuery()) {
                if (rs.next()) {
                    return createManager(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load users by ID", ex);
        }

        return null;
    }

    @Override
    public Manager getManager(String email, String password) throws DataException {
        Manager result = null;

        try {
            sManagersByEmailAndPwd.setString(1, email);
            sManagersByEmailAndPwd.setString(2, password);
            try (ResultSet rs = sManagersByEmailAndPwd.executeQuery()) {
                while (rs.next()) {
                    result = ((Manager) getManager(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load managers by email", ex);
        }
        if (result == null) {
            throw new DataException("Not existing manager");
        }
        return result;
    }


    @Override
    public List<Manager> getManagers() throws DataException {
        
        List<Manager> result = new ArrayList();

        try (ResultSet rs = sManagers.executeQuery()) {
            while (rs.next()) {
                result.add((Manager) getManager(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load managers", ex);
        }
        return result;
    }


    @Override
    public void storeManager(ManagerImpl manager) throws DataException {
        int key = manager.getKey();
        try {
            if (manager.getKey() > 0) { //update

                uManager.setString(1, manager.getEmail());
                uManager.setString(2, manager.getPassword());
                uManager.setInt(4, manager.getKey());
                uManager.executeUpdate();

            } else { //insert
                iManager.setString(1, manager.getEmail());
                iManager.setString(2, manager.getPassword());

                if (iManager.executeUpdate() == 1) {
                    try (ResultSet keys = iManager.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    manager.setKey(key);
                }
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to store manager", ex);
        }
    }


    @Override
    public void deleteManager(Manager manager) throws DataException {
        try {
            dManager.setInt(1, manager.getKey());
            dManager.executeUpdate();

        } catch (SQLException ex) {
            throw new DataException("Unable to delete manager", ex);
        }
    }

    @Override
    public Manager createManager() {
        return new ManagerImpl();
    }
}
