package com.grswebservices.employees;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        String peopleText = """
            Flinstone, Fred, 1/1/1900, Programmer, {locpd=2000,yoe=10,iq=140}
            Flinstone, Fred, 1/1/1900, Programmer, {locpd=1300,yoe=14,iq=100}
            Flinstone, Fred, 1/1/1900, Programmer, {locpd=2300,yoe=8,iq=105}
            Flinstone, Fred, 1/1/1900, Programmer, {locpd=1630,yoe=3,iq=115}
            Flinstone, Fred, 1/1/1900, Programmer, {locpd=5,yoe=10,iq=100}
            Rubble, Barney, 2/2/1905, Manager, {orgSize=300,dr=10}
            Rubble, Barney, 2/2/1905, Manager, {orgSize=100,dr=4}
            Rubble, Barney, 2/2/1905, Manager, {orgSize=200,dr=2}
            Rubble, Barney, 2/2/1905, Manager, {orgSize=500,dr=8}
            Rubble, Barney, 2/2/1905, Manager, {orgSize=175,dr=20}
            Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=3}
            Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=4}
            Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=5}
            Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=6}
            Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=9}
            Rubble, Betty, 4/4/1915, CEO, {avgStockPrice=300}
            """;

        Matcher peopleMat = Employee.PEOPLE_PAT.matcher(peopleText);

        int totalSalaries = 0;
        IEmployee employee = null;
        List<IEmployee> employees = new ArrayList<>();
        while (peopleMat.find()) {
            employee = Employee.createEmployee(peopleMat.group());
            employees.add(employee);
        }
        // for loop specifically designed for Collections
        for (IEmployee worker: employees) {
            System.out.println(worker.toString());
            totalSalaries+= worker.getSalary();
        }
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        System.out.printf("The total payout should be %s%n", currencyInstance.format(totalSalaries));

        // RECORD VS CLASS
        WeirdoRecord larry = new WeirdoRecord("David", "Larry", LocalDate.of(1951, 1, 1));
        WeirdoClass larry2 = new WeirdoClass("David", "Larry", LocalDate.of(1951, 1, 1));

        WeirdoRecord jake = new WeirdoRecord("Snake", "Jake");
        jake.sayHello();


    }
}



// JAVA 17 switch
//            switch (employee) {
//                case Programmer prog -> System.out.println(prog.getIq());
//                case Manager man -> System.out.println(man.toString());
//                case Analyst analyst ->
//                    // older / conventional way before pattern matching
//                        System.out.println(analyst.toString());
//                case CEO ceo -> System.out.println();
//                default -> System.out.println("Default output");
//            }

// pattern matching available Java 16
//            if (employee instanceof Programmer prog) {
//                System.out.println(prog.getIq());
//            } else if (employee instanceof Manager man) {
//                System.out.println(man.toString());
//            } else if (employee instanceof Analyst) {
//                // older / conventional way before pattern matching
//                Analyst analyst = (Analyst) employee;
//                System.out.println(analyst.toString());
//            } else if (employee instanceof CEO) {
//                System.out.println();
//            } else {
//                System.out.println("Default output");
//            }
// Not idiomatic Java:
//            if (employee.getClass().equals(Programmer.class)) {
//                System.out.println();
//            } else if (employee.getClass().equals(Manager.class)) {
//                System.out.println();
//            } else {
//                System.out.println("Default output");
//            }
// Polymorphism
//            System.out.println(employee.toString());
//            totalSalaries+= employee.getSalary();

/*
/// BAD PROCEDURAL IMPLEMENTATION
package com.grswebservices.employees;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String peopleText = """
    Flinstone, Fred, 1/1/1900, Programmer, {locpd=2000,yoe=10,iq=140}
    Flinstone, Fred, 1/1/1900, Programmer, {locpd=1300,yoe=14,iq=100}
    Flinstone, Fred, 1/1/1900, Programmer, {locpd=2300,yoe=8,iq=105}
    Flinstone, Fred, 1/1/1900, Programmer, {locpd=1630,yoe=3,iq=115}
    Flinstone, Fred, 1/1/1900, Programmer, {locpd=5,yoe=10,iq=100}
    Rubble, Barney, 2/2/1905, Manager, {orgSize=300,dr=10}
    Rubble, Barney, 2/2/1905, Manager, {orgSize=100,dr=4}
    Rubble, Barney, 2/2/1905, Manager, {orgSize=200,dr=2}
    Rubble, Barney, 2/2/1905, Manager, {orgSize=500,dr=8}
    Rubble, Barney, 2/2/1905, Manager, {orgSize=175,dr=20}
    Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=3}
    Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=4}
    Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=5}
    Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=6}
    Flinstone, Wilma, 3/3/1910, Analyst, {projectCount=9}
    Rubble, Betty, 4/4/1915, CEO, {avgStockPrice=300}
    """;

        String peopleRegex = "(?<lastName>\\w+),\\s*(?<firstName>\\w+),\\s*(?<dob>\\d{1,2}/\\d{1,2}/\\d{4}),\\s*(?<role>\\w+)(?:,\\s*\\{(?<details>.*)\\})?\\n";
        Pattern peoplePat = Pattern.compile(peopleRegex);
        Matcher peopleMat = peoplePat.matcher(peopleText);

        String progRegex = "\\w+=(?<locpd>\\w+),\\w+=(?<yoe>\\w),\\w+=(?<iq>\\w)";
        Pattern coderPat = Pattern.compile(progRegex);

        String mgrRegex = "\\w+=(?<orgSize>\\w+),\\w+=(?<dr>\\w+)";
        Pattern mgrPat = Pattern.compile(mgrRegex);

        String analystRegex = "\\w+=(?<projectCount>\\w+)";
        Pattern analystPat = Pattern.compile(analystRegex);

        String ceoRegex = "\\w+=(?<avgStockPrice>\\w+)";
        Pattern ceoPat = Pattern.compile(ceoRegex);

        int totalSalaries = 0;
        // peopleMat.find() returns a boolean
        while (peopleMat.find()) {
            totalSalaries+= switch (peopleMat.group("role")) {
                case "Programmer" -> {
                    Programmer programmer = new Programmer(peopleMat.group());
                    String details = peopleMat.group("details");
                    Matcher coderMat = coderPat.matcher(peopleMat.group("details"));
//                    System.out.println(details);
                    int salary = 0;
                    if (coderMat.find()) {
                        int locpd = Integer.parseInt(coderMat.group("locpd"));
                        int yoe = Integer.parseInt(coderMat.group("yoe"));
                        int iq = Integer.parseInt(coderMat.group("iq"));
                        // System.out.printf("Programmer loc: %s yoe: %s iq: %s%n", locpd, yoe, iq);
                        salary = 3000 + locpd * yoe * iq;
                    } else {
                        salary = 3000;
                    }
                    String lastName = peopleMat.group("lastName");
                    String firstName = peopleMat.group("firstName");
                    System.out.printf("%s: %s: %s%n", lastName, firstName, NumberFormat.getCurrencyInstance().format(salary));
                    yield salary;
                }
                case "Manager" -> {
                    String details = peopleMat.group("details");
                    Matcher mgrMat = mgrPat.matcher(details);
                    int salary = 0;
                    if (mgrMat.find()) {
                        int orgSize = Integer.parseInt(mgrMat.group("orgSize"));
                        int directReports = Integer.parseInt(mgrMat.group("dr"));
                        salary = 3500 + orgSize * directReports;
                    } else {
                        salary = 3500;
                    }
                    String lastName = peopleMat.group("lastName");
                    String firstName = peopleMat.group("firstName");
                    System.out.printf("%s: %s: %s%n", lastName, firstName, NumberFormat.getCurrencyInstance().format(salary));
                    yield salary;
                }
                case "Analyst" -> {
                    String details = peopleMat.group("details");
                    Matcher analystMat = analystPat.matcher(details);
                    int salary = 0;
                    if (analystMat.find()) {
                        int projectCount = Integer.parseInt(analystMat.group("projectCount"));
                        salary = 2500 + projectCount * 2;
                    } else {
                        salary = 2500;
                    }
                    String lastName = peopleMat.group("lastName");
                    String firstName = peopleMat.group("firstName");
                    System.out.printf("%s: %s: %s%n", lastName, firstName, NumberFormat.getCurrencyInstance().format(salary));
                    yield salary;
                }
                case "CEO" -> {
                    String details = peopleMat.group("details");
                    Matcher ceoMat = ceoPat.matcher(details);
                    int salary = 0;
                    if (ceoMat.find()) {
                        int avgStockPrice = Integer.parseInt(ceoMat.group("avgStockPrice"));
                        salary = 5000 * avgStockPrice;
                    } else {
                        salary = 5000;
                    }
                    String lastName = peopleMat.group("lastName");
                    String firstName = peopleMat.group("firstName");
                    System.out.printf("%s: %s: %s%n", lastName, firstName, NumberFormat.getCurrencyInstance().format(salary));
                    yield salary;
                }
                default -> 0;
            };
//            System.out.printf("%s %s %s %s%n", peopleMat.group("firstName"), peopleMat.group("lastName"), peopleMat.group("dob"), peopleMat.group("role"));
        }
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        System.out.printf("The total payout should be %s%n", currencyInstance.format(totalSalaries));
        ;
    }
}

 */