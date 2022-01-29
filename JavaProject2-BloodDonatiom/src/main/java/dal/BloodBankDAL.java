package dal;

import entity.BloodBank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jing Zhao
 */
public class BloodBankDAL extends GenericDAL<BloodBank> {

    public BloodBankDAL() {
        super(BloodBank.class);
    }

    /**
     * set up find all
     *
     * @return findResults("BloodBank.findAll", null
     */
    @Override
    public List<BloodBank> findAll() {
        // named query defined BloodBank entity
        return findResults("BloodBank.findAll", null);

    }

    /**
     * set up findById
     *
     * @param bankId int, bank id
     * @return findResult("BloodBank.findByBankId", map)
     */
    @Override
    public BloodBank findById(int bankId) {
        //first argument is a name given to a named query defined in  BloodBank  entity
        //second argument is map used for parameter substitution.
        //in this case the parameter is named "bankId" and value for it is put in map
        Map<String, Object> map = new HashMap<>();
        map.put("bankId", bankId);
        return findResult("BloodBank.findByBankId", map);

    }

    /**
     * set up findByNam
     *
     * @param name String, name
     * @return findResult("BloodBank.findByName", map)
     */
    public BloodBank findByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResult("BloodBank.findByName", map);
    }

    /**
     * set up find by privately owned
     *
     * @param privatelyOwned Boolean, private owned
     * @return findResults("BloodBank.findByPrivatelyOwned", map)
     */
    public List<BloodBank> findByPrivatelyOwned(boolean privatelyOwned) {
        Map<String, Object> map = new HashMap<>();
        map.put("privatelyOwned", privatelyOwned);
        return findResults("BloodBank.findByPrivatelyOwned", map);

    }

    /**
     * set up findByEstablished
     *
     * @param established Date, established date
     * @return findResults("BloodBank.findByEstablished", map)
     */
    public List<BloodBank> findByEstablished(Date established) {
        Map<String, Object> map = new HashMap<>();
        map.put("established", established);
        return findResults("BloodBank.findByEstablished", map);
    }

    /**
     * set up findByEmplyeeCount
     *
     * @param emplyeeCount int emplyeeCount
     * @return
     */
    public List<BloodBank> findByEmplyeeCount(int emplyeeCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("emplyeeCount", emplyeeCount);
        return findResults("BloodBank.findByEmplyeeCount", map);
    }

    /**
     * set up findByOwner
     *
     * @param ownerId int, ownerId
     * @return
     */
    public BloodBank findByOwner(int ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", ownerId);
        return findResult("BloodBank.findByOwner", map);
    }

    /**
     * set up findByOwner
     *
     * @param search String search
     * @return findResults("BloodBank.search", map)
     */
    public List<BloodBank> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults("BloodBank.findContaining", map);
    }

}
