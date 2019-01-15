/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guilherme.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guilherme.model.Faixa.Faixa;
import com.guilherme.model.Spotify;
import com.guilherme.model.Tempo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author guilh
 */
@RestController
public class ProcuraPlaylistResource {

    String app_key_Spotify = "BQBYwwUZl3DZNmHaUbBnyhrKpd7Lq4MMmWsPWVxFebYr-CYj7W_qG_cHIGK02lzhJroOnwp-dHB84IPg0hy5oAfpJ5cMeaTw1lON49Jt18m-QwXprQ4gQ2piARhamRdu1TxQldH0TbeKdTvlQ2Oy0b0WkDw4XEyXlv8AZYs";
    String app_key_OpenWeather = "087055547f0ebc1a6a2133177ff2487f";
    
    private String baseUrl = "http://api.openweathermap.org/data/2.5/";

    @RequestMapping(value = "/buscarViaCidade/{cidade}/{siglaPais}", method = RequestMethod.GET)
    public ResponseEntity<Faixa> buscarViaCidade(@PathVariable("cidade") String cidade, @PathVariable("siglaPais") String siglaPais) {
        String subUrl = "weather?q=" + cidade + "," + siglaPais;
        Tempo tempo = buscarTemperatura(subUrl);
        Spotify spotify = buscarPlaylist(tempo);
        Faixa faixa = buscarFaixa(spotify);
        if (spotify == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Faixa>(faixa, HttpStatus.OK);
    }

    @RequestMapping(value = "/buscarViaCoordenadas/{lat}/{lon}", method = RequestMethod.GET)
    public ResponseEntity<Faixa> buscarViaCoordenadas(@PathVariable("lat") String latitude, @PathVariable("lon") String longitude) {
        String subUrl = "weather?lat=" + latitude + "&lon=" + longitude;
        Tempo tempo = buscarTemperatura(subUrl);
        Spotify spotify = buscarPlaylist(tempo);
        Faixa faixa = buscarFaixa(spotify);
        if (spotify == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Faixa>(faixa, HttpStatus.OK);
    }

    private Tempo buscarTemperatura(String subUrl) {

        Tempo tempo = null;
        StringBuilder result = new StringBuilder();
        String charEncoding = "UTF-8";
        try {
            java.net.URL url = new java.net.URL(baseUrl + subUrl + "&appid=" + app_key_OpenWeather + "&units=metric");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charEncoding));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            String retorno = result.toString();
            Gson gson = new GsonBuilder().create();
            tempo = gson.fromJson(retorno, Tempo.class);
        } catch (Exception ex) {
            Logger.getLogger(ProcuraPlaylistResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tempo;
    }

    public Spotify buscarPlaylist(Tempo tempo) {
        Spotify spotify = null;

        StringBuilder result = new StringBuilder();
        String charEncoding = "UTF-8";
        String estilo = null;

        if (tempo.pegaTempo() > 30) {
            estilo = "party";
        } else if (tempo.pegaTempo() >= 15 && tempo.pegaTempo() <= 30) {
            estilo = "pop";
        } else if (tempo.pegaTempo() >= 10 && tempo.pegaTempo() <= 14) {
            estilo = "rock";
        } else if (tempo.pegaTempo() < 10) {
            estilo = "musica%20classica";
        }

        try {
            String urlPedido = "https://api.spotify.com/v1/search?q=" + estilo + "&type=playlist";
            java.net.URL url = new java.net.URL(urlPedido);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + app_key_Spotify);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charEncoding));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            String retorno = result.toString();
            Gson gson = new GsonBuilder().create();
            spotify = gson.fromJson(retorno, Spotify.class);

        } catch (Exception ex) {
            Logger.getLogger(ProcuraPlaylistResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return spotify;
    }

    public Faixa buscarFaixa(Spotify spotify) {
        Faixa faixa = null;

        StringBuilder result = new StringBuilder();
        String charEncoding = "UTF-8";

        try {
            String urlPedido = "https://api.spotify.com/v1/playlists/" + spotify.getPlaylists().getItems().get(1).getId();
            java.net.URL url = new java.net.URL(urlPedido);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer " + app_key_Spotify);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charEncoding));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            String retorno = result.toString();
            Gson gson = new GsonBuilder().create();
            faixa = gson.fromJson(retorno, Faixa.class);

        } catch (Exception ex) {
            Logger.getLogger(ProcuraPlaylistResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        return faixa;
    }

    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        String charEncoding = "UTF-8";
        java.net.URL url = new java.net.URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), charEncoding));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

}
