package planner;

import java.util.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.List;
import java.awt.Scrollbar;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.Label;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StateEditGUI extends JFrame {

	private JPanel contentPane;
	private Panel addPanel;
	private JComboBox addComboBox;
	private Label label;
	private Label label_1;
	private Label label_2;
	private TextField textFieldX;
	private TextField textFieldY;
	private Button addButton;
	private Panel deletePanel;
	private Label label_3;
	private JComboBox deleteComboBox;
	private Button deleteButton;
	String fileName;
	static StateEditGUI frame;

	/**
	 * Launch the application.
	 */
	public static void stateEditGUI(String fileName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new StateEditGUI(fileName);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Create the frame.
	 */
	public StateEditGUI(String fileName) {
		setTitle("編集用ウィンドウ");
		setFileName(fileName);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(GUI.frame.getX()+430, GUI.frame.getY(), 360, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		addPanel = new Panel();
		addPanel.setBounds(10, 10, 325, 101);
		contentPane.add(addPanel);
		addPanel.setLayout(null);

		String[] state = {"clear X","holding X","X on Y","ontable X","handEmpty","X is color of Y","X is shape of Y"};
		addComboBox = new JComboBox(state);
		addComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String)addComboBox.getSelectedItem();
				if(s.equals("clear X")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(false);
				}else if(s.equals("holding X")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(false);
				}else if(s.equals("X on Y")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(true);
				}else if(s.equals("ontable X")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(false);
				}else if(s.equals("handEmpty")) {
					textFieldX.setEnabled(false);
					textFieldY.setEnabled(false);
				}else if(s.equals("X is color of Y")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(true);
				}else if(s.equals("X is shape of Y")) {
					textFieldX.setEnabled(true);
					textFieldY.setEnabled(true);
				}
			}
		});
		addComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
		addComboBox.setBounds(0, 20, 152, 23);
		addPanel.add(addComboBox);

		label = new Label("追加");
		label.setBounds(0, 0, 69, 23);
		addPanel.add(label);

		label_1 = new Label("X =");
		label_1.setBounds(176, 20, 23, 23);
		addPanel.add(label_1);

		label_2 = new Label("Y =");
		label_2.setBounds(176, 49, 23, 23);
		addPanel.add(label_2);

		textFieldX = new TextField();
		textFieldX.setBounds(205, 20, 113, 23);
		addPanel.add(textFieldX);

		textFieldY = new TextField();
		textFieldY.setBounds(205, 49, 113, 23);
		textFieldY.setEnabled(false);
		addPanel.add(textFieldY);

		addButton = new Button("追加");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int check = 0;
				String state = (String)addComboBox.getSelectedItem();
				if(state.equals("clear X")&&!textFieldX.getText().equals("")) {
					String addItem = "clear "+textFieldX.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("holding X")&&!textFieldX.getText().equals("")) {
					String addItem = "holding "+textFieldX.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("X on Y")&&!textFieldX.getText().equals("")&&!textFieldY.getText().equals("")) {
					String addItem = textFieldX.getText()+" on "+textFieldY.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("ontable X")&&!textFieldX.getText().equals("")) {
					String addItem = "ontable "+textFieldX.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("handEmpty")) {
					String addItem = "handEmpty";
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("X is color of Y")&&!textFieldX.getText().equals("")&&!textFieldY.getText().equals("")) {
					String addItem = textFieldX.getText()+" is color of "+textFieldY.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else if(state.equals("X is shape of Y")&&!textFieldX.getText().equals("")&&!textFieldY.getText().equals("")) {
					String addItem = textFieldX.getText()+" is shape of "+textFieldY.getText();
					if(maching(addItem)==-1) {
						addItem(addItem);
						deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
					}else {
						check = 1;
					}
				}else {
					check = 2;
				}
				if(check==1) {
					frame.setEnabled(false);
					ErrorMessageGUI.errorMessageGUI("同一のアイテムがあります");
				}else if(check == 2) {
					frame.setEnabled(false);
					ErrorMessageGUI.errorMessageGUI("入力に不備があります");
				}
				textFieldX.setText("");
				textFieldY.setText("");
				GUI.frame.goalOutput();
				GUI.frame.initialOutput();
			}
		});
		addButton.setBounds(249, 78, 76, 23);
		addPanel.add(addButton);

		deletePanel = new Panel();
		deletePanel.setBounds(10, 117, 325, 53);
		contentPane.add(deletePanel);
		deletePanel.setLayout(null);

		deleteComboBox = new JComboBox(loadFile());
		deleteComboBox.setBounds(0, 20, 152, 23);
		deletePanel.add(deleteComboBox);

		label_3 = new Label("削除");
		label_3.setBounds(0, 0, 69, 23);
		deletePanel.add(label_3);

		deleteButton = new Button("削除");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deleteItem = (String)deleteComboBox.getSelectedItem();
				deleteItem(deleteItem);
				deleteComboBox.setModel(new DefaultComboBoxModel(loadFile()));
				GUI.frame.goalOutput();
				GUI.frame.initialOutput();
			}
		});
		deleteButton.setBounds(249, 20, 76, 23);
		deletePanel.add(deleteButton);

		Button backButton = new Button("戻る");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				GUI.setOnTop();
				GUI.frame.goalOutput();
				GUI.frame.initialOutput();
				GUI.enabledControl(true);
			}
		});
		backButton.setBounds(259, 188, 76, 23);
		contentPane.add(backButton);
	}
	public String[] loadFile() {
		ArrayList<String> lists = FileReader.toArrayList(fileName);
		String[] result = new String[lists.size()];
		for(int i = 0;i<lists.size();i++) {
			result[i] = lists.get(i);
		}
		return result;
	}
	public void addItem(String addItem) {
		ArrayList<String> lists = FileReader.toArrayList(fileName);
		lists.add(addItem);
		FileWriter.write(lists, fileName);
	}
	public void deleteItem(String deleteItem) {
		ArrayList<String> lists = FileReader.toArrayList(fileName);
		lists.remove(maching(deleteItem));
		FileWriter.write(lists, fileName);
	}
	public int maching(String machingItem) {
		ArrayList<String> lists = FileReader.toArrayList(fileName);
		for(int i = 0;i<lists.size();i++) {
			if(lists.get(i).equals(machingItem)) {
				return i;
			}
		}
		return -1;
	}
}
