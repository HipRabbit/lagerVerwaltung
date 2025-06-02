package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Repräsentiert Filterkriterien für die Suche im Lagerverwaltungssystem, wie Zeitraum, Mindestbetrag und Produkt-ID.
 * 
 * @author Lennart Höpfner
 */
public class FilterCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date startDate;
    private Date endDate;
    private Double minAmount;
    private Integer productId;

    /**
     * Konstruktor mit allen Attributen.
     * 
     * @param startDate das Startdatum des Zeitraums
     * @param endDate das Enddatum des Zeitraums
     * @param minAmount der Mindestbetrag
     * @param productId die Produkt-ID
     */
    public FilterCriteria(Date startDate, Date endDate, Double minAmount, Integer productId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.productId = productId;
    }

    /**
     * Gibt das Startdatum des Zeitraums zurück.
     * 
     * @return das Startdatum
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Gibt das Enddatum des Zeitraums zurück.
     * 
     * @return das Enddatum
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Gibt den Mindestbetrag zurück.
     * 
     * @return der Mindestbetrag
     */
    public Double getMinAmount() {
        return minAmount;
    }

    /**
     * Gibt die Produkt-ID zurück.
     * 
     * @return die Produkt-ID
     */
    public Integer getProductId() {
        return productId;
    }
}