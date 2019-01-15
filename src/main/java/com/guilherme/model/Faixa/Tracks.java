/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guilherme.model.Faixa;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guilh
 */
class Tracks {
    private List<Item> items;
    
    public Tracks(){
        items = new ArrayList();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    
}
