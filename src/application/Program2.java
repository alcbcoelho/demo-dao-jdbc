package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: Department findById ===");
        Department dep = departmentDao.findById(3);
        System.out.println(dep);

        System.out.println("\n=== TEST 2: Department findAll === ");
        List<Department> departments = departmentDao.findAll();
        departments.forEach(System.out::println);

        System.out.println("\n=== TEST 3: Department insert === ");
        dep = new Department(null, "Food");
        departmentDao.insert(dep);
        System.out.println("Inserted! New id = " + dep.getId());

        System.out.println("\n=== TEST 4: Department update === ");
        dep.setName("Music");
        departmentDao.update(dep);

        System.out.println("\n=== TEST 5: Department delete === ");
        departmentDao.deleteById(10);
    }
}
