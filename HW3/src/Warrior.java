import java.util.*;
public class Warrior extends Basic{
	private float strength;
	private Noble employer;
	public Warrior(String name) {
		super(name);
	}
	public Warrior(String name, float strength){
		super(name);
		this.strength = strength;
	}
	
	public String toString(){
		return getName() + ": " + strength;
	}

	public float getStrength(){
		return strength;
	}
	public void setStrength(float strengthRatio){
		strength = strength*(1-strengthRatio);
	}
	public boolean setEmployer(Noble aNoble){
		if (isDead()==false){
			employer = aNoble;
			return true;
		}
		else
			return false;
	}
	public void runaway(){
		if(employer!=null && isDead()==false){
			System.out.println("So long " + employer.getName() + ". I'm out'a here! -- " + getName());
			Noble exEmployer = employer;
			employer = null;
			exEmployer.fire(this);
		}
			
	}
	public void kill(){
		super.kill();
		strength = 0;
	}
}
