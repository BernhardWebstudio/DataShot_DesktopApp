/**
 * ImagePanel.java
 * edu.harvard.mcz.imagecapture
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.ui.frame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * ImageZoomPanel displays an image in an ImagePanel within a JScrollPane
 * along with a set of zoom controls.
 *
 * @see ImagePanel
 * @see ImageDisplayFrame
 */
public class ImageZoomPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 8831170774688354296L;

    private static final Logger log =
            LoggerFactory.getLogger(ImageZoomPanel.class);

    private JPanel jPanel = null;
    private JButton jButton = null;
    private JButton jButton1 = null;
    private JButton jButton2 = null;
    private JButton jButton3 = null;
    private JScrollPane jScrollPane = null;
    private ImagePanel imagePanel = null;
    private JButton jButton4 = null;
    private Cursor originalCursor = null;
    private Cursor zoomCursor = null;

    /**
     * This method initializes without an image
     */
    public ImageZoomPanel() {
        super();
        initialize();
    }

    /**
     * Constructor to initialize with an image.
     *
     * @param anImage the image to display
     */
    public ImageZoomPanel(BufferedImage anImage) {
        super();
        initialize();
        setImage(anImage);
    }

    public void setImage(Image anImage) {
        imagePanel.setImage(anImage);
    }

    public void setImage(BufferedImage anImage) {
        // ImageObserver observer = null;
        imagePanel.setImage(anImage);
        //		//jLabel.setText("");
        //		Dimension size = new Dimension(image.getWidth(),
        // image.getHeight());
        //        // Center image in this component.
        //        int x = (jLabel.getWidth() - size.width)/2;
        //        int y = (jLabel.getHeight() - size.height)/2;
        //		jLabel.prepareImage(anImage, observer);
        //		Graphics2D g2 = (Graphics2D) jLabel.getGraphics();
        //		g2.drawImage(image, x, y, observer);
        //		jLabel.paint(g2);
        imagePanel.repaint();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(new Dimension(553, 323));
        this.setLayout(new BorderLayout());
        this.add(getJPanel(), BorderLayout.NORTH);
        this.add(getJScrollPane(), BorderLayout.CENTER);
        URL cursorFile = this.getClass().getResource(
                "/edu/harvard/mcz/imagecapture/resources/images/magnifying_glass.gif");
        try {
            Image zoomCursorImage = ImageIO.read(cursorFile);
            Point hotPoint = new Point(5, 5);
            zoomCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    zoomCursorImage, hotPoint, "ZoomCursor");
        } catch (IOException | IllegalArgumentException e) {
            log.error("Unable to load ZoomCursor. ", e);
            zoomCursor = null;
        }
        imagePanel.addMouseListener(this);
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 4;
            gridBagConstraints12.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 3;
            gridBagConstraints11.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getJButton(), gridBagConstraints2);
            jPanel.add(getJButton1(), gridBagConstraints);
            jPanel.add(getJButton2(), gridBagConstraints1);
            jPanel.add(getJButton3(), gridBagConstraints11);
            jPanel.add(getJButton4(), gridBagConstraints12);
        }
        return jPanel;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Zoom In +");
            // VK_PLUS only works for keyboard layouts where the + key is
            // primary, not for US keyboard layouts where it is <shift>=
            // VK_ADD references the + key on a numeric keypad.
            // jButton.setMnemonic(KeyEvent.VK_PLUS);
            // VK_EQUALS as a mnemonic recognizes <alt><shift>= and <alt>=
            jButton.setMnemonic(KeyEvent.VK_EQUALS);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    zoomIn();
                    zoomIn();
                    zoomOut();
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Zoom Out -");
            jButton1.setMnemonic(KeyEvent.VK_MINUS);
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    zoomOut();
                }
            });
        }
        return jButton1;
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton2() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setText("Full 1");
            jButton2.setMnemonic(KeyEvent.VK_1);
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    zoomFull();
                }
            });
        }
        return jButton2;
    }

    /**
     * This method initializes jButton3
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton3() {
        if (jButton3 == null) {
            jButton3 = new JButton();
            jButton3.setText("Fit Window");
            jButton3.setMnemonic(KeyEvent.VK_F);
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    zoomToFit();
                }
            });
        }
        return jButton3;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            imagePanel = new ImagePanel();
            // jLabel.setText("Image goes here");
            jScrollPane = new JScrollPane();
            jScrollPane.setAutoscrolls(true);
            jScrollPane.setViewportView(imagePanel);
        }
        return jScrollPane;
    }

    public void zoomFull() {
        imagePanel.fullSize();
        imagePanel.repaint();
        imagePanel.doLayout();
        jScrollPane.doLayout();
    }

    public void zoomToFit() {
        imagePanel.zoomToFit(jScrollPane.getWidth() - 1, jScrollPane.getHeight() - 1);
        imagePanel.repaint();
        imagePanel.doLayout();
        jScrollPane.doLayout();
    }

    public void zoomIn() {
        imagePanel.zoomIn();
        imagePanel.repaint();
        imagePanel.doLayout();
        int yval = jScrollPane.getVerticalScrollBar().getValue();
        jScrollPane.getVerticalScrollBar().setValue(
                (int) (yval + (jScrollPane.getHeight() * 0.1)));
        int xval = jScrollPane.getHorizontalScrollBar().getValue();
        jScrollPane.getHorizontalScrollBar().setValue(
                (int) (xval + (jScrollPane.getWidth() * 0.1)));
        jScrollPane.revalidate();
        jScrollPane.getParent().validate();
        jScrollPane.setVisible(false);
        jScrollPane.setVisible(true);
        jScrollPane.validate();
        jScrollPane.doLayout();
    }

    public void zoomOut() {
        imagePanel.zoomOut();
        imagePanel.repaint();
        imagePanel.doLayout();
        int yval = jScrollPane.getVerticalScrollBar().getValue();
        jScrollPane.getVerticalScrollBar().setValue(
                (int) (yval - (jScrollPane.getHeight() * 0.1)));
        int xval = jScrollPane.getHorizontalScrollBar().getValue();
        jScrollPane.getHorizontalScrollBar().setValue(
                (int) (xval - (jScrollPane.getWidth() * 0.1)));
        jScrollPane.setVisible(false);
        jScrollPane.setVisible(true);
        jScrollPane.validate();
        jScrollPane.doLayout();
    }

    public void center() {
        imagePanel.center();
        imagePanel.repaint();
        imagePanel.doLayout();
        jScrollPane.doLayout();
    }

    /**
     * This method initializes jButton4
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton4() {
        if (jButton4 == null) {
            jButton4 = new JButton();
            jButton4.setText("Center");
            jButton4.setMnemonic(KeyEvent.VK_C);
            jButton4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    center();
                }
            });
        }
        return jButton4;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point clickAt = e.getPoint();
            Dimension size = imagePanel.getSize();
            imagePanel.zoomIn();
            imagePanel.repaint();
            imagePanel.doLayout();
            imagePanel.centerOn(clickAt);
            imagePanel.invalidate();
            jScrollPane.revalidate();
            jScrollPane.getParent().validate();
            jScrollPane.setVisible(false);
            jScrollPane.setVisible(true);
            jScrollPane.validate();
            jScrollPane.doLayout();
            zoomIn();
            zoomOut();
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            Point clickAt = e.getPoint();
            zoomOut();
            imagePanel.centerOn(clickAt);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        originalCursor = imagePanel.getCursor();
        if (zoomCursor != null) {
            imagePanel.setCursor(zoomCursor);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        imagePanel.setCursor(originalCursor);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

} //  @jve:decl-index=0:visual-constraint="10,10"
