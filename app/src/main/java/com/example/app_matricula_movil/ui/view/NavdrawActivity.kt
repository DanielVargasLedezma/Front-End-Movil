package com.example.app_matricula_movil.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.app_matricula_movil.R
import com.example.app_matricula_movil.data.models.Usuario
import com.example.app_matricula_movil.data.repository.UsuarioRepository
import com.example.app_matricula_movil.databinding.ActivityNavdrawBinding
import com.example.app_matricula_movil.ui.view.fragment.carreras.CarrerasFragment
import com.example.app_matricula_movil.ui.view.fragment.alumnos.AlumnosFragment
import com.example.app_matricula_movil.ui.view.fragment.ciclos.CiclosFragment
import com.example.app_matricula_movil.ui.view.fragment.cursos.CursosFragment
import com.example.app_matricula_movil.ui.view.fragment.grupos.GruposFragment
import com.example.app_matricula_movil.ui.view.fragment.matricula.MatriculasFragment
import com.example.app_matricula_movil.ui.view.fragment.ofertaAcademica.OfertaAcademicaFragment
import com.example.app_matricula_movil.ui.view.fragment.profesores.ProfesoresFragment
import com.example.app_matricula_movil.ui.view.fragment.usuarios.UsuariosFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavdrawActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityNavdrawBinding

    /*
     * Variable que viene siendo lo que abre la navdraw desde el toolbar. Es lo de alguna manera permite funcionar la nav,
     * hay dudas porque malfunciona un poco.
     */
    private lateinit var toggle: ActionBarDrawerToggle

    /*
     * Usuario que se pretende setear con lo que se manda desde el Login. No tiene mucha utilidad por el momento, quizá
     * para la hora de crear aplicaciones del job para linkearlo a dicho usuario.
     */
    var userLogged: Usuario? = null

    private val usuarioRepository = UsuarioRepository()

    var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLogged = intent.getSerializableExtra("Usuario") as Usuario
        token = intent.getStringExtra("Token")

        binding.apply {
            setSupportActionBar(appBarNavdraw.toolbar)

            supportActionBar?.title = ""
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            /*
             * Inicialización del toggle, técnicamente lo que hace es que recibe la actividad, la navdraw como tal y textos
             * como un alt en caso de que no carguen las imagenes.
             */
            toggle = ActionBarDrawerToggle(
                this@NavdrawActivity, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )

            /*
             * Le añade al navdraw el listener del toggle, lo que abre o cierra el navdraw. Lo sincroniza por alguna razón
             * y sin eso no funca.
             */
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            /*
             * Le añade el listener a la navdraw para que haga una cosa u otra cuando se seleccione cada item de la nav.
             */
            navView.setNavigationItemSelectedListener(this@NavdrawActivity)

            when (userLogged!!.tipo_usuario) {
                1 -> {
                    navView.menu.getItem(7).isVisible = false
                    navView.menu.getItem(8).isVisible = false
                    navView.menu.getItem(9).isVisible = false
                }
                2 -> {
                    hideZeroToSix()
                    navView.menu.getItem(8).isVisible = false
                    navView.menu.getItem(9).isVisible = false
                }
                3 -> {
                    hideZeroToSix()
                    navView.menu.getItem(7).isVisible = false
                    navView.menu.getItem(9).isVisible = false
                }
                4 -> {
                    hideZeroToSix()
                    navView.menu.getItem(7).isVisible = false
                    navView.menu.getItem(8).isVisible = false
                }
            }
        }
    }

    private fun hideZeroToSix() {
        binding.apply {
            navView.menu.getItem(0).isVisible = false
            navView.menu.getItem(1).isVisible = false
            navView.menu.getItem(2).isVisible = false
            navView.menu.getItem(3).isVisible = false
            navView.menu.getItem(4).isVisible = false
            navView.menu.getItem(5).isVisible = false
            navView.menu.getItem(6).isVisible = false
        }
    }

    /*
     * Método que reemplaza los fragments dentro del main body de la actividad que tiene el navdraw. Aquí solo se realiza
     * la acción que reemplaza un framelayout por el fragment. El método se usa donde se escucha cuando se clickea un item
     */
    private fun replaceFragments(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.appBarNavdraw.contentMain.id, fragment)
        fragmentTransaction.commit()
    }

    /*
     * Ni idea qué hace la verdad.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navdraw, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        /*
         * Hace que se resalte el item seleccionado, debe de estar dentro de un grupo para que solo se seleccione un item
         * a la vez.
         */
        item.isChecked = true

        /*
         * Cierra la navdraw para enseñar el contenido principal.
         */
        binding.drawerLayout.closeDrawers()

        /*
         * Según el id del item clickeado hace el cambio respectivo, faltaría crear los fragments nuevos para el job y
         * para el recycler view. Luego de eso se podría ver para consultar uno.
         */
        when (item.itemId) {
            /*
             * Según si es inicio, reemplaza con una nueva instancia que recibe los parámeros para meterlos en el bundle.
             * Se pueden pasar serializables como el jobForm en cuestión para hacerle display en caso de que sea un usuario
             * estándar y ya haya registrado uno, esto para hacer el editar.
             */
            R.id.usuarios -> {
                supportActionBar?.title = "Usuarios Registrados"
                replaceFragments(
                    UsuariosFragment.newInstance(
                        token!!,
                        userLogged!!
                    )
                )
            }
            R.id.carreras -> {
                supportActionBar?.title = "Carreras Registradas"
                replaceFragments(
                    CarrerasFragment.newInstance()
                )
            }
            R.id.cursos -> {
                supportActionBar?.title = "Cursos Registrados"
                replaceFragments(
                    CursosFragment.newInstance()
                )
            }
            R.id.profesores ->{
                supportActionBar?.title = "Profesores Registrados"
                replaceFragments(
                    ProfesoresFragment.newInstance()
                )
            }
            R.id.alumnos -> {
                supportActionBar?.title = "Alumnos Registrados"
                replaceFragments(
                    AlumnosFragment.newInstance(
                        token!!, userLogged!!
                    )
                )
            }
            R.id.ciclos -> {
                supportActionBar?.title = "Ciclos Registrados"
                replaceFragments(
                    CiclosFragment.newInstance( )
                )
            }
            R.id.oferta_academica -> {
                supportActionBar?.title = "Oferta Académica"
                replaceFragments(
                    OfertaAcademicaFragment.newInstance(
                        userLogged!!,
                        token!!
                    )
                )
            }
            R.id.matricula -> {
                supportActionBar?.title = "Alumnos Registrados"
                replaceFragments(
                    AlumnosFragment.newInstance(
                        token!!, userLogged!!, "GruposMatriculadosAlumno"
                    )
                )
            }
            R.id.grupos_asignados -> {
                supportActionBar?.title = "Grupos Asignados"
                replaceFragments(
                    GruposFragment.newInstance(null, "GruposAsignados")
                )
            }
            R.id.historial_matricula -> {
                supportActionBar?.title = "Historial Académico"
                replaceFragments(
                    MatriculasFragment.newInstance()
                )
            }
            R.id.logout -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = usuarioRepository.logout(token!!)

                    if (response) {
                        runOnUiThread {
                            userLogged = null

                            finish()
                            startActivity(Intent(this@NavdrawActivity, MainActivity::class.java))
                        }
                    } else {
                        runOnUiThread {
                            userLogged = null

                            finish()
                            startActivity(Intent(this@NavdrawActivity, MainActivity::class.java))
                        }
                    }
                }
            }
        }

        return true
    }
}