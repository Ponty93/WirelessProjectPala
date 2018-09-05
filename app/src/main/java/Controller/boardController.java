package Controller;

import com.project.wirelessteam.Views.BoardActivity;

import org.json.JSONObject;

import Actors.Player;
import Model.Board;

public class boardController {
    private Board model;
    private BoardActivity view;


    public boardController(Board m,BoardActivity v){
        model= m;
        view = v;
    }

    public boolean surrender() {
        return model.surrender();
    }

    public JSONObject updateRound(){
        return model.updateRound();
    }

    public Player getPlayer1(){
        return model.getPlayer1();
    }
    public Player getPlayer2(){
        return model.getPlayer2();
    }

    public void setDiceResToNull(){
        model.setDiceResToNull();
    }

    public void setNumberOfMove(int val){
        model.setNumberOfMove(val);
    }

    public int getRoundsCounter(){
        return model.getRoundsCounter();
    }

    public boolean playerOrder(int id){
        return model.playerOrder(id);
    }

    public void endTurn(){
        model.endTurn();
    }

    public void roll(){
        model.roll();
    }

    public boolean updateBoard(JSONObject json){
        return model.updateBoard(json);
    }

    public int getNumberOfMove(){
        return model.getNumberOfMove();
    }

    public void movePawn(int idPawn,int posNewParent){
        model.movePawn(idPawn,posNewParent);
    }

    public int getDiceRes(int index){
        return model.getDiceRes(index);
    }

    public void setDiceResToNullInPos(int index){
        model.setDiceResToNullInPos(index);
    }

    public void setCounter(int val){
        model.setCounter(val);
    }
}

