package com.grswebservices.employees;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programmer extends Employee implements IEmployee, Apple, Chef {
    private int linesOfCode = 0;
    private int yearsOfExp = 0;
    private int iq = 0;

    private final String progRegex = "\\w+\\=(?<locpd>\\w+)\\,\\w+\\=(?<yoe>\\w+)\\,\\w+\\=(?<iq>\\w+)";    private final Pattern progPat = Pattern.compile(progRegex);

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public int getIq() {
        return iq;
    }

    public void setIq(int iq) {
        this.iq = iq;
    }

    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public Programmer(String personText) {
        // calls the constructor of the super class
        super(personText);
        Matcher progMat = progPat.matcher(peopleMat.group("details"));
        if (progMat.find()) {
            this.linesOfCode = Integer.parseInt(progMat.group("locpd"));
            this.yearsOfExp = Integer.parseInt(progMat.group("yoe"));
            this.iq = Integer.parseInt(progMat.group("iq"));
        }
    }

    public int getSalary() {
        return 3000 + linesOfCode * yearsOfExp * iq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // delegates up to the super class, so gives us chance to also compare firstName, lastName, dob first, which we want from the super class, and then we also compare the iq
        if (!super.equals(o)) return false;
        Programmer that = (Programmer) o;
        return iq == that.iq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), iq);
    }
}
