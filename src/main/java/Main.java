import static java.util.stream.Collectors.summarizingDouble;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] args) {
        DataGenerator dataGenerator = new DataGenerator();
        List<Customer> customers = dataGenerator.getCustomers();
        List<Order> orders = dataGenerator.getOrders();
        List<Product> products = dataGenerator.getProducts();

        System.out.println("------------------1-------------------");
        List<Product> books = products.stream()
                .filter(p -> p.getPrice() > 100 && p.getCategory().equals("Books"))
                .collect(Collectors.toList());
        System.out.println(books);

        System.out.println("------------------2-------------------");
        List<Order> orderList = orders.stream()
                .filter(o -> o.getProducts().stream().anyMatch(p -> p.getCategory().equals("Baby"))                        )
                .collect(Collectors.toList());
        System.out.println(orderList);

        System.out.println("------------------3-------------------");
        products.stream()
                .filter(p -> p.getCategory().equals("Toys"))
                .peek(p -> System.out.println(p.getPrice()))
                .forEach(p -> p.setPrice(p.getPrice() * 0.9));
        System.out.println("-------------------------------------");
        products.stream()
                .filter(p -> p.getCategory().equals("Toys"))
                .forEach(p -> System.out.println(p.getPrice()));

        System.out.println("------------------4-------------------");
        List<Product> productList = orders.stream()
                .filter(o -> o.getDeliveryDate().isAfter(LocalDate.parse("2021-02-01"))
                        && o.getDeliveryDate().isBefore(LocalDate.parse("2021-04-01"))
                        && o.getCustomer().getTier().equals(2))
                .flatMap(o -> o.getProducts().stream())
                .collect(Collectors.toList());
        System.out.println(productList);

        System.out.println("------------------5-------------------");
        Optional<Product> product = products.stream()
                .filter(p -> p.getCategory().equals("Books"))
                .sorted(Comparator.comparing(Product::getPrice))
                .findFirst();
        System.out.println(product);

        System.out.println("------------------6-------------------");
        List<Order> lastOrders = orders.stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(lastOrders);

        System.out.println("------------------7-------------------");
        List<Set<Product>> collectProducts = orders.stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.parse("2021-03-15")))
                .peek(System.out::println)
                .map(Order::getProducts)
                .collect(Collectors.toList());
        System.out.println(collectProducts);

        System.out.println("------------------8-------------------");
        double sum = orders.stream()
                .filter(o -> o.getOrderDate().isAfter(LocalDate.parse("2021-01-31"))
                        && o.getOrderDate().isAfter(LocalDate.parse("2021-03-01")))
                .flatMap(o -> o.getProducts().stream())
                .flatMapToDouble(p -> DoubleStream.of(p.getPrice()))
                .sum();
        System.out.println(sum);

        System.out.println("------------------9-------------------");
        double sumAll = orders.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.parse("2021-03-15")))
                .flatMap(o -> o.getProducts().stream())
                .flatMapToDouble(p -> DoubleStream.of(p.getPrice()))
                .sum();
        long count = orders.stream().filter(o -> o.getOrderDate().isEqual(LocalDate.parse("2021-03-15")))
                .count();
        System.out.println(sumAll / count);

        System.out.println("------------------10-------------------");
        DoubleSummaryStatistics statistics = products.stream()
                .filter(p -> p.getCategory().equals("Books"))
                .collect(summarizingDouble(Product::getPrice));
        System.out.println(statistics);

        System.out.println("------------------11-------------------");
        Map<Long, Long> collect = orders.stream()
                .collect(Collectors.toMap(Order::getId, o -> (long) o.getProducts().size()));
        System.out.println(collect);

        System.out.println("------------------12-------------------");
        Map<Customer, List<Order>> listMap = orders.stream().
                collect(Collectors.groupingBy(Order::getCustomer, Collectors.toList()));
        System.out.println(listMap);

        System.out.println("------------------13-------------------");
        Map<Long, Double> doubleMap = orders.stream()
                .collect(Collectors.toMap(Order::getId,
                        o -> o.getProducts().stream()
                                .mapToDouble(Product::getPrice).sum()));
        System.out.println(doubleMap);

        System.out.println("------------------14-------------------");
        Map<String, List<String>> collectByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.mapping(Product::getName, Collectors.toList())));
        System.out.println(collectByCategory);

        System.out.println("------------------15-------------------");
        Map<String, Optional<Product>> maxPriceByCategory = products.stream()
                .collect(Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.maxBy(Comparator.comparing(Product::getPrice))));
        System.out.println(maxPriceByCategory);
    }
}
