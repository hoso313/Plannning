package planner;

import java.awt.Image;

//各ブロックの属性を保存するクラス
public class BlockAttribute {
	private int num = -1;
	private String name;
	private String color = "white";
	private String shape = "square";
	private int firstX = Integer.MIN_VALUE;
	private int firstY = Integer.MIN_VALUE;
	private int x = Integer.MIN_VALUE;
	private int y = Integer.MIN_VALUE;
	private Image image;
	private BlockAttribute overBlock;

	public BlockAttribute(String name,int i) {
		this.name = name;
		this.num = i;
	}

	public int getNum() {
		return num;
	}
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public String getShape() {
		return shape;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Image getImage() {
		return image;
	}
	public int getFirstX() {
		return firstX;
	}
	public int getFirstY() {
		return firstY;
	}
	public BlockAttribute getOverBlock() {
		return overBlock;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public void setFirstPlase(int x,int y) {
		this.firstX = x;
		this.firstY = y;
	}
	public void setPlase(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public void setOverBlock(BlockAttribute overBlock) {
		this.overBlock = overBlock;
	}
	public String toString() {
		String result = "NAME : "+getName()+"\nCOLOR : "+getColor()+"\nSHAPE : "+getShape()+"\nX : "+getX();
		return result;
	}
}
