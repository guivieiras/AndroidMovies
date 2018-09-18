package br.guilherme.androidmovies.base;

import android.preference.PreferenceManager;

import br.guilherme.androidmovies.data.DataManager;


public abstract class BasePresenter<V extends BaseActivity> {

    /** Classe auxiliar para manejamento de configurações */
    public DataManager dataManager;

    protected V view;

    public BasePresenter(V view)
    {
        this.view = view;
        this.dataManager = new DataManager(PreferenceManager.getDefaultSharedPreferences(view));
    }

}