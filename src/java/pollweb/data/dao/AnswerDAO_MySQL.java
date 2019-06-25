package pollweb.data.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import pollweb.data.impl.AnswerImpl;
import pollweb.data.model.Answer;
import pollweb.data.model.Poll;
import pollweb.data.model.User;
import pollweb.data.util.DAO;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

public class AnswerDAO_MySQL extends DAO implements AnswerDAO {

    protected DataLayer dl;


    public AnswerDAO_MySQL(DataLayer d) {
        super(d);
        this.dl = d;
    }

    private PreparedStatement sAnswerByPoll, sAnswerByID, sAnswerByUser, sAnswers;
    private PreparedStatement iAnswer, uAnswer, dAnswer;

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompile all the queries uses in this class
            sAnswerByID = connection.prepareStatement("SELECT * FROM poll_answer WHERE ID=?");
            sAnswerByPoll = connection.prepareStatement("SELECT * FROM poll_answer WHERE poll_ID=?");
            sAnswerByUser = connection.prepareStatement("SELECT * FROM poll_answer WHERE user_ID=?");
            sAnswers = connection.prepareStatement("SELECT ID FROM poll_answer");

            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iAnswer = connection.prepareStatement("INSERT INTO poll_answer (poll_ID, user_ID, answers) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uAnswer = connection.prepareStatement("UPDATE poll_answer SET poll_ID=?,user_ID=?, answers=? WHERE ID=?");
            dAnswer = connection.prepareStatement("DELETE FROM poll_answer WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing pollweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //also closing PreparedStamenents is a good practice...
        try {
            sAnswerByID.close();
            sAnswerByPoll.close();
            iAnswer.close();
            uAnswer.close();
            dAnswer.close();

        } catch (SQLException ex) {
            Logger.getLogger(ManagerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.destroy();
    }  
    
    @Override
    public Answer getAnswerByUser(int user_key) throws DataException {
        Answer result = null;
        try{
            sAnswerByUser.setInt(1, user_key);
            try (ResultSet rs = sAnswerByUser.executeQuery()) {
                while (rs.next()) {
                    result = ((Answer) getAnswer(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load answer by userID", ex);
        }
        if (result == null) {
            throw new DataException("Not existing answer by user");
        }
        return result;       
    }
    
    
    @Override
    public List<Answer> getAnswerByPoll(int poll_id) throws DataException {
        List<Answer> result = new ArrayList();

        try (ResultSet rs = sAnswerByPoll.executeQuery()) {
            while (rs.next()) {
                result.add((Answer) getAnswer(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load answers by poll", ex);
        }
        return result;
    }
    
    @Override
    public Answer getAnswer(int answer_key) throws DataException {
        try {
            sAnswerByID.setInt(1, answer_key);
            try (ResultSet rs = sAnswerByID.executeQuery()) {
                if (rs.next()) {
                    return createAnswer(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load answer by ID", ex);
        }

        return null;
    }
     
    @Override
    public List<Answer> getAnswers() throws DataException {
       List<Answer> answers = new ArrayList();
        try (ResultSet rs = sAnswers.executeQuery()) {
            while (rs.next()) {
                answers.add(getAnswer(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load answer", ex);
        }
        return answers;
    }
    
    @Override
    public void storeAnswer(AnswerImpl answer) throws DataException {
        int key = answer.getKey();
        try {
            if (answer.getKey() > 0) { //update
           
                uAnswer.setInt(1, answer.getPoll().getKey());
                if(answer.getUser() != null) {
                    uAnswer.setInt(2, answer.getUser().getKey());
                } else {
                    uAnswer.setInt(2,0);
                }
                uAnswer.setBinaryStream(3, new FileInputStream(answer.getFile()),answer.getFile().length());
                uAnswer.executeUpdate();

            } else { //insert
                iAnswer.setInt(1, answer.getPoll().getKey());
                if(answer.getUser() != null) {
                    iAnswer.setInt(2, answer.getUser().getKey());
                } else {
                    iAnswer.setInt(2,0);
                }
                iAnswer.setBinaryStream(3, new FileInputStream(answer.getFile()),answer.getFile().length());

                if (iAnswer.executeUpdate() == 1) {
                    try (ResultSet keys = iAnswer.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    answer.setKey(key);
                }
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to store answer", ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnswerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public AnswerImpl createAnswer() {
        return new AnswerImpl();
    }

    @Override
    public Answer createAnswer(ResultSet rs) throws DataException {       
        AnswerImpl a = createAnswer();
        FileOutputStream fos = null;
        File file = null;
        try {
            String path = String.format("G:\\Univ_LAquila\\Web engineering\\Poll\\PollWeb\\build\\web\\%d.csv", rs.getInt("ID"));
            file = new File(path);
            fos = new FileOutputStream(file);

            int read;

            while (rs.next()) {
                InputStream input = rs.getBinaryStream("answers");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
            
            fos.close();
            a.setKey(rs.getInt("ID"));
            a.setUser(((UserDAO) dl.getDAO(User.class)).getUser(rs.getInt("user_ID")));
            a.setPoll(((PollDAO) dl.getDAO(Poll.class)).getPoll(rs.getInt("poll_ID")));
            a.setFile(file);
                
        }catch (FileNotFoundException ex) {
            throw new DataException("Unable to create answer object form " + file.getAbsolutePath(), ex);
        } catch (IOException ex) {
            Logger.getLogger(AnswerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AnswerDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }

    @Override
    public void deleteAnswer(Answer answer) throws DataException {
        try {
            dAnswer.setInt(1, answer.getKey());
            dAnswer.executeUpdate();

        } catch (SQLException ex) {
            throw new DataException("Unable to delete answer", ex);
        }
    }
}
