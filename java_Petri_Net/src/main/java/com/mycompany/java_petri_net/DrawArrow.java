/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java_petri_net;

/**
 *
 * @author caparies
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Draw arrows
 *
 * @author xiangqian
 * @date 16:00 2019/10/31
 */
public class DrawArrow {

    public static class DrawPanel extends JPanel {

        private BasicStroke lineStroke;

        public DrawPanel() {
            lineStroke = new BasicStroke(2.0f);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            Line2D.Double line2D = null;

            //
            line2D = new Line2D.Double(120, 100, 300, 300);
            draw(g2d, line2D);

//            line2D = new Line2D.Double(110, 330, 300, 330);
//            draw(g2d, line2D);
//
//            line2D = new Line2D.Double(400, 200, 260, 200);
//            draw(g2d, line2D);
        }

        private void draw(Graphics2D g2d, Line2D.Double line2D) {
            drawArrow(g2d, line2D);
            drawLine(g2d, line2D);
        }

        /**
         * Draw the line
         */
        private void drawLine(Graphics2D g2d, Line2D.Double line2D) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(lineStroke);
            g2d.draw(line2D);
        }

        /**
         * Draw arrows
         */
        private void drawArrow(Graphics2D g2d, Line2D.Double line2D) {
            Arrow arrow = getArrow(line2D);
            GeneralPath arrow2D = new GeneralPath();
            arrow2D.moveTo(arrow.point1.x, arrow.point1.y);
            arrow2D.lineTo(arrow.point2.x, arrow.point2.y);
            arrow2D.lineTo(arrow.point3.x, arrow.point3.y);
            arrow2D.closePath();
            g2d.setColor(arrow.color);
            g2d.fill(arrow2D);
        }

        /**
         * Get the arrow entity class
         */
        private Arrow getArrow(Line2D.Double line2D) {
            Arrow arrow;
            arrow = new Arrow();
            arrow.height = 60;
            arrow.angle = 30;
            arrow.color = Color.BLACK;

            // Calculate the hypotenuse
            double hypotenuse = arrow.height / Math.cos(Math.toRadians(arrow.angle / 2));

            // Calculate the quadrant of the current line
            int quadrant = -1;
            if (line2D.x1 > line2D.x2 && line2D.y1 < line2D.y2) {
                quadrant = 1;
            } else if (line2D.x1 < line2D.x2 && line2D.y1 < line2D.y2) {
                quadrant = 2;
            } else if (line2D.x1 < line2D.x2 && line2D.y1 > line2D.y2) {
                quadrant = 3;
            } else if (line2D.x1 > line2D.x2 && line2D.y1 > line2D.y2) {
                quadrant = 4;
            }

            // Calculate the angle of the line
            double linAngle = getLineAngle(line2D.x1, line2D.y1, line2D.x2, line2D.y2);
            if (Double.isNaN(linAngle)) {
                // The line is perpendicular to the x axis
                if (line2D.x1 == line2D.x2) {
                    if (line2D.y1 < line2D.y2) {
                        linAngle = 90;
                    } else {
                        linAngle = 270;
                    }
                    quadrant = 2;
                }
            } // The line is perpendicular to the y axis
            else if (linAngle == 0) {
                if (line2D.y1 == line2D.y2) {
                    if (line2D.x1 < line2D.x2) {
                        linAngle = 0;
                    } else {
                        linAngle = 180;
                    }
                    quadrant = 2;
                }
            }

            // Upper half arrow
            double xAngle = linAngle - arrow.angle / 2; // Angle with x axis
            double py0 = hypotenuse * Math.sin(Math.toRadians(xAngle)); // Calculate the increment in the y direction
            double px0 = hypotenuse * Math.cos(Math.toRadians(xAngle)); // Calculate the increment in the x direction

            // lower half arrow
            double yAngle = 90 - linAngle - arrow.angle / 2; // Angle with y axis
            double px1 = hypotenuse * Math.sin(Math.toRadians(yAngle));
            double py1 = hypotenuse * Math.cos(Math.toRadians(yAngle));

            // first quadrant
            if (quadrant == 1) {
                px0 = -px0;
                px1 = -px1;

            } else if (quadrant == 2) {
                // do nothing
            } else if (quadrant == 3) {
                py0 = -py0;
                py1 = -py1;

            } else if (quadrant == 4) {
                py0 = -py0;
                px0 = -px0;

                px1 = -px1;
                py1 = -py1;
            }

            // build
            arrow.point1 = new Point2D.Double();
            arrow.point1.x = line2D.x1;
            arrow.point1.y = line2D.y1;

            arrow.point2 = new Point2D.Double();
            arrow.point2.x = line2D.x1 + px0;
            arrow.point2.y = line2D.y1 + py0;

            arrow.point3 = new Point2D.Double();
            arrow.point3.x = line2D.x1 + px1;
            arrow.point3.y = line2D.y1 + py1;

            return arrow;
        }

        /**
         * Get the angle between the line and the X axis
         *
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        protected double getLineAngle(double x1, double y1, double x2, double y2) {
            double k1 = (y2 - y1) / (x2 - x1);
            double k2 = 0;
            return Math.abs(Math.toDegrees(Math.atan((k2 - k1) / (1 + k1 * k2))));
        }
    }

    /**
     * Arrow entity class
     *
     * @author xiangqian
     * @date 16:06 2019/10/31
     */
    public static class Arrow {

        double height; // the height of the arrow
        double angle; // arrow angle
        Color color; // Arrow color
        Point2D.Double point1;
        Point2D.Double point2;
        Point2D.Double point3;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Draw the arrow");
        Dimension dimension = new Dimension(800, 600);
        frame.setSize(dimension);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new DrawPanel());
        frame.setVisible(true);
    }

}
