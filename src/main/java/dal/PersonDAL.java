package dal;

import entity.Person;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Xiaodan Chen
 */
public class PersonDAL extends GenericDAL<Person> {
    
    public PersonDAL() {
        super(Person.class);
    }

    /**
     * set up findAll
     *
     * @return findResults("Person.findAll", null)
     */
    @Override
    public List<Person> findAll() {
        // named query defined Person entity
        return findResults("Person.findAll", null);
    }

    /**
     * set up findById
     *
     * @param id int id
     * @return findResult("Person.findById", map)
     */
    @Override
    public Person findById(int id) {
        //first argument is a name given to a named query defined in  Person  entity
        //second argument is map used for parameter substitution.
        //in this case the parameter is named "id" and value for it is put in map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult("Person.findById", map);
    }

    /**
     * set up findByFirstName
     *
     * @param firstName String, firstName
     * @return findResults("Person.findByFirstName", map)
     */
    public List<Person> findByFirstName(String firstName) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", firstName);
        return findResults("Person.findByFirstName", map);
    }

    /**
     * set up findByLastName
     *
     * @param lastName String, lastName
     * @return findResults("Person.findByLastName", map)
     */
    public List<Person> findByLastName(String lastName) {
        Map<String, Object> map = new HashMap<>();
        map.put("lastName", lastName);
        return findResults("Person.findByLastName", map);
    }

    /**
     * set up findByPhone
     *
     * @param phone String, phone
     * @return findResults("Person.findByPhone", map)
     */
    public List<Person> findByPhone(String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        return findResults("Person.findByPhone", map);
    }

    /**
     * set up findByAddress
     *
     * @param address String address
     * @return findResults("Person.findByAddress", map)
     */
    public List<Person> findByAddress(String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        return findResults("Person.findByAddress", map);
    }

    /**
     * set up findByBirth
     *
     * @param birth Date, birth
     * @return findResults("Person.findByBirth", map)
     */
    public List<Person> findByBirth(Date birth) {
        Map<String, Object> map = new HashMap<>();
        map.put("birth", birth);
        return findResults("Person.findByBirth", map);
    }

    /**
     * set up findContaining
     *
     * @param search String, search
     * @return findResults("Person.findContaining", map)
     */
    public List<Person> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults("Person.findContaining", map);
    }

}
