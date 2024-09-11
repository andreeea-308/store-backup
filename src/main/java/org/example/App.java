package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jdk.jfr.SettingControl;
import org.example.binding.UiModule;
import org.example.model.RoleType;
import org.example.model.User;
import org.example.service.CartService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.example.utils.ApplicationSettingsSingleton;
import org.example.utils.Language;
import org.example.utils.Utils;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        userMenu();
    }

    private static void mainMenu(boolean isAdministrator) {

        Utils.printSuccess(Language.getMessageWelcome() + ApplicationSettingsSingleton.getInstance().getCurrentUser().getName() + "!");
        Injector injector = Guice.createInjector(new UiModule());
        ProductService productService = injector.getInstance(ProductService.class);
        CartService cartService = injector.getInstance(CartService.class);
        Scanner scanner = injector.getInstance(Scanner.class);

        UserService userService = injector.getInstance(UserService.class);

        while (true) {
            if(ApplicationSettingsSingleton.getInstance().getCurrentUser() == null)
                break;
            System.out.println("0. Logout");
            System.out.println("1. Vizualizare produse");
            System.out.println("2. Adaugare produs in cos");
            System.out.println("3. Listare Cos");
            System.out.println("4. Trimitere comanda");
            System.out.println("5. Stergere cont");
            System.out.println("6. Schimba parola");
            if (isAdministrator) {
                System.out.println("10. Creare produs");
                System.out.println("11. Stergere produs");
                System.out.println("7. Editare produs");
                System.out.println("8. Total Incasari pe zi");
                System.out.println("9. Total Incasari pe luna");
            }

            int number = Utils.readInt(scanner, "Alegeti o optiune", 0, isAdministrator ? 11 : 6);

            if (number == 0 ) {
                ApplicationSettingsSingleton.getInstance().setCurrentUser(null);
                Utils.printSuccess("La revedere, " + ApplicationSettingsSingleton.getInstance().getCurrentUser().getName() + "!");
                break;
            } else
                switch (number) {
                    case 1: {
                        Utils.printProducts(productService.viewProducts());
                        break;
                    }

                    case 2: {
                        cartService.addProductToCart();
                        break;
                    }
                    case 3: {
                        cartService.listCart();
                        break;
                    }
                    case 4: {
                        cartService.checkoutCart();
                        break;

                    }
                    case 10: {
                        if (isAdministrator)

                            productService.addProduct();
                        break;
                    }
                    case 11: {
                        if (isAdministrator)
                            productService.deletePoduct();
                        break;
                    }
                    case 7: {
                        if (isAdministrator)
                            productService.editProduct();
                        break;
                    }
                    case 8: {
                        if (isAdministrator)
                            System.out.println(cartService.getTotalDay());
                        break;
                    }
                    case 9: {
                        if (isAdministrator)
                            System.out.println(cartService.getTotalMonth());
                        break;
                    }
                    case 5: {
                        userService.deleteAccount();
                        break;
                    }
                    case 6: {
                       userService.changePassword();
                        break;
                    }
                    default:
                        Utils.printError("Nu exista optiunea. Va rugam reincercati.");
                        break;
                }
        }
    }

    private static void userMenu() {
        Injector injector = Guice.createInjector(new UiModule());

        UserService userService = injector.getInstance(UserService.class);

        Scanner scanner = injector.getInstance(Scanner.class);

        while (true) {
            System.out.println("0. Iesire");
            System.out.println("1. Login");
            System.out.println("2. Creare cont");

//            int number = scanner.nextInt();
            int number = Utils.readInt(scanner, "Alegeti o optiune", 0, 2);
            if (number == 0)
                break;
            else
                switch (number) {
                    case 1: {
                        User currentUser = userService.login();
                        if (currentUser != null) {
                            ApplicationSettingsSingleton.getInstance().setCurrentUser(currentUser);
                            mainMenu(currentUser.getRoleType() == RoleType.ADMINISTRATOR);
                        } else
                            Utils.printError("Ai gresit utilizatorul sau parola. Incerca din nou!");
                        break;
                    }
                    case 2: {
                        userService.register();
                        break;
                    }
                    default:
                        Utils.printError("Nu exista optiunea. Va rugam reincercati.");
                        break;
                }
        }
    }

}
