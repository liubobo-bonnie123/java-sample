import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
public class Life {
	private int row = 0,col = 0,generation;
	private int mat[][];
	private String filename;
	public Life() throws IOException{
		this.generation = 10;
		this.filename = "life.txt";
	
	}
	public Life(int generation, String filename) throws IOException{
		this.filename = filename;
		this.generation = generation;

	}
	public Life(int generation) throws IOException{
		this.generation = generation;
		this.filename = "life.txt";

	}
	
	public Life readFile() throws IOException{

		Scanner scan = new Scanner(new File(this.filename));
		if(scan.hasNextInt()){
			this.row = scan.nextInt();
			//System.out.println("Row:" + "\t"+this.row);
		}
		
		if(scan.hasNextInt()){
			this.col = scan.nextInt();
			//System.out.println("Column:" + "\t" + this.col);
		}
		
		mat = new int[this.row+2][this.col+2];
		
			
		for (int i=0; i<this.row; ++i){
			if (scan.hasNextLine()) {
				String line = scan.nextLine();
				for (int j=0; j<line.length(); ++j){
					if (line.charAt(j) == '*')
						this.mat[i+1][j+1] = 1;
				}
			}
		}
			
		scan.close();
		System.out.println("Generation: 1");
		this.print();
		return this;
	}
	
	public void print(){
		for(int i=1; i<this.row+1; i++){
			for(int j=1; j<this.col+1; j++)
				switch (this.mat[i][j]){
				case 1: System.out.print('*');break;
				default: System.out.print('-');
				}
			System.out.println();
		}
			
				
	}
	
	public void evolution(){
		//this.generation = generation;
		for (int i=2; i<=this.generation; ++i){
			int nextGenMat[][] = new int[this.row+2][this.col+2];;
			for (int j=1; j<this.row+1; j++){
				for (int k=1; k<this.col+1; k++){
					int neighboreNum =	 this.mat[j-1][k-1]
										+this.mat[j-1][k]
										+this.mat[j-1][k+1]
										+this.mat[j][k-1]
										+this.mat[j][k+1]
										+this.mat[j+1][k-1]
										+this.mat[j+1][k]
										+this.mat[j+1][k+1];
					switch (neighboreNum){
						case 3: nextGenMat[j][k] = 1;break;
						case 2: nextGenMat[j][k] = this.mat[j][k]; break;
						
					}
				}
			}
			for (int j=0; j<this.row+2; ++j){
				this.mat[j] = Arrays.copyOf(nextGenMat[j], this.col+2);
			}
			System.out.println("=========================");
			System.out.println("Generation: " + i);
			this.print();
		}
		
	}
	
	public static void main(String[] args) throws IOException{
		Life life = null;
		int generation;
		switch(args.length){
			case 0:	life = new Life();	break;
			case 1: generation = Integer.valueOf(args[0]); life = new Life(generation);	break;
			case 2: generation = Integer.valueOf(args[0]); life = new Life(generation, args[1]);break;
			default: System.out.println("Too many arguments");
		}
		life.readFile();
		life.evolution();
	}
}
