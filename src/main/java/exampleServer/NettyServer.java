package exampleServer;

public class NettyServer {

    public static void main(String[] args) throws Exception{
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 9080;
        }
        System.out.println("Server Start "+port);
        new Server(port).run();
    }

}
