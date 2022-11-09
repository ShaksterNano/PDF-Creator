package io.github.shaksternano.pdfcreator;

public class Counter {

    private float value;

    public void add(float value) {
        this.value += value;
    }

    public void set(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
