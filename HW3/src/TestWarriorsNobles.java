
public class TestWarriorsNobles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Noble art = new Noble("King Arthur");
	        Noble lance = new Noble("Lancelot du Lac");
	        Noble jim = new Noble("Jim");
	        Noble linus = new Noble("Linus Torvalds");
	        Noble billie = new Noble("Bill Gates");

	        Warrior cheetah = new Warrior("Tarzan", 10);
	        Warrior wizard = new Warrior("Merlin", 15);
	        Warrior theGovernator = new Warrior("Conan", 12);
	        Warrior nimoy = new Warrior("Spock", 15);
	        Warrior lawless = new Warrior("Xena", 20);
	        Warrior mrGreen = new Warrior("Hulk", 8);
	        Warrior dylan = new Warrior("Hercules", 3);

	        jim.hire(nimoy);
	        lance.hire(theGovernator);
	        art.hire(wizard);
	        lance.hire(dylan);
	        linus.hire(lawless);
	        billie.hire(mrGreen);
	        art.hire(cheetah);

	        System.out.println(jim);
	        System.out.println(lance);
	        System.out.println(art);
	        System.out.println(linus);
	        System.out.println(billie);
	 
	        cheetah.runaway();
	        System.out.println(art);

	        art.battle(lance);
	        jim.battle(lance);
	        linus.battle(billie);
	        billie.battle(lance);
	}

}
