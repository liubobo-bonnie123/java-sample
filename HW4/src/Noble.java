import java.util.*;
public abstract class Noble extends Person{
	
	public Noble(String name) {
		super(name);
	}
	public abstract float getStrength();
	public abstract void setStrength(float strengthRatio);
	public abstract void fight();

	
	public void battle(Noble enemy){
		System.out.println(getName() + " battles " + enemy.getName());
		
		if (isDead() && enemy.isDead())
			System.out.println("Oh, NO! They're both dead! Yuck!");
		else
			if (isDead() || enemy.isDead())
				System.out.println("He's dead, " + (isDead()?enemy.getName():getName()));
			else{
				fight();
				enemy.fight();				
				float strenghtDifference = getStrength() - enemy.getStrength();

				if (strenghtDifference > 0){//win
					setStrength(enemy.getStrength() / getStrength());					
					enemy.kill();
					System.out.println(getName() + " defeats " + enemy.getName());
				}
				else
					if(strenghtDifference < 0){//loss
						enemy.setStrength(getStrength() / enemy.getStrength());
						this.kill();
						System.out.println(enemy.getName() + " defeats " + getName() + "!");
					}
					else{//both dead
						this.kill();
						enemy.kill();
						System.out.println("Mutual Annihilation" + getName() + " and " + enemy.getName() + " die at each other's hands!");

					}
			}
	}
}
