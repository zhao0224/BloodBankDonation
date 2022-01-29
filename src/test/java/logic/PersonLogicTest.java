package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.Account;
import entity.Person;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
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
 * @author xiaod
 */
class PersonLogicTest {
    private PersonLogic logic;
    private Person expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setUp() throws Exception {
        logic = LogicFactory.getFor( "Person" );
        EntityManager em = EMFactory.getEMF().createEntityManager();
        em.getTransaction().begin();

        Person entity = new Person();
        entity.setFirstName( "Xiaodan" );
        entity.setLastName( "Chen" );
        entity.setPhone( "123456789" );
        entity.setAddress( "Ottawa" );
        entity.setBirth(logic.convertStringToDate("2008-03-20 03:20:00"));

        expectedEntity = em.merge( entity );
        em.getTransaction().commit();
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }
    
    @Test
    final void testGetAll() {
        List<Person> list = logic.getAll();
        int originalSize = list.size();

        assertNotNull( expectedEntity );
        logic.delete( expectedEntity );

        list = logic.getAll();
        assertEquals( originalSize - 1, list.size() );
    }

    /**
     * helper method for testing all account fields
     *
     * @param expected
     * @param actual
     */
    private void assertPersonEquals( Person expected, Person actual ) {
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getFirstName(), actual.getFirstName());
        assertEquals( expected.getLastName(), actual.getLastName());
        assertEquals( expected.getPhone(), actual.getPhone() );
        assertEquals( expected.getAddress(), actual.getAddress());
        assertEquals( expected.getBirth(), actual.getBirth());
    }
    
    @Test
    final void testGetWithId() {
        Person returnedPerson = logic.getWithId( expectedEntity.getId() );
        assertPersonEquals( expectedEntity, returnedPerson );
    }

    @Test
    final void testGetPersonWithFirstName() {
        List<Person> returnedPerson = logic.getPersonWithFirstName( expectedEntity.getFirstName() );
        for (Person person : returnedPerson) {
            assertEquals(expectedEntity.getFirstName(), person.getFirstName());
        }
    }

    @Test
    final void testGetPersonWithLastName() {
        List<Person> returnedPerson = logic.getPersonWithLastName( expectedEntity.getLastName() );
        for (Person person : returnedPerson) {
            assertEquals(expectedEntity.getLastName(), person.getLastName());
        }
    }

    @Test
    final void testGetPersonWithPhone() {
        List<Person> returnedPerson = logic.getPersonWithPhone( expectedEntity.getPhone() );
        for (Person person : returnedPerson) {
            assertEquals(expectedEntity.getPhone(), person.getPhone());
        }
    }
    
    @Test
    final void testGetPersonWithAddress() {
        List<Person> returnedPerson = logic.getPersonWithAddress( expectedEntity.getAddress() );
        for (Person person : returnedPerson) {
            assertEquals(expectedEntity.getAddress(), person.getAddress());
        }
    }
    
    @Test
    final void testGetPersonWithBirth() {
        List<Person> returnedPerson = logic.getPersonWithBirth( expectedEntity.getBirth() );
        for (Person person : returnedPerson) {
            assertEquals(expectedEntity.getBirth(), person.getBirth());
        }
    }
    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList( "ID","First_Name", "Last_Name", "Phone", "Address", "Birth" ), list );
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList( PersonLogic.ID,PersonLogic.FIRST_NAME, PersonLogic.LAST_NAME, PersonLogic.PHONE, PersonLogic.ADDRESS, PersonLogic.BIRTH), list );
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getFirstName(), list.get( 1) );
        assertEquals( expectedEntity.getLastName(), list.get( 2 ) );
        assertEquals( expectedEntity.getPhone(), list.get( 3 ) );
        assertEquals( expectedEntity.getAddress(), list.get( 4 ) );
        assertEquals( logic.convertDateToString(expectedEntity.getBirth()), list.get( 5 ) );
        assertEquals( expectedEntity.getId(), list.get( 0) );
    }
    
    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ "Xiaodan" } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ "Chen" } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ "123456789" } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ "Ottawa" } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "1111-11-11 11:11:11" } );

        Person returnedPerson = logic.createEntity( sampleMap );
        logic.add( returnedPerson);

        returnedPerson = logic.getWithId( returnedPerson.getId() );

        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], returnedPerson.getFirstName());
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], returnedPerson.getLastName());
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], returnedPerson.getPhone());
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], returnedPerson.getAddress());
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(returnedPerson.getBirth()));
        
        logic.delete( returnedPerson );
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName()} );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName()} );
        sampleMap.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone()} );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress()} );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth())} );
        
        Person returnedAccount = logic.createEntity( sampleMap );

        assertPersonEquals( expectedEntity, returnedAccount );
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName()} );
            map.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName()} );
            map.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone()} );
            map.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress()} );
            map.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth())} );
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.FIRST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.LAST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.LAST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.PHONE, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.PHONE, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ADDRESS, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.BIRTH, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.BIRTH, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName()} );
            map.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName()} );
            map.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone()} );
            map.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress()} );
            map.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth())} );
        };

        IntFunction<String> generateString = ( int length ) -> {
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ID, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ID, new String[]{ "11a" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 51) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.LAST_NAME, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace(PersonLogic.LAST_NAME, new String[]{ generateString.apply( 51 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.PHONE, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.PHONE, new String[]{ generateString.apply( 16 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{ "" } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{ generateString.apply( 101 ) } );
        assertThrows( ValidationException.class, () -> logic.createEntity( sampleMap ) );

    }

    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
   //     sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "2008-10-10 12:00:00" } );
        
        //idealy every test should be in its own method
        Person returnedPerson = logic.createEntity( sampleMap );
       // assertEquals( Integer.parseInt( sampleMap.get( PersonLogic.ID )[ 0 ] ), returnedPerson.getId() );
        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], returnedPerson.getFirstName());
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], returnedPerson.getLastName());
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], returnedPerson.getPhone());
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], returnedPerson.getAddress());
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(returnedPerson.getBirth()));
        
        sampleMap = new HashMap<>();
     //   sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 50 ) } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 50 ) } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 15 ) } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 100 ) } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "2008-10-10 12:00:00" } );

        //idealy every test should be in its own method
        returnedPerson = logic.createEntity( sampleMap );
   //     assertEquals( Integer.parseInt( sampleMap.get( PersonLogic.ID )[ 0 ] ), returnedPerson.getId() );
        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], returnedPerson.getFirstName());
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], returnedPerson.getLastName());
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], returnedPerson.getPhone());
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], returnedPerson.getAddress());
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(returnedPerson.getBirth()));
    }
    
    
    @Test
    final void testSearch() {
        int foundFull = 0;
        //search for a substring of one of the fields in the expectedAccount
        String searchString = expectedEntity.getFirstName().substring( 3 );
        //in account we only search for display name and user, this is completely based on your design for other entities.
        List<Person> returnedPerson = logic.search( searchString );
        for( Person person: returnedPerson ) {
            //all accounts must contain the substring
            assertTrue( person.getFirstName().contains( searchString ));
            //exactly one account must be the same
            if( person.getId().equals( expectedEntity.getId() ) ){
                assertPersonEquals( expectedEntity, person );
                foundFull++;
            }
        }
        assertEquals( 1, foundFull, "if zero means not found, if more than one means duplicate" );
    }
}
