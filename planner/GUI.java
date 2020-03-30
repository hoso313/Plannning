package planner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Label;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class GUI extends JFrame {

	private JPanel contentPane;
	private Panel goalPanel;
	private TextArea goalArea;
	private Label goalLabel;
	private Button goalEditButton;
	private Panel initialPanel;
	private Label initialLabel;
	private TextArea initialArea;
	private Button initialEditButton;
	static GUI frame;
	private Button decisionButton;
	private Panel panel;
	static String goalName = "rep05/goal.txt";
	static String initialName = "rep05/initialState.txt";
	private Button button;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 441, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		goalPanel = new Panel();
		goalPanel.setBounds(10, 10, 200, 233);
		contentPane.add(goalPanel);
		goalPanel.setLayout(null);

		goalArea = new TextArea();
		goalArea.setBackground(Color.WHITE);
		goalArea.setEditable(false);
		goalArea.setBounds(0, 20, 200, 190);
		goalPanel.add(goalArea);
		goalOutput();

		goalLabel = new Label("目的状態");
		goalLabel.setAlignment(Label.CENTER);
		goalLabel.setBounds(0, 0, 177, 23);
		goalPanel.add(goalLabel);

		goalEditButton = new Button("編集する");
		goalEditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enabledControl(false);
				StateEditGUI.stateEditGUI(goalName);
			}
		});
		goalEditButton.setBounds(124, 210, 76, 23);
		goalPanel.add(goalEditButton);

		initialPanel = new Panel();
		initialPanel.setBounds(216, 10, 200, 233);
		contentPane.add(initialPanel);
		initialPanel.setLayout(null);

		initialArea = new TextArea();
		initialArea.setBackground(Color.WHITE);
		initialArea.setEditable(false);
		initialArea.setBounds(0, 20, 200, 190);
		initialPanel.add(initialArea);
		initialOutput();

		initialLabel = new Label("初期状態");
		initialLabel.setAlignment(Label.CENTER);
		initialLabel.setBounds(0, 0, 179, 23);
		initialPanel.add(initialLabel);

		initialEditButton = new Button("編集する");
		initialEditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enabledControl(false);
				StateEditGUI.stateEditGUI(initialName);
			}
		});
		initialEditButton.setBounds(124, 210, 76, 23);
		initialPanel.add(initialEditButton);

		button = new Button("表示する");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageResultGUI.getInitialState(new AttributeEditer().getBlockAttribute());
			}
		});
		button.setBounds(42, 210, 76, 23);
		initialPanel.add(button);

		panel = new Panel();
		panel.setBounds(110, 249, 306, 24);
		contentPane.add(panel);
				panel.setLayout(null);

				decisionButton = new Button("決定");
				decisionButton.setBounds(230, 0, 76, 23);
				panel.add(decisionButton);
				decisionButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						enabledControl(false);
						Planner planner = new Planner();
						(planner).start();
						ResultGUI.resultGUI(planner);
					}
				});
	}
	public void goalOutput() {
		goalArea.setText("");
		ArrayList<String> goals = FileReader.toArrayList(goalName);
		for(String goal : goals) {
			goalArea.append(goal+"\n");
		}
	}
	public void initialOutput() {
		initialArea.setText("");
		ArrayList<String> goals = FileReader.toArrayList(initialName);
		for(String goal : goals) {
			initialArea.append(goal+"\n");
		}
	}
	public static void enabledControl(boolean b) {
		//frame.setEnabled(b);
		frame.goalEditButton.setEnabled(b);
		frame.initialEditButton.setEnabled(b);
		frame.decisionButton.setEnabled(b);
	}
	//ウィンドウを前に出す
	public static void setOnTop() {
		frame.setAlwaysOnTop(true);
		frame.setAlwaysOnTop(false);
	}
}
