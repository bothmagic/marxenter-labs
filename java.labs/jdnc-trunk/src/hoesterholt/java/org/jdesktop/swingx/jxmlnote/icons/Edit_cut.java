/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/

package org.jdesktop.swingx.jxmlnote.icons;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


/**
 * This class has been automatically generated using <a
 * href="https://flamingo.dev.java.net">Flamingo SVG transcoder</a>.
 */
public class Edit_cut implements FlamencoIconAdapter {
	/**
	 * Paints the transcoded SVG image on the specified graphics context. You
	 * can install a custom transformation on the graphics context to scale the
	 * image.
	 * 
	 * @param g
	 *            Graphics context.
	 */
	public  void paint(Graphics2D g) {
        Shape shape = null;
        Paint paint = null;
        Stroke stroke = null;
        
        float origAlpha = 1.0f;
        Composite origComposite = ((Graphics2D)g).getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
        AffineTransform defaultTransform_ = g.getTransform();
// 
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_0 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0
paint = new LinearGradientPaint(new Point2D.Double(292.9716796875, 4.75927734375), new Point2D.Double(296.9397888183594, 10.711433410644531), new float[] {0.0f,1.0f}, new Color[] {new Color(238, 238, 236, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(-4.12775993347168f, 0.0f, 0.0f, 4.136600017547607f, 1244.4599609375f, -11.904999732971191f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(34.174313, 1.6249996);
((GeneralPath)shape).curveTo(34.38626, 1.6935354, 34.59157, 1.7696619, 34.798294, 1.842502);
((GeneralPath)shape).curveTo(35.44971, 4.0395036, 38.469776, 6.261222, 37.321354, 8.449133);
((GeneralPath)shape).curveTo(33.49551, 14.82952, 29.697021, 21.294565, 25.89976, 27.72527);
((GeneralPath)shape).curveTo(25.154013, 27.872171, 24.401731, 27.952183, 23.647995, 27.96996);
((GeneralPath)shape).curveTo(22.061604, 28.01017, 20.433064, 27.775465, 18.927431, 27.23589);
((GeneralPath)shape).curveTo(23.978304, 18.684616, 29.031301, 10.114483, 34.174313, 1.6249996);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(34.174313, 1.6249996);
((GeneralPath)shape).curveTo(34.38626, 1.6935354, 34.59157, 1.7696619, 34.798294, 1.842502);
((GeneralPath)shape).curveTo(35.44971, 4.0395036, 38.469776, 6.261222, 37.321354, 8.449133);
((GeneralPath)shape).curveTo(33.49551, 14.82952, 29.697021, 21.294565, 25.89976, 27.72527);
((GeneralPath)shape).curveTo(25.154013, 27.872171, 24.401731, 27.952183, 23.647995, 27.96996);
((GeneralPath)shape).curveTo(22.061604, 28.01017, 20.433064, 27.775465, 18.927431, 27.23589);
((GeneralPath)shape).curveTo(23.978304, 18.684616, 29.031301, 10.114483, 34.174313, 1.6249996);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_0);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_1 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
paint = new LinearGradientPaint(new Point2D.Double(20.28802490234375, 6.460364818572998), new Point2D.Double(24.325969696044922, 23.942537307739258), new float[] {0.0f,1.0f}, new Color[] {new Color(226, 226, 226, 255),new Color(216, 216, 216, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(34.288822, 4.25);
((GeneralPath)shape).curveTo(34.0577, 4.5574527, 33.839207, 5.120942, 33.602795, 5.40625);
((GeneralPath)shape).curveTo(29.555939, 12.158979, 25.440784, 18.90033, 21.378977, 25.625);
((GeneralPath)shape).curveTo(21.318424, 25.878117, 20.565046, 26.637291, 21.366936, 26.567963);
((GeneralPath)shape).curveTo(22.478493, 26.765842, 23.638681, 26.918568, 24.746761, 26.625);
((GeneralPath)shape).curveTo(28.505753, 20.407795, 32.19264, 14.142582, 35.943047, 7.923178);
((GeneralPath)shape).curveTo(36.28552, 7.5359044, 36.35216, 6.99792, 35.9924, 6.611197);
((GeneralPath)shape).curveTo(35.462387, 5.794589, 34.925465, 4.936482, 34.382374, 4.15625);
((GeneralPath)shape).lineTo(34.311813, 4.2269607);
((GeneralPath)shape).lineTo(34.288822, 4.25);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_1);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_2 = g.getTransform();
g.transform(new AffineTransform(3.637890100479126f, 0.0f, 0.0f, 3.4703800678253174f, -1056.1199951171875f, -16.007200241088867f));
// _0_0_2
paint = new LinearGradientPaint(new Point2D.Double(294.594970703125, 12.187602996826172), new Point2D.Double(297.1851501464844, 13.339599609375), new float[] {0.0f,1.0E-9f,1.0f}, new Color[] {new Color(150, 42, 183, 255),new Color(168, 26, 201, 255),new Color(220, 76, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0103000402450562f, 1.00797003441323E-18f, -0.15980100631713867f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(297.044, 12.3003);
((GeneralPath)shape).lineTo(296.399, 13.3848);
((GeneralPath)shape).lineTo(295.133, 14.7188);
((GeneralPath)shape).lineTo(294.732, 13.6729);
((GeneralPath)shape).lineTo(295.747, 11.9604);
((GeneralPath)shape).lineTo(297.044, 12.3003);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(102, 12, 0, 255);
stroke = new BasicStroke(0.281441f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(297.044, 12.3003);
((GeneralPath)shape).lineTo(296.399, 13.3848);
((GeneralPath)shape).lineTo(295.133, 14.7188);
((GeneralPath)shape).lineTo(294.732, 13.6729);
((GeneralPath)shape).lineTo(295.747, 11.9604);
((GeneralPath)shape).lineTo(297.044, 12.3003);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_2);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_3 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
paint = new LinearGradientPaint(new Point2D.Double(296.4861145019531, 15.506916046142578), new Point2D.Double(296.529052734375, 9.876952171325684), new float[] {0.0f,1.0E-9f,1.0f}, new Color[] {new Color(150, 42, 183, 255),new Color(168, 26, 201, 255),new Color(220, 76, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(3.637890100479126f, 0.0f, 0.0f, 3.4703800678253174f, -1056.1199951171875f, -16.007200241088867f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(20.40625, 26.96875);
((GeneralPath)shape).curveTo(19.183905, 27.455467, 19.192232, 29.00393, 18.481272, 29.932762);
((GeneralPath)shape).curveTo(18.138948, 30.648558, 17.537483, 31.27899, 17.28125, 32.03125);
((GeneralPath)shape).curveTo(17.27157, 32.546642, 17.729202, 33.391476, 18.3125, 32.9375);
((GeneralPath)shape).curveTo(19.697475, 31.791172, 20.876865, 30.39882, 21.756725, 28.810629);
((GeneralPath)shape).curveTo(21.989088, 28.320597, 22.552477, 27.916466, 22.625, 27.40625);
((GeneralPath)shape).curveTo(22.086432, 26.835442, 21.112183, 26.873224, 20.40625, 26.96875);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_3);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_4 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
paint = new LinearGradientPaint(new Point2D.Double(292.9716796875, 4.75927734375), new Point2D.Double(296.9397888183594, 10.711433410644531), new float[] {0.0f,1.0f}, new Color[] {new Color(238, 238, 236, 255),new Color(255, 255, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(4.053430080413818f, 0.0f, 0.0f, 4.136600017547607f, -1175.5400390625f, -11.904999732971191f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.960099, 1.6249996);
((GeneralPath)shape).curveTo(12.751966, 1.6935354, 12.550355, 1.7696619, 12.347353, 1.842502);
((GeneralPath)shape).curveTo(11.707669, 4.0395036, 8.741988, 6.261222, 9.86973, 8.449133);
((GeneralPath)shape).curveTo(13.626677, 14.82952, 17.35676, 21.294565, 21.08564, 27.72527);
((GeneralPath)shape).curveTo(21.817957, 27.872171, 22.55669, 27.952183, 23.296852, 27.96996);
((GeneralPath)shape).curveTo(24.854677, 28.01017, 26.45389, 27.775465, 27.932407, 27.23589);
((GeneralPath)shape).curveTo(22.972492, 18.684616, 18.010492, 10.114483, 12.960099, 1.6249996);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.960099, 1.6249996);
((GeneralPath)shape).curveTo(12.751966, 1.6935354, 12.550355, 1.7696619, 12.347353, 1.842502);
((GeneralPath)shape).curveTo(11.707669, 4.0395036, 8.741988, 6.261222, 9.86973, 8.449133);
((GeneralPath)shape).curveTo(13.626677, 14.82952, 17.35676, 21.294565, 21.08564, 27.72527);
((GeneralPath)shape).curveTo(21.817957, 27.872171, 22.55669, 27.952183, 23.296852, 27.96996);
((GeneralPath)shape).curveTo(24.854677, 28.01017, 26.45389, 27.775465, 27.932407, 27.23589);
((GeneralPath)shape).curveTo(22.972492, 18.684616, 18.010492, 10.114483, 12.960099, 1.6249996);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_4);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_5 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
paint = new LinearGradientPaint(new Point2D.Double(20.28802490234375, 6.460364818572998), new Point2D.Double(24.325969696044922, 23.942537307739258), new float[] {0.0f,1.0f}, new Color[] {new Color(226, 226, 226, 255),new Color(216, 216, 216, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(12.719667, 4.25);
((GeneralPath)shape).curveTo(12.336632, 5.3766794, 11.270006, 6.2059646, 11.004855, 7.40625);
((GeneralPath)shape).curveTo(14.713376, 13.800362, 18.475798, 20.175379, 22.181757, 26.5625);
((GeneralPath)shape).curveTo(23.380123, 26.820799, 24.610197, 26.655657, 25.795113, 26.40625);
((GeneralPath)shape).curveTo(25.606339, 25.665808, 25.056911, 25.07532, 24.76513, 24.3767);
((GeneralPath)shape).curveTo(20.870525, 17.806173, 16.94143, 11.242872, 13.087127, 4.65625);
((GeneralPath)shape).curveTo(13.072466, 4.50464, 12.870425, 4.1721153, 12.719667, 4.25);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_5);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_6 = g.getTransform();
g.transform(new AffineTransform(0.9798930287361145f, 0.0f, 0.0f, 1.0f, 0.311383992433548f, 0.17404299974441528f));
// _0_0_6
paint = new LinearGradientPaint(new Point2D.Double(22.225399017333984, 23.84343147277832), new Point2D.Double(24.190448760986328, 22.86090660095215), new float[] {0.0f,1.0f}, new Color[] {new Color(186, 189, 182, 255),new Color(238, 238, 236, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(24.190449, 23.843431);
((GeneralPath)shape).curveTo(24.190748, 24.194653, 24.003546, 24.519323, 23.699429, 24.695023);
((GeneralPath)shape).curveTo(23.395311, 24.87072, 23.020536, 24.87072, 22.71642, 24.695023);
((GeneralPath)shape).curveTo(22.412302, 24.519323, 22.2251, 24.194653, 22.225399, 23.843431);
((GeneralPath)shape).curveTo(22.2251, 23.49221, 22.412302, 23.16754, 22.71642, 22.99184);
((GeneralPath)shape).curveTo(23.020536, 22.816143, 23.395311, 22.816143, 23.699429, 22.99184);
((GeneralPath)shape).curveTo(24.003546, 23.16754, 24.190748, 23.49221, 24.190449, 23.843431);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_6);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_7 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7
paint = new LinearGradientPaint(new Point2D.Double(7.184844970703125, 31.056621551513672), new Point2D.Double(25.15223503112793, 50.77488708496094), new float[] {0.0f,1.0E-9f,1.0f}, new Color[] {new Color(150, 42, 183, 255),new Color(168, 26, 201, 255),new Color(220, 76, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.1618800163269043f, 0.0f, 0.0f, 0.9924970269203186f, -2.4307799339294434f, 0.26576098799705505f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.700394, 30.286934);
((GeneralPath)shape).curveTo(20.935404, 32.013584, 21.19623, 36.899853, 18.278337, 41.201286);
((GeneralPath)shape).curveTo(15.360479, 45.50525, 10.373849, 47.596474, 7.1373806, 45.87742);
((GeneralPath)shape).curveTo(3.9008825, 44.15077, 3.6415462, 39.267033, 6.5594354, 34.965595);
((GeneralPath)shape).curveTo(9.475807, 30.664165, 14.463925, 28.572945, 17.700394, 30.286934);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(15.845268, 33.02908);
((GeneralPath)shape).curveTo(14.408745, 32.26545, 11.33781, 33.5696, 9.378926, 36.463108);
((GeneralPath)shape).curveTo(7.4160166, 39.356613, 7.5560293, 42.376625, 8.991202, 43.13795);
((GeneralPath)shape).curveTo(10.426348, 43.90618, 13.499985, 42.59743, 15.458868, 39.703926);
((GeneralPath)shape).curveTo(17.42313, 36.81042, 17.281765, 33.79271, 15.845268, 33.02908);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(99, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(17.700394, 30.286934);
((GeneralPath)shape).curveTo(20.935404, 32.013584, 21.19623, 36.899853, 18.278337, 41.201286);
((GeneralPath)shape).curveTo(15.360479, 45.50525, 10.373849, 47.596474, 7.1373806, 45.87742);
((GeneralPath)shape).curveTo(3.9008825, 44.15077, 3.6415462, 39.267033, 6.5594354, 34.965595);
((GeneralPath)shape).curveTo(9.475807, 30.664165, 14.463925, 28.572945, 17.700394, 30.286934);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(15.845268, 33.02908);
((GeneralPath)shape).curveTo(14.408745, 32.26545, 11.33781, 33.5696, 9.378926, 36.463108);
((GeneralPath)shape).curveTo(7.4160166, 39.356613, 7.5560293, 42.376625, 8.991202, 43.13795);
((GeneralPath)shape).curveTo(10.426348, 43.90618, 13.499985, 42.59743, 15.458868, 39.703926);
((GeneralPath)shape).curveTo(17.42313, 36.81042, 17.281765, 33.79271, 15.845268, 33.02908);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_7);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_8 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_8
paint = new LinearGradientPaint(new Point2D.Double(13.825360298156738, 40.06875228881836), new Point2D.Double(7.670061111450195, 2.3262276649475098), new float[] {0.0f,1.0E-9f,1.0f}, new Color[] {new Color(150, 42, 183, 255),new Color(168, 26, 201, 255),new Color(220, 76, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.1618800163269043f, 0.0f, 0.0f, 0.9924970269203186f, -2.6669700145721436f, 0.06400520354509354f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(14.3255, 30.583288);
((GeneralPath)shape).curveTo(12.400369, 30.97051, 10.691041, 32.037308, 9.278593, 33.06453);
((GeneralPath)shape).curveTo(8.52683, 33.759434, 8.035029, 34.514454, 7.362945, 35.31874);
((GeneralPath)shape).curveTo(5.654618, 37.670807, 4.938707, 40.76217, 6.290107, 43.38841);
((GeneralPath)shape).curveTo(6.90956, 44.841515, 8.932742, 45.435852, 10.658323, 45.067543);
((GeneralPath)shape).curveTo(12.110236, 44.819077, 13.339639, 43.90647, 14.470735, 43.268642);
((GeneralPath)shape).curveTo(15.391637, 42.47786, 16.02475, 41.64213, 16.803625, 40.677364);
((GeneralPath)shape).curveTo(18.612986, 38.20296, 19.595537, 34.928688, 18.101604, 32.16508);
((GeneralPath)shape).curveTo(17.377897, 31.022951, 15.866963, 30.41829, 14.3255, 30.583288);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(14.797513, 31.54477);
((GeneralPath)shape).curveTo(16.814016, 31.795124, 18.154488, 33.577583, 17.92006, 35.266636);
((GeneralPath)shape).curveTo(17.940832, 37.553574, 16.774038, 39.710728, 15.196909, 41.500755);
((GeneralPath)shape).curveTo(13.779705, 42.902737, 11.848294, 44.229027, 9.532754, 44.137077);
((GeneralPath)shape).curveTo(8.1739, 44.13421, 7.100179, 43.224777, 6.716933, 42.176617);
((GeneralPath)shape).curveTo(6.1002936, 39.644695, 6.9116497, 36.91139, 8.683129, 34.83862);
((GeneralPath)shape).curveTo(10.041367, 33.315308, 11.877976, 31.95152, 14.150642, 31.596926);
((GeneralPath)shape).curveTo(14.366331, 31.581652, 14.581522, 31.554432, 14.797513, 31.54477);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_8);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_9 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_9
paint = new RadialGradientPaint(new Point2D.Double(34.37609100341797, 37.50008010864258), 8.388787f, new Point2D.Double(34.37609100341797, 37.50008010864258), new float[] {0.0f,1.0E-9f,1.0f}, new Color[] {new Color(150, 42, 183, 255),new Color(168, 26, 201, 255),new Color(220, 76, 255, 255)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.060379981994629f, 0.0f, -2.2995100021362305f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(30.331764, 30.286934);
((GeneralPath)shape).curveTo(27.096752, 32.013584, 26.83593, 36.899853, 29.75382, 41.201286);
((GeneralPath)shape).curveTo(32.67168, 45.50525, 37.65831, 47.596474, 40.894775, 45.87742);
((GeneralPath)shape).curveTo(44.131275, 44.15077, 44.39061, 39.267033, 41.47272, 34.965595);
((GeneralPath)shape).curveTo(38.55635, 30.664165, 33.568233, 28.572945, 30.331764, 30.286934);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(32.18689, 33.02908);
((GeneralPath)shape).curveTo(33.623413, 32.26545, 36.694347, 33.5696, 38.653233, 36.463108);
((GeneralPath)shape).curveTo(40.616142, 39.356613, 40.476128, 42.376625, 39.040955, 43.13795);
((GeneralPath)shape).curveTo(37.60581, 43.90618, 34.532173, 42.59743, 32.57329, 39.703926);
((GeneralPath)shape).curveTo(30.609028, 36.81042, 30.750393, 33.79271, 32.18689, 33.02908);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new Color(107, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(30.331764, 30.286934);
((GeneralPath)shape).curveTo(27.096752, 32.013584, 26.83593, 36.899853, 29.75382, 41.201286);
((GeneralPath)shape).curveTo(32.67168, 45.50525, 37.65831, 47.596474, 40.894775, 45.87742);
((GeneralPath)shape).curveTo(44.131275, 44.15077, 44.39061, 39.267033, 41.47272, 34.965595);
((GeneralPath)shape).curveTo(38.55635, 30.664165, 33.568233, 28.572945, 30.331764, 30.286934);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(32.18689, 33.02908);
((GeneralPath)shape).curveTo(33.623413, 32.26545, 36.694347, 33.5696, 38.653233, 36.463108);
((GeneralPath)shape).curveTo(40.616142, 39.356613, 40.476128, 42.376625, 39.040955, 43.13795);
((GeneralPath)shape).curveTo(37.60581, 43.90618, 34.532173, 42.59743, 32.57329, 39.703926);
((GeneralPath)shape).curveTo(30.609028, 36.81042, 30.750393, 33.79271, 32.18689, 33.02908);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_9);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_10 = g.getTransform();
g.transform(new AffineTransform(3.6244399547576904f, 0.0f, 0.0f, 3.6244399547576904f, -1053.1800537109375f, -16.847200393676758f));
// _0_0_10
paint = new LinearGradientPaint(new Point2D.Double(298.478515625, 13.599584579467773), new Point2D.Double(298.8694763183594, 13.802948951721191), new float[] {0.0f,1.0f}, new Color[] {new Color(181, 42, 223, 255),new Color(223, 42, 42, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(296.956, 12.3003);
((GeneralPath)shape).lineTo(297.6, 13.3848);
((GeneralPath)shape).lineTo(298.867, 14.7188);
((GeneralPath)shape).lineTo(299.268, 13.6729);
((GeneralPath)shape).lineTo(298.254, 11.9604);
((GeneralPath)shape).lineTo(296.956, 12.3003);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(298.478515625, 13.599584579467773), new Point2D.Double(298.8694763183594, 13.802948951721191), new float[] {0.0f,1.0f}, new Color[] {new Color(95, 12, 0, 255),new Color(154, 12, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(0.275905f,1,1,4.0f,null,0.0f);
shape = new GeneralPath();
((GeneralPath)shape).moveTo(296.956, 12.3003);
((GeneralPath)shape).lineTo(297.6, 13.3848);
((GeneralPath)shape).lineTo(298.867, 14.7188);
((GeneralPath)shape).lineTo(299.268, 13.6729);
((GeneralPath)shape).lineTo(298.254, 11.9604);
((GeneralPath)shape).lineTo(296.956, 12.3003);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(defaultTransform__0_0_10);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_11 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_11
paint = new LinearGradientPaint(new Point2D.Double(296.7619934082031, 12.012225151062012), new Point2D.Double(297.7982177734375, 10.946586608886719), new float[] {0.0f,1.0f}, new Color[] {new Color(181, 42, 223, 255),new Color(223, 42, 42, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(3.6244399547576904f, 0.0f, 0.0f, 3.6244399547576904f, -1053.1800537109375f, -16.847200393676758f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(26.15625, 27.9375);
((GeneralPath)shape).curveTo(25.729502, 28.13632, 25.139437, 28.13898, 24.8125, 28.4375);
((GeneralPath)shape).curveTo(25.76252, 29.838888, 26.702412, 31.352161, 27.66338, 32.650078);
((GeneralPath)shape).curveTo(28.331932, 33.40462, 29.019194, 34.150303, 29.78125, 34.8125);
((GeneralPath)shape).curveTo(30.516527, 33.421078, 29.91641, 31.751291, 28.96875, 30.625);
((GeneralPath)shape).curveTo(28.366215, 29.725307, 28.138927, 28.512037, 27.125, 28.03125);
((GeneralPath)shape).curveTo(26.820951, 27.91284, 26.474384, 27.853373, 26.15625, 27.9375);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_11);
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
AffineTransform defaultTransform__0_0_12 = g.getTransform();
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_12
paint = new LinearGradientPaint(new Point2D.Double(39.61994171142578, 44.540931701660156), new Point2D.Double(-3.532515048980713, -11.889041900634766), new float[] {0.0f,1.0f}, new Color[] {new Color(155, 29, 180, 255),new Color(164, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.1618800163269043f, 0.0f, 0.0f, 0.9924970269203186f, -5.112110137939453f, 0.06400520354509354f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(32.280087, 30.449093);
((GeneralPath)shape).curveTo(30.759703, 30.678844, 29.385141, 31.534748, 29.039639, 32.837055);
((GeneralPath)shape).curveTo(27.908495, 35.23251, 28.824762, 37.95057, 30.319418, 40.063908);
((GeneralPath)shape).curveTo(31.421345, 41.40911, 32.259487, 42.99382, 33.959, 43.83788);
((GeneralPath)shape).curveTo(35.429653, 44.7615, 37.300144, 45.72845, 39.17664, 45.138767);
((GeneralPath)shape).curveTo(40.689957, 44.70532, 41.547314, 43.4582, 41.85681, 42.166912);
((GeneralPath)shape).curveTo(42.461243, 39.85688, 41.561115, 37.49095, 40.149845, 35.53043);
((GeneralPath)shape).curveTo(39.491173, 34.616722, 38.81686, 33.64722, 38.03653, 32.83578);
((GeneralPath)shape).curveTo(36.84197, 31.93233, 35.398613, 31.184254, 33.94769, 30.603432);
((GeneralPath)shape).curveTo(33.41359, 30.49302, 32.832462, 30.37069, 32.280087, 30.449093);
((GeneralPath)shape).closePath();
((GeneralPath)shape).moveTo(32.715794, 31.658699);
((GeneralPath)shape).curveTo(34.473095, 31.591923, 35.950306, 32.398155, 37.092163, 33.427666);
((GeneralPath)shape).curveTo(38.12446, 34.396793, 39.113815, 35.23287, 39.754673, 36.42654);
((GeneralPath)shape).curveTo(40.831856, 38.24711, 41.142532, 40.4065, 40.594776, 42.39007);
((GeneralPath)shape).curveTo(40.0664, 43.714584, 38.36862, 44.36211, 36.803658, 44.00652);
((GeneralPath)shape).curveTo(34.821777, 43.77769, 33.586315, 42.335503, 32.277092, 41.19816);
((GeneralPath)shape).curveTo(30.771343, 39.76677, 29.83647, 37.719532, 29.76651, 35.715782);
((GeneralPath)shape).curveTo(29.780622, 34.698112, 29.740042, 33.53736, 30.464653, 32.682213);
((GeneralPath)shape).curveTo(30.876926, 32.13906, 31.84466, 31.627886, 32.715794, 31.658699);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_12);
g.setComposite(AlphaComposite.getInstance(3, 0.267045f * origAlpha));
AffineTransform defaultTransform__0_0_13 = g.getTransform();
g.transform(new AffineTransform(1.2560499906539917f, 0.0f, 0.0f, 0.8191490173339844f, -7.199389934539795f, 9.09041976928711f));
// _0_0_13
paint = new RadialGradientPaint(new Point2D.Double(25.1875, 41.625), 18.0625f, new Point2D.Double(25.1875, 41.625), new float[] {0.0f,1.0f}, new Color[] {new Color(0, 0, 0, 255),new Color(0, 0, 0, 0)}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.3252600133419037f, 2.0296300403131105E-16f, 28.08609962463379f));
shape = new GeneralPath();
((GeneralPath)shape).moveTo(43.25, 41.625);
((GeneralPath)shape).curveTo(43.25, 44.869675, 35.163143, 47.5, 25.1875, 47.5);
((GeneralPath)shape).curveTo(15.211857, 47.5, 7.125, 44.869675, 7.125, 41.625);
((GeneralPath)shape).curveTo(7.125, 38.380325, 15.211857, 35.75, 25.1875, 35.75);
((GeneralPath)shape).curveTo(35.163143, 35.75, 43.25, 38.380325, 43.25, 41.625);
((GeneralPath)shape).closePath();
g.setPaint(paint);
g.fill(shape);
g.setTransform(defaultTransform__0_0_13);
g.setTransform(defaultTransform__0_0);
g.setTransform(defaultTransform__0);
g.setTransform(defaultTransform_);

	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public  int getOrigX() {
        return 2;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public  int getOrigY() {
        return 2;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     * 
     * @return The width of the bounding box of the original SVG image.
     */
    public  int getOrigWidth() {
        return 46;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     * 
     * @return The height of the bounding box of the original SVG image.
     */
    public  int getOrigHeight() {
        return 47;
    }
}

