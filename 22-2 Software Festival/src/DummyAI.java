import java.util.Scanner;

public class DummyAI {
	
	static int mine;
	static int opponent;
	
	
	//main method
	public static void main(String[] args) throws Exception {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Input the ip address > ");
		String ip = scanner.nextLine();
		System.out.print("Input the port number > ");
		int port = Integer.parseInt(scanner.nextLine());
		System.out.print("Input the color > ");
		String color = scanner.nextLine();
		
		//베타고에서 사용하기 위한 머시깽이... ----------------------------------------

		if (color.toLowerCase().compareTo("white") == 0) {
			mine = 2;
			opponent = 1;
		} else if (color.toLowerCase().compareTo("black") == 0) {
			mine = 1;
			opponent = 2;
		} 
		//----------------------------------------------------------------
		
		ConnectSix conSix = new ConnectSix(ip, port, color); //making instance of ConnectSix as name conSix
		System.out.println("Red Stone positions are " + conSix.redStones);
		
		//if AI choose black, start with one stone at the middle of the board.
		if (color.toLowerCase().compareTo("black") == 0) {
			String first = conSix.drawAndRead("K10");
		} else if (color.toLowerCase().compareTo("white") == 0) {
			String first = conSix.drawAndRead("");
		}
		
		while (true) {
			String draw;
			
			/*
			char alpha1 = (char) ((Math.random() * 19) + 'A');
			int num1 = (int)( Math.random() * 19) + 1;
			char alpha2 = (char) ((Math.random() * 19) + 'A');
			int num2 = (int)( Math.random() * 19) + 1;
			draw = String.format("%c%02d:%c%02d", alpha1, num1, alpha2, num2);
			*/
			
			draw = Betago.returnStringCoor();
			
			String read = conSix.drawAndRead(draw);
			
			if(read.compareTo("WIN") == 0 || read.compareTo("LOSE") == 0 || read.compareTo("EVEN") == 0) {
				 break;
			}
		}

	}
	
	// return the AI color. black = 1, white = 2, red = 3, empty = 0
	public static int getMyColor() {
		return mine;
	}
	// return the person color. black = 1, white = 2, red = 3, empty = 0
		public static int getYourColor() {
			return opponent;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}