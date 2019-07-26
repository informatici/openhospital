/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.isf.operation.model;

import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.isf.accounting.model.Bill;
import org.isf.admission.model.Admission;
import org.isf.opd.model.Opd;

/**
 *
 * @author xavier
 */
@Entity
@Table(name = "OPERATIONROW")
public class OperationRow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "OPER_ID_A")
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "OPER_ID")
    private Operation operation;

    @NotNull
    @Column(name = "OPER_PRESCRIBER")
    private String prescriber;

    @NotNull
    @Column(name = "OPER_RESULT")
    private String opResult;

    @NotNull
    @Column(name = "OPER_OPDATE")
    private GregorianCalendar opDate;

    @Column(name = "OPER_REMARKS")
    private String remarks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "OPER_ADMISSION_ID")
    private Admission admission;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "OPER_OPD_ID")
    private Opd opd;

    @ManyToOne
    @JoinColumn(name = "OPER_BILL_ID")
    private Bill bill;

    @NotNull
    @Column(name = "OPER_TRANS_UNIT")
    private Float transUnit;
    
    @Transient
    private volatile int hashCode = 0;

    public OperationRow() {
	super();
    }
    
    public OperationRow(Operation operation, 
            String prescriber, 
            String opResult, 
            GregorianCalendar opDate, 
            String remarks, 
            Admission admission, 
            Opd opd, 
            Bill bill, 
            Float transUnit) {
        super();
        this.operation = operation;
        this.prescriber = prescriber;
        this.opResult = opResult;
        this.opDate = opDate;
        this.remarks = remarks;
        this.admission = admission;
        this.opd = opd;
        this.bill = bill;
        this.transUnit = transUnit;
    }

    public OperationRow(int id, 
            Operation operation, 
            String prescriber, 
            String opResult, 
            GregorianCalendar opDate, 
            String remarks, 
            Admission admission, 
            Opd opd, 
            Bill bill, 
            Float transUnit) {
        this(operation, prescriber, opResult, opDate, remarks, admission, opd, bill, transUnit);
        this.id = id;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public String getOpResult() {
        return opResult;
    }

    public void setOpResult(String opResult) {
        this.opResult = opResult;
    }

    public GregorianCalendar getOpDate() {
        return opDate;
    }

    public void setOpDate(GregorianCalendar opDate) {
        this.opDate = opDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
    }

    public Opd getOpd() {
        return opd;
    }

    public void setOpd(Opd opd) {
        this.opd = opd;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Float getTransUnit() {
        return transUnit;
    }

    public void setTransUnit(Float transUnit) {
        this.transUnit = transUnit;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }

        if (!(anObject instanceof OperationRow)) {
            return false;
        }

        OperationRow operationRow = (OperationRow) anObject;
        return (this.getOperation().equals(operationRow.getOperation())
                && this.getPrescriber().equals(operationRow.getPrescriber()))
                && operationRow.getTransUnit().equals(this.getTransUnit())
                && this.getAdmission().equals(operationRow.getAdmission())
                && this.getOpd().equals(operationRow.getOpd());
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + ((operation == null) ? 0 : operation.hashCode());
            c = m * c + ((prescriber == null) ? 0 : prescriber.hashCode());
            c = m * c + ((admission == null) ? 0 : admission.hashCode());
            c = m * c + ((opd == null) ? 0 : opd.hashCode());
            c = m * c + ((transUnit == null) ? 0 : transUnit.intValue());

            this.hashCode = c;
        }

        return this.hashCode;
    }

    public String toString() {
        return this.operation.getDescription() + " " + this.admission.getUserID();
    }
}
