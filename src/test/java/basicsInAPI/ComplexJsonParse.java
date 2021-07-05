package basicsInAPI;

import files.Payload;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

public class ComplexJsonParse {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(Payload.CoursePrice());

		// print no of course returned by API
		int count = js.getInt("courses.size");

		System.out.println(count);
		
		//Print Purchase Amount

		int purchaseAmount = js.getInt("dashboard.purchaseAmount");

		System.out.println(purchaseAmount);
		
		//Print Title of the first Course

		String titleFirstCourse = js.get("courses[0].title");

		System.out.println(titleFirstCourse);

		// Print All course titles and their respective Prices

		for (int i = 0; i < count; i++) {

			String courseTitles = js.get("courses[" + i + "].title");
			int coursePrice = js.get("courses[" + i + "].price");
			System.out.println(courseTitles);
			System.out.println(coursePrice);
		}

		System.out.println("Print no of copies sold by RPA Course");

		for (int i = 0; i < count; i++) {

			String title = js.get("courses[" + i + "].title");

			if (title.equalsIgnoreCase("RPA")) {

				int copies = js.getInt("courses[" + i + "].copies");

				System.out.println(copies);
				break;
			}
		}

		System.out.println("=======================SumValidation=========================");

		int actualAmount = 0;
		for (int i = 0; i < count; i++) {

			int price = js.getInt("courses[" + i + "].price");
			int copies = js.getInt("courses[" + i + "].copies");
			int finalAmount = price * copies;
			System.out.println(finalAmount);
			actualAmount = actualAmount + finalAmount;

		}

		System.err.println("Final purchasing Amount "+actualAmount);

		int purcaseAmount = js.getInt("dashboard.purchaseAmount");

		System.err.println(purcaseAmount);

		Assert.assertEquals(purcaseAmount, actualAmount);
	}

}
