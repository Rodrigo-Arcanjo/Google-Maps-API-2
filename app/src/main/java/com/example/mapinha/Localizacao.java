package com.example.mapinha;

import java.io.Serializable;

public class Localizacao implements Serializable  {

    private String ACI_Logradouro;
    private String ACI_Numero;
    private String ACI_Bairro;
    private String ACI_Referencia;
    private double ACI_Latitude;
    private double ACI_Longitude;
    private String ACI_Data;
    private String ACI_Hora;
    private String dataFormatada;
    private String ACI_Local_Img;

    public String getACI_Logradouro() {
        return ACI_Logradouro;
    }

    public void setACI_Logradouro(String ACI_Logradouro) {
        this.ACI_Logradouro = ACI_Logradouro;
    }

    public String getACI_Numero() {
        return ACI_Numero;
    }

    public void setACI_Numero(String ACI_Numero) {
        this.ACI_Numero = ACI_Numero;
    }

    public String getACI_Bairro() {
        return ACI_Bairro;
    }

    public void setACI_Bairro(String ACI_Bairro) {
        this.ACI_Bairro = ACI_Bairro;
    }

    public String getACI_Referencia() {
        return ACI_Referencia;
    }

    public void setACI_Referencia(String ACI_Referencia) {
        this.ACI_Referencia = ACI_Referencia;
    }

    public double getACI_Latitude() {
        return ACI_Latitude;
    }

    public void setACI_Latitude(double ACI_Latitude) {
        this.ACI_Latitude = ACI_Latitude;
    }

    public double getACI_Longitude() {
        return ACI_Longitude;
    }

    public void setACI_Longitude(double ACI_Longitude) {
        this.ACI_Longitude = ACI_Longitude;
    }

    public String getACI_Data() {
        return ACI_Data;
    }

    public void setACI_Data(String ACI_Data) {
        this.ACI_Data = ACI_Data;
    }

    public String getACI_Hora() {
        return ACI_Hora;
    }

    public void setACI_Hora(String ACI_Hora) {
        this.ACI_Hora = ACI_Hora;
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }

    public String getACI_Local_Img() {
        return ACI_Local_Img;
    }

    public void setACI_Local_Img(String ACI_Local_Img) {
        this.ACI_Local_Img = ACI_Local_Img;
    }
}
