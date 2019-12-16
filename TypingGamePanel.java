import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;


class TypingGamePanel extends JPanel{
	JGameGroundPanel groundPanel = new JGameGroundPanel(); //단어 떨어지는 스크린
	private JInputPanel inputPanel = new JInputPanel(); //입력창에 입력하는 스크린

	public static String playStatus= "NotPlaying";
	
	private String fallingWord = null;
	private ArrayList<String> answerVector = new ArrayList<String>();

	private JLabel newLabel; //새로 생기는 레이블
	
	
	public TypingGamePanel() { //setLayout
		setLayout(new BorderLayout());
		add(groundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	class JGameGroundPanel extends JPanel { //단어 떨어지는 스크린.
		
		private JLabel label = new JLabel(); //단어
		private JLabel resLabel = new JLabel(); //단어 oX 판단레이블
		private Words words = null;
		
		private FallingThread thread = null; //레이블 떨어짐
		private RandomThread randomThread = null;
		private boolean gameOn = false;
		
		
		public JGameGroundPanel() {
			setBackground(Color.PINK);
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
				return; // 스레드가 없음 
			thread.interrupt(); // 스레드 강제 종료
			thread = null;
			gameOn = false;
		}
		
		public void stopSelfAndNewGame() { // 스레드가 바닥에 닿아서 실패할 때 호출
			startGame();			
		}
		
		public void printResult(String text) {
			resLabel.setText(text);
		}
		public void startGame() {
			
			while(true) {
				if(TypingGamePanel.playStatus.equals("NotPlaying")) {
					//리스너 안불리면 시작안하고 계속 상태체크
					System.out.println("클릭 안햇음");
					continue;
				}
					
					
				if(TypingGamePanel.playStatus.equals("Playing")) { //리스너 불리면 스레드 시작하고 break;
					System.out.println("리스너 작동");
					
					randomThread = new RandomThread();
					randomThread.start();
				
					gameOn = true;
					break;
				}	
			}
		}
		
		public boolean matchWord(String text) {
			
			System.out.println(answerVector);
			for(int i=0; i<answerVector.size(); i++) {
				if(answerVector.get(i).equals(text)) { 
					System.out.println("text is answer");
					return true; 
				} 
			}
	
			return false;

			
		}
		class RandomThread extends Thread{

			//private JLabel newLabel; //게임 숫자를 출력하는 레이블

			public void run() {
				while(true) {
					try {
						fallingWord = words.getRandomWord();
						answerVector.add(fallingWord);
						
						int x = ((int)(Math.random() * groundPanel.getWidth()));
						int y = 5;
						newLabel = new JLabel(fallingWord);
						newLabel.setSize(200, 30);
						newLabel.setLocation(x, y);
						newLabel.setForeground(TypingGameFrame.selectedColor);
						newLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
						groundPanel.add(newLabel);
						//groundPanel.repaint();
						
						
						
						thread = new FallingThread(groundPanel, newLabel);
						thread.start();
						//fallings.add(thread); //떨어지는 중인 쓰레드들을 벡터에 저장
						
						Thread.sleep(5000);
						
						
					}catch(InterruptedException e){
						/*
						 * while(true) { if(TypingGamePanel.pauseOrResume=="Resume")break; }
						 */
						return;
					}
				}
			}
		}
		class FallingThread extends Thread {
			private JGameGroundPanel panel;
			private JLabel label; //게임 숫자를 출력하는 레이블
			private long delay = 1000; // 지연 시간의 초깃값 = 200
			private boolean falling = false; // 떨이지고 있는지. 초깃값 = false
			
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
				while(true) {
					try {
						sleep(delay);
						
						int y = label.getY() + 5; //5픽셀 씩 아래로 이동
						if(y >= panel.getHeight()-label.getHeight()) {//바닥에 닿음
							ScorePanel.correct=-1; //점수 감점
							ScorePanel.checkSuccess();
							falling = false;
							label.setText("");
							groundPanel.printResult("시간초과실패");
							//groundPanel.stopSelfAndNewGame();
							this.interrupt();//return;//break; // 스레드 종료
							break;
						}
						else {
							label.setLocation(label.getX(), y);
							TypingGamePanel.this.repaint();
						}

					} catch (InterruptedException e) {
						
						//이동안함
						falling = false;
						return; // 스레드 종료
					}
				}
			}	
		}
	}

	class JInputPanel extends JPanel {
		
		public JInputPanel() {
			
			setLayout(new FlowLayout());
			JTextField tf = new JTextField(40); //입력창
			add(tf);

			tf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) e.getSource();
					String answer = t.getText();

					if(answer.equals("그만"))
						System.exit(0);
					//if(!groundPanel.isGameOn())
					//	return;
					
					boolean match = groundPanel.matchWord(answer);
					
					if(match == true) {
						System.out.println(answer + "is in the answerVector"); 
						setBackground(Color.GREEN);
						ScorePanel.correct=1;
						ScorePanel.checkSuccess();
						groundPanel.printResult("성공");
						Component[] children = groundPanel.getComponents();
						for(int i=0; i<children.length; i++) {
							if(((JLabel)children[i]).getText().equals(answer)) {
								groundPanel.remove(children[i]);
								groundPanel.revalidate();
							}
						}
						//groundPanel.stopGame();
						//groundPanel.startGame();
						tf.setText("");
					}
					else {
						setBackground(Color.RED);
						ScorePanel.correct=-1;
						ScorePanel.checkSuccess();
						groundPanel.printResult(tf.getText() + " 틀림");
						tf.setText("");
					}
				}
			});
		}
	}
}

//words.txt 파일을 읽고 벡터에 저장하고 벡터로부터 랜덤하게 단어를 추출하는 클래스
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
		final int WORDMAX = wordVector.size(); // 총 단어의 개수
		int index = (int) (Math.random() * WORDMAX);
		return wordVector.get(index);
	}
}