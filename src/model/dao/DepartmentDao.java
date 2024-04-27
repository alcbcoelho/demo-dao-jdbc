package model.dao;

import model.entities.Department;

import java.util.List;

/*

=== PADRÃO DAO ===

O padrão de projeto DAO surgiu com a necessidade de separarmos a lógica de negócios da lógica de persistência de dados,
esta última sendo de responsabilidade do DAO (data access object).

Classes DAO são responsáveis por trocar informações com o SGBD e oferecer operações CRUD e de pesquisas. Eles extraem
dados do BD e transformam-nos em objetos ou lista de objetos para uso na aplicação, além de também fazerem o caminho
inverso, de transformarem objetos e listas em dados para o BD.

*/

public interface DepartmentDao {
    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
