package com.grswebservices.employees;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Predicate.not;

public class StreamsStuff {

    private static String myVar;

    public static void main(String[] args) {
        String peopleText = """
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=2000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=4000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=5000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=6000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=7000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmer, {locpd=8000,yoe=10,iq=140}
                Flinstone, Fred, 1/1/1900, Programmerzzzzz, {locpd=9000,yoe=10,iq=140}
                Flinstone2, Fred2, 1/1/1900, Programmer, {locpd=1300,yoe=14,iq=100}
                Flinstone3, Fred3, 1/1/1900, Programmer, {locpd=2300,yoe=8,iq=105}
                Flinstone4, Fred4, 1/1/1900, Programmer, {locpd=1630,yoe=3,iq=115}
                Flinstone5, Fred5, 1/1/1900, Programmer, {locpd=5,yoe=10,iq=100}
                Rubble, Barney, 2/2/1905, Manager, {orgSize=300,dr=10}
                Rubble2, Barney2, 2/2/1905, Manager, {orgSize=100,dr=4}
                Rubble3, Barney3, 2/2/1905, Manager, {orgSize=200,dr=2}
                Rubble4, Barney4, 2/2/1905, Manager, {orgSize=500,dr=8}
                Rubble5, Barney5, 2/2/1905, Manager, {orgSize=175,dr=20}
                Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=3}
                Flinstone2, Wilma2, 3/3/1910, Analyst, {projectCount=4}
                Flinstone3, Wilma3, 3/3/1910, Analyst, {projectCount=5}
                Flinstone4, Wilma4, 3/3/1910, Analyst, {projectCount=6}
                Flinstone5, Wilma5, 3/3/1910, Analyst, {projectCount=9}
                Rubble, Betty, 4/4/1915, CEO, {avgStockPrice=300}
                """;

//        absoluteFileStream();

//        tabulateEmployees(peopleText);

//        streamsPractice1();
//
//        streamsPractice2();

        String myVar = emptyStream();
//        nullableStream(myVar);
//
//        intStream();
//
//        arrayStream();

//        streamsPractice(peopleText);

//        flattenStreamsOfStreams(peopleText);

//        alternativesToFilter(peopleText);

        mapReducePattern(peopleText);

        reduceNonNumericValues();


    }

    private static void reduceNonNumericValues() {
        Optional<String> output = Stream.of("tom", "jerry", "mary", "sam")
                // reduce down to one concatenated string
                .reduce((a, b) -> a.concat("_").concat(b).toUpperCase());
        System.out.println(output.orElse(""));
    }

    private static void mapReducePattern(String peopleText) {
        Predicate<Employee> dummyEmployeeSelector = employee -> "N/A".equals(employee.getLastName());
        Predicate<Employee> overFiveKSelector = e -> e.getSalary() > 5000;
        Predicate<Employee> noDummiesAndOverFiveK = dummyEmployeeSelector.negate().and(overFiveKSelector);
        OptionalInt result = peopleText
                .lines()
                .map(Employee::createEmployee)
                .map(e -> (Employee)e)
                .filter(noDummiesAndOverFiveK)
                .collect(Collectors.toSet())
                .stream()
                .sorted(comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName)
                        .thenComparingInt(Employee::getSalary))
                .skip(5)
                .mapToInt(StreamsStuff::showEmpAndGetSalary)
                // Reduce Methods
                // Min Salary
                // .reduce(Math::min);
                // Max salary with reduce
                // .reduce(150000, (a,b) -> a < b ? a : b);
                // .reduce(Math::max);
                // .reduce((a, b) -> Math.max(a, b));
                 .reduce((a, b) -> a > b ? a : b);
                // .reduce(0, (a, b) -> a + b);
                // .count();
                // .max(); // find max salary in string
                // .min();
                // .average(); // determines the number of items (in this case integers) that are coming out of mapToInt stream and also determining the sum and then dividing those to get the average

        System.out.println(result.orElse(-1));
//        System.out.println(result.orElse(0));
    }

    private static void flattenStreamsOfStreams(String peopleText) {
        peopleText
                .lines()
                .map(Employee::createEmployee)
                .map(e -> (Employee) e)
                .map(Employee::getFirstName)
                .map(firstName -> firstName.split(""))
                .flatMap(Arrays::stream) // flattens redundant child streams into one super stream with whatever the original contents were emdedded
                .map(String::toLowerCase)
                .distinct() // get rid of duplicate letters
                .forEach(System.out::print);
    }

    private static void alternativesToFilter(String peopleText) {
        Predicate<Employee> dummySelector = e -> e.getLastName().equals("N/A");
        Optional<Employee> optionalEmployee = peopleText
                .lines()
                .map(Employee::createEmployee)
                .map(e -> (Employee) e)
                .filter(dummySelector.negate())
                .findAny();
//                .findFirst();
        System.out.println(optionalEmployee // If there is an employee then get the first name out
                .map(Employee::getFirstName)
                .orElse("Nobody")); // otherwise return nobody
//                .noneMatch(e -> e.getSalary() < 0);
//                .anyMatch(e -> e.getSalary() > 10000000);
//                .allMatch(e -> e.getSalary() > 2500);

        /*
        if (employee != null) {
            System.out.println(employee.getFirstName());
        } else {
            System.out.println("Nobody");
        }
         */
    }

    private static void streamsPractice(String peopleText) {
        Predicate<Employee> dummyEmployeeSelector = employee -> "N/A".equals(employee.getLastName());
        Predicate<Employee> overFiveKSelector = e -> e.getSalary() > 5000;
        Predicate<Employee> noDummiesAndOverFiveK = dummyEmployeeSelector.negate().and(overFiveKSelector);
        int sum = peopleText
                .lines()
                // .filter(((Predicate<String>) s -> s.contains("Programmerzzzzz")).negate())
                .map(Employee::createEmployee)
                .map(e -> (Employee)e) // cast as Employee
                .filter(noDummiesAndOverFiveK)
                .filter(not(e -> e instanceof Programmer)) // everyone except Programmers
                .filter(not(e -> e.getLastName().equals("N/A"))) // filter out N/A
                .filter(overFiveKSelector)
                .filter(e -> e.getSalary() < 10000)
                .collect(Collectors.toSet()) // Set<Employee> - terminal operation
                .stream()// have to convert back to Stream after toSet()
                // .distinct()// Returns a stream consisting of the distinct elements (according to Object. equals(Object)) of this stream.
                .sorted(comparing(Employee::getLastName) // (x,y) -> x.getLastName().compareTo(y.getLastName())
                    .thenComparing(Employee::getFirstName)
                    .thenComparingInt(Employee::getSalary))
                .mapToInt(StreamsStuff::showEmpAndGetSalary)
                .sum();
        System.out.println(sum);
    }

    private static void setOfEmployees(String peopleText) {
        Set<Employee> empSet = peopleText
                .lines()
                .map(Employee::createEmployee)
                .map(e -> (Employee)e) // cast as Employee
                .collect(Collectors.toSet()); // Set<Employee> - terminal operation
        System.out.println(empSet);
    }

    private static int showEmpAndGetSalary(IEmployee emp) {
        System.out.println(emp);
        return emp.getSalary();
    }

    private static void absoluteFileStream() {
        try {
            Files.lines(Path.of("C:\\Users\\georg\\IdeaProjects\\Employees\\src\\main\\java\\com\\grswebservices\\employees\\employees.txt"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void arrayStream() {
        String[] names = {"bob", "tom", "jane"};
        Arrays.stream(names)
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    private static void intStream() {
        IntStream.rangeClosed(1,5) // returns IntStream
                .boxed()// returns Stream<Integer> : a stream of integers
                .map(String::valueOf)
//                .map(String::valueOf) // mapToObj() - useful for converting values in IntStream to something that is probably not numerical
                .map(s -> s.concat("-"))
                .forEach(System.out::println);
    }

    private static void nullableStream(String myVar) {
        Stream.ofNullable(myVar) // get nothing - empty stream - subsequent steps of stream's pipeline don't execute because at end of stream already
//        Stream.of(myVar)
                .forEach(System.out::println);
    }

    private static String emptyStream() {
        myVar = null;
        Stream myStream = null;
        if (myVar == null) {
            myStream = Stream.empty();
        } else {
            myStream = Stream.of(myVar);
        }
        myStream
                .forEach(System.out::println);
        return myVar;
    }

    private static void streamsPractice2() {
        record Car(String make, String model){}

        Stream.of(new Car("Ford", "Bronco"), new Car("Tesla", "X"), new Car("Tesla", "3"))
//        Stream.of(1,2,3,4)
                .filter(c -> "Tesla".equals(c.make))
//                .filter(c -> c.make.equals("Tesla"))
                .forEach(System.out::println);
    }

    private static void streamsPractice1() {
        Collection<String> nums = Set.of("one", "two", "three", "four");
        nums.stream()
                .map(String::hashCode)
                .map(Integer::toHexString) // hashcode in decimal form
//                .map(String::length)
//                .map(String::toUpperCase)
                .forEach(h -> System.out.printf("%h%n", h));
//                .forEach(System.out::println);
    }

    private static void tabulateEmployees(String peopleText) {
        peopleText
                .lines()
                .map(Employee::createEmployee) // converting from string to type IEmployee
                .forEach(System.out::println); // System.out.println("hello"); - prints the string to the screen but it does not return any output
    }
}
