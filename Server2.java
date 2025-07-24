import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
public class Server2 {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dns", "javauser", "javapass");
        Statement stm = con.createStatement();
        ResultSet rs;
        ServerSocket ss = new ServerSocket(9091);
        Socket socket;
        DataInputStream in;
        DataOutputStream out;
        String url,u,s;
         u = s = "\0";
        while(true)
        {
            socket = ss.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            url = in.readUTF();
            StringTokenizer st = new StringTokenizer(url,".");
            while(st.countTokens() > 1)
            {
                s = s + st.nextToken() + ".";
            }
            u = st.nextToken();
            s = s.substring(0,s.length() - 1).trim();
            rs = stm.executeQuery("select pno,ipad from google where name= '"+u+"';");
            if(rs.next())
            {
                out.writeUTF(rs.getString(1));
                out.writeUTF(rs.getString(2));
                out.writeUTF(s);

            }
            else
            {
                System.out.println("Not found");
                out.writeUTF("No found");
            }
        }
    }
}
