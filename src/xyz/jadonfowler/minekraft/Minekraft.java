package xyz.jadonfowler.minekraft;

import java.net.*;
import java.util.*;
import org.spacehq.mc.auth.exception.*;
import org.spacehq.mc.protocol.*;
import org.spacehq.mc.protocol.data.status.*;
import org.spacehq.mc.protocol.data.status.handler.*;
import org.spacehq.packetlib.*;
import org.spacehq.packetlib.tcp.*;
import xyz.jadonfowler.minekraft.entity.*;
import com.nishu.utils.*;

public class Minekraft extends Screen{

    public static Minekraft instance;
    public static final int WIDTH = 1280, HEIGHT = 720;
    public GameLoop gameLoop;
    public EntityPlayer thePlayer;
    public String host;
    public int port;
    public Proxy proxy;
    public String username, password;
    public static Minekraft getInstace() {
        return instance;
    }

    public Minekraft(String host, int port, Proxy proxy, String username, String password) {
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.username = username;
        this.password = password;
        instance = this;
        Window.createWindow(WIDTH, HEIGHT, "Minekraft", false);
        gameLoop = new GameLoop();
        gameLoop.setScreen(this);
        gameLoop.setDebugMode(true);
        gameLoop.start(60);//TODO Option for FPS cap
        //status();
    }

    private void status() {
        MinecraftProtocol protocol = new MinecraftProtocol(ProtocolMode.STATUS);
        Client client = new Client(host, port, protocol, new TcpSessionFactory(proxy));
        client.getSession().setFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {

            @Override public void handle(Session session, ServerStatusInfo info) {
                System.out.println("Version: " + info.getVersionInfo().getVersionName() + ", "
                        + info.getVersionInfo().getProtocolVersion());
                System.out.println("Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / "
                        + info.getPlayerInfo().getMaxPlayers());
                System.out.println("Players: " + Arrays.toString(info.getPlayerInfo().getPlayers()));
                System.out.println("Description: " + info.getDescription().getFullText());
                System.out.println("Icon: " + info.getIcon());
            }
        });
        client.getSession().setFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {

            @Override public void handle(Session session, long pingTime) {
                System.out.println("Server ping took " + pingTime + "ms");
            }
        });
        client.getSession().connect();
        while (client.getSession().isConnected()) {
            try {
                Thread.sleep(5);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private  void login() {
        MinecraftProtocol protocol = null;
            try {
                protocol = new MinecraftProtocol(username, password, false);
                System.out.println("> Successfully authenticated user.");
            }
            catch (AuthenticationException e) {
                e.printStackTrace();
                return;
            }
        Client client = new Client(host, port, protocol, new TcpSessionFactory(proxy));
        client.getSession().addListener(new PacketHandler());
        client.getSession().connect();
    }

    @Override public void dispose() {
        
    }

    @Override public void init() {
        
    }

    @Override public void initGL() {
        
    }

    @Override public void render() {
        
    }

    @Override public void update() {
        
    }
}
