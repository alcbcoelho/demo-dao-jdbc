package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
           st = conn.prepareStatement(
                    "SELECT seller.*, department.name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.id "
                    + "WHERE seller.Id = ?"
            );

           st.setInt(1, id);
           rs = st.executeQuery();

           if (rs.next()) {
               return new Seller(
                       rs.getInt("Id"),
                       rs.getString("Name"),
                       rs.getString("Email"),
                       rs.getDate("BirthDate"),
                       rs.getDouble("BaseSalary"),
                       new Department(rs.getInt("DepartmentId"), rs.getString("DepName"))
               );
           }
           return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(rs);
            DB.close(st);
            // não fechamos a conexão porque pode ser que aproveitemos o DAO para outras operações;
            // assim, fecha-se a conexão no programa principal, quando a instância do DAO tiver
            // concluído todas as operações desejadas.
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
