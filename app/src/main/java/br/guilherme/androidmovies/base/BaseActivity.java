package br.guilherme.androidmovies.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import br.guilherme.androidmovies.R;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity
{

    private ProgressDialog progressDialog;
    private Toast toast;
    protected P presenter;

    /** Executa a Activity da classe indicada e finaliza a Activity atual */
    protected void startAndClose(Class<? extends Activity> clazz){
        start(clazz);
        finish();
    }

    /** Executa a Activity da classe indicada */
    protected void start(Class<? extends Activity> clazz){
        Intent intent = new Intent(getApplicationContext(), clazz);
        startActivity(intent);
    }

    /** Exibe uma caixa de dialogo, a qual ira finalizar a Activity após ser fechada */
    protected void showDialogMessageAndFinish(int messageId, Integer titleId)
    {
        showDialogMessage(messageId, titleId, new DialogInterface.OnClickListener()
                          {
                              public void onClick(DialogInterface dialog, int id)
                              {
                                  dialog.dismiss();
                                  finish();
                              }
                          }
        );
    }

    /** Exibe uma caixa de dialogo com uma mensagem e um titulo, o qual é opcional */
    protected void showDialogMessage(int messageId, Integer titleId){
        showDialogMessage(messageId, titleId, new DialogInterface.OnClickListener()
                          {
                              public void onClick(DialogInterface dialog, int id)
                              {
                                  dialog.dismiss();
                              }
                          }
        );
    }

    /** Exibe uma caixa de dialogo com uma mensagem e um titulo, o qual é opcional, mais a ação do botão de Ok */
    public void showDialogMessage(int messageId, Integer titleId, DialogInterface.OnClickListener ok)
    {
        if (isFinishing())
            return;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActivity.this);

        if (titleId != null) alertDialogBuilder.setTitle(titleId);

        alertDialogBuilder
                .setMessage(messageId)
                .setCancelable(false)
                .setPositiveButton("Ok", ok);

        alertDialogBuilder.create().show();
    }

    /** Exibe uma caixa de dialogo de sim e não com uma mensagem e um titulo, o qual é opcional, mais a ação do botão sim e do botão não */
    protected void showYesNoDialog(int messageId, Integer titleId, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no)
    {
        if (isFinishing())
            return;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (titleId != null) alertDialogBuilder.setTitle(titleId);

        alertDialogBuilder.setMessage(messageId)
                .setCancelable(false)
               // .setIcon(R.drawable.ic_stat_aviso)
                .setNegativeButton("Não", no)
                .setPositiveButton("Sim", yes);

        alertDialogBuilder.create().show();
    }


    /** Exibe dialogo de Loading */
    public void showProgress()
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /** Esconde diálogo de Loading */
    public void hideProgress()
    {
        if (isFinishing())
            return;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /** Retorna true caso tenha conexão com a internet */
    public boolean isInternetAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /** Exibe toast com mensagem e tempo determinado */
    protected void showToast(String msg, int time)
    {
        if (toast == null)
        {
            toast = Toast.makeText(this, msg, time);
            toast.show();
        }
        else
        {
            toast.setText(msg);
            toast.show();
        }
    }

    /** Exibe toast com tempo padrão */
    public void showToast(final String msg)
    {
        showToast(msg, Toast.LENGTH_LONG);
    }

    /** Exibe dialog com mensagem de erro TODO title botar algo com erro, icone também*/
    public void showError(int idMessage)
    {
        showDialogMessage(idMessage, null);
    }

    /** Todo não sei o que isso faz */
    protected void hideSoftKeyBoard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // verify if the soft keyboard is open
        if (imm.isAcceptingText())
        {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /** Inicializa o presenter ao dar resume na Activity */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /** Finaliza o presenter ao finalizar a Activity */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Esconde dialog caso ainda esteja exibindo
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

}