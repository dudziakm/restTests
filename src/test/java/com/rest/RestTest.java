package com.rest;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.specification.ProxySpecification.host;

public class RestTest {
    @Before
    public void setup() {
        baseURI = "https://jsonplaceholder.typicode.com/";
        port = 443;

        // For debugging and testing with Fiddler:
        //RestAssured.proxy = host("127.0.0.1").withPort(8888);
        // great option to manage lack of certification for https decryption in fiddler:
        //RestAssured.useRelaxedHTTPSValidation();
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

    @Test
    public void shouldAddCommentToMaxId(){
        EncoderConfig encoderconfig = new EncoderConfig();

        List<Integer> userIds = when().request("GET", "posts/").then().extract().path("userId");
        int maxUserId = Collections.max(userIds);
        List<Integer> ids = when().request("GET", "/posts?userId="+maxUserId).then().extract().path("id");
        int maxId = Collections.max(ids);
        System.out.println("Max Id for Max UserID: " + maxId);

        given().config(RestAssured.config().encoderConfig(encoderconfig.appendDefaultContentCharsetToContentTypeIfUndefined(false))).param("postId", "100").header("charset","utf-8f").log().all()
                .when().post("/comments?postId=" + maxId)
                .then().assertThat().statusCode(201); //created
    }
}
