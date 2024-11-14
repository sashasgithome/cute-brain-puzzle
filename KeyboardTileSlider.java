//Class   : AP Programming II
//Name    : Sasha A.
//Project : Keyboard Slider GUI Project
//Details : GUI project where user clicks up/down/right/left
//to move Totoro until numbers on the board are in order. 
//Uses an inner class, utilizes JFrame, gridlayout, PicPanel, 
//and implements KeyListener.

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;

public class KeyboardTileSlider extends JFrame implements KeyListener {

	private BufferedImage image;

	private PicPanel[][] allPanels;

	private int totRow;	// location of Totoro
	private int totCol;
	
	private int moves;  //track Totoro's moves --> an added instance variable

	public KeyboardTileSlider(){
		
		//main frame basics
		setSize(375,375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tile Slider");
		getContentPane().setBackground(Color.black);

		//creates board where Totoro moves
		allPanels = new PicPanel[4][4];
		setLayout(new GridLayout(4,4,2,2));
		setBackground(Color.black);

		//checks image
		try {
			image = ImageIO.read(new File("totoro.jpg"));
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Could not read in the pic");
			System.exit(0);
		}	
		
		//generates 16 numbers for board in random order
		ArrayList<Integer> labels = new ArrayList<Integer> (16);
		int i = 16;
		for(; i >= 1; i--) {
		
			//finds one random number at a time
			int num;
			do {
				num = (int) (Math.random()*16) + 1;
			}while(labels.indexOf(num) != -1);
				
			labels.add(num);
		}
		
		//fills panels for the board & adds each panel to frame
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				
				allPanels[r][c] = new PicPanel ();
				add(allPanels[r][c]);
				
				//places either a number or Totoro
				allPanels[r][c].setNumber(labels.get(i));
				if(labels.get(i) == 16) {
					allPanels[r][c].removeNumber();
					totRow = r;
					totCol = c;
				}
				
				i++;
			}
		}
		
		moves = 0;		
		this.addKeyListener(this);
		setVisible(true);
	}
	
	
	@Override // when user moves totoro to a different location
	public void keyPressed(KeyEvent arg) {
		
		int newRow = totRow;
		int newCol = totCol;
		
		//finds new Totoro position
		if(arg.getKeyCode() == KeyEvent.VK_LEFT)
			newCol--;
		else if(arg.getKeyCode() == KeyEvent.VK_RIGHT)
			newCol++;
		else if(arg.getKeyCode() == KeyEvent.VK_UP)
			newRow--;
		else if(arg.getKeyCode() == KeyEvent.VK_DOWN)
			newRow++;
		
		//checks for out of bounds
		if(newRow < 0 || newRow > 3 || newCol < 0 || newCol > 3) {	
			return;
		}
		
		//switches Totoro with the number
		allPanels[totRow][totCol].setNumber(allPanels[newRow][newCol].number);
		allPanels[newRow][newCol].removeNumber();
		moves++;
		
		totRow = newRow;
		totCol = newCol;
		
		checkWin();
	}
	
	//helper : to check if board is in correct order already
	//a separate function for organization purposes
	private void checkWin() {
		
		//if Totoro is not in bottom right corner
		if(totRow != 3 && totCol != 3)
			return;
		
		//checks each panels in order
		int num = 1;
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				
				if(allPanels[r][c].number != num)
					return;
				num++;
			}
		}
		
		//when all numbers are in order & Totoro is in last position
		JOptionPane.showMessageDialog(null, "Congratulations, you win!! Totoro's total moves : " + moves);
		totRow = -1;
		totCol = -1;
	}
	
	class PicPanel extends JPanel{
		private int width = 76;
		private int height = 80;	//dimensions of the Panel 

		private int number=-1;		// -1 when Totoro is at that position.
		private JLabel text;

		public PicPanel(){

			setBackground(Color.white);
			setLayout(null);

		}		

		//changes the panel to have the given number
		public void setNumber(int num){	
			number = num;
			text = new JLabel(""+number,SwingConstants.CENTER);
			text.setFont(new Font("Calibri",Font.PLAIN,55));
			text.setBounds(0,35,70,50);
			this.add(text);

			repaint();
		}
		
		//replaces the number with Totoro
		public void removeNumber(){
			this.remove(text);
			number = -1;
			repaint();
		}

		public Dimension getPreferredSize() {
			return new Dimension(width,height);
		}

		//this will draw the image or the number
		// called by repaint and when the panel is initially drawn
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(number == -1)
				g.drawImage(image,8,0,this);
		}
		
	}
	
	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

	
	public static void main(String[] args){
		new KeyboardTileSlider();
	}

}
