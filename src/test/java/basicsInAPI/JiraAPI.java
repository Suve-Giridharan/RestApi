package basicsInAPI;

import org.testng.AssertJUnit;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.apache.http.client.params.AllClientPNames;

public class JiraAPI {

	public static void main(String[] args) {

		RestAssured.baseURI = "http://localhost:8080/";

		// login Scenario

		SessionFilter session = new SessionFilter();

		String loginResponse = given().relaxedHTTPSValidation().log().all().header("content-Type", "application/json")
				.body("{ \"username\": \"megiridharan74\", \"password\": \"Suve_Classmate5\" }").filter(session).when()
				.post("rest/auth/1/session").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		String expectedMsg = "Thaniega is So Beautiful Baby";

		// Add Comment

		String addCommentResponse = given().pathParam("id", "10201").log().all()
				.header("content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"" + expectedMsg + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("rest/api/2/issue/{id}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();

		JsonPath jsp = new JsonPath(addCommentResponse);
		
		String commentID = jsp.getString("id");
		
		System.err.println(commentID);

		// Add Attachment
		given().pathParam("id", "10201").filter(session).header("X-Atlassian-Token", "no-check")
				.header("content-Type", "multipart/form-data")
				.multiPart("file", new File("C:\\Users\\Giridharan\\eclipse-workspace\\com.Sc.Testing\\Jira.txt"))
				.when().post("rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);

		// Get Issue
		String issueResponse = given().filter(session).log().all().pathParam("id", "10201")
				.queryParam("fields", "comment").when().get("rest/api/2/issue/{id}").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		System.err.println(issueResponse);

		JsonPath json = new JsonPath(issueResponse);
		int commentsCount = json.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentsCount; i++) {

			String jsonID = json.get("fields.comment.comments[" + i + "].id").toString();
			System.out.println(jsonID);

			if (commentID.equalsIgnoreCase(jsonID)) {

				String body = json.get("fields.comment.comments[" + i + "].body").toString();

				System.out.println(body);

				AssertJUnit.assertEquals(expectedMsg, body);
			}

		}

	}

}
