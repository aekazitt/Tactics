package io.pomelo.tactics.mechanics;


public abstract class Anim extends Element {
    /* Logging Assistants */
    protected boolean expired = false;
    protected int imageCycle;

    public boolean isExpired() {
        return expired;
    }

    /*
     * Abstract Functions
     */
    abstract public void expires();

    abstract public void updateImage();
}
