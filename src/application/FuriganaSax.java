package application;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class FuriganaSax extends DefaultHandler{
	private String chunk = "";
	private int scr = 0;
	private int len = 0;
	private ArrayList<String> result = new ArrayList<String>();
	private HashMap<String, Integer> df = new HashMap<String, Integer>();
	private ArrayList<Integer> score = new ArrayList<Integer>();
	private ArrayList<Integer> textReadLength = new ArrayList<Integer>();
	private boolean isSurface = false;
	private boolean isReading = false;
	private boolean isPOS = false;
	private String pos = "";
	private String surface = "";
	FuriganaSax(){
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
		}else if(qName.equals("Reading")){
			isReading = true;
		}else if(qName.equals("POS")){
			isPOS = true;
		}
	}

	//Text
	public void characters(char[] ch,
			int offset,
			int length) {

		//System.out.println("テキストデータ：" + new String(ch, offset, length));
		String s =  new String(ch, offset, length);
		if(isSurface){
			surface = s;
			chunk = chunk + surface;
//			if(df.containsKey(s)){
//				scr += df.get(s);
//			}else{
//				scr += 6;
//			}
		}
		if(isReading){
			len += s.length();
		}
		if(isPOS){
			pos = s;
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
			result.add(chunk);
			chunk = "";
			score.add(scr);
			scr = 0;
			textReadLength.add(len);
			len = 0;
		}else if(qName.equals("Reading")){
			isReading = false;
		}else if(qName.equals("POS")){
			if(!pos.equals("助詞")&&!pos.equals("特殊")&&!pos.equals("助動詞")&&!pos.equals("接尾辞")){
				if(df.containsKey(surface)&&df.get(surface)>scr){
					scr = df.get(surface);
				}else {
					scr = 7;
				}
			}
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
	public ArrayList<Integer> getTextReadLength(){
		return textReadLength;
	}

}