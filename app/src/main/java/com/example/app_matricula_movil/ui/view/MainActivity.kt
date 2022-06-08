package com.example.app_matricula_movil.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.app_matricula_movil.databinding.ActivityMainBinding
import com.example.app_matricula_movil.ui.viewModel.UsuarioLoggeadoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    /*
     * View binding, para acceder a las vistas de este componente más fácil.
     */
    private lateinit var binding: ActivityMainBinding

    /*
     *  View Model, útil para el login asíncrono porque permite observar los datos que se
     *  registren en el archivo de ViewModel en ui.viewModel. El view model es una dependencia
     *  que se agrega en el build.gradle del app.
     */
    private val usuarioLoggeadoViewModel: UsuarioLoggeadoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Darle valor al view binding, se infla con componentes que son del view binding,
         * luego se setea el contenido con la raíz que es toda la vista.
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
         * Método custom para setear el token en vacío por el funcionamiento del cliente en core.Client.
         */
        usuarioLoggeadoViewModel.onCreate()

        val intent = Intent(this@MainActivity, NavdrawActivity::class.java)

        /*
         * Así es como se observan los datos del view model. En este caso vemos el estado de loggeo,
         * esto porque depende de cómo termine el intento de loggeo la bandera se pone en false o true;
         * entonces, si es falso ponemos el error en la UI visible. Se puede acceder al valor actualizado en
         * el View Model con el "it", que puede ser renombrado como más abajo.
         */
        usuarioLoggeadoViewModel.loggedState.observe(this) {
            if (!it) {
                binding.errorLogin.isVisible = !it
            }
        }

        /*
         * Se setea el token en el intent que se va a pasar. No sé cómo hacerlo de otra manera todavía, pero tiene que haber
         * una manera más efectiva con el view model. Solo que nunca logré sacar el dato que está posteado en el View Model.
         */
        usuarioLoggeadoViewModel.token.observe(this) {
            intent.putExtra("Token", it)
        }

        /*
         * Se setea el usuario en el intent que se va a pasar. No sé cómo hacerlo de otra manera todavía, pero tiene que haber
         * una manera más efectiva con el view model. Solo que nunca logré sacar el dato que está posteado en el View Model. Véase
         * que se renombra el "it" a "usuario" y luego se pone un ->. Solo así logré sacar el dato; a no ser que implementemos un state
         * custom con una clase ahí bien perrona que sea singletón.
         */
        usuarioLoggeadoViewModel.usuario.observe(this) { usuario ->
            intent.putExtra("Usuario", usuario)
        }

        binding.loginButton.setOnClickListener {
            /*
             * La parte más compleja, lo que significa hacer la conexión con el API. Primero se setea el error de la UI a
             * invisible para efectos de retries, porque la petición dura un ratito. Luego se empieza un Corrutina, que viene siendo
             * un hilo alterno porque correr algo asíncrono en el hilo de la UI hace que se quede pegada y de esta manera no serviría.
             * Esto de Corrutinas es otra dependencia que se agrega en el gradle.app. Dispatchers es un elemento de Corrutinas y no tengo
             * mucho que decir, solo tener cuidado al exportarlo porque hay varios y tiene que ser el de Corrutinas.
             */
            binding.errorLogin.isVisible = false

            CoroutineScope(Dispatchers.IO).launch {
                /*
                 * Luego se llama al método del view model para intentar hacer el login. Es una cadena de llamadas un poco XD
                 * pero recomiendo seguir la cadena para que entienda mejor, o bueno, al menos eso es lo que intento. Se le pasan
                 * los parámetros para realizar el intento. Dentro de este método se van a actualizar los valores del view model
                 * dependiendo del resultado del intento, lo que va a hacer una cosa u otra. Le recomiendo ir al método (a ver t odo el viewModel)
                 *  y luego volver a aquí para entender lo máximo.
                 */
                usuarioLoggeadoViewModel.login(binding.usuarioALoggear.text.toString(), binding.passALoggear.text.toString())

                /*
                 * Al estar ejecutándose en un hilo aparte, las cosas relacionadas a la UI no van a servir si se ejecutan en el
                 * hilo aparte. Por eso mismo, se usa el "runOnUiThread" para ejecutar tasks en el hilo del UI. En este caso, si el
                 * error de la vista no está visible significa que t odo está correcto y no se puso la bandera en false, entonces no está
                 * visible el error en la UI. Nótese que al principio del setOnClick se setea el error a invisible porque si no nunca
                 * pasaría nada xdxdxd. Y si se da que no está visible, iniciamos la siguiente actividad que por el momento es algo X,
                 * pero debería ser el navDraw.
                 */
                runOnUiThread {
                    if(!binding.errorLogin.isVisible) {
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    }
}