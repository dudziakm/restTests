package com.rest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;

public class RestTest {
    @Before
    public void setup() {
        //baseURI = "https://api.github.com";
        baseURI = "https://jsonplaceholder.typicode.com/";
        port = 443;
    }
    @Ignore
    @Test
    public void whenRequestGet_thenOK(){
        when().request("GET", "/users/eugenp").then().statusCode(200);
    }

    @Test
    public void shouldGetHighestUserId(){
        List<Integer> userIds = when().request("GET", "posts/").then().extract().path("userId");
        System.out.println("User IDs: " + userIds);

        int maxUserId = Collections.max(userIds);
        System.out.println("Max ID: " + maxUserId);
    }

    @Test
    public void shouldGetHighestId(){
        List<Integer> userIds = when().request("GET", "posts/").then().extract().path("userId");
        System.out.println("User IDs: " + userIds);
        Set<Integer> setIds = new HashSet<>(userIds);
        System.out.println("Set User IDs: " + setIds);

        for (Integer uId : setIds) {
            List<Integer> ids = when().request("GET", "/posts?userId="+uId).then().extract().path("id");
            System.out.println("For userId number: "+ uId+ " list of IDs: " + ids);

            int maxId = Collections.max(ids);
            System.out.println("Max ID in this group: " + maxId);
        }


    }

    @Test
    public void shouldGetMaxIdForMaxUserId(){
        List<Integer> userIds = when().request("GET", "posts/").then().extract().path("userId");
        int maxUserId = Collections.max(userIds);
        List<Integer> ids = when().request("GET", "/posts?userId="+maxUserId).then().extract().path("id");
        int maxId = Collections.max(ids);
        System.out.println("Max Id for Max UserID: " + maxId);
    }
}
