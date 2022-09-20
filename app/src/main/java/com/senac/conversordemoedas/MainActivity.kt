package com.senac.conversordemoedas


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Find view by id! pegar os elementos da interface
        val converter = findViewById<Button>(R.id.btnconvert)
        //atribuir função ao botão
        converter.setOnClickListener {
            convert()
        }


    }
//Escrever uma função completa.
@SuppressLint("SetTextI18n")
private fun convert (){
//referenciar o grupo seletor de moeda
val moeda = findViewById<RadioGroup>(R.id.radialGroup)
    //referenciar o texto que mostrará o resultado
val result = findViewById<TextView>(R.id.resultado)
//buscar id do botão selecionado
val checked = when(moeda.checkedRadioButtonId){
R.id.radioUsd -> "USD"
    R.id.radioEur -> "EUR"
    else -> "BTC"
}
//pegar valor digitado pelo usuário
    val enterValue = findViewById<EditText>(R.id.valor)
    val value = enterValue.text.toString()
//checar se foi digitado algum valor no campo, caso contrãrio nada será executado
if (value.isEmpty())
    return

    //Abrir atividade em segundo plano
    Thread {
    //indicar o site onde faremos o requerimento dos dados
     val site = URL("https://free.currconv.com/api/v7/convert?q=${checked}_BRL&compact=ultra&apiKey=da41c51f18795c636424")
        val conn = site.openConnection() as HttpsURLConnection

        //pegar dados da conexão e tratar possíveis erros
        try {
            //fazer leitura dos dados e transformar em texto
           val data = conn.inputStream.bufferedReader().readText()


            //formatar objeto Json
            val obj = JSONObject(data)

            //realizar operações e mostrar na tela
            runOnUiThread {
                //pegar dados do JSON e transformar em numero real para calcular
                val res = obj.getDouble("${checked}_BRL")

                //fazer calculo e mostrar resultado
                result.text = "R$ ${"%,.2f".format(value.toDouble() * res)}"
                result.visibility = View.VISIBLE

            }



        } finally {
            //finalizar desconectando da internet
            conn.disconnect()

        }


    }.start()

}

}