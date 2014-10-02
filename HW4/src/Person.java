
public class Person {
	private String name;
	private boolean isDead;
	public Person(String name){
		this.name = name;
	}
	public void kill( ){
		isDead = true;
	}
	public String toString(){
		return name;
	}
	public boolean isDead(){
		return isDead;
	}
	public String getName(){
		return name;
	}
	
}
