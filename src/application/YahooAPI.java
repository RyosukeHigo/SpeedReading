package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

public class YahooAPI {
	static String input;
	static boolean isChecked;
	/**
	 * Yahoo!ディベロッパーのAPP ID
	 */
	private static String APP_ID = "dj0zaiZpPXY3U0syYkg4eXR5cSZzPWNvbnN1bWVyc2VjcmV0Jng9MDg-";

	/**
	 * Yahooテキスト分析APIのベースURI
	 */
	private static String PARSE_URL = "http://jlp.yahooapis.jp/DAService/V1/parse";
	private static String FURIGANA_URL = "http://jlp.yahooapis.jp/FuriganaService/V1/furigana";

	YahooAPI(String input, boolean isChecked){
		YahooAPI.input = input;
		YahooAPI.isChecked = isChecked;
	}
	public void textAPI(String type) throws IOException{
		String utf8 = new String(input.getBytes("UTF8"), "UTF8");
		if(utf8 != null){
			utf8 = URLEncoder.encode(utf8,"UTF8");
			URL url = null;
			if(type.equals("parse")){
				url = new URL(PARSE_URL+"?appid="+APP_ID+"&sentence="+utf8);
			}else if(type.equals("furigana")){
				url = new URL(FURIGANA_URL+"?appid="+APP_ID+"&sentence="+utf8);
			}
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.uec.ac.jp", 8080));
			HttpURLConnection urlconn;
			if(isChecked){
				urlconn = (HttpURLConnection)url.openConnection(proxy);
			}else{
				urlconn = (HttpURLConnection)url.openConnection();
			}
			urlconn.setRequestMethod("GET");
			urlconn.setInstanceFollowRedirects(false);
			urlconn.connect();
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(urlconn.getInputStream(),"UTF-8"));

			StringBuffer responseBuffer = new StringBuffer();
			while (true){
				String line = reader.readLine();
				if ( line == null ){
					break;
				}
				responseBuffer.append(line);
			}
			reader.close();
			urlconn.disconnect();
			String response = responseBuffer.toString();
			//System.out.println(response);
			File file = new File(type +".xml");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8")));
			pw.print(response);
			pw.close();
		}
	}

}