package logic;

import dal.BloodDonationDAL;
import entity.BloodDonation;
import java.util.List;
import java.util.Map;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Arrays;
import java.util.Date;
import common.ValidationException;

/**
 *
 * @author danping tang
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL> {

    /**
     * create static final variables with proper name of each column of
     * BloodDonation table instead always refer to these variables.
     */
    public static final String BANK_ID = "bank_id";
    public static final String MILLILITERS = "milliliters";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String RHESUS_FACTOR = "rhesus_factor";
    public static final String CREATED = "created";
    public static final String ID = "id";
    
    /**
     * 
     */
    BloodDonationLogic() {
        super(new BloodDonationDAL());
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Bank_id", "Milliliters", "Blood_group", "Rhesus_factor", "Created");
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, BANK_ID, MILLILITERS, BLOOD_GROUP, RHESUS_FACTOR, CREATED);
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * @param e given Entity to extract data from.
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList(BloodDonation e) {
            return Arrays.asList(e.getId(), e.getBloodBank()==null?"null":e.getBloodBank().getId(), e.getMilliliters(), e.getBloodGroup().toString(), e.getRhd().getSymbol(),convertDateToString(e.getCreated()));
    }
    
    /**
     * 
     * @param parameterMap
     * @return 
     */
    @Override
    public BloodDonation createEntity(Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            throw new NullPointerException("parameterMap cannot be null");
        }
        //create a new BloodDonation Entity object
        BloodDonation bdEntity = new BloodDonation();
        //ID check
        if (parameterMap.containsKey(ID)) {
            try {
                bdEntity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        
        String created = parameterMap.get(CREATED)[0];
        created = created.replaceAll("T", " ");
        
        bdEntity.setMilliliters(Integer.parseInt(parameterMap.get(MILLILITERS)[0]));
        bdEntity.setBloodGroup(BloodGroup.valueOf(parameterMap.get(BLOOD_GROUP)[0]));
        bdEntity.setRhd(RhesusFactor.getRhesusFactor(parameterMap.get(RHESUS_FACTOR)[0]));
        bdEntity.setCreated(convertStringToDate(created));
        
        // no string,no need to validate for bollod_group,reh ENUM,milliters,datetime  
        return bdEntity;
    }
    
    /**
     * 
     * @param search
     * @return 
     */
    @Override
    public List<BloodDonation> search( String search ) {
        return get( () -> dal().findContaining( search ) );
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<BloodDonation> getAll() {
        return get(() -> dal().findAll());
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    @Override
    public BloodDonation getWithId(int id) {
        return get(() -> dal().findById(id));
    }
    
    /**
     * 
     * @param milliliters
     * @return 
     */
    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return get(() -> dal().findByMilliliters(milliliters));

    }
    
    /**
     * 
     * @param bloodGroup
     * @return 
     */
    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return get(() -> dal().findByBloodGroup(bloodGroup));
    }
    
    /**
     * 
     * @param created
     * @return 
     */
    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));

    }
    
    /**
     * 
     * @param rhd
     * @return 
     */
    public List<BloodDonation> getBloodDonationsWithRhd(RhesusFactor rhd) {
        return get(() -> dal().findByRhd(rhd));
    }
    
    /**
     * 
     * @param bankId
     * @return 
     */
    public List<BloodDonation> getBloodDonationsWithBloodBank(int bankId) {
        return get(() -> dal().findByBloodBank(bankId));
    }

}

