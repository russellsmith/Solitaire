/*
 * \brief Implementation of Solitare.
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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Solitaire extends JPanel implements MouseListener
{
    // Define lists holding the cards of the game.
    protected LinkedList<Card> _stockDown,
            _stockUp,
            _tableau1,
            _tableau2,
            _tableau3,
            _tableau4,
            _tableau5,
            _tableau6,
            _tableau7,
            _foundation1,
            _foundation2,
            _foundation3,
            _foundation4,
            _selectionSource;
    protected LinkedList<LinkedList<Card>> _tableaus,
            _foundations;
    protected java.util.List<Card> _curSelection;
    protected HashMap<String, BufferedImage> _images;
    protected int _cardWidth = 75,
            _cardHeight = 108,
            _cardWidthOffset = 20,
            _cardHeightOffset = 30,
            _stockYDist = 30,
            _windowHeight = 700,
            _windowWidth = 700,
            _selectionIndex = -1;

    // Define bounding rectangles for each stack
    // Tableau's get taller stacks due to the ability to vertically stack cards
    protected Rectangle _stockDownPos = new Rectangle(10, 10, _cardWidth, _cardHeight),
            _stockUpPos = new Rectangle(10 + _cardWidth + _cardWidthOffset, 10, _cardWidth, _cardHeight),
            _tab1Pos = new Rectangle(10, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab2Pos = new Rectangle(10 + _cardWidthOffset + _cardWidth, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab3Pos = new Rectangle(10 + _cardWidthOffset * 2 + _cardWidth * 2, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab4Pos = new Rectangle(10 + _cardWidthOffset * 3 + _cardWidth * 3, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab5Pos = new Rectangle(10 + _cardWidthOffset * 4 + _cardWidth * 4, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab6Pos = new Rectangle(10 + _cardWidthOffset * 5 + _cardWidth * 5, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _tab7Pos = new Rectangle(10 + _cardWidthOffset * 6 + _cardWidth * 6, 10 + _stockYDist + _cardHeight, _cardWidth, _cardHeight + _cardHeightOffset * 19),
            _found1Pos = new Rectangle(10 + _cardWidthOffset * 3 + _cardWidth * 3, 10, _cardWidth, _cardHeight),
            _found2Pos = new Rectangle(10 + _cardWidthOffset * 4 + _cardWidth * 4, 10, _cardWidth, _cardHeight),
            _found3Pos = new Rectangle(10 + _cardWidthOffset * 5 + _cardWidth * 5, 10, _cardWidth, _cardHeight),
            _found4Pos = new Rectangle(10 + _cardWidthOffset * 6 + _cardWidth * 6, 10, _cardWidth, _cardHeight);

    public Solitaire()
    { 
        InitializeGraphics();
        InitializeGame();
        //InitializeWindow();
    }

    /*
     * \brief Handles initialization of graphics.
     */
    private final void InitializeGraphics()
    {
        _images = new HashMap<String, BufferedImage>();
        try
        {
        _images.put("back.png", ImageIO.read(new File("images/back.png")));
        _images.put("clubs-2.png", ImageIO.read(new File("images/clubs-2.png")));
        _images.put("clubs-3.png", ImageIO.read(new File("images/clubs-3.png")));
        _images.put("clubs-4.png", ImageIO.read(new File("images/clubs-4.png")));
        _images.put("clubs-5.png", ImageIO.read(new File("images/clubs-5.png")));
        _images.put("clubs-6.png", ImageIO.read(new File("images/clubs-6.png")));
        _images.put("clubs-7.png", ImageIO.read(new File("images/clubs-7.png")));
        _images.put("clubs-8.png", ImageIO.read(new File("images/clubs-8.png")));
        _images.put("clubs-9.png", ImageIO.read(new File("images/clubs-9.png")));
        _images.put("clubs-10.png", ImageIO.read(new File("images/clubs-10.png")));
        _images.put("clubs-j.png", ImageIO.read(new File("images/clubs-j.png")));
        _images.put("clubs-q.png", ImageIO.read(new File("images/clubs-q.png")));
        _images.put("clubs-k.png", ImageIO.read(new File("images/clubs-k.png")));
        _images.put("clubs-a.png", ImageIO.read(new File("images/clubs-a.png")));

        _images.put("diamonds-2.png", ImageIO.read(new File("images/diamonds-2.png")));
        _images.put("diamonds-3.png", ImageIO.read(new File("images/diamonds-3.png")));
        _images.put("diamonds-4.png", ImageIO.read(new File("images/diamonds-4.png")));
        _images.put("diamonds-5.png", ImageIO.read(new File("images/diamonds-5.png")));
        _images.put("diamonds-6.png", ImageIO.read(new File("images/diamonds-6.png")));
        _images.put("diamonds-7.png", ImageIO.read(new File("images/diamonds-7.png")));
        _images.put("diamonds-8.png", ImageIO.read(new File("images/diamonds-8.png")));
        _images.put("diamonds-9.png", ImageIO.read(new File("images/diamonds-9.png")));
        _images.put("diamonds-10.png", ImageIO.read(new File("images/diamonds-10.png")));
        _images.put("diamonds-j.png", ImageIO.read(new File("images/diamonds-j.png")));
        _images.put("diamonds-q.png", ImageIO.read(new File("images/diamonds-q.png")));
        _images.put("diamonds-k.png", ImageIO.read(new File("images/diamonds-k.png")));
        _images.put("diamonds-a.png", ImageIO.read(new File("images/diamonds-a.png")));

        _images.put("hearts-2.png", ImageIO.read(new File("images/hearts-2.png")));
        _images.put("hearts-3.png", ImageIO.read(new File("images/hearts-3.png")));
        _images.put("hearts-4.png", ImageIO.read(new File("images/hearts-4.png")));
        _images.put("hearts-5.png", ImageIO.read(new File("images/hearts-5.png")));
        _images.put("hearts-6.png", ImageIO.read(new File("images/hearts-6.png")));
        _images.put("hearts-7.png", ImageIO.read(new File("images/hearts-7.png")));
        _images.put("hearts-8.png", ImageIO.read(new File("images/hearts-8.png")));
        _images.put("hearts-9.png", ImageIO.read(new File("images/hearts-9.png")));
        _images.put("hearts-10.png", ImageIO.read(new File("images/hearts-10.png")));
        _images.put("hearts-j.png", ImageIO.read(new File("images/hearts-j.png")));
        _images.put("hearts-q.png", ImageIO.read(new File("images/hearts-q.png")));
        _images.put("hearts-k.png", ImageIO.read(new File("images/hearts-k.png")));
        _images.put("hearts-a.png", ImageIO.read(new File("images/hearts-a.png")));

        _images.put("spades-2.png", ImageIO.read(new File("images/spades-2.png")));
        _images.put("spades-3.png", ImageIO.read(new File("images/spades-3.png")));
        _images.put("spades-4.png", ImageIO.read(new File("images/spades-4.png")));
        _images.put("spades-5.png", ImageIO.read(new File("images/spades-5.png")));
        _images.put("spades-6.png", ImageIO.read(new File("images/spades-6.png")));
        _images.put("spades-7.png", ImageIO.read(new File("images/spades-7.png")));
        _images.put("spades-8.png", ImageIO.read(new File("images/spades-8.png")));
        _images.put("spades-9.png", ImageIO.read(new File("images/spades-9.png")));
        _images.put("spades-10.png", ImageIO.read(new File("images/spades-10.png")));
        _images.put("spades-j.png", ImageIO.read(new File("images/spades-j.png")));
        _images.put("spades-q.png", ImageIO.read(new File("images/spades-q.png")));
        _images.put("spades-k.png", ImageIO.read(new File("images/spades-k.png")));
        _images.put("spades-a.png", ImageIO.read(new File("images/spades-a.png")));
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /*
     * \brief Initializes the game state.
     */
    public final void InitializeGame()
    {
        // These will get set when the user makes card selections with mouse presses
        _curSelection = null;
        _selectionSource = null;

        _stockDown = new LinkedList<Card>();
        _stockUp = new LinkedList<Card>();
        _tableau1 = new LinkedList<Card>();
        _tableau2 = new LinkedList<Card>();
        _tableau3 = new LinkedList<Card>();
        _tableau4 = new LinkedList<Card>();
        _tableau5 = new LinkedList<Card>();
        _tableau6 = new LinkedList<Card>();
        _tableau7 = new LinkedList<Card>();
        _tableaus = new LinkedList<LinkedList<Card>>();
        _tableaus.add(_tableau1);
        _tableaus.add(_tableau2);
        _tableaus.add(_tableau3);
        _tableaus.add(_tableau4);
        _tableaus.add(_tableau5);
        _tableaus.add(_tableau6);
        _tableaus.add(_tableau7);

        _foundation1 = new LinkedList<Card>();
        _foundation2 = new LinkedList<Card>();
        _foundation3 = new LinkedList<Card>();
        _foundation4 = new LinkedList<Card>();
        _foundations = new LinkedList<LinkedList<Card>>();
        _foundations.add(_foundation1);
        _foundations.add(_foundation2);
        _foundations.add(_foundation3);
        _foundations.add(_foundation4);

        
        LinkedList<Card> cards = new LinkedList<Card>();
        // Initialize hearts
        for(int i = 2; i < 15; ++i)
        {
            if(i == 11)
            {
                cards.add(new Card(_images.get("hearts-j.png"), "Hearts", "Jack"));
            }
            else if(i == 12)
            {
                cards.add(new Card(_images.get("hearts-q.png"), "Hearts", "Queen"));
            }
            else if(i == 13)
            {
                cards.add(new Card(_images.get("hearts-k.png"), "Hearts", "King"));
            }
            else if(i == 14)
            {
                cards.add(new Card(_images.get("hearts-a.png"), "Hearts", "Ace"));
            }
            else
            {
                cards.add(new Card(_images.get("hearts-" + i + ".png"), "Hearts", Integer.toString(i)));
            }
        }

        // Initialize spades
        for(int i = 2; i < 15; ++i)
        {
            if(i == 11)
            {
                cards.add(new Card(_images.get("spades-j.png"), "Spades", "Jack"));
            }
            else if(i == 12)
            {
                cards.add(new Card(_images.get("spades-q.png"), "Spades", "Queen"));
            }
            else if(i == 13)
            {
                 cards.add(new Card(_images.get("spades-k.png"), "Spades", "King"));
            }
            else if(i == 14)
            {
                cards.add(new Card(_images.get("spades-a.png"), "Spades", "Ace"));
            }
            else
            {
                cards.add(new Card(_images.get("spades-" + i + ".png"), "Spades", Integer.toString(i)));
            }
        }

        // Initialize diamonds
        for(int i = 2; i < 15; ++i)
        {
            if(i == 11)
            {
                cards.add(new Card(_images.get("diamonds-j.png"), "Diamonds", "Jack"));
            }
            else if(i == 12)
            {
                cards.add(new Card(_images.get("diamonds-q.png"), "Diamonds", "Queen"));
            }
            else if(i == 13)
            {
                 cards.add(new Card(_images.get("diamonds-k.png"), "Diamonds", "King"));
            }
            else if(i == 14)
            {
                cards.add(new Card(_images.get("diamonds-a.png"), "Diamonds", "Ace"));
            }
            else
            {
                cards.add(new Card(_images.get("diamonds-" + i + ".png"), "Diamonds", Integer.toString(i)));
            }
        }

        // Initialize clubs
        for(int i = 2; i < 15; ++i)
        {
            if(i == 11)
            {
                cards.add(new Card(_images.get("clubs-j.png"), "Clubs", "Jack"));
            }
            else if(i == 12)
            {
                cards.add(new Card(_images.get("clubs-q.png"), "Clubs", "Queen"));
            }
            else if(i == 13)
            {
                 cards.add(new Card(_images.get("clubs-k.png"), "Clubs", "King"));
            }
            else if(i == 14)
            {
                 cards.add(new Card(_images.get("clubs-a.png"), "Clubs", "Ace"));
            }
            else
            {
                cards.add(new Card(_images.get("clubs-" + i + ".png"), "Clubs", Integer.toString(i)));
            }
        }

        // Shuffle cards
        Collections.shuffle(cards);

        ListIterator lIt = cards.listIterator();
        Card c;
        // Place cards in tableau's then in stock
        // Tableau 1 gets one card face up
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau1.add(c);

        // Tableau 2 gets one card down, one up
        _tableau2.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau2.add(c);

        // Tableau 3 gets two cards down, one up
        _tableau3.add((Card)lIt.next());
        _tableau3.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau3.add(c);

        // Tableau 4 gets three cards down, one up
        _tableau4.add((Card)lIt.next());
        _tableau4.add((Card)lIt.next());
        _tableau4.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau4.add(c);

        // Tableau 5 gets four cards down, one up
        _tableau5.add((Card)lIt.next());
        _tableau5.add((Card)lIt.next());
        _tableau5.add((Card)lIt.next());
        _tableau5.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau5.add(c);

        // Tableau 6 gets five cards down, one up
        _tableau6.add((Card)lIt.next());
        _tableau6.add((Card)lIt.next());
        _tableau6.add((Card)lIt.next());
        _tableau6.add((Card)lIt.next());
        _tableau6.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau6.add(c);

        // Tableau 7 gets six cards down, one up
        _tableau7.add((Card)lIt.next());
        _tableau7.add((Card)lIt.next());
        _tableau7.add((Card)lIt.next());
        _tableau7.add((Card)lIt.next());
        _tableau7.add((Card)lIt.next());
        _tableau7.add((Card)lIt.next());
        c = (Card)lIt.next();
        c.SetFaceUp(true);
        _tableau7.add(c);

        // Remaining cards go onto the stock
        while(lIt.hasNext())
        {
            _stockDown.add((Card)lIt.next());
        }

        // Last card put on the stock gets put on the stock up stack
        c = _stockDown.removeLast();
        c.SetFaceUp(true);
        _stockUp.add(c);
    }

    /*
     * \brief Initializes the Window.
     */
    /*public final JFrame InitializeWindow()
    {
        JFrame frame = new JFrame("Solitaire");
        frame.setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.darkGray);
        frame.setSize(_windowWidth, _windowHeight);
        frame.setContentPane(this);
        frame.setVisible(true);
        this.addMouseListener(this);

        return frame;
    }*/

    /*
     * \brief Responsible for drawing the state of the game on screen.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        Card c;
        ListIterator lIt;
        int offset;

        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        g2d.drawRoundRect(_stockDownPos.x - 2, _stockDownPos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_stockDown.isEmpty())
        {
            // Draw one face down card image to represent stock stack
            g2d.drawImage(_images.get("back.png"), _stockDownPos.x, _stockDownPos.y, this);
        }

        g2d.drawRoundRect(_stockUpPos.x - 2, _stockUpPos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_stockUp.isEmpty())
        {
            // Draw most recently picked card from the stock
            c = _stockUp.peekLast();
            g2d.drawImage(c.GetImage(), _stockUpPos.x, _stockUpPos.y, this);
        }


        if(!_tableau1.isEmpty())
        {
            offset = 0;
            lIt = _tableau1.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab1Pos.x, _tab1Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab1Pos.x, _tab1Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau2.isEmpty())
        {
            offset = 0;
            lIt = _tableau2.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab2Pos.x, _tab2Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab2Pos.x, _tab2Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau3.isEmpty())
        {
            offset = 0;
            lIt = _tableau3.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab3Pos.x, _tab3Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab3Pos.x, _tab3Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau4.isEmpty())
        {
            offset = 0;
            lIt = _tableau4.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab4Pos.x, _tab4Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab4Pos.x, _tab4Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau5.isEmpty())
        {
            offset = 0;
            lIt = _tableau5.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab5Pos.x, _tab5Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab5Pos.x, _tab5Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau6.isEmpty())
        {
            offset = 0;
            lIt = _tableau6.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab6Pos.x, _tab6Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab6Pos.x, _tab6Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        if(!_tableau7.isEmpty())
        {
            offset = 0;
            lIt = _tableau7.listIterator();
            while(lIt.hasNext())
            {
                c = (Card)lIt.next();
                if(c.GetFaceUp())
                {
                    g2d.drawImage(c.GetImage(), _tab7Pos.x, _tab7Pos.y + offset * _cardHeightOffset, this);
                }
                else
                {
                    g2d.drawImage(_images.get("back.png"), _tab7Pos.x, _tab7Pos.y + offset * _cardHeightOffset, this);
                }
                ++offset;
            }
        }

        g2d.drawRoundRect(_found1Pos.x - 2, _found1Pos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_foundation1.isEmpty())
        {
            c = _foundation1.peekLast();
            g2d.drawImage(c.GetImage(), _found1Pos.x, _found1Pos.y, this);
        }

        g2d.drawRoundRect(_found2Pos.x - 2, _found2Pos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_foundation2.isEmpty())
        {
            c = _foundation2.peekLast();
            g2d.drawImage(c.GetImage(), _found2Pos.x, _found2Pos.y, this);
        }

        g2d.drawRoundRect(_found3Pos.x - 2, _found3Pos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_foundation3.isEmpty())
        {
            c = _foundation3.peekLast();
            g2d.drawImage(c.GetImage(), _found3Pos.x, _found3Pos.y, this);
        }

        g2d.drawRoundRect(_found4Pos.x - 2, _found4Pos.y - 2, _cardWidth + 4, _cardHeight + 4, 5, 5);
        if(!_foundation4.isEmpty())
        {
            c = _foundation4.peekLast();
            g2d.drawImage(c.GetImage(), _found4Pos.x, _found4Pos.y, this);
        }
           
        // Highlight current selection
        g2d.setColor(Color.WHITE);
        if(_curSelection != null && _selectionSource != null && _selectionIndex != -1)
        {
            Rectangle r = (Rectangle)GetBoundingRect(_selectionSource).clone();
            if(r != null)
            {
                if(_selectionSource != _stockUp)
                {
                    r.y += _selectionIndex * _cardHeightOffset;
                    r.height = _cardHeight + (_curSelection.size() - 1) * _cardHeightOffset;
                }
                g2d.drawRoundRect(r.x - 2, r.y - 2, _cardWidth + 2, r.height + 2, 5, 5);
            }
        }

    }

    /*
     * \brief Event handler for mouse presses.
     */
    public void mousePressed(MouseEvent e)
    {
        Point clickPoint = e.getPoint();

        //if(_curSelection == null && _selectionSource == null && _selectionIndex == -1)
        {
            // Find the stack that was pressed, if any
            // First check stock stacks
            if(_stockDownPos.contains(clickPoint))
            {
                StockDownClick();
            }
            else if(_stockUpPos.contains(clickPoint))
            {
                StockUpClick();
            }
            // Now check foundation stacks
            else if(_found1Pos.contains(clickPoint))
            {
                FoundationClick(_foundation1);
            }else if(_found2Pos.contains(clickPoint))
            {
                FoundationClick(_foundation2);
            }else if(_found3Pos.contains(clickPoint))
            {
                FoundationClick(_foundation3);
            }else if(_found4Pos.contains(clickPoint))
            {
                FoundationClick(_foundation4);
            }
            // Now check tableau stacks
            else if(_tab1Pos.contains(clickPoint))
            {
                TableauClick(_tableau1, clickPoint, _tab1Pos);
            }else if(_tab2Pos.contains(clickPoint))
            {
                TableauClick(_tableau2, clickPoint, _tab2Pos);
            }else if(_tab3Pos.contains(clickPoint))
            {
                TableauClick(_tableau3, clickPoint, _tab3Pos);
            }else if(_tab4Pos.contains(clickPoint))
            {
                TableauClick(_tableau4, clickPoint, _tab4Pos);
            }else if(_tab5Pos.contains(clickPoint))
            {
                TableauClick(_tableau5, clickPoint, _tab5Pos);
            }else if(_tab6Pos.contains(clickPoint))
            {
                TableauClick(_tableau6, clickPoint, _tab6Pos);
            }else if(_tab7Pos.contains(clickPoint))
            {
                TableauClick(_tableau7, clickPoint, _tab7Pos);
            }
            else
                return;
        }

    }

    /*
     * \brief Handles the logic behind clicking on a foundation stack.
     */
    public void FoundationClick(LinkedList<Card> found)
    {
        if(_curSelection != null && _selectionSource != null && _selectionIndex != -1)
        {
            if(_curSelection.size() != 1)
            {
                // Trying to move too many cards onto the foundation at once
                ResetSelection();
            }
            Card last;
            if(found.size() > 0)
                last = found.peekLast();
            else
                last = null;
            if(ValidFoundationMove(last, _curSelection.get(0)))
            {
                found.addAll(_curSelection);
                _curSelection.clear();
                _curSelection = null;

                if(_selectionSource.size() > 0)
                    _selectionSource.peekLast().SetFaceUp(true);

                _selectionSource = null;
                _selectionIndex = -1;
            }
            else
                ResetSelection();
        }
        else
        {
            if(found.size() > 0)
            {
                _selectionSource = found;
                _selectionIndex = found.size() - 1;
                _curSelection = found.subList(_selectionIndex, _selectionIndex + 1);
            }
        }
        repaint();
        if(CheckVictory())
            JOptionPane.showMessageDialog(this, "Victory!");
    }

    /*
     * \brief Handles the logic behind clicking on a tableau stack.
     */
    public void TableauClick(LinkedList<Card> tab, Point clickPoint, Rectangle tabBounds)
    {
        int index = 0;
        ListIterator lIt;
        Card c;
        Rectangle cardBounds = new Rectangle(tabBounds.x, tabBounds.y, _cardWidth, _cardHeight);
        if(_curSelection == null && _selectionSource == null && _selectionIndex == -1)
        {
            if(tab.size() > 0)
            {
                if(tab.size() == 1)
                {
                    if(cardBounds.contains(clickPoint))
                    {
                        _curSelection = tab.subList(0, 1);
                        _selectionIndex = 0;
                        _selectionSource = tab;
                    }
                }
                else
                {
                    lIt = tab.listIterator();
                    while(lIt.hasNext())
                    {
                        c = (Card)lIt.next();
                        if(c.GetFaceUp() && cardBounds.contains(clickPoint))
                        {
                            _selectionIndex = index;
                            _curSelection = tab.subList(index, tab.size());
                            _selectionSource = tab;
                        }
                        cardBounds.y += _cardHeightOffset;
                        ++index;
                    }
                }
            }
        }
        else
        {
            // Trying to move one stack of cards onto another
            // Make sure the move is valid
            if(_selectionSource != tab)
            {
                // Colors are alternating
                Card lastCard;
                if(tab.size() == 0)
                    lastCard = null;
                else
                    lastCard = tab.getLast();
                if(ValidTableauMove(lastCard, _curSelection.get(0)))
                {
                    tab.addAll(_curSelection);
                    _curSelection.clear();
                    

                    if(_selectionSource.size() > 0)
                    {
                        _selectionSource.peekLast().SetFaceUp(true);
                    }
                    ResetSelection();
                }
                else
                {
                    // Invalid move
                    ResetSelection();
                }
            }
            else
            {
                // Invalid move
                ResetSelection();
            }
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    /*
     * \brief Handles the logic for clicking on the downward facing stock.
     */
    public void StockDownClick()
    {
        // Reset selection markers
        ResetSelection();
        Card c;
        if(!_stockDown.isEmpty())
        {
            // Place top of Stock Down list on to Stock Up list
            c = _stockDown.removeFirst();
            c.SetFaceUp(true);
            _stockUp.add(c);
            repaint();
        }else
        {
            // Stock down is empty : if stock up is not empty then copy elements of stock up to stock down
            // and set all cards to face down
            if(!_stockUp.isEmpty())
            {
                _stockDown = (LinkedList<Card>)_stockUp.clone();
                ListIterator lIt = _stockDown.listIterator();
                while(lIt.hasNext())
                {
                    ((Card)lIt.next()).SetFaceUp(false);
                }
                _stockUp.clear();
                repaint();
            }
        }
    }

    /*
     * \brief Checks to see if it is valid to move a card onto a foundation
     *        by checking the card currently on the top of the foundation (lower)
     *        against the value of the card being added (upper).
     */
    public boolean ValidFoundationMove(Card lower, Card upper)
    {
        // Moving an ace onto empty spot
        if(lower == null && upper.GetCard().equalsIgnoreCase("Ace"))
            return true;
        else if(lower == null)
            return false;
        
        // Check for same suit
        if(!lower.GetSuit().equalsIgnoreCase(upper.GetSuit()))
            return false;
        
        // CHeck for ascending order
        if(lower.GetCard().equalsIgnoreCase("Ace"))
            return upper.GetCard().equalsIgnoreCase("2");
        else if(lower.GetCard().equalsIgnoreCase("2"))
            return upper.GetCard().equalsIgnoreCase("3");
        else if(lower.GetCard().equalsIgnoreCase("3"))
            return upper.GetCard().equalsIgnoreCase("4");
        else if(lower.GetCard().equalsIgnoreCase("4"))
            return upper.GetCard().equalsIgnoreCase("5");
        else if(lower.GetCard().equalsIgnoreCase("5"))
            return upper.GetCard().equalsIgnoreCase("6");
        else if(lower.GetCard().equalsIgnoreCase("6"))
            return upper.GetCard().equalsIgnoreCase("7");
        else if(lower.GetCard().equalsIgnoreCase("7"))
            return upper.GetCard().equalsIgnoreCase("8");
        else if(lower.GetCard().equalsIgnoreCase("8"))
            return upper.GetCard().equalsIgnoreCase("9");
        else if(lower.GetCard().equalsIgnoreCase("9"))
            return upper.GetCard().equalsIgnoreCase("10");
        else if(lower.GetCard().equalsIgnoreCase("10"))
            return upper.GetCard().equalsIgnoreCase("Jack");
        else if(lower.GetCard().equalsIgnoreCase("Jack"))
            return upper.GetCard().equalsIgnoreCase("Queen");
        else if(lower.GetCard().equalsIgnoreCase("Queen"))
            return upper.GetCard().equalsIgnoreCase("King");
        else
            return false;
        
    }


    /*
     * \brief Checks to see if it is valid to move a stack of cards onto a tableau stack by
     *        checking the card currently on top of the tableau (lower) against the card
     *        at the bottom of the stack being added (upper).
     */
    public boolean ValidTableauMove(Card lower, Card upper)
    {
        // Moving a King onto empty spot
        if(lower == null && upper.GetCard().equalsIgnoreCase("King"))
            return true;
        else if(lower == null)
            return false;

        // Check for moving same color on top of each other
        if(lower.GetColor().equalsIgnoreCase(upper.GetColor()))
            return false;

        // Check for descending order
        if(lower.GetCard().equalsIgnoreCase("King"))
            return upper.GetCard().equalsIgnoreCase("Queen");
        else if(lower.GetCard().equalsIgnoreCase("Queen"))
            return upper.GetCard().equalsIgnoreCase("Jack");
        else if(lower.GetCard().equalsIgnoreCase("Jack"))
            return upper.GetCard().equalsIgnoreCase("10");
        else if(lower.GetCard().equalsIgnoreCase("10"))
            return upper.GetCard().equalsIgnoreCase("9");
        else if(lower.GetCard().equalsIgnoreCase("9"))
            return upper.GetCard().equalsIgnoreCase("8");
        else if(lower.GetCard().equalsIgnoreCase("8"))
            return upper.GetCard().equalsIgnoreCase("7");
        else if(lower.GetCard().equalsIgnoreCase("7"))
            return upper.GetCard().equalsIgnoreCase("6");
        else if(lower.GetCard().equalsIgnoreCase("6"))
            return upper.GetCard().equalsIgnoreCase("5");
        else if(lower.GetCard().equalsIgnoreCase("5"))
            return upper.GetCard().equalsIgnoreCase("4");
        else if(lower.GetCard().equalsIgnoreCase("4"))
            return upper.GetCard().equalsIgnoreCase("3");
        else if(lower.GetCard().equalsIgnoreCase("3"))
            return upper.GetCard().equalsIgnoreCase("2");
        else if(lower.GetCard().equalsIgnoreCase("2"))
            return upper.GetCard().equalsIgnoreCase("Ace");
        else if(lower.GetCard().equalsIgnoreCase("Ace"))
            return false;
        else
            return false;

    }

    /*
     * \brief Handles the logic for clicking on the upward facing stock of cards.
     */
    public void StockUpClick()
    {
        if(!_stockUp.isEmpty())
        {
            _selectionIndex = _stockUp.size() - 1;
            _curSelection = _stockUp.subList(_selectionIndex, _selectionIndex + 1);
            _selectionSource = _stockUp;
            repaint();
        }
    }

    /*
     * \brief Returns the bounding rectangle for the specified stack list.
     */
    public Rectangle GetBoundingRect(LinkedList<Card> lst)
    {
        if(lst == _foundation1)
            return _found1Pos;
        else if(lst == _foundation2)
            return _found2Pos;
        else if(lst == _foundation3)
            return _found3Pos;
        else if(lst == _foundation4)
            return _found4Pos;
        else if(lst == _tableau1)
            return _tab1Pos;
        else if(lst == _tableau2)
            return _tab2Pos;
        else if(lst == _tableau3)
            return _tab3Pos;
        else if(lst == _tableau4)
            return _tab4Pos;
        else if(lst == _tableau5)
            return _tab5Pos;
        else if(lst == _tableau6)
            return _tab6Pos;
        else if(lst == _tableau7)
            return _tab7Pos;
        else if(lst == _foundation1)
            return _found1Pos;
        else if(lst == _foundation2)
            return _found2Pos;
        else if(lst == _foundation3)
            return _found3Pos;
        else if(lst == _foundation4)
            return _found4Pos;
        else if(lst == _stockUp)
            return _stockUpPos;
        else if(lst == _stockDown)
            return _stockDownPos;
        else
            return null;

    }

    /*
     * \brief Resets the current user selection.
     */
    public void ResetSelection()
    {
        _curSelection = null;
        _selectionSource = null;
        _selectionIndex = -1;
    }

    /*
     * \brief Checks the victory condition of the game.
     */
    public boolean CheckVictory()
    {
        if(_foundation1.size() == 13 && _foundation2.size() == 13 && _foundation3.size() == 13 && _foundation4.size() == 13)
            return true;
        else return false;
    }
}
