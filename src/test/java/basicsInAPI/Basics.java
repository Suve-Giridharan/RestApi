package basicsInAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;

import files.Payload;
import files.ReusableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {

		// Validate the Add place API is working as expected

		// given() --> All INput details
		// when() ---> Submit the APIs Resources and HTTP Method
		// Then() ---> Validate the response
		// Content of the File to String ----> Content of the file can Convert into
		// Byte----> Byte data to String

   		RestAssured.baseURI = "https://rahulshettyacademy.com";

		String response = given().log().all().queryParam("Key", "qaclick123")
				.headers("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Giridharan\\Desktop\\FilePath.txt")))).when()
				.post("/maps/api/place/add/json").then().log().all().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract().response()
				.asString();

		System.out.print(response);

		// Add Place --->Update Place with New Address --> Get Place to validate if New
		// address is present
		// in the response
		// JsonPath ---> this class takes the input as string and Convert it as Json and
		// finally it will helps to pass the Json

		JsonPath js = new JsonPath(response);
		String placeId = js.getString("place_id");

		System.out.println(placeId);

		// Update Place

		String newAddress = "70 winter walk, Africa";

		given().log().all().queryParam("key", "qaclick123").headers("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + "")
				.when().put("/maps/api/place/update/json").then().assertThat().log().all().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Get Place

		String getPlace = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).when()
				.get("/maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract().response()
				.asString();

		System.out.println(getPlace);

		JsonPath j = new JsonPath(getPlace);

		String string = j.getString("address");

		System.err.println(string);

		Assert.assertEquals(string, newAddress);

	}

}
