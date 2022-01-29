package dal;

import entity.BloodDonation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Date;

/**
 *
 * @author Danping Tang
 */
public class BloodDonationDAL extends GenericDAL<BloodDonation> {

    public BloodDonationDAL() {
        super(BloodDonation.class);
    }
    
    /**
     * set up findAll
     * @return findResults("BloodDonation.findAll", null)
     */
    @Override
    public List<BloodDonation> findAll() {
        // named query defined BloodDonation entity
        return findResults("BloodDonation.findAll", null);
    }
    
    /**
     * set up findById
     * @param donationId int, donationaId
     * @return findResult("BloodDonation.findByDonationId", map)
     */
    @Override
    public BloodDonation findById(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("donationId", donationId);
        //first argument is a name given to a named query defined in  BloodDonation  entity
        //second argument is map used for parameter substitution.
        //in this case the parameter is named "id" and value for it is put in map
        return findResult("BloodDonation.findByDonationId", map);
    }
    
    /**
     * set findByMilliliters
     * @param milliliters int, milliliters
     * @return findResults("BloodDonation.findByMilliliters", map)
     */
    public List<BloodDonation> findByMilliliters(int milliliters) {
        Map<String, Object> map = new HashMap<>();
        map.put("milliliters", milliliters);
        return findResults("BloodDonation.findByMilliliters", map);
    }
    
    /**
     * set up findByBloodGroup
     * @param bloodGroup BloodGroup, bloodGroup
     * @return findResults("BloodDonation.findByBloodGroup", map)
     */
    public List<BloodDonation> findByBloodGroup(BloodGroup bloodGroup) {
        Map<String, Object> map = new HashMap<>();
        map.put("bloodGroup", bloodGroup);
        return findResults("BloodDonation.findByBloodGroup", map);
    }
    
    /**
     * set up findByRhd
     * @param rhd RhesusFactor, rhd
     * @return findResults("BloodDonation.findByRhd", map)
     */
    public List<BloodDonation> findByRhd(RhesusFactor rhd) {
        Map<String, Object> map = new HashMap<>();
        map.put("rhd", rhd);
        return findResults("BloodDonation.findByRhd", map);
    }
    
    /**
     * set up findByCreated
     * @param created Date, created
     * @return findResults("BloodDonation.findByCreated", map)
     */
    public List<BloodDonation> findByCreated(Date created) {
        Map<String, Object> map = new HashMap<>();
        map.put("created", created);
        return findResults("BloodDonation.findByCreated", map);
    }
    
    /**
     * set up findByBloodBank
     * @param bloodBankId int, bloodBankId
     * @return findResults("BloodDonation.findByBloodBank", map)
     */
    public List<BloodDonation> findByBloodBank(int bloodBankId) {
        Map<String, Object> map = new HashMap<>();
        map.put("bloodBankId", bloodBankId);
        return findResults("BloodDonation.findByBloodBank", map);
    }
    
    /**
     * set up findContaining
     * @param search String search
     * @return findResults("BloodDonation.findContaining", map)
     */
    public List<BloodDonation> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults("BloodDonation.findContaining", map);
    }

}
