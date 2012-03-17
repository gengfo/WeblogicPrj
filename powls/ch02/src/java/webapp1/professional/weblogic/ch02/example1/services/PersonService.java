package professional.weblogic.ch02.example1.services;


import java.util.*;

import professional.weblogic.ch02.example1.objects.Person;

public class PersonService {

    private static Map<Integer, Person> people;
    private static int nextValidId = 0;

    static { // static initializer code
        System.out.println("Initializing people list...");
        people = new HashMap<Integer,Person>();
        people.put(new Integer(1), new Person(1,"Mr.","Gregory","","Nyberg"));
        people.put(new Integer(2), new Person(2,"Mr.","Robert","","Patrick"));
        people.put(new Integer(3), new Person(3,"Mr.","John","Q.","Public"));
        nextValidId = 4;
    }

    // Public Methods

    public static Person findPersonById(int id) {
        return (Person)(people.get(new Integer(id)));
    }

    public static void createPerson(Person pPerson) {
        Integer key = new Integer(nextValidId++);
        pPerson.setId(key.intValue());
        people.put(key, pPerson);
    }

    public static void updatePerson(Person pPerson) {
        Integer key = new Integer(pPerson.getId());
        people.put(key, pPerson);
    }

    public static Collection<Person> getPersonList() {
        return people.values();
    }

    public static List<String> getSalutationList() {
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("Miss");
        list.add("Mr.");
        list.add("Mrs.");
        list.add("Ms.");
        return list;
    }

}
