package Model;

import java.util.concurrent.ExecutionException;

import API.phpConnect;

public class registrationModel {


    public boolean goToLogin(String name,String pass){
        phpConnect conn = new phpConnect("https://www.psionofficial.com/Wireless/register.php",-1);
        try {
            return conn.execute("c","USERS",name,pass,"1").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkInput(String name,String pass){
        if(!name.equals("") && !pass.equals(""))
            return true;
        else {
            return false;
        }
    }
}
