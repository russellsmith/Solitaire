/*
 * \brief Implementation of an AI controlled Solitaire player.
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class AIPlayer extends Solitaire implements ActionListener, ItemListener{
    private long _pauseTime = 750; // Pause time between AI moves in milliseconds
    private Card _stockMarker;
    private boolean _enableAI;
    private JCheckBoxMenuItem _cbMenuItem;

    public AIPlayer()
    {
        super();
        InitializeWindow();
        _stockMarker = null;
    }

    /*
     * \brief Plays an Ace or Deuce to the foundation stacks if possible as per rule:
     *        1. Always play an Ace or Deuce wherever you can immediately.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean PlayAceDeuce()
    {
        // Check Stock stack for an Ace or Deuce
        LinkedList<Card> stack;
        ListIterator lIt;
        if(_stockUp.size() > 0)
        {
            if(_stockUp.peekLast().GetCard().equalsIgnoreCase("Ace"))
            {
                // Find first empty foundation stack
                if(_foundation1.size() == 0)
                {
                    MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, _foundation1);
                    return true;
                }
                else if(_foundation2.size() == 0)
                {
                    MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, _foundation2);
                    return true;
                }
                else if(_foundation3.size() == 0)
                {
                    MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, _foundation3);
                    return true;
                }
                else if(_foundation4.size() == 0)
                {
                    MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, _foundation4);
                    return true;
                }
            }
            else if(_stockUp.peekLast().GetCard().equalsIgnoreCase("2"))
            {
                stack = GetFoundationStack(_stockUp.peekLast().GetSuit());
                if(stack != null)
                {
                    MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, stack);
                    return true;
                }
            }
        }

        // Search through each tableau stack for either an Ace or Deuce on top
        lIt = _tableaus.listIterator();
        while(lIt.hasNext())
        {
            stack = (LinkedList<Card>)lIt.next();
            if(stack.size() > 0)
            {
                if(stack.peekLast().GetCard().equalsIgnoreCase("Ace"))
                {
                    // Find first empty foundation stack
                    if(_foundation1.size() == 0)
                    {
                        MoveCards(stack.subList(stack.size() - 1, stack.size()), stack.size() - 1, stack, _foundation1);
                        return true;
                    }
                    else if(_foundation2.size() == 0)
                    {
                        MoveCards(stack.subList(stack.size() - 1, stack.size()), stack.size() - 1, stack, _foundation2);
                        return true;
                    }
                    else if(_foundation3.size() == 0)
                    {
                        MoveCards(stack.subList(stack.size() - 1, stack.size()), stack.size() - 1, stack, _foundation3);
                        return true;
                    }
                    else if(_foundation4.size() == 0)
                    {
                        MoveCards(stack.subList(stack.size() - 1, stack.size()), stack.size() - 1, stack, _foundation4);
                        return true;
                    }
                }
                else if(stack.peekLast().GetCard().equalsIgnoreCase("2"))
                {
                    LinkedList<Card> stack2 = GetFoundationStack(stack.peekLast().GetSuit());
                    if(stack2 != null)
                    {
                        MoveCards(stack.subList(stack.size() - 1, stack.size()), stack.size() - 1, stack, stack2);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(!_enableAI)
        {
            if(e.getButton() == e.BUTTON3)
                MakeMove();
            else
                super.mousePressed(e);
        }
    }

    /*
     * \brief Pops up game over dialog.
     */
    private void GameOver()
    {
        JOptionPane.showMessageDialog(this, "Unable to find additional moves.");
        _enableAI = false;
        _cbMenuItem.setState(_enableAI);
    }

    /*
     * \brief Game loop.  Continues infinitely until no more moves are found or the AI is disabled.
     */
    private void MoveLoop()
    {
        while(MakeMove() && _enableAI)
        {
            // Do nothing
        }
    }

    /*
     * \brief Allows the AI to attempt to make one move.
     */
    private boolean MakeMove()
    {
        if(PlayAceDeuce())
        {
            _stockMarker = null;
            return true;
        }
            
        if(FreeDownCard())
        {
            _stockMarker = null;
            return true;
        }
        if(SmoothStacks())
        {
            _stockMarker = null;
            return true;
        }
        if(ClearSpot())
        {
            _stockMarker = null;
            return true;
        }
        if(BuildFoundations(true))
        {
            _stockMarker = null;
            HandleWin();
            return true;
        }
        if(PlayStock())
        {
            _stockMarker = null;
            return true;
        }
        if(_stockMarker == null && !_stockUp.isEmpty())
        {
            _stockMarker = _stockUp.peekLast();
            StockDownClick();
            return true;
        }
        else if(!_stockUp.isEmpty())
        {
            if(_stockMarker != _stockUp.peekLast())
            {
                if(BuildFoundations(false))
                {
                    // Successfully moved a card to the foundation stack with Next Card Protection disabled
                    _stockMarker = null;
                }
                else
                {
                    // Failed to move a card to the foundation stack with Next Card Protection disabled
                    StockDownClick();
                }
                return true;
            }
            else
            {
                // Have made one loop through stock stack with no moves made : try to make a move to the foundation stacks with
                // Next Card Protection rule disabled
                if(!BuildFoundations(false))
                {
                    // Failed, no more moves available
                    GameOver();
                    return false;
                }
                else
                {
                    _stockMarker = null;
                    HandleWin();
                    return true;
                }
                    
            }
        }
        else
        {
            StockDownClick();
            Pause(_pauseTime);
            if(_stockUp.isEmpty())
            {
                // Stock stack is empty and there are no more cards available : try to make a move to the foundation stack with
                // Next Card Protection rule disabled
                if(!BuildFoundations(false))
                {
                    GameOver();
                    return false;
                }
                else
                {
                    HandleWin();
                    return true;
                }
            }

        }

        return true;

    }

    /*
     * \brief Attempts to free a down card if possible as per rules:
     *        2. Always make the play or transfer that frees (or allows a play that frees) a downcard, regardless of any other considerations.
     *        3. When faced with a choice, always make the play or transfer that frees (or allows a play that frees) the downcard from the biggest pile of downcards.
     *
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean FreeDownCard()
    {
        LinkedList<LinkedList<Card>> list = GetDownCardList();
        LinkedList<Card> destination, source;
        ListIterator lIt = list.listIterator();
        Card highestFaceUp;
        
        while(lIt.hasNext())
        {
            source = (LinkedList<Card>)lIt.next();
            highestFaceUp = GetHighestFaceUp( source );
            destination = FindMove(highestFaceUp);
            if(destination != null)
            {
                int index = source.indexOf(highestFaceUp);
                MoveCards(source.subList(index, source.size()), index, source, destination);
                return true;
            }
        }
        return false;
    }

    /*
     * \brief Attempts to clear a spot on the board only if a King is available to take its place as per rule:
     *        5. Don't clear a spot unless there's a King IMMEDIATELY waiting to occupy it.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean ClearSpot()
    {
        LinkedList<LinkedList<Card>> list = GetNoDownCardList();
        LinkedList<Card> source1 = null,
                source2 = null,
                destination1 = null,
                destination2 = null;
        Card c = null;
        ListIterator lIt = list.listIterator();

        while(lIt.hasNext())
        {
            source1 = (LinkedList<Card>)lIt.next();
            destination1 = FindMove(source1.peekFirst());
            if(destination1 != null)
                break;
        }

        if(destination1 != null)
        {
            list = GetDownCardList();
            lIt = list.listIterator();
            while(lIt.hasNext())
            {
                source2 = (LinkedList<Card>)lIt.next();
                c = GetHighestFaceUp(source2);
                if(c != null)
                {
                    if(c.GetCard().equalsIgnoreCase("King"))
                    {
                        destination2 = source1;
                        break;
                    }
                }
            }
        }
        else
            return false;

        if(destination2 == null)
        {
            // Check the stock up list for a King to move onto cleared spot
            c = _stockUp.peekLast();
            if(c != null)
            {
                if(c.GetCard().equalsIgnoreCase("King"))
                {
                    source2 = _stockUp;
                    destination2 = source1;
                }
            }
        }

        if(destination2 != null)
        {
            // A spot to clear was found and a King to replace it was also found.
            MoveCards(source1.subList(0, source1.size()), 0, source1, destination1);
            int index = source2.indexOf(c);
            MoveCards(source2.subList(index, source2.size()), index, source2, destination2);
            
            return true;
        }
        return false;
    }

    /*
     * \brief Attempts to move a card to the foundation stacks if possible as per rule:
     *        7. Only build your Ace stacks (with anything other than an Ace or Deuce) when the play will:
     *              Not interfere with your Next Card Protection
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean BuildFoundations(boolean enableProtection)
    {
        ListIterator lIt;
        Card c;
        LinkedList<Card> source = null, destination = null;

        lIt = _tableaus.listIterator();
        // Check card on the top of each tableau stack to see if it can be moved to a foundation stack
        while(lIt.hasNext())
        {
            source = (LinkedList<Card>)lIt.next();
            if(!source.isEmpty())
            {
                c = source.peekLast();
                if(c.GetFaceUp())
                {
                    destination = GetFoundationStack(c.GetSuit());
                }
                if(destination != null)
                {
                    if(ValidFoundationMove(destination.peekLast(), c)) // Check to see that the move is even valid
                    {
                        // A foundation stack of the same suit was found : now check Next Card Protection rule
                        if(NextCardProtected(c) || !enableProtection)
                        {

                            MoveCards(source.subList(source.size() - 1, source.size()), source.size() - 1, source, destination);
                            return true;
                        }
                    }
                }
            }
        }

        // Check card on top of the stock up stack to see if it can be moved to a foundation stack
        if(!_stockUp.isEmpty())
        {
            c = _stockUp.peekLast();
            destination = GetFoundationStack(c.GetSuit());

            if(destination != null)
            {
                if(ValidFoundationMove(destination.peekLast(), c)) // Check to see that the move is even valid
                {
                    // A foundation stack of the same suit was found : now check Next Card Protection Rule
                    if(NextCardProtected(c) || !enableProtection)
                    {
                        MoveCards(_stockUp.subList(_stockUp.size() - 1, _stockUp.size()), _stockUp.size() - 1, _stockUp, destination);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
     * \brief Checks to see that a card satisfies the Next Card Protection rule.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     * \param c - Card to check for compliance of Next Card Protection.
     */
    private boolean NextCardProtected(Card c)
    {
        boolean nextProtected = false;
        if(c.GetSuit().equalsIgnoreCase("Clubs"))
            nextProtected = FindFaceUpCard(c.GetCard(), "Spades");
        else if(c.GetSuit().equalsIgnoreCase("Spades"))
            nextProtected = FindFaceUpCard(c.GetCard(), "Clubs");
        else if(c.GetSuit().equalsIgnoreCase("Hearts"))
            nextProtected = FindFaceUpCard(c.GetCard(), "Diamonds");
        else if(c.GetSuit().equalsIgnoreCase("Diamonds"))
            nextProtected = FindFaceUpCard(c.GetCard(), "Hearts");

        if(!nextProtected)
            nextProtected = NCPLowerCheck(c);

        return nextProtected;
    }

    /*
     * \brief Checks to see if a card is face up in any of the foundation stacks.
     *
     * \param card - Card rank
     * \param suit - Card suit
     */
    private boolean FindFaceUpCard(String card, String suit)
    {
        ListIterator lIt = _tableaus.listIterator();
        LinkedList<Card> list;
        Card c;

        while(lIt.hasNext())
        {
            list = (LinkedList<Card>)lIt.next();
            ListIterator lIt2 = list.listIterator();
            if(lIt2.hasNext())
            {
                c = (Card)lIt2.next();
                if(c.GetFaceUp() && c.GetCard().equalsIgnoreCase(card) && c.GetSuit().equalsIgnoreCase(suit))
                    return true;
            }
        }
        return false;
    }

    /*
     * \brief Finds and returns the specified face up card in a stack if it exists.
     */
    private Card FindFaceUpCard(String card, String suit, LinkedList<Card> source)
    {
        ListIterator lIt = source.listIterator();
        Card c = null;


        while(lIt.hasNext())
        {
            c = (Card)lIt.next();
            if(c.GetCard().equalsIgnoreCase(card) && c.GetSuit().equalsIgnoreCase(suit) && c.GetFaceUp())
                return c;
        }
        return null;
    }

    /*
     * \brief Finds the specified card in the tableau stacks if it exists and returns it.
     */
    private LinkedList<Card> FindFaceUpCardStack(String card, String suit)
    {
        ListIterator lIt = _tableaus.listIterator();
        LinkedList<Card> list;
        Card c;

        while(lIt.hasNext())
        {
            list = (LinkedList<Card>)lIt.next();
            ListIterator lIt2 = list.listIterator();
            if(lIt2.hasNext())
            {
                c = (Card)lIt2.next();
                if(c.GetFaceUp() && c.GetCard().equalsIgnoreCase(card) && c.GetSuit().equalsIgnoreCase(suit))
                    return list;
            }
        }
        return null;
    }

    /*
     * \brief Checks to see if the Next Lowest Card's are either both in the foundation stacks or both in the tableau stacks.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     * \param c - Card to check.
     */
    private boolean NCPLowerCheck(Card c)
    {
        String color = c.GetColor();
        String card = c.GetCard();
        ListIterator lIt, lIt2;
        LinkedList<Card> list;
        Card b;
        int found = 0;

        // Searching for the opposite color card
        if(color.equalsIgnoreCase("Red"))
            color = "Black";
        else
            color = "Red";

        // Searching for card one rank below
        if(card.equalsIgnoreCase("King"))
            card = "Queen";
        else if(card.equalsIgnoreCase("Queen"))
            card = "Jack";
        else if(card.equalsIgnoreCase("Jack"))
            card = "10";
        else if(card.equalsIgnoreCase("10"))
            card = "9";
        else if(card.equalsIgnoreCase("9"))
            card = "8";
        else if(card.equalsIgnoreCase("8"))
            card = "7";
        else if(card.equalsIgnoreCase("7"))
            card = "6";
        else if(card.equalsIgnoreCase("6"))
            card = "5";
        else if(card.equalsIgnoreCase("5"))
            card = "4";
        else if(card.equalsIgnoreCase("4"))
            card = "3";
        else if(card.equalsIgnoreCase("3"))
            card = "2";
        else
            // Card is an ace or deuce, no protection required
            return true;

        // Search through foundation stacks first and count number of results
        lIt = _foundations.listIterator();
        while(lIt.hasNext())
        {
            list = (LinkedList<Card>)lIt.next();
            lIt2 = list.listIterator();
            while(lIt2.hasNext())
            {
                b = (Card)lIt2.next();
                if(!b.GetColor().equalsIgnoreCase(color))
                {
                    // Card color isn't what we're looking for : ignore this stack
                    break;
                }

                if(b.GetCard().equalsIgnoreCase(card))
                    // Card is the same color and rank as what we're looking for
                    ++found;
            }
        }

        if(found == 2)
            // Both cards are in the foundation stack
            return true;
        else if(found == 1)
            // One card exists in the foundation stack, therefore both cannot exist in the tableau stacks
            return false;
        else
            // No cards found in foundation stacks
            found = 0;

        // Search for face up cards in the tableau stacks and count number of results
        lIt = _tableaus.listIterator();
        while(lIt.hasNext())
        {
            list = (LinkedList<Card>)lIt.next();
            lIt2 = list.listIterator();
            while(lIt2.hasNext())
            {
                b = (Card)lIt2.next();
                if(b.GetFaceUp() && b.GetColor().equalsIgnoreCase(color) && b.GetCard().equalsIgnoreCase(card))
                {
                    // Card is the same color and rank as what we're looking for
                    ++found;

                    // break out of current loop as there are no more possible results in this stack
                    break;
                }
            }
        }
        if(found == 2)
            // Both cards are face up in the tableau stacks
            return true;
        else
            return false;
    }

    /*
     * \brief Tries to play the card on the top of the stock stack.
     */
    private boolean PlayStock()
    {
        LinkedList<Card> destination = null;
        if(!_stockUp.isEmpty())
            destination = FindMove(_stockUp.peekLast());

        if(destination != null)
        {
            int index = _stockUp.size() - 1;
            MoveCards(_stockUp.subList(index, index + 1), index, _stockUp, destination);
            if(_stockUp.isEmpty())
            {
                StockDownClick();
            }
            return true;
        }
        return false;
    }

    /*
     * \brief Counts the number of cards face down in a given stack.
     *
     * \param list - Stack to count.
     */
    public int NumDownCards(LinkedList<Card> list)
    {
        int count = 0;
        ListIterator lIt = list.listIterator();

        while(lIt.hasNext())
        {
            if(!((Card)lIt.next()).GetFaceUp())
                // Card is face down
                ++count;
        }
        return count;
    }

    /*
     * \brief Returns a linked list containing linked lists of each stack that contains cards which are face down.
     */
    private LinkedList<LinkedList<Card>> GetDownCardList()
    {
        ListIterator lIt = _tableaus.listIterator();
        LinkedList<LinkedList<Card>> list = new LinkedList<LinkedList<Card>>();
        LinkedList<Card> cardList;

        while(lIt.hasNext())
        {
            cardList = (LinkedList<Card>)lIt.next();
            if(!cardList.isEmpty())
            {
                if( !cardList.peekFirst().GetFaceUp() )
                    // List has a face down card as its highest card, add it to list
                    list.add(cardList);
            }
        }

        Collections.sort(list, GetDownCardComparator());

        return list;
    }

    /*
     * \brief Returns a linked list containing linked lists of each stack that contains no face down cards.
     */
    private LinkedList<LinkedList<Card>> GetNoDownCardList()
    {
        ListIterator lIt = _tableaus.listIterator();
        LinkedList<LinkedList<Card>> list = new LinkedList<LinkedList<Card>>();
        LinkedList<Card> cardList;

        while(lIt.hasNext())
        {
            cardList = (LinkedList<Card>)lIt.next();
            if(!cardList.isEmpty())
            {
                if(cardList.peekFirst().GetFaceUp())
                    // List has a face up card as its highest card, add it to list
                    list.add(cardList);
            }
        }
        return list;
    }

    /*
     * \brief Comparator that compares the number of face down cards between two linked lists.
     */
    public Comparator<LinkedList<Card>> GetDownCardComparator()
    {
        return new Comparator<LinkedList<Card>>() {
                public int compare(LinkedList<Card> o1, LinkedList<Card> o2)
                {
                    return NumDownCards(o2) - NumDownCards(o1);
                }};
    }

    /*
     * \brief Attempts to find a move for the specified card on the tableau stacks.  Returns the list
     *        which the card can be moved to, null if none found.
     *
     * \param c - Card to find a move for.
     */
    private LinkedList<Card> FindMove(Card c)
    {
        ListIterator lIt = _tableaus.listIterator();
        LinkedList<Card> list;

        while(lIt.hasNext())
        {
            list = (LinkedList<Card>)lIt.next();
            if(ValidTableauMove(list.peekLast(), c))
            {
                return list;
            }
        }
        return null;
    }

    /*
     * \brief Returns the highest face up card from a linked list of cards. Returns null if none found.
     *
     * \param ll - Linked list to scan for face up card.
     */
    private Card GetHighestFaceUp(LinkedList<Card> ll)
    {
        ListIterator lIt = ll.listIterator();
        Card c;

        while(lIt.hasNext())
        {
            c = (Card)lIt.next();
            if(c.GetFaceUp())
                // Card is face up
                return c;
        }
        // No face up cards found : stack is empty
        return null;
    }

    /*
     * \brief Returns the foundation stack for a given suit, null if none exists.
     *
     * \param suit - Suit of the foundation stack to be returned.
     */
    private LinkedList<Card> GetFoundationStack(String suit)
    {
        if(_foundation1.size() > 0)
        {
            if(_foundation1.getFirst().GetSuit().equalsIgnoreCase(suit))
                return _foundation1;
        }
        if(_foundation2.size() > 0)
        {
            if(_foundation2.getFirst().GetSuit().equalsIgnoreCase(suit))
                return _foundation2;
        }
        if(_foundation3.size() > 0)
        {
            if(_foundation3.getFirst().GetSuit().equalsIgnoreCase(suit))
                return _foundation3;
        }
        if(_foundation4.size() > 0)
        {
            if(_foundation4.getFirst().GetSuit().equalsIgnoreCase(suit))
                return _foundation4;
        }
        return null;
    }

    /*
     * \brief Moves cards from one list to another.
     *
     * \param cards - List of cards to be moved.
     * \param index - Index in the source list of cards to be moved.
     * \param source - Source list
     * \param destination - Destination list
     */
    private void MoveCards(java.util.List<Card> cards, int index, LinkedList<Card> source, LinkedList<Card> destination)
    {
        _curSelection = cards;
        _selectionSource = source;
        _selectionIndex = index;
        this.paintImmediately(0, 0, _windowWidth, _windowHeight);
        //repaint();

        // Pause for visual effect
        Pause(_pauseTime);

        destination.addAll(_curSelection);
        _curSelection.clear();

        if(_selectionSource.size() > 0)
        {
            _selectionSource.peekLast().SetFaceUp(true);
        }
        ResetSelection();
        this.paintImmediately(0, 0, _windowWidth, _windowHeight);
    }

    /*
     * \brief Pops up a Victory dialog box and disables the AI.
     */
    private void HandleWin()
    {
        if(CheckVictory())
        {
            JOptionPane.showMessageDialog(this, "Victory!");
            _enableAI = false;
            _cbMenuItem.setState(false);
        }
    }

    /*
     * \brief Returns true if the card is a 5, 6, 7 or 8.  Returns false otherwise.
     */
    private boolean Is5678(Card c)
    {
        if(c.GetCard().equalsIgnoreCase("5") || c.GetCard().equalsIgnoreCase("6") || c.GetCard().equalsIgnoreCase("7") || c.GetCard().equalsIgnoreCase("8"))
            return true;
        else
            return false;
    }

    /*
     * \brief Checks to see if moving the specified card will allow a downcard to be turned over.
     */
    private boolean FreesDownCard(Card c, LinkedList<Card> source)
    {
        int index = 0;
        Card upper, lower = null;
        if(source.size() > 1)
        {
            index = source.indexOf(c);
            if(index > 0)
            {
                lower = source.get(index - 1);
                if(!lower.GetFaceUp())
                    // Card below c on stack is face down
                    return true;
            }
        }

        LinkedList<LinkedList<Card>> list = GetDownCardList();
        ListIterator lIt = list.listIterator();
        LinkedList<Card> lst;

        // Check to see if cards from other stacks can be moved to c's current position
        while(lIt.hasNext())
        {
            lst = (LinkedList<Card>)lIt.next();
            upper = GetHighestFaceUp(lst);
            if(upper != null)
            {
                if(ValidTableauMove(lower, upper))
                    // The move allows a down card to be freed
                    return true;
            }
        }
        return false;
    }

    /*
     * \brief Determines if moving a card to the specified location satisfies the smoothness rule.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean IsSmooth(Card c, LinkedList<Card> destination)
    {
        if(destination.size() > 1)
        {
            // Get card underneath the card on top of stack
            Card b = destination.get(destination.size() - 2);

            if(b.GetSuit().equalsIgnoreCase(c.GetSuit()) && b.GetFaceUp())
            {
                // Card is face up and of the same suit
                return true;
            }
        }
        return false;
    }

    /*
     * \brief Checks whether or not the move should be blocked as per rule:
     *        8. Don't play or transfer a 5, 6, 7 or 8 anywhere unless at least one of these situations will apply after the play:
     *              - It is smooth with it's next highest even/odd partner in the column
     *              - It will allow a play or transfer that will IMMEDIATELY free a downcard
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     *
     * \param c - Card to check
     * \param source - The stack that c belongs to.
     * \param destination - The destination for c.
     */
    private boolean Block5678(Card c, LinkedList<Card> source, LinkedList<Card> destination)
    {
        if(!Is5678(c))
            // Card isn't a 5, 6, 7, or 8 : don't block
            return false;
        if(IsSmooth(c, destination) || FreesDownCard(c, source))
            // Move matches at least one criteria for an allowed move : don't block
            return false;
        else
            // Move matches no criteria for an allowed move : block move
            return true;
    }

    /*
     * \brief Returns the face value of the next lowest card, null if an Ace is passed.
     */
    private String GetNextLowestCard(Card c)
    {
        if(c.GetCard().equalsIgnoreCase("King"))
            return "Queen";
        else if(c.GetCard().equalsIgnoreCase("Queen"))
            return "Jack";
        else if(c.GetCard().equalsIgnoreCase("Jack"))
            return "10";
        else if(c.GetCard().equalsIgnoreCase("10"))
            return "9";
        else if(c.GetCard().equalsIgnoreCase("9"))
            return "8";
        else if(c.GetCard().equalsIgnoreCase("8"))
            return "7";
        else if(c.GetCard().equalsIgnoreCase("7"))
            return "6";
        else if(c.GetCard().equalsIgnoreCase("6"))
            return "5";
        else if(c.GetCard().equalsIgnoreCase("5"))
            return "4";
        else if(c.GetCard().equalsIgnoreCase("4"))
            return "3";
        else if(c.GetCard().equalsIgnoreCase("3"))
            return "2";
        else if(c.GetCard().equalsIgnoreCase("2"))
            return "Ace";
        else
            return null;
    }

    /*
     * \brief Attempts to smooth columns as per rule:
     *        4. Transfer cards from column to column only to allow a downcard to be freed or to make the columns smoother.
     *
     * \reference http://www.chessandpoker.com/solitaire_strategy.html
     */
    private boolean SmoothStacks()
    {
        Card b, c = null;
        LinkedList<Card> source = null, curList;
        String nextCard;
        ListIterator lIt = _tableaus.listIterator();

        while(lIt.hasNext())
        {
            curList = (LinkedList<Card>)lIt.next();
            if(curList.size() > 1)
            {
                b = curList.get(curList.size() - 2);
                if(b.GetFaceUp())
                {
                    nextCard = GetNextLowestCard(curList.peekLast());
                    if(nextCard != null)
                    {
                        source = FindFaceUpCardStack(nextCard, b.GetSuit());
                        if(source != null)
                            c = FindFaceUpCard(nextCard, b.GetSuit(), source);
                        else
                            continue;

                        if(c == null)
                            continue;
                        else if(!source.peekFirst().GetFaceUp())
                        {
                            int index = source.indexOf(c);
                            MoveCards(source.subList(index, source.size()), index, source, curList);
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    /*
     * \brief Initializes the Window.
     */
    public final void InitializeWindow()
    {
        JFrame frame = new JFrame("Solitaire");
        frame.setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.darkGray);
        frame.setSize(_windowWidth, _windowHeight);
        frame.setContentPane(this);
        
        this.addMouseListener(this);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        

        JMenuItem menuItem = new JMenuItem("New Game");
        menuItem.addActionListener(this);
        _cbMenuItem = new JCheckBoxMenuItem("Enable AI");
        _cbMenuItem.addItemListener(this);
        menu.add(menuItem);
        menu.add(_cbMenuItem);
        
        menuBar.add(menu);
        

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

    }

    /*
     * \brief Action Performed Event Handler
     */
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equalsIgnoreCase("New Game"))
        {
            InitializeGame();
            repaint();
        }

    }

    /*
     * \brief Item State Change Event Handler for Enable AI checkbox
     */
    public void itemStateChanged(ItemEvent e)
    {
        if(e.getStateChange() == 1)
        {
            _enableAI = true;
            MoveLoop();
        }
        else
        {
            _enableAI = false;
        }
    }

    /*
     * \brief Pauses execution for a specified time period.
     */
    public void Pause(long timeMillis)
    {
        long curTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - curTime < timeMillis)
        {
            // Do nothing
        }
    }
}