package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * This class is has the example of how to add dependency when working with
 * junit. it is commented because some components need to be made first. You
 * will have to import everything you need.
 *
 * @author Shariar (Shawn) Emami
 */

class BloodDonationTest {

    private BloodDonationLogic logic;
    private BloodDonation expectedEntity;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor("BloodDonation");
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the BloodDonationt to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();
        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        TypedQuery<BloodBank> tq = em.createNamedQuery("BloodBank.findAll", BloodBank.class);
        List<BloodBank> bloodBank = tq.getResultList();
        BloodBank bb =null;
        //if result is null create the entity and persist it
        if (bloodBank.isEmpty()) {
            //cearet object
            bb = new BloodBank();
            bb.setName("NIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
            //persist the dependency first
            em.persist(bb);
        }else{
            bb = bloodBank.get(0);
        }

        //create the desired entity
        BloodDonation entity = new BloodDonation();
        entity.setMilliliters(100);
        entity.setBloodGroup(BloodGroup.AB);
        entity.setRhd(RhesusFactor.Negative);
        entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
        //add dependency to the desired entity
        entity.setBloodBank(bb);

        //add desired entity to hibernate, entity is now managed.
        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em.merge(entity);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<BloodDonation> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure BloodDonation was created successfully
        assertNotNull(expectedEntity);
        //delete the new BloodDonation
        logic.delete(expectedEntity);

        //get all BloodDonation again
        list = logic.getAll();
        //the new size of BloodDonation must be one less
        assertEquals(originalSize - 1, list.size());
    }

    /**
     * helper method for testing all BloodDonation fields
     *
     * @param expected entity of BloodDonation
     * @param actual return entity of BloodDonation
     */
    private void assertBloodDonationEquals(BloodDonation expected, BloodDonation actual) {
        //assert all field to guarantee they are the same
        assertBloodDonationEquals(expected, actual, true);
    }

    private void assertBloodDonationEquals(BloodDonation expected, BloodDonation actual, boolean hasDepen) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMilliliters(), actual.getMilliliters());
        assertEquals(expected.getBloodGroup(), actual.getBloodGroup());
        assertEquals(expected.getRhd(), actual.getRhd());
        assertEquals(expected.getCreated(), actual.getCreated());
        if (hasDepen) {
            assertEquals(expected.getBloodBank().getId(), actual.getBloodBank().getId());
        }
    }

    /**
     * test method getWithID
     */
    @Test
    final void testGetWithId() {
        //using the id of test get BloodDonation another bloodDonation from logic
        BloodDonation returnedBD = logic.getWithId(expectedEntity.getId());
        //the two BloodDonations must be the same   
        assertBloodDonationEquals(expectedEntity, returnedBD);
    }

    /**
     * test getColumnNames()
     */
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Bank_id", "Milliliters", "Blood_group", "Rhesus_factor", "Created"), list);
    }

    /**
     * test getColumnCodes()
     */
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodDonationLogic.ID, BloodDonationLogic.BANK_ID, BloodDonationLogic.MILLILITERS, BloodDonationLogic.BLOOD_GROUP, BloodDonationLogic.RHESUS_FACTOR, BloodDonationLogic.CREATED), list);
    }

    /**
     * test extract data from column
     */
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        assertEquals(expectedEntity.getBloodBank().getId(), list.get(1));
        assertEquals(expectedEntity.getMilliliters(), list.get(2));
        assertEquals(expectedEntity.getBloodGroup().toString(), list.get(3));
        assertEquals(expectedEntity.getRhd().getSymbol(), list.get(4));
        assertEquals(logic.convertDateToString(expectedEntity.getCreated()), list.get(5));
    }

    @Test
    final void testEdgeGetBloodDonationWithMilliliters() {
        List<BloodDonation> returnedBd = logic.getBloodDonationWithMilliliters(expectedEntity.getMilliliters());
        for (BloodDonation bloodDonation : returnedBd) {
            assertEquals(expectedEntity.getMilliliters(), bloodDonation.getMilliliters());
        }
    }

    @Test
    final void testGetBloodDonationWithBloodGroup() {
        List<BloodDonation> returnedBd = logic.getBloodDonationWithBloodGroup(expectedEntity.getBloodGroup());
        for (BloodDonation bloodDonation : returnedBd) {
            assertEquals(expectedEntity.getBloodGroup(), bloodDonation.getBloodGroup());
        }
    }

    @Test
    final void testGetBloodDonationWithCreated() {
        List<BloodDonation> returnedBd = logic.getBloodDonationWithCreated(expectedEntity.getCreated());
        for (BloodDonation bloodDonation : returnedBd) {
            assertEquals(expectedEntity.getCreated(), bloodDonation.getCreated());
        }
    }

    @Test
    final void testGetBloodDonationsWithRhd() {
        List<BloodDonation> returnedBd = logic.getBloodDonationsWithRhd(expectedEntity.getRhd());
        for (BloodDonation bloodDonation : returnedBd) {
            assertEquals(expectedEntity.getRhd(), bloodDonation.getRhd());
        }
    }

    @Test
    final void testGetBloodDonationsWithBloodBank() {
        List<BloodDonation> returnedBd = logic.getBloodDonationsWithBloodBank(expectedEntity.getBloodBank().getId());
        for (BloodDonation bloodDonation : returnedBd) {
            assertEquals(expectedEntity.getBloodBank().getId(), bloodDonation.getBloodBank().getId());
        }
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        testMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        testMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        testMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().toString()});
        testMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().getSymbol()});
        testMap.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        BloodDonation returnedBD = logic.createEntity(testMap);
        //the two BloodDonation entities must be the same
        assertBloodDonationEquals(expectedEntity, returnedBD, false);
    }

    /**
     * Error case: test when null and empty values throws exception
     */
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> testMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
            map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
            map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().toString()});
            map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().getSymbol()});
            map.put(BloodDonationLogic.CREATED, new String[]{logic.convertDateToString(expectedEntity.getCreated())});

        };
        fillMap.accept(testMap);
        //donation id can not be null
        testMap.replace(BloodDonationLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(BloodDonationLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        fillMap.accept(testMap);
        //MILLILITERS can not be null
        testMap.replace(BloodDonationLogic.MILLILITERS, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(BloodDonationLogic.MILLILITERS, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        fillMap.accept(testMap);
        //BLOOD_GROUP can not be null
        testMap.replace(BloodDonationLogic.BLOOD_GROUP, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(BloodDonationLogic.BLOOD_GROUP, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        fillMap.accept(testMap);
        //rhd can not be null
        testMap.replace(BloodDonationLogic.RHESUS_FACTOR, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(BloodDonationLogic.RHESUS_FACTOR, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

        fillMap.accept(testMap);
        //crated date can not be null
        testMap.replace(BloodDonationLogic.CREATED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));
        testMap.replace(BloodDonationLogic.CREATED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(testMap));

    }
    
    
    @Test
    final void testSearch() {
        int foundFull = 0;
        String searchString = String.valueOf(expectedEntity.getMilliliters()).substring(1);
        //in account we only search for display name and user, this is completely based on your design for other entities.
        List<BloodDonation> returnedDonations = logic.search( searchString );
        for( BloodDonation bloodDonation: returnedDonations ) {
            //all accounts must contain the substring
            assertTrue( String.valueOf(bloodDonation.getMilliliters()).contains( searchString ));
            //exactly one account must be the same
            if( bloodDonation.getId().equals( expectedEntity.getId() ) ){
                assertBloodDonationEquals( expectedEntity, bloodDonation );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
    }

}
