package application;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SaxSample extends DefaultHandler{
	private String tmp = "";
	private ArrayList<String> result = new ArrayList<String>();
	private boolean isSurface = false;
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
			tmp = tmp + new String(ch, offset, length);
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

}