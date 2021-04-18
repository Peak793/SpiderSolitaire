package sample;

public class Card {
    CardValue value;

    Boolean revealed = false;
    Boolean selected = false;

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
}
