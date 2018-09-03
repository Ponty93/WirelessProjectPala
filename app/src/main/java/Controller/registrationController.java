package Controller;

import android.widget.EditText;
import android.widget.TextView;

import com.project.wirelessteam.Views.registration;

import Model.registrationModel;

public class registrationController {

    private registration view;
    private registrationModel model;

    public registrationController(registration v,registrationModel m){
        view = v;
        model = m;
    }

    public boolean goToLogin(EditText Name, EditText Pass){
        String name = Name.getText().toString();
        String pass = Pass.getText().toString();
        pass = pass.replaceAll("\\s","");
        name = name.replaceAll("\\s","");

        return model.goToLogin(name,pass);
    }

    public boolean checkInput(EditText Name, EditText Pass){
        String name = Name.getText().toString();
        String pass = Pass.getText().toString();
        return model.checkInput(name,pass);
    }
}
