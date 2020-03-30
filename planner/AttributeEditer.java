package planner;

import java.util.*;

public class AttributeEditer {

	String initialStateName = "rep05/initialState.txt";

	//初期状態からブロックの属性を保存する
	public  ArrayList<BlockAttribute> getBlockAttribute() {
		ArrayList<String> initialStates = FileReader.toArrayList(initialStateName);
		ArrayList<BlockAttribute> blockAttributes = new ArrayList<>();
		//BlockAttributeインスタンスを作成
		int i =0;
		for(String name : nameSelecter(initialStates)) {
			BlockAttribute ba = new BlockAttribute(name,i);
			blockAttributes.add(ba);
			i++;
		}
		//色と形を追加する
		HashMap<String,String> color = colorSelecter(initialStates);
		HashMap<String,String> shape = shapeSelecter(initialStates);
		for(BlockAttribute ba :blockAttributes) {
			if(color.containsKey(ba.getName())) {
				ba.setColor(color.get(ba.getName()));
			}
			if(shape.containsKey(ba.getName())) {
				ba.setShape(shape.get(ba.getName()));
			}
		}
		return blockAttributes;
	}

	//ブロックの名前を取得
	public ArrayList<String> nameSelecter(ArrayList<String> initialStates) {
		ArrayList<String> Names = new ArrayList<>();
		for(String initialState:initialStates) {
			//初期状態の各文を空白で分割
			String[] terms = initialState.split(" ");
			//文字数でどこに変数があるかを判断する
			if(terms.length==2) {
				if(!Names.contains(terms[1]))Names.add(terms[1]);
			}else if(terms.length==3) {
				if(!Names.contains(terms[0]))Names.add(terms[0]);
				if(!Names.contains(terms[2]))Names.add(terms[2]);
			}else if(terms.length==5) {
				if(!Names.contains(terms[0]))Names.add(terms[0]);
			}
		}
		return Names;
	}
	public HashMap<String,String> colorSelecter(ArrayList<String> initialStates) {
		HashMap<String,String> color = new HashMap();
		for(String initialState:initialStates) {
			//初期状態の各文を空白で分割
			String[] terms = initialState.split(" ");
			//文字数によって"X is color of Y"と"X is shape of Y"を選択
			if(terms.length==5) {
				if(terms[2].equals("color")) {
					color.put(terms[0], terms[4]);
				}
			}
		}
		return color;
	}
	public HashMap<String,String> shapeSelecter(ArrayList<String> initialStates) {
		HashMap<String,String> shape = new HashMap();
		for(String initialState:initialStates) {
			//初期状態の各文を空白で分割
			String[] terms = initialState.split(" ");
			//文字数によって"X is color of Y"と"X is shape of Y"を選択
			if(terms.length==5) {
				if(terms[2].equals("shape")) {
					shape.put(terms[0], terms[4]);
				}
			}
		}
		return shape;
	}
	/*
	//こっちのchangeToNameは重複に対応していない

	public HashMap<String,String> getRelation(ArrayList<BlockAttribute> blockAttributes){
		HashMap<String,String> relation = new HashMap<>();
		for(BlockAttribute ba : blockAttributes) {
			relation.put(ba.getColor(),ba.getName());
			relation.put(ba.getShape(),ba.getName());
		}
		return relation;
	}

	public String changeToName(String befor,HashMap<String,String>relation){
		String[] terms = befor.split(" ");
		String result="";
		for(String term : terms) {
			if(relation.containsKey(term)) {
				result += relation.get(term);
			}else {
				result += term;
			}
			result += " ";
		}
		return result.trim();
	}
	*/
	public ArrayList<String> getKeyword(ArrayList<BlockAttribute> blockAttributes){
		ArrayList<String> keyword = new ArrayList<>();
		for(BlockAttribute ba : blockAttributes) {
			if(!keyword.contains(ba.getColor()))keyword.add(ba.getColor());
			if(!keyword.contains(ba.getShape()))keyword.add(ba.getShape());
		}
		return keyword;
	}
	public ArrayList<String> changeToName(String befor,ArrayList<BlockAttribute> blockAttributes){
		ArrayList<String>keyword = getKeyword(blockAttributes);
		String[] termArray = befor.split(" ");
		ArrayList<String> termList = new ArrayList<>();
		ArrayList<String> results = new ArrayList<>();
		ArrayList<String> close = new ArrayList<>();
		String result = "";
		for(String term : termArray) {
			termList.add(term);
		}
		return createToName(termList,keyword,results,result,blockAttributes,close,0);
	}
	public ArrayList<String> createToName(ArrayList<String>terms,ArrayList<String>keyword,ArrayList<String>results,String result,ArrayList<BlockAttribute> blockAttributes,ArrayList<String>close,int i) {
		boolean check=true;
		for(;i < terms.size();i++) {
			if(keyword.contains(terms.get(i))) {
				for(BlockAttribute ba : blockAttributes) {
					if((ba.getColor().equals(terms.get(i))||ba.getShape().equals(terms.get(i)))&&!close.contains(ba.getName())) {
						close.add(ba.getName());
						createToName(terms,keyword,results,result+ba.getName()+" ",blockAttributes,close,i+1);
						close.remove(close.size()-1);
					}else {
						check =false;
					}
				}
			}else {
				result += terms.get(i)+" ";
			}
		}
		if(check&&result.split(" ").length==terms.size()) {
			results.add(result.trim());
		}
		return results;
	}
}