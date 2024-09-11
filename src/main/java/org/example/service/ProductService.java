package org.example.service;

import jakarta.inject.Inject;
import org.example.model.Product;
import org.example.utils.ConstantsRomanian;
import org.example.utils.Utils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class ProductService {

    @Inject
    private Scanner scanner;

    @Inject
    private Session session;

    private void readProduct(Product p) {
        System.out.print(ConstantsRomanian.LABEL_NAME);
        p.setName(scanner.next());
        System.out.print(ConstantsRomanian.LABEL_PRICE);
        p.setPrice(scanner.nextFloat());
        int stock = Utils.readInt(scanner, ConstantsRomanian.LABEL_STOCK, 1, null);
        p.setStock(stock);
    }

    public void addProduct() {
        Product p = new Product();
        readProduct(p);

        Transaction t = session.beginTransaction();
        session.persist(p);
        Utils.printSuccess(ConstantsRomanian.PRODUCT_ADDED);
        t.commit();
    }

    public List<Product> viewProducts() {
//       Query<Product> query = session.createNativeQuery("SELECT * FROM product",Product.class);
        Query<Product> query = session.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    public void deletePoduct() {
        Product product = Utils.chooseProduct(viewProducts(), scanner);

        Transaction t = session.beginTransaction();
        session.remove(product);
        Utils.printSuccess(ConstantsRomanian.PRODUCT_REMOVED);
        t.commit();

    }

    public void editProduct() {
        Product product = Utils.chooseProduct(viewProducts(), scanner);
        readProduct(product);

        Transaction t = session.beginTransaction();
        session.merge(product);
        Utils.printSuccess(ConstantsRomanian.PRODUCT_EDITED);
        t.commit();

    }
}
