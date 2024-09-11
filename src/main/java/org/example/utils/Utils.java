package org.example.utils;

import org.example.model.Product;

import java.util.List;
import java.util.Scanner;

public class Utils {
    public static void printProducts(List<Product> productList) {
        productList
                .forEach(System.out::println);
    }

    public static Product chooseProduct(List<Product> productList, Scanner scanner) {
        for (int i = 0; i < productList.size(); i++) {
            System.out.println((i + 1) + ". " + productList.get(i).getName() + " " + productList.get(i).getPrice() + " " + productList.get(i).getStock() + "buc");
        }

//        int index = scanner.nextInt();
        int index = Utils.readInt(scanner, "Alegeti un produs", 1, productList.size());
        return productList.get(index - 1);
    }

    public static int readInt(Scanner scanner, String label, Integer min, Integer max) {
        System.out.print(label + " (" + min + " - " + max + "): ");
        int quantity = scanner.nextInt();
        while ((min != null && quantity < min) || (max != null && quantity > max)) {
            Utils.printError(ConstantsRomanian.INVALID_INPUT);
            System.out.print(label + ": ");
            quantity = scanner.nextInt();
        }
        return quantity;
    }

    public static void printError(String message) {
        // https://www.compart.com/en/unicode/U+1F534
        // https://www.compart.com/en/unicode/search?q=red#characters
        System.out.println("\uD83D\uDD34 " + message);
    }

    public static void printSuccess(String message) {
        // https://www.compart.com/en/unicode/U+1F7E2
        // https://www.compart.com/en/unicode/search?q=green#characters
        System.out.println("\uD83D\uDFE2 " + message);
    }
}
