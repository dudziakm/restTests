package com.rest;

import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;


public class RestTest {
    @Before
    public void setup() {
        baseURI = "https://api.github.com";
        port = 443;
    }
    @Test
    public void whenRequestGet_thenOK(){
        when().request("GET", "/users/eugenp").then().statusCode(200);
    }
}
