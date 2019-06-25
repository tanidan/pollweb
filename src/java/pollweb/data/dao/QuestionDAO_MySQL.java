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
import pollweb.data.impl.QuestionImpl;
import pollweb.data.model.Poll;
import pollweb.data.model.Question;
import pollweb.data.util.DAO;
import pollweb.data.util.DataException;
import pollweb.data.util.DataLayer;

/**
 *
 * @author venecia2
 */
public class QuestionDAO_MySQL extends DAO implements QuestionDAO {

    private PreparedStatement getQuestionByID, getQuestionsByPoll;
    private PreparedStatement iQuestion, uQuestion, dQuestion;

    DataLayer dataLayer;

    public QuestionDAO_MySQL(DataLayer d) {
        super(d);
        dataLayer = d;
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompile all the queries uses in this class
            getQuestionByID = connection.prepareStatement("SELECT * FROM question WHERE ID=?");
            getQuestionsByPoll = connection.prepareStatement("SELECT ID AS questionID FROM question WHERE poll_ID=? ORDER BY position");

            //note the last parameter in this call to prepareStatement:
            //it is used to ensure that the JDBC will sotre and return
            //the auto generated key for the inserted recors
            iQuestion = connection.prepareStatement("INSERT INTO question (poll_ID,type,isMandatory,text,answer,note,position) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uQuestion = connection.prepareStatement("UPDATE question SET poll_ID=?,type=?,isMandatory=?,text=?,answer=?,note=?,position=? WHERE ID=?");
            dQuestion = connection.prepareStatement("DELETE FROM question WHERE ID=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing pollweb data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            getQuestionByID.close();
            getQuestionsByPoll.close();

            iQuestion.close();
            uQuestion.close();
            uQuestion.close();

        } catch (SQLException ex) {
            throw new DataException("Error destroying pollweb data layer", ex);
        }
        super.destroy();
    }

    @Override
    public Question createQuestion() {
        return new QuestionImpl();
    }

    @Override
    public Question createQuestion(ResultSet rs) throws DataException {
        QuestionImpl q = (QuestionImpl) createQuestion();
        try {
            q.setKey(rs.getInt("ID"));
            q.setPoll(((PollDAO) dataLayer.getDAO(Poll.class)).getPoll(rs.getInt("poll_ID")));
            q.setText(rs.getString("text"));
            q.setAnswer(rs.getString("answer"));
            q.setType(rs.getString("type"));
            q.setNote(rs.getString("note"));
            q.setPosition(rs.getInt("position"));
            q.setMandatory(rs.getBoolean("isMandatory"));

            return q;
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
    }

    @Override
    public Question getQuestion(int question_ID) throws DataException {
        try {
            getQuestionByID.setInt(1, question_ID);
            try (ResultSet rs = getQuestionByID.executeQuery()) {
                if (rs.next()) {
                    return createQuestion(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load question by ID", ex);
        }

        return null;
    }

    @Override
    public List<Question> getQuestionsByPoll(Poll poll) throws DataException {
        List<Question> result = new ArrayList();

        try {
            getQuestionsByPoll.setInt(1, poll.getKey());
            try (ResultSet rs = getQuestionsByPoll.executeQuery()) {
                while (rs.next()) {
                    result.add((Question) getQuestion(rs.getInt("questionID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load questions by poll", ex);
        }
        return result;
    }

    @Override
    public void storeQuestion(QuestionImpl question) throws DataException {
        int key = question.getKey();
        try {
            if (question.getKey() > 0) { //update
                uQuestion.setInt(1, question.getPoll().getKey());
                uQuestion.setString(2, question.getType());
                uQuestion.setBoolean(3, question.isMandatory());
                uQuestion.setString(4, question.getText());
                if (question.getAnswer() != null) {
                    uQuestion.setString(5, question.getAnswer());
                } else {
                    uQuestion.setNull(5, java.sql.Types.INTEGER);
                }
                if (question.getNote() != null) {
                    uQuestion.setString(6, question.getNote());
                } else {
                    uQuestion.setNull(6, java.sql.Types.INTEGER);
                }
                uQuestion.setInt(6, question.getPosition());
                uQuestion.executeUpdate();
            } else { //insert
                iQuestion.setInt(1, question.getPoll().getKey());
                iQuestion.setString(2, question.getType());
                iQuestion.setBoolean(3, question.isMandatory());
                iQuestion.setString(4, question.getText());
                if (question.getAnswer() != null) {
                    iQuestion.setString(5, question.getAnswer());
                } else {
                    iQuestion.setNull(5, java.sql.Types.INTEGER);
                }
                if (question.getNote() != null) {
                    iQuestion.setString(6, question.getNote());
                } else {
                    iQuestion.setNull(6, java.sql.Types.INTEGER);
                }
                iQuestion.setInt(7, question.getPosition());
                if (iQuestion.executeUpdate() == 1) {
                    try (ResultSet keys = iQuestion.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                    question.setKey(key);
                }
            }

        } catch (SQLException ex) {
            throw new DataException("Unable to store question", ex);
        }
    }

}
