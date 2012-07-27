package org.jdesktop.swingx;

import org.jdesktop.swingx.calendar.DateSpan;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 05-Apr-2007
 * Time: 15:30:25
 */

public class JXDateTimePicker extends JXDatePicker {

    private TodayPanel linkPanel;

    public JXDateTimePicker() {
        this(System.currentTimeMillis());
    }

    public JXDateTimePicker(long millis) {
        super(millis);
        getEditor().setFont(UIManager.getFont("TextField.font"));       // jse5.0 buggy
        getEditor().setBorder(UIManager.getBorder("TextField.border")); // .. I don't like the existing choice (inconsistant)
        //((JTextComponent)getEditor()).setMargin((Insets) UIManager.get("TextField.margin"));
        linkPanel = new TodayPanel();
    }

    @Override
    public JPanel getLinkPanel() {
        return linkPanel;
    }

    public void setLinkDate(long linkDate, String linkFormatString) {
        super.setLinkDate(linkDate, linkFormatString);
    }

    public Date getTime() {
        return linkPanel != null ? linkPanel.getTime() : null;
    }

    public Date getDate() {
        Date date = super.getDate();
        if (date != null) {
            Calendar dt = Calendar.getInstance();
            dt.setTime(date);
            Calendar tm = Calendar.getInstance();
            Date time = getTime();
            if (time != null) {
                tm.setTime(time);
                dt.set(Calendar.HOUR_OF_DAY, tm.get(Calendar.HOUR_OF_DAY));
                dt.set(Calendar.MINUTE, tm.get(Calendar.MINUTE));
                dt.set(Calendar.SECOND, tm.get(Calendar.SECOND));
            }
            return dt.getTime();
        }
        return date;
    }

    /* time spinner hack ontop of the same class in JXDatePicker */

    protected class TodayPanel extends JXPanel {
        MessageFormat linkFormat = new MessageFormat(UIManager.getString("JXDatePicker.linkFormat"));
        JSpinner spinner;

        protected TodayPanel() {
            super(new FlowLayout());
            setBackgroundPainter(new MattePainter(new GradientPaint(0, 0, new Color(238, 238, 238), 0, 1, Color.WHITE)));
            JXHyperlink todayLink = new JXHyperlink(new TodayAction());
            Color textColor = new Color(16, 66, 104);
            todayLink.setUnclickedColor(textColor);
            todayLink.setClickedColor(textColor);

            add(new JLabel("Time: "));
            spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm:ss"));
            spinner.setFont(JXDateTimePicker.this.getFont());                                           // jse5.0 buggy
            ((JComponent) spinner.getEditor()).setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));  // insets?
            ((JComponent) spinner.getEditor()).setBackground(UIManager.getColor("TextField.background"));
            add(spinner);
            //pending - quick entry for times? popup JSlider? tick granularity??

            //NB re-adding today link over-extends bounds of the popup (white edges to jxmonthview)
            add(todayLink);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(187, 187, 187));
            g.drawLine(0, 0, getWidth(), 0);
            g.setColor(new Color(221, 221, 221));
            g.drawLine(0, 1, getWidth(), 1);
        }

        public Date getTime() {
            return (Date) spinner.getValue();
        }

        class TodayAction extends AbstractAction {
            TodayAction() {
                super(linkFormat.format(new Object[]{new Date(getLinkDate())}));
            }

            public void actionPerformed(ActionEvent ae) {
                DateSpan span = new DateSpan(getLinkDate(), getLinkDate());
                getMonthView().ensureDateVisible(span.getStart());
            }
        }
    }
}
