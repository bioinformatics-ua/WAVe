/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.bioinformatics.wave.domain;

import java.io.Serializable;

/**
 *
 * @author pedrolopes
 */
public class Item implements Serializable{

    private String value;
    private String gene;
    private int node;
    private Gene g;

    public Gene getG() {
        return g;
    }

    public void setG(Gene g) {
        this.g = g;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Item(String value, int node, String gene) {
        this.value = value;
        this.node = node;
        this.gene = gene;
    }

    public Item(String value, int node, String gene, Gene g) {
        this.value = value;
        this.gene = gene;
        this.node = node;
        this.g = g;
    }
}
