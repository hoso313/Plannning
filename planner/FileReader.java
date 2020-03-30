package planner;

import java.io.*;
import java.util.*;

class FileReader {

	public static Vector read(String fileName){
		Vector goal = new Vector();
		try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文

			// 文字コードUTF-8を指定してBufferedReaderオブジェクトを作る
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));

			String line;
			StringTokenizer token;
			while ((line = in.readLine()) != null) {
				//改行文字を区切り文字として分割
				token = new StringTokenizer(line, "\n");
				while (token.hasMoreTokens()) {
					goal.addElement(token.nextToken());
				}
			}
			in.close();  // ファイルを閉じる
		} catch (IOException e) {
			e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
		}
		return goal;
	}

	public static ArrayList<String> toArrayList(String fileName) {
		ArrayList<String>result	= new ArrayList<>();
		Vector goals = read(fileName);
		for(Object goal : goals) {
			result.add((String)goal);
		}
		return result;
	}
}
