package org.example.service;

import jakarta.inject.Inject;
import org.example.model.*;
import org.example.utils.ApplicationSettingsSingleton;
import org.example.utils.ConstantsRomanian;
import org.example.utils.Utils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CartService {
    @Inject
    private Scanner scanner;

    @Inject
    private Session session;

    @Inject
    private ProductService productService;

    public void addProductToCart() {
        Product product = Utils.chooseProduct(productService.viewProducts(), scanner);
        Cart openCart = getOpenCart();

        Query<CartItem> query = session.createQuery("SELECT c FROM CartItem c where c.product = :product AND c.cart = :cart", CartItem.class);
        query.setParameter("product", product);
        query.setParameter("cart", openCart);
        List<CartItem> cartItemList = query.getResultList();
        CartItem cartItem = (cartItemList.isEmpty() ? null : cartItemList.get(0));

        if (cartItem == null) {
            CartItem c = new CartItem();
            c.setProduct(product);
            c.setCart(openCart);

            Transaction t = session.beginTransaction();
            session.persist(c);
            t.commit();

            cartItem = c;

        }

        int quantity = Utils.readInt(scanner, "Cantitate", 0, product.getStock() - cartItem.getQuantity());

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        Transaction t = session.beginTransaction();

        if (cartItem.getQuantity() > 0) {
            session.merge(cartItem);
            Utils.printSuccess(ConstantsRomanian.PRODUCT_ADDED_TO_CART);
        }
        else {
            session.remove(cartItem);
            Utils.printSuccess(ConstantsRomanian.PRODUCT_REMOVED_FROM_CART);
        }
        t.commit();

    }

    private Cart getOpenCart() {
        User currentUser = ApplicationSettingsSingleton.getInstance().getCurrentUser();
        Query<Cart> query = session.createQuery("SELECT c FROM Cart c where c.user = :user AND c.status = 0", Cart.class);
        query.setParameter("user", currentUser);
        List<Cart> cartList = query.getResultList();
        Cart cart = (cartList.isEmpty() ? null : cartList.get(0));

        if (cart == null) {
            Cart c = new Cart();
            c.setUser(currentUser);
            c.setStatus(CartStatus.OPEN);

            Transaction t = session.beginTransaction();
            session.persist(c);
            t.commit();

            cart = c;

        }

        return cart;
    }

    public void listCart() {
        Cart openCart = getOpenCart();
        for (CartItem cartItem : openCart.getCartItems()) {
            System.out.println(cartItem.getProduct().getName() + " " + cartItem.getQuantity() + " " + cartItem.getTotal());
        }
        System.out.println("Total : " + openCart.getTotal());
    }

    public void checkoutCart() {
        Cart openCart = getOpenCart();
        openCart.setStatus(CartStatus.CLOSED);
        openCart.setCheckoutDate(LocalDate.now());

        Transaction t = session.beginTransaction();
        session.merge(openCart);
        Utils.printSuccess(ConstantsRomanian.ORDER_SENT);
        t.commit();
    }

    public float getTotalDay() {
        System.out.print(ConstantsRomanian.INPUT_A_DAY);
        String localDateString = scanner.next();
        LocalDate localDate = LocalDate.parse(localDateString);

        Query<Cart> query = session.createQuery("SELECT c FROM Cart c where c.checkoutDate = :checkoutDate AND c.status = 1", Cart.class);
        query.setParameter("checkoutDate", localDate);
        List<Cart> cartList = query.getResultList();

        return (float) cartList.stream()
                .mapToDouble(Cart::getTotal)
                .sum();

    }

    public float getTotalMonth() {
        System.out.print(ConstantsRomanian.INPUT_A_MONTH);
        String yearMonthString = scanner.next();
        String[] stringSplit = yearMonthString.split("-");
        if (stringSplit.length > 1) {
            String monthString = stringSplit[1];
            int nextMonth = Integer.parseInt(monthString)+1;

//            if(nextMonth < 10) {
//                String nextMonthStr = "0" + nextMonth;
//            }
//            else{
//                String nextMonthStr = "" + nextMonth;
//            }
            String nextMonthStr = nextMonth < 10?"0"+nextMonth:""+nextMonth;

            String endDateStr = stringSplit[0] + "-" + nextMonthStr + "-01";
            LocalDate from = LocalDate.parse(yearMonthString + "-01");
            LocalDate end = LocalDate.parse(endDateStr);
            //        LocalDate localDate = LocalDate.parse(localDateString);

            Query<Cart> query = session.createQuery("SELECT c FROM Cart c where c.checkoutDate >= :from AND c.checkoutDate < :end AND c.status = 1", Cart.class);
            query.setParameter("from", from);
            query.setParameter("end", end);
            List<Cart> cartList = query.getResultList();

            return (float) cartList.stream()
                    .mapToDouble(Cart::getTotal)
                    .sum();
        }
        Utils.printError(ConstantsRomanian.INVALID_FORMAT);
        return 0;
    }
}
