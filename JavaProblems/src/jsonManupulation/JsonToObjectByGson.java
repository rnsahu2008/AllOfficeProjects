package jsonManupulation;

import com.google.gson.Gson;

public class JsonToObjectByGson 
{
	public static void main(String[] args) {
		
	String json = "{\"brand\":\"Jeep\", \"doors\": 3,\"win\": 5}";
	Gson gson = new Gson();
	Car car = gson.fromJson(json, Car.class);
	System.out.println(car.getDoors()+ " "+ car.getBrand()+" "+car.getWin()+"  ");

}
}

