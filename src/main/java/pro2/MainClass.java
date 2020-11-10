package pro2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro2.model.People;
import pro2.model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainClass {
    private People people = new People();

    public MainClass() {
        Person p1 = new Person("Jan", "Kočí");
        Person p2 = new Person("Alena", "Rychlá");
        p1.setP2(p2);
        p2.setP2(p1);
        people.getList().add(p1);
        people.getList().add(p2);
    }

    public void saveJson() {
        ObjectMapper m = new ObjectMapper();
        try {
            String s = m.writeValueAsString(people);
            System.out.println(s);
            m.writeValue(new File("model.json"), people);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadJson() {
        ObjectMapper m = new ObjectMapper();
        try {
            people = m.readValue(new File("model.json"), People.class);
            dumpPeople();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCsv() {
        try (PrintWriter w =
                     new PrintWriter(
                             new OutputStreamWriter(
                                     new FileOutputStream("model.csv"), StandardCharsets.UTF_8
                             )
                     )
        ) {
            for (Person p : people.getList()) {
                // vytiskneme 1 radek do souboru
                w.print(p.getFirstName());
                w.print(";");
                w.print(p.getLastName());
                w.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCsv() {
        try (BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream("model.csv"), StandardCharsets.UTF_8))) {
            String s;
            people = new People();
            int i = 1;
            while ((s = r.readLine()) != null) {
                // precteme jeden radek a vyrobime z nej instanci osoby a pridame ji do seznamu
                String[] cols = s.split(";");
                if (cols.length == 2) {
                    Person p = new Person(cols[0], cols[1]);
                    people.getList().add(p);
                } else {
                    System.out.println("WARNING: wrong column count on row " + i);
                }
                i++;
            }
            dumpPeople();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpPeople() {
        System.out.println("========= PEOPLE: =========");
        for (Person p : people.getList()) {
            System.out.println(p);
        }
        System.out.println("=====================");
    }

    public static void main(String[] args) {
        MainClass c = new MainClass();
        //c.saveJson();
        //c.loadJson();
        //c.saveCsv();
        c.loadCsv();
    }
}
