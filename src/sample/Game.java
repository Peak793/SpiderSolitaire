package sample;

import com.sun.jdi.Value;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    GraphicsContext gc;

    //Constants
    final double CARD_WIDTH = 78, CARD_HEIGHT = 107,
            ROUNDING_FACTOR = 10, PADDING = 25;

    Random random = new Random();

    //store all card's texture
    private final Map<String, Image> imageCache = new HashMap<>();

    final Stack<Card> deck = new Stack<>();

    List<Stack<Card>> board = new ArrayList<>();
    List<List<BoundingBox>> boardBound = new ArrayList<>();

    Game ()
    {

    }

    Game (GraphicsContext gc)
    {
        this.gc = gc;

        loadImages();
        fillDeck();
        shuffle();
        layBoard();
        generateCardBound();
        revealCards();
        drawGame();
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
        gc.clearRect(0,0,1300,800);
        double x;
        double y;

        //draw the board
        for(int i = 0;i<10;i++)
        {
            x = PADDING + (CARD_WIDTH + PADDING) * i;
            Stack<Card> stack = board.get(i);
            if(stack.isEmpty())
            {
                drawEmpty(x,PADDING*2);
            }else
            {
                for(int j=0;j<stack.size();j++)
                {
                    y = PADDING * (2 + j);
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
        if(card.isRevealed())
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
     * generate bound for all of the card
     */
    void generateCardBound()
    {
        boardBound.clear();
        for(int i=0;i<7;i++)
        {
            boardBound.add(new ArrayList<>());
            Stack<Card> c = board.get(i);
            boardBound.get(i).add(new BoundingBox(PADDING + (CARD_WIDTH + PADDING) * i,2*PADDING+(PADDING*i),CARD_WIDTH,CARD_HEIGHT));
            for(int j = 1;j<c.size();j++)
            {
                boardBound.get(i).add(new BoundingBox(PADDING + (CARD_WIDTH + PADDING) * i,2*PADDING+(PADDING*i),CARD_WIDTH,CARD_HEIGHT));
            }
        }
    }

    public void handleMouseClicked(MouseEvent me) {

    }
}
