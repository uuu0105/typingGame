import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;


class TypingGamePanel extends JPanel{
	JGameGroundPanel groundPanel = new JGameGroundPanel(); //�ܾ� �������� ��ũ��
	private JInputPanel inputPanel = new JInputPanel(); //�Է�â�� �Է��ϴ� ��ũ��

	public static String playStatus= "NotPlaying";
	
	private String fallingWord = null;
	private ArrayList<String> answerVector = new ArrayList<String>();

	private JLabel newLabel; //���� ����� ���̺�
	
	
	public TypingGamePanel() { //setLayout
		setLayout(new BorderLayout());
		add(groundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	class JGameGroundPanel extends JPanel { //�ܾ� �������� ��ũ��.
		
		private JLabel label = new JLabel(); //�ܾ�
		private JLabel resLabel = new JLabel(); //�ܾ� oX �Ǵܷ��̺�
		private Words words = null;
		
		private FallingThread thread = null; //���̺� ������
		private RandomThread randomThread = null;
		private boolean gameOn = false;
		
		
		public JGameGroundPanel() {
			
			setLayout(null);
			add(label);
			
			resLabel.setLocation(0, 0);
			resLabel.setSize(100, 30);
			add(resLabel);
			words = new Words("words.txt");

		}
		public boolean isGameOn() {
			return gameOn;
		}
		
		public void stopGame() {
			if(thread == null)
				return; // �����尡 ���� 
			thread.interrupt(); // ������ ���� ����
			thread = null;
			gameOn = false;
		}
		
		public void writeRecords(String name, int score) {
			try {
				String recordPath = "records.txt";
				File recordFile = new File(recordPath);
				FileWriter fileWriter = new FileWriter(recordFile, true);
				BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				fileWriter.append("Player : " + name + "\n");
				fileWriter.append("Score : " + Integer.toString(score) + "\n");
				fileWriter.append("Date : " + formatter.format(currentTime) + "\n");
				fileWriter.append("---------------------------------------\n");
				bufferFileWriter.close();
				System.out.println("records Completed");
			} catch(Exception e) { System.out.println("record error"); }
		}
		public void gameOver() {
			inputPanel.tf.setEditable(false);
			Component[] children = groundPanel.getComponents();
			for (int i = 0; i < children.length; i++) {
				groundPanel.remove(children[i]);
				groundPanel.revalidate();
				groundPanel.repaint();
			}
			thread= null;
			//thread.interrupt();
			randomThread = null;
			//randomThread.interrupt();
			// ���̾�α� â���� �̸� �Է� �޾� ���Ͽ� �̸��� ���� ���
			String name = (String) JOptionPane.showInputDialog(this, "Write your name", "Save your record", JOptionPane.PLAIN_MESSAGE);
			//String name = (String) JOptionPane.showInputDialog(null, "input your name");
			System.out.println("user name : " + name);
			if(name != null) {
				JOptionPane.showMessageDialog(null, "Successfully saved your record, " + name);
				writeRecords(name, ScorePanel.score);
				return;
			}
			else {
				// ���̾�α� â �ݱ�
				System.exit(0);
			}
		}
		public void printResult(String text) {
			resLabel.setText(text);
		}
		public void startGame() {
			
			while(true) {
				if(TypingGamePanel.playStatus.equals("NotPlaying")) {
					//������ �ȺҸ��� ���۾��ϰ� ��� ����üũ
					System.out.println("Ŭ�� ������");
					continue;
				}
					
					
				if(TypingGamePanel.playStatus.equals("Playing")) { //������ �Ҹ��� ������ �����ϰ� break;
					System.out.println("������ �۵�");
					
					randomThread = new RandomThread();
					randomThread.start();
				
					gameOn = true;
					break;
				}	
			}
		}
		
		public boolean matchWord(String text) {
			
			System.out.println(answerVector);
			for (int i = 0; i < answerVector.size(); i++) {
				System.out.println("vector size : " + answerVector.size());
				if (answerVector.get(i).equals(text)) {
					answerVector.remove(text);
					System.out.println("vector size : " + answerVector.size());
					System.out.println("text is answer");
					return true;
				}
				
			}
	
			return false;

			
		}
		class RandomThread extends Thread{

			//private JLabel newLabel; //���� ���ڸ� ����ϴ� ���̺�
			private int generateCycle = 3000;
			public void run() {
				while (!Thread.interrupted()) {
					try {
						if (TimerRunnable.isFinished == true) {
							gameOver();
							this.interrupt();
						}
						if(TimerRunnable.remainedTime > 50 && TimerRunnable.remainedTime <= 100) {
							generateCycle = 2000;
							TimerRunnable.level=2;
						}
						else if(TimerRunnable.remainedTime <= 50) {
							generateCycle = 1500;
							TimerRunnable.level=3;
						}
						fallingWord = words.getRandomWord();
						answerVector.add(fallingWord);
						newLabel = new JLabel(fallingWord);
						newLabel.setSize(180, 30);
						int x = ((int) (Math.random() * (groundPanel.getWidth() - newLabel.getWidth())));
						int y = 5;
						newLabel.setLocation(x, y);
						newLabel.setForeground(TypingGameFrame.selectedColor);
						newLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
						groundPanel.add(newLabel);

						thread = new FallingThread(groundPanel, newLabel);
						thread.start();

						Thread.sleep(generateCycle);

					} catch (InterruptedException e) {
						System.out.println("random Exception");
						return; // ������ ����
					}
				}
			}
		}
		class FallingThread extends Thread {
			private JGameGroundPanel panel;
			private JLabel label; //���� ���ڸ� ����ϴ� ���̺�
			private long fallingDelay = 300; // ���� �ð��� �ʱ갪 = 200
			private boolean falling = false; // �������� �ִ���. �ʱ갪 = false
			private int fallingCount =0; //������ Ƚ��
			
			public FallingThread(JGameGroundPanel panel, JLabel label) {
				this.panel = panel;
				this.label = label;
			}
			
			public boolean isFalling() {
				return falling; 
			}	
			
			@Override
			public void run() {
				falling = true;
				while (!Thread.interrupted()) {
					try {
						sleep(fallingDelay);
						if (TimerRunnable.isFinished == true)
							gameOver();
						if(TimerRunnable.remainedTime<=100)
							fallingDelay = 150;
						int y = label.getY() + 5; // 5�ȼ� �� �Ʒ��� �̵�
						if (y >= panel.getHeight()) { //- label.getHeight()) {// �ٴڿ� ����
							fallingCount++;
							TimerRunnable.life--;
							//ScorePanel.correct = -1; // ���� ����
							//ScorePanel.checkSuccess();
							falling = false;
							label.setText("");
							groundPanel.printResult("�ð��ʰ�����");
							if (TimerRunnable.life < 0)
								gameOver();
							this.interrupt(); // ������ ����
						} else {
							label.setLocation(label.getX(), y);
							TypingGamePanel.this.repaint();
						}
					} catch (InterruptedException e) {
						System.out.println("Exception working");
						ScorePanel.correct = -1; // ���� ����
						ScorePanel.checkSuccess();
						falling = false;
						return; // ������ ����
					}
				}
			}	
		}
	}

	class JInputPanel extends JPanel {
		private JTextField tf;
		public JInputPanel() {
			
			setLayout(new FlowLayout());
			JTextField tf = new JTextField(40); //�Է�â
			add(tf);

			tf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) e.getSource();
					String answer = t.getText();

					if(answer.equals("�׸�"))
						System.exit(0);
					
					boolean match = groundPanel.matchWord(answer);
					
					if(match == true) {
						System.out.println(answer + "is in the answerVector"); 
						setBackground(Color.GREEN);
						ScorePanel.correct=1;
						ScorePanel.checkSuccess();
						groundPanel.printResult("����");
						Component[] children = groundPanel.getComponents();
						for(int i=0; i<children.length; i++) {
							if(((JLabel)children[i]).getText().equals(answer)) {
								groundPanel.remove(children[i]);
								groundPanel.revalidate();
							}
						}
					
						tf.setText("");
					}
					else {
						setBackground(Color.RED);
						ScorePanel.correct=-1;
						ScorePanel.checkSuccess();
						groundPanel.printResult(tf.getText() + " Ʋ��");
						tf.setText("");
					}
				}
			});
		}
	}
}

//words.txt ������ �а� ���Ϳ� �����ϰ� ���ͷκ��� �����ϰ� �ܾ �����ϴ� Ŭ����
class Words {
	Vector<String> wordVector = new Vector<String>();

	public Words(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				wordVector.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("file not found error");
			System.exit(0);
		} catch (IOException e) {
			System.out.print("input output error");
		}
	}

	public String getRandomWord() {
		final int WORDMAX = wordVector.size(); // �� �ܾ��� ����
		int index = (int) (Math.random() * WORDMAX);
		return wordVector.get(index);
	}
}