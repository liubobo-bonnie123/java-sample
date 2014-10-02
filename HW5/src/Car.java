import java.util.ArrayList;


public class Car implements Cloneable,Comparable <Car>{
	private String brand = "Poly";
	private int wheelNum = 4;
	private Engine engine ;
	private ArrayList<Wheel> wheel = new ArrayList(); ; 

	public Car() {
		assembleCar();
	}
	public Car(String brand){
		this.brand = brand;
		assembleCar();
	}
	public Car(int wheelNum){
		this.wheelNum = wheelNum;
		assembleCar();
	}
	public Car(String brand, int wheelNum){
		this.wheelNum = wheelNum;
		this.brand = brand;
		assembleCar();
	}
	public void assembleWheel(){
		for(int i=wheel.size()+1; i<= wheelNum; i++)
			wheel.add(new Wheel(brand,i));
	}
	public boolean assembleWheel(int position, Wheel newWheel){
		System.out.println("Adding wheel to position " + position + "...");
		if (position > wheelNum){
			System.out.println("Can't add wheel: no such position");
			return false;
		}
		for(Wheel w: wheel){
			if (w.getPosition() == position){
				System.out.println("Can't add wheel: non-empty position");
				return false;
			}
		}
		newWheel.setPosition(position);
		wheel.add(position-1, newWheel);
		System.out.println("A wheel was added to position " + position);
		return true;
	}
	public boolean assembleEngine(){
		if (engine == null){
			engine = new Engine(brand);
			return true;
		}
		else
			return false;
	}
	public boolean assembleEngine(Engine engine){
		if (this.engine == null){
			this.engine = engine;
			return true;
		}
		else
			return false;
	}
	public void assembleCar(){
		assembleEngine();
		assembleWheel();
	}
	public boolean removeEngine(){
		if(engine == null){
			return false;		
		} 
		else{
			engine = null;
			return true;
		}
	}
	public boolean removeWheel(int position){
		for(Wheel w: wheel){
			if (w.getPosition() == position){
				wheel.remove(w);
				return true;
			}
		}
		return false;
	}
	public String toString(){
		return 	"======================================\n" + 
				"Engine:\n" + ((engine == null)?"\tNone\n":engine) + 
				"\n" + 
				"Wheel:\t" + wheel.size() + " of total " + wheelNum +"\n" + 
				"\t" + "position" + "\t" + "brand" + "\t" + "radius" + "\n" +
				wheel.toString().replace(",","").replace("[","").replace("]","") +
				"======================================";
	}
	public int hashCode(){
		return wheel.hashCode() + engine.hashCode() ;
	}
	@Override
	public int compareTo(Car otherCar) {
		return this.engine.getHorsepower() - otherCar.engine.getHorsepower();
	}
	public boolean equals(Car otherCar){
		if (this.wheel.size()!=otherCar.wheel.size())
			return false;
		for (int i=0; i<wheel.size(); i++){
			if (!this.wheel.get(i).equals(otherCar.wheel.get(i)))
				return false;
		}
		if (!this.engine.equals(otherCar.engine)) 
			return false;
		else
			return true;
	}
	public Car clone() throws CloneNotSupportedException{
		Car result = (Car) super.clone();
		result.wheel = (ArrayList<Wheel>) result.wheel.clone();
		return result;
	}
	
}
