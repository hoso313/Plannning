package planner;

import java.io.*;
import java.util.*;

class FileWriter{
	public static void write(String[] lines,String fileName){
		try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
			// 文字コードUTF-8を指定してPrintWriterオブジェクトを作る。
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
			for(String line : lines){
				out.print(line.trim());
				out.print("\n");
			}
			out.close();        // ファイルを閉じる
		} catch (IOException e) {
			e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
		}
	}
	public static void write(ArrayList<String> lines,String fileName){
		try {    // ファイル読み込みに失敗した時の例外処理のためのtry-catch構文
			// 文字コードUTF-8を指定してPrintWriterオブジェクトを作る。
			PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
			for(String line : lines){
				out.print(line.trim());
				out.print("\n");
			}
			out.close();        // ファイルを閉じる
		} catch (IOException e) {
			e.printStackTrace(); // 例外が発生した所までのスタックトレースを表示
		}
	}
}
