package jsonManupulation;

import java.util.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class comparejsonswithDiffertnOrder {
	public void comparejsonswithDiffertnOrder1(String a, String b) {
		// by jackson Api
		ObjectMapper om = new ObjectMapper();
		try {
			Map<String, Object> m1 = om.readValue(a, Map.class);
			Map<String, Object> m2 = om.readValue(b, Map.class);
			System.out.println(m1);
			System.out.println(m2);
			System.out.println(m1.equals(m2));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void comparejsonswithDiffertnOrder2() {

		String A = "{a : {a : 2}, b : 2}";
		String B = "{b : 2, a : {a : 2}}";
		// by gson Api
		JsonParser parser = new JsonParser();
		JsonElement o1 = parser.parse(A);
		JsonElement o2 = parser.parse(B);
		Assert.assertEquals(o1, o2);

	}

	public static void main(String... args) {
		String input1 = "{\"state\":1,\"cmd\":1}";
		String input2 = "{\"cmd\":1,\"state\":1}";
		float t = 5.5378273872835f;
		double k = 5.5378273872835d;
		System.out.println(t);
		System.out.println(k);
		comparejsonswithDiffertnOrder obj = new comparejsonswithDiffertnOrder();
		 obj.comparejsonswithDiffertnOrder1(input1, input2);
		//obj.comparejsonswithDiffertnOrder2();

	}
}