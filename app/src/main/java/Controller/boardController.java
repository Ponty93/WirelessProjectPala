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

    public boolean defeat() {
        return model.defeat();
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
        model.getDice().setDiceResToNull();
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

    public boolean endTurn(){
        return model.endTurn();
    }

    public void roll(){
        model.getDice().roll();
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
        return model.getDice().getDiceRes(index);
    }

    public void setDiceResToNullInPos(int index){
        model.getDice().setDiceResToNullInPos(index);
    }

    public void setCounter(int val){
        model.setCounter(val);
    }

    public void eatPawn(int id){
        model.eatPawn(id);
    }

    public boolean canEat(int id1,int id2,int posToStart,int posToArrive){
        return model.canEat(id1,id2,posToStart,posToArrive);
    }

    public int howManyPawns(int pos, int playerId){
        return model.howManyPawns(pos,playerId);
    }

    public boolean checkIfPawnWin(int val){
        return model.checkIfPawnWin(val);
    }

    public boolean doubleDiceRes(){
        return model.getDice().getDouble();
    }

    public void setDoubleDown(){
        model.getDice().setDoubleRoll(false);
    }

}

