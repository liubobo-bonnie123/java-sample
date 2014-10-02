
public class Basic {
	private String name;
	private boolean isDead;
	public Basic(String name){
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
