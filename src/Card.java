/*
 * \brief Class used to store information about one card in a deck.
 *
 * \author Jonah Monte-Alegre
 * \author Carlyn-Ann Lee
 * \author Thomas Lowerre
 * \author Travis Powell
 * \author Russell Smith
 *
 * \date November 29, 2010
 */
package solitaire;
import java.awt.image.BufferedImage;

public class Card
{
    private BufferedImage _image;
    private String _suit, _card;
    boolean _faceUp;

    public Card(BufferedImage img, String suit, String card)
    {
        _image = img;
        _suit = suit;
        _card = card;
        _faceUp = false;
    }

    public String GetSuit()
    {
        return _suit;
    }

    public String GetCard()
    {
        return _card;
    }

    public BufferedImage GetImage()
    {
        return _image;
    }

    public String GetColor()
    {
        if(_suit.equalsIgnoreCase("hearts") || _suit.equalsIgnoreCase("diamonds"))
            return "Red";
        else
            return "Black";
    }

    public boolean GetFaceUp()
    {
        return _faceUp;
    }

    public void SetFaceUp(boolean f)
    {
        _faceUp = f;
    }


}
