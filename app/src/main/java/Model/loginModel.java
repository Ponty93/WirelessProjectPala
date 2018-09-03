package Model;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class loginModel {


    public String makeMD5(String param) {
        StringBuffer sb = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((param).getBytes());
            byte[] digest = md.digest();
            sb = new StringBuffer();
            for(byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sb.toString();
    }


    public JSONObject sendLogin(String name, String pass){
        phpConnect conn = new phpConnect("https://psionofficial.com/Wireless/login.php",-1);
        try {
            if(conn.execute("r",name,"-1", makeMD5(name+pass),"-2").get())
                return conn.getResJson();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
