package application;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

//import com.sun.javafx.fxml.expression.Expression.Parser.Token;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
public class SampleController implements Initializable{
	@FXML private Button Button1;
	@FXML private Label Label1;
	@FXML private Slider Slider1;
	@FXML private TextArea TextArea1;
	@FXML private Label Speed;
	@FXML private Button test;
	@FXML private Label MinLength;
	@FXML private Button plus;
	@FXML private Button minus;
	private Display d;

	@FXML
	public void onClicked(ActionEvent event){
		d.setString(TextArea1.getText().replaceAll("\n", ""));
		d.setLabel(Label1);
		d.setSlider(Slider1);
		d.setMin(MinLength);
		d.restart();
	}
	@FXML
	public void onDragDetected() {
		Speed.setText(Integer.valueOf(MinLength.getText())*(60*1000)/(650-(int)Slider1.getValue()*6)+"Char/s");
	}
	@FXML
	public void onClikedPlus(){
		MinLength.setText(String.valueOf(Integer.valueOf(MinLength.getText())+1));
	}
	@FXML
	public void onClikedMinus(){
		MinLength.setText(String.valueOf(Integer.valueOf(MinLength.getText())-1));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		d = new Display();
		Label1.textProperty().bind(d.messageProperty());
		Speed.setText(Integer.valueOf(MinLength.getText())*(60*1000)/(650-(int)Slider1.getValue()*6)+"char/min");
	}
	static class Display extends Service<Void> {
		String s;
		Label l;
		Slider sld;
		Label min;
		private String tmp = "";
		private void setString(String s) {
			this.s = s;
		}
		private void setLabel(Label l){
			this.l = l;
		}
		private void setSlider(Slider sld){
			this.sld = sld;
		}
		private void setMin(Label min){
			this.min = min;
		}
		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() {
					YahooAPI API = new YahooAPI(s);
					try {
						API.textAPI();
					} catch (IOException e2) {
						// TODO 自動生成された catch ブロック
						e2.printStackTrace();
					}
					SAXParserFactory spfactory = SAXParserFactory.newInstance();
					SAXParser parser = null;
					try {
						parser = spfactory.newSAXParser();
					} catch (ParserConfigurationException | SAXException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					SaxSample sax = new SaxSample();
					try {
						parser.parse(new File("test.xml"), sax);
					} catch (SAXException | IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					System.out.println("ste");
					final ArrayList<String> TextList = sax.getResult();
					final ArrayList<Integer> ScoreList = sax.getScore();
					TextList.get(0);
					for (int i=0;i<TextList.size();i++) {
						tmp = TextList.get(i);
						System.out.println(tmp);
						updateMessage(tmp);
						int l = tmp.length();
						int s = ScoreList.get(i);
							try {
								Thread.sleep((int)(650 - sld.getValue()*6 + l*10 + s*10));
							} catch (InterruptedException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
							//System.out.println(sld.getValue());
					}
					return null;
				}
			};
		}
	}
}