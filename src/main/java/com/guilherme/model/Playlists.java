/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guilherme.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guilh
 */
public class Playlists {
   // private  String href;
    
    private List<Item> items;

    public Playlists() {
        items = new ArrayList();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

  /*  public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }*/
    
}
