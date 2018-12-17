import io.qameta.allure.*;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import junit.framework.TestListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


@Epic("Some epic mane")
@Feature("Some feature")

public class restAssuredTest {


    String successCode;
    String BaseUrl = "https://dev-lt-portal.tk:50443/api/auth/login/";
    String PostMessageUrl = "https://dev-lt-portal.tk:50443/api/messages/";
    JSONObject requestBody = new JSONObject();
    int pollId;

    @Test (priority = 1, description= "Login Scenario with valid username and password.")
    @Severity(SeverityLevel.MINOR)
    @Description("Test description: Login with a non admin user")
    @Story("Some story")

    public void Login()
    {

        requestBody.put("email", "feriksatan@gmail.com");
        requestBody.put("password", "12qw!@QW");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());
        Response response = request.post(BaseUrl);

        int statusCode = response.getStatusCode();

        System.out.print(requestBody);
        System.out.println(response.getBody().asString());
        System.out.println(successCode);
        Headers allHeaders = response.headers();
        String bodyAsString = response.getBody().asString();
        Assert.assertEquals(bodyAsString.toLowerCase().contains("user"), true);
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(response.jsonPath().get("user.id").toString(), "398");
    }
    @Test
    public void CretePollMessage()
    {

        JSONArray obj = new JSONArray();
        for(int i = 0; i < 3; i++) {
            JSONObject list1 = new JSONObject();
            list1.put("value","null" + i);
            obj.put(list1);
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("message_type", "poll");
        requestBody.put("options", obj);

        requestBody.put("poll_type", "public");
        requestBody.put("poll_end_date","2018-12-18");
        requestBody.put("text", "poll text string");
        requestBody.put("title", "rest assured poll delete");


        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json")
                .header("Authorization", "token " + "f8e6e93dd349db251516f7845f7aed51ba8b9d02")
                .header("Cookie", "sessionid=pnw1ku9f11m8m2jtz031gb4lvci2nczn");


        request.body(requestBody.toString());
        Response response = request.post(PostMessageUrl);
        pollId =   response.jsonPath().getInt("id");
        System.out.print(requestBody +  "                   ");
        System.out.println(response.getBody().asString());
        
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);
    }
    @Test
    public void GetMessages()
    {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json")
                .header("Authorization", "token " + "f8e6e93dd349db251516f7845f7aed51ba8b9d02");
        Response response = request.get(PostMessageUrl);
        System.out.print(response.getBody().asString());
        String bodyAsString = response.getBody().asString();
        Assert.assertEquals(bodyAsString.toLowerCase().contains("count"), true);
        //Assert.assertEquals(bodyAsString.toLowerCase().contains("next"), true);
        //Assert.assertEquals(bodyAsString.toLowerCase().contains("results"), true);
        Assert.assertEquals(response.getStatusCode(), 200);

    }


    
    @Test
    public void DeleteMessage()
    {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json")
                .header("Authorization", "token " + "f8e6e93dd349db251516f7845f7aed51ba8b9d02");
        Response response = request.delete(PostMessageUrl + pollId + "/");
        Assert.assertEquals(response.getStatusCode(), 204);

    }
}