package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Gabriel Matte
 */
public class DonationRecordLogicTest {
    
    private DonationRecordLogic drLogic;
    private DonationRecord expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setup() throws Exception {
        
        // Create instances of logicfactory and entity manager. Begin transaction on entity manager
        // This is done only for test purposes. 
        drLogic = LogicFactory.getFor("DonationRecord");
        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();
        
        // Create Person and BloodDonation entities for the purposes of testing only
        // instance of BloodBank may also be required if BloodDonation dependency does not already exist in DB
        TypedQuery<Person> tq = em.createNamedQuery("Person.findAll", Person.class);
        List<Person> persons = tq.getResultList();
        // Check if the Person dependency exists on DB already
        Person person = null;
        // If result is null, create the entity and persist it
        if (persons.isEmpty()) {
            person = new Person();
            person.setFirstName("first");
            person.setLastName("last");
            person.setPhone("9991234567");
            person.setAddress("123 somewhere");
            person.setBirth(drLogic.convertStringToDate("2000-01-01 12:00:00"));
            // Persist the dependency
            em.persist(person);
        }else{
            person = persons.get(0);
        }
        
        TypedQuery<BloodBank> tq2 = em.createNamedQuery("BloodBank.findAll", BloodBank.class);
        List<BloodBank> banks = tq2.getResultList();
        
        BloodBank bb = null;
        // If result is null, create the entity and persist it
        if (banks.isEmpty()) {
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(drLogic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            // Persist the dependency
            em.persist(bb);
            
            System.out.println(bb.toString());
        }else{
            bb = banks.get(0);
        }
          
        TypedQuery<BloodDonation> tq3Query = em.createNamedQuery("BloodDonation.findAll", BloodDonation.class);
        List<BloodDonation> blood = tq3Query.getResultList();
       
        // Check if the BloodDonation dependency exists on DB already
        BloodDonation bd =null;
        // If result is null, create the entity and persist it
        if (blood.isEmpty()) {
            bd = new BloodDonation();
            bd.setBloodBank(bb);
            bd.setMilliliters(100);
            bd.setBloodGroup(BloodGroup.AB);
            bd.setRhd(RhesusFactor.Negative);
            bd.setCreated(drLogic.convertStringToDate("1111-11-11 11:11:11"));
            // Persist the dependency
            em.persist(bd);
        }else{
            bd =blood.get(0);
        }
        
        // Finally, create the DonationRecord Entity to be tested        
        DonationRecord entity = new DonationRecord();
        // Set dependencies
        entity.setPerson(person);
        entity.setBloodDonation(bd);
        // Set remaining parameters
        entity.setTested(true);
        entity.setAdministrator("testadmin");
        entity.setHospital("testhospital");
        entity.setCreated(drLogic.convertStringToDate("1111-11-11 11:11:11"));
        
        // Add entity to Hibernate
        // Merge so that entity remains available for testing
        expectedEntity = em.merge(entity);
        // Commit changes and close EntityManager
        em.getTransaction().commit();
        em.close();
       
    }
    
    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            drLogic.delete(expectedEntity);
        }
    }
    
    // Tests the getAll() method - Should return a list with all DonationRecords in the DB
    @Test
    public void testGetAll() {
        List<DonationRecord> list = drLogic.getAll();
        // Get initial size of the list of DonationRecords
        int size = list.size();
        // Check that the entity was actually created
        assertNotNull(expectedEntity);
        // Remove the expected entity
        drLogic.delete(expectedEntity);
        list = drLogic.getAll();
        // Size of DonationRecords list should be one smaller after removing expected entity
        assertEquals(size -1, list.size());
    }
    
    /**
     * Test getWithId()
     * Retrieves the record from the DB and ensures that it matches the expected entity
     */
    @Test
    public void testGetWithId() {
        DonationRecord record = drLogic.getWithId(expectedEntity.getId());
        assertDonationRecordEquals(expectedEntity, record);
    }
    
    @Test 
    final void testGetColumnNames() {
        List<String> list = drLogic.getColumnNames();
        assertEquals(Arrays.asList("Record ID", "Person ID", "Donation ID", "Tested", "Administrator", "Hospital", "Created"), list);
    }
    
    @Test
    final void testGetColumnCodes() {
        List<String> list = drLogic.getColumnCodes();
        assertEquals(Arrays.asList(DonationRecordLogic.ID, DonationRecordLogic.PERSON_ID, DonationRecordLogic.DONATION_ID, 
                DonationRecordLogic.TESTED, DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED), list);
    }
    
    /**
     * Test extractDataAsList()
     * Ensures that the method returns the correct data in the correct format
     */
    @Test
    final void testExtractDataAsList() {
        List<?> list = drLogic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getPerson().getId(), list.get(1));
        assertEquals(expectedEntity.getBloodDonation().getId(), list.get(2));
        assertEquals(expectedEntity.getTested(), list.get(3));
        assertEquals(expectedEntity.getAdministrator(), list.get(4));
        assertEquals(expectedEntity.getHospital(), list.get(5));
        assertEquals(drLogic.convertDateToString(expectedEntity.getCreated()), list.get(6));
    }
    
    /**
     * Tests getDonationRecordWithTested()
     * Gets the list of records from the DB that have the same value for 'Tested' as the test Entity
     * Loops through each record to confirm that its 'Tested' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithTested() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithTested(expectedEntity.getTested());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getTested(), donationRecord.getTested());
        }
    }
    
    /**
     * Tests getDonationRecordWithAdministrator()
     * Gets the list of records from the DB that have the same value for 'Administrator' as the test Entity
     * Loops through each record to confirm that its 'Administrator' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithAdministrator() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithAdministrator(expectedEntity.getAdministrator());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getAdministrator(), donationRecord.getAdministrator());
        }
    }
    
    /**
     * Tests getDonationRecordWithHospital()
     * Gets the list of records from the DB that have the same value for 'Hospital' as the test Entity
     * Loops through each record to confirm that its 'Hospital' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithHospital() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithHospital(expectedEntity.getHospital());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getHospital(), donationRecord.getHospital());
        }
    }
    
    /**
     * Tests getDonationRecordWithCreated()
     * Gets the list of records from the DB that have the same value for 'Created' as the test Entity
     * Loops through each record to confirm that its 'Created' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithCreated() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithCreated(expectedEntity.getCreated());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getCreated(), donationRecord.getCreated());
        }
    }
    
    /**
     * Tests getDonationRecordWithPerson()
     * Gets the list of records from the DB that have the same value for 'PersonId' as the test Entity
     * Loops through each record to confirm that its 'PersonId' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithPerson() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithPerson(expectedEntity.getPerson().getId());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getPerson().getId(), donationRecord.getPerson().getId());
        }
    }
    
    /**
     * Tests getDonationRecordWithDonation()
     * Gets the list of records from the DB that have the same value for 'BloodDonationId' as the test Entity
     * Loops through each record to confirm that its 'BloodDonationId' value matches the expected Entity
     */
    @Test
    final void testGetDonationRecordWithDonation() {
        List<DonationRecord> recordsDB = drLogic.getDonationRecordWithDonation(expectedEntity.getBloodDonation().getId());
        for (DonationRecord donationRecord : recordsDB) {
            assertEquals(expectedEntity.getBloodDonation().getId(), donationRecord.getBloodDonation().getId());
        }
    }
    
    
    /**
     * Tests createEntity()
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Attempts to create a new entity and ensure that the parameters match the expected entity
     */
    @Test
    final void testCreateEntity() {
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
        testMap.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
        testMap.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
        testMap.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
        testMap.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        
        // Create a new record using mapped values. Check that it is equal to the existing entity in the DB.
        DonationRecord newRecord = drLogic.createEntity(testMap);
        assertDonationRecordEquals(expectedEntity, newRecord, false);
    }
    
    /**
     * Tests createEntity() for Null and Empty values of ID
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Replaces ID with null and empty values and test that the method throws a NullPointerException
     * Confirms that this value cannot be empty
     */
    @Test
    final void testCreateEntityNullAndEmptyID() {
        Map<String, String []> testMap = new HashMap<>();
        Consumer<Map<String, String [] >> fillMap = (Map<String, String [] > map) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
            map.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
            map.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
            map.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
            map.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.ID, null);
        assertThrows(NullPointerException.class, () -> drLogic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.ID, new String [] {} );
        assertThrows(IndexOutOfBoundsException.class, () -> drLogic.createEntity(testMap));             
    }
    
    /**
     * Tests createEntity() for Null and Empty values of Tested
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Replaces 'Tested' with null and empty values and test that the method throws a NullPointerException
     * Confirms that this value cannot be empty when creating a new entity
     */
    @Test
    final void testCreateEntityNullAndEmptyTested() {
        Map<String, String []> testMap = new HashMap<>();
        Consumer<Map<String, String [] >> fillMap = (Map<String, String [] > map) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
            map.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
            map.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
            map.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
            map.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.TESTED, null);
        assertThrows(NullPointerException.class, () -> drLogic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.TESTED, new String [] {} );
        assertThrows(IndexOutOfBoundsException.class, () -> drLogic.createEntity(testMap));             
    }
    
    /**
     * Tests createEntity() for Null and Empty values of Administrator
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Replaces 'Administrator' with null and empty values and test that the method throws a NullPointerException
     * Confirms that this value cannot be empty when creating a new entity
     */
    @Test
    final void testCreateEntityNullAndEmptyAdministrator() {
        Map<String, String []> testMap = new HashMap<>();
        Consumer<Map<String, String [] >> fillMap = (Map<String, String [] > map) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
            map.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
            map.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
            map.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
            map.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.ADMINISTRATOR, null);
        assertThrows(NullPointerException.class, () -> drLogic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.ADMINISTRATOR, new String [] {} );
        assertThrows(IndexOutOfBoundsException.class, () -> drLogic.createEntity(testMap));             
    }
    
    
    /**
     * Tests createEntity() for Null and Empty values of Hospital
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Replaces 'Hospital' with null and empty values and test that the method throws a NullPointerException
     * Confirms that this value cannot be empty when creating a new entity
     */
    @Test
    final void testCreateEntityNullAndEmptyHospital() {
        Map<String, String []> testMap = new HashMap<>();
        Consumer<Map<String, String [] >> fillMap = (Map<String, String [] > map) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
            map.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
            map.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
            map.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
            map.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.HOSPITAL, null);
        assertThrows(NullPointerException.class, () -> drLogic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.HOSPITAL, new String [] {} );
        assertThrows(IndexOutOfBoundsException.class, () -> drLogic.createEntity(testMap));             
    }
    
    /**
     * Tests createEntity() for Null and Empty values of Created
     * Creates a new parameter map with the parameters obtained from the expected entity
     * Replaces 'Created' with null and empty values and test that the method throws a NullPointerException
     * Confirms that this value cannot be empty when creating a new entity
     */
    @Test
    final void testCreateEntityNullAndEmptyCreated() {
        Map<String, String []> testMap = new HashMap<>();
        Consumer<Map<String, String [] >> fillMap = (Map<String, String [] > map) -> {
            map.clear();
            map.put( DonationRecordLogic.ID, new String [] { Integer.toString(expectedEntity.getId())});
            map.put( DonationRecordLogic.TESTED, new String [] { Boolean.toString(expectedEntity.getTested())});
            map.put( DonationRecordLogic.ADMINISTRATOR, new String [] { expectedEntity.getAdministrator()});
            map.put( DonationRecordLogic.HOSPITAL, new String [] { expectedEntity.getHospital()});
            map.put( DonationRecordLogic.CREATED, new String [] { drLogic.convertDateToString(expectedEntity.getCreated())});
        };
        
        fillMap.accept(testMap);
        testMap.replace(DonationRecordLogic.CREATED, null);
        assertThrows(NullPointerException.class, () -> drLogic.createEntity(testMap));
        testMap.replace(DonationRecordLogic.CREATED, new String [] {} );
        assertThrows(IndexOutOfBoundsException.class, () -> drLogic.createEntity(testMap));             
    }    
    
    /**
     * Helper method to determine equality between expected entity and actual entity.
     * 
     * @param expected The pre-defined entity used for test purposes
     * @param actual The entity pulled from the DB
     */
    private void assertDonationRecordEquals(DonationRecord expected, DonationRecord actual) {
        assertDonationRecordEquals(expected, actual, true);
    }
    
    /**
     * Helper method to determine equality between expected entity and actual entity.
     * Boolean hasDependency triggers a check for equality in Person and BloodDonation.
     * Generally, there will be a dependency when checking against records from the DB.
     * However, some methods are tested without pulling entities from DB - hasDependency would be false.
     */
    /**
     * Helper method to determine equality between expected entity and actual entity.
     * Boolean hasDependency triggers a check for equality in Person and BloodDonation.
     * Generally, there will be a dependency when checking against records from the DB.
     * However, some methods are tested without pulling entities from DB - hasDependency would be false.
     * 
     * @param expected The pre-defined entity used for test purposes
     * @param actual The entity pulled from the DB
     * @param hasDependency Whether the entity being tested has dependencies in the DB
     */
    private void assertDonationRecordEquals(DonationRecord expected, DonationRecord actual, boolean hasDependency) {
        
        assertEquals( expected.getId(), actual.getId());
        assertEquals( expected.getTested(), actual.getTested());
        assertEquals( expected.getAdministrator(), actual.getAdministrator());
        assertEquals( expected.getHospital(), actual.getHospital());
        assertEquals( expected.getCreated(), actual.getCreated());
        
        if (hasDependency) {
            assertEquals( expected.getPerson().getId(), actual.getPerson().getId());
            assertEquals( expected.getBloodDonation().getId(), actual.getBloodDonation().getId());
        }
    }
    
        @Test
    final void testSearch() {
        int foundFull = 0;
        //search for a substring of one of the fields in the expectedAccount
        String searchString = expectedEntity.getHospital().substring( 3 );
        //in account we only search for display name and user, this is completely based on your design for other entities.
        List<DonationRecord> returnedRecord = drLogic.search( searchString );
        for( DonationRecord donationRecord: returnedRecord ) {
            //all accounts must contain the substring
            assertTrue( donationRecord.getHospital().contains( searchString ) );
            //exactly one account must be the same
            if( donationRecord.getId().equals( expectedEntity.getId() ) ){
                assertDonationRecordEquals( expectedEntity, donationRecord );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
    }
}