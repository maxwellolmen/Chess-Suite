import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

public class CommTester {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setPort(3001);

        final SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                System.out.println(data.getMessage());
            }
        });

        server.addEventListener("signal", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                System.out.println(s);
            }
        });

        server.addConnectListener(socketIOClient -> {
            socketIOClient.sendEvent("msg", "hi");
            System.out.println("Client connected");
        });

        server.start();
    }
}
