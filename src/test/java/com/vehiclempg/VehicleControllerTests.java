package com.vehiclempg;

import com.vehiclempg.models.Vehicle;
import com.vehiclempg.repositories.VehicleRepository;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class VehicleControllerTests {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        // this should be in @BeforeClass but vehicleRepository cannot be in a static method
        vehicleRepository.deleteAll();

        Vehicle acura = new Vehicle(30, 22, 26, "Acura", "Integra", 2000, 4, 2.0f, "Regular");
        Vehicle ford = new Vehicle(30, 22, 26, "Ford", "Mustang", 2006, 8, 4.6f, "Premium");
        Vehicle bmw = new Vehicle(30, 22, 26, "BMW", "M3", 2010, 8, 4.0f, "Premium");

        vehicleRepository.save(Arrays.asList(acura, ford, bmw));

        // RestAssured setup
        RestAssured.port = port;
    }

    @After
    public void tearDown() {
        // remove all test vehicles
        // this should be in @AfterClass but vehicleRepository cannot be in a static method
        vehicleRepository.deleteAll();
    }

    @Test
    public void testAllEmpty() {
        // delete all to see status code
        vehicleRepository.deleteAll();

        when().
                get("/vehicles/all").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testAllNotEmpty() {

        when().
                get("/vehicles/all").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(3));
    }

    @Test
    public void testGetMake() {

        when().
                get("/vehicles/make?make=BMW").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(1));
    }

    @Test
    public void testGetModel() {

        when().
                get("/vehicles/model?model=M3").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(1))
                .body("[0].model", equalTo("M3"));

    }

    @Test
    public void testGetModelEmpty() {

        when().
                get("/vehicles/model?model=Escalade").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }


    @Test
    public void testGetYear() {

        when().
                get("/vehicles/year?year=2000").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("[0].make", equalTo("Acura"))
                .body("size()", is(1));
    }

    @Test
    public void testGetYearEmpty() {

        when().
                get("/vehicles/year?year=1991").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testGetYears() {

        when().
                get("/vehicles/years?from=2005&to=2007").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(1));
    }

    @Test
    public void testGetYearsEmpty() {

        when().
                get("/vehicles/years?from=2010&to=2015").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testGetCylinders() {

        when().
                get("/vehicles/cylinders?cylinders=8").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(2));
    }

    @Test
    public void testGetCylindersEmpty() {

        when().
                get("/vehicles/cylinders?cylinders=6").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testGetFuelType() {

        when().
                get("/vehicles/fuel?type=Regular").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(1));
    }

    @Test
    public void testGetFuelTypeEmpty() {

        when().
                get("/vehicles/fuel?type=MidGrade").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testFilterWithAll() {

        when().
                get("/vehicles/filter?make=Ford&from=2005&to=2007").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("[0].make", equalTo("Ford"));
    }

    @Test
    public void testFilterWithMake() {

        when().
                get("/vehicles/filter?make=Acura").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("[0].make", equalTo("Acura"));
    }

    @Test
    public void testFilterWithFromYear() {

        when().
                get("/vehicles/filter?from=2008").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("[0].make", equalTo("BMW"))
                .body("size()", is(1));
    }

    @Test
    public void testFilterWithToYear() {

        when().
                get("/vehicles/filter?to=2000").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("[0].make", equalTo("Acura"))
                .body("size()", is(1));
    }

    @Test
    public void testEmptyFilter() {

        when().
                get("/vehicles/filter").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(3));
    }

    @Test
    public void testFilterNoResults() {

        when().
                get("/vehicles/filter?to=1900").
                then().
                statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void testGetMakes() {

        when().
                get("/vehicles/makes").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(3))
                .body("", hasItems("Acura", "BMW", "Ford"));
    }

    @Test
    public void testAverages() {
        when().
                get("/vehicles/averages?makes=Acura,BMW&type=averageMPG").
                then().
                statusCode(HttpStatus.SC_OK)
                .body("isEmpty()", is(false))
                .body("size()", is(2));
    }
}

