package Controller;

import android.widget.EditText;
import android.widget.TextView;

import com.project.wirelessteam.Views.R;
import com.project.wirelessteam.Views.login;

import org.json.JSONObject;

import Model.loginModel;

public class loginController {
    private login view;
    private loginModel model;

    public loginController(login v, loginModel m){
        view = v;
        model = m;
    }

    public JSONObject sendLogin(EditText name, EditText pass){
        String Pass= pass.getText().toString();
        Pass = Pass.replaceAll("\\s","");
        String Name= name.getText().toString();
        Name = Name.replaceAll("\\s","");

        return model.sendLogin(Name,Pass);
    }


}
