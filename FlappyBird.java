import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.util.Timer;
import java.util.TimerTask;

public class FlappyBird extends JFrame {
	static JPanel menu = new JPanel();

	private static double yPos = 240;// y position of birb
	private static double yVelo;// y velocity of birb
	private static double yAcc;// y acceleration of birb
	public static boolean isDead = true;// if birb is dead or alive
	public static int score = 0;// score
	static int secondsPassed = 0;// used to track time of birb falling
	static Timer time = new Timer();// timer used by birb

	static JLabel flappyBird = new JLabel();// birb's label
	static JLabel yourScoreLabel = new JLabel();// score label
	public JLabel title = new JLabel("Flappy Bird");
	JButton start = new JButton();
	static JButton jump = new JButton();// binded to space

	Image img = Toolkit.getDefaultToolkit().createImage("background.png");

	protected JFrame fb;

	static TimerTask task = new TimerTask() {
		public void run() {
			secondsPassed++;
//			System.out.println("miliseconds passed: " + secondsPassed);

		}
	};

	// magnum opusz
	// basically runs the programs to generate pipes at relevant intervals
	// also checks for bird collisions with pipes
	// note that it is a recursive function
	public static void multithread(JFrame fb) {

		Pipe a = new Pipe(); // creates new pipe with every new iteration

		// first thread used to display the image of pipe
		Thread threadb = new Thread() {
			public void run() {
				a.generatePipe(fb);
			}
		};

		// second thread used to check if another pipe is ready to place and to recall
		// if so
		Thread threadc = new Thread() {
			public void run() {
				while (true) {
//					System.out.println(a.ready);
					System.out.println();
					if (a.ready) {// ready being when the old pipe has reached a certain x position
						multithread(fb);// create a new pipe by recalling the method
						break;
					}

				}
			}
		};
		// this thread used to check if bird has hit pipe
		Thread threadd = new Thread() {
			public void run() {
				while (true) {
					boolean oldInter = a.intersection; // to prevent recounting(a pipe increasing score by more than 1)
					// we check if the pipe has crossed a certain x position using old position and
					// comparing it to new
					System.out.print("");
					if ((intersects(a.getBotJLabel()) || intersects(a.getTopJLabel())) && a.intersection) {
//						System.out.println("PIPE HIT");
						isDead = true;// stop bird and pipe generation
						break;
					} else if (!intersects(a.getBotJLabel()) && !intersects(a.getTopJLabel()) && a.intersection == false
							&& oldInter == true) // if the bird has not hit the pipes and the
					// bird has crossed the pipe, increase the
					// score and update it
					{
						score++;
						updateScore(); // updates the JLabel
					}
				}
			}
		};
		// running the threads
		threadb.start();
		threadd.start();
		threadc.start();

	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		isDead = true;
		FlappyBird fb = new FlappyBird();

		// just checking if the user has pressed the "start game" button to start the
		// game
		while (true) {
			System.out.print("");
			if (isDead == false) {// using the isDead variable to check if the button is pressed
				startGame(fb);
				break;
			}
		}

	}

	public static void startGame(JFrame fb) {
		fb.remove(menu);// removing the menu to start the game
		Thread a = new Thread() {
			public void run() {
				multithread(fb);// creating the pipes, checking if the bird has hit the pipes, etc.
			}
		};
		// thread used to update bird position and physics/mechanics
		Thread b = new Thread() {
			public void run() {
				startBird();
			}
		};
		// starting the threads
		a.start();
		b.start();

//		startGame(fb);
	}

	public static void start() {
		time.scheduleAtFixedRate(task, 500, 10);// used for the timer for the bird
		// used to calculate the speed of the bird and the bird's displacement
	}

	public FlappyBird() {
		setContentPane(new JLabel(new ImageIcon("C:\\Users\\rober\\OneDrive\\Documents\\background.png")));
		setLayout(null);

		//setting menu
		menu.setBounds(160, 120, 320, 240);
		menu.setLayout(new GridLayout(2, 1));
		menu.add(title);
		menu.add(start);
		title.setFont(new Font("Times New Roman", 40, 40));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		start.setHorizontalAlignment(SwingConstants.CENTER);
		start.setText("Start Game");
		start.addActionListener(new ButtonListener());

		//setting up birb
		flappyBird.setIcon(new ImageIcon("C:\\Users\\rober\\OneDrive\\Documents\\bird.png"));
		flappyBird.setBounds(25, 240, 25, 25);
		jump.addActionListener(new ButtonListener());
		yourScoreLabel.setBounds(320, 50, 50, 50);
		yourScoreLabel.setFont(new Font("Times New Roman", 40, 40));
		yourScoreLabel.setText("" + score);

		add(jump);
		add(flappyBird);
		add(menu);
		add(yourScoreLabel);

		//setting up JFrame
		setSize(640, 480);
		setResizable(false);
		setTitle("Flappy Bird");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	// updates the score for "score" variable and score label
	public static void updateScore() {
		System.out.println();
		yourScoreLabel.setText("" + score);
	}

	// checking if the birb has hit the pipes
	// returns true if birb has hit pipe,
	// returns false if birb has not
	public static boolean intersects(JLabel testa) {
		Rectangle rectB = flappyBird.getBounds();

		Rectangle result = SwingUtilities.computeIntersection(testa.getX(), testa.getY(), testa.getWidth(),
				testa.getHeight(), rectB);

		return (result.getWidth() > 0 && result.getHeight() > 0);
	}

	// how the bird knows how to move
	// note that the birb does not move horizontally
	public static void startBird() {
		isDead = false;
		yVelo = 0;// velocity
		yAcc = 0.06;// acceleration
		start();// starting timer so birb can start falling
		while (flappyBird.getY() < 480 && !isDead) {// making sure that the birb has not fell out of the map
			flappyBird.setBounds(25,
					(int) ((0.5 * yAcc * (secondsPassed * secondsPassed)) + (yPos + (yVelo * secondsPassed))), 25, 25);
			// updates the birb's new location based on the secondsPassed or time passed
			// note that secondsPassed does not actually mean each second passed
			if (flappyBird.getY() < 0) {// to check if birb has hit the top and to block it from doing so
				yPos = 0;
				yVelo = 0;
			}
		}
		isDead = true;// kill the bird if it falls out

	}

	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e1) {
			System.out.print("");
			if (start == e1.getSource()) {// start menu button to start game
				remove(menu);
				isDead = false;

			}

			if (jump == e1.getSource() && !isDead) {// changes the velocity of the bird to create gradual jumping effect
				// also sets secondsPassed to 0, making it seem like the birb is jumping up
				yPos = flappyBird.getY();
				secondsPassed = 0;
				yVelo = -3;

			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {// setting space to jump
				jump.doClick();
			}
		}

	}

}
