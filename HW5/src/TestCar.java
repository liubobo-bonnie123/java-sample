
public class TestCar   {

	public static void main(String[] args) throws CloneNotSupportedException {
		Car car = new Car("NYU",5);					//construct a 5 wheels car with brand "NYU" 
		Car car2 = (Car) car.clone();				//make a copy of that car to car2
		Car car3 = (Car) car.clone();				//make another copy to car3
		System.out.println(car.equals(car2));		//car and car2 are same
		car2.removeEngine();						//remove the engine of car2
		car2.assembleEngine(new Engine(150));		//add a powerfull engine to car2
		System.out.println(car.equals(car2));		//now car and car2 are no long same
		System.out.println(car.compareTo(car2));    //car2 is 50 faster than car
		car3.removeWheel(3);						//remove the the wheel at position3 of car3 
		car3.assembleWheel(3, new Wheel("Poly"));	//change it with another wheel with brand "Poly"
		System.out.println(car.compareTo(car3));	//car and car3 have same speed
		System.out.println(car.equals(car3));		//but they are still different
		car3.removeWheel(3);						//remove the wheel of position 3 of car3	
		car3.assembleWheel(3, new Wheel("NYU"));	//change it with a new wheel with brand "NYU"
		System.out.println(car.equals(car3));		//now they are exactly the same
	}

}
