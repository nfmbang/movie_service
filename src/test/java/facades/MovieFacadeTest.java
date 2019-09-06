package facades;

import entities.Movie;
import utils.EMF_Creator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.Settings;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;
    private String[] arr1 = {"Kirk Cameron", "Darren Doane", "Bridgette Cameron", "Ben Kientz"};
    private String[] arr2 = {"Bill Murray", "Owen Wilson", "Anjelica Huston"};
    private String[] arr3 = {"George Clooney", "Quentin Tarantino", "Harvey Keitel", "Juliette Lewis"};
    private Movie A = new Movie(2014, "Darren Doane", "Saving Christmas", arr1, 1.8);
    private Movie B = new Movie(2004, "Wes Anderson", "The Life Aquatic with Steve Zissou", arr2, 7.3);
    private Movie C = new Movie(1996, "Robert Rodriguez", "From Dusk Till Dawn", arr3, 7.2);

    public MovieFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/movieDB_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = MovieFacade.getMovieFacade(emf);
    }

    /*   **** HINT ****
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that.
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        facade = MovieFacade.getMovieFacade(emf);

    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();

            em.persist(A);
            em.persist(B);
            em.persist(C);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method
    @Test
    public void testGetMovieCount() {
        assertEquals(3, facade.getMovieCount(), "Expects three rows in the database");
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = facade.getAll();
        assertThat(movies, containsInAnyOrder(A, B, C));
    }

    @Test
    public void testGetMovieById() {
        Movie movie = facade.getMovie(A.getId());
//        System.out.println(movie.getName());
        assertThat(movie.getActors()[0], containsString("Kirk"));
    }

    @Test
    public void testGetMovieByName() {
        Movie mov = facade.getMovie(B.getName());
        assertEquals(mov, B);
    }

    @Test
    public void testMovieHasActors() {
        Movie movie = facade.getMovie(B.getId());
        assertThat(movie.getActors(), arrayContaining("Bill Murray", "Owen Wilson", "Anjelica Huston"));
    }

}
