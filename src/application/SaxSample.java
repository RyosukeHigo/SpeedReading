package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SaxSample extends DefaultHandler{
	private String tmp = "";
	private int scr = 0;
	private ArrayList<String> result = new ArrayList<String>();
	private HashMap<String, Integer> df = new HashMap<String, Integer>();
	private ArrayList<Integer> score = new ArrayList<Integer>();
	private boolean isSurface = false;
	SaxSample(){
		try {
			File csv = new File("goi.csv"); // CSVデータファイル
			BufferedReader br = new BufferedReader(new FileReader(csv));
			// 最終行まで読み込む
			String line = "";
			while ((line = br.readLine()) != null) {
				// 1行をデータの要素に分割
				StringTokenizer st = new StringTokenizer(line, ",");
				// 1行の各要素をタブ区切りで表示
				String first = st.nextToken();
				String second = st.nextToken();
				String third = st.nextToken();
				String fourth = st.nextToken();
				df.put(second, Integer.valueOf(fourth));
				//System.out.println(second);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外捕捉
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedReaderオブジェクトのクローズ時の例外捕捉
			e.printStackTrace();
		}
	}
	//start Document
	public void startDocument() {
		//System.out.println("ドキュメント開始");
	}
	//start Element
	public void startElement(String uri,
			String localName,
			String qName,
			Attributes attributes) {

		//System.out.println("要素開始:" + qName);
		if(qName.equals("Surface")){
			isSurface = true;
		}
	}

	//Text
	public void characters(char[] ch,
			int offset,
			int length) {

		//System.out.println("テキストデータ：" + new String(ch, offset, length));
		if(isSurface){
			String s =  new String(ch, offset, length);
			tmp = tmp + s;
			if(df.containsKey(s)){
				scr += df.get(s);
			}else{
				scr += 6;
			}
		}
	}

	//End Element
	public void endElement(String uri,
			String localName,
			String qName) {

		//System.out.println("要素終了:" + qName);
		if(qName.equals("Surface")){
			isSurface = false;
		}else if (qName.equals("Chunk")) {
			result.add(tmp);
			tmp = "";
			score.add(scr);
			scr = 0;
		}
	}
	//End of Document
	public void endDocument(){
		//System.out.println("ドキュメント終了");
		//		for (String string : result) {
		//			System.out.println(string);
		//		}
	}
	public ArrayList<String> getResult(){
		return result;
	}
	public  ArrayList<Integer> getScore(){
		return score;
	}

}