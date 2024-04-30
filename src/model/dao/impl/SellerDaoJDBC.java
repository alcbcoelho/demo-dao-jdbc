package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                    + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS     // retorna no formato ResultSet coluna com ID do elemento inserido
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setDouble(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));    // populando a instância de Seller que serve de argumento p/
                                                            // o método com o ID gerado pela operação de inserção ao BD
                }
                DB.close(rs);   // fechando o ResultSet por aqui porque ele foi criado dentro de um bloco 'if', só existindo
                                // portanto aqui dentro
            } else
                throw new DbException("Unexpected error! No rows affected!");
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(st);
        }
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
               Department dep = instantiateDepartment(rs);
               return instantiateSeller(rs, dep);
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

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        return new Seller(
                rs.getInt("Id"),
                rs.getString("Name"),
                rs.getString("Email"),
                rs.getDate("BirthDate"),
                rs.getDouble("BaseSalary"),
                dep
        );
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
    }

    @Override
    public List<Seller> findAll() {
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(
                    "SELECT *, department.Name as DepName FROM seller "
                    + "INNER JOIN department "
                    + "ON DepartmentId = department.id "
                    + "ORDER BY seller.Id"
            );

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>(); // vamos usar este map para fazer um algoritmo (ln 105-109)
            // que possa nos auxiliar a não criar instâncias repetidas de um mesmo Department para cada Seller
            // que é adicionado na lista.

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                sellers.add(instantiateSeller(rs, dep));
            }
            return (sellers.size() > 0) ? sellers : null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(rs);
            DB.close(st);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT *, department.Name as DepName FROM seller "
                            + "INNER JOIN department ON department.Id = seller.DepartmentId "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY seller.Name"
            );

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> sellers = new ArrayList<>();

            while (rs.next()) {
                sellers.add(instantiateSeller(rs, department));
            }
            return (sellers.size() > 0) ? sellers : null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(rs);
            DB.close(st);
        }
    }
}
