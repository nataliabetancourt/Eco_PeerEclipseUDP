
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CommunicationUDP extends Thread {

	private DatagramSocket ds;
	private IObserver observer;

	public CommunicationUDP(IObserver observer) {
		this.observer = observer;
	}

	@Override
	public void run() {
		startPeer();
	}

	private void startPeer(){
        try {
            //This users port
            ds = new DatagramSocket(9001);

            //Receive the messages sent to this port
            receiveMessages();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void receiveMessages(){
        while (true) {
            //Array for message length and packet ready for when message is sent
            byte [] forMessage = new byte[100];
            DatagramPacket packet = new DatagramPacket(forMessage, forMessage.length);

            //Try catch while its waiting for packet
            try {
                ds.receive(packet); //Waits till packet arrives

                //Turn bytes from message to string
                String message = new String((packet.getData())).trim(); //Trim excess space off
                observer.notifyMessage(message); //Send message to activity
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        new Thread(()->{
            try {
                //IP where I want to send the message (computer) and port that computer is using
                InetAddress peerIP = InetAddress.getByName("192.168.39.113");
                int peerPort = 9000;

                System.out.println("Enviando mensaje");
                
                //Creating packet to be sent and sending
                DatagramPacket packetSent = new DatagramPacket(message.getBytes(), message.getBytes().length, peerIP, peerPort);
                ds.send(packetSent);
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

}
