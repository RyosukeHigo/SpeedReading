package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class YahooAPI {
	static String input;
	/**
	 * Yahoo!ディベロッパーのAPP ID
	 */
	private static String APP_ID = "dj0zaiZpPXY3U0syYkg4eXR5cSZzPWNvbnN1bWVyc2VjcmV0Jng9MDg-";

	/**
	 * Yahooテキスト分析APIのベースURI
	 */
	private static String BASE_URI = "http://jlp.yahooapis.jp/DAService/V1/parse";
	YahooAPI(String input){
		YahooAPI.input = input;
	}
//	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
//		Scanner sc = new Scanner(System.in);
//		try{
//			String input = sc.next();
//			String utf8 = new String(input.getBytes("UTF8"), "UTF8");
//			textAPI(utf8);
//
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		SAXParserFactory spfactory = SAXParserFactory.newInstance();
//
//        SAXParser parser = spfactory.newSAXParser();
//
//        parser.parse(new File("test.xml"), new SaxSample());
//	}

	public void textAPI() throws IOException{
		String utf8 = new String(input.getBytes("UTF8"), "UTF8");
		if(utf8 != null){
			utf8 = URLEncoder.encode(utf8,"UTF8");
			URL url = new URL(BASE_URI+"?appid="+APP_ID+"&sentence="+utf8);
			//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("kaba", 3128));
			//HttpURLConnection urlconn = (HttpURLConnection)url.openConnection(proxy);
			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.setInstanceFollowRedirects(false);

			urlconn.connect();

			BufferedReader reader =
					new BufferedReader(new InputStreamReader(urlconn.getInputStream()));

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
			File file = new File("test.xml");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.print(response);
			pw.close();
		}
	}

}