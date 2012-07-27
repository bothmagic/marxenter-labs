/*
 * $Id: Orientation.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.image;

/**
 * Defines an Orientation tag for an EXIF image. Rotations returned through
 * these enum types are compatible with the AffineTransform object which means
 * that they act in an Anti-Clockwise direction.
 */
public enum Orientation {
    /**
     * The standard orientation.
     */
    TOP_LEFT(1, 0, false),
    /**
     * The image is flipped so that top-left becomes top-right.
     */
    TOP_RIGHT(2, 0, true),
    /**
     * The image is rotated 180 degrees.
     */
    BOTTOM_RIGHT(3, Math.PI, false),
    /**
     * The image is flipped along its vertical axis then rotated 180 degrees.
     */
    BOTTOM_LEFT(4, Math.PI / 2d, true),
    /**
     * The image is flipped along its vertical axis then rotated 270 degrees.
     */
    LEFT_TOP(5, Math.PI / 2d, true),
    /**
     * The image is rotated 270 degrees.
     */
    RIGHT_TOP(6, Math.PI / 2d, false),
    /**
     * The image is flipped along its vertical axis then rotated 90 degrees.
     */
    RIGHT_BOTTOM(7, 3 * Math.PI / 2d, true),
    /**
     * The image is rotated 90 degrees.
     */
    LEFT_BOTTOM(8, 3 * Math.PI / 2d, false);





    private final int exifCode;
    private final boolean mirrored;
    private final double rotation;
    private Orientation(int exifCode, double rotation, boolean mirrored) {
        this.exifCode = exifCode;
        this.rotation = rotation;
        this.mirrored = mirrored;
    }





    /**
     * Returns true if the image is mirrored.
     *
     * @return boolean
     */
    public boolean isMirrored() {
        return mirrored;
    }





    /**
     * Returns the rotation of the image after mirroring.
     *
     * @return double
     */
    public double getRotation() {
        return rotation;
    }





    /**
     * Gets the code that is defined by the EXIF specification for this
     * Orientation.
     *
     * @return int
     */
    public int getExifCode() {
        return exifCode;
    }





    /**
     * Get the Image IO IIOMetadata value for the ImageOrientation node. This
     * will be one of:
     * <ul>
     * <li>{@code "Normal"}</li>
     * <li>{@code "Rotate90"}</li>
     * <li>{@code "Rotate180"}</li>
     * <li>{@code "Rotate270"}</li>
     * <li>{@code "FlipH"}</li>
     * <li>{@code "FlipV"}</li>
     * <li>{@code "FlipHRotate90"}</li>
     * <li>{@code "FlipVRotate90"}</li>
     * </ul>
     * With all rotation in the counter-clockwise direction.
     *
     * @return One of the above values.
     */
    public String getIIOImageOrientation() {
        String result;

        switch (this) {
            case TOP_LEFT:
                result = "Normal";
                break;
            case LEFT_BOTTOM:
                result = "Rotate90";
                break;
            case BOTTOM_RIGHT:
                result = "Rotate180";
                break;
            case RIGHT_TOP:
                result = "Rotation270";
                break;
            case TOP_RIGHT:
                result = "FlipH";
                break;
            case BOTTOM_LEFT:
                result = "FlipV";
                break;
            case LEFT_TOP:
                result = "FlipHRotate90";
                break;
            case RIGHT_BOTTOM:
                result = "FlipVRotate90";
                break;
            default:
                result = "Normal";
                break;
        }

        return result;
    }





    /**
     * Gets the Orientation for the given EXIF code.
     *
     * @param exifCode int
     * @return Orientation
     */
    public static Orientation valueOf(int exifCode) {
        if (exifCode < 1 || exifCode > 8) {
            throw new IllegalArgumentException("EXIF codes only come in the range [1,8] : " + exifCode);
        }
        return values()[exifCode - 1];
    }
}
