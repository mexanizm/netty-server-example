# Simple example netty server

    This server is simple example shows how to transfer Java objects between client-server application in netty

## Client on JavaFx example. 

### Connection
```java
       //Connect 
       private Channel Connect(String host , int port) throws InterruptedException {
               if(connected.get()){
                   return channel;
               }
               this.group = new NioEventLoopGroup();
               try{
                   Bootstrap clientBootstrap = new Bootstrap();
       
                   clientBootstrap.group(this.group);
                   clientBootstrap.channel(NioSocketChannel.class);
                   clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
                   clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                       protected void initChannel(SocketChannel socketChannel) throws Exception {
                           socketChannel.pipeline().addFirst("decoder" , new ObjectDecoder(ClassResolvers.cacheDisabled(Packet.class.getClassLoader())));
                           socketChannel.pipeline().addAfter("decoder" ,"encoder" , new ObjectEncoder());
                           socketChannel.pipeline().addLast(new ClientHandler());
                       }
                   });
                   ChannelFuture channelFuture = clientBootstrap.connect().sync();
                   return channelFuture.channel();
               } catch (InterruptedException e) {
                   e.printStackTrace();
                   //Log here
               }
               return null;
           }
```
### Client handler
```java           
           public class ClientHandler extends ChannelInboundHandlerAdapter {
           
               @Override
               public void channelActive(ChannelHandlerContext channelHandlerContext){
                   //On channel active
               }
           
               @Override
               public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
                   cause.printStackTrace();
                   channelHandlerContext.close();
               }
           
               @Override
               public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                   Packet pc = (Packet) msg;
                   //Do some on recivie message 
                   super.channelRead(ctx, msg);
               }
           }
```
### Call connection
```java
           private BooleanProperty connected = new SimpleBooleanProperty(false);
           Task<Channel> task = new Task<Channel>() {
                            @Override
                            protected Channel call() throws Exception {
                               return SocketClient.this.Connect("127.0.0.1" , 9090);
                            }
                
                            @Override
                            protected void succeeded() {
                                channel = getValue();
                                connected.set(true);
                            }
                
                            @Override
                            protected void failed() {
                                Throwable exc = getException();
                                exc.printStackTrace();
                                //Log here
                                connected.set(false);
                                group.shutdownGracefully();
                            }
           }
           new Thread(task).start();
```
### Send object method 
```java
           public <T extends Packet> void sendPacket(T packet){
                   if(!connected.get()){
                       return;
                   }
                   Task task = new Task() {
                       @Override
                       protected Object call() throws Exception {
                           channel.writeAndFlush(packet).sync();
                           return null;
                       }
                       @Override
                       protected void failed() {
                           Throwable exc = getException();
                           exc.printStackTrace();
                           //Log here
                       }
                   };
                   new Thread(task).start();
               }
           
```
## !!! PACKAGE NAME IN CLIENT AND SERVER MUST BE THE SAME. In other case we have `ClassNotFoundException`!!!
#### in this example we have package "exampleServer" and "packetHandlers" in sources path. packetHandlers is the Packet's packege in both (client & server) applications

### Packet example
```java
            package packetHandlers;
            import java.io.Serializable;
            
            public abstract class Packet implements Serializable {
            
                private UserHandler userHandler;
            
                public UserHandler getUserHandler() {
                    return userHandler;
                }
            
                public void setUserHandler(UserHandler userHandler) {
                    this.userHandler = userHandler;
                }
            }
```
### Pacet data handler class
```java
            package packetHandlers;
            import java.io.Serializable;
            
            public class ChatMessage extends Packet implements Serializable {
            
                private String message;
            
                public ChatMessage(){}
            
                public ChatMessage(String message) {
                    super();
                    this.message = message;
                }
            
                public String getMessage() {
                    return message;
                }
            
                public void setMessage(String message) {
                    this.message = message;
                }
            }
```