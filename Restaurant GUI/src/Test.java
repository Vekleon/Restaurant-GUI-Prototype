import dishes.*;
import employees.*;
import javafx.scene.text.Font;
import restaurant.*;

public class Test {
    public static void main(String[] args) {
        String s = "1: ba sd ads, -2: sdasakjdg sadkfn sdkj, c: fa";
        System.out.println(s.length() == 0 ||
                s.matches("(-?[0-9]+:\\s[a-zA-z\\s]+)(,\\s-?[0-9]+:\\s[a-zA-z\\s]+)*"));

//        System.out.println("".matches("[^\\.]"));
        Restaurant restaurant = new Restaurant();
        Server server = new Server();
        Order order = server.createOrder(1, restaurant.getMenu());
//        Recipe dish1 = server.addToOrder(order, "Combo1");
//        server.addToOrder(order, "Combo2");
//        Recipe dish3 = server.addToOrder(order, "5");
//        server.addToOrder(order, "8");
//        server.addToOrder(order, "Combo1");
//        server.addToOrder(order, "Combo2");
//        server.addToOrder(order, "5");
//        server.addToOrder(order, "8");
        Order order2 = server.createOrder(2, restaurant.getMenu());
//        server.addToOrder(order2, "Combo1");
//        server.addToOrder(order2, "Combo2");
//        server.addToOrder(order2, "5");
//        server.addToOrder(order2, "8");
//        server.makeAccommodation(order.getDish("Combo1"), "Hash Browns", "Onions", 5);
//        server.makeAccommodation(order.getDish("Combo1"), "Hash Browns", "Bacon", 5);
//        server.makeAccommodation(order.getDish(dish3.getName()), dish3.getName(), "Ketchup", 5);
        Recipe[] dishes = new Recipe[order.getStatusDishes("unconfirmed").size()];
        Recipe[] dishes2 = new Recipe[order2.getStatusDishes("unconfirmed").size()];
        int i = 0;
        for (Recipe dish : order.getStatusDishes("unconfirmed")) {
            dishes[i] = dish;
            i++;
        }
        int j = 0;
        for (Recipe dish : order2.getStatusDishes("unconfirmed")) {
            dishes2[j] = dish;
            j++;
        }
//        ArrayList<Recipe[]> array = new ArrayList<>();
//        array.add(dishes);
//        array.add(dishes2);
//        Bill bill = new Bill(array, 8);
//        String[] bills = bill.printBill();
//        String finalLine = bills[0].split("\n")[bills[0].split("\n").length - 1];
//        System.out.println(finalLine.split("\\s+")[1]);
//        BillCreator billCreator = new BillCreator(server);
//        if (bills.length == 1) {
//            Stage singleBillPopUp = billCreator.singleBillStage(bills[0]);
//            singleBillPopUp.show();
//        } else {
//            Stage multiplePopUp = billCreator.multipleBillStage(bills);
//            multiplePopUp.show();
//        }
//        System.out.println(("Bill split among 8 people.").length());
//        System.out.println(DishInterpreter.chefDishInfo(dish1, "   "));
        for (String font : Font.getFontNames()) {
            System.out.println(font);
        }
    }
}
