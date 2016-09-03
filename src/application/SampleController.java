package application;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

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
		d.setString(TextArea1.getText());
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
	    			Tokenizer tokenizer = Tokenizer.builder().build();
	    			List<Token> tokens = tokenizer.tokenize(s);
	    			tmp = "";
	    			for (Token token : tokens) {
	    				tmp = tmp + token.getSurfaceForm();
	    				if(tmp.length()>=Integer.valueOf(min.getText())){
	    					//Platform.runLater( () -> l.setText(tmp));
	    					//l.setText(tmp);
	    					updateMessage(tmp);
	    					tmp = "";
	    				}else{
	    					continue;
	    				}
	    				try{
	    					Thread.sleep((int)(650 - sld.getValue()*6));
	    					//System.out.println(sld.getValue());
	    				}catch(InterruptedException e){};
	    			}
	    			updateMessage(tmp);
					return null;
	            }
	        };
	    }
	}
}
