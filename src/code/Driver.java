package code;

import javax.swing.SwingUtilities;

import code.fileIO.FileIO;
import code.gui.KeyBricksGUI;
import code.model.KeyBricksModel;

public class Driver {
	private static KeyBricksModel _km;
	
	public Driver( KeyBricksModel m)
	{
		_km = m;
	}
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new KeyBricksGUI(new KeyBricksModel()));	
	
		/*int length = args.length;
		//String arg0 = args[0];
		
		for ( int i=0; i<args.length;i++){
			System.out.println(i+ ") "+ args[i]);
			
			if (args.length ==0){
			//start a fresh gane
				
			}
			else {
			//restore from save file
				//String s = _km.abc();
				SwingUtilities.invokeLater(new KeyBricksGUI(new KeyBricksModel()));	
				//arg0 = FileIO.readFileToString(args[0]);
				//restore(arg0);
			}
		}*/
	}
	private static void restore(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
