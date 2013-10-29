/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uel.database.postgres;

import br.uel.database.DAOException;
import br.uel.database.DAOFactory;
import br.uel.database.ProviderDAO;
import br.uel.entity.Category;
import br.uel.entity.Provider;
import br.uel.entity.ProviderSought;
import br.uel.entity.User;
import br.uel.log.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author leticia
 */
public class PgProviderDAO extends ProviderDAO {

    public PgProviderDAO(DAOFactory daoFactory_) {
        daoFactory = daoFactory_;
    }

    @Override
    public void create(int pId) throws DAOException {
        try {
            String query = "INSERT INTO spn.provider(provider_id,dat)"
                    + " VALUES (?,'NOW');";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, pId);
            ps.executeQuery();
            conn.close();

        } catch (SQLException ex) {
            Logger.getInstance().setLog(ex.getMessage() + ex.getCause());
        }
    }

    @Override
    public List<User> list(boolean orderByName) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User read(int pid) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Provider p) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(int pId) throws DAOException {
        try {
            String query = "DELETE FROM spn.provider WHERE provider_id=?";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, pId);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            throw new DAOException(ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public boolean providerExistsById(int uid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Category> listCat(int provId) throws DAOException {
        List<Category> list = null;
        try {

            String query = "SELECT * FROM spn.provider NATURAL JOIN ("
                    + "SELECT * FROM spn.prov_has_cat NATURAL JOIN spn.category) AS FOO\n"
                    + "WHERE provider_id=?";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, provId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setCatId(rs.getInt("cat_id"));
                c.setParentId(rs.getInt("parent_id"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
            conn.close();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PgProviderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public void connect(Integer userId, int catId) {
        try {
            String query = "INSERT INTO spn.prov_has_cat VALUES(?,?,'NOW');";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, catId);
            ps.execute();
            conn.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PgProviderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void disconnect(Integer userId, int catId) {
        try {
            String query = "DELETE FROM spn.prov_has_cat WHERE provider_id=? AND cat_id=?;";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setInt(2, catId);
            ps.execute();
            conn.close();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PgProviderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ProviderSought> searchByCategory( int catId, int limit, int offset) {
        ArrayList<ProviderSought> array = new ArrayList();
        try {
            String query = "SELECT u.user_id, u.name AS user_name,u.city, u.state, prov_cat.cat_id,prov_cat.name AS cat_name  FROM spn.user u INNER JOIN "
                    + " (SELECT * FROM spn.prov_has_cat NATURAL JOIN "
                    + "(SELECT * FROM spn.category WHERE cat_id = ? LIMIT 1) as category) AS prov_cat"
                    + " ON u.user_id = prov_cat.provider_id LIMIT ? OFFSET ?; ";
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, catId);
           
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                array.add(this.getObjProviderSought(rs));
            }
            conn.close();

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PgProviderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }

    @Override
    public List<ProviderSought> searchByKeyWord(String keyword, int limit, int offset) {
        ArrayList<ProviderSought> array = new ArrayList();
        try {

            String query = "SELECT u.user_id, u.name AS user_name,u.city, u.state, prov_cat.cat_id,prov_cat.name AS cat_name  FROM spn.user u INNER JOIN "
                    + " (SELECT * FROM spn.prov_has_cat NATURAL JOIN "
                    + "(" + this.getLikesCategory(keyword) + ") as category) AS prov_cat"
                    + " ON u.user_id = prov_cat.provider_id LIMIT ? OFFSET ?; ";
            Logger.getInstance().setLog("teste query : " + query);
            Connection conn = daoFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
           
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                array.add(this.getObjProviderSought(rs));
            }
            conn.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PgProviderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;


    }

    private ProviderSought getObjProviderSought(ResultSet rs) throws SQLException {
        ProviderSought ps = new ProviderSought();
        ps.setId(rs.getInt("user_id"));
        ps.setName(rs.getString("user_name"));
        ps.setCity(rs.getString("city"));
        ps.setState(rs.getString("state"));
        ps.setCatId(rs.getInt("cat_id"));
        ps.setCatName(rs.getString("cat_name"));
        return ps;
    }

    private String getLikesCategory(String keyword) {
        String str = "SELECT * FROM spn.category ";
        String s[] = keyword.split(" ");
        str += " WHERE upper(name) LIKE upper('%" + s[0] + "%') ";
        for (int i = 1; i < s.length; i++) {
            str += " OR upper(name) LIKE upper('%" + s[i] + "%') ";
        }
        return str;
    }
}