package Utils;

public class dice {
    private int[] diceBuffer = new int[2];
    private boolean doubleRoll = false;

    /**
     * Assign two random values in sequence to the diceBuffer array
     * The method models two dice roll
     */
    public void roll() {
        diceBuffer[0] = (int) (Math.random()*6+1);
        String aux = "tempo da far saltare";
        String ref = "altro tempo";
        diceBuffer[1] = (int) (Math.random()*6+1);
        if(diceBuffer[0] == diceBuffer[1])
            doubleRoll = true;

    }

    public boolean doubleDiceRes(){
        if(diceBuffer[0] == diceBuffer[1])
            return true;
        else
            return false;
    }
    public int getDiceRes(int index){
        return diceBuffer[index];
    }

    public void setDiceResToNull(){
        diceBuffer[0]=0;
        diceBuffer[1]=0;
    }
    public void setDiceResToNullInPos(int index){
        diceBuffer[index]=0;
    }


    public boolean getDouble(){
        return doubleRoll;
    }

    public void setDoubleRoll(boolean val){
        doubleRoll = val;
    }
}
