package jsonManupulation;

import com.google.gson.Gson;

public class GsonTest {

	public static void main(String[] args) {

				Car car = new Car();
				car.setBrand("Rover");
				car.setDoors(3);
				car.setWin(6);
				

		System.out.println(car.getBrand()+car.getDoors()+car.getWin());
		Gson gson = new Gson();

		String json = gson.toJson(car);
	//	String json = gson.fromJson(json, typeOfT)
		System.out.println(json);

		
	}
}
	 


