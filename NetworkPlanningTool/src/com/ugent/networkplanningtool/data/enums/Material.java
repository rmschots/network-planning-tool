package com.ugent.networkplanningtool.data.enums;

import android.graphics.Color;

import com.ugent.networkplanningtool.MainActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The material of a Wall
 */
public enum Material {
    BRICK(MainActivity.getInstance().getResources().getString(R.string.brickText), Color.rgb(221, 0, 0)),
    LAYERED_DRYWALL(MainActivity.getInstance().getResources().getString(R.string.layeredDrywallText), Color.rgb(176, 129, 64)),
    CONCRETE(MainActivity.getInstance().getResources().getString(R.string.concreteText), Color.rgb(136, 136, 136)),
    WOOD(MainActivity.getInstance().getResources().getString(R.string.woodText), Color.rgb(252, 206, 140)),
    GLASS(MainActivity.getInstance().getResources().getString(R.string.glassText), Color.rgb(149, 165, 236)),
    METAL(MainActivity.getInstance().getResources().getString(R.string.metalText), Color.rgb(102, 102, 153)),
    ZERO_DB(MainActivity.getInstance().getResources().getString(R.string.zeroDbText), Color.rgb(221, 221, 221));

    private static Material[] doorMaterials = new Material[]{GLASS, WOOD};
    private static Material[] wallMaterials = new Material[]{BRICK, LAYERED_DRYWALL, CONCRETE, WOOD, GLASS, METAL, ZERO_DB};
    private static Material[] windowMaterials = new Material[]{GLASS};

    private String text;
    private int color;

    private static Map<String, Material> textToMaterialMapping;

    private Material(String text, int color) {
        this.text = text;
        this.color = color;

    }

    private static void initMapping() {
        textToMaterialMapping = new HashMap<String, Material>();
        for (Material s : values()) {
            textToMaterialMapping.put(s.getText(), s);
        }
    }

    public static Material getMaterialByText(String text) {
        if (textToMaterialMapping == null) {
            initMapping();
        }
        return textToMaterialMapping.get(text);
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public static Material[] getMaterialsForWallType(WallType wt) {
        switch (wt) {
            case WALL:
                return wallMaterials;
            case DOOR:
                return doorMaterials;
            case WINDOW:
                return windowMaterials;
            default:
                return new Material[0];
        }
    }

}
