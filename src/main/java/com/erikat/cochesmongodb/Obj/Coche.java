package com.erikat.cochesmongodb.Obj;

import java.util.Objects;

public class Coche {
    String matricula;
    String marca;
    String modelo;
    String tipo;

    public Coche(String matricula, String marca, String modelo, String tipo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() { //Muestra el objeto en forma de String. Necesario para la ListView
        return "Matrícula: " + this.matricula + "; Marca: " + this.marca + "; Modelo: " + this.modelo + "; Tipo: " + this.tipo;
    }

    @Override
    public boolean equals(Object o) { //Compara todos los atributos de dos objetos de tipo Coche
        if (this == o) return true; //Devuelve true si son literalmente el mismo objeto (Si comparten la dirección de memoria)
        if (o == null || getClass() != o.getClass()) return false; //Devuelve false si no son de la misma clase (Al comparar con un Object)
        Coche coche = (Coche) o; //Se convierte en tipo Coche
        return Objects.equals(matricula, coche.matricula) && Objects.equals(marca, coche.marca) && Objects.equals(modelo, coche.modelo) && Objects.equals(tipo, coche.tipo);
        //Devuelve true si y solo si todos sus atributos son iguales
    }
}
