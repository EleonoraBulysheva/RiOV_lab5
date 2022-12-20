package runner;

import dto.Cart;
import dto.Catalog;
import dto.User;
import dto.item.impl.ChairItem;
import dto.item.impl.LampItem;
import dto.item.impl.MeatItem;
import dto.item.impl.TableItem;
import enums.SortEnum;
import repo.CatalogRepo;
import repo.ItemRepo;

import java.util.Date;
import java.util.HashMap;

import static constant.PropertyConstant.BCRYPT_CONSTANT_PARAMETER;

public class OnlineMarket {

    private static final HashMap<String, User> users = new HashMap<>(10);
    private static final Cart cart = new Cart();

    public static boolean isExistsUser(String login) {
        return users.containsKey(login);
    }

    public static User getUser(String login) {
        return users.get(login);
    }


    public static void main(String[] args) {

        init();

        User user = User.login("Никита", "123Oleg");
        System.out.printf("Не существует пользователя: %s\n", user.getLogin());

        user = User.login("Максим", "123Amin");
        System.out.printf("Существующий пользователь: %s\n", user.getLogin());

        Catalog.showAllCatalogs();

        initItems();
        System.out.println("Сортировать по цене");
        CatalogRepo.findByName("Мебель").printAndSortCatalogItems(SortEnum.PRICE);

        System.out.println("Сортировать по названию");
        Catalog catalog = CatalogRepo.findByName("Мебель");
        catalog.printAndSortCatalogItems(SortEnum.NAME);

        cart.addItem("Новый стул");
        cart.addItem("Стол");
        cart.addItem("Осветительная лампа");
        System.out.println("Показать перед добавлением в корзину");
        catalog.printAndSortCatalogItems(SortEnum.NAME);

        System.out.println("Общая сумма корзины");
        System.out.printf("%.2f\n",cart.getSummary());

        cart.doOrder();
        System.out.println("Общая сумма корзины после покупки");
        System.out.printf("%.2f\n",cart.getSummary());
    }

    private static void init() {
        System.setProperty(BCRYPT_CONSTANT_PARAMETER, "$2a$10$DjOkk0IX5.zEu/VraKU7re");
        users.put("Вика", User.register("Вика", "123Вика"));
        users.put("Лера", User.register("Лера", "123Лера"));
        users.put("Даня", User.register("Даня", "123Даня"));

        CatalogRepo.save(new Catalog("Еда"));
        CatalogRepo.save(new Catalog("Мебель"));
    }

    private static void initItems() {
        Catalog catalog = CatalogRepo.findByName("Мебель");
        Catalog food = CatalogRepo.findByName("Еда");
        ItemRepo.add(new ChairItem("Новый стул", 12.2, catalog, "дсп", "высокие технологии"));
        ItemRepo.add(new ChairItem("Абсолютный стул", 21.2, catalog, "дерево", "старый"));
        ItemRepo.add(new TableItem("Стол", 212.2, catalog, "дерево", "старый"));
        ItemRepo.add(new LampItem("Осветительная лампа", 21.2, catalog, "сталь", "стиль", "белый"));
        ItemRepo.add(new MeatItem("Говядина", 41.2, food, new Date(), "старый"));
    }

}
