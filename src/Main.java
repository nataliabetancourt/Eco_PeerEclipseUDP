import java.util.ArrayList;

import com.google.gson.Gson;

import model.Order;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Main extends PApplet implements IObserver{
	
	private CommunicationUDP udp;
	private Gson gson;
	private ArrayList<Order> orders;
	private PImage hotDog, sandwich, juice, iceCream;
	private PFont font;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	@Override
	public void settings() {
		size(500, 550);
	}
	
	@Override
	public void setup() {
		udp = new CommunicationUDP(this);
		udp.start();
		
		gson = new Gson();
		
		orders = new ArrayList<>();
		
		//Images
		hotDog = loadImage("./data/hotdog.jpg");
		sandwich = loadImage("./data/sandwich.jpg");
		juice = loadImage("./data/juice.jpg");
		iceCream = loadImage("./data/icecream.jpg");
		
		font = createFont("./data/Poppins-Medium.ttf", 18);
		
	}
	
	@Override
	public void draw() {
		background(255);
		paintOrders();
	}
	
	@Override
	public void mousePressed() {
		for (int i = 0; i < orders.size(); i++) {
			//When clicking on the order, send message to let the person know that the order is ready and delete it from the list
			if (mouseX > 40 && mouseX < 120 && mouseY > (70+(100*i)) - 40 && mouseY < (70+(100*i)) + 40) {
				udp.sendMessage("¡El " + orders.get(i).getOrder() + " está listo!");
				orders.remove(i);
			}
		}
		
	}

	@Override
	public void notifyMessage(String message) {
		//Make object out of message
		Order baseOrder = gson.fromJson(message, Order.class);
		
		//Rename order based on number of orders and add the order to the list
		baseOrder.setOrder("Pedido #" + orders.size());
		//Only add orders until screen is full
		if (orders.size() < 5) {
			orders.add(baseOrder);		
		}

		
	}
	
	private void paintOrders() {
		for (int i = 0; i < orders.size(); i++) {
			int yImage = 70;
			switch (orders.get(i).getProduct()) {
			case "hot dog":
				imageMode(CENTER);
				image(hotDog, 80, yImage + (100*i), 80, 80);
				break;
			case "sandwich":
				imageMode(CENTER);
				image(sandwich, 80, yImage + (100*i), 80, 80);
				break;
			case "juice":
				imageMode(CENTER);
				image(juice, 80, yImage + (100*i), 80, 80);
				break;
			case "ice cream":
				imageMode(CENTER);
				image(iceCream, 80, yImage + (100*i), 80, 80);
				break;
			}
			
			textFont(font);
			fill(80);
			textAlign(LEFT);
			text(orders.get(i).getOrder(), 130, 60 + (100*i));
			textSize(14);
			text("Hora: " + orders.get(i).getTime(), 130, 80 + (100*i));	
		}

	}

	
	
}
