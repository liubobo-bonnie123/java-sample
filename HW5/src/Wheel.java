
public class Wheel implements Comparable<Wheel>{

	public Wheel() {
	}
	public Wheel(String brand){
		this.brand = brand;
	}
	public Wheel(String brand, int position){
		this.brand = brand;
		this.position = position;
	}
	public Wheel(String brand, int position, int radius){
		this.brand = brand;
		this.radius = radius;
		this.position = position;
	}
	public int getPosition(){
		return position;
	}
	public void setPosition(int position){
		this.position = position;
	}
	private String brand = "Poly";
	private int radius = 100;
	private int position = -1;
	public String toString(){
		return 	"\t" + position + "\t\t" + brand + "\t" + radius + "\n";
	}
	public boolean equals(Wheel otherWheel){
		if (this.brand.equals(otherWheel.brand))
			if (this.radius == otherWheel.radius)
				return true;
			else
				return false;
		else
			return false;
	}
	@Override
	public int compareTo(Wheel otherWheel) {
		return this.position - otherWheel.position;
	}
}
