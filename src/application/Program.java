package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        try {
            SellerDao sellerDao = DaoFactory.createSellerDao();

            System.out.println("=== TEST 1:  Seller findById ===");
            Seller seller = sellerDao.findById(3);
            System.out.println(seller);

            System.out.println("\n=== TEST 2:  Seller findByDepartment ===");
            Department dep = new Department(1, "Computers");
            List<Seller> sellerList = sellerDao.findByDepartment(dep);
            sellerList.forEach(System.out::println);

            System.out.println("\n=== TEST 3:  Seller findAll ===");
            sellerList = sellerDao.findAll();
            sellerList.forEach(System.out::println);

            System.out.println("\n=== TEST 4:  Seller insert ===");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            seller = new Seller(
                    null,
                    "Donald Brown",
                    "donald@gmail.com",
                    sdf.parse("31/12/1994"),
                    1000.0,
                    dep
            );
            sellerDao.insert(seller);
            System.out.println("Inserted! New id = " + seller.getId());

            System.out.println("\n=== TEST 5:  Seller update ===");
            seller = sellerDao.findById(9);
            seller.setName("Richard Fuchsia");
            seller.setEmail("richard@gmail.com");
            sellerDao.update(seller);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
