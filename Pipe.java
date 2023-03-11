import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Pipe {
	Random r = new Random();
	private int height = generateHeight();// generates height upon object creation, this the space between pipes
	private int topPipeint = 240 + (height / 2);// this actually generates the bottom pipe
	private int botPipeint = 240 - (height / 2);// this actually generates the top pipe
	public int yDis = r.nextInt(120);// creating variation within the pipes height
	private int posneg = r.nextInt(2);// determines whether the pipe is above or below the center of the map
	private int secondsPassed2 = 0;//used for timer
	public boolean ready = false;//used to determine if another pipe can generate
	public boolean intersection = false;//to determine if the birb can intersect with pipe

	JLabel topPipe = new JLabel();
	JLabel botPipe = new JLabel();
	
	
	public Pipe() {
		if (posneg == 0) {
			yDis = -yDis;//creating variance by having the pipe be below or above by yDis
		}
	}

	public int generateHeight() {
		return (r.nextInt(10) + 10) * 10;//generating a height
	}

	public int getHeight() {
		return height;
	}

	public int getTop() {
		return topPipeint;
	}

	public int getBot() {
		return botPipeint;
	}
	public JLabel getTopJLabel(){
		return topPipe;
	}
	public JLabel getBotJLabel() {
		return botPipe;
	}

	//generates pipes
	//indicates whether a new pipe can be formed with "ready" variable
	//indicates whether the pipe can intersect with the birb with "intersect" variable
	public void generatePipe(JFrame frame) {
		System.out.println("GENERATING PIPE");
		Pipe newPipe = new Pipe();
		newPipe.generateHeight();//creating a hegiht
		
		//creating a timer to move the pipe
		secondsPassed2 = 0;
		Timer time2 = new Timer();
		TimerTask task2 = new TimerTask() {
			public void run() {
				secondsPassed2++;
			}
		};
		time2.scheduleAtFixedRate(task2, 0, 50);
		
		//adding the top and bottom pipe to the JFrame
		frame.add(botPipe);
		frame.add(topPipe);
		topPipe.setBounds(700, newPipe.getTop(), 50, 300);
		botPipe.setBounds(700, 0, 50, newPipe.getBot());

		topPipe.setIcon(new ImageIcon("C:\\Users\\rober\\OneDrive\\Documents\\pipe.png"));
		botPipe.setIcon(new ImageIcon("C:\\Users\\rober\\OneDrive\\Documents\\pipe.png"));
		
		//checking if pipe is still valid and the birb is not dead
		while (topPipe.getX() > -150 && !FlappyBird.isDead) {
System.out.print("");
			int xPipe = secondsPassed2 * 3;//determines the distance each seconds travels
			topPipe.setBounds(700 - xPipe, newPipe.getTop() + yDis, 50, 300);//moving the topPipe
			botPipe.setBounds(700 - xPipe, 0, 50, newPipe.getBot() + yDis);//moving the botPipe
			if (topPipe.getX() < 450) {
				ready = true;//if the pipe has traveled below 450, then another pipe can generate
			}
			if (topPipe.getX() < 50&& topPipe.getX() > -25)
				intersection = true;//this is where the bird's x location is around and to check if the y coordinates intersect
			else
				intersection = false;
		}
		frame.remove(botPipe);//removing the pipe once they are off the screen
		frame.remove(topPipe);
		task2.cancel();//stopping the timer for the pipes once they are off screen
	}

}
