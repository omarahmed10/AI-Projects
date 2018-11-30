package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
 
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
 
/**
 * This class is a Swing component that presents a choice to the user. It allows
 * the choice to be presented in a JLists. Additionally, it displays the name of
 * the choice with a JLabel. It allows an arbitrary value to be associated with
 * each possible choice. Note that this component only allows one item to be
 * selected at a time. Multiple selections are not supported.
 */
public class Starter extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
 
    // These fields hold property values for this component
    String name; // The overall name of the choice
    String[] labels; // The text for each choice option
 
    Object[] values; // Arbitrary values associated with each option
 
    int selection; // The selected choice
 
    // These are the legal values for the presentation field
    Starter otherPlayer;
    // These components are used for each of the 3 possible presentations
    JList list; // One type of presentation
    JRadioButton[] radiobuttons; // Yet another type
    // The list of objects that are interested in our state
    ArrayList listeners = new ArrayList();
 
    // other Player Type
    // The constructor method sets everything up
    public Starter(String name, String[] labels, Object[] values, int defaultSelection, Starter other) {
        // Copy the constructor arguments to instance fields
        this.name = name;
        this.labels = labels;
        this.values = values;
        this.selection = defaultSelection;
        this.otherPlayer = other;
        // If no values were supplied, use the labels
        if (values == null)
            this.values = labels;
        initList();
    }
 
    // Initialization for JList presentation
    void initList() {
        list = new JList(labels); // Create the list
        list.setSelectedIndex(selection); // Set initial state
        // Handle state changes
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Starter.this.select(list.getSelectedIndex());
 
            }
        });
 
        // Lay out list and name label vertically
        BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS); // vertical
        JLabel label = new JLabel(name);
        this.removeAll();
        this.setLayout(box);
        this.add(label);
        this.add(new JScrollPane(list));
    }
 
    // These simple property accessor methods just return field values
    // These are read-only properties. The values are set by the constructor
    // and may not be changed.
    @Override
    public String getName() {
        return name;
    }
 
    public String[] getLabels() {
        return labels;
    }
 
    public Object[] getValues() {
        return values;
    }
 
    /** Return the index of the selected item */
    public int getSelectedIndex() {
        return selection;
    }
 
    /** Return the object associated with the selected item */
    public Object getSelectedValue() {
        return values[selection];
    }
 
    public void setOtherPlayer(Starter o) {
        this.otherPlayer = o;
    }
 
    public Starter getOtherPlayer() {
        return this.otherPlayer;
    }
 
    /**
     * Set the selected item by specifying its index. Calling this method
     * changes the on-screen display but does not generate events.
     */
    public void setSelectedIndex(int selection) {
        list.setSelectedIndex(selection);
        this.selection = selection;
    }
 
    /**
     * This internal method is called when the selection changes. It stores the
     * new selected index, and fires events to any registered listeners. The
     * event listeners registered on the JList, JComboBox, or JRadioButtons all
     * call this method.
     */
    protected void select(int selection) {
        this.selection = selection; // Store the new selected index
        if (this.selection == 4 || this.selection == 5 || this.selection == 6) {
            this.getOtherPlayer().list.disable();
            this.getOtherPlayer().list.setSelectedIndex(0);
        } else {
            this.getOtherPlayer().list.enable();
        }
        if (!listeners.isEmpty()) { // If there are any listeners registered
            // Create an event object to describe the selection
            Starter.Event e = new Starter.Event(this, selection, values[selection]);
            // Loop through the listeners using an Iterator
            for (Iterator i = listeners.iterator(); i.hasNext();) {
                Starter.Listener l = (Starter.Listener) i.next();
                l.itemChosen(e); // Notify each listener of the selection
            }
        }
    }
 
    private void setLabels() {
        if (this.selection == 4 || this.selection == 5 || this.selection == 6) {
            this.getOtherPlayer().list.disable();
        }
        else if (this.selection == 1 || this.selection == 2 || this.selection == 3) {
            this.getOtherPlayer().labels = new String[] {"Passive", "Aggresive", "Human", "Pacifist" };
 
        }
        else {
            this.getOtherPlayer().labels = new String[] {"Passive", "Aggresive", "Human", "Pacifist", "Greedy", "Astar",
                    "RTAstar" };
        }
        this.getOtherPlayer().initList();
 
    }
 
    // These methods are for event listener registration and deregistration
    public void addItemChooserListener(Starter.Listener l) {
        listeners.add(l);
    }
 
    public void removeItemChooserListener(Starter.Listener l) {
        listeners.remove(l);
    }
 
    /**
     * This inner class defines the event type generated by ItemChooser objects
     * The inner class name is Event, so the full name is ItemChooser.Event
     */
    public static class Event extends java.util.EventObject {
        int selectedIndex; // index of the selected item
 
        Object selectedValue; // the value associated with it
 
        public Event(Starter source, int selectedIndex, Object selectedValue) {
            super(source);
            this.selectedIndex = selectedIndex;
            this.selectedValue = selectedValue;
        }
 
        public Starter getItemChooser() {
            return (Starter) getSource();
        }
 
        public int getSelectedIndex() {
            return selectedIndex;
        }
 
        public Object getSelectedValue() {
            return selectedValue;
        }
    }
 
    /**
     * This inner interface must be implemented by any object that wants to be
     * notified when the current selection in a ItemChooser component changes.
     */
    public interface Listener extends java.util.EventListener {
        public void itemChosen(Starter.Event e);
    }
 
 
}