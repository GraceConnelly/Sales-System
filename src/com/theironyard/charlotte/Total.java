package com.theironyard.charlotte;

/**
 * Created by graceconnelly on 1/17/17.
 */
public class Total {
    Double subtotal;
    Double shipping;
    Double subPreTax;
    Double tax;
    Double grandTotal;

    public Total(Double subtotal, Double shipping, Double subPreTax, Double tax, Double grandTotal) {
        this.subtotal = subtotal;
        this.shipping = shipping;
        this.subPreTax = subPreTax;
        this.tax = tax;
        this.grandTotal = grandTotal;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getShipping() {
        return shipping;
    }

    public void setShipping(Double shipping) {
        this.shipping = shipping;
    }

    public Double getSubPreTax() {
        return subPreTax;
    }

    public void setSubPreTax(Double subPreTax) {
        this.subPreTax = subPreTax;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }
}
