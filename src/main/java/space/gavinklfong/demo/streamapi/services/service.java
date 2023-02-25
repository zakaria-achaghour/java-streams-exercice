package space.gavinklfong.demo.streamapi.services;

import lombok.RequiredArgsConstructor;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class service {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final CustomerRepo customerRepo;

   // Exercise 1 — Obtain a list of products belongs to category “Books” with price > 100
    public List<Product> ex1() {
        return productRepo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .filter(p -> p.getPrice() > 100)
                .collect(Collectors.toList());
    }

    //Exercise 2 — Obtain a list of order with products belong to category “Baby”
    public List<Order> ex2() {
        return orderRepo.findAll()
                .stream()
                .filter(o ->
                        o.getProducts()
                                .stream()
                                .anyMatch(p -> p.getCategory().equalsIgnoreCase("Baby"))
                )
                .collect(Collectors.toList());
    }

//Exercise 3 — Obtain a list of product with category = “Toys” and then apply 10% discount
    public List<Product> ex3() {
        return productRepo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
                .map(p -> p.withPrice(p.getPrice() * 0.9))
                .collect(Collectors.toList());
    }
    // Exercise 4 — Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021
        public List<Product> ex4() {
            return orderRepo.findAll()
                    .stream()
                    .filter(o -> o.getCustomer().getTier() == 2)
                    .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
                    .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 4, 1)) <= 0)
                    .flatMap(o -> o.getProducts().stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
//Exercise 5 — Get the cheapest products of “Books” category
    public Optional<Product> ex5() {
        return productRepo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .min(Comparator.comparing(Product::getPrice));
    }
// Exercise 6 — Get the 3 most recent placed order

    public List<Order> ex6() {
        return orderRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    // Exercise 7 — Get a list of orders which were ordered on 15-Mar-2021, log the order records to the console and then return its product list
    public List<Product> ex7() {
        return orderRepo.findAll()
                .stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .peek(o -> System.out.println(o.toString()))
                .flatMap(o -> o.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    // Exercise 8 — Calculate total lump sum of all orders placed in Feb 2021
    public Double ex8() {
        return orderRepo.findAll()
                .stream()
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 3, 1)) < 0)
                .flatMap(o -> o.getProducts().stream())
                .mapToDouble(p -> p.getPrice())
                .sum();
    }

// Exercise 9 — Calculate order average payment placed on 14-Mar-2021
    public Double ex9() {
        return orderRepo.findAll()
                .stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .flatMap(o -> o.getProducts().stream())
                .mapToDouble(p -> p.getPrice())
                .average().getAsDouble();
    }

    // Exercise 10 — Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category “Books”
    public DoubleSummaryStatistics statistics () {
        return productRepo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(p -> p.getPrice())
                .summaryStatistics();
    }

    // Exercise 11 — Obtain a data map with order id and order’s product count

    public Map<Long, Integer> ex11() {
        return orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                order -> order.getId(),
                                order -> order.getProducts().size()
                        )
                );
    }

    // Exercise 12 — Produce a data map with order records grouped by customer
    public Map<Customer, List<Order>> ex12 () {
        return orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(Order::getCustomer)
                );
    }

    // Exercise 13 — Produce a data map with order record and product total sum
    // Function.identity() is used to tell Collectors.toMap() to use the data element as the key.
    public Map<Order, Double> ex13 () {
        return orderRepo.findAll()
                .stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                order -> order.getProducts().stream()
                                        .mapToDouble(p -> p.getPrice()).sum()
                        )
                );
    }

    // Exercise 14 — Obtain a data map with list of product name by category

    public  Map<String, List<String>> ex14 () {
        return  productRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.mapping(product -> product.getName(), Collectors.toList()))
                );
    }

    // Exercise 15 — Get the most expensive product by category
    public  Map<String, Optional<Product>> ex15() {
        return productRepo.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory,
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)))
                );
    }




}
