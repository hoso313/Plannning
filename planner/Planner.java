package planner;

import java.util.*;

import planner.FileReader;

public class Planner {
	Vector operators;
	Random rand;
	Vector plan;
	static ArrayList<String> result = new ArrayList<>();////////////////////////////////////

	public static void main(String argv[]) {
		(new Planner()).start();
	}

	Planner() {
		result = new ArrayList<>();/////////////////////////////////////
		rand = new Random();
	}

	public void start() {
		initOperators();
		Vector goalList = initGoalList();
		Vector initialState = initInitialState();

		Hashtable theBinding = new Hashtable();
		plan = new Vector();
		boolean tt = planning(goalList, initialState, theBinding);
		if (!tt) {
			System.out.println("nasi");
			return;
		}
		System.out.println("***** This is a plan! *****");
		for (int i = 0; i < plan.size(); i++) {
			Operator op = (Operator) plan.elementAt(i);
			System.out.println((op.instantiate(theBinding)).name);
			result.add((op.instantiate(theBinding)).name);/////////////////////////////////////////
		}
	}

	private boolean planning(Vector theGoalList, Vector theCurrentState, Hashtable theBinding) {
		System.out.println("*** GOALS ***" + theGoalList);
		if (theGoalList.size() == 1) {
			String aGoal = (String) theGoalList.elementAt(0);
			if (planningAGoal(aGoal, theCurrentState, theBinding, 0) != -1) {
				return true;
			} else {
				return false;
			}
		} else {
			String aGoal = (String) theGoalList.elementAt(0);
			int cPoint = 0;
			while (cPoint < operators.size()) {
				// System.out.println("cPoint:"+cPoint);
				// Store original binding
				Hashtable orgBinding = new Hashtable();
				for (Enumeration e = theBinding.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = (String) theBinding.get(key);
					orgBinding.put(key, value);
				}
				Vector orgState = new Vector();
				for (int i = 0; i < theCurrentState.size(); i++) {
					orgState.addElement(theCurrentState.elementAt(i));
				}

				int tmpPoint = planningAGoal(aGoal, theCurrentState, theBinding, cPoint);
				// System.out.println("tmpPoint: "+tmpPoint);
				if (tmpPoint != -1) {
					theGoalList.removeElementAt(0);
					System.out.println(theCurrentState);
					if (planning(theGoalList, theCurrentState, theBinding)) {
						// System.out.println("Success !");
						return true;
					} else {
						cPoint = tmpPoint;
						// System.out.println("Fail::"+cPoint);
						theGoalList.insertElementAt(aGoal, 0);

						theBinding.clear();
						for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
							String key = (String) e.nextElement();
							String value = (String) orgBinding.get(key);
							theBinding.put(key, value);
						}
						theCurrentState.removeAllElements();
						for (int i = 0; i < orgState.size(); i++) {
							theCurrentState.addElement(orgState.elementAt(i));
						}
					}
				} else {
					theBinding.clear();
					for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
						String key = (String) e.nextElement();
						String value = (String) orgBinding.get(key);
						theBinding.put(key, value);
					}
					theCurrentState.removeAllElements();
					for (int i = 0; i < orgState.size(); i++) {
						theCurrentState.addElement(orgState.elementAt(i));
					}
					return false;
				}
			}
			return false;
		}
	}

	private int planningAGoal(String theGoal, Vector theCurrentState, Hashtable theBinding, int cPoint) {
		System.out.println("**" + theGoal);
		int size = theCurrentState.size();
		for (int i = 0; i < size; i++) {
			String aState = (String) theCurrentState.elementAt(i);
			if ((new Unifier()).unify(theGoal, aState, theBinding)) {
				return 0;
			}
		}

		// 競合解決のための4行（初期状態では乱数使ってオペレータの順番を適当に入れ替えてる)
		// int randInt = Math.abs(rand.nextInt()) % operators.size();
		// Operator op = (Operator)operators.elementAt(randInt);
		// System.out.println(operators.elementAt(randInt)+"わけわからん");
		// operators.removeElementAt(randInt);
		// operators.addElement(op);

		// 自分で考えた競合解決(どのルールが発火したことあるかと、最も以前に発火したルールがどれかをわかるように)
		// とりあえずaddの行数順にソートしてみる
		for (int i = 0; i < operators.size() - 1; i++) {

			for (int j = operators.size() - 1; j > i; j--) {

				int length1 = ((Operator) (operators.elementAt(j - 1))).getAddList().size();
				int length2 = ((Operator) (operators.elementAt(j))).getAddList().size();
				if (length2 < length1) {
					Operator op = (Operator) operators.elementAt(j - 1);
					operators.removeElementAt(j - 1);
					operators.add(j, op);

				}
			}

		}

		for (int i = cPoint; i < operators.size(); i++) {
			// ここで適用するオペレータを選んでいる？
			Operator anOperator = rename((Operator) operators.elementAt(i));
			// System.out.println((Operator)operators.elementAt(i)+"選ばれた");
			// 現在のCurrent state, Binding, planをbackup
			Hashtable orgBinding = new Hashtable();
			for (Enumeration e = theBinding.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) theBinding.get(key);
				orgBinding.put(key, value);
			}
			Vector orgState = new Vector();
			for (int j = 0; j < theCurrentState.size(); j++) {
				orgState.addElement(theCurrentState.elementAt(j));
			}
			Vector orgPlan = new Vector();
			for (int j = 0; j < plan.size(); j++) {
				orgPlan.addElement(plan.elementAt(j));
			}

			Vector addList = (Vector) anOperator.getAddList();
			for (int j = 0; j < addList.size(); j++) {
				if ((new Unifier()).unify(theGoal, (String) addList.elementAt(j), theBinding)) {
					Operator newOperator = anOperator.instantiate(theBinding);
					Vector newGoals = (Vector) newOperator.getIfList();
					System.out.println(newOperator.name);
					if (planning(newGoals, theCurrentState, theBinding)) {
						System.out.println(newOperator.name);
						plan.addElement(newOperator);
						theCurrentState = newOperator.applyState(theCurrentState);
						return i + 1;
					} else {
						// 失敗したら元に戻す．
						theBinding.clear();
						for (Enumeration e = orgBinding.keys(); e.hasMoreElements();) {
							String key = (String) e.nextElement();
							String value = (String) orgBinding.get(key);
							theBinding.put(key, value);
						}
						theCurrentState.removeAllElements();
						for (int k = 0; k < orgState.size(); k++) {
							theCurrentState.addElement(orgState.elementAt(k));
						}
						plan.removeAllElements();
						for (int k = 0; k < orgPlan.size(); k++) {
							plan.addElement(orgPlan.elementAt(k));
						}
					}
				}
			}
		}
		return -1;
	}

	int uniqueNum = 0;

	private Operator rename(Operator theOperator) {
		Operator newOperator = theOperator.getRenamedOperator(uniqueNum);
		uniqueNum = uniqueNum + 1;
		return newOperator;
	}

	private Vector initGoalList() {
		Vector goalList = new Vector();
		/*
		goalList.addElement("triangle on green");
		goalList.addElement("B on triangle");
		*/
		goalList = FileReader.read("rep05/goal.txt");/////////////////////////////////////
		return goalList;
	}

	private Vector initInitialState() {
		Vector initialState = new Vector();
		/*初期状態一覧
		clear A
		clear B
		clear C
		ontable A
		ontable B
		ontable C
		handEmpty
		A is color of red
		B is color of blue
		C is color of green
		A is shape of triangle
		B is shape of square
		C is shape of circle
		*/
		initialState = FileReader.read("rep05/initialState.txt");/////////////////////////////////////////////
		return initialState;
	}

	private void initOperators() {
		operators = new Vector();

		// OPERATOR 1
		/// NAME
		String name1 = new String("Place ?x on ?y");
		/// IF
		Vector ifList1 = new Vector();
		ifList1.addElement(new String("clear ?y"));
		ifList1.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList1 = new Vector();
		addList1.addElement(new String("?x on ?y"));
		addList1.addElement(new String("clear ?x"));
		addList1.addElement(new String("handEmpty"));
		/// DELETE-LIST
		Vector deleteList1 = new Vector();
		deleteList1.addElement(new String("clear ?y"));
		deleteList1.addElement(new String("holding ?x"));
		Operator operator1 = new Operator(name1, ifList1, addList1, deleteList1);
		operators.addElement(operator1);

		// OPERATOR 2
		/// NAME
		String name2 = new String("remove ?x from on top ?y");
		/// IF
		Vector ifList2 = new Vector();
		ifList2.addElement(new String("?x on ?y"));
		ifList2.addElement(new String("clear ?x"));
		ifList2.addElement(new String("handEmpty"));
		/// ADD-LIST
		Vector addList2 = new Vector();
		addList2.addElement(new String("clear ?y"));
		addList2.addElement(new String("holding ?x"));
		/// DELETE-LIST
		Vector deleteList2 = new Vector();
		deleteList2.addElement(new String("?x on ?y"));
		deleteList2.addElement(new String("clear ?x"));
		deleteList2.addElement(new String("handEmpty"));
		Operator operator2 = new Operator(name2, ifList2, addList2, deleteList2);
		operators.addElement(operator2);

		// OPERATOR 3
		/// NAME
		String name3 = new String("pick up ?x from the table");
		/// IF
		Vector ifList3 = new Vector();
		ifList3.addElement(new String("ontable ?x"));
		ifList3.addElement(new String("clear ?x"));
		ifList3.addElement(new String("handEmpty"));
		/// ADD-LIST
		Vector addList3 = new Vector();
		addList3.addElement(new String("holding ?x"));
		/// DELETE-LIST
		Vector deleteList3 = new Vector();
		deleteList3.addElement(new String("ontable ?x"));
		deleteList3.addElement(new String("clear ?x"));
		deleteList3.addElement(new String("handEmpty"));
		Operator operator3 = new Operator(name3, ifList3, addList3, deleteList3);
		operators.addElement(operator3);

		// OPERATOR 4
		/// NAME
		String name4 = new String("put ?x down on the table");
		/// IF
		Vector ifList4 = new Vector();
		ifList4.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList4 = new Vector();
		addList4.addElement(new String("ontable ?x"));
		addList4.addElement(new String("clear ?x"));
		addList4.addElement(new String("handEmpty"));
		/// DELETE-LIST
		Vector deleteList4 = new Vector();
		deleteList4.addElement(new String("holding ?x"));
		Operator operator4 = new Operator(name4, ifList4, addList4, deleteList4);
		operators.addElement(operator4);

		// COLOR

		// OPERATOR5
		String name5 = new String("?x is color of ?a");
		/// IF
		Vector ifList5 = new Vector();
		ifList5.addElement(new String("?x is color of ?a"));
		ifList5.addElement(new String("clear ?x"));
		/// ADD-LIST
		Vector addList5 = new Vector();
		addList5.addElement(new String("clear ?a"));
		/// DELETE-LIST
		Vector deleteList5 = new Vector();
		deleteList5.addElement(new String("?x is color of ?a"));
		deleteList5.addElement(new String("clear ?x"));
		Operator operator5 = new Operator(name5, ifList5, addList5, deleteList5);
		operators.addElement(operator5);

		// OPERATOR6
		String name6 = new String("?x is color of ?a");
		/// IF
		Vector ifList6 = new Vector();
		ifList6.addElement(new String("?x is color of ?a"));
		ifList6.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList6 = new Vector();
		addList6.addElement(new String("holding ?a"));
		/// DELETE-LIST
		Vector deleteList6 = new Vector();
		deleteList6.addElement(new String("?x is color of ?a"));
		deleteList6.addElement(new String("holding ?x"));
		Operator operator6 = new Operator(name6, ifList6, addList6, deleteList6);
		operators.addElement(operator6);

		// OPERATOR7
		String name7 = new String("?x is color of ?a");
		/// IF
		Vector ifList7 = new Vector();
		ifList7.addElement(new String("?x is color of ?a"));
		ifList7.addElement(new String("ontable ?x"));
		/// ADD-LIST
		Vector addList7 = new Vector();
		addList7.addElement(new String("ontable ?a"));
		/// DELETE-LIST
		Vector deleteList7 = new Vector();
		deleteList7.addElement(new String("?x is color of ?a"));
		deleteList7.addElement(new String("ontable ?x"));
		Operator operator7 = new Operator(name7, ifList7, addList7, deleteList7);
		operators.addElement(operator7);

		// SHAPE

		// OPERATOR8
		String name8 = new String("?x is shape of ?a");
		/// IF
		Vector ifList8 = new Vector();
		ifList8.addElement(new String("?x is shape of ?a"));
		ifList8.addElement(new String("clear ?x"));
		/// ADD-LIST
		Vector addList8 = new Vector();
		addList8.addElement(new String("clear ?a"));
		/// DELETE-LIST
		Vector deleteList8 = new Vector();
		deleteList8.addElement(new String("?x is shape of ?a"));
		deleteList8.addElement(new String("clear ?x"));
		Operator operator8 = new Operator(name8, ifList8, addList8, deleteList8);
		operators.addElement(operator8);

		// OPERATOR9
		String name9 = new String("?x is shape of ?a");
		/// IF
		Vector ifList9 = new Vector();
		ifList9.addElement(new String("?x is shape of ?a"));
		ifList9.addElement(new String("holding ?x"));
		/// ADD-LIST
		Vector addList9 = new Vector();
		addList9.addElement(new String("holding ?a"));
		/// DELETE-LIST
		Vector deleteList9 = new Vector();
		deleteList9.addElement(new String("?x is shape of ?a"));
		deleteList9.addElement(new String("holding ?x"));
		Operator operator9 = new Operator(name9, ifList9, addList9, deleteList9);
		operators.addElement(operator9);

		// OPERATOR10
		String name10 = new String("?x is shape of ?a");
		/// IF
		Vector ifList10 = new Vector();
		ifList10.addElement(new String("?x is shape of ?a"));
		ifList10.addElement(new String("ontable ?x"));
		/// ADD-LIST
		Vector addList10 = new Vector();
		addList10.addElement(new String("ontable ?a"));
		/// DELETE-LIST
		Vector deleteList10 = new Vector();
		deleteList10.addElement(new String("?x is shape of ?a"));
		deleteList10.addElement(new String("ontable ?x"));
		Operator operator10 = new Operator(name10, ifList10, addList10, deleteList10);
		operators.addElement(operator10);
	}
}

class Operator {
	String name;
	Vector ifList;
	Vector addList;
	Vector deleteList;

	Operator(String theName, Vector theIfList, Vector theAddList, Vector theDeleteList) {
		name = theName;
		ifList = theIfList;
		addList = theAddList;
		deleteList = theDeleteList;
	}

	public Vector getAddList() {
		return addList;
	}

	public Vector getDeleteList() {
		return deleteList;
	}

	public Vector getIfList() {
		return ifList;
	}

	public String toString() {
		String result = "NAME: " + name + "\n" + "IF :" + ifList + "\n" + "ADD:" + addList + "\n" + "DELETE:"
				+ deleteList;
		return result;
	}

	public Vector applyState(Vector theState) {
		for (int i = 0; i < addList.size(); i++) {
			theState.addElement(addList.elementAt(i));
		}
		for (int i = 0; i < deleteList.size(); i++) {
			theState.removeElement(deleteList.elementAt(i));
		}
		return theState;
	}

	public Operator getRenamedOperator(int uniqueNum) {
		Vector vars = new Vector();
		// IfListの変数を集める
		for (int i = 0; i < ifList.size(); i++) {
			String anIf = (String) ifList.elementAt(i);
			vars = getVars(anIf, vars);
		}
		// addListの変数を集める
		for (int i = 0; i < addList.size(); i++) {
			String anAdd = (String) addList.elementAt(i);
			vars = getVars(anAdd, vars);
		}
		// deleteListの変数を集める
		for (int i = 0; i < deleteList.size(); i++) {
			String aDelete = (String) deleteList.elementAt(i);
			vars = getVars(aDelete, vars);
		}
		Hashtable renamedVarsTable = makeRenamedVarsTable(vars, uniqueNum);

		// 新しいIfListを作る
		Vector newIfList = new Vector();
		for (int i = 0; i < ifList.size(); i++) {
			String newAnIf = renameVars((String) ifList.elementAt(i), renamedVarsTable);
			newIfList.addElement(newAnIf);
		}
		// 新しいaddListを作る
		Vector newAddList = new Vector();
		for (int i = 0; i < addList.size(); i++) {
			String newAnAdd = renameVars((String) addList.elementAt(i), renamedVarsTable);
			newAddList.addElement(newAnAdd);
		}
		// 新しいdeleteListを作る
		Vector newDeleteList = new Vector();
		for (int i = 0; i < deleteList.size(); i++) {
			String newADelete = renameVars((String) deleteList.elementAt(i), renamedVarsTable);
			newDeleteList.addElement(newADelete);
		}
		// 新しいnameを作る
		String newName = renameVars(name, renamedVarsTable);

		return new Operator(newName, newIfList, newAddList, newDeleteList);
	}

	private Vector getVars(String thePattern, Vector vars) {
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				vars.addElement(tmp);
			}
		}
		return vars;
	}

	private Hashtable makeRenamedVarsTable(Vector vars, int uniqueNum) {
		Hashtable result = new Hashtable();
		for (int i = 0; i < vars.size(); i++) {
			String newVar = (String) vars.elementAt(i) + uniqueNum;
			result.put((String) vars.elementAt(i), newVar);
		}
		return result;
	}

	private String renameVars(String thePattern, Hashtable renamedVarsTable) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + (String) renamedVarsTable.get(tmp);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	public Operator instantiate(Hashtable theBinding) {
		// name を具体化
		String newName = instantiateString(name, theBinding);
		// ifList を具体化
		Vector newIfList = new Vector();
		for (int i = 0; i < ifList.size(); i++) {
			String newIf = instantiateString((String) ifList.elementAt(i), theBinding);
			newIfList.addElement(newIf);
		}
		// addList を具体化
		Vector newAddList = new Vector();
		for (int i = 0; i < addList.size(); i++) {
			String newAdd = instantiateString((String) addList.elementAt(i), theBinding);
			newAddList.addElement(newAdd);
		}
		// deleteListを具体化
		Vector newDeleteList = new Vector();
		for (int i = 0; i < deleteList.size(); i++) {
			String newDelete = instantiateString((String) deleteList.elementAt(i), theBinding);
			newDeleteList.addElement(newDelete);
		}
		return new Operator(newName, newIfList, newAddList, newDeleteList);
	}

	private String instantiateString(String thePattern, Hashtable theBinding) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				String newString = (String) theBinding.get(tmp);
				if (newString == null) {
					result = result + " " + tmp;
				} else {
					result = result + " " + newString;
				}
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}
}

class Unifier {
	StringTokenizer st1;
	String buffer1[];
	StringTokenizer st2;
	String buffer2[];
	Hashtable vars;

	Unifier() {
		// vars = new Hashtable();
	}

	public boolean unify(String string1, String string2, Hashtable theBindings) {
		Hashtable orgBindings = new Hashtable();
		for (Enumeration e = theBindings.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) theBindings.get(key);
			orgBindings.put(key, value);
		}
		this.vars = theBindings;
		if (unify(string1, string2)) {
			return true;
		} else {
			// 失敗したら元に戻す．
			theBindings.clear();
			for (Enumeration e = orgBindings.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String value = (String) orgBindings.get(key);
				theBindings.put(key, value);
			}
			return false;
		}
	}

	public boolean unify(String string1, String string2) {
		// 同じなら成功
		if (string1.equals(string2))
			return true;

		// 各々トークンに分ける
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が異なったら失敗
		if (st1.countTokens() != st2.countTokens())
			return false;

		// 定数同士
		int length = st1.countTokens();
		buffer1 = new String[length];
		buffer2 = new String[length];
		for (int i = 0; i < length; i++) {
			buffer1[i] = st1.nextToken();
			buffer2[i] = st2.nextToken();
		}

		// 初期値としてバインディングが与えられていたら
		if (this.vars.size() != 0) {
			for (Enumeration keys = vars.keys(); keys.hasMoreElements();) {
				String key = (String) keys.nextElement();
				String value = (String) vars.get(key);
				replaceBuffer(key, value);
			}
		}

		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				return false;
			}
		}

		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
		if (var(token1) && var(token2))
			return varMatching(token1, token2);
		return false;
	}

	boolean varMatching(String vartoken, String token) {
		if (vars.containsKey(vartoken)) {
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {
			replaceBuffer(vartoken, token);
			if (vars.contains(vartoken)) {
				replaceBindings(vartoken, token);
			}
			vars.put(vartoken, token);
		}
		return true;
	}

	void replaceBuffer(String preString, String postString) {
		for (int i = 0; i < buffer1.length; i++) {
			if (preString.equals(buffer1[i])) {
				buffer1[i] = postString;
			}
			if (preString.equals(buffer2[i])) {
				buffer2[i] = postString;
			}
		}
	}

	void replaceBindings(String preString, String postString) {
		Enumeration keys;
		for (keys = vars.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			if (preString.equals(vars.get(key))) {
				vars.put(key, postString);
			}
		}
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

}
