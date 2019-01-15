/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guilherme.model;

/**
 *
 * @author guilh
 */
public class Tempo {
    private Main main;
    
    public Tempo(){
        main = new Main();
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
    
    public double pegaTempo(){
        return main.getTemp();
        
    }

    
    
}
