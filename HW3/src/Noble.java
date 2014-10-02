import java.util.*;
public class Noble extends Basic{
	private ArrayList<Warrior> employee = new ArrayList();
	public Noble(String name) {
		super(name);
	}
	public String toString(){
		return getName() + " has an army of " + employee.size() + "\n\t" + employee;
	}
	public void hire (Warrior aWarrior){
		if(aWarrior.setEmployer(this))
			employee.add(aWarrior);
		else
			System.out.println(aWarrior.getName() + " is dead, cannot hire him");
	}
	public void fire (Warrior aWarrior){
		if (employee.remove(aWarrior))
			aWarrior.runaway();
	}

	public float getStrength(){
		int totalStrength = 0;
		for(Warrior aWarrior:employee)
			totalStrength += aWarrior.getStrength();
		return totalStrength;
	}
	public void setStrength(float strengthRatio){
		for(Warrior aWarrior:employee)
			aWarrior.setStrength(strengthRatio);
	}
	public void kill(){
		super.kill();
		for(Warrior aWarrior:employee)
			aWarrior.kill();
	}
	
	public void battle(Noble enemy){
		System.out.println(getName() + " battles " + enemy.getName() + "...");
		if (isDead() && enemy.isDead())
			System.out.println("Oh, NO! They're both dead! Yuck!");
		else
			if (isDead() || enemy.isDead())
				System.out.println("He's dead, " + (isDead()?getName():enemy.getName()) + "!");
			else{
				float strengthRatio = enemy.getStrength() / getStrength();
				if (strengthRatio<1){//win
					System.out.println(getName() + " defeats " + enemy.getName() + "!");
					enemy.kill();
					setStrength(strengthRatio);
				}
				else
					if(strengthRatio>1){//loss
						System.out.println(enemy.getName() + " defeats " + getName() + "!");
						this.kill();
						enemy.setStrength(strengthRatio);
					}
					else{//both dead
						System.out.println("Mutual Annihilation" + getName() + " and " + enemy.getName() + " die at each other's hands!");
						this.kill();
						enemy.kill();
					}
			}
	}
}
