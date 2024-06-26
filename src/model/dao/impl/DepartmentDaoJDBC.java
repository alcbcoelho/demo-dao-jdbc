package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                "INSERT INTO department (Name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next())
                    obj.setId(rs.getInt(1));
                DB.close(rs);
            } else
                throw new DbException("Unexpected error! No rows affected!");
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE department "
                    + "SET Name = ? "
                    + "WHERE Id = ?"
            );

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0)  // mensagem de erro caso o ID especificado não exista no BD
                throw new DbException("The specified ID doesn't exist!");
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next())
                return instantiateDepartment(rs);
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(rs);
            DB.close(st);
        }
    }

    @Override
    public List<Department> findAll() {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM department ORDER BY Id");

            List<Department> departments = new ArrayList<>();

            while (rs.next()) {
                departments.add(instantiateDepartment(rs));
            }
            return (departments.size() > 0) ? departments : null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.close(rs);
            DB.close(st);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("Id"), rs.getString("Name"));
    }
}
