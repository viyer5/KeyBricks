package code.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import code.fileIO.FileIO;

public class RestoreEventHandler implements ActionListener {
	
private JFrame _win;
	
	public RestoreEventHandler(JFrame w){
		_win = w;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		FileIO.readFileToString("viyer5.kbr");
	}

}
