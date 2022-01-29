package logic;

import dal.DonationRecordDAL;
import entity.DonationRecord;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import common.ValidationException;
import entity.BloodDonation;
import entity.Person;
import java.util.function.ObjIntConsumer;

/**
 *
 * @author Gabriel Matte
 */
public class DonationRecordLogic extends GenericLogic<DonationRecord, DonationRecordDAL> {

    public static final String PERSON_ID = "person_id";
    public static final String DONATION_ID = "donation_id";
    public static final String TESTED = "tested";
    public static final String ADMINISTRATOR = "administrator";
    public static final String HOSPITAL = "hospital";
    public static final String CREATED = "created";
    public static final String ID = "record_id";

    DonationRecordLogic() {
        super(new DonationRecordDAL());
    }

    @Override
    public List<DonationRecord> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public DonationRecord getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<DonationRecord> getDonationRecordWithTested(boolean tested) {
        return get(() -> dal().findByTested(tested));
    }

    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator) {
        return get(() -> dal().findByAdministrator(administrator));
    }

    public List<DonationRecord> getDonationRecordWithHospital(String username) {
        return get(() -> dal().findByHospital(username));
    }

    public List<DonationRecord> getDonationRecordWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<DonationRecord> getDonationRecordWithPerson(int personId) {
        return get(() -> dal().findByPerson(personId));
    }

    public List<DonationRecord> getDonationRecordWithDonation(int donationId) {
        return get(() -> dal().findByDonation(donationId));
    }
    
    @Override
    public List<DonationRecord> search( String search ) {
        return get( () -> dal().findContaining( search ) );
    }

    @Override
    public DonationRecord createEntity(Map<String, String[]> parameterMap) {

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        DonationRecord entity = new DonationRecord();

        // Check for an ID. It should be auto-generated by the DB.
        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException e) {
                throw new ValidationException(e);
            }
        }

        ObjIntConsumer< String> validator = (value, length) -> {
            if (value == null || value.trim().isEmpty() || value.length() > length) {
                String error = "";
                if (value == null || value.trim().isEmpty()) {
                    error = "value cannot be null or empty: " + value;
                }
                if (value.length() > length) {
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException(error);
            }
        };

        boolean tested;
        if (parameterMap.get(TESTED)[0].trim().equals("true")) {
            tested = true;
        } else {
            tested = false;
        }

        String administrator = parameterMap.get(ADMINISTRATOR)[0].trim();
        String hospital = parameterMap.get(HOSPITAL)[0].trim();
        String date = parameterMap.get(CREATED)[0].trim();

        validator.accept(administrator, 100);
        validator.accept(hospital, 100);

        entity.setTested(tested);
        entity.setAdministrator(administrator);
        entity.setHospital(hospital);
        entity.setCreated(convertStringToDate(date.replace("T", " ")));
        

        return entity;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("Record ID", "Person ID", "Donation ID", "Tested", "Administrator", "Hospital", "Created");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
    }

    @Override
    public List<?> extractDataAsList(DonationRecord e) {

        return Arrays.asList(e.getId(), e.getPerson() == null ? "null" : e.getPerson().getId(),
                e.getBloodDonation() == null ? "null" : e.getBloodDonation().getId(), e.getTested(),
                e.getAdministrator(), e.getHospital(), convertDateToString(e.getCreated()));
    }

    public Person getPersonForDonationRecord(int id) {
        Person p = new Person(id);
        return p;
    }

    public BloodDonation getBloodDonationForDonationRecord(int id) {
        BloodDonation bd = new BloodDonation(id);
        return bd;
    }

}