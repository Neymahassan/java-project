/* =====================================================================================
 *  JavaCourseDemo.java
 *  CHAPTER 2 : Inheritance, Abstraction, Polymorphism and Exception Handling
 *  CHAPTER 3 : Stacks in Java (java.util.Stack, ArrayStack, LinkedStack)
 *
 *  ALL code examples of both chapters in ONE file, one topic per banner comment.
 *  Every "WILL IT COMPILE?" / vote snippet is kept as a COMMENT so the file always builds.
 *
 *  RUN (single-file mode, JDK 11+):   java JavaCourseDemo.java
 *  RUN (classic):                     javac JavaCourseDemo.java && java JavaCourseDemo
 *
 *  Author : <Your Name> — <Your ID> — <Course / Section>
 *  Ref    : Java Software Structures, 4th Edition, Lewis & Chase (Chapter 3)
 * ===================================================================================== */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCourseDemo {

    /* =================================================================================
     * MAIN — runs every topic in lecture order
     * ================================================================================= */
    public static void main(String[] args) throws Exception {

        banner("CHAPTER 2 — INHERITANCE, ABSTRACTION, POLYMORPHISM, EXCEPTION HANDLING");
        DuplicationProblem.run();          // 1
        ParentClassSolution.run();         // 2
        IsAHasA.run();                     // 3
        PersonFamily.run();                // 4
        VehicleFamily.run();               // 5
        ExtendsRules.run();                // 6
        MultilevelInheritance.run();       // 7
        HierarchicalLab1.run();            // 8  + LAB PART 1
        ProtectedAccess.run();             // 9
        ConstructorChaining.run();         // 10
        SuperFourUses.run();               // 11
        SuperRequired.run();               // 12
        PolymorphismWork.run();            // 13
        WithdrawRules.run();               // 14
        OverridingRules.run();             // 15
        OverrideOrOverload.run();          // 16
        FinalKeyword.run();                // 17
        AbstractGraphics.run();            // 18
        AbstractBankLab2.run();            // 19 + LAB PART 2
        AbstractCompileRules.run();        // 20
        ExceptionFlow.run();               // 21
        CheckedExceptions.run();           // 22
        UncheckedExceptions.run();         // 23
        TryCatchFinallyBlocks.run();       // 24
        MultipleCatchBlocks.run();         // 25
        ThrowKeyword.run();                // 26
        ThrowsKeyword.run();               // 27
        CustomExceptionDemo.run();         // 28
        AtmRecovery.run();                 // 29
        LabPart3Exceptions.run();          // 30 + LAB PART 3

        banner("CHAPTER 3 — STACKS IN JAVA");
        CtrlZWarmup.run();                 // 31
        LifoPopEffect.run();               // 32
        GenericsBox.run();                 // 33
        StackToStringDemo.run();           // 34
        PeekVsPopDemo.run();               // 35
        UndoDepositUtil.run();             // 36
        UndoCourseUtil.run();              // 37
        ReverseStringDemo.run();           // 38
        StackApplications.run();           // 39
        ArrayStackTrace.run();             // 40 (+ ArrayStack + EmptyCollectionException)
        OverflowUnderflow.run();           // 41
        ArrayStackHomework.run();          // 42
        HumanChain.run();                  // 43
        LinkedStackTrace.run();            // 44 (+ Node + LinkedStack)
        InsertAtHeadOrder.run();           // 45
        DeleteAndTraverse.run();           // 46
        UndoDepositLinked.run();           // 47
        UndoCourseLinked.run();            // 48
        StackComparison.run();             // 49

        banner("END OF ALL CHAPTER 2 + CHAPTER 3 EXAMPLES");
    }

    private static void banner(String title) {
        System.out.println("\n" + "=".repeat(78) + "\n" + title + "\n" + "=".repeat(78));
    }

    private static void topic(String title) {
        System.out.println("\n--- " + title + " " + "-".repeat(Math.max(3, 72 - title.length())));
    }

    /* =================================================================================
     * TOPIC 1 — BEFORE CLASS: SPOT THE PROBLEM (code duplication)
     * Two account classes repeat the same fields and the same deposit logic.
     * VOTE: bank rejects deposits <= 0. How many edits? 2 types = 2, 10 types = 10.
     * KEYWORD the AI should give you: extends
     * ================================================================================= */
    static class DuplicationProblem {

        static class SavingsAccount {
            private String accountNumber;
            private double balance;
            SavingsAccount(String accountNumber) { this.accountNumber = accountNumber; }
            public void deposit(double amount) { balance += amount; }
            public double getBalance() { return balance; }
            public String getAccountNumber() { return accountNumber; }
        }

        static class CurrentAccount {                       // same code again = duplication
            private String accountNumber;
            private double balance;
            CurrentAccount(String accountNumber) { this.accountNumber = accountNumber; }
            public void deposit(double amount) { balance += amount; }
            public double getBalance() { return balance; }
            public String getAccountNumber() { return accountNumber; }
        }

        static void run() {
            topic("TOPIC 1: the problem — duplicated code");
            SavingsAccount sav = new SavingsAccount("SAV-1");
            CurrentAccount cur = new CurrentAccount("CUR-1");
            sav.deposit(-50);                               // no rule yet: accepted!
            cur.deposit(-50);
            System.out.println(sav.getAccountNumber() + " = " + sav.getBalance());
            System.out.println(cur.getAccountNumber() + " = " + cur.getBalance());
            System.out.println("Adding the <= 0 rule means editing BOTH classes (DRY violated).");
        }
    }

    /* =================================================================================
     * TOPIC 2 — SOLUTION: CREATE A PARENT CLASS + extends
     * The new deposit rule is written ONCE. Empty subclasses still inherit it.
     * VOTE: SavingsAccount has zero lines inside. Can it call deposit()?  ANSWER: A) Yes
     * Misconception to name: "inherited means copied" — nothing is copied.
     * ================================================================================= */
    static class ParentClassSolution {

        static class BankAccount {                          // superclass / parent / base
            private String accountNumber;
            private double balance;
            BankAccount() { }
            BankAccount(String accountNumber) { this.accountNumber = accountNumber; }
            public void deposit(double amount) {
                if (amount <= 0) return;                    // new rule, written once
                balance += amount;
            }
            public double getBalance() { return balance; }
            public String getAccountNumber() { return accountNumber; }
            @Override public String toString() {
                return getClass().getSimpleName() + "[" + accountNumber + ", balance=" + balance + "]";
            }
        }

        static class SavingsAccount extends BankAccount { }  // subclass / child / derived
        static class CurrentAccount extends BankAccount { }  // empty body is legal

        static void run() {
            topic("TOPIC 2: parent class + extends (empty subclass still works)");
            SavingsAccount sav = new SavingsAccount();
            sav.deposit(-50);
            System.out.println("Empty subclass balance after deposit(-50): " + sav.getBalance());
            sav.deposit(20);
            System.out.println("Empty subclass balance after deposit(20) : " + sav.getBalance());

            SavingsAccount s2 = new SavingsAccount("SAV-2") { };
            CurrentAccount c2 = new CurrentAccount();
            s2.deposit(100);
            c2.deposit(300);
            System.out.println(s2);
            System.out.println(c2);
            System.out.println("Places to edit the <= 0 rule now: ONE, for 2 or 10 account types.");
        }
    }

    /* =================================================================================
     * TOPIC 3 — IS-A vs HAS-A (card sort A–F)
     * IS-A  -> inherit (extends)      HAS-A -> contain (a field / composition)
     * ANSWERS: A IS-A, B HAS-A, C IS-A, D HAS-A (trap), E IS-A, F HAS-A
     * ================================================================================= */
    static class IsAHasA {

        static class Person { String name = "person"; }
        static class Lecturer extends Person { }                  // A: Lecturer IS-A Person
        static class BankAccount { double balance; }
        static class SavingsAccount extends BankAccount { }        // C: Savings IS-A BankAccount
        static class Engine { String start() { return "engine started"; } }
        static class Vehicle { }
        static class Car extends Vehicle {                        // B: Car HAS-A Engine
            Engine engine = new Engine();
        }
        static class ElectricCar extends Car { }                   // E: ElectricCar IS-A Car
        static class Course { String code = "CA314"; }
        static class Student {                                    // D: Student HAS-A Course
            List<Course> courses = new ArrayList<>();
        }
        static class Battery { int percent = 80; }
        static class Phone {                                      // F: Phone HAS-A Battery
            Battery battery = new Battery();
        }

        // WON'T COMPILE / WRONG DESIGN: class Car extends Engine { }  -> a Car is NOT an Engine
        // THE TEST: say "A X is a Y" aloud. If it sounds wrong, Y becomes a FIELD.

        static void run() {
            topic("TOPIC 3: IS-A (extends) vs HAS-A (field)");
            Car car = new Car();
            Student student = new Student();
            student.courses.add(new Course());
            Phone phone = new Phone();
            System.out.println("Car HAS-A Engine  -> " + car.engine.start());
            System.out.println("Student HAS-A " + student.courses.size() + " Course(s)");
            System.out.println("Phone HAS-A Battery at " + phone.battery.percent + "%");
            System.out.println("Lecturer IS-A Person: " + (new Lecturer() instanceof Person));
            System.out.println("A IS-A | B HAS-A | C IS-A | D HAS-A | E IS-A | F HAS-A");
        }
    }

    /* =================================================================================
     * TOPIC 4 — YOUR TURN: Person -> Student, Lecturer
     * Each subclass inherits name and id, adds ONE field, plus a subclass-only method.
     * ================================================================================= */
    static class PersonFamily {

        static class Person {
            private String name;
            private String id;
            Person(String name, String id) { this.name = name; this.id = id; }
            public String getName() { return name; }
            public String getId() { return id; }
            @Override public String toString() {
                return getClass().getSimpleName() + "[" + name + ", " + id + "]";
            }
        }

        static class Student extends Person {
            private double gpa;
            Student(String name, String id, double gpa) {
                super(name, id);
                this.gpa = gpa;
            }
            public double getGpa() { return gpa; }
            public void study(String course) { System.out.println(getName() + " studies " + course); }
        }

        static class Lecturer extends Person {
            private double salary;
            Lecturer(String name, String id, double salary) {
                super(name, id);
                this.salary = salary;
            }
            public double getSalary() { return salary; }
            public void teach(String module) { System.out.println(getName() + " teaches " + module); }
        }

        static void run() {
            topic("TOPIC 4: Person -> Student, Lecturer");
            Person p = new Person("Amina", "P001");
            Student s = new Student("Hodan", "S001", 3.8);
            Lecturer l = new Lecturer("Ali", "L001", 1200);
            System.out.println(p);
            System.out.println(s + " gpa=" + s.getGpa());
            System.out.println(l + " salary=" + l.getSalary());
            s.study("CA314");
            l.teach("CA301");

            Person upcast = s;                       // a Student IS-A Person: OK
            System.out.println("Upcast getName(): " + upcast.getName());
            // upcast.study("CA314");                // WON'T COMPILE: Person has no study()
            // Student bad = new Person("X","P2");   // WON'T COMPILE: a Person is not a Student
            // this.name = "x";  inside Student      // WON'T COMPILE: name is private in Person
        }
    }

    /* =================================================================================
     * TOPIC 5 — BASIC SYNTAX + EVERY CHAIN STARTS AT Object
     * Object -> Vehicle -> Car -> ElectricCar   (no extends clause = extends Object)
     * ================================================================================= */
    static class VehicleFamily {

        static class Vehicle {                       // implicitly extends Object
            private String make;
            private int year;
            Vehicle(String make, int year) { this.make = make; this.year = year; }
            public String getMake() { return make; }
            public int getYear() { return year; }
        }

        static class Car extends Vehicle {
            private int doors;
            Car(String make, int year, int doors) {
                super(make, year);
                this.doors = doors;
            }
            public int getDoors() { return doors; }
            public String describe() { return getYear() + " " + getMake() + ", " + doors + " doors"; }
        }

        static class ElectricCar extends Car {       // real-world extension: adds a battery
            private double batteryKwh;
            ElectricCar(String make, int year, int doors, double batteryKwh) {
                super(make, year, doors);
                this.batteryKwh = batteryKwh;
            }
            @Override public String describe() { return super.describe() + ", " + batteryKwh + " kWh"; }
        }

        static void run() {
            topic("TOPIC 5: Object -> Vehicle -> Car -> ElectricCar");
            Car car = new Car("Toyota", 2020, 4);
            ElectricCar ev = new ElectricCar("BYD", 2026, 4, 60.0);
            System.out.println(car.describe());
            System.out.println(ev.describe());
            System.out.println("ev IS-A Car=" + (ev instanceof Car)
                    + ", IS-A Vehicle=" + (ev instanceof Vehicle)
                    + ", IS-A Object=" + (ev instanceof Object));
            System.out.println("Root of the chain: "
                    + ev.getClass().getSuperclass().getSuperclass().getSuperclass().getName());
        }
    }

    /* =================================================================================
     * TOPIC 6 — THE extends KEYWORD: 4 RULES, 4 VOTES, SINGLE + MULTIPLE INHERITANCE
     * 1) class Manager extends Employee { }                 -> COMPILES
     * 2) class Manager extends Employee, Person { }          -> ERROR: only ONE class
     * 3) class Manager extends Employee extends Person { }   -> ERROR: extends once only
     * 4) class Dog extends Object { }                       -> COMPILES (redundant)
     * Rule 4: to extend a class from another package, the superclass must be public.
     * DIAMOND PROBLEM: AmphibiousCar extends Car, Boat -> which startEngine()? Ambiguous.
     * Java: multiple inheritance of TYPE via interfaces, single of STATE via classes.
     * ================================================================================= */
    static class ExtendsRules {

        static class Employee {                      // SINGLE INHERITANCE: one parent
            private String name;
            private double salary;
            Employee(String name, double salary) { this.name = name; this.salary = salary; }
            public String getName() { return name; }
            public double getSalary() { return salary; }
        }

        static class Manager extends Employee {
            private String department;
            Manager(String name, double salary, String department) {
                super(name, salary);
                this.department = department;
            }
            public String getDepartment() { return department; }
        }

        interface Drivable { default void startEngine() { System.out.println("Car engine"); } }
        interface Floatable { default void startEngine() { System.out.println("Boat engine"); } }

        static class AmphibiousCar implements Drivable, Floatable {
            @Override public void startEngine() {    // clash MUST be resolved explicitly
                Drivable.super.startEngine();
                Floatable.super.startEngine();
            }
        }

        // WON'T COMPILE:
        // public class Manager extends Employee, Person { }
        // public class Manager extends Employee extends Person { }
        // public class AmphibiousCar extends Car, Boat { }

        static void run() {
            topic("TOPIC 6: extends rules, single inheritance, diamond problem");
            Manager m = new Manager("Yusuf", 1500, "Finance");
            System.out.println(m.getName() + " earns " + m.getSalary() + " and runs " + m.getDepartment());
            new AmphibiousCar().startEngine();
            System.out.println("Votes: 1 YES, 2 NO, 3 NO, 4 YES (redundant).");
        }
    }

    /* =================================================================================
     * TOPIC 7 — MULTILEVEL INHERITANCE: Person -> Employee -> Manager
     * Slide typo "extends Persin" corrected to "extends Person". Keep it shallow (3 levels).
     * ================================================================================= */
    static class MultilevelInheritance {

        static class Person {                        // Level 1
            private String name;
            private int age;
            Person(String name, int age) { this.name = name; this.age = age; }
            public String getName() { return name; }
            public int getAge() { return age; }
        }

        static class Employee extends Person {       // Level 2
            private String empId;
            private double salary;
            Employee(String name, int age, String empId, double salary) {
                super(name, age);
                this.empId = empId;
                this.salary = salary;
            }
            public String getEmpId() { return empId; }
            public double getSalary() { return salary; }
        }

        static class Manager extends Employee {      // Level 3
            private int teamSize;
            Manager(String name, int age, String empId, double salary, int teamSize) {
                super(name, age, empId, salary);
                this.teamSize = teamSize;
            }
            public void manageTeam() {
                System.out.println(getName() + " manages a team of " + teamSize);
            }
        }

        static void run() {
            topic("TOPIC 7: multilevel inheritance");
            Manager m = new Manager("Sahra", 34, "E-77", 1800, 6);
            System.out.println("from Person   : " + m.getName() + ", age " + m.getAge());
            System.out.println("from Employee : " + m.getEmpId() + ", salary " + m.getSalary());
            m.manageTeam();
        }
    }

    /* =================================================================================
     * TOPIC 8 — HIERARCHICAL INHERITANCE  +  LAB PART 1
     * One superclass, many subclasses: Savings(minimumBalance), Current(overdraftLimit),
     * Student(studentId). LAB 1: create one of each, deposit, print balances.
     * ================================================================================= */
    static class HierarchicalLab1 {

        static class BankAccount {
            private String accountNumber;
            private double balance;
            BankAccount(String accountNumber) { this.accountNumber = accountNumber; }
            public void deposit(double amount) {
                if (amount <= 0) return;             // rejects <= 0
                balance += amount;
            }
            public double getBalance() { return balance; }
            public String getAccountNumber() { return accountNumber; }
        }

        static class SavingsAccount extends BankAccount {
            private double minimumBalance;
            SavingsAccount(String n, double minimumBalance) { super(n); this.minimumBalance = minimumBalance; }
            public double getMinimumBalance() { return minimumBalance; }
        }

        static class CurrentAccount extends BankAccount {
            private double overdraftLimit;
            CurrentAccount(String n, double overdraftLimit) { super(n); this.overdraftLimit = overdraftLimit; }
            public double getOverdraftLimit() { return overdraftLimit; }
        }

        static class StudentAccount extends BankAccount {
            private String studentId;
            StudentAccount(String n, String studentId) { super(n); this.studentId = studentId; }
            public String getStudentId() { return studentId; }
        }

        static void run() {
            topic("TOPIC 8 + LAB PART 1: hierarchical inheritance");
            SavingsAccount sav = new SavingsAccount("SAV-100", 50);
            CurrentAccount cur = new CurrentAccount("CUR-100", 200);
            StudentAccount stu = new StudentAccount("STU-100", "S001");

            sav.deposit(500);  sav.deposit(-20);     // -20 ignored
            cur.deposit(1000); cur.deposit(0);       // 0 ignored
            stu.deposit(150);

            System.out.printf("%s balance=%.1f min=%.1f%n",
                    sav.getAccountNumber(), sav.getBalance(), sav.getMinimumBalance());
            System.out.printf("%s balance=%.1f overdraft=%.1f%n",
                    cur.getAccountNumber(), cur.getBalance(), cur.getOverdraftLimit());
            System.out.printf("%s balance=%.1f studentId=%s%n",
                    stu.getAccountNumber(), stu.getBalance(), stu.getStudentId());
        }
    }

    /* =================================================================================
     * TOPIC 9 — protected AND DEFAULT (package-private)
     * protected  = same package + subclasses (even in other packages)
     * default    = same package ONLY (a deliberate "package scope")
     * Overriding may WIDEN visibility (default -> public/protected), never narrow it.
     * NOTE: a single file cannot show two packages; the cross-package version is below
     *       in comments. Precision: a package-private method is NOT inherited across
     *       packages, so it cannot be overridden there — it would be a NEW method.
     * ================================================================================= */
    static class ProtectedAccess {

        static class Person {                        // slide package: university
            protected String department;
            String note;                             // package-private field
            protected void announce() { System.out.println("From " + department); }
            void internalLog() { System.out.println("internal log: " + note); }
        }

        static class SamePackageStudent extends Person {
            SamePackageStudent(String department, String note) {
                this.department = department;        // protected: OK
                this.note = note;                    // package-private: OK, same package
            }
            @Override public void internalLog() {    // widening default -> public: legal
                System.out.println("student log: " + note);
            }
        }

        /*  ---- other package version (needs its own file) ----
         *  package other;
         *  import university.Person;
         *  public class OtherPackageStudent extends Person {
         *      public OtherPackageStudent(String d) {
         *          this.department = d;   // OK: protected through inheritance
         *          // this.note = "x";    // WON'T COMPILE: package-private, not visible
         *          // internalLog();      // WON'T COMPILE: not inherited across packages
         *      }
         *  }
         */

        static void run() {
            topic("TOPIC 9: protected vs package-private");
            SamePackageStudent s = new SamePackageStudent("CS", "same package");
            s.announce();
            s.internalLog();
            System.out.println("protected = package + subclasses; default = package only.");
        }
    }

    /* =================================================================================
     * TOPIC 10 — CONSTRUCTOR CHAINING (predict, then vote)
     * new Postgrad() prints? A) Postgrad,Student,Person B) Person,Student,Postgrad C) Postgrad
     * ANSWER: B — CALLS GO UP, BODIES RUN DOWN, all the way to Object().
     * Constructors are NOT members, so they are NOT inherited.
     * ================================================================================= */
    static class ConstructorChaining {

        static class Person {
            Person() { super(); System.out.println("Person"); }
        }
        static class Student extends Person {
            Student() { super(); System.out.println("Student"); }   // Java inserts super()
        }
        static class Postgrad extends Student {
            Postgrad() { super(); System.out.println("Postgrad"); }
        }

        static void run() {
            topic("TOPIC 10: constructor chaining");
            new Postgrad();                          // EXPECTED: Person / Student / Postgrad
            System.out.println("Chain: Object() -> Person() -> Student() -> Postgrad()");
        }
    }

    /* =================================================================================
     * TOPIC 11 — THE super KEYWORD: FOUR USES
     * 1) super()  2) super(args)  3) super.field  4) super.method()
     * super(...) must be the FIRST statement. Static methods are hidden, not overridden,
     * so super.method() applies to instance methods only.
     * ================================================================================= */
    static class SuperFourUses {

        static class Vehicle {
            protected String make;
            Vehicle() { this("unknown"); }
            Vehicle(String make) { this.make = make; }
            public String describe() { return "Vehicle: " + make; }
        }

        static class Car extends Vehicle {
            private int doors;
            Car() {
                super();                             // use 1: no-arg super constructor
                this.doors = 4;
            }
            Car(String make, int doors) {
                super(make);                         // use 2: parameterized, FIRST line
                this.doors = doors;
            }
            String parentMake() { return super.make; }                 // use 3: super field
            @Override public String describe() {                       // use 4: super method
                return super.describe() + " / Car doors=" + doors;
            }
        }

        static void run() {
            topic("TOPIC 11: four uses of super");
            System.out.println(new Car().describe());
            Car toyota = new Car("Toyota", 5);
            System.out.println(toyota.describe());
            System.out.println("super.make = " + toyota.parentMake());
        }
    }

    /* =================================================================================
     * TOPIC 12 — super(...) IS REQUIRED WHEN THE PARENT HAS NO NO-ARG CONSTRUCTOR
     * WILL IT COMPILE?  CurrentAccount(double limit) { overdraftLimit = limit; }
     * ANSWER: NO. Java inserts super(); but BankAccount only has BankAccount(String).
     * IntelliJ: "constructor BankAccount in class BankAccount cannot be applied to
     *            given types; required: String, found: no arguments"
     * FIX: one line -> super("CUR-0000");  (better: take the number as a parameter)
     * ================================================================================= */
    static class SuperRequired {

        static class BankAccount {
            private String accountNumber;
            protected double balance;
            BankAccount(String accountNumber) { this.accountNumber = accountNumber; }
            public void deposit(double a) { if (a > 0) balance += a; }
            public String getAccountNumber() { return accountNumber; }
            public double getBalance() { return balance; }
        }

        static class SavingsAccount extends BankAccount {
            private double minimumBalance;
            SavingsAccount(String n, double min) {
                super(n);                            // must be first
                this.minimumBalance = min;
            }
            public double getMinimumBalance() { return minimumBalance; }
        }

        // WON'T COMPILE (slide version):
        // static class CurrentAccount extends BankAccount {
        //     private double overdraftLimit;
        //     CurrentAccount(double limit) { overdraftLimit = limit; }   // implicit super() fails
        // }

        static class CurrentAccount extends BankAccount {   // FIXED
            private double overdraftLimit;
            CurrentAccount(String n, double limit) {
                super(n);                            // the one missing line
                this.overdraftLimit = limit;
            }
            public double getOverdraftLimit() { return overdraftLimit; }
        }

        static void run() {
            topic("TOPIC 12: missing super(...) compile error + fix");
            SavingsAccount sav = new SavingsAccount("SAV-9", 50);
            CurrentAccount cur = new CurrentAccount("CUR-9", 200);
            sav.deposit(120);
            cur.deposit(80);
            System.out.println(sav.getAccountNumber() + " -> " + sav.getBalance()
                    + " (min " + sav.getMinimumBalance() + ")");
            System.out.println(cur.getAccountNumber() + " -> " + cur.getBalance()
                    + " (overdraft " + cur.getOverdraftLimit() + ")");
        }
    }

    /* =================================================================================
     * TOPIC 13 — POLYMORPHISM "MANY FORMS": METHOD OVERRIDING + SUPERCLASS REFERENCES
     * Same call, different behaviour, chosen at RUNTIME by the object's real type.
     * ================================================================================= */
    static class PolymorphismWork {

        static class Employee {
            public void work() { System.out.println("Employee works"); }
        }

        static class Manager extends Employee {
            @Override public void work() { System.out.println("Manager manages the team"); }
        }

        static void run() {
            topic("TOPIC 13: polymorphism through superclass references");
            Employee e = new Employee();
            Manager m = new Manager();
            e.work();
            m.work();
            Employee ref = new Manager();            // parent variable, child object
            ref.work();
            for (Employee staff : new Employee[] { new Employee(), new Manager(), new Manager() }) {
                staff.work();
            }
        }
    }

    /* =================================================================================
     * TOPIC 14 — EACH ACCOUNT WITHDRAWS ITS OWN WAY (overriding withdraw)
     * THE TEST: every account starts with 200 and runs the SAME call withdraw(300).
     * Savings: refused (keep 50) -> 200 | Current: allowed -> -100 | Student: refused (max 100)
     * ================================================================================= */
    static class WithdrawRules {

        static class BankAccount {
            protected double balance;
            BankAccount(double balance) { this.balance = balance; }
            void withdraw(double a) {                // basic rule
                if (a > balance) refuse("not enough money");
                else balance -= a;
            }
            void refuse(String why) {
                System.out.printf("%-15s refused (%s)%n", getClass().getSimpleName(), why);
            }
            double getBalance() { return balance; }
        }

        static class SavingsAccount extends BankAccount {
            SavingsAccount(double b) { super(b); }
            @Override void withdraw(double a) {
                if (balance - a < 50) refuse("keep 50");
                else balance -= a;
            }
        }

        static class CurrentAccount extends BankAccount {
            CurrentAccount(double b) { super(b); }
            @Override void withdraw(double a) {
                if (balance - a < -200) refuse("overdraft");
                else balance -= a;
            }
        }

        static class StudentAccount extends BankAccount {
            StudentAccount(double b) { super(b); }
            @Override void withdraw(double a) {
                if (a > 100) refuse("max 100");
                else super.withdraw(a);              // augment, don't duplicate
            }
        }

        static void run() {
            topic("TOPIC 14: three withdraw() rules, one call withdraw(300)");
            BankAccount[] accounts = {
                    new SavingsAccount(200), new CurrentAccount(200), new StudentAccount(200)
            };
            for (BankAccount acc : accounts) {
                acc.withdraw(300);
                System.out.printf("%-15s balance %.1f%n",
                        acc.getClass().getSimpleName(), acc.getBalance());
            }
            accounts[2].withdraw(80);                // allowed, uses super.withdraw
            System.out.println("Student after withdraw(80): " + accounts[2].getBalance());
            // EXPECTED: 200.0 / -100.0 / 200.0, then 120.0
        }
    }

    /* =================================================================================
     * TOPIC 15 — RULES OF METHOD OVERRIDING
     * signature identical | return type same or COVARIANT | visibility may WIDEN
     * instance overrides instance, static HIDES static (mixing them = compile error)
     * ================================================================================= */
    static class OverridingRules {

        static class Parent {
            protected Object make() { return new Object(); }
            static String label() { return "Parent.label (static)"; }
        }

        static class Child extends Parent {
            @Override public String make() {        // covariant return + wider visibility
                return "String (covariant return, protected -> public)";
            }
            static String label() { return "Child.label (static HIDING, not overriding)"; }
        }

        // WON'T COMPILE:
        // @Override private Object make() { ... }   // cannot reduce visibility
        // @Override static Object make() { ... }    // cannot switch instance <-> static
        // @Override public void make(int x) { }     // different signature = overload, not override

        static void run() {
            topic("TOPIC 15: overriding rules (covariant, visibility, static hiding)");
            Parent p = new Child();
            System.out.println(p.make());            // runtime dispatch -> Child
            System.out.println(Parent.label() + " | " + Child.label());
            System.out.println("Static calls are bound at COMPILE time (hiding).");
        }
    }

    /* =================================================================================
     * TOPIC 16 — OVERRIDE OR OVERLOAD? (four votes)
     * 1) Savings: @Override void withdraw(double a)   -> OVERRIDE
     * 2) BankAccount: void deposit(double a, String note) -> OVERLOAD (same class)
     * 3) Current: void withdraw(int a)                -> OVERLOAD (trap: int != double)
     * 4) Student: void withdraw(double amount)        -> OVERRIDE (@Override optional)
     * Overriding = runtime, needs inheritance. Overloading = compile time, new API shape.
     * ================================================================================= */
    static class OverrideOrOverload {

        static class BankAccount {
            void withdraw(double a) { System.out.println("BankAccount.withdraw(double " + a + ")"); }
            void deposit(double a) { System.out.println("deposit(" + a + ")"); }
            void deposit(double a, String note) {      // (2) OVERLOAD
                System.out.println("deposit(" + a + ", \"" + note + "\")");
            }
        }
        static class SavingsAccount extends BankAccount {
            @Override void withdraw(double a) { System.out.println("Savings.withdraw(double)"); }
        }
        static class CurrentAccount extends BankAccount {
            void withdraw(int a) {                    // (3) OVERLOAD; @Override would fail
                System.out.println("Current.withdraw(int " + a + ")");
            }
        }
        static class StudentAccount extends BankAccount {
            void withdraw(double amount) {            // (4) OVERRIDE without annotation
                System.out.println("Student.withdraw(double)");
            }
        }

        static void run() {
            topic("TOPIC 16: override vs overload votes");
            new SavingsAccount().withdraw(300);
            BankAccount acc = new BankAccount();
            acc.deposit(50);
            acc.deposit(50, "salary");
            BankAccount current = new CurrentAccount();
            current.withdraw(300);                    // parent's double version still inherited
            new CurrentAccount().withdraw(300);       // int literal -> the new overload
            BankAccount student = new StudentAccount();
            student.withdraw(300);                    // runtime -> Student version
            System.out.println("Answers: 1 OVERRIDE, 2 OVERLOAD, 3 OVERLOAD, 4 OVERRIDE.");
        }
    }

    /* =================================================================================
     * TOPIC 17 — THE final KEYWORD (three jobs)
     * final variable = constant | final method = cannot be overridden | final class = no subclass
     * String is final: that decision underpins its immutability guarantees.
     * ================================================================================= */
    static class FinalKeyword {

        static class Account {
            static final int MAX = 10;                // final variable
            private final String id;
            Account(String id) { this.id = id; }
            public final String id() { return id; }   // final method
        }

        static class SavingsAccount extends Account {
            SavingsAccount(String id) { super(id); }
            // WON'T COMPILE: overridden method is final
            // @Override public String id() { return "hacked"; }
        }

        static final class Money {                    // final class
            private final double amount;
            Money(double amount) { this.amount = amount; }
            double getAmount() { return amount; }
        }

        // WON'T COMPILE: cannot inherit from final Money
        // static class Crypto extends Money { }
        // Account.MAX = 20;                          // cannot assign a value to final variable

        static void run() {
            topic("TOPIC 17: final variable / method / class");
            System.out.println("MAX = " + Account.MAX);
            System.out.println("id() = " + new SavingsAccount("SAV-1").id());
            System.out.println("Money = " + new Money(99.5).getAmount());
        }
    }

    /* =================================================================================
     * TOPIC 18 — ABSTRACT CLASS: GraphicObject + Circle
     * An abstract class is a PARTIAL implementation: fields + concrete + abstract members.
     * A concrete subclass must implement EVERY abstract method or be abstract itself.
     * ================================================================================= */
    static class AbstractGraphics {

        static abstract class GraphicObject {
            int x, y;                                 // concrete fields
            void moveTo(int nx, int ny) { x = nx; y = ny; }   // concrete method
            abstract void draw();                     // no body
            abstract void resize();
        }

        static class Circle extends GraphicObject {
            private int radius;
            Circle(int radius) { this.radius = radius; }
            @Override public void draw() { System.out.println("circle"); }   // visibility widened
            @Override public void resize() { if (radius < 100) radius *= 2; }
            int getRadius() { return radius; }
        }

        static void run() {
            topic("TOPIC 18: abstract GraphicObject / concrete Circle");
            // GraphicObject g = new GraphicObject();  // WON'T COMPILE: abstract; cannot instantiate
            GraphicObject shape = new Circle(40);      // reference type may be abstract
            shape.moveTo(3, 4);
            shape.draw();
            shape.resize();
            System.out.println("position=(" + shape.x + "," + shape.y + ") radius="
                    + ((Circle) shape).getRadius());   // EXPECTED: radius 80
        }
    }

    /* =================================================================================
     * TOPIC 19 — MAKE BankAccount ABSTRACT  +  LAB PART 2
     * Customers never open "just a bank account": every subclass MUST write withdraw().
     * LAB 2: one object of each type in BankAccount variables; withdraw(300) and show().
     * ================================================================================= */
    static class AbstractBankLab2 {

        static abstract class BankAccount {
            private String accountNumber;
            protected double balance;
            BankAccount(String n, double b) { accountNumber = n; balance = b; }
            abstract void withdraw(double a);         // no body: contract for subclasses
            void deposit(double a) { if (a > 0) balance += a; }   // shared
            void show() {
                System.out.printf("%-15s %s balance %.1f%n",
                        getClass().getSimpleName(), accountNumber, balance);
            }
            void refuse(String why) {
                System.out.printf("%-15s refused (%s)%n", getClass().getSimpleName(), why);
            }
        }

        static class SavingsAccount extends BankAccount {
            SavingsAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) {
                if (balance - a < 50) refuse("keep at least 50");
                else balance -= a;
            }
        }

        static class CurrentAccount extends BankAccount {
            CurrentAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) {
                if (balance - a < -200) refuse("overdraft limit -200");
                else balance -= a;
            }
        }

        static class StudentAccount extends BankAccount {
            StudentAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) {
                if (a > 100) refuse("max 100 per withdrawal");
                else if (a > balance) refuse("not enough money");
                else balance -= a;
            }
        }

        static void run() {
            topic("TOPIC 19 + LAB PART 2: abstract BankAccount");
            // new BankAccount("BAS-1", 100);          // error: BankAccount is abstract; cannot be instantiated
            BankAccount[] accounts = {
                    new SavingsAccount("SAV-1", 200),
                    new CurrentAccount("CUR-1", 200),
                    new StudentAccount("STU-1", 200)
            };
            for (BankAccount acc : accounts) { acc.withdraw(300); acc.show(); }
            System.out.println("-- allowed withdrawals --");
            accounts[0].withdraw(100);
            accounts[2].withdraw(100);
            for (BankAccount acc : accounts) acc.show();
        }
    }

    /* =================================================================================
     * TOPIC 20 — ABSTRACT: WILL IT COMPILE? (four votes)
     * 1) abstract class WITH a constructor + super(n,b)  -> YES (common wrong belief!)
     * 2) concrete subclass with NO withdraw()            -> NO: "FixedAccount is not abstract
     *    and does not override abstract method withdraw(double) in BankAccount"
     * 3) abstract void withdraw(double a) { ... }         -> NO: abstract methods cannot have a body
     * 4) BankAccount acc = new CurrentAccount(...)        -> YES: variable abstract, object concrete
     * ================================================================================= */
    static class AbstractCompileRules {

        static abstract class Account {               // (1) abstract class CAN have a constructor
            private final String number;
            protected double balance;
            Account(String number, double balance) { this.number = number; this.balance = balance; }
            abstract void withdraw(double a);
            void show() {
                System.out.printf("%-15s %s balance %.1f%n",
                        getClass().getSimpleName(), number, balance);
            }
        }

        static abstract class FixedAccount extends Account {   // (2) legal ONLY while abstract
            FixedAccount(String n, double b) { super(n, b); }
            // no withdraw(): the contract is passed down
        }

        static class CurrentAccount extends Account {          // concrete: fulfils the contract
            CurrentAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) { balance -= a; }
        }

        // (3) WON'T COMPILE: abstract methods cannot have a body
        // abstract void withdraw(double a) { balance -= a; }

        static void run() {
            topic("TOPIC 20: abstract compile rules");
            Account acc = new CurrentAccount("CUR-1", 200);    // (4) legal
            acc.withdraw(50);
            acc.show();
            System.out.println("Votes: 1 YES, 2 NO, 3 NO, 4 YES.");
        }
    }

    /* =================================================================================
     * TOPIC 21 — EXCEPTIONS: DETECT & THROW -> WALK THE CALL STACK -> HANDLE OR TERMINATE
     * ATM story: withdraw 500 with 200 -> throw -> catch shows message -> back to menu, no crash.
     * ================================================================================= */
    static class ExceptionFlow {

        static void run() {
            topic("TOPIC 21: exception flow");
            System.out.println("1. Program starts");
            try {
                System.out.println("2. Risky operation: 10 / 0");
                int result = 10 / 0;                  // exception object created and thrown here
                System.out.println("never printed " + result);
            } catch (ArithmeticException e) {
                System.out.println("3. Handler ran: " + e.getMessage());
            }
            System.out.println("4. Program continues instead of crashing");
        }
    }

    /* =================================================================================
     * TOPIC 22 — CHECKED EXCEPTIONS: THE CATCH OR SPECIFY REQUIREMENT
     * IOException, SQLException, FileNotFoundException, ClassNotFoundException.
     * Either catch it, or declare it with throws — otherwise it will not compile.
     * ================================================================================= */
    static class CheckedExceptions {

        /** SPECIFY: hand responsibility to the caller. */
        static List<String> readAll(String p) throws IOException {
            return Files.readAllLines(Path.of(p));
        }

        static void run() throws IOException {
            topic("TOPIC 22: checked exceptions (catch or specify)");

            // CATCH — recover by asking again (try-with-resources closes the reader)
            try (FileReader r = new FileReader("data.txt")) {
                System.out.println("opened data.txt, first char = " + r.read());
            } catch (FileNotFoundException e) {
                System.out.println("data.txt missing -> try another");
            }

            // SPECIFY — the caller must catch or declare further
            Path temp = Files.createTempFile("scores", ".txt");
            try {
                Files.writeString(temp, "Hodan 90\nAli 85\n");
                System.out.println("readAll -> " + readAll(temp.toString()));
            } finally {
                Files.deleteIfExists(temp);
            }

            try {
                readAll("no-such-file.txt");
            } catch (IOException e) {
                System.out.println("Caller handled: " + e.getClass().getSimpleName());
            }
        }
    }

    /* =================================================================================
     * TOPIC 23 — UNCHECKED: RuntimeException AND Error
     * Runtime = usually programming bugs (fix the bug, don't catch it).
     * Error = JVM trouble (OutOfMemoryError, StackOverflowError) — don't catch.
     * ================================================================================= */
    static class UncheckedExceptions {

        static int divide(int a, int b) { return a / b; }          // ArithmeticException
        static String third(String[] list) { return list[2]; }      // IndexOutOfBoundsException
        static int len(String s) { return s.length(); }             // NullPointerException

        static void run() {
            topic("TOPIC 23: unchecked exceptions");
            try { System.out.println(divide(10, 0)); }
            catch (ArithmeticException e) { System.out.println("ArithmeticException -> validate b != 0"); }

            try { System.out.println(third(new String[] { "a", "b" })); }
            catch (IndexOutOfBoundsException e) { System.out.println("IndexOutOfBounds -> check length first"); }

            try { System.out.println(len(null)); }
            catch (NullPointerException e) { System.out.println("NullPointer -> check for null before use"); }

            System.out.println("Fixed versions: " + divide(10, 2) + ", " + len("stack"));
        }
    }

    /* =================================================================================
     * TOPIC 24 — try / catch / finally
     * try = risky code (I/O, parsing, methods declared to throw). It MUST be followed by
     * catch or finally. finally runs even on exception and even on return -> cleanup.
     * Never swallow an exception with an empty catch block.
     * ================================================================================= */
    static class TryCatchFinallyBlocks {

        static String finallyOnReturn() {
            try { return "value from try"; }
            finally { System.out.println("finally runs even on return"); }
        }

        static void run() throws IOException {
            topic("TOPIC 24: try / catch / finally");

            // The try block from the slides: Scanner over scores.txt
            Path scores = Files.createTempFile("scores", ".txt");
            Files.writeString(scores, "90\n85\n77\n");
            try (Scanner in = new Scanner(scores.toFile())) {
                int total = 0;
                while (in.hasNextInt()) total += in.nextInt();
                System.out.println("total = " + total);
            }

            try (Scanner missing = new Scanner(new File("scores.txt"))) {
                System.out.println(missing.nextLine());
            } catch (FileNotFoundException e) {
                System.err.println("Caught: scores.txt not found");
            } catch (NumberFormatException e) {
                System.err.println("Not a number");
            } finally {
                Files.deleteIfExists(scores);
                System.out.println("finally: cleanup done");
            }

            // finally from the slides: PrintWriter out.txt (temp file used to keep repo clean)
            Path out = Files.createTempFile("out", ".txt");
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(out.toFile());
                writer.println("hello");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                if (writer != null) writer.close();     // the place for closing files
                Files.deleteIfExists(out);
            }

            System.out.println(finallyOnReturn());
        }
    }

    /* =================================================================================
     * TOPIC 25 — MULTIPLE catch BLOCKS + MULTI-CATCH (Java 7+)
     * Order from SPECIFIC to GENERAL. A catch after its superclass = unreachable = error.
     * ================================================================================= */
    static class MultipleCatchBlocks {

        static String readFile(String name) throws IOException { return Files.readString(Path.of(name)); }
        static void saveRow(String row) throws SQLException {
            if (row.isBlank()) throw new SQLException("empty row");
        }

        // WON'T COMPILE (unreachable catch):
        // try { readFile(n); } catch (IOException e) { } catch (FileNotFoundException e) { }

        static void run() throws IOException {
            topic("TOPIC 25: multiple catch and multi-catch");
            try {
                readFile("missing.txt");
            } catch (FileNotFoundException e) {
                System.out.println("file missing (specific first)");
            } catch (IOException e) {
                System.out.println("io problem (broader after)");
            }

            Path temp = Files.createTempFile("row", ".txt");
            try {
                Files.writeString(temp, "");
                saveRow(readFile(temp.toString()));
            } catch (IOException | SQLException ex) {          // multi-catch
                System.out.println("multi-catch handled: " + ex.getClass().getSimpleName()
                        + " - " + ex.getMessage());
            } finally {
                Files.deleteIfExists(temp);
            }
        }
    }

    /* =================================================================================
     * TOPIC 26 — THE throw KEYWORD (the ACT: one exception, raised now)
     * Only Throwable subtypes may be thrown. EmptyStackException is unchecked -> no throws.
     * ================================================================================= */
    static class ThrowKeyword {

        static class ObjectStack {
            private Object[] items = new Object[10];
            private int size;
            void push(Object o) { items[size++] = o; }
            public Object pop() {
                if (size == 0) {
                    throw new EmptyStackException();     // creating + handing off = throwing
                }
                Object top = items[size - 1];
                items[size - 1] = null;
                size--;
                return top;
            }
        }

        static void run() {
            topic("TOPIC 26: throw");
            ObjectStack stack = new ObjectStack();
            stack.push("A");
            System.out.println("pop -> " + stack.pop());
            try {
                stack.pop();                             // underflow
            } catch (EmptyStackException e) {
                System.out.println("Caught EmptyStackException (unchecked, no throws needed)");
            }
        }
    }

    /* =================================================================================
     * TOPIC 27 — THE throws KEYWORD (the WARNING LABEL on the method)
     * throw  = statement inside the body, one Throwable, jumps to a handler.
     * throws = in the signature, comma-separated list, shifts handling to the caller.
     * ================================================================================= */
    static class ThrowsKeyword {

        static List<String> parseLines(String text) throws ParseException {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            fmt.setLenient(false);
            List<String> lines = new ArrayList<>();
            for (String line : text.split("\\R")) {
                if (line.isBlank()) continue;
                fmt.parse(line.trim());                  // may throw ParseException
                lines.add(line.trim());
            }
            return lines;
        }

        /** Statement of intent: this method may throw these checked exceptions. */
        static List<String> loadFile(String path) throws IOException, ParseException {
            String text = Files.readString(Path.of(path));
            return parseLines(text);
        }

        static void run() throws IOException {
            topic("TOPIC 27: throws");
            Path bad = Files.createTempFile("bad", ".txt");
            Path good = Files.createTempFile("good", ".txt");
            try {
                Files.writeString(bad, "2026-11-13\nnot-a-date\n");
                Files.writeString(good, "2026-11-13\n2026-12-01\n");
                try {
                    loadFile(bad.toString());
                } catch (IOException | ParseException e) {
                    System.out.println("Caller handled " + e.getClass().getSimpleName());
                }
                System.out.println("loadFile(good) -> " + loadFile(good.toString()));
            } finally {
                Files.deleteIfExists(bad);
                Files.deleteIfExists(good);
            }
        }
    }

    /* =================================================================================
     * TOPIC 28 — CUSTOM EXCEPTION TYPES (domain-specific failures)
     * Name ends with Exception. extends Exception = checked, extends RuntimeException = unchecked.
     * Hierarchy: Throwable -> Error (unchecked) / Exception -> RuntimeException (unchecked)
     *            catching a superclass catches all subclasses.
     * ================================================================================= */
    static class InsufficientBalanceException extends Exception {
        private static final long serialVersionUID = 1L;
        private final double balance, requested;
        InsufficientBalanceException(double balance, double requested) {
            super(String.format("Insufficient balance: requested %.2f, available %.2f", requested, balance));
            this.balance = balance;
            this.requested = requested;
        }
        InsufficientBalanceException(String message, double balance, double requested) {
            super(message);
            this.balance = balance;
            this.requested = requested;
        }
        double getBalance() { return balance; }
        double getRequested() { return requested; }
        double getShortfall() { return requested - balance; }
    }

    static class DuplicateException extends Exception {          // checked
        private static final long serialVersionUID = 1L;
        private final String id;
        DuplicateException(String id) {
            super("Duplicate student ID: " + id);
            this.id = id;
        }
        String getId() { return id; }
    }

    static class InvalidGradeException extends RuntimeException { // unchecked
        private static final long serialVersionUID = 1L;
        private final int grade;
        InvalidGradeException(int grade) {
            super("Invalid grade: " + grade + " (expected 0..100)");
            this.grade = grade;
        }
        int getGrade() { return grade; }
    }

    /* =================================================================================
     * TOPIC 28 (demo) — USING THE CUSTOM EXCEPTIONS
     * ================================================================================= */
    static class CustomExceptionDemo {

        static class Registry {
            private final Set<String> ids = new HashSet<>();
            void register(String id) throws DuplicateException {
                if (!ids.add(id)) throw new DuplicateException(id);
                System.out.println("registered " + id);
            }
            void setGrade(int grade) {
                if (grade < 0 || grade > 100) throw new InvalidGradeException(grade);
                System.out.println("grade saved: " + grade);
            }
        }

        static void run() {
            topic("TOPIC 28: custom exceptions");
            Registry reg = new Registry();
            try {
                reg.register("S001");
                reg.register("S001");                    // duplicate
            } catch (DuplicateException e) {
                System.out.println("Caught: " + e.getMessage() + " -> ask for a different ID");
            }
            try {
                reg.setGrade(150);
            } catch (InvalidGradeException e) {
                System.out.println("Caught: " + e.getMessage());
            }
            reg.setGrade(88);
        }
    }

    /* =================================================================================
     * TOPIC 29 — THE SAME FLOW AT AN ATM: no crash, no lost card, balance unchanged
     * ================================================================================= */
    static class AtmRecovery {

        static class Atm {
            private double balance = 200;
            void withdraw(double amount) throws InsufficientBalanceException {
                if (amount > balance) throw new InsufficientBalanceException(balance, amount);
                balance -= amount;
                System.out.printf("Dispensed %.2f, balance %.2f%n", amount, balance);
            }
            double getBalance() { return balance; }
        }

        static void run() {
            topic("TOPIC 29: ATM recovery with try/catch");
            Atm atm = new Atm();
            for (double request : new double[] { 500, 50, 5000, 100 }) {
                try {
                    atm.withdraw(request);
                } catch (InsufficientBalanceException e) {
                    System.out.println("SCREEN: " + e.getMessage());
                    System.out.printf("Shortfall %.2f -> returning to main menu%n", e.getShortfall());
                }
            }
            System.out.println("Final balance (unchanged by failures): " + atm.getBalance());
        }
    }

    /* =================================================================================
     * TOPIC 30 — LAB PART 3 (Week 3): REPLACE PRINTED REFUSALS WITH EXCEPTIONS
     * abstract withdraw(double) throws InsufficientBalanceException; caller reacts.
     * ================================================================================= */
    static class LabPart3Exceptions {

        static abstract class BankAccount {
            private final String accountNumber;
            protected double balance;
            BankAccount(String n, double b) { accountNumber = n; balance = b; }
            abstract void withdraw(double a) throws InsufficientBalanceException;
            void deposit(double a) { if (a > 0) balance += a; }
            void show() {
                System.out.printf("%-15s %s balance %.1f%n",
                        getClass().getSimpleName(), accountNumber, balance);
            }
            protected void checkPositive(double a) {
                if (a <= 0) throw new IllegalArgumentException("amount must be > 0");
            }
        }

        static class SavingsAccount extends BankAccount {
            SavingsAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) throws InsufficientBalanceException {
                checkPositive(a);
                if (balance - a < 50) throw new InsufficientBalanceException(balance, a);
                balance -= a;
            }
        }

        static class CurrentAccount extends BankAccount {
            CurrentAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) throws InsufficientBalanceException {
                checkPositive(a);
                if (balance - a < -200) throw new InsufficientBalanceException(balance, a);
                balance -= a;
            }
        }

        static class StudentAccount extends BankAccount {
            StudentAccount(String n, double b) { super(n, b); }
            @Override void withdraw(double a) throws InsufficientBalanceException {
                checkPositive(a);
                if (a > 100) throw new InsufficientBalanceException(
                        "Student accounts allow at most 100.00 per withdrawal", balance, a);
                if (a > balance) throw new InsufficientBalanceException(balance, a);
                balance -= a;
            }
        }

        static void run() {
            topic("TOPIC 30 + LAB PART 3: exceptions instead of printed refusals");
            BankAccount[] accounts = {
                    new SavingsAccount("SAV-3", 200),
                    new CurrentAccount("CUR-3", 200),
                    new StudentAccount("STU-3", 200)
            };
            for (BankAccount acc : accounts) {
                try {
                    acc.withdraw(300);
                    System.out.println(acc.getClass().getSimpleName() + " withdrew 300");
                } catch (InsufficientBalanceException e) {
                    System.out.println(acc.getClass().getSimpleName() + " -> " + e.getMessage());
                }
                acc.show();
            }
        }
    }

    /* =================================================================================
     * TOPIC 31 — CHAPTER 3 WARM-UP: Ctrl + Z
     * Type Welcome, to, JUST then press Ctrl+Z twice -> "Welcome" remains.
     * JUST disappeared first because it was typed LAST (LIFO).
     * ================================================================================= */
    static class CtrlZWarmup {

        static void run() {
            topic("TOPIC 31: Ctrl+Z is a stack");
            Stack<String> typed = new Stack<>();
            typed.push("Welcome");
            typed.push("to");
            typed.push("JUST");
            System.out.println("screen: " + String.join(" ", typed));
            System.out.println("Ctrl+Z removes: " + typed.pop());
            System.out.println("Ctrl+Z removes: " + typed.pop());
            System.out.println("screen now: " + String.join(" ", typed));
        }
    }

    /* =================================================================================
     * TOPIC 32 — DEFINITION, LIFO, POP EFFECT + THE FIVE STACK OPERATIONS
     * push = add at top | pop = remove top | peek = examine top | isEmpty | size
     * VOTE: push(10) push(20) push(30) pop() removes?  ANSWER: C) 30
     * ================================================================================= */
    static class LifoPopEffect {

        static void run() {
            topic("TOPIC 32: LIFO + pop effect");
            Stack<Integer> s = new Stack<>();
            s.push(10);
            s.push(20);
            s.push(30);
            System.out.println("stack (bottom->top): " + s);
            System.out.println("pop() removes: " + s.pop());     // 30, not 10
            System.out.println("size=" + s.size() + ", isEmpty=" + s.isEmpty() + ", peek=" + s.peek());
        }
    }

    /* =================================================================================
     * TOPIC 33 — GENERICS: class Box<T>, Box<Integer>, Box<String>
     * Benefits: code reusability, type safety, less repetition, easy maintenance.
     * T (Type) / E (Element) are conventions; any identifier works.
     * ================================================================================= */
    static class GenericsBox {

        static class Box<T> {
            private T item;
            void set(T item) { this.item = item; }
            T get() { return item; }
            boolean isEmpty() { return item == null; }
        }

        static void run() {
            topic("TOPIC 33: generics");
            int age = 25; String name = "Ali"; double price = 15.5;   // specific types
            System.out.println(age + " / " + name + " / " + price);

            Box<Integer> box1 = new Box<>();
            Box<String> box2 = new Box<>();
            box1.set(age);
            box2.set(name);
            int unboxed = box1.get();                    // no cast needed: assumed Integer
            System.out.println("box1=" + unboxed + ", box2=" + box2.get());
            // box1.set("hello");                        // WON'T COMPILE: wrong type, caught early
            System.out.println("One reusable class, compile-time type safety.");
        }
    }

    /* =================================================================================
     * TOPIC 34 — java.util.Stack EXAMPLE 1: PREDICT THE OUTPUT OF toString()
     * VOTE: A [10,20,30]  B [30,20,10]  C 30  D [30]      ANSWER: A
     * toString() prints BOTTOM to TOP: the top is the RIGHT-MOST value. Printing removes nothing.
     * ================================================================================= */
    static class StackToStringDemo {

        static void run() {
            topic("TOPIC 34: toString() prints bottom -> top");
            Stack<Integer> s1 = new Stack<>();
            s1.push(10);
            s1.push(20);
            s1.push(30);
            System.out.println(s1.toString());           // EXPECTED: [10, 20, 30]
            System.out.println("top = 30 (right-most), size still " + s1.size());
        }
    }

    /* =================================================================================
     * TOPIC 35 — EXAMPLE 2: PREDICT ALL FIVE LINES (peek LOOKS, pop TAKES)
     * EXPECTED:
     *   1. Original Stack: [10, 20, 30]
     *   2. top element: 30
     *   3. Stack after accessing top element: [10, 20, 30]   <- the trap: peek removed nothing
     *   4. Removed element: 30
     *   5. Stack after Pop Operation: [10, 20]
     * ================================================================================= */
    static class PeekVsPopDemo {

        static void run() {
            topic("TOPIC 35: peek() looks, pop() takes");
            Stack<Integer> s = new Stack<>();
            s.push(10); s.push(20); s.push(30);
            System.out.println("1. Original Stack: " + s);
            System.out.println("2. top element: " + s.peek());
            System.out.println("3. Stack after accessing top element: " + s);
            System.out.println("4. Removed element: " + s.pop());
            System.out.println("5. Stack after Pop Operation: " + s);
        }
    }

    /* =================================================================================
     * TOPIC 36 — APPLICATION: UNDO THE LAST DEPOSIT (UndoDepositDemo.java)
     * EXPECTED: Deposit 50->150, 20->170, 30->200, Undo 30->170, Undo 20->150
     * ================================================================================= */
    static class UndoDepositUtil {

        static class BankAccount {
            private double balance;
            private Stack<Double> history = new Stack<>();
            BankAccount(double balance) { this.balance = balance; }
            public void deposit(double amount) {
                balance = balance + amount;
                history.push(amount);                    // remember it
                System.out.println("Deposit " + amount + " -> balance " + balance);
            }
            public void undoLastDeposit() {
                if (history.isEmpty()) { System.out.println("Nothing to undo"); return; }
                double last = history.pop();             // most recent deposit
                balance = balance - last;
                System.out.println("Undo " + last + " -> balance " + balance);
            }
        }

        static void run() {
            topic("TOPIC 36: undo last deposit (java.util.Stack)");
            BankAccount acc = new BankAccount(100);
            acc.deposit(50);
            acc.deposit(20);
            acc.deposit(30);
            acc.undoLastDeposit();
            acc.undoLastDeposit();
        }
    }

    /* =================================================================================
     * TOPIC 37 — STUDENT EXAMPLE: UNDO THE LAST COURSE (UndoCourseDemo.java)
     * JUST portal registration. Printed list is bottom->top: newest course on the right.
     * ================================================================================= */
    static class UndoCourseUtil {

        static class Registration {
            private String studentName;
            private Stack<String> courses = new Stack<>();
            Registration(String studentName) { this.studentName = studentName; }
            String getStudentName() { return studentName; }
            public void addCourse(String code) {
                courses.push(code);
                System.out.println("Added " + code + " -> " + courses);
            }
            public void undoLastCourse() {
                if (courses.isEmpty()) { System.out.println("Nothing to undo"); return; }
                String removed = courses.pop();
                System.out.println("Removed " + removed + " -> " + courses);
            }
        }

        static void run() {
            topic("TOPIC 37: undo last course (java.util.Stack)");
            Registration reg = new Registration("Hodan");
            System.out.println("Student: " + reg.getStudentName());
            reg.addCourse("CA314");
            reg.addCourse("CA301");
            reg.addCourse("MA210");
            reg.undoLastCourse();
        }
    }

    /* =================================================================================
     * TOPIC 38 — APPLICATION: REVERSING A STRING
     * Push every character, then pop them all: ABCDE -> EDCBA, JAVA -> AVAJ
     * ================================================================================= */
    static class ReverseStringDemo {

        static String reverse(String input) {
            Stack<Character> stack = new Stack<>();
            for (char c : input.toCharArray()) stack.push(c);
            StringBuilder out = new StringBuilder(input.length());
            while (!stack.isEmpty()) out.append(stack.pop());
            return out.toString();
        }

        static void run() {
            topic("TOPIC 38: reversing a string with a stack");
            System.out.println("ABCDE -> " + reverse("ABCDE"));
            System.out.println("JAVA  -> " + reverse("JAVA"));
            Stack<Character> trace = new Stack<>();
            for (char c : "JAVA".toCharArray()) trace.push(c);
            System.out.print("pop order: ");
            while (!trace.isEmpty()) System.out.print(trace.pop() + " ");
            System.out.println();
        }
    }

    /* =================================================================================
     * TOPIC 39 — REAL-WORLD STACKS + "SPOT THE STACK" PREDICTIONS
     * HTML tags: <html><head><title> open -> </title> must close next.
     * Browser history: Google, JUST, Gmail, GitHub + Back twice -> JUST website.
     * Method calls: main->one->two->three: three() is on top and finishes FIRST.
     * Compiler syntax checking of ( [ { } ] ).
     * ================================================================================= */
    static class StackApplications {

        /* ---- 1. Browser history (navigation) ---- */
        static class Browser {
            private final LinkedStack<String> back = new LinkedStack<>();
            private String current;
            void visit(String url) {
                if (current != null) back.push(current);
                current = url;
                System.out.println("visit  -> " + current);
            }
            void pressBack() {
                if (back.isEmpty()) { System.out.println("no history"); return; }
                current = back.pop();
                System.out.println("Back   -> " + current);
            }
        }

        /* ---- 2. Function-call tracing ---- */
        private static int depth = 0;
        private static void enter(String m) { System.out.println("  ".repeat(depth++) + "push " + m); }
        private static void exit(String m)  { System.out.println("  ".repeat(--depth) + "pop  " + m); }
        static void three() { enter("three()"); exit("three()"); }
        static void two()   { enter("two()"); three(); exit("two()"); }
        static void one()   { enter("one()"); two(); exit("one()"); }

        /* ---- 3. Compiler syntax checking: brackets ---- */
        static boolean bracketsBalanced(String code) {
            LinkedStack<Character> open = new LinkedStack<>();
            for (char c : code.toCharArray()) {
                if (c == '(' || c == '[' || c == '{') open.push(c);
                else if (c == ')' || c == ']' || c == '}') {
                    if (open.isEmpty()) return false;
                    char top = open.pop();
                    if ((c == ')' && top != '(') || (c == ']' && top != '[') || (c == '}' && top != '{'))
                        return false;
                }
            }
            return open.isEmpty();
        }

        /* ---- 4. HTML tag matching (simple tags, no attributes) ---- */
        private static final Pattern TAG = Pattern.compile("<([^>]+)>");
        private static final Set<String> VOID_TAGS = Set.of("br", "img", "hr", "meta", "link", "input");
        static boolean htmlBalanced(String html) {
            LinkedStack<String> open = new LinkedStack<>();
            Matcher m = TAG.matcher(html);
            while (m.find()) {
                String tag = m.group(1).trim();
                if (tag.startsWith("!") || tag.startsWith("?")) continue;
                if (tag.startsWith("/")) {
                    String name = tag.substring(1).trim().toLowerCase();
                    if (open.isEmpty() || !open.pop().equals(name)) return false;
                } else if (!tag.endsWith("/") && !VOID_TAGS.contains(tag.toLowerCase())) {
                    open.push(tag.toLowerCase());
                }
            }
            return open.isEmpty();
        }

        static void run() {
            topic("TOPIC 39: real-world stacks");
            Browser browser = new Browser();
            browser.visit("Google");
            browser.visit("JUST website");
            browser.visit("Gmail");
            browser.visit("GitHub");
            browser.pressBack();
            browser.pressBack();                          // ANSWER: JUST website

            System.out.println("call stack:");
            enter("main()"); one(); exit("main()");

            System.out.println("braces balanced  {a[b(c)]} : " + bracketsBalanced("{a[b(c)]}"));
            System.out.println("braces balanced  {a[b(c)]  : " + bracketsBalanced("{a[b(c)]"));
            System.out.println("html balanced <html><head><title>x</title></head></html> : "
                    + htmlBalanced("<html><head><title>x</title></head></html>"));
            System.out.println("html balanced <html><head><title>x</head></title></html> : "
                    + htmlBalanced("<html><head><title>x</head></title></html>"));
            System.out.println("After <html><head><title>, the next closing tag must be </title>.");
        }
    }

    /* =================================================================================
     * TOPIC 40 — IMPLEMENTING A STACK WITH AN ARRAY: ArrayStack<T>
     * + EmptyCollectionException + StackADT (the interface = Chapter 2 abstraction reused)
     *
     * Assumptions: array of object references, bottom fixed at index 0,
     * top = index of the next free slot = number of elements.
     * Java cannot create new T[]; create new Object[] and cast to T[].
     * Full array? three options: throw / return status / RESIZE (we double the capacity).
     * ================================================================================= */

    /** Thrown when pop or peek is attempted on an empty collection (unchecked). */
    static class EmptyCollectionException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public EmptyCollectionException(String collection) {
            super("The " + collection + " is empty.");
        }
    }

    /** The stack contract: push, pop, peek, isEmpty, size (+ toString). */
    interface StackADT<T> {
        void push(T element);
        T pop();
        T peek();
        boolean isEmpty();
        int size();
        String toString();
    }

    static class ArrayStack<T> implements StackADT<T> {

        private static final int DEFAULT_CAPACITY = 100;
        private int top;                 // next free index == element count
        private T[] stack;

        @SuppressWarnings("unchecked")
        ArrayStack() { this(DEFAULT_CAPACITY); }

        @SuppressWarnings("unchecked")
        ArrayStack(int initialCapacity) {
            if (initialCapacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
            top = 0;
            stack = (T[]) new Object[initialCapacity];   // generic array creation trick
        }

        @Override public void push(T element) {
            if (size() == stack.length) expandCapacity();
            stack[top] = element;
            top++;
        }

        /** New array with twice the capacity, elements copied over. */
        private void expandCapacity() { stack = Arrays.copyOf(stack, stack.length * 2); }

        @Override public T pop() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            top--;
            T result = stack[top];
            stack[top] = null;                          // avoid a lingering reference
            return result;
        }

        @Override public T peek() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            return stack[top - 1];
        }

        @Override public boolean isEmpty() { return top == 0; }
        @Override public int size() { return top; }
        int capacity() { return stack.length; }          // teaching aid for the trace demo

        @Override public String toString() {             // bottom -> top
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < top; i++) {
                if (i > 0) sb.append(", ");
                sb.append(stack[i]);
            }
            return sb.append(']').toString();
        }
    }

    /* =================================================================================
     * TOPIC 40 (demo) — ArrayStackTraceDemo: THE ARRAY GREW, THEN RAN OUT
     * Slide sets DEFAULT_CAPACITY = 2; here we pass 2 explicitly (same output).
     * EXPECTED:
     *   size 2, capacity 2
     *   size 3, capacity 4
     *   peek: C
     *   pop:  C / pop:  B / pop:  A
     *   4th pop -> EmptyCollectionException: The stack is empty.   (UNDERFLOW)
     * Overflow never happens here: expandCapacity() doubles the array instead.
     * ================================================================================= */
    static class ArrayStackTrace {

        static void run() {
            topic("TOPIC 40: ArrayStack trace (grow, then underflow)");
            ArrayStack<String> s = new ArrayStack<>(2);
            s.push("A");
            s.push("B");
            System.out.println("size " + s.size() + ", capacity " + s.capacity());
            s.push("C");                                 // triggers expandCapacity()
            System.out.println("size " + s.size() + ", capacity " + s.capacity());
            System.out.println("peek: " + s.peek());
            System.out.println("pop:  " + s.pop());
            System.out.println("pop:  " + s.pop());
            System.out.println("pop:  " + s.pop());
            try {
                System.out.println("pop:  " + s.pop());  // 4th pop on an empty stack
            } catch (EmptyCollectionException e) {
                System.out.println("Exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println("(uncaught, the slide shows: Exception in thread \"main\" ... )");
        }
    }

    /* =================================================================================
     * TOPIC 41 — STACK OVERFLOW vs STACK UNDERFLOW (fixed-capacity version)
     * underflow = pop/peek on empty | overflow = push when the array is full
     * ================================================================================= */
    static class FixedCapacityStack<T> {
        private final Object[] items;
        private int size;
        FixedCapacityStack(int capacity) { items = new Object[capacity]; }
        void push(T element) {
            if (size == items.length)
                throw new IllegalStateException("Stack overflow: capacity " + items.length);
            items[size++] = element;
        }
        @SuppressWarnings("unchecked")
        T pop() {
            if (size == 0) throw new EmptyCollectionException("stack");
            T result = (T) items[--size];
            items[size] = null;
            return result;
        }
        int size() { return size; }
    }

    static class OverflowUnderflow {

        static void run() {
            topic("TOPIC 41: overflow and underflow");
            FixedCapacityStack<Integer> fixed = new FixedCapacityStack<>(3);
            fixed.push(1); fixed.push(2); fixed.push(3);
            try {
                fixed.push(4);                           // OVERFLOW
            } catch (IllegalStateException e) {
                System.out.println("push -> " + e.getMessage());
            }
            while (fixed.size() > 0) System.out.print(fixed.pop() + " ");
            System.out.println();
            try {
                fixed.pop();                             // UNDERFLOW
            } catch (EmptyCollectionException e) {
                System.out.println("pop  -> " + e.getMessage());
            }
        }
    }

    /* =================================================================================
     * TOPIC 42 — HOMEWORK: ARRAY STACK, A DIFFERENT APPROACH
     * Convention A (above): top = next free slot, empty when top == 0, size = top.
     * Convention B (here) : top = INDEX of the top element, empty when top == -1,
     *                       size = top + 1, push writes stack[++top].
     * ================================================================================= */
    static class ArrayStackAlt<T> implements StackADT<T> {

        private T[] stack;
        private int top;                                 // index of the top element

        @SuppressWarnings("unchecked")
        ArrayStackAlt(int capacity) {
            stack = (T[]) new Object[Math.max(1, capacity)];
            top = -1;
        }

        @Override public void push(T element) {
            if (top + 1 == stack.length) stack = Arrays.copyOf(stack, stack.length * 2);
            stack[++top] = element;
        }

        @Override public T pop() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            T result = stack[top];
            stack[top--] = null;
            return result;
        }

        @Override public T peek() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            return stack[top];
        }

        @Override public boolean isEmpty() { return top == -1; }
        @Override public int size() { return top + 1; }
        int capacity() { return stack.length; }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i <= top; i++) { if (i > 0) sb.append(", "); sb.append(stack[i]); }
            return sb.append(']').toString();
        }
    }

    static class ArrayStackHomework {

        static void run() {
            topic("TOPIC 42: homework — the other top convention");
            ArrayStackAlt<String> alt = new ArrayStackAlt<>(2);
            System.out.println("empty? " + alt.isEmpty() + " (top == -1)");
            alt.push("A"); alt.push("B");
            System.out.println(alt + " size=" + alt.size() + " capacity=" + alt.capacity());
            alt.push("C");
            System.out.println(alt + " size=" + alt.size() + " capacity=" + alt.capacity());
            System.out.println("pop " + alt.pop() + " -> " + alt);
        }
    }

    /* =================================================================================
     * TOPIC 43 — BEFORE WE START: A HUMAN CHAIN (linked list, no array)
     * Amina->Hodan->Ali->Yusuf->null
     * 1) Hodan leaves: ONLY Amina changes her card (Hodan -> Ali).
     * 2) Sahra joins at the front: only Sahra writes a card (Amina); nobody moves.
     * In an array, inserting at index 0 means shifting everyone.
     * ================================================================================= */
    static class HumanChain {

        static void run() {
            topic("TOPIC 43: human chain");
            Node<String> amina = new Node<>("Amina");
            Node<String> hodan = new Node<>("Hodan");
            Node<String> ali = new Node<>("Ali");
            Node<String> yusuf = new Node<>("Yusuf");
            amina.setNext(hodan); hodan.setNext(ali); ali.setNext(yusuf);
            Node<String> head = amina;
            System.out.println("start        : " + chain(head));

            // 1) Hodan leaves: the predecessor's card is rewritten
            for (Node<String> cur = head; cur.getNext() != null; cur = cur.getNext()) {
                if ("Hodan".equals(cur.getNext().getElement())) {
                    System.out.println("card changed : " + cur.getElement()
                            + " now points to " + cur.getNext().getNext().getElement());
                    cur.setNext(cur.getNext().getNext());
                    break;
                }
            }
            System.out.println("after removal: " + chain(head));

            // 2) Sahra joins at the front
            Node<String> sahra = new Node<>("Sahra");
            sahra.setNext(head);
            head = sahra;
            System.out.println("after join   : " + chain(head));
            System.out.println("Only ONE card changed each time; nobody moved.");
        }
    }

    /* =================================================================================
     * TOPIC 44 — IMPLEMENTING A STACK WITH LINKS: Node<T> + LinkedStack<T>
     * Why linked? arrays are fixed size, may waste space, need contiguous memory.
     * A linked structure is dynamic: it grows and shrinks as needed.
     * All stack activity is at ONE end, so the FRONT of the chain is the TOP.
     * push: temp.setNext(top) THEN top = temp.   pop: read, top = top.getNext(), count--.
     * Our LinkedStack deliberately has NO toString() (as in the slides).
     * ================================================================================= */
    static class Node<T> {
        private T element;
        private Node<T> next;
        Node() { this(null, null); }
        Node(T element) { this(element, null); }
        Node(T element, Node<T> next) { this.element = element; this.next = next; }
        T getElement() { return element; }
        void setElement(T element) { this.element = element; }
        Node<T> getNext() { return next; }
        void setNext(Node<T> next) { this.next = next; }
    }

    static class LinkedStack<T> implements StackADT<T> {
        private int count;
        private Node<T> top;
        LinkedStack() { count = 0; top = null; }

        @Override public void push(T element) {
            Node<T> temp = new Node<>(element);
            temp.setNext(top);                           // 1. link FIRST
            top = temp;                                  // 2. then move top
            count++;
        }

        @Override public T pop() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            T result = top.getElement();
            top = top.getNext();                         // old top becomes garbage
            count--;
            return result;
        }

        @Override public T peek() {
            if (isEmpty()) throw new EmptyCollectionException("stack");
            return top.getElement();
        }

        @Override public boolean isEmpty() { return count == 0; }
        @Override public int size() { return count; }
        // no toString(): the demos print peek() and size() instead
    }

    /** Safe chain printer: stops if a cycle was created by a link-order bug. */
    private static <T> String chain(Node<T> head) {
        StringBuilder sb = new StringBuilder();
        int guard = 0;
        for (Node<T> cur = head; cur != null; cur = cur.getNext()) {
            sb.append(cur.getElement());
            if (++guard >= 6) return sb.append(" -> ... (CYCLE detected)").toString();
            sb.append(" -> ");
        }
        return sb.append("null").toString();
    }

    /* =================================================================================
     * TOPIC 44 (demo) — LinkedStackTraceDemo: THE NEWEST NODE IS AT THE FRONT
     * EXPECTED: size: 3 / peek: C / pop:  C / size: 2 / peek: B
     * top -> C -> B -> A -> null   (after pop, C has no reference: garbage collected)
     * ================================================================================= */
    static class LinkedStackTrace {

        static void run() {
            topic("TOPIC 44: LinkedStack trace");
            LinkedStack<String> s = new LinkedStack<>();
            s.push("A");
            s.push("B");
            s.push("C");
            System.out.println("size: " + s.size());
            System.out.println("peek: " + s.peek());
            System.out.println("pop:  " + s.pop());
            System.out.println("size: " + s.size());
            System.out.println("peek: " + s.peek());
            System.out.println("chain: top -> C -> B -> A -> null (before pop)");
        }
    }

    /* =================================================================================
     * TOPIC 45 — INSERT AT THE HEAD: WHAT IF WE SWAP THE STEPS? (vote)
     * correct: newNode.setNext(head); head = newNode;
     * swapped: head = newNode; newNode.setNext(head);
     * List was B -> null, insert A. ANSWER: B) A points to itself; B is lost.
     * Output: A -> A -> A -> ... (silent bug: no exception, no compile error)
     * RULE: link the new node FIRST, then move head — same rule as push().
     * ================================================================================= */
    static class InsertAtHeadOrder {

        static void run() {
            topic("TOPIC 45: insert at head, correct vs swapped order");

            Node<String> head = new Node<>("B");         // B -> null
            Node<String> newNode = new Node<>("A");
            newNode.setNext(head);                       // 1. link
            head = newNode;                              // 2. move head
            System.out.println("correct order : " + chain(head));

            Node<String> head2 = new Node<>("B");
            Node<String> newNode2 = new Node<>("A");
            head2 = newNode2;                            // 2. move head FIRST (bug)
            newNode2.setNext(head2);                     // 1. link -> points to itself
            System.out.println("swapped order : " + chain(head2));
            System.out.println("B has no reference left -> garbage collected. Answer: B.");
        }
    }

    /* =================================================================================
     * TOPIC 46 — DELETE AT THE HEAD + TRAVERSING A LINKED LIST
     * Delete: keep a reference if needed, then head = head.getNext(); old node is collected.
     * Traversal: no indexes, follow the references (slide's current.getElement fixed to ()).
     * ================================================================================= */
    static class DeleteAndTraverse {

        static void run() {
            topic("TOPIC 46: delete at head + traversal");
            Node<String> head = new Node<>("A", new Node<>("B", new Node<>("C", new Node<>("D"))));
            System.out.println("list: " + chain(head));

            System.out.print("traversal: ");
            Node<String> current = head;
            while (current != null) {
                System.out.print(current.getElement() + " ");
                current = current.getNext();
            }
            System.out.println();

            Node<String> removed = head;                 // keep it if needed elsewhere
            head = head.getNext();                       // reassign the front reference
            System.out.println("deleted " + removed.getElement() + " -> " + chain(head));
            System.out.println("insert at front and delete at front need NO shifting.");
        }
    }

    /* =================================================================================
     * TOPIC 47 — BANK EXAMPLE: SAME PROGRAM, NEW STACK (UndoDepositLinkedDemo.java)
     * Only change: LinkedStack instead of Stack. Output identical.
     * The class uses only push, pop, isEmpty -> it does not care how the stack is stored.
     * ================================================================================= */
    static class UndoDepositLinked {

        static class BankAccount {
            private double balance;
            private LinkedStack<Double> history = new LinkedStack<>();   // the only change
            BankAccount(double balance) { this.balance = balance; }
            public void deposit(double amount) {
                balance = balance + amount;
                history.push(amount);
                System.out.println("Deposit " + amount + " -> balance " + balance);
            }
            public void undoLastDeposit() {
                if (history.isEmpty()) { System.out.println("Nothing to undo"); return; }
                double last = history.pop();
                balance = balance - last;
                System.out.println("Undo " + last + " -> balance " + balance);
            }
        }

        static void run() {
            topic("TOPIC 47: undo deposit with LinkedStack");
            BankAccount acc = new BankAccount(100);
            acc.deposit(50);
            acc.deposit(20);
            acc.deposit(30);
            acc.undoLastDeposit();
            acc.undoLastDeposit();
        }
    }

    /* =================================================================================
     * TOPIC 48 — STUDENT EXAMPLE: UNDO WITH LinkedStack (UndoCourseLinkedDemo.java)
     * No toString(), so we print peek() and size().
     * Extension: 3rd undo -> Removed CA314 | size: 0 ; 4th undo -> Nothing to undo
     * ================================================================================= */
    static class UndoCourseLinked {

        static class Registration {
            private LinkedStack<String> courses = new LinkedStack<>();
            public void addCourse(String code) {
                courses.push(code);
                System.out.println("Added " + code + " | top: " + courses.peek()
                        + " | size: " + courses.size());
            }
            public void undoLastCourse() {
                if (courses.isEmpty()) { System.out.println("Nothing to undo"); return; }
                String removed = courses.pop();
                System.out.println("Removed " + removed + " | size: " + courses.size());
            }
        }

        static void run() {
            topic("TOPIC 48: undo course with LinkedStack");
            Registration reg = new Registration();
            reg.addCourse("CA314");
            reg.addCourse("CA301");
            reg.addCourse("MA210");
            reg.undoLastCourse();
            reg.undoLastCourse();
            reg.undoLastCourse();                        // extension: Removed CA314 | size: 0
            reg.undoLastCourse();                        // extension: Nothing to undo
        }
    }

    /* =================================================================================
     * TOPIC 49 — COMPARISON, WHEN TO USE EACH, AND PROGRAMMING TO THE INTERFACE
     *
     * Feature            | ArrayStack                  | LinkedStack
     * -------------------|-----------------------------|--------------------------------
     * Data structure     | Array                       | Singly linked list
     * Storage            | Indexed array positions     | Individual nodes
     * Capacity           | Fixed (we resize by doubling)| Dynamic
     * Top representation | Integer index               | Node reference
     * Memory allocation  | Array allocated in advance  | Nodes allocated as needed
     * Overflow           | When the array is full      | Only when memory runs out
     * Memory overhead    | Unused array positions      | Extra reference + object/node
     * Implementation     | Simpler                     | Slightly more complex
     *
     * WHICH WOULD YOU CHOOSE? (the reason matters more than the choice)
     *  - Calculator undo, max 10 steps -> ArrayStack: known maximum, simplest.
     *  - Browser history, 3 or 3000 pages -> LinkedStack: size unknown, grows as needed.
     *  - ATM session, max 5 transactions -> either: ArrayStack(5) is simplest.
     * ================================================================================= */
    static class StackComparison {

        /** Same code works with BOTH implementations: program to StackADT, not to a class. */
        static void fill(StackADT<Double> stack) { stack.push(50.0); stack.push(20.0); stack.push(30.0); }

        static double undoAll(StackADT<Double> history, double balance) {
            while (!history.isEmpty()) balance -= history.pop();
            return balance;
        }

        static void run() {
            topic("TOPIC 49: ArrayStack vs LinkedStack (one API, two implementations)");
            StackADT<Double> array = new ArrayStack<>(2);
            StackADT<Double> linked = new LinkedStack<>();
            fill(array);
            fill(linked);
            System.out.println("ArrayStack  undo-all -> balance " + undoAll(array, 200));
            System.out.println("LinkedStack undo-all -> balance " + undoAll(linked, 200));
            System.out.println("Calculator=ArrayStack | Browser=LinkedStack | ATM=either (justify it).");
        }
    }
}