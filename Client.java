import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
public class Client {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dns", "javauser", "javapass");        
        Statement stm = con.createStatement();
        ResultSet rs;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a DNS : ");
        String url = sc.nextLine();
        String s,u,ip;
         u = ip ="\0";
        boolean status = true;
        int pno = 9090;
        while(status)
        {
            s ="\0";
            StringTokenizer st = new StringTokenizer(url,".");
            if(st.countTokens() == 1)status = false;
            while(st.countTokens() > 1)
            {
                s = s + st.nextToken() + ".";
            }
            u = st.nextToken();
            s = s.substring(0,s.length() - 1).trim();
            rs = stm.executeQuery("select pno,ipad from client where name= '"+u+"';");
            if(rs.next())
            {
                pno = Integer.parseInt(rs.getString(1));
                ip = rs.getString(2) +"."+ip;
                url = s;

            }
            else{
                Socket socket = new Socket("localhost",pno);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(url);
                pno = Integer.parseInt(in.readUTF());
                ip = in.readUTF() + "."+ip;
                url = in.readUTF();

            }
            System.out.println(ip);
        }
        System.out.println("ip is "+ip);
    }
}
