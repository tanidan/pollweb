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
import pollweb.data.impl.PollImpl;
import pollweb.data.model.Manager;
import pollweb.data.model.Poll;
import pollweb.data.util.DAO;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

/**
 *
 * @author venecia2
 */
public class PollDAO_MySQL extends DAO implements PollDAO{
    
    private PreparedStatement getPollById;
    private PreparedStatement getPollsByManager,getPollsByUser,getUnsignPolls;
    private PreparedStatement iPoll, uPoll, dPoll;
    
    protected DataLayer dataLayer;


    public PollDAO_MySQL(DataLayer d) {
        super(d);
        this.dataLayer = d;
    }
    
    @Override
    public void init()throws DataException{
        try{
            super.init();
            getPollById = connection.prepareStatement("SELECT * FROM poll WHERE ID=?");
            getPollsByManager = connection.prepareStatement("SELECT ID FROM poll WHERE managerID=?");
            getUnsignPolls = connection.prepareStatement("SELECT ID FROM poll WHERE isReserved IS false");
            
            iPoll = connection.prepareStatement("INSERT INTO poll (title,open_tag,close_tag,isReserved,managerID) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uPoll = connection.prepareStatement("UPDATE poll SET title=?, open_tag=?, close_tag=?, isReserved=?,managerID=? WHERE ID=?");
            dPoll = connection.prepareStatement("DELETE FROM poll WHERE ID=?");
            
        }catch(SQLException ex){
            throw new DataException("Error initializing pollweb data layer",ex);
        }                    
    }
    
    @Override
    public void destroy()throws DataException{
        try{
            getPollById.close();
            //getPolls.close();
            getPollsByManager.close();
            getPollsByUser.close();
            getUnsignPolls.close();
            
            iPoll.close();
            uPoll.close();
            dPoll.close();
        }catch(SQLException ex){
            throw new DataException("Error destroying pollweb data layer",ex);
        }
        super.destroy();
    }
    
    @Override
    public Poll createPoll() {
        return new PollImpl();
    }

    @Override
    public Poll createPoll(ResultSet rs) throws DataException {
        PollImpl p = (PollImpl) createPoll();
        try{
            p.setKey(rs.getInt("ID"));
            Manager manager = ((ManagerDAO) dataLayer.getDAO(Manager.class)).getManager(rs.getInt("managerID"));
            p.setManager(manager);
            p.setTitle(rs.getString("title"));
            p.setOpenText(rs.getString("open_tag"));
            p.setCloseText(rs.getString("close_tag"));
            p.setReserved(rs.getBoolean("isReserved"));
            return p;
        }catch(SQLException ex){
            throw new DataException("Unable to create poll object from ResultSet",ex);
        }      
    }

    @Override
    public Poll getPoll(int poll_key) throws DataException {
         try {
            getPollById.setInt(1, poll_key);
            try (ResultSet rs = getPollById.executeQuery()) {
                if (rs.next()) {
                    return createPoll(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load poll by ID", ex);
        }
        return null;
    }

    @Override
    public List<Poll> getPolls(Manager manager) throws DataException {
        List<Poll> result = new ArrayList();
        
        try{
            getPollsByManager.setInt(1, manager.getKey());
            try(ResultSet rs = getPollsByManager.executeQuery()){
                while (rs.next()) {
                    result.add(getPoll(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load polls by manager", ex);
        }
        return result;
    }
    
    @Override
    public void storePoll(Poll poll) throws DataException {
        int key = poll.getKey();
        try {
            if (poll.getKey() > 0) { //update
            
                
                if (poll.getTitle() != null) {
                uPoll.setString(1, poll.getTitle());
                } else {
                    uPoll.setNull(1, java.sql.Types.INTEGER);
                }
                if (poll.getOpenText() != null) {
                uPoll.setString(2, poll.getOpenText());
                }else {
                    uPoll.setNull(2, java.sql.Types.INTEGER);
                }
                if (poll.getCloseText() != null) {
                uPoll.setString(3, poll.getCloseText());
                }else {
                    uPoll.setNull(3, java.sql.Types.INTEGER);
                }
                uPoll.setBoolean(4, poll.isReserved());
                uPoll.setInt(5, poll.getManager().getKey());
                uPoll.setInt(6, poll.getKey());

            } else { //insert
            
                if (poll.getTitle() != null) {
                iPoll.setString(1, poll.getTitle());
                } else {
                    iPoll.setNull(1, java.sql.Types.INTEGER);
                }
                if (poll.getOpenText() != null) {
                iPoll.setString(2, poll.getOpenText());
                }else {
                    iPoll.setNull(2, java.sql.Types.INTEGER);
                }
                if (poll.getCloseText() != null) {
                iPoll.setString(3, poll.getCloseText());
                }else {
                    iPoll.setNull(3, java.sql.Types.INTEGER);
                }
                iPoll.setBoolean(4, poll.isReserved());
                iPoll.setInt(5, poll.getManager().getKey());

                if (iPoll.executeUpdate() == 1) {
                    try (ResultSet keys = iPoll.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    poll.setKey(key);
                }
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to store poll", ex);
        }
    }

    @Override
    public List<Poll> getUnsignedPolls() throws DataException {
         List<Poll> result = new ArrayList();
        
        try(ResultSet rs = getUnsignPolls.executeQuery()){
            while (rs.next()) {
                result.add(getPoll(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load unsigned polls", ex);
        }
        return result;
    }

    @Override
    public void deletePoll(Poll poll) throws DataException {
        try {
            dPoll.setInt(1, poll.getKey());
            dPoll.executeUpdate();

        } catch (SQLException ex) {
            throw new DataException("Unable to delete poll", ex);
        }
    }
}
