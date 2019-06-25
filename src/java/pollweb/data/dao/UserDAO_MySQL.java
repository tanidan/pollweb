package pollweb.data.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pollweb.data.impl.UserImpl;
import pollweb.data.model.Poll;
import pollweb.data.model.User;
import pollweb.data.util.DAO;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

public class UserDAO_MySQL extends DAO implements UserDAO {

    protected DataLayer dl;


    public UserDAO_MySQL(DataLayer d) {
        super(d);
        this.dl = d;
    }

    private PreparedStatement sUsersByID;
    private PreparedStatement sUsers, sUsersByEmailAndPwd, sUsersByEmail;
    private PreparedStatement iUser, uUser, dUser;

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompile all the queries uses in this class
            sUsersByID = connection.prepareStatement("SELECT * FROM user WHERE ID=?");
            sUsersByEmailAndPwd = connection.prepareStatement("SELECT ID FROM user WHERE email=? AND password=?");
            sUsers = connection.prepareStatement("SELECT ID FROM user");

            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iUser = connection.prepareStatement("INSERT INTO user (email, password, poll_ID) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uUser = connection.prepareStatement("UPDATE user SET email=?,password=?, poll_ID=? WHERE ID=?");
            dUser = connection.prepareStatement("DELETE FROM user WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing pollweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //also closing PreparedStamenents is a good practice...
        try {

            sUsersByID.close();

            sUsersByEmailAndPwd.close();
            sUsersByEmail.close();
            sUsers.close();

            iUser.close();
            uUser.close();
            dUser.close();

        } catch (SQLException ex) {
            Logger.getLogger(ManagerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
    }

    @Override
    public User getUser(int user_key) throws DataException {

        try {
            sUsersByID.setInt(1, user_key);
            try (ResultSet rs = sUsersByID.executeQuery()) {
                if (rs.next()) {
                    return createUser(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load users by ID", ex);
        }

        return null;
    }

    @Override
    public User getUser(String email, String password) throws DataException {
        User result = null;

        try {
            sUsersByEmailAndPwd.setString(1, email);
            sUsersByEmailAndPwd.setString(2, password);
            try (ResultSet rs = sUsersByEmailAndPwd.executeQuery()) {
                while (rs.next()) {
                    result = ((User) getUser(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load users by email", ex);
        }
        if (result == null) {
            throw new DataException("Not existing user");
        }
        return result;
    }

    @Override
    public List<User> getUsers() throws DataException {
        List<User> result = new ArrayList();

        try (ResultSet rs = sUsers.executeQuery()) {
            while (rs.next()) {
                result.add((User) getUser(rs.getInt("userID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load users", ex);
        }
        return result;
    }

    @Override
    public void storeUser(UserImpl user) throws DataException {
        int key = user.getKey();
        try {
            if (user.getKey() > 0) { //update

                uUser.setString(1, user.getEmail());
                uUser.setString(2, user.getPassword());
                uUser.setInt(3, user.getPoll().getKey());
                uUser.setInt(4, user.getKey());
                uUser.executeUpdate();

            } else { //insert
                iUser.setString(1, user.getEmail());
                iUser.setString(2, user.getPassword());
                iUser.setInt(3, user.getPoll().getKey());

                if (iUser.executeUpdate() == 1) {
                    try (ResultSet keys = iUser.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    user.setKey(key);
                }
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to store user", ex);
        }
    }

    @Override
    public UserImpl createUser() {
        return new UserImpl();
    }

    @Override
    public UserImpl createUser(ResultSet rs) throws DataException {

        UserImpl a = createUser();
        try {
            a.setKey(rs.getInt("ID"));
            a.setPassword(rs.getString("password"));
            a.setEmail(rs.getString("email"));
            a.setPoll(((PollDAO) dl.getDAO(Poll.class)).getPoll(rs.getInt("poll_ID")));
        
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
        return a;

    }

    @Override
    public void deleteUser(User user) throws DataException {

        try {
            dUser.setInt(1, user.getKey());
            dUser.executeUpdate();

        } catch (SQLException ex) {
            throw new DataException("Unable to delete user", ex);
        }
    }
}
