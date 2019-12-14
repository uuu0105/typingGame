
import java.awt.Color;

import javax.swing.*;

public class ScorePanel extends JPanel {
	static int correct=0;
	static int score=0;
	private JLabel textLabel = new JLabel("점수");
	static JLabel scoreLabel=new JLabel("0");
	
	static public void checkSuccess() { //맞췄을 때 점수 바뀌기
		if(ScorePanel.correct==1) {
			ScorePanel.score+=20;
			scoreLabel.setText(Integer.toString(ScorePanel.score));
			//System.out.println(ScorePanel.score);
		}
		else if(ScorePanel.correct ==-1) {
			if(ScorePanel.score<10)
				ScorePanel.score=0;
			else
				ScorePanel.score-=10;
			scoreLabel.setText(Integer.toString(ScorePanel.score));
			//System.out.println(ScorePanel.score);
		}
		else { //sucess 가 디폴트로 0일 때. 즉 아무일도 업을 때
			return;
		}
		ScorePanel.correct=0;
	}

	public ScorePanel() {
		setBackground(Color.PINK);	// 배경은 노란색
		setLayout(null);	// 개발자가 레이아웃 설정
		textLabel.setSize(50, 30);	// 텍스트 사이즈 50x30 설정
		textLabel.setLocation(10, 10);	// 텍스트 위치 10x10 설정
		add(textLabel);	// 텍스트를 panel에 추가
		
		scoreLabel.setSize(100, 30);
		scoreLabel.setLocation(70, 10);
		add(scoreLabel);
		checkSuccess();
	}
}
