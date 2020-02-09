/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 *
 * @author win10
 */
public final class MultiSlider extends JPanel implements ValueChangeListener {

    public MultiSlider(ORIENTATION o, int min_value, int max_value, 
        int step_size, boolean can_cross, Dimension display_size)
        throws InvalidSliderRangeException {
        /*	Initialise the basic panel object. */
        super();
        // Set the orientation of the slider
        this.orientation = o;
        /* We can't create a valid MultiSlider if the minimum and maximum values
        *   are the same.*/
        if (min_value == max_value) {
            throw new InvalidSliderRangeException();
        }
        /* Make sure minimum and maximum values are what they say they are. */
        if (min_value > max_value) {
            int temp = max_value;
            max_value = min_value;
            min_value = temp;
        }
        /* If the step size is zero, or larger than the differeence between 
        *   maximum and minimum values or is not a divisor, we can't create the 
        *   MultiSlider. */
        if (step_size == 0 || step_size > max_value - min_value
            || (max_value - min_value) % step_size != 0) {
            throw new InvalidSliderRangeException();
        }

        this.min_value = min_value;
        this.max_value = max_value;
        this.step_size = step_size;
        this.can_cross = can_cross;

        if (this.orientation == ORIENTATION.VERTICAL) {
            display_size.setSize(display_size.height, display_size.width);
        }
        this.setPreferredSize(display_size);
        this.setMinimumSize(display_size);
        this.setMaximumSize(display_size);
    }

    void addTab(MultiSliderTab t) {
        if (t == null) {
            return;
        }
        t.setParent(this);
        tabs.getLast().setActive(false);
        tabs.add(t);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.DARK_GRAY);

        if (this.orientation == ORIENTATION.HORIZONTAL) {
            int middle = this.getHeight() / 2;
            g2.drawLine(8, middle, this.getWidth() - 16, middle);
        } else {
            int middle = this.getWidth() / 2;
            g2.drawLine(middle, 8, middle, this.getHeight() - 16);
        }
    }

    public void addTab(String name, Color colour) {
        int tab_value = this.min_value;
        boolean value_ok = false;

        if (tabs.size() > 0) {
            while (tab_value < this.max_value && !value_ok) {
                value_ok = true;
                for (MultiSliderTab t : tabs) {
                    if (t.getValue() == tab_value) {
                        value_ok = false;
                        break;
                    }
                }
                if (!value_ok) tab_value += this.step_size;
            }
        }

        MultiSliderTab t = new MultiSliderTab(name, colour, tab_value, this);
    }

    @Override
    public void valueChanged(int new_value) {

    }

    @Override
    public ORIENTATION getOrientation() {
        return null;
    }

    public enum ORIENTATION {
        HORIZONTAL, VERTICAL, BOTH
    }

    private final ORIENTATION orientation;
    private int min_value, max_value, step_size;
    private boolean can_cross;
    private LinkedList<MultiSliderTab> tabs;
}

class MultiSliderTab {

    MultiSliderTab(String name, Color colour, int value, ValueChangeListener v) {
        this.name = name;
        this.colour = (colour != null) ? colour : Color.GRAY;
        this.value = value;
        this.v = v;
        this.is_active = false;
    }

    void setParent(MultiSlider parent) {
        this.parent = parent;
        this.setActive(true);
    }

    int getValue() {
        return this.value;
    }

    void setActive(boolean a) {
        this.is_active = a;
    }

    boolean isActive() {
        return this.is_active;
    }

    private MultiSlider parent;
    private final ValueChangeListener v;
    private final String name;
    private final Color colour;
    private final int value;
    private boolean is_active;
}
