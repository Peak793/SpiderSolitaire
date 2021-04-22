package sample;

import javafx.geometry.BoundingBox;
import javafx.stage.Stage;


public class Card {

    CardValue value;

    Boolean revealed = false;
    Boolean selected = false;
    private double width = 78,height=107;
    BoundingBox bb;
    Card()
    {

    }

    Card(CardValue value)
    {
        this.value = value;
    }

    /**
     * @return the name of card
     */
    String getName(){
        return value.toString().toLowerCase() + "of" + "spades";
    }

    /**
     *  Check if the card is revealed or not
     * @return revealed
     */
    Boolean isRevealed()
    {
        return revealed;
    }

    /**
     * reveal the card
     */
    void reveal()
    {
        revealed = true;
    }

    void setCoordinate(double x,double y){
        bb = new BoundingBox(x,y,width,height);
    }

    CardValue getValue()
    {
        return value;
    }

    // เปลี่ยนค่า ระหว่าง การ์ดถูกเลือก กับ ไม่ถูกเลือก
    void toggleSelected()
    {
        selected = !selected;
    }

    Boolean isSelected(){
        return selected;
    }


}
