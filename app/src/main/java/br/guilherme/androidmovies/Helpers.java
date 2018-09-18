package br.guilherme.androidmovies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers
{
    public static String getFormatedDate(String date){

        try
        {
            Date sdf = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return new SimpleDateFormat("dd/MM/yyyy").format(sdf);

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;

    }
}
