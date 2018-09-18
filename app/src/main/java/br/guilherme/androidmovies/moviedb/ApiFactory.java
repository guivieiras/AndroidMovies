package br.guilherme.androidmovies.moviedb;

import info.movito.themoviedbapi.TmdbApi;

public class ApiFactory
{
    public static String key = "450bddb2c5f31a1d05476a13bb7ba876";

    public static TmdbApi newInstance() {return new TmdbApi(key);}
}
