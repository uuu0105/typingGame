
import java.awt.Color;

import javax.swing.*;

public class ScorePanel extends JPanel {
	static int correct=0;
	static int score=0;
	private JLabel textLabel = new JLabel("����");
	static JLabel scoreLabel=new JLabel("0");
	
	static public void checkSuccess() { //������ �� ���� �ٲ��
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
		else { //sucess �� ����Ʈ�� 0�� ��. �� �ƹ��ϵ� ���� ��
			return;
		}
		ScorePanel.correct=0;
	}

	public ScorePanel() {
		setBackground(Color.PINK);	// ����� �����
		setLayout(null);	// �����ڰ� ���̾ƿ� ����
		textLabel.setSize(50, 30);	// �ؽ�Ʈ ������ 50x30 ����
		textLabel.setLocation(10, 10);	// �ؽ�Ʈ ��ġ 10x10 ����
		add(textLabel);	// �ؽ�Ʈ�� panel�� �߰�
		
		scoreLabel.setSize(100, 30);
		scoreLabel.setLocation(70, 10);
		add(scoreLabel);
		checkSuccess();
	}
}
