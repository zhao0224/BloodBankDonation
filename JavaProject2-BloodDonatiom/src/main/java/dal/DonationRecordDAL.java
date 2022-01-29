package dal;

import entity.BloodBank;
import entity.DonationRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriel Matte
 */
public class DonationRecordDAL extends GenericDAL<DonationRecord> {
    
    public DonationRecordDAL() {
        super(DonationRecord.class);
    }

    @Override
    public List<DonationRecord> findAll() {
        return findResults( "DonationRecord.findAll", null);
    }

    /**
     * Searches the database using the Record ID
     * @param id record ID
     * @return 
     */
    @Override
    public DonationRecord findById(int recordId) {
        Map<String, Object> map = new HashMap<>();
        map.put( "recordId", recordId);
        return findResult( "DonationRecord.findByRecordId", map);
    }
    
    public List<DonationRecord> findByTested(boolean tested) {
        Map<String, Object> map = new HashMap<>();
        map.put( "tested", tested);
        return findResults( "DonationRecord.findByTested", map);
    }
    
    public List<DonationRecord> findByAdministrator(String administrator) {
        Map<String, Object> map = new HashMap<>();
        map.put( "administrator", administrator);
        return findResults( "DonationRecord.findByAdministrator", map);
    }
    
    public List<DonationRecord> findByHospital(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put( "username", username);
        return findResults( "DonationRecord.findByHospital", map);
    }
        
    public List<DonationRecord> findByCreated(Date date) {
        Map<String, Object> map = new HashMap<>();
        map.put( "created", date);
        return findResults( "DonationRecord.findByCreated", map);
    }
    
    public List<DonationRecord> findByPerson(int personId) {
        Map<String, Object> map = new HashMap<>();
        map.put( "personId", personId);
        return findResults( "DonationRecord.findByPerson", map);
    }
    
    public List<DonationRecord> findByDonation(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put( "donationId", donationId);
        return findResults( "DonationRecord.findByDonation", map);
    }
    
       public List<DonationRecord> findContaining(String search){
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults( "DonationRecord.findContaining", map);
    }

}
