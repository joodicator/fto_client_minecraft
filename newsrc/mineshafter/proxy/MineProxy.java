 package mineshafter.proxy;
 
 import com.sun.net.httpserver.Headers;
 import com.sun.net.httpserver.HttpExchange;
 import com.sun.net.httpserver.HttpHandler;
 import com.sun.net.httpserver.HttpServer;
 import com.sun.net.httpserver.HttpsConfigurator;
 import com.sun.net.httpserver.HttpsServer;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.io.PrintStream;
 import java.net.HttpURLConnection;
 import java.net.InetSocketAddress;
 import java.net.Proxy;
 import java.net.URI;
 import java.net.URL;
 import java.net.UnknownHostException;
 import java.security.KeyStore;
 import java.util.List;
 import java.util.Map;
 import javax.net.ssl.KeyManagerFactory;
 import javax.net.ssl.SSLContext;
 import javax.net.ssl.TrustManagerFactory;
 import mineshafter.util.Resources;
 import mineshafter.util.Streams;
 
 public class MineProxy
 {
   public static float version = 2.9F;
   public static int port = 0;
   public static int httpport = 0;
   public static int httpsport = 0;
   public static String authServer = Resources.loadString("auth");
   public static String skinServer = authServer;
   public static URLRewriter[] rewriters = null;
 
   public static void listen(int port, int httpport, int httpsport, float version) throws UnknownHostException, IOException {
     listen(port, httpport, httpsport, version, null);
   }
 
   public static void listen(int port, int httpport, int httpsport, float version, String authServer) throws UnknownHostException, IOException {
     try {
       if (authServer == null) {
         authServer = MineProxy.authServer;
       }
 
       rewriters = new URLRewriter[] { 
         new URLRewriter(
            "http://s3\\.amazonaws\\.com/MinecraftSkins/(.+?)\\.png",
            "http://" + skinServer + "/game/getskin.php?name=%s"),
         new URLRewriter(
            "http://skins\\.minecraft\\.net/MinecraftSkins/(.+?)\\.png",
            "http://" + skinServer + "/game/getskin.php?name=%s"),
         new URLRewriter(
            "http://s3\\.amazonaws\\.com/MinecraftCloaks/(.+?)\\.png",
            "http://" + authServer + "/game/getcloak.php?user=%s"),
         new URLRewriter(
            "http://skins\\.minecraft\\.net/MinecraftCloaks/(.+?)\\.png",
            "http://" + authServer + "/game/getcloak.php?user=%s"),
         new URLRewriter(
            "http://www\\.minecraft\\.net/game/(.*)",
            "http://" + authServer + "/game/%s"),
         new URLRewriter(
            "http://session\\.minecraft\\.net/game/(.*)",
            "http://" + authServer + "/game/%s") };
       version = version;
       port = port;
       httpport = httpport;
       httpsport = httpsport;
 
       KeyStore ks = KeyStore.getInstance("JKS");
       char[] pass = "password".toCharArray();
       ks.load(Resources.load("keys.jks"), pass);
       KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
       TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
       kmf.init(ks, pass);
       tmf.init(ks);
 
       SSLContext context = SSLContext.getInstance("TLS");
       context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
       HttpServer server = HttpServer.create(new InetSocketAddress(port), 4);
       server.createContext("/", new ProxyHandler());
       server.setExecutor(null);
 
       server.start();
 
       HttpsServer sslserver = HttpsServer.create(new InetSocketAddress(httpsport), 4);
       sslserver.setHttpsConfigurator(new HttpsConfigurator(context));
       sslserver.createContext("/", new ProxyHandler());
       sslserver.setExecutor(null);
 
       sslserver.start();
     }
     catch (Exception e)
     {
       System.out.println("Proxy starting error:");
     }
   }
 
   public static String RewriteURL(String u)
   {
     try
     {
       for (URLRewriter ur : rewriters) {
         String newurl = ur.MatchAndReplace(u);
         if (newurl != null) {
           u = newurl;
           break;
         }
       }
 
       if (new URL(u).getPath().endsWith("/game/getversion.php"))
         u = u + "?proxy=" + Float.toString(version);
     }
     catch (Exception e) {
       System.out.println("URL couldn't be rewritten:");
     }
 
     return u;
   }
 
   public static class ProxyHandler
     implements HttpHandler
   {
     public void handle(HttpExchange t)
       throws IOException
     {
       String method = t.getRequestMethod();
       String url = makeURL(t);
       String murl = MineProxy.RewriteURL(url);
 
       System.out.println("Request: " + method + " " + url + "\nWill go to " + murl);
 
       URL urlm = new URL(murl);
       String host = urlm.getHost();
 
       boolean post = method.equalsIgnoreCase("POST");
 
       HttpURLConnection c = (HttpURLConnection)new URL(murl).openConnection(Proxy.NO_PROXY);
       c.setRequestMethod(method);
 
       if (post) {
         c.setDoOutput(true);
       }
 
       transferHeaders(t, c, host);
 
       if (post) {
         Streams.pipeStreams(t.getRequestBody(), c.getOutputStream());
       }
 
       transferHeaders(c, t);
       int contentLength = c.getContentLength();
 
       if (contentLength == -1) {
         String encoding = (String)((List)c.getHeaderFields().get("Transfer-Encoding")).get(0);
 
         if (encoding.equalsIgnoreCase("chunked")) {
           contentLength = 0;
         }
       }
 
       System.out.println("resp: " + c.getResponseCode() + ", len: " + contentLength);
 
       t.sendResponseHeaders(c.getResponseCode(), contentLength);
       OutputStream out = t.getResponseBody();
 
       Streams.pipeStreams(c.getInputStream(), out);
       out.close();
     }
 
     protected void transferHeaders(HttpExchange t, HttpURLConnection c, String host)
     {
       Headers headers = t.getRequestHeaders();
 
       for (Object h_obj : headers.keySet())
         if (h_obj != null)
         {
                    String h = (String) h_obj;
           if (h.equalsIgnoreCase("host"))
             c.setRequestProperty(h, host);
           else
             c.setRequestProperty(h, headers.getFirst(h));
         }
     }
 
     protected void transferHeaders(HttpURLConnection c, HttpExchange t)
     {
       Map serverHeaders = c.getHeaderFields();
       Headers clientHeaders = t.getResponseHeaders();
 
       for (Object h_obj : serverHeaders.keySet())
         if (h_obj != null)
         {
                    String h = (String) h_obj;
           clientHeaders.put(h, (List)serverHeaders.get(h));
         }
     }
 
     protected String makeURL(HttpExchange t) {
       URI uri = t.getRequestURI();
       String host = uri.getHost();
 
       if (host == null) {
         host = t.getRequestHeaders().getFirst("host");
       }
 
       String path = uri.getPath();
 
       if (uri.getQuery() != null) {
         path = path + "?" + uri.getQuery();
       }
 
       String url = uri.getScheme();
 
       if (url == null)
         url = "http://";
       else {
         url = url + "://";
       }
 
       url = url + host + path;
 
       return url;
     }
   }
 }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.proxy.MineProxy
 * JD-Core Version:    0.6.2
 */
