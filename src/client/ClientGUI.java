package client;

import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ClientGUI extends Application {
	private static final String windowsTitle = "Quote Service";
	private static final double drawWindowsWidth = 520;
	private static final double drawWindowsHeight = 300;
	private Client client;
	private TextArea textArea;
	

    public static void main(String[] args) {
    	launch(args); 
    }
    
    @Override 
    public void start(Stage primaryStage) throws RemoteException{
    	createClient();
        init(primaryStage);
        client.addClient();
        primaryStage.show();
    }
    
    private void createClient(){
    	client = new Client(this);
    }
   
	private void init(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(windowsTitle);
		primaryStage.setWidth(drawWindowsWidth);
		primaryStage.setHeight(drawWindowsHeight);
		scene.getStylesheets().add("test.css");
		
	
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					client.removeClient();
					System.exit(0);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		
		Button getNewBtn = new Button("Want a new");
		getNewBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				textArea.setText(client.getLastQuote());
			}
		});
		
		Image image = new Image("test.jpeg");    
	    ImageView iv1 = new ImageView();
	    iv1.setImage(image);

		textArea = new TextArea("");
		textArea.setEditable(false);
		textArea.setPrefSize(380, 60);
		textArea.setWrapText(true);
		
		VBox b = new VBox();
		b.setSpacing(10);
		b.setAlignment(Pos.CENTER);
		b.setPadding(new Insets(10,10,10,10));
		b.getChildren().addAll(textArea, getNewBtn);
		root.getChildren().addAll(iv1,b);
	}

    public void displayQuote(String quote){
    	textArea.setText(quote);
    }

}
