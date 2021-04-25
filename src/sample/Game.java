package sample;


import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    //Constants
    final double CARD_WIDTH = 78, CARD_HEIGHT = 107,
            ROUNDING_FACTOR = 10, PADDING = 25;
    private GraphicsContext gc;

    public int score;

    BoundingBox mainMenuBound = new BoundingBox(8,4,182,47);
    BoundingBox restartBound = new BoundingBox(923,11,125,42);


    Random random = new Random();

    //store all card's texture
    private final Map<String, Image> imageCache = new HashMap<>();

    final Stack<Card> deck = new Stack<>();
    BoundingBox deckBound = new BoundingBox(1055 - PADDING - CARD_WIDTH, 800 -PADDING - CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);

    private Stack<Card> selected = new Stack<>();

    List<Stack<Card>> board = new ArrayList<>();
    List<List<BoundingBox>> boardBound = new ArrayList<>();

    List<Stack<Card>> foundation = new ArrayList<>();
    List<BoundingBox> foundationBound = new ArrayList<>();

    private String winText = "";
    private String alertText = "";

    Game() {
        gc = null;
    }

    Game(GraphicsContext gc) {
        this.gc = gc;
        score = 500;
        initFoundation();
        loadImages();
        fillDeck();
        shuffle();
        layBoard();
        generateCardBound();
        revealCards();
        drawGame();
    }

    private void initFoundation()
    {
        foundation.clear();
        for(int i =0 ;i<8;i++)
        {
            foundation.add(new Stack());
            foundationBound.add(new BoundingBox(PADDING,800 - PADDING - CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT));
        }
    }

    /**
     * Load all the textures needed for cards
     */
    void loadImages()
    {
        for(CardValue value : CardValue.values())
        {
            String name = value.toString().toLowerCase() + "of" + "spades";
            imageCache.put(name,new Image("/res/Cards/" + name +".png"));
        }
        //black
//        imageCache.put("cardback",new Image("/res/Cards/cardback2.jpg"));

        //blue
        imageCache.put("cardback",new Image("/res/Cards/cardback3.png"));

    }

    /**
     * Fill 52 cards to the deck
     */
    private void fillDeck() {
        for(CardValue value : CardValue.values())
        {
            for(int i =0 ; i<8;i++)
            {
                deck.push(new Card(value));
            }
        }
    }

    /**
     * Randomly shuffle the card decks by using Fisher Yates shuffle
     */
    private void shuffle()
    {
        for (int i = 0; i < 104; i++) {
            swapCard(random.nextInt(104- i), 103 - i);
        }
    }

    /**
     * swap card i1 with i2
     */
    private void swapCard(int i1, int i2) {
        Card temp = deck.get(i1);
        deck.set(i1, deck.get(i2));
        deck.set(i2, temp);
    }

    private void layBoard()
    {
        board.clear();
        int n = 0;
        for(int i =0 ;i<10;i++)
        {
            Stack<Card> stack = new Stack<>();
            if(n<4) {
                for (int j = 0; j < 6; j++) {
                    stack.push(deck.pop());
                }
            }else
            {
                for (int j = 0; j < 5; j++) {
                    stack.push(deck.pop());
                }
            }
            n++;
            board.add(stack);
        }
    }

    /**
     * draw game
     */
    void drawGame()
    {
        gc.clearRect(0,0,1055,800);
        double x;
        double y;

        //draw the board
        for(int i = 0;i<board.size();i++)
        {
            x = PADDING + (CARD_WIDTH + PADDING) * i;
            Stack<Card> stack = board.get(i);
            if(stack.isEmpty())
            {
                drawEmpty(x,PADDING*2+15);
            }else
            {
                for(int j=0;j<stack.size();j++)
                {
                    y = PADDING * (2 + j)+15;
                    Card card = stack.get(j);
                    if(card.isRevealed())
                    {
                        drawCard(card,x,y);
                    }else
                    {
                        drawCardBack(x,y);
                    }
                }
            }
        }

        //draw foundation
        for(int i=0;i<foundation.size();i++)
        {
            if(!foundation.get(i).isEmpty())
            {
                drawCard(new Card(CardValue.KING), (PADDING) + (PADDING*i),800 - PADDING - CARD_HEIGHT);
            }
        }
        drawHand();

        Rectangle rec = new Rectangle(0, 0, 100, 100);

        //draw text
        drawText(alertText,PADDING,800-PADDING,30,Color.RED,TextAlignment.LEFT);

        drawText(winText,527.5,400,70,Color.WHITE,TextAlignment.CENTER);

        drawText(Integer.toString(score),527.5,42,30,Color.WHITE,TextAlignment.LEFT);
    }

    /**
     * Draw the specified text to the game canvas at the specified coordinates with additional attributes.
     * @param text the text to draw
     * @param x the x coordinate to draw at
     * @param y the y coordinate to draw at
     * @param paint the Paint to draw the text with
     * @param textAlignment the TextAlignment to draw with
     */
    private void drawText(String text, double x, double y, double size, Paint paint, TextAlignment textAlignment) {
        gc.setFont(Font.font("Brush Script MT", FontWeight.BOLD,size));
        gc.setFill(paint);
        gc.setTextAlign(textAlignment);
        gc.fillText(text, x, y);
    }

    /**
     * draw empty box
     * @param x the x coordinate to draw at
     * @param y the y coordinate to draw at
     */
    void drawEmpty(double x,double y)
    {
        //set the stroke color
        gc.setStroke(Color.WHITE);
        //set stroke width
        gc.setLineWidth(1);
        //draw empty box at coordinate x,y
        gc.strokeRoundRect(x,y,CARD_WIDTH, CARD_HEIGHT,ROUNDING_FACTOR,ROUNDING_FACTOR);
    }

    /**
     * reveal the top card
     */
    void revealCards(){
        for (Stack<Card> stack : board) {
            if (!stack.isEmpty()) {
                Card card = stack.peek();
                if (!card.isRevealed()) {
                    card.reveal();
                }
            }
        }
    }

    /**
     * Draw the specified Card to the game canvas
     * @param card the card to drawn
     * @param x the x coordinate to draw at
     * @param y the y coordinate to draw at
     */
    void drawCard(Card card,double x,double y)
    {
        gc.drawImage(imageCache.get(card.getName()),x,y,CARD_WIDTH, CARD_HEIGHT);
        if(card.isSelected())
        {
            //gold stroke
//            gc.setStroke(Color.rgb(	229, 173, 77));
            gc.setStroke(Color.LIGHTBLUE);
            gc.setLineWidth(3);
            gc.strokeRoundRect(x,y,CARD_WIDTH, CARD_HEIGHT, ROUNDING_FACTOR, ROUNDING_FACTOR);
        }
    }

    /**
     * draw card back to the fucking screen
     * @param x x coordinate to draw at
     * @param y y coordinate to draw at
     */
    void drawCardBack(double x, double y)
    {
            gc.drawImage(imageCache.get("cardback"), x, y, CARD_WIDTH, CARD_HEIGHT);
    }

    /**
     * draw bottom left corner deck i don't know what it's called
     */
    void drawHand()
    {

        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineWidth(3);
        gc.strokeRoundRect(1055-PADDING-CARD_WIDTH,800-PADDING-CARD_HEIGHT,CARD_WIDTH, CARD_HEIGHT, ROUNDING_FACTOR, ROUNDING_FACTOR);
        if(deck.size()>=10)
        {
            drawCardBack(1055-PADDING-CARD_WIDTH,800-PADDING-CARD_HEIGHT);
        }else
        {
            drawEmpty(1055-PADDING-CARD_WIDTH,800-PADDING-CARD_HEIGHT);
        }

        gc.setFont(Font.font("Brush Script MT", FontWeight.BOLD,40));
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(10);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(Integer.toString((int)deck.size()/10),1045-PADDING-CARD_WIDTH/2,PADDING-CARD_HEIGHT);
    }

    /**
     * generate bound for all of the card
     */
    void generateCardBound()
    {
        boardBound.clear();
        for(int i=0;i<board.size();i++)
        {
            boardBound.add(new ArrayList<>());
            Stack<Card> c = board.get(i);
            boardBound.get(i).add(new BoundingBox(PADDING + (CARD_WIDTH + PADDING) * i,PADDING * (2)+15,CARD_WIDTH,CARD_HEIGHT));
            for(int j = 1;j<c.size();j++)
            {
                boardBound.get(i).add(new BoundingBox(PADDING + (CARD_WIDTH + PADDING) * i,PADDING * (2 + j)+15,CARD_WIDTH,CARD_HEIGHT));
            }
        }

    }

    int handleMouseClicked(MouseEvent ME)
    {

        double x= ME.getX();
        double y= ME.getY();

        //Reset teh alert texture
        alertText = null;

        if(mainMenuBound.contains(x,y))
        {
            ME.consume();
            return 1;
        }

        if(restartBound.contains(x,y))
        {
            ME.consume();
            return 2;
        }

        if(deckBound.contains(x,y) && deck.size()/10 > 0)
        {
            deselect();
            addCardToBoard();
            revealCards();
            generateCardBound();
            drawGame();
            ME.consume();
        }

        boolean boardClicked = false;
        int indexX = -1, indexY = -1;
        for(int i=0;i<board.size();i++)
        {
            List<BoundingBox> boundList = boardBound.get(i);
            for(int j=0;j<boundList.size();j++)
            {
                if(boundList.get(j).contains(x,y))
                {
                    if(board.get(i).isEmpty() || board.get(i).get(j).isRevealed())
                    {
                        indexX = i;
                        indexY = j;
                        boardClicked = true;
                    }
                }
            }
            if (boardClicked)
            {
                boardClicked(indexX,indexY);
                finish(ME);
                return 0;
            }
        }
        return 0;
    }

    void addCardToBoard()
    {
        for(int i = 0;i<board.size();i++)
        {
            if(board.get(i).isEmpty()) {
                alertText = "You can not deal with a new row while any column are empty";
                return;
            }

        }
        for (int i = 0; i < board.size(); i++) {
            board.get(i).push(deck.pop());
        }

    }

    /**
     * Perform final cleanup for the handleMouseClicked() function
     * @param me the MouseEvent passed by handleMouseClicked()
     * เปิดการ์ดแล้ววาดเฟรมใหม่
     */
    private void finish(MouseEvent me) {
        revealCards();
        drawGame();
        me.consume();
    }

    /**
     * Handles the board being clicked
     * @param indexX the column on the board clicked
     * @param indexY the card in the column clicked
     */
    private void boardClicked(int indexX ,int indexY) {
        Stack<Card> stack = board.get(indexX);
        Card card = null;
        if (!stack.isEmpty()) {
            card = stack.get(indexY);
        }
        if (!selected.isEmpty()) {
            if (selected.contains(card)) {
                deselect();
            } else if (isValidBoardMove(card, selected.get(0)) && (indexY == stack.size() - 1 || indexY == 0)) {
                moveCards(stack);
                score -=1;
                generateCardBound();
                deselect();
            } else {
                alertText = "Invalid move!";
                deselect();
            }
        } else {
            deselect();
            if(isValidSelect(indexY,stack))
            {
                for(int i = indexY;i<stack.size();i++)
                {
                    select(stack.get(i));
                }
            }
        }
        addToFoundation();
        if(isGameWon())
        {
            winText = "YOU WIN!!";
        }
    }

    private boolean isValidBoardMove(Card parent, Card child)
    {
        if(parent == null)
        {
           return true;
        }
        if(parent.getValue().ordinal() != child.getValue().ordinal() + 1)
        {
            return false;
        }
        return true;
    }

    /**
     * Move the selected card to the specified Stack
     * TODO: Make more efficient than checking each Stack for the presence of the selected card
     * @param stack the Stack to move to
     */
    private void moveCards(Stack<Card> stack) {
        for (Card card : selected) {

            for (Stack<Card> boardStack : board) {
                boardStack.remove(card);
            }
            stack.push(card);
        }
//        if (isGameWon()) {
//            winText = "You win!";
//        }
    }


    /**
     * Set the selected variable to be equal to a card
     * @param card the card to select
     */
    private void select(Card card) {
        card.toggleSelected();
        selected.add(card);
    }

    /**
     * Deselect the currently selected card
     */
    private void deselect() {
        if (!selected.isEmpty()) {
            for (Card card : selected) {
                card.toggleSelected();
            }
            selected.clear();
        }
    }

    private boolean isValidSelect(int y,Stack<Card> e)
    {
        if(y == e.size()-1)
        {
            return true;
        }
        for(int i = y;i<e.size()-1;i++)
        {
            if(e.get(i).getValue().ordinal() != e.get(i+1).getValue().ordinal()+1)
            {
                return false;
            }
        }
        return true;
    }

    public boolean isGameWon()
    {
        for (Stack<Card> stack : foundation) {
            if (stack.size() != 13) {
                return false;
            }
        }
        return true;
    }

    private boolean addToFoundation()
    {
        for(int i =0;i<board.size();i++)
        {
            Stack<Card> K = new Stack();
           for(int j=0;j<board.get(i).size();j++)
           {
                   if (board.get(i).get(j).getValue().equals(CardValue.KING) && board.get(i).get(j).isRevealed()) {
                       for(int z =j ;z<board.get(i).size();z++)
                       {
                          K.add(board.get(i).get(z));
                       }

                       if(K.size()==13 && CheckKING(K))
                       {
                           moveCardToF(K);
                           score +=100;
                           generateCardBound();
                       }
                       break;
               }
           }
        }
        return true;
    }

    private boolean CheckKING(Stack<Card> b)
    {
        for(int i =0;i<b.size()-1;i++)
        {
            if(b.get(i).getValue().ordinal() != b.get(i+1).getValue().ordinal()+1)
            {
                return false;
            }
        }
        return true;
    }

    private void moveCardToF(Stack<Card> t) {
        for(Card card : t)
        {
            for(Stack card2 : board)
            {
                card2.remove(card);
            }
        }

        if (foundation.get(0).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(0).push(t.peek());
            }
        } else if (foundation.get(1).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(1).push(t.peek());
            }
        } else if (foundation.get(2).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(2).push(t.peek());
            }
        } else if (foundation.get(3).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(3).push(t.peek());
            }
        } else if (foundation.get(4).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(4).push(t.peek());
            }
        } else if (foundation.get(5).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(5).push(t.peek());
            }
        } else if (foundation.get(6).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(6).push(t.peek());
            }
        } else if (foundation.get(7).isEmpty()) {
            for (int i = 0; i < t.size(); i++) {
                foundation.get(7).push(t.peek());
            }
        }
    }
}
