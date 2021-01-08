package com.jpl.games.model;


import java.io.Serializable;

public class dataModel implements Serializable {
    private static final long serialVersionUID = 1L;
    public double Tx,Ty,ry,rx;
    public String change = "A";

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public double getTx() {
        return Tx;
    }

    public void setTx(double tx) {
        Tx = tx;
    }

    public double getTy() {
        return Ty;
    }

    public void setTy(double ty) {
        Ty = ty;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double xFlip = -1.0;
    public double yFlip = 1.0;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int order;

    public dataModel cloneThis() {
        dataModel returnDatamodel = new dataModel();
        returnDatamodel.setDragZ(this.dragZ);
        returnDatamodel.setzZoom(this.zZoom);
        returnDatamodel.setzScroll(this.zScroll);
        returnDatamodel.setRx(this.rx);
        returnDatamodel.setRy(this.ry);
        returnDatamodel.setTy(this.Ty);
        returnDatamodel.setTx(this.Tx);
        returnDatamodel.setOrder(this.order);
        returnDatamodel.setChange(this.change);
        return returnDatamodel;
    }

    @Override
    public String toString() {
        return "dataModel{" +
                "Tx=" + Tx +
                ", Ty=" + Ty +
                ", ry=" + ry +
                ", rx=" + rx +
                ", change='" + change + '\'' +
                ", xFlip=" + xFlip +
                ", yFlip=" + yFlip +
                ", order=" + order +
                ", dragZ=" + dragZ +
                ", zScroll=" + zScroll +
                ", zZoom=" + zZoom +
                '}';
    }

    public double getDragZ() {
        return dragZ;
    }

    public void setDragZ(double dragZ) {
        this.dragZ = dragZ;
    }

    public double dragZ;

    double zScroll;



    public double getzScroll() {
        return zScroll;
    }

    public void setzScroll(double zScroll) {
        this.zScroll = zScroll;
    }

    public double getzZoom() {
        return zZoom;
    }

    public void setzZoom(double zZoom) {
        this.zZoom = zZoom;
    }

    double zZoom;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
