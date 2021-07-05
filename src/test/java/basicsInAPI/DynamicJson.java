package basicsInAPI;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;

public class DynamicJson {

	@Test(dataProvider = "bookData")
	public void addBook(String ispn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";

		String response = given().log().all().header("Content-Type", "application/json")
				.body(Payload.Addbook(ispn, aisle)).when().post("/Library/Addbook.php").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		System.err.println(response);
		JsonPath js = new JsonPath(response);
		String ADDBookID = js.getString("ID");
		System.err.println(ADDBookID);

		String DeleteResponse = given().log().all().header("Content-Type", "application/json")
				.body(Payload.DeleteBook(ADDBookID)).when().post("/Library/DeleteBook.php").then().log().all()
				.assertThat().statusCode(200).extract().response().asString();

		JsonPath js1 = new JsonPath(DeleteResponse);
		String DeleteBookID = js1.getString("msg");
		System.err.println(DeleteBookID);
	}

	@DataProvider(name = "bookData")
	public Object getData() {

		// Array = Collection of Components/Elements
		// multidimensional Array = Collection of Arrays
		Object obj = new Object[][] { { "Suvadha", "8220" }, { "Mukesh", "8344" }, { "Priya", "9890" } };
		return obj;
	}

	

}