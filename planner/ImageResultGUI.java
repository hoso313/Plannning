package planner;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class ImageResultGUI extends JFrame {

	private Button button;
	ArrayList<BlockAttribute> blockAttributes;
	ArrayList<String> result = new ArrayList<>();
	private int width;
	private int height;
	private int[] moveX ;
	private int[] moveY;
	private int handX = 30;
	private int handY = 70;
	private final int[] step;
	private final int[] check;
	private int speed=1;

	/**
	 * Launch the application.
	 */
	public static void getInitialState(ArrayList<BlockAttribute> blockAttributes) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageResultGUI frame = new ImageResultGUI(blockAttributes,true);
					frame.initialStateOutput("rep05/initialState.txt");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static void getResultMovie(ArrayList<BlockAttribute> blockAttributes) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageResultGUI frame = new ImageResultGUI(blockAttributes,300);
					frame.initialStateOutput("rep05/initialState.txt");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void setResult() {
		result = Planner.result;
		AttributeEditer ae = new AttributeEditer();
		int size = result.size();
		for(int i = 0;i<size;i++) {
			if(ae.changeToName(result.get(0), blockAttributes).size()!=1) {
				for(;i<size;i++) {
					result.remove(0);
				}
			}else {
				result.add(ae.changeToName(result.get(0), blockAttributes).get(0));
				result.remove(0);
			}
		}
		ArrayList<String> ope = new ArrayList<>();
		ope.add("Place");ope.add("remove");ope.add("pick");ope.add("put");
		for(int i = 0;i<result.size();i++) {
			if(!ope.contains(result.get(i).split(" ")[0])) {
				result.remove(i);
				i--;
			}
		}
	}
	/**
	 * Create the frame.
	 */
	public ImageResultGUI(ArrayList<BlockAttribute> blockAttributes,boolean aaaa) {
		this.blockAttributes = blockAttributes;
		moveX = new int[blockAttributes.size()];
		moveY = new int[blockAttributes.size()];
		step = new int[result.size()];
		check = new int[result.size()];
		width = blockAttributes.size()*50+200;
		height = blockAttributes.size()*50+100;
		setBounds(GUI.frame.getX()+440, GUI.frame.getY(), width+8, height+38);
	}
	/**
	 * @wbp.parser.constructor
	 */
	public ImageResultGUI(ArrayList<BlockAttribute> blockAttributes,int flameY) {
		this.blockAttributes = blockAttributes;
		moveX = new int[blockAttributes.size()];
		moveY = new int[blockAttributes.size()];
		setResult();
		step = new int[result.size()];
		check = new int[result.size()];
		width = blockAttributes.size()*50+200;
		height = blockAttributes.size()*50+100;
		int i = 0;
		for(BlockAttribute ba : blockAttributes) {
			int x = i*50+(i+1)*10+20;
			int y = height-70;
			ba.setFirstPlase(x, y);
			i++;
		}
		setBounds(GUI.frame.getX()+440, GUI.frame.getY()+flameY, width+8, height+38);
		button = new Button("次へ");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0;i<step.length;i++) {
					if(step[i]==0&&check[i]==1) {
						step[i]=1;
					}else if(step[step.length-1]==1) {
						button.setEnabled(false);
					}
				}
			}
		});
		button.setBounds(10,height-35, 100, 30);
		getContentPane();setLayout(null);
		getContentPane().add(button);
		// タイマー
		Timer timer = new java.util.Timer();
		timer.schedule(new MyTimeTask(), 1l, 10l);
	}

	public void paint(Graphics g) {
		g.drawImage(getScreen(),0,0,this);
	}

	private Image getScreen() {
		BufferedImage image = null;
		BufferedImage leftHand = null;
		BufferedImage rightHand = null;
		try {
			image = ImageIO.read(new File("rep05/img/table.png"));
			leftHand = ImageIO.read(new File("rep05/img/leftHand.png"));
			rightHand = ImageIO.read(new File("rep05/img/rightHand.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		Image screen = createImage(1000,height+38);
		Graphics g = screen.getGraphics();
		g.drawImage(image,0,height-20,this);
		g.drawImage(leftHand,handX-30,handY,this);
		outputObject();
		for(BlockAttribute ba : blockAttributes) {
			moveX[ba.getNum()]=ba.getX();
			moveY[ba.getNum()]=ba.getY();
		}
		//動かす
		move();
		for(BlockAttribute ba : blockAttributes) {
			ba.setPlase(moveX[ba.getNum()],moveY[ba.getNum()]);
			g.drawImage(ba.getImage(),ba.getX(),ba.getY(),this);
		}
		g.drawImage(rightHand,handX-30,handY,this);
		return screen;
	}
	public void move() {
		int i=0;
		for(String str:result) {
			String [] term = str.split(" ");
			if(term[0].equals("Place")) {
				BlockAttribute x = searchBlock(term[1]);
				BlockAttribute y = searchBlock(term[3]);
				if(i==0&&step[i]==0) {
					placeXonY(x,y,i);
				}else if(i!=0&&step[i-1]==1&&step[i]==0) {
					placeXonY(x,y,i);
				}
			}else if(term[0].equals("pick")) {
				BlockAttribute x = searchBlock(term[2]);
				if(i==0&&step[i]==0) {
					pickUp(x,i);
				}else if(i!=0&&step[i-1]==1&&step[i]==0) {
					pickUp(x,i);
				}
			}else if(term[0].equals("remove")) {
				BlockAttribute x = searchBlock(term[1]);
				BlockAttribute y = searchBlock(term[5]);
				if(i==0&&step[i]==0) {
					removeXY(x,y,i);
				}else if(i!=0&&step[i-1]==1&&step[i]==0) {
					removeXY(x,y,i);
				}
			}else if(term[0].equals("put")) {
				BlockAttribute x = searchBlock(term[1]);
				if(i==0&&step[i]==0) {
					putX(x,i);
				}else if(i!=0&&step[i-1]==1&&step[i]==0) {
					putX(x,i);
				}
			}
			i++;
		}
	}
	public BlockAttribute searchBlock(String BlockName) {
		for(BlockAttribute ba : blockAttributes) {
			if(ba.getName().equals(BlockName)) {
				return ba;
			}
		}
		return null;
	}

	public void placeXonY(BlockAttribute x,BlockAttribute y,int stepCount) {
		if(x.getY()<y.getY()-50) {
			moveY[x.getNum()]+=speed;
			if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'y',speed);
		}else if(x.getY()>y.getY()-50) {
			moveY[x.getNum()]-=speed;
			if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'y',-speed);
		}else {
			if(x.getX()<y.getX()) {
				moveX[x.getNum()]+=speed;
				if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'x',speed);
			}else if(x.getX()>y.getX()) {
				moveX[x.getNum()]-=speed;
				if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'x',-speed);
			}else {
				y.setOverBlock(x);
				check[stepCount]=1;
			}
		}
	}
	public void accompany(BlockAttribute x,char key,int val) {
		if(x.getOverBlock()!=null) {
			accompany(x.getOverBlock(),key,val);
		}
		if(key=='x') {
			moveX[x.getNum()]+=val;
			x.setPlase(moveX[x.getNum()], x.getY());
		}else if(key=='y') {
			moveY[x.getNum()]+=val;
			x.setPlase(x.getX(), moveY[x.getNum()]);
		}
	}
	public void removeXY(BlockAttribute x,BlockAttribute y,int stepCount) {
		if(x.getX()<handX) {
			moveX[x.getNum()]+=speed;
		}else if(x.getX()>handX) {
			moveX[x.getNum()]-=speed;
		}else {
			if(x.getY()<handY) {
				moveY[x.getNum()]+=speed;
			}else if(x.getY()>handY) {
				moveY[x.getNum()]-=speed;
			}else {
				y.setOverBlock(null);
				check[stepCount]=1;
			}
		}
	}
	public void pickUp(BlockAttribute x,int stepCount) {
		/*
		moveY[x.getNum()]-=20;
		if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'y',-20);
		check[stepCount]=1;
		*/
		if(x.getX()<handX) {
			moveX[x.getNum()]+=speed;
			if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'x',speed);
		}else if(x.getX()>handX) {
			moveX[x.getNum()]-=speed;
			if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'x',-speed);
		}else {
			if(x.getY()<handY) {
				moveY[x.getNum()]+=speed;
				if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'y',speed);
			}else if(x.getY()>handY) {
				moveY[x.getNum()]-=speed;
				if(x.getOverBlock()!=null)accompany(x.getOverBlock(),'y',-speed);
			}else {
				check[stepCount]=1;
			}
		}
	}
	public void putX(BlockAttribute x,int stepCount) {
		if(x.getX()<x.getFirstX()) {
			moveX[x.getNum()]+=speed;
		}else if(x.getX()>x.getFirstX()) {
			moveX[x.getNum()]-=speed;
		}else {
			if(x.getY()<x.getFirstY()) {
				moveY[x.getNum()]+=speed;
			}else if(x.getY()>x.getFirstY()) {
				moveY[x.getNum()]-=speed;
			}else {
				check[stepCount]=1;
			}
		}
	}
	public void initialStateOutput(String initialStateName) {
		ArrayList<String> initialStates = FileReader.toArrayList(initialStateName);
		int i = 0;
		for(String initialState:initialStates) {
			String[] strs=initialState.split(" ");
			if(strs.length==2) {
				if(strs[0].equals("ontable")) {
					for(BlockAttribute ba :blockAttributes) {
						if(strs[1].equals(ba.getName())) {
							int x = i*50+(i+1)*10+150;
							int y = height-70;
							ba.setPlase(x, y);
							ba.setOverBlock(null);
							createObject(ba.getName(),ba.getShape(),selectColor(ba.getColor()));
							i++;
						}
					}
				}
			}
		}
		for(int j = 0;j<initialStates.size();j++) {
			String[] strs=initialStates.get(j).split(" ");
			if(strs.length==3) {
				if(strs[1].equals("on")) {
					for(BlockAttribute ba1 :blockAttributes) {
						if(strs[2].equals(ba1.getName())&&ba1.getX()!=Integer.MIN_VALUE) {
							for(BlockAttribute ba2 :blockAttributes) {
								if(strs[0].equals(ba2.getName())) {
									ba2.setPlase(ba1.getX(), ba1.getY()-50);
									ba1.setOverBlock(ba2);
									createObject(ba2.getName(),ba2.getShape(),selectColor(ba2.getColor()));
								}
							}
						}else if(strs[2].equals(ba1.getName())&&ba1.getX()==Integer.MIN_VALUE) {
							initialStates.add(initialStates.get(j));
							initialStates.remove(j);
							j--;
						}
					}
				}
			}
		}
	}
	public Color selectColor(String	colorName) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(colorName);
			color = (Color)field.get(null);
		} catch (Exception e) {
			//対応する色がない場合は白にする
			color = Color.WHITE;
		}
		return color;
	}
	public void createObject(String blockName,String shape,Color color) {
		BufferedImage image = null;
		String path = "rep05/img/"+shape+".png";
		try {
			image = ImageIO.read(new File(path));
		}catch(Exception e) {
			e.printStackTrace();
		}
		//色付け作業
		if(color!=Color.white) {
			for(int i = 0;i<image.getWidth();i++) {
				for(int j = 0;j<image.getHeight();j++) {
					if(image.getRGB(i,j)==rgb(255,255,255)) {
						image.setRGB(i, j,rgb(color.getRed(),color.getGreen(),color.getBlue()));
					}
				}
			}
		}
		//名前付け
		Graphics graphics = image.createGraphics();
		if(color != Color.BLACK){
			graphics.setColor(Color.BLACK);
		}
		graphics.drawString(blockName, 20, 30);
		try{
			ImageIO.write(image, "png", new File("rep05/result/"+blockName+".png"));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void outputObject() {
		for(BlockAttribute ba : blockAttributes) {
			BufferedImage image = null;
			String path = "rep05/result/"+ba.getName()+".png";
			try {
				image = ImageIO.read(new File(path));
				ba.setImage(image);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int a(int c){
		return c>>>24;
	}
	public static int r(int c){
		return c>>16&0xff;
	}
	public static int g(int c){
		return c>>8&0xff;
	}
	public static int b(int c){
		return c&0xff;
	}
	public static int rgb(int r,int g,int b){
		return 0xff000000 | r <<16 | g <<8 | b;
	}
	private class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			repaint();
		}
	}
}
